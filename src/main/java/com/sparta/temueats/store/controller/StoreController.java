//package com.sparta.temueats.store.controller;
//
//import com.sparta.temueats.global.ResponseDto;
//import com.sparta.temueats.global.ex.CustomApiException;
//import com.sparta.temueats.store.dto.AddFavStoreRequestDto;
//import com.sparta.temueats.store.dto.StoreResDto;
//import com.sparta.temueats.store.dto.StoreUpdateDto;
//import com.sparta.temueats.store.service.StoreService;
//import com.sparta.temueats.store.util.ValidUtils;
//import com.sparta.temueats.user.entity.P_user;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/stores")
//public class StoreController {
//
//    private final StoreService storeService;
//
//    @PutMapping
//    public ResponseDto<Object> update(@Valid @RequestBody StoreUpdateDto storeUpdateDto, BindingResult bindingResult) {
//        ValidUtils.throwIfHasErrors(bindingResult, "가게 정보 수정 실패");
//
//        // user will be switched from session later
//        P_user user = storeService.findById(storeUpdateDto.getStoreId()).orElseThrow().getUser();
//        storeService.update(storeUpdateDto, user);
//        return new ResponseDto<>(1, "가게 정보 수정 성공", null);
//    }
//
//    @GetMapping
//    public ResponseDto<List<StoreResDto>> findByName(@RequestParam String name) {
//        if (name == null || name.trim().isEmpty()) {
//            throw new CustomApiException("검색어는 필수입니다.");
//        }
//
//        return new ResponseDto<>(1, "가게 검색 성공", storeService.findByName(name));
//    }
//
//    // 즐겨찾기 추가, 삭제
//    @PostMapping("/fav")
//    public ResponseDto favStore(@RequestBody AddFavStoreRequestDto requestDto, HttpServletRequest req) {
//
//        return storeService.addFavStore(requestDto, req);
//    }
//
//    // 즐겨찾기 가게 목록 조회
//    @GetMapping("/fav")
//    public ResponseDto getFavStoreList(HttpServletRequest req) {
//
//        return storeService.getFavStoreList(req);
//    }
//
//}
