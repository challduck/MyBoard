package dev.challduck.portfolio.config.oauth;

import dev.challduck.portfolio.config.jwt.TokenProvider;
import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.RefreshToken;
import dev.challduck.portfolio.repository.RefreshTokenRepository;
import dev.challduck.portfolio.service.MemberService;
import dev.challduck.portfolio.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final MemberService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        if(authentication instanceof OAuth2AuthenticationToken oauthToken){
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = null;

            log.info("registartionId : {}" , registrationId);

            switch (registrationId) {
                case "google" -> email = (String) oAuth2User.getAttributes().get("email");
                case "kakao" -> {
                    Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
                    email = (String) kakaoAccount.get("email");
                }
                case "naver" -> {
                    Map<String, Object> naverAccount = (Map<String, Object>) oAuth2User.getAttributes().get("response");
                    email = (String) naverAccount.get("email");
                }
            }

            Member member = userService.findByEmail(email);

            String refreshToken= tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
            saveRefreshToken(member, refreshToken);
            addRefreshTokenToCookie(request, response, refreshToken);

            String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
            String targetUrl = getTargetUrl(accessToken);

            clearAuthenticationAttributes(request, response);

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }

    private void saveRefreshToken(Member member, String newRefreshToken){
        RefreshToken refreshToken = refreshTokenRepository.findById(member.getId())
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(member, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken){
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String getTargetUrl(String token){
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
