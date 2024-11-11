package com.sparta.temueats.menu.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.menu.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/request")
    public ResponseDto<String> request(@RequestBody String request) {
        if (request == null || request.trim().isEmpty()) {
            throw new CustomApiException("요청 메시지 누락");
        }

        System.out.println("request! = " + request);
        return new ResponseDto<>(1, "요청 성공", aiService.request(request));
    }

}
