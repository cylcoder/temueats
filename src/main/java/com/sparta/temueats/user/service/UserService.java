package com.sparta.temueats.user.service;


import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.Util.JwtUtil;
import com.sparta.temueats.user.dto.*;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final GeometryFactory geometryFactory = new GeometryFactory();  // GeometryFactory 인스턴스 생성

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseDto createUser(CreateUserRequestDto request) {

        Point latLngPoint = geometryFactory.createPoint(new Coordinate(request.getLng(), request.getLat()));

        // 이메일 중복확인
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ResponseDto<>(-1, "중복된 이메일입니다", null);
        }
        // 닉네임 중복확인
        if(userRepository.findByNickname(request.getNickname()).isPresent()) {
            return new ResponseDto<>(-1, "중복된 닉네임입니다", null);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        P_user user = P_user.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .birth(request.getBirth())
                .use_yn(true)
                .role(UserRoleEnum.CUSTOMER)
                .imageProfile(request.getImageProfile())
                .latLng(latLngPoint)
                .address(request.getAddress())
                .build();

        userRepository.save(user);
        return new ResponseDto<>(1, "회원가입이 완료되었습니다", null);

    }

    public ResponseDto login(LoginRequestDto request, HttpServletResponse res) {
        String email = request.getEmail();
        String password = request.getPassword();

        // 존재하는 유저인지 확인
        Optional<P_user> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            logger.error("존재하지 않는 사용자");
            return new ResponseDto<>(-1, "존재하지 않는 사용자입니다", null);
        }

        P_user user = userOptional.get();

        // 비밀번호 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.error("비밀번호 불일치");
            return new ResponseDto<>(-1, "비밀번호가 일치하지 않습니다", null);
        }

        // JWT 생성, 쿠키 저장
        String token = jwtUtil.createToken(user.getId().toString(), user.getRole());
        logger.info("token : {}", token);
        jwtUtil.addJwtToCookie(token, res);

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole().toString())
                .build();

        return new ResponseDto<>(1, "로그인이 성공적으로 완료되었습니다", responseDto);
    }

    public ResponseDto logout(HttpServletResponse res) {

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return new ResponseDto<>(1, "로그아웃이 완료되었습니다", null);
    }

    public ResponseDto getMypage(HttpServletRequest req) {

        P_user user = validateTokenAndGetUser(req).orElse(null);
        if (user == null) {
            return new ResponseDto<>(-1, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다", null);
        }

        MypageResponseDto response = MypageResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .birth(user.getBirth())
                .imageProfile(user.getImageProfile())
                .address(user.getAddress())
                .lat(user.getLatLng().getY())
                .lng(user.getLatLng().getX())
                .build();

        return new ResponseDto<>(1, null, response);
    }

    public ResponseDto updateMypage(UpdateMypageRequestDto request, HttpServletRequest req) {

        P_user user = validateTokenAndGetUser(req).orElse(null);
        if (user == null) {
            return new ResponseDto<>(-1, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다", null);
        }
        // 닉네임 중복확인
        if(userRepository.findByNickname(request.getNickname()).isPresent()) {
            return new ResponseDto<>(-1, "중복된 닉네임입니다", null);
        }

        user.updateUserInfo(request, geometryFactory);
        userRepository.save(user);

        MypageResponseDto response = MypageResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .birth(user.getBirth())
                .role(user.getRole().toString())
                .imageProfile(user.getImageProfile())
                .address(user.getAddress())
                .lat(user.getLatLng().getY())
                .lng(user.getLatLng().getX())
                .build();

        return new ResponseDto<>(1, "성공적으로 수정되었습니다", response);

    }

    public Optional<P_user> validateTokenAndGetUser(HttpServletRequest req) {

        String token = jwtUtil.getTokenFromCookies(req);
        token = jwtUtil.substringToken(token);
        // 토큰 값 검증
        if (!jwtUtil.validateToken(token)) {
            logger.error("유효하지 않은 토큰");
            return Optional.empty();
        }

        // 토큰이 유효하지 않으면 Optional.empty() 반환
        if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
            logger.error("유효하지 않은 토큰");
            return Optional.empty();
        }

        try {
            // 사용자 ID 추출
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            logger.info("사용자 ID: {}", claims.getSubject());
            Long userId = Long.parseLong(claims.getSubject());


            // 사용자 조회 및 반환
            return userRepository.findById(userId);
        } catch (NumberFormatException e) {
            logger.error("잘못된 사용자 ID", e);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("사용자 검증 중 오류 발생", e);
            return Optional.empty();
        }
    }
}
