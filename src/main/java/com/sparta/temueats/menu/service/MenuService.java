package com.sparta.temueats.menu.service;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.menu.dto.MenuCreateDto;
import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.menu.repository.MenuRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.service.StoreService;
import com.sparta.temueats.user.entity.P_user;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreService storeService;

    public MenuResDto save(MenuCreateDto menuCreateDto, P_user user) {
        P_store store = storeService.findById(menuCreateDto.getStoreId())
                .orElseThrow(() -> new CustomApiException("유효하지 않은 가게 번호입니다."));

        menuRepository.findByName(menuCreateDto.getName()).ifPresent(m -> {
            throw new CustomApiException("이미 존재하는 메뉴입니다.");
        });

        P_menu menu = menuCreateDto.toEntity(store);
        menu.setCreatedBy(user.getNickname());
        return new MenuResDto(menuRepository.save(menu));
    }

}
