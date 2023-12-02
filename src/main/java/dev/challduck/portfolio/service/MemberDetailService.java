package dev.challduck.portfolio.service;

import dev.challduck.portfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Member member = userRepository.findByEmail(email)
//                .orElseThrow(()-> new UsernameNotFoundException("User not found with email : " + email));
//        return new (member);
        return null;
    }
}
