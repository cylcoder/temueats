package com.sparta.temueats.menu.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.dto.MenuCreateDto;
import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.service.MenuService;
import com.sparta.temueats.store.util.UserUtils;
import com.sparta.temueats.store.util.ValidUtils;
import com.sparta.temueats.user.entity.P_user;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;
    private final UserUtils userUtils;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MenuResDto> save(@RequestBody @Valid MenuCreateDto menuCreateDto, BindingResult bindingResult) {
        ValidUtils.throwIfHasErrors(bindingResult, "메뉴 등록 실패");

        // user will be switched from session later
        P_user user = userUtils.createMockUser();
        return new ResponseDto<>(1, "메뉴 등록 성공", menuService.save(menuCreateDto, user));
    }

}
