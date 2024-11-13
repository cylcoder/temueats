package com.sparta.temueats.menu.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.dto.MenuCreateDto;
import com.sparta.temueats.menu.dto.MenuCreateWithImageDto;
import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.dto.MenuUpdateDto;
import com.sparta.temueats.menu.service.MenuService;
import com.sparta.temueats.store.util.AuthUtils;
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
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;
    private final AuthUtils authUtils;

    @PostMapping
    public ResponseDto<MenuResDto> save(
            @RequestBody @Valid MenuCreateDto menuCreateDto,
            BindingResult res
    ) {
        AuthUtils.AuthStatus authStatus = authUtils.validate(List.of(OWNER, MANAGER, MASTER));
        if (!authStatus.equals(AUTHORIZED)) {
            return new ResponseDto<>(ResponseDto.FAILURE, authStatus.getMsg());
        }

        ValidUtils.throwIfHasErrors(res, "메뉴 등록 실패");

        return menuService.save(menuCreateDto);
    }

    @PostMapping("/with-image")
    public ResponseDto<MenuResDto> save(
            @Valid @ModelAttribute MenuCreateWithImageDto menuCreateWithImageDto,
            BindingResult res
    ) {
        ValidUtils.throwIfHasErrors(res, "메뉴 등록 실패");

        return menuService.save(menuCreateWithImageDto);
    }

    @PutMapping
    public ResponseDto<MenuResDto> update(
            @RequestBody @Valid MenuUpdateDto menuUpdateDto,
            BindingResult res
    ) {
        ValidUtils.throwIfHasErrors(res, "메뉴 수정 실패");

        return menuService.update(menuUpdateDto);
    }

}
