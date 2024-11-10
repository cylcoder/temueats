package com.sparta.temueats.cart.controller;

import com.sparta.temueats.cart.dto.CartResponseDto;
import com.sparta.temueats.cart.dto.CartRequestDto;
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
    public ResponseDto<?> cartAddResponseDto(@RequestBody @Valid CartUpdateRequestDto cartUpdateRequestDto, BindingResult bindingResult) {
        CartUpdateResponseDto cartUpdateResponseDto = cartService.createCarts(cartUpdateRequestDto, USER_ID);
        return new ResponseDto<>(1, "장바구니 추가가 완료되었습니다.", cartUpdateResponseDto);
    }

    // 장바구니 전체 조회
    @GetMapping("/carts")
    public ResponseDto<?> cartGetListResponseDto() {
        List<CartGetListResponseDto> cartGetListResponseListDto = cartService.getCarts();
        return new ResponseDto<>(1, "장바구니 전체 조회가 완료되었습니다.", cartGetListResponseListDto);

    }

    // 장바구니 메뉴 개수 수정
//    @PutMapping("/carts/cart_id}/modify")
//    public ResponseDto<?> cartMenuUpdateResponseDto(@RequestBody @Valid CartUpdateRequestDto cartUpdateRequestDto, BindingResult bindingResult) {
//        CartUpdateResponseDto cartUpdateResponseDto = cartService.updateCarts(cartUpdateRequestDto, USER_ID);
//        return new ResponseDto<>(1, "장바구니 물품 수량 변경 성공", cartUpdateResponseDto);
//    }

//    // 장바구니 삭제
//    @PutMapping("/carts/{cart_id}")
}
