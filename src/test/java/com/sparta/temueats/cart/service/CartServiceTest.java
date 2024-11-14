package com.sparta.temueats.cart.service;

import com.sparta.temueats.cart.dto.CartUpdateRequestDto;
import com.sparta.temueats.cart.dto.CartUpdateResponseDto;
import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.menu.repository.MenuRepository;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import com.sparta.temueats.store.entity.StoreState;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jdk.jfr.Name;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @Mock
    private UserRepository userRepository;

    private P_user user;
    private P_menu menu;
    private P_store store;
    private CartUpdateRequestDto cartUpdateRequestDto = new CartUpdateRequestDto();

    @BeforeEach
    void setUp() {
        // mock 객체 설정
        user = mockUserSetting();
        menu = mockMenuSetting();
        store = mockStoreSetting();
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
    @DisplayName("장바구니_생성_실패1_영업중이_아닌_가게")
    void createCartsFail1() {
    }


    @Test
    @DisplayName("장바구니_생성_실패2_이미_같은_메뉴가_존재함")
    void createCartsFail2() {
    }

    @Test
    @DisplayName("장바구니_생성_실패3_다른_가게와_중복_불가능")
    void createCartsFail3() {
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


    private P_user mockUserSetting() {
        GeometryFactory geometryFactory = new GeometryFactory();
        return P_user.builder()
                .email("CustomerTest@test.com")
                .password("1234")
                .phone("010-1234-5678")
                .nickname("고객 테스트")
                .birth(Date.valueOf("2002-12-26"))
                .use_yn(true)
                .role(UserRoleEnum.CUSTOMER)
                .imageProfile("img_url")
                .latLng(geometryFactory.createPoint(new Coordinate(123, 123)))
                .address("11층 11호")
                .build();
    }

    private P_menu mockMenuSetting() {
        return P_menu.builder()
                .store(mockStoreSetting())
                .name("맛있는 김치찌개")
                .description("얼큰한 맛이 끝내주는 김치찌개입니다.")
                .price(8000)
                .image("img_url")
                .category(Category.KOREAN)
                .sellState(SellState.SALE)
                .signatureYn(true)
                .build();
    }

    private P_store mockStoreSetting() {
        GeometryFactory geometryFactory = new GeometryFactory();
        return P_store.builder()
                .user(mockUserSetting())
                .name("얼큰가게")
                .image("img_url")
                .number("031-1111-1111")
                .state(StoreState.OPENED)
                .leastPrice(10000)
                .category(Category.KOREAN)
                .latLng(geometryFactory.createPoint(new Coordinate(124, 124)))
                .address("고등로 15")
                .build();
    }


}