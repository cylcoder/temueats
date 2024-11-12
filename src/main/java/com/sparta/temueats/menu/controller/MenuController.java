package com.sparta.temueats.menu.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.dto.MenuCreateDto;
import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.service.MenuService;
import com.sparta.temueats.store.util.ValidUtils;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MenuResDto> save(
            @RequestBody @Valid MenuCreateDto menuCreateDto,
            BindingResult res,
            HttpServletRequest req) {
        ValidUtils.throwIfHasErrors(res, "메뉴 등록 실패");

        return menuService.save(menuCreateDto, req);
    }

}
