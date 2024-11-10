package com.sparta.temueats.user.service;


import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.Util.JwtUtil;
import com.sparta.temueats.user.dto.CreateUserRequest;
import com.sparta.temueats.user.dto.LoginRequestDto;
import com.sparta.temueats.user.dto.LoginResponseDto;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final GeometryFactory geometryFactory = new GeometryFactory();  // GeometryFactory 인스턴스 생성


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseDto<?> createUser(CreateUserRequest request) {

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

    public ResponseDto<?> login(LoginRequestDto request, HttpServletResponse res) {
        String email = request.getEmail();
        String password = request.getPassword();

        // 존재하는 유저인지 확인
        Optional<P_user> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return new ResponseDto<>(-1, "존재하지 않는 사용자입니다", null);
        }

        P_user user = userOptional.get();

        // 비밀번호 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseDto<>(-1, "비밀번호가 일치하지 않습니다", null);
        }

        // JWT 생성, 쿠키 저장
        String token = jwtUtil.createToken(user.getNickname(), user.getRole());
        jwtUtil.addJwtToCookie(token, res);

        Cookie jwtCookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true); // HttpOnly 설정
        res.addCookie(jwtCookie);

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .nickname(user.getNickname())
                .role(user.getRole().toString())
                .build();

        return new ResponseDto<>(1, "로그인이 성공적으로 완료되었습니다", responseDto);
    }

    public ResponseDto<?> logout(HttpServletResponse res) {

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return new ResponseDto<>(1, "로그아웃이 완료되었습니다", null);
    }
}
