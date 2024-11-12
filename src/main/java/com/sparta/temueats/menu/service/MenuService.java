package com.sparta.temueats.menu.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.dto.MenuCreateDto;
import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.menu.repository.MenuRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.service.StoreService;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.temueats.global.ResponseDto.FAILURE;
import static com.sparta.temueats.global.ResponseDto.SUCCESS;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreService storeService;
    private final UserService userService;


    public ResponseDto<MenuResDto> save(MenuCreateDto menuCreateDto, HttpServletRequest req) {
        Optional<P_user> userOptional = userService.validateTokenAndGetUser(req);
        if (userOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다.");
        }

        Optional<P_store> storeOptional = storeService.findById(menuCreateDto.getStoreId());
        if (storeOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "유효하지 않은 가게 번호입니다.");
        }

        if (menuRepository.findByName(menuCreateDto.getName()).isPresent()) {
            return new ResponseDto<>(FAILURE, "이미 존재하는 메뉴입니다.");
        }

        P_menu menu = menuCreateDto.toEntity(storeOptional.get());
        menu.setCreatedBy(userOptional.get().getNickname());
        menuRepository.save(menu);
        return new ResponseDto<>(SUCCESS,"메뉴 등록 성공", new MenuResDto(menu));
    }

}
