package dev.challduck.portfolio.config.oauth;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.repository.MemberRepository;
import dev.challduck.portfolio.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser (OAuth2UserRequest request) throws OAuth2AuthenticationException {

        String registrationId = request.getClientRegistration().getRegistrationId();
        OAuth2User user = super.loadUser(request);
        log.info("registrationId : {}", registrationId);
        switch (registrationId) {
            case "google":
                handleGoogleUser(user);
                break;
            case "kakao":
                handleKakaoUser(user);
                break;
            case "naver":
                handleNaverUser(user);
                break;
            default:
                throw new IllegalArgumentException("Unsupported registrationId: " + registrationId);
        }
        return user;
    }
    private Member saveOrUpdate(String email, String name) {
        Member member = memberRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(Member.builder()
                        .email(email)
                        .nickname(name)
                        .roles(Collections.singleton(Role.OAUTH2_MEMBER))
                        .build());

        return memberRepository.save(member);
    }

    private void handleGoogleUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        saveOrUpdate(email, name);
    }
    private void handleKakaoUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String name = (String) profile.get("nickname");
        saveOrUpdate(email, name);
    }
    private void handleNaverUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        String email = (String) response.get("email");
        String name = (String) response.get("nickname");
        log.info("naver email : {}", email);
        log.info("naver nickname : {}", name);
        saveOrUpdate(email, name);
    }
}
