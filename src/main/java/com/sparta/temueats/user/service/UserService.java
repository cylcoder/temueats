package com.sparta.temueats.user.service;


import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.dto.CreateUserRequest;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.repository.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {


    private final UserRepository userRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();  // GeometryFactory 인스턴스 생성


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        P_user user = P_user.builder()
                .email(request.getEmail())
                .password(request.getPassword())
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
}
