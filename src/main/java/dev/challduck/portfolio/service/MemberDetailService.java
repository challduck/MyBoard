package dev.challduck.portfolio.service;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.repository.MemberRepository;
import dev.challduck.portfolio.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;


    // 사용자 이름(email)로 사용자의 정보를 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email : "+ email));

//        return memberRepository.findByEmail(email)
//                .orElseThrow(()-> new UsernameNotFoundException("User not found with email : " + email));
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRoles().stream().map(Role::getKey).toArray(String[]::new))
                .build();
    }
}
