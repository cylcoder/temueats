package com.sparta.temueats.user.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.dto.CreateUserRequest;
import com.sparta.temueats.user.dto.LoginRequestDto;
import com.sparta.temueats.user.service.UserService;
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
    public ResponseDto<?> createUser(@RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto request, HttpServletResponse res) {

        return userService.login(request, res);
    }

    // 로그아웃
    @PostMapping("/auth/logout")
    public ResponseDto<?> logout(HttpServletResponse res) {
        return userService.logout(res);
    }


}
