package dev.challduck.portfolio.service;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.MemberLoginLog;
import dev.challduck.portfolio.domain.RefreshToken;
import dev.challduck.portfolio.dto.member.*;
import dev.challduck.portfolio.exception.IncorrectPasswordException;
import dev.challduck.portfolio.exception.InvalidPasswordException;
import dev.challduck.portfolio.exception.UserNotFoundException;
import dev.challduck.portfolio.repository.MemberRepository;
import dev.challduck.portfolio.repository.RefreshTokenRepository;
import dev.challduck.portfolio.util.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MemberService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final MemberLoginLogService memberLoginLogService;

    // 회원가입
    public Member saveMember(AddMemberRequest dto) {
        if(memberRepository.existsByEmail(dto.getEmail())){
            throw new DuplicateKeyException("이미 존재하는 이메일 입니다.");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return memberRepository.save(
                Member.builder()
                        .email(dto.getEmail())
                        .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                        .roles(Collections.singleton(Role.MEMBER))
                        .nickname(dto.getNickname())
                        .build());
    }

    // 로그인
    public Member signIn(SignInMemberRequest dto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(()->new UserNotFoundException("아이디 또는 비밀번호가 일치하지않습니다."));

        // 일치하지 않으면 null 반환
        if(!bCryptPasswordEncoder.matches(dto.getPassword(), member.getPassword())){
            throw new IncorrectPasswordException("아이디 또는 비밀번호가 일치하지않습니다.");
        }
        return member;
    }

    public void changePassword(UpdateMemberPasswordRequest request) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!isOauthUser()){
            Member member = findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            if(bCryptPasswordEncoder.matches(request.getExistingPassword() ,member.getPassword())){
                memberRepository.save(member.updatePassword(request.getNewPassword()));
            }
            else {
                throw new InvalidPasswordException("기존 비밀번호가 일치하지 않습니다.");
            }
        }
        throw new IllegalStateException("올바르지 않은 접근입니다.");
    }
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected member"));
    }
    public Member findByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected member"));
    }
    public void changeNickname(UpdateMemberNicknameRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null){
            throw new UsernameNotFoundException("로그인한 사용자만 닉네임을 변경 할 수 있습니다.");
        }
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("변경할 회원의 정보가 존재하지않습니다."));
        member.update(request.getNewNickname());
        memberRepository.save(member);
    }

    public MemberDataResponse getMyData() {
        String requestMemberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(requestMemberEmail==null){
            throw new UsernameNotFoundException("로그인한 사용자만 회원정보를 조회할 수 있습니다.");
        }
        Member member = memberRepository.findByEmail(requestMemberEmail).orElseThrow(()->
                new IllegalArgumentException("가져올 회원정보가 존재하지않습니다."));
        MemberLoginLog memberLoginLog = memberLoginLogService.getMemberLoginLog(member);
        return new MemberDataResponse(member, isOauthUser(), memberLoginLog);
    }

    public boolean isOauthUser(){
        Collection<? extends GrantedAuthority> authorities =
            SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(Role.OAUTH2_MEMBER.getKey())) {
                return true;
            }
        }
        return false;
    }

    public void logout() {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(()-> new UsernameNotFoundException("등록된 사용자가 아닙니다."));
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("Refresh Token을 찾을 수 없습니다."));
        refreshToken.updateLogoutStatus();
        refreshTokenRepository.save(refreshToken);
    }
}
