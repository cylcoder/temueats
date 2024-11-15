package com.sparta.temueats.order.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.coupon.repository.CouponRepository;
import com.sparta.temueats.coupon.service.CouponService;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.order.dto.DeliveryOrderCreateRequestDto;
import com.sparta.temueats.order.entity.OrderState;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.user.entity.P_user;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.temueats.dummy.DummyTestData.*;
import static javax.management.Query.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderCustomerServiceTest {

    @InjectMocks
    private OrderCustomerService orderCustomerService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CouponService couponService;

    private P_user user;
    private P_menu menu;
    private P_cart cart;
    private P_coupon coupon = mockCouponSetting();
    private P_order order;
    private DeliveryOrderCreateRequestDto withCoupon =
            new DeliveryOrderCreateRequestDto("젓가락 2개씩 챙겨주세요.", coupon.getId());
    private DeliveryOrderCreateRequestDto withoutCoupon =
            new DeliveryOrderCreateRequestDto("젓가락 2개씩 챙겨주세요.");

    @Test
    @DisplayName("Customer_주문_생성_성공_쿠폰_사용O")
    void createDeliveryOrdersWithCouponSuccess() {
        // given
        P_cart cartItem = mockCartSetting();
        user = mockCustomerUserSetting();

        when(orderRepository.findAllByUserIdIsIng(user.getId(), OrderState.STANDBY)).thenReturn(List.of());
        when(cartRepository.findAllBySelectAndUserId(user.getId())).thenReturn(List.of(cartItem));
        when(couponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(orderRepository.save(any(P_order.class))).thenReturn(new P_order());


        // when
        Assertions.assertDoesNotThrow(() -> orderCustomerService.createDeliveryOrders(withCoupon, user));

        // then
        long expectedTotalAmount = (cartItem.getMenu().getPrice() * cartItem.getQuantity()) + cartItem.getMenu().getStore().getDeliveryPrice() - coupon.getDiscountAmount();
        Assertions.assertEquals(expectedTotalAmount, 16000);
    }

    @Test
    @DisplayName("Customer_주문_생성_성공_쿠폰_사용X")
    void createDeliveryOrdersSuccess() {
        // given
        P_cart cartItem = mockCartSetting();
        user = mockCustomerUserSetting();

        when(orderRepository.findAllByUserIdIsIng(user.getId(), OrderState.STANDBY)).thenReturn(List.of());
        when(cartRepository.findAllBySelectAndUserId(user.getId())).thenReturn(List.of(cartItem));
        when(couponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(orderRepository.save(any(P_order.class))).thenReturn(new P_order());


        // when
        Assertions.assertDoesNotThrow(() -> orderCustomerService.createDeliveryOrders(withoutCoupon, user));

        // then
        long expectedTotalAmount = (cartItem.getMenu().getPrice() * cartItem.getQuantity()) + cartItem.getMenu().getStore().getDeliveryPrice();
        Assertions.assertEquals(expectedTotalAmount, 18000);
    }

    @Test
    @DisplayName("Customer_주문_생성_실패1_진행중인_주문이_있으면_주문생성_불가")
    void createDeliveryOrdersFail1() {
        // given
        P_order IsIngOrder = order;
        when(orderRepository.findAllByUserIdIsIng(1L, OrderState.STANDBY))
                .thenReturn(List.of(IsIngOrder));

        // when
        CustomApiException exception = Assertions.assertThrows(CustomApiException.class, () ->
                orderCustomerService.createDeliveryOrders(withCoupon, user));

        Assertions.assertEquals(exception.getMessage(), "현재 진행중인 주문이 있습니다. 해당 주문에 대한 결제를 먼저 진행해주세요.");

    }

    @Test
    @DisplayName("Customer_주문_생성_실패2_장바구니에서_주문할_메뉴선택없이_주문생성_불가")
    void createDeliveryOrdersFail2() {

    }

    @Test
    @DisplayName("Customer_주문_생성_실패3_주문금액이_가게의_최소주문금액_미만이면_주문생성_불가")
    void createDeliveryOrdersFail3() {

    }



}

