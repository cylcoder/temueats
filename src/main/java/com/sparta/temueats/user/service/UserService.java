package com.sparta.temueats.user.service;


import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.security.UserDetailsImpl;
import com.sparta.temueats.user.util.JwtUtil;
import com.sparta.temueats.user.dto.*;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Slf4j
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

    public ResponseDto getMypage(UserDetailsImpl userDetails) {

        P_user user = userDetails.getUser();

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

    public P_user findUserById(Long owner) {
        return userRepository.findById(owner).orElse(null);
    }

    public P_user findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public ResponseDto updateRole(UpdateRoleRequestDto request, HttpServletRequest req) {

        // 요청자 검증
        P_user user = validateTokenAndGetUser(req).orElse(null);
        if (user == null) {
            return new ResponseDto<>(-1, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다", null);
        }

        // 요청자권한 검증
        if (user.getRole().equals(UserRoleEnum.CUSTOMER) || user.getRole().equals(UserRoleEnum.OWNER)) {
            return new ResponseDto<>(-1, "권한이 없는 사용자입니다", null);
        }

        // 수신자 검증
        P_user targetUser = findUserById(request.getId());
        if (targetUser == null) {
            return new ResponseDto<>(-1, "존재하지 않는 사용자입니다", null);
        }
        // 권한 부여
        targetUser.setRole(request.getRole());
        userRepository.save(targetUser);
        return new ResponseDto<>(1, "권한 변경 완료", null);
    }

    public Optional<P_user> validateTokenAndGetUser(HttpServletRequest req) {

        String token = jwtUtil.getTokenFromCookies(req);
        token = jwtUtil.substringToken(token);
        // 토큰 값 검증
        if (!jwtUtil.validateToken(token)) {
            log.error("유효하지 않은 토큰");
            return Optional.empty();
        }

        // 토큰이 유효하지 않으면 Optional.empty() 반환
        if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
            log.error("유효하지 않은 토큰");
            return Optional.empty();
        }

        try {
            // 사용자 ID 추출
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            Long userId = Long.parseLong(claims.getSubject());


            // 사용자 조회 및 반환
            return userRepository.findById(userId);
        } catch (NumberFormatException e) {
            log.error("잘못된 사용자 ID", e);
            return Optional.empty();
        } catch (Exception e) {
            log.error("사용자 검증 중 오류 발생", e);
            return Optional.empty();
        }
    }


}
