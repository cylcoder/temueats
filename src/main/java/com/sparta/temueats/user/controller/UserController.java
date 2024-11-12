package com.sparta.temueats.user.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.dto.CreateUserRequestDto;
import com.sparta.temueats.user.dto.LoginRequestDto;
import com.sparta.temueats.user.dto.UpdateMypageRequestDto;
import com.sparta.temueats.user.dto.UpdateRoleRequestDto;
import com.sparta.temueats.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 회원가입
    @PostMapping("/auth/signup")
    public ResponseDto createUser(@RequestBody CreateUserRequestDto request) {

        return userService.createUser(request);
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseDto login(@RequestBody LoginRequestDto request, HttpServletResponse res) {

        return userService.login(request, res);
    }

    // 로그아웃
    @PostMapping("/auth/logout")
    public ResponseDto logout(HttpServletResponse res) {
        return userService.logout(res);
    }

    // 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseDto getMypage(HttpServletRequest req) {
        return userService.getMypage(req);
    }

    // 개인 정보 수정
    @PutMapping("/mypage")
    public ResponseDto updateMypage(@RequestBody UpdateMypageRequestDto request, HttpServletRequest req) {

        return userService.updateMypage(request, req);
    }

    // 권한 수정
    @PutMapping("/manage")
    public ResponseDto updateRole(@RequestBody UpdateRoleRequestDto request, HttpServletRequest req) {

        return userService.updateRole(request, req);
    }

}
