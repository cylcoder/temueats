package com.sparta.temueats.store.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.store.dto.StoreReqCreateDto;
import com.sparta.temueats.store.dto.StoreReqCreateWithImageDto;
import com.sparta.temueats.store.dto.StoreReqResDto;
import com.sparta.temueats.store.dto.StoreReqUpdateDto;
import com.sparta.temueats.store.service.StoreReqService;
import com.sparta.temueats.store.util.AuthUtils;
import com.sparta.temueats.store.util.AuthUtils.AuthStatus;
import com.sparta.temueats.store.util.ValidUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.sparta.temueats.store.util.AuthUtils.AuthStatus.AUTHORIZED;
import static com.sparta.temueats.user.entity.UserRoleEnum.*;

@Tag(name = "가게 등록 요청/수정/취소")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/req")
public class StoreReqController {

    private final StoreReqService storeReqService;
    private final AuthUtils authUtils;

    @Operation(summary = "가게 등록 요청")
    @PostMapping
    public ResponseDto<StoreReqResDto> save(
            @Valid StoreReqCreateDto storeReqCreateDto,
            BindingResult res
    ) {
        AuthStatus authStatus = authUtils.validate(List.of(CUSTOMER, MANAGER, MASTER));
        if (!authStatus.equals(AUTHORIZED)) {
            return new ResponseDto<>(ResponseDto.FAILURE, authStatus.getMsg());
        }

        ValidUtils.throwIfHasErrors(res, "가게 등록 요청 실패");

        return storeReqService.save(storeReqCreateDto);
    }

    @Operation(summary = "가게 등록 요청 (이미지) (?)")
    @PostMapping("/with-image")
    public ResponseDto<StoreReqResDto> save(
            @Valid @ModelAttribute StoreReqCreateWithImageDto storeReqCreateWithImageDto,
            BindingResult res
    ) {
        AuthStatus authStatus = authUtils.validate(List.of(CUSTOMER, MANAGER, MASTER));
        if (!authStatus.equals(AUTHORIZED)) {
            return new ResponseDto<>(ResponseDto.FAILURE, authStatus.getMsg());
        }

        ValidUtils.throwIfHasErrors(res, "가게 등록 요청 실패");

        return storeReqService.save(storeReqCreateWithImageDto);
    }

    @Operation(summary = "가게 등록 요청 상태 변경")
    @PutMapping
    public ResponseDto<Object> update(@RequestBody StoreReqUpdateDto storeReqUpdateDto) {
        AuthStatus authStatus = authUtils.validate(List.of(MANAGER));
        if (!authStatus.equals(AUTHORIZED)) {
            return new ResponseDto<>(ResponseDto.FAILURE, authStatus.getMsg());
        }

        return storeReqService.update(storeReqUpdateDto);
    }

    @Operation(summary = "가게 등록 요청 취소")
    @DeleteMapping("/{storeReqId}")
    public ResponseDto<Object> delete(@PathVariable UUID storeReqId) {
        AuthStatus authStatus = authUtils.validate(List.of(OWNER, MANAGER, MASTER));
        if (!authStatus.equals(AUTHORIZED)) {
            return new ResponseDto<>(ResponseDto.FAILURE, authStatus.getMsg());
        }

        return storeReqService.delete(storeReqId);
    }

}
