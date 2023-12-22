package dev.challduck.portfolio.config;

import dev.challduck.portfolio.config.jwt.TokenProvider;
import dev.challduck.portfolio.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import dev.challduck.portfolio.config.oauth.OAuth2SuccessHandler;
import dev.challduck.portfolio.config.oauth.OAuth2UserCustomService;
import dev.challduck.portfolio.repository.RefreshTokenRepository;
import dev.challduck.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    @Bean
    public WebSecurityCustomizer configure(){
        return (web)->web.ignoring()
//                .requestMatchers(toH2Console())
                .requestMatchers(antMatcher("/img/**"),antMatcher("/css/**"),antMatcher("/js/**"));
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrf)->csrf.disable())
                .httpBasic((basic)->basic.disable())
                .formLogin((login)->login.disable())
                .logout((logout)->logout.logoutSuccessUrl("/login").invalidateHttpSession(true)) // 로그아웃 이후 세션을 전체 삭제할것인지 여부
//                    .formLogin((login)->login.loginPage("/login").defaultSuccessUrl("/articles"))
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeHttpRequests((request)->request // 인증, 인가 설정
                        .requestMatchers(antMatcher("/api/user/my-page"),antMatcher("/api/user/new-nickname"))
                            .hasAnyAuthority("ROLE_OAUTH2_MEMBER","ROLE_MEMBER", "ROLE_ADMIN")
                        .requestMatchers(antMatcher("/api/user/new-password"))
                            .hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN")
                        .requestMatchers(antMatcher("/api/token"),antMatcher("/api/user/login"),antMatcher("/api/user/signup"))
                        .permitAll()
                        .anyRequest()
                        .permitAll());

        http
                .oauth2Login((login)->login
                    .loginPage("/signup")
                    .authorizationEndpoint(endPoint -> endPoint
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                    .successHandler(oAuth2SuccessHandler())
                    .userInfoEndpoint((endPoint)->endPoint.userService(oAuth2UserCustomService)));

        http
                .exceptionHandling((handling)->handling
                    .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**")));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            BCryptPasswordEncoder bCryptPasswordEncoder) {

        return http
                .authenticationProvider(authenticationProvider(bCryptPasswordEncoder))
                .getSharedObject(AuthenticationManager.class);
    }

    private DaoAuthenticationProvider authenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(){
        return new OAuth2SuccessHandler(tokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(),memberService);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository(){
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
