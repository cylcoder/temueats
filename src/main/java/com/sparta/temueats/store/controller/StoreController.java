package com.sparta.temueats.store.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.store.dto.AddFavStoreRequestDto;
import com.sparta.temueats.store.dto.StoreDetailResDto;
import com.sparta.temueats.store.dto.StoreResDto;
import com.sparta.temueats.store.dto.StoreUpdateDto;
import com.sparta.temueats.store.service.StoreService;
import com.sparta.temueats.store.util.AuthUtils;
import com.sparta.temueats.store.util.ValidUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.sparta.temueats.global.ResponseDto.FAILURE;
import static com.sparta.temueats.store.util.AuthUtils.AuthStatus.AUTHORIZED;
import static com.sparta.temueats.user.entity.UserRoleEnum.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;
    private final AuthUtils authUtils;

    @PutMapping
    public ResponseDto<Object> update(
            @Valid @RequestBody StoreUpdateDto storeUpdateDto,
            BindingResult res
    ) {
        AuthUtils.AuthStatus authStatus = authUtils.validate(List.of(OWNER, MANAGER, MASTER));
        if (!authStatus.equals(AUTHORIZED)) {
            return new ResponseDto<>(ResponseDto.FAILURE, authStatus.getMsg());
        }

        ValidUtils.throwIfHasErrors(res, "가게 정보 수정 실패");

        return storeService.update(storeUpdateDto);
    }

    @GetMapping
    public ResponseDto<List<StoreResDto>> findByNameContaining(
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String menu,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "createdAt") String orderBy,
            @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size
    ) {
        if (store != null && !store.trim().isEmpty()) {
            return storeService.findByStoreNameContaining(store, order, orderBy, page, size);
        }

        if (menu != null && !menu.trim().isEmpty()) {
            return storeService.findByMenuNameContaining(menu, order, orderBy, page, size);
        }

        return new ResponseDto<>(FAILURE, "검색어는 필수입니다.");
    }

    @GetMapping("/{storeId}")
    public ResponseDto<StoreDetailResDto> findDetailById(@PathVariable UUID storeId) {
        return storeService.findDetailById(storeId);
    }

    @DeleteMapping("/{storeId}")
    public ResponseDto<Object> delete(@PathVariable UUID storeId) {
        AuthUtils.AuthStatus authStatus = authUtils.validate(List.of(OWNER, MANAGER, MASTER));
        if (!authStatus.equals(AUTHORIZED)) {
            return new ResponseDto<>(ResponseDto.FAILURE, authStatus.getMsg());
        }

        return storeService.delete(storeId);
    }

    // 즐겨찾기 추가, 삭제
    @PostMapping("/fav")
    public ResponseDto favStore(@RequestBody AddFavStoreRequestDto requestDto) {

        return storeService.addFavStore(requestDto);
    }

    // 즐겨찾기 가게 목록 조회
    @GetMapping("/fav")
    public ResponseDto getFavStoreList() {

        return storeService.getFavStoreList();
    }

}
