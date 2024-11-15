package com.sparta.temueats.menu.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.menu.dto.MenuCreateDto;
import com.sparta.temueats.menu.dto.MenuCreateWithImageDto;
import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.dto.MenuUpdateDto;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.menu.repository.MenuRepository;
import com.sparta.temueats.s3.service.FileService;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.repository.StoreRepository;
import com.sparta.temueats.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.temueats.global.ResponseDto.FAILURE;
import static com.sparta.temueats.global.ResponseDto.SUCCESS;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;
    private final FileService fileService;

    public ResponseDto<MenuResDto> save(MenuCreateDto menuCreateDto) {
        Optional<P_store> storeOptional = storeRepository.findById(menuCreateDto.getStoreId());
        if (storeOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "유효하지 않은 가게 번호입니다.");
        }

        if (menuRepository.findByName(menuCreateDto.getName()).isPresent()) {
            return new ResponseDto<>(FAILURE, "이미 존재하는 메뉴입니다.");
        }

        P_menu menu = menuCreateDto.toEntity(storeOptional.get());
        menuRepository.save(menu);
        return new ResponseDto<>(SUCCESS,"메뉴 등록 성공", new MenuResDto(menu));
    }

    public ResponseDto<MenuResDto> save(MenuCreateWithImageDto menuCreateWithImageDto) {
        Optional<P_store> storeOptional = storeRepository.findById(menuCreateWithImageDto.getStoreId());
        if (storeOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "유효하지 않은 가게 번호입니다.");
        }

        if (menuRepository.findByName(menuCreateWithImageDto.getName()).isPresent()) {
            return new ResponseDto<>(FAILURE, "이미 존재하는 메뉴입니다.");
        }

        String image = null;
        if (menuCreateWithImageDto.getImage() != null && !menuCreateWithImageDto.getImage().isEmpty()) {
            try {
                image = fileService.uploadFile(menuCreateWithImageDto.getImage());
            } catch (IOException e) {
                return new ResponseDto<>(ResponseDto.FAILURE, "이미지 업로드 실패");
            }
        }

        P_menu menu = menuCreateWithImageDto.toEntity(storeOptional.get(), image);
        menuRepository.save(menu);
        return new ResponseDto<>(SUCCESS,"메뉴 등록 성공", new MenuResDto(menu));
    }


    public ResponseDto<MenuResDto> update(MenuUpdateDto menuUpdateDto) {
        Optional<P_menu> menuOptional = menuRepository.findById(menuUpdateDto.getMenuId());
        if (menuOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "유효하지 않은 메뉴 번호입니다.");
        }

        P_menu menu = menuOptional.get().update(menuUpdateDto, userService.getUser());
        return new ResponseDto<>(SUCCESS,"메뉴 수정 성공", new MenuResDto(menu));
    }

    public ResponseDto<Object> findById(UUID menuId) {
        Optional<P_menu> menuOptional = menuRepository.findById(menuId);

        if (menuOptional.isEmpty()) {
            return new ResponseDto<>(FAILURE, "유효하지 않은 메뉴 번호입니다.");
        }

        menuOptional.get().delete();
        return new ResponseDto<>(SUCCESS, "메뉴 삭제 성공");
    }
}
