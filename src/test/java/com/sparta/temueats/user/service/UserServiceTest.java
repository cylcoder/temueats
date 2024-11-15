package com.sparta.temueats.user.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.dto.CreateUserRequestDto;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void createUserTest_EmailDuplicate() {
        // Given
        CreateUserRequestDto request = createRequest("test@test.com", "password", "nickname", null);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new P_user()));

        // When
        ResponseDto response = userService.createUser(request);

        // Then
        assertEquals(-1, response.getCode());
        assertEquals("중복된 이메일입니다", response.getMsg());
    }

    @Test
    void createUserTest_NicknameDuplicate() {
        // Given
        CreateUserRequestDto request = createRequest("test@test.com", "password", "nickname", null);

        // Mocking: 이메일은 중복되지 않음
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Mocking: 닉네임이 중복되는 상황 시뮬레이션
        when(userRepository.findByNickname(request.getNickname())).thenReturn(Optional.of(new P_user()));

        // When
        ResponseDto response = userService.createUser(request);

        // Then
        assertEquals(-1, response.getCode());
        assertEquals("중복된 닉네임입니다", response.getMsg());
    }

    @Test
    void createUserTest_KakaoSignupSuccess() {
        // Given
        String encodedPassword = "encodedRandomPassword";
        CreateUserRequestDto request = createRequest("test@test.com", null, "kakaoUser", 123456789L);

        // Mocking: 이메일과 닉네임 중복 없음, 비밀번호는 존재여부 확인
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(request.getNickname())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        // When
        ResponseDto response = userService.createUser(request);

        // Then
        assertEquals(1, response.getCode());
        assertEquals("회원가입이 완료되었습니다", response.getMsg());

        // Verify: 저장된 사용자 정보 확인
        verify(userRepository).save(argThat(user ->
                user.getEmail().equals("test@test.com") &&
                        user.getNickname().equals("kakaoUser") &&
                        user.getKakaoId().equals(123456789L) &&
                        user.getSocialProvider().equals("KAKAO") &&
                        user.getPassword().equals(encodedPassword)
        ));
    }

    @Test
    void createUser_WhenPasswordIsNullForGeneralUser() {
        // Given
        CreateUserRequestDto request = createRequest("test@test.com", null, "nickname", null);

        // Mocking: 이메일과 닉네임 중복 없음
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(request.getNickname())).thenReturn(Optional.empty());

        // When
        ResponseDto response = userService.createUser(request);

        // Then
        assertEquals(-1, response.getCode());
        assertEquals("비밀번호가 없습니다", response.getMsg());
    }

    @Test
    void createUserTest_Success() {
        // Given
        CreateUserRequestDto request = createRequest("test@test.com", "password", "nickname", null);

        // Mocking: 이메일과 닉네임 중복 없음
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(request.getNickname())).thenReturn(Optional.empty());

        // Mocking: 비밀번호 암호화
        String password = "password";
        when(passwordEncoder.encode("password")).thenReturn(password);

        // When
        ResponseDto response = userService.createUser(request);

        // Then
        assertEquals(1, response.getCode());
        assertEquals("회원가입이 완료되었습니다", response.getMsg());

        // Verify: 저장된 사용자 정보 확인
        verify(passwordEncoder).encode("password"); // 일반 유저 비밀번호 인코딩 검증
        verify(userRepository).save(argThat(user ->
                user.getEmail().equals("test@test.com") &&
                        user.getNickname().equals("nickname") &&
                        user.getKakaoId() == null &&
                        user.getSocialProvider().equals("NONE") &&
                        user.getPassword().equals(password) // 암호화된 비밀번호 저장 확인
        ));
    }

    private CreateUserRequestDto createRequest(String email, String password, String nickname, Long kakaoId) {
        return CreateUserRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .phone("010-1234-5678")
                .birth(LocalDate.parse("1990-01-01"))
                .imageProfile("profile.png")
                .lat(37.5665f)
                .lng(126.9780f)
                .address("상세주소")
                .kakaoId(kakaoId)
                .build();
    }
}