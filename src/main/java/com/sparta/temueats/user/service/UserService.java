package com.sparta.temueats.user.service;


import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.security.UserDetailsImpl;
import com.sparta.temueats.user.dto.*;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeometryFactory geometryFactory = new GeometryFactory();  // GeometryFactory 인스턴스 생성



    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public ResponseDto getMypage() {

        P_user user = getUser();

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

    public ResponseDto updateMypage(UpdateMypageRequestDto request) {

        P_user user = getUser();

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

    public ResponseDto updateRole(UpdateRoleRequestDto request) {


        // 요청자 검증
        P_user user = getUser();

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

    // 현재 로그인한 유저 객체 반환
    public P_user getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            String email = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getEmail();
            P_user user = findByEmail(email);
            if (user == null) {
                throw new CustomApiException("해당하는 사용자 없음");
            }
        }
        throw new CustomApiException("인증된 사용자 아님");
    }

    public P_user findUserById(Long owner) {
        return userRepository.findById(owner).orElse(null);
    }

    public P_user findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserRoleEnum findRoleByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null).getRole();
    }


}
