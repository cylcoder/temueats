package com.sparta.temueats.store.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.store.dto.StoreReqCreateDto;
import com.sparta.temueats.store.dto.StoreReqResDto;
import com.sparta.temueats.store.dto.StoreReqUpdateDto;
import com.sparta.temueats.store.service.StoreReqService;
import com.sparta.temueats.store.util.UserUtils;
import com.sparta.temueats.store.util.ValidUtils;
import com.sparta.temueats.user.entity.P_user;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/req")
public class StoreReqController {

    private final StoreReqService storeReqService;
    private final UserUtils userUtils;

    @PostMapping
    public ResponseDto<StoreReqResDto> save(@Valid StoreReqCreateDto storeReqCreateDto, BindingResult bindingResult) {
        ValidUtils.throwIfHasErrors(bindingResult, "가게 등록 요청 실패");

        // user will be switched from session later
        P_user user = userUtils.createMockUser();
        StoreReqResDto storeReqResDto = storeReqService.save(storeReqCreateDto, user);
        return new ResponseDto<>(1, "가게 등록 요청 성공", storeReqResDto);
    }

    @PutMapping
    public ResponseDto<Object> update(@RequestBody StoreReqUpdateDto storeReqUpdateDto) {
        P_user user = userUtils.createMockUser();
        storeReqService.update(storeReqUpdateDto, user);
        return new ResponseDto<>(1, "가게 요청 상태 수정 완료", null);
    }

}
