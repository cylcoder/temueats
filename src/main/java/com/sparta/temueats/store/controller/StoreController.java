package com.sparta.temueats.store.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.store.dto.StoreReqDto;
import com.sparta.temueats.store.dto.StoreResDto;
import com.sparta.temueats.store.service.StoreReqService;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.repository.UserRespository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreReqService storeReqService;
    private final UserRespository userRepository;

    @PostMapping("/stores/req")
    public ResponseDto<StoreResDto> saveStoreReq(@Valid StoreReqDto storeReqDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new CustomApiException("가게 등록 요청 실패: " + errorMessages);
        }

        P_user user = createMockUser();
        StoreResDto storeResDto = storeReqService.saveStoreReq(storeReqDto, user);
        return new ResponseDto<>(1, "가게 등록 요청 성공", storeResDto);
    }

    @PostMapping("/stores/{storeReqId}/approve")
    public ResponseDto<Object> approveStoreReq(@PathVariable("storeReqId") UUID storeReqId) {
        P_user user = createMockUser();
        storeReqService.approveStoreReq(storeReqId, user);
        return new ResponseDto<>(1, "가게 등록 완료", null);
    }

    P_user createMockUser() {
        String nickname = "user" + (new Random().nextInt(9) + 1);
        String email = nickname + "@gmail.com";

        P_user user = P_user.builder()
                .email(email)
                .password("password")
                .nickname(nickname)
                .birth(new Date(100, Calendar.JANUARY, 1))
                .use_yn(true)
                .imageProfile("https://s3.com/john.jpg")
                .latLng(new Point(126.978, 37.5665))
                .address("Hotel Casa Amsterdam")
                .build();

        return userRepository.save(user);
    }

}
