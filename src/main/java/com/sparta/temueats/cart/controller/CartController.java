package com.sparta.temueats.cart.controller;

import com.sparta.temueats.cart.dto.CartUpdateResponseDto;
import com.sparta.temueats.cart.dto.CartUpdateRequestDto;
import com.sparta.temueats.cart.dto.CartGetListResponseDto;
import com.sparta.temueats.cart.service.CartService;
import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    // 장바구니 추가
    @PostMapping("/carts/{menuId}")
    public ResponseDto<?> cartAddResponseDto(@RequestBody @Valid CartUpdateRequestDto cartUpdateRequestDto, BindingResult bindingResult,
                                             @PathVariable UUID menuId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartUpdateResponseDto cartUpdateResponseDto = cartService.createCarts(cartUpdateRequestDto, userDetails.getUser(), menuId);
        return new ResponseDto<>(1, "장바구니 추가가 완료되었습니다.", cartUpdateResponseDto);
    }

    // 장바구니 전체 조회
    @GetMapping("/carts")
    public ResponseDto<?> cartGetListResponseDto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CartGetListResponseDto> cartGetListResponseListDto = cartService.getCarts(userDetails.getUser());
        return new ResponseDto<>(1, "장바구니 전체 조회가 완료되었습니다.", cartGetListResponseListDto);

    }

    // 장바구니 메뉴 개수 수정
    @PutMapping("/carts/{cartId}/modify")
    public ResponseDto<?> cartUpdateResponseDto(@RequestBody @Valid CartUpdateRequestDto cartUpdateRequestDto, BindingResult bindingResult,
                                                @PathVariable UUID cartId) {
        CartUpdateResponseDto cartUpdateResponseDto = cartService.updateCarts(cartUpdateRequestDto, cartId);
        return new ResponseDto<>(1, "장바구니 메뉴 수량 변경이 완료되었습니다.", cartUpdateResponseDto);
    }

    // 장바구니 삭제
    @PutMapping("/carts/{cartId}/delete")
    public ResponseDto<?> cartDeleteResponse(@PathVariable UUID cartId) {
        cartService.deleteCarts(cartId);
        return new ResponseDto<>(1, "장바구니 메뉴가 정상적으로 삭제되었습니다.", null);
    }

    // 장바구니 결제할 물품 선택
    @PutMapping("/carts/{cartId}/select")
    public ResponseDto<?> cartSelectResponse(@PathVariable UUID cartId) {
        cartService.selectCarts(cartId);
        return new ResponseDto<>(1, "장바구니 메뉴 선택이 수정되었습니다.", null);
    }


}
