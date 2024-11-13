package com.sparta.temueats.user.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.security.UserDetailsImpl;
import com.sparta.temueats.user.dto.CreateUserRequestDto;
import com.sparta.temueats.user.dto.UpdateMypageRequestDto;
import com.sparta.temueats.user.dto.UpdateRoleRequestDto;
import com.sparta.temueats.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

}
