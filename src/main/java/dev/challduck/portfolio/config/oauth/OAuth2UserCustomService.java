package dev.challduck.portfolio.config.oauth;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser (OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(request);
        saveOrUpdate(user);
        return user;
    }

    private Member saveOrUpdate(OAuth2User user) {
        Map<String, Object> attributes = user.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        Member member = memberRepository.findByEmail(email)
                .map((entity)-> entity.update(name))
                .orElse(Member.builder()
                        .email(email)
                        .nickname(name)
                        .build());
        return memberRepository.save(member);
    }
}
