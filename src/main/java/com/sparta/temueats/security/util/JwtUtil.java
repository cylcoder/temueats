package com.sparta.temueats.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@PropertySource("classpath:application.yml")
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    public static final String BEARER_PREFIX = "Bearer ";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자

    // 토큰 만료시간
    private static final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000L; // 60분
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // 7일

    @Value("${spring.jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 액세스 토큰 생성
    public String createAccessToken(String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(email) // 사용자 식별자
                .claim("role", role) // 권한 정보 추가
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(email) // 사용자 식별자만 포함
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // 액세스 토큰을 헤더에 추가
    public void addAccessTokenToHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
    }

    // 리프레시 토큰을 HttpOnly 쿠키에 추가
    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS에서만 전송되도록 설정
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty");
        }
        return false;
    }

    public String getTokenFromHeader(HttpServletRequest request, String headerName) {
        // 요청에서 헤더 값 가져오기
        String headerValue = request.getHeader(headerName);

        // 헤더 값이 존재하고, "Bearer "로 시작할 경우
        if (StringUtils.hasText(headerValue) && headerValue.startsWith(BEARER_PREFIX)) {
            // "Bearer "를 제거하고 순수 토큰만 반환
            return headerValue.substring(BEARER_PREFIX.length());
        }

        // 토큰이 없거나 형식이 맞지 않을 경우 null 반환
        return null;
    }

    // 토큰을 헤더에 추가
    public void addJwtToHeader(String token, HttpServletResponse response, String headerName) {
        // 헤더에 "Bearer " 접두사와 함께 토큰을 추가
        response.setHeader(headerName, BEARER_PREFIX + token);
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    // 쿠키에서 지정된 이름의 토큰 추출
    public String getTokenFromCookies(HttpServletRequest req, String cookieName) {
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue().replace("%20", " "); // 토큰 값 반환
                }
            }
        }
        return null;
    }

}