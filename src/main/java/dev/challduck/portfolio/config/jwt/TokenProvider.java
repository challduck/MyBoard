package dev.challduck.portfolio.config.jwt;

import dev.challduck.portfolio.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(Member member, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member);
    }

    public String makeToken(Date expiry, Member member) {
        Date now = new Date();

        Collection<? extends GrantedAuthority> authorities = member.getAuthorities();
        List<String> authorityStrings = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ : JWT
                // 내용 iss : properties에서 설정한 issure 값
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now) // 내용 iat : 현재 시간
                .setExpiration(expiry) // 내용 exp : expiry 멤버 변수값
                .setSubject(member.getEmail()) // 내용 sub : 유저의 이메일
                .claim("id", member.getId()) // 클레임 id : 유저 ID
                // 서명 : seceretKey와 함께 HS256 알고리즘으로 암호화
                .claim("authorities", authorityStrings)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // seceretKey로 복호화
                    .parseClaimsJws(token);
            return true;
        }
        catch (Exception e){ // 복호화 과정에서 에러 발생시 유효하지 않은 토큰임
            return false;
        }
    }

    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(claims);

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(),"",authorities), token, authorities);
    }

    // claim에 등록된 authorities 가져오기
    private Collection<? extends GrantedAuthority> extractAuthorities (Claims claims){
        List<String> authorities = claims.get("authorities", List.class);
        if(authorities == null){
            return Collections.emptyList();
        }
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

    // token 기반으로 member id를 가져오는 메서드
    public Long getUserId (String token){
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser() // claim 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
