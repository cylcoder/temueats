package com.sparta.temueats.cart.controller;

import com.sparta.temueats.cart.dto.CartUpdateResponseDto;
import com.sparta.temueats.cart.dto.CartUpdateRequestDto;
import com.sparta.temueats.cart.dto.CartGetListResponseDto;
import com.sparta.temueats.cart.service.CartService;
import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    @PutMapping("/carts/{cartId}/modify")
    public ResponseDto<?> cartUpdateResponseDto(@RequestBody @Valid CartUpdateRequestDto cartUpdateRequestDto, BindingResult bindingResult,
                                                @PathVariable UUID cartId) {
        CartUpdateResponseDto cartUpdateResponseDto = cartService.updateCarts(cartUpdateRequestDto, USER_ID, cartId);
        return new ResponseDto<>(1, "장바구니 물품 수량 변경이 완료되었습니다.", cartUpdateResponseDto);
    }

    // 장바구니 삭제
    @PutMapping("/carts/{cartId}/delete")
    public ResponseDto<?> cartDeleteResponse(@PathVariable UUID cartId) {
        cartService.deleteCarts(USER_ID, cartId);
        return new ResponseDto<>(1, "장바구니 물품이 정상적으로 삭제되었습니다.", null);
    }
}
