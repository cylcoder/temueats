package com.sparta.temueats.user.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.dto.CreateUserRequest;
import com.sparta.temueats.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("")
    public ResponseDto<?> createUser(@RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }



}
