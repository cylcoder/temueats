package com.sparta.temueats.cart.service;

import com.sparta.temueats.cart.dto.CartUpdateRequestDto;
import com.sparta.temueats.cart.dto.CartUpdateResponseDto;
import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.menu.repository.MenuRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import com.sparta.temueats.store.entity.StoreState;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.temueats.dummy.DummyTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class CartServiceTest {

    @InjectMocks
    private CartService cartService;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private MenuRepository menuRepository;

    private P_user user;
    private P_menu menu;

    private CartUpdateRequestDto cartUpdateRequestDto = new CartUpdateRequestDto();

    @BeforeEach
    void setUp() {
        // mock 객체 설정
        user = mockCustomerUserSetting();
        menu = mockMenuSetting();
        cartUpdateRequestDto.setQuantity(3L);
    }


    @Test
    @DisplayName("장바구니_생성_성공")
    void createCartsSuccess() {
        // given
        UUID menuId = mockMenuSetting().getMenuId();
        when(cartRepository.findAllByUserId(user.getId())).thenReturn(List.of());
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(cartRepository.save(any(P_cart.class))).thenReturn(mockCartResponse());

        // when
        CartUpdateResponseDto responseDto = cartService.createCarts(cartUpdateRequestDto, user, menuId);

        // then
        assertNotNull(responseDto);
        assertEquals(cartUpdateRequestDto.getQuantity(), responseDto.getQuantity());
        assertEquals(menuId, responseDto.getMenuId());
    }

    @Test
    @DisplayName("장바구니_생성_실패1_이미_같은_메뉴가_존재함")
    void createCartsFail1() {
        // given
        P_cart existingCart = mockCartSetting();
        when(cartRepository.findAllByUserId(user.getId())).thenReturn(List.of(existingCart));
        when(menuRepository.findById(menu.getMenuId())).thenReturn(Optional.of(menu));
        when(cartRepository.findByMenuIdByUserId(menu.getMenuId(), user.getId())).thenReturn(existingCart);

        // when, then
        CustomApiException exception = assertThrows(CustomApiException.class, () ->
                cartService.createCarts(cartUpdateRequestDto, user, menu.getMenuId()));

        assertEquals("이미 해당 메뉴가 장바구니에 존재합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("장바구니_생성_실패2_다른_가게와_중복_불가능")
    void createCartsFail2() {
        // given
        GeometryFactory geometryFactory = new GeometryFactory();
        P_store differentStore = P_store.builder()
                .storeId(UUID.randomUUID())
                .user(mockCustomerUserSetting())
                .name("맛있는피자가게")
                .image("img_url")
                .number("031-2222-2222")
                .state(StoreState.OPENED)
                .leastPrice(8000)
                .category(Category.PIZZA)
                .latLng(geometryFactory.createPoint(new Coordinate(135, 135)))
                .address("피자로 11")
                .build();

        P_menu differentStoreMenu = P_menu.builder()
                .menuId(UUID.randomUUID())
                .store(differentStore)
                .name("포테이토 피자")
                .description("통통한 포테이토가 들어있어요.")
                .price(5000)
                .image("img_url")
                .category(Category.PIZZA)
                .sellState(SellState.SALE)
                .signatureYn(true)
                .build();

        P_cart existingCart = P_cart.builder()
                .quantity(3L)
                .selectYn(false)
                .user(mockCustomerUserSetting())
                .menu(differentStoreMenu)
                .deletedYn(false)
                .build();

        when(cartRepository.findAllByUserId(user.getId())).thenReturn(List.of(existingCart));
        when(menuRepository.findById(menu.getMenuId())).thenReturn(Optional.of(menu));
        when(cartRepository.findByMenuIdByUserId(menu.getMenuId(), user.getId())).thenReturn(null);

        // when, then
        CustomApiException exception = assertThrows(CustomApiException.class, () ->
                cartService.createCarts(cartUpdateRequestDto, user, menu.getMenuId()));

        assertEquals("서로 다른 가게 메뉴를 함께 담을 수 없습니다.", exception.getMessage());

    }


    private P_cart mockCartResponse() {
        return P_cart.builder()
                .menu(menu)
                .user(user)
                .quantity(3L)
                .selectYn(false)
                .deletedYn(false)
                .build();
    }
}