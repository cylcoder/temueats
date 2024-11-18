package com.sparta.temueats.user.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.dto.CreateUserRequestDto;
import com.sparta.temueats.user.dto.UpdateMypageRequestDto;
import com.sparta.temueats.user.dto.UpdateRoleRequestDto;
import com.sparta.temueats.user.service.KakaoService;
import com.sparta.temueats.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Tag(name="유저 로그인/회원가입/조회/수정")
@RestController
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    public UserController(UserService userService, KakaoService kakaoService) {
        this.userService = userService;
        this.kakaoService = kakaoService;
    }


    @Operation(summary = "회원가입")
    @PostMapping("/auth/signup")
    public ResponseDto createUser(@RequestBody CreateUserRequestDto request) {

        return userService.createUser(request);
    }


    @Operation(summary = "마이페이지 조회")
    @GetMapping("/mypage")
    public ResponseDto getMypage() {
        return userService.getMypage();
    }

    @Operation(summary = "개인 정보 수정")
    @PutMapping("/mypage")
    public ResponseDto updateMypage(@RequestBody UpdateMypageRequestDto request) {

        return userService.updateMypage(request);
    }

    @Operation(summary = "권한 수정")
    @PutMapping("/manage")
    public ResponseDto updateRole(@RequestBody UpdateRoleRequestDto request) {

        return userService.updateRole(request);
    }

    @Operation(summary = "카카오 로그인 요청")
    @GetMapping("/auth/kakao-login")
    public ResponseEntity<ResponseDto> kakaoLogin(@RequestParam String code) throws ParseException {
        System.out.println("kakaoLogin: " + code);
        return kakaoService.processKakaoLogin(code);
    }

}

