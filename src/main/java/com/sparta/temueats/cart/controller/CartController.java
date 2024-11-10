package com.sparta.temueats.cart.controller;

import com.sparta.temueats.cart.dto.CartAddResponseDto;
import com.sparta.temueats.cart.dto.CartAddRequestDto;
import com.sparta.temueats.cart.dto.CartGetListResponseDto;
import com.sparta.temueats.cart.service.CartService;
import com.sparta.temueats.global.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    static final Long USER_ID = 1L;

    private final CartService cartService;

    // 장바구니 추가
    @PostMapping("/carts")
    public ResponseDto<?> cartResponseDto(@RequestBody @Valid CartAddRequestDto cartAddRequestDto, BindingResult bindingResult) {
        CartAddResponseDto cartAddResponseDto = cartService.createCarts(cartAddRequestDto, USER_ID);
        return new ResponseDto<>(1, "장바구니 추가가 완료되었습니다.", cartAddResponseDto);
    }

    // 장바구니 전체 조회
    // todo 조회용 dto 새로 만들기
    @GetMapping("/carts")
    public ResponseDto<?> cartResponseDto() {
        List<CartGetListResponseDto> cartGetListResponseListDto = cartService.getCarts();
        return new ResponseDto<>(1, "장바구니 전체 조회가 완료되었습니다.", cartGetListResponseListDto);

    }

//    // 장바구니 메뉴 개수 수정
//    @PutMapping("/carts/cart_id}/modify")
//
//    // 장바구니 삭제
//    @PutMapping("/carts/{cart_id}")
}
