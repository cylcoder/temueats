package com.sparta.temueats.store.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.store.dto.StoreResDto;
import com.sparta.temueats.store.dto.StoreUpdateDto;
import com.sparta.temueats.store.service.StoreService;
import com.sparta.temueats.store.util.UserUtils;
import com.sparta.temueats.user.entity.P_user;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;
    private final UserUtils userUtils;

    @PutMapping
    public ResponseDto<Object> update(@Valid @RequestBody StoreUpdateDto storeUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new CustomApiException("가게 정보 수정  실패: " + errorMessages);
        }

        // user will be switched from session later
        P_user user = storeService.findById(storeUpdateDto.getStoreId()).orElseThrow().getUser();
        storeService.update(storeUpdateDto, user);
        return new ResponseDto<>(1, "가게 정보 수정 성공", null);
    }

    @GetMapping
    public ResponseDto<List<StoreResDto>> findByName(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomApiException("검색어 미입력");
        }

        return new ResponseDto<>(1, "가게 검색 성공", storeService.findByName(name));
    }

}
