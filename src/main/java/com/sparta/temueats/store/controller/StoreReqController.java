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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.temueats.store.util.AuthUtils.AuthStatus.AUTHORIZED;
import static com.sparta.temueats.user.entity.UserRoleEnum.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/req")
public class StoreReqController {

    private final StoreReqService storeReqService;
    private final AuthUtils authUtils;

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

    @PutMapping
    public ResponseDto<Object> update(@RequestBody StoreReqUpdateDto storeReqUpdateDto) {
        AuthStatus authStatus = authUtils.validate(List.of(MANAGER));
        if (!authStatus.equals(AUTHORIZED)) {
            return new ResponseDto<>(ResponseDto.FAILURE, authStatus.getMsg());
        }

        return storeReqService.update(storeReqUpdateDto);
    }

}
