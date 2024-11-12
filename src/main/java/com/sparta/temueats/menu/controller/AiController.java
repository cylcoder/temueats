package com.sparta.temueats.menu.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.menu.service.AiService;
import com.sparta.temueats.store.util.UserUtils;
import com.sparta.temueats.user.entity.P_user;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final UserUtils userUtils;

    @PostMapping("/request")
    public ResponseDto<String> request(@RequestBody Map<String, String> requestMap) {
        String request = requestMap.get("request");
        if (request == null || request.trim().isEmpty()) {
            throw new CustomApiException("요청 메시지는 필수입니다.");
        }

        // user will be switched from session later
        P_user user = userUtils.createMockUser();
        return new ResponseDto<>(1, "요청 성공", aiService.request(request, user));
    }

}
