package com.sparta.temueats.payment.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.payment.entity.P_payment;
import com.sparta.temueats.payment.repository.PaymentRepository;
import com.sparta.temueats.user.entity.P_user;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.sparta.temueats.dummy.DummyTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CartRepository cartRepository;

    @Test
    @DisplayName("결제_생성_성공")
    void createPaymentsSuccess() {
        // given
        P_user user = mockCustomerUserSetting();
        P_order order = mockOrderSetting();
        P_payment payment = mockPaymentSetting();
        P_cart cart = mockCartSetting();

        List<P_cart> cartList = List.of(cart);

        when(orderRepository.findByUserId(user.getId())).thenReturn(order);
        when(paymentRepository.save(any(P_payment.class))).thenReturn(payment);
        when(cartRepository.findAllByUserId(user.getId())).thenReturn(cartList);

        // when
        Assertions.assertDoesNotThrow(() -> paymentService.createPayments(user));

        // then
        verify(cartRepository, times(1)).deleteAll(cartList);

    }
}