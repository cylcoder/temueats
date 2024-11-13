package com.sparta.temueats.store.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.store.dto.AddFavStoreRequestDto;
import com.sparta.temueats.store.dto.StoreDetailResDto;
import com.sparta.temueats.store.dto.StoreResDto;
import com.sparta.temueats.store.dto.StoreUpdateDto;
import com.sparta.temueats.store.service.StoreService;
import com.sparta.temueats.store.util.ValidUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.sparta.temueats.global.ResponseDto.FAILURE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @PutMapping
    public ResponseDto<Object> update(
            @Valid @RequestBody StoreUpdateDto storeUpdateDto,
            BindingResult res,
            HttpServletRequest req) {
        ValidUtils.throwIfHasErrors(res, "가게 정보 수정 실패");

        return storeService.update(storeUpdateDto, req);
    }

    @GetMapping
    public ResponseDto<List<StoreResDto>> findByNameContaining(
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String menu,
            HttpServletRequest req) {

        if (store != null && !store.trim().isEmpty()) {
            return storeService.findByStoreNameContaining(store, req);
        }

        if (menu != null && !menu.trim().isEmpty()) {
            return storeService.findByMenuNameContaining(menu, req);
        }

        return new ResponseDto<>(FAILURE, "검색어는 필수입니다.");
    }



    @GetMapping("/{storeId}")
    public ResponseDto<StoreDetailResDto> findDetailById(@PathVariable UUID storeId, HttpServletRequest req) {
        return storeService.findDetailById(storeId, req);
    }

    // 즐겨찾기 추가, 삭제
    @PostMapping("/fav")
    public ResponseDto favStore(@RequestBody AddFavStoreRequestDto requestDto, HttpServletRequest req) {

        return storeService.addFavStore(requestDto, req);
    }

    // 즐겨찾기 가게 목록 조회
    @GetMapping("/fav")
    public ResponseDto getFavStoreList(HttpServletRequest req) {

        return storeService.getFavStoreList(req);
    }

}
