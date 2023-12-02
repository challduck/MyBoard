package dev.challduck.portfolio.config.jwt;

import dev.challduck.portfolio.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(Member member, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member);
    }

    public String makeToken(Date expiry, Member member) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ : JWT
                // 내용 iss : properties에서 설정한 issure 값
                .setIssuer(jwtProperties.getIssure())
                .setIssuedAt(now) // 내용 iat : 현재 시간
                .setExpiration(expiry) // 내용 exp : expiry 멤버 변수값
                .setSubject(member.getEmail()) // 내용 sub : 유저의 이메일
                .claim("id", member.getMemberId()) // 클레임 id : 유저 ID
                // 서명 : seceretKey와 함께 HS256 알고리즘으로 암호화
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

    // token 기반으로 인증 정보를 가져오는 메서드
    /*
    * token을 받아 인증 정보를 담은 객체 Authentication을 받는 메서드이다.
    * properties 파일에 저장한 secret 값으로 토큰을 복호화한 뒤
    * claim을 가져오는 private 메서드인 getClaims()를 호출해서
    * claim 정보를 반환받아 사용자 이메일이 들어있는 token 제목 sub와 token기반으로 인증 정보를 생성한다.
    * 이떄 UsernamePassword~~~~ 의 첫 인자로 들어가는 User는 프로젝트에서 만든 User가 아니라 spring security에서 제공하는 객체인
    * User 클래스를 import 해야한다.
    *
    */
    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),"",authorities), token, authorities);
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
