package com.sparta.temueats.cart.service;

import com.sparta.temueats.cart.dto.CartUpdateRequestDto;
import com.sparta.temueats.cart.dto.CartUpdateResponseDto;
import com.sparta.temueats.cart.dto.CartGetListResponseDto;
import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    // todo 더미 데이터 싹 수정
    static final Long USER_ID = 1L;

    static P_menu staticMenu = P_menu.builder()
            .menuId(UUID.randomUUID())
            .store(new P_store())
            .name("딸기 탕후루")
            .description("맛있는 딸기 탕후루입니다.")
            .price(3000)
            .image("img_url")
            .category(Category.CHINESE)
            .sellState(SellState.SALE)
            .signatureYn(true)
            .build();

    static GeometryFactory geometryFactory = new GeometryFactory();
    static P_user staticUser = P_user.builder()
            .id(USER_ID)
            .email("email@naver.com")
            .password("임시1234")
            .phone("010-1234-5678")
            .nickname("네임")
            .birth(Date.valueOf("20021226"))
            .use_yn(true)
            .role(UserRoleEnum.CUSTOMER)
            .imageProfile("img_url")
            .latLng(geometryFactory.createPoint(new Coordinate(123, 123)))
            .address("123층 123호")
            .build();

    private final CartRepository cartRepository;

    public CartUpdateResponseDto createCarts(CartUpdateRequestDto cartUpdateRequestDto, Long user) {

        // 1. 장바구니가 비어 있으면 바로 담기 & 바로 리턴
        List<P_cart> allByUserId = cartRepository.findAllByUserId(staticUser.getId());
        if (allByUserId.isEmpty()) {
            P_cart cart3 = cartRepository.save(P_cart.builder()
                    .quantity(cartUpdateRequestDto.getQuantity())
                    .selectYn(false)
                    .user(staticUser)
                    .menu(staticMenu)
                    .deletedYn(false)
                    .build());
            return new CartUpdateResponseDto(cart3);
        }

        // 2-1. 장바구니에 같은 메뉴 id가 있는 경우
        P_cart cart = cartRepository.findByMenuIdByUserId(staticMenu.getMenuId(), staticUser.getId());
        if (cart != null) {
            // 이미 같은 메뉴가 있다는 예외
            throw new CustomApiException("이미 해당 메뉴가 장바구니에 존재합니다.");
        }

        // 2-2. 담겨져 있는 메뉴의 가게 id와 새로 담는 메뉴의 가게 id가 다를 경우 예외
        if (cart.getMenu().getStore().getStoreId().equals(staticMenu.getStore().getStoreId())) {
            throw new CustomApiException("서로 다른 가게 메뉴를 함께 담을 수 없습니다.");
        }

        // 3. 오류가 없으면 담기
        P_cart cart3 = cartRepository.save(P_cart.builder()
                        .quantity(cartUpdateRequestDto.getQuantity())
                        .selectYn(false)
                        .user(staticUser)
                         .menu(staticMenu)
                        .deletedYn(false)
                .build());
        return new CartUpdateResponseDto(cart3);
    }

    public List<CartGetListResponseDto> getCarts() {
        List<P_cart> cartList = cartRepository.findAllByUserId(USER_ID);
        List<CartGetListResponseDto> responseDtoList = new ArrayList<>();

        for (P_cart cart : cartList) {
            responseDtoList.add(new CartGetListResponseDto(cart));
        }

        return responseDtoList;
    }

    @Transactional
    public CartUpdateResponseDto updateCarts(CartUpdateRequestDto cartUpdateRequestDto, Long userId, UUID cartId) {
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
    public void deleteCarts(Long userId, UUID cartId) {
        P_cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new CustomApiException("해당 장바구니 품목을 찾을 수 없습니다."));
        cart.delete();
    }

    @Transactional
    public void selectCarts(Long userId, UUID cartId) {
        P_cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new CustomApiException("해당 장바구니 품목을 찾을 수 없습니다."));
        cart.changeSelect();
    }
}