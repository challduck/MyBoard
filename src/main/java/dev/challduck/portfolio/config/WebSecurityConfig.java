package dev.challduck.portfolio.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public WebSecurityCustomizer configure(){
        return (web)->web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(
                        antMatcher("/img/**"),
                        antMatcher("/css/**"),
                        antMatcher("/js/**")
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
            http
                .csrf((csrf)->csrf.disable())
                    .httpBasic((basic)->basic.disable())
                    .formLogin((login)->login.disable())
                    .logout((logout)->logout.disable());

            http.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            http
                    .authorizeHttpRequests((request)->request
                            .requestMatchers(antMatcher("/api/token"),antMatcher("/api/user/**"))
                            .permitAll()
                            .requestMatchers(antMatcher("/api/**"))
                            .authenticated()
                            .anyRequest()
                            .permitAll());

            http
                    .logout((logout)->logout.logoutSuccessUrl("/login"));

            http
                    .exceptionHandling((handling)->handling
                    .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**")));

            return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
