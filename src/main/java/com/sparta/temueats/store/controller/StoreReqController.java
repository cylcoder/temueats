package com.sparta.temueats.store.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.store.dto.StoreReqCreateDto;
import com.sparta.temueats.store.dto.StoreReqResDto;
import com.sparta.temueats.store.dto.StoreReqUpdateDto;
import com.sparta.temueats.store.service.StoreReqService;
import com.sparta.temueats.store.util.ValidUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/req")
public class StoreReqController {

    private final StoreReqService storeReqService;

    @PostMapping
    public ResponseDto<StoreReqResDto> save(
            @Valid StoreReqCreateDto storeReqCreateDto,
            BindingResult res,
            HttpServletRequest req) {
        ValidUtils.throwIfHasErrors(res, "가게 등록 요청 실패");

        return storeReqService.save(storeReqCreateDto, req);
    }

    @PutMapping
    public ResponseDto<Object> update(@RequestBody StoreReqUpdateDto storeReqUpdateDto, HttpServletRequest req) {
        return storeReqService.update(storeReqUpdateDto, req);
    }

}
