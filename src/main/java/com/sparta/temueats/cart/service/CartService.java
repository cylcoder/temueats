package com.sparta.temueats.cart.service;

import com.sparta.temueats.cart.dto.CartUpdateRequestDto;
import com.sparta.temueats.cart.dto.CartUpdateResponseDto;
import com.sparta.temueats.cart.dto.CartGetListResponseDto;
import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.menu.repository.MenuRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;

    public CartUpdateResponseDto createCarts(CartUpdateRequestDto cartUpdateRequestDto, P_user user, UUID menuId) {
        // 1. 장바구니가 비어 있으면 바로 담기 & 바로 리턴
        List<P_cart> allByUserId = cartRepository.findAllByUserId(user.getId());
        P_menu menu = menuRepository.findById(menuId).orElseThrow(() ->
                new CustomApiException("해당 메뉴가 존재하지 않습니다."));
        if (allByUserId.isEmpty()) {
            P_cart cart3 = cartRepository.save(P_cart.builder()
                    .quantity(cartUpdateRequestDto.getQuantity())
                    .selectYn(false)
                    .user(user)
                    .menu(menu)
                    .deletedYn(false)
                    .build());
            return new CartUpdateResponseDto(cart3);
        }

        // 2-1. 장바구니에 같은 메뉴 id가 있는 경우
        P_cart cart = cartRepository.findByMenuIdByUserId(menu.getMenuId(), user.getId());
        if (cart != null) {
            // 이미 같은 메뉴가 있다는 예외
            throw new CustomApiException("이미 해당 메뉴가 장바구니에 존재합니다.");
        }

        // 2-2. 담겨져 있는 메뉴들의 가게 id와 새로 담는 메뉴의 가게 id가 다를 경우 예외 처리
        for (P_cart existingCart : allByUserId) {
            if (!existingCart.getMenu().getStore().getStoreId().equals(menu.getStore().getStoreId())) {
                throw new CustomApiException("서로 다른 가게 메뉴를 함께 담을 수 없습니다.");
            }
        }

        // 3. 오류가 없으면 담기
        P_cart cart3 = cartRepository.save(P_cart.builder()
                        .quantity(cartUpdateRequestDto.getQuantity())
                        .selectYn(false)
                        .user(user)
                        .menu(menu)
                        .deletedYn(false)
                .build());
        return new CartUpdateResponseDto(cart3);
    }

    public List<CartGetListResponseDto> getCarts(P_user user) {
        List<P_cart> cartList = cartRepository.findAllByUserId(user.getId());
        List<CartGetListResponseDto> responseDtoList = new ArrayList<>();

        for (P_cart cart : cartList) {
            responseDtoList.add(new CartGetListResponseDto(cart));
        }

        return responseDtoList;
    }

    @Transactional
    public CartUpdateResponseDto updateCarts(CartUpdateRequestDto cartUpdateRequestDto, UUID cartId) {
        Long updatePrice = cartUpdateRequestDto.getQuantity();

        if (updatePrice <= 0 || updatePrice > 50) {
            throw new CustomApiException("수량은 최소 1부터 50까지 변경할 수 있습니다.");
        }

        P_cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new CustomApiException("해당 장바구니 품목을 찾을 수 없습니다."));
        cart.update(cartUpdateRequestDto);

        return new CartUpdateResponseDto(cart);
    }

    @Transactional
    public void deleteCarts(UUID cartId) {
        P_cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new CustomApiException("해당 장바구니 품목을 찾을 수 없습니다."));
        cart.delete();
    }

    @Transactional
    public void selectCarts(UUID cartId) {
        P_cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new CustomApiException("해당 장바구니 품목을 찾을 수 없습니다."));
        cart.changeSelect();
    }
}