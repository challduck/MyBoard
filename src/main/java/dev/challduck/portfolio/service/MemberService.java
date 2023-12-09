package dev.challduck.portfolio.service;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.dto.member.AddMemberRequest;
import dev.challduck.portfolio.dto.member.SignInMemberRequest;
import dev.challduck.portfolio.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest request;

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
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return memberRepository.save(
                Member.builder()
                        .email(dto.getEmail())
                        .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                        .nickname(dto.getNickname())
                        .build());
    }

    // 로그인
    public Member signIn(SignInMemberRequest dto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
//                .orElseThrow(()->new UserNotFoundException("존재하지않는 회원입니다."));
        if (optionalMember.isEmpty()) { return null; }

        // optional 사용했으니 데이터를 가져와보자.
        Member member = optionalMember.get();
        // 일치하지 않으면 null 반환
        if(!bCryptPasswordEncoder.matches(dto.getPassword(), member.getPassword())){ return null; }

        // LoadBalancer 를 구성하지 않았다면 request.getRemoteAddr 메서드로 가져온다.
        updateMemberIp(optionalMember.get().getEmail(), request.getRemoteAddr());
        // loadBalancer 를 구성하였다면 getClientIp 메서드를 사용하여 가져온다.
        // Http Header는 사용자가 수정하여 요청하는 것이 가능하다.
        // 즉, Http Header를 신뢰할 수 있는 경우(Load Balancer를 구성했을 때) Header의 값을 가져올 수 있다.
        return member;
    }

    // 로그인 사용자의 Ip주소를 가져오는 메서드
//    private String getClientIp(){
//        String xForwardedFor = request.getHeader("X-Forwarded-For");
//        if(xForwardedFor == null || xForwardedFor.isEmpty()){
//            return request.getRemoteAddr();
//        } else {
//            return xForwardedFor.split(",")[0].trim();
//        }
//    }
    // 마지막 접속 ip 저장 메서드
    private void updateMemberIp(String email, String ip){
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        optionalMember.ifPresent(member -> {
            member.setLastIpAddress(ip);
            memberRepository.save(member);
        });
    }

    //
    public Member getMemberById(Long memberId){
        if (memberId == null) return null;
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElse(null);
    }

    public Member getMemberByEmail(String email){
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected user"));
    }

    public Member findByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected member"));
    }

}
