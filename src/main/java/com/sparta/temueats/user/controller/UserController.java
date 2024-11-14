package com.sparta.temueats.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.dto.CreateUserRequestDto;
import com.sparta.temueats.user.dto.UpdateMypageRequestDto;
import com.sparta.temueats.user.dto.UpdateRoleRequestDto;
import com.sparta.temueats.user.service.KakaoService;
import com.sparta.temueats.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    public UserController(UserService userService, KakaoService kakaoService) {
        this.userService = userService;
        this.kakaoService = kakaoService;
    }


    // 회원가입
    @PostMapping("/auth/signup")
    public ResponseDto createUser(@RequestBody CreateUserRequestDto request) {

        return userService.createUser(request);
    }


    // 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseDto getMypage() {
        return userService.getMypage();
    }

    // 개인 정보 수정
    @PutMapping("/mypage")
    public ResponseDto updateMypage(@RequestBody UpdateMypageRequestDto request) {

        return userService.updateMypage(request);
    }

    // 권한 수정
    @PutMapping("/manage")
    public ResponseDto updateRole(@RequestBody UpdateRoleRequestDto request) {

        return userService.updateRole(request);
    }

    // 카카오 로그인 요청
    @GetMapping("/auth/kakao-login")
    public ResponseEntity<ResponseDto> kakaoLogin(@RequestParam String code) throws ParseException {
        System.out.println("kakaoLogin: " + code);
        return kakaoService.processKakaoLogin(code);
    }

}

