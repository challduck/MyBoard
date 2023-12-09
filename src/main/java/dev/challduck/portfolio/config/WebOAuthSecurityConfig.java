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
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    private final UserDetailsService userDetailsService;

    @Bean
    public WebSecurityCustomizer configure(){
        return (web)->web.ignoring()
                .requestMatchers(toH2Console())
//                .requestMatchers(antMatcher("/static/**"));
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
                        // TODO: Project Build 할 때 인가 활성화 하기
//                        .requestMatchers(antMatcher("/api/token"),antMatcher("/api/user/signin"),antMatcher("/api/user/signup"))
//                        .permitAll()
//                        .requestMatchers(antMatcher("/api/articles/**"))
//                        .authenticated()
                        .anyRequest()
                        .permitAll());

        http
                .oauth2Login((login)->login.loginPage("/signin")
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
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserDetailsService userDetailsService) throws Exception {

        return http
                .authenticationProvider(authenticationProvider(bCryptPasswordEncoder, userDetailsService))
                .getSharedObject(AuthenticationManager.class);
    }

    private DaoAuthenticationProvider authenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder,UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
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
