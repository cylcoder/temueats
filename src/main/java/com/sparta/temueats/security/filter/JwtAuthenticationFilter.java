package com.sparta.temueats.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.security.UserDetailsImpl;
import com.sparta.temueats.security.util.JwtUtil;
import com.sparta.temueats.user.dto.LoginRequestDto;
import com.sparta.temueats.user.entity.UserRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/members/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            logger.error("Error parsing LoginRequestDto in attemptAuthentication", e);
            return null;
        } catch (AuthenticationException e) {
            logger.error("Authentication error in attemptAuthentication", e);
            throw e;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getEmail();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createAccessToken(email, role.name());
        String refreshToken = jwtUtil.createRefreshToken(email);

        jwtUtil.addAccessTokenToHeader(accessToken, response);  // 액세스 토큰을 헤더에 추가
        jwtUtil.addRefreshTokenToCookie(refreshToken, response);  // 리프레시 토큰을 쿠키에 추가

        ResponseDto responseDto = new ResponseDto(1, "로그인 성공", null);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String errorMsg;

        if (failed instanceof org.springframework.security.authentication.BadCredentialsException) {
            errorMsg = "이메일이나 비밀번호가 틀렸습니다";
            sendErrorResponse(response, ResponseDto.FAILURE, errorMsg);
        } else {
            errorMsg = "인증 실패";
            sendErrorResponse(response, ResponseDto.FAILURE, errorMsg);
        }

        logger.info("로그인 실패: ");
    }

    private void sendErrorResponse(HttpServletResponse response, Integer code, String msg) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ResponseDto<Object> responseDto = new ResponseDto<>(code, msg);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
    }

}