package com.sparta.temueats.menu.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.service.AiService;
import com.sparta.temueats.store.util.AuthUtils;
import com.sparta.temueats.store.util.AuthUtils.AuthStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.sparta.temueats.store.util.AuthUtils.AuthStatus.*;
import static com.sparta.temueats.user.entity.UserRoleEnum.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final AuthUtils authUtils;

    @PostMapping("/request")
    public ResponseDto<String> request(@RequestBody Map<String, String> aiReqMap) {
        AuthStatus authStatus = authUtils.validate(List.of(OWNER, MANAGER, MASTER));
        if (!authStatus.equals(AUTHORIZED)) {
            return new ResponseDto<>(ResponseDto.FAILURE, authStatus.getMsg());
        }

        return aiService.request(aiReqMap);
    }

}
