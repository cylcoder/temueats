package com.sparta.temueats.menu.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.dto.MenuCreateDto;
import com.sparta.temueats.menu.dto.MenuCreateWithImageDto;
import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.dto.MenuUpdateDto;
import com.sparta.temueats.menu.service.MenuService;
import com.sparta.temueats.store.util.ValidUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseDto<MenuResDto> save(
            @RequestBody @Valid MenuCreateDto menuCreateDto,
            BindingResult res,
            HttpServletRequest req) {
        ValidUtils.throwIfHasErrors(res, "메뉴 등록 실패");

        return menuService.save(menuCreateDto, req);
    }

    @PostMapping("/with-image")
    public ResponseDto<MenuResDto> save(
            @Valid @ModelAttribute MenuCreateWithImageDto menuCreateWithImageDto,
            BindingResult res,
            HttpServletRequest req) {
        ValidUtils.throwIfHasErrors(res, "메뉴 등록 실패");

        return menuService.save(menuCreateWithImageDto, req);
    }

    @PutMapping
    public ResponseDto<MenuResDto> update(
            @RequestBody @Valid MenuUpdateDto menuUpdateDto,
            BindingResult res,
            HttpServletRequest req) {
        ValidUtils.throwIfHasErrors(res, "메뉴 수정 실패");

        return menuService.update(menuUpdateDto, req);
    }

}
