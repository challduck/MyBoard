package dev.challduck.portfolio.service;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.dto.AddMemberRequest;
import dev.challduck.portfolio.dto.SignInMemberRequest;
import dev.challduck.portfolio.exception.IncorrectPasswordException;
import dev.challduck.portfolio.exception.UserNotFoundException;
import dev.challduck.portfolio.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 이메일 중복 체크
    public boolean checkEmailDuplicate(String email){
        return memberRepository.existsByEmail(email);
    }
    // 닉네임 중복 체크
    public boolean checkNicknameDuplicate(String nickname){
        return memberRepository.existsByNickname(nickname);
    }

    // 회원가입
    public Member saveMember(AddMemberRequest dto) {
        return memberRepository.save(
                Member.builder()
                        .email(dto.getEmail())
                        .password(bCryptPasswordEncoder.encode(dto.getPassword()))
//                        .password(dto.getPassword())
                        .nickname(dto.getNickname())
                        .build());
    }

    // 로그인
    public Member signIn(SignInMemberRequest dto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
//                .orElseThrow(()->new UserNotFoundException("존재하지않는 회원입니다."));
        if (optionalMember.isEmpty()) {
            return null;
        }
        Member member = optionalMember.get();

        if(!bCryptPasswordEncoder.matches(dto.getPassword(), member.getPassword())){
            // 일치하지 않으면 null 반환
            return null;
        }

        return member;
    }

    //
    public Member getMemberById(Long memberId){
        if (memberId == null) return null;
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElse(null);
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected user"));
    }

}
