package com.sparta.temueats.cart.service;

import com.sparta.temueats.cart.dto.CartAddRequestDto;
import com.sparta.temueats.cart.dto.CartAddResponseDto;
import com.sparta.temueats.cart.dto.CartGetListResponseDto;
import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    // todo 더미 데이터 싹 수정
    static final Long USER_ID1 = 1L;

    static final UUID MENU_ID = UUID.fromString("e95cb701-81b3-4124-9dd6-6b8fd423e783");

    static final UUID PRE_STORE_ID1 = UUID.randomUUID();
    static final UUID PRE_STORE_ID2 = UUID.randomUUID();

    private final CartRepository cartRepository;

    public CartAddResponseDto createCarts(CartAddRequestDto cartAddRequestDto, Long userId) {

        // 2-1. 장바구니에 같은 메뉴 id가 있는 경우
        boolean isPresentMenu = cartRepository.findByMenuIdByUserId(MENU_ID, USER_ID1).isPresent();
        // todo 임시 데이터
        if (isPresentMenu) {
            // 이미 같은 메뉴가 있다는 예외
            throw new CustomApiException("이미 해당 메뉴가 장바구니에 존재합니다.");
        }

        // 2-2. 담겨져 있는 메뉴의 가게 id와 새로 담는 메뉴의 가게 id가 다를 경우 예외
//        P_cart cart2 = cartRepository.findByMenuIdByUserId(MENU_ID, USER_ID1).orElseThrow(() ->
//                new CustomApiException("해당 MENU_ID가 존재하지 않습니다."));
        // todo 임시 데이터
        // todo cart2.getMenu().getStore() 탐색 로직 추가해서 storeId 구해야 함.
        if (PRE_STORE_ID1.equals(PRE_STORE_ID2)) {
            throw new CustomApiException("서로 다른 가게 메뉴를 함께 담을 수 없습니다.");
        }

        // 3. 오류가 없으면 담기
        P_cart cart3 = cartRepository.save(new P_cart(cartAddRequestDto, USER_ID1, MENU_ID));
        return new CartAddResponseDto(cart3);
    }

    public List<CartGetListResponseDto> getCarts() {
        List<P_cart> cartList = cartRepository.findAllByUserId(USER_ID1);
        List<CartGetListResponseDto> responseDtoList = new ArrayList<>();

        for (P_cart cart : cartList) {
            responseDtoList.add(new CartGetListResponseDto(cart));
        }

        return responseDtoList;
    }
}