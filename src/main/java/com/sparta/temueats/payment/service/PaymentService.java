package com.sparta.temueats.payment.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.payment.entity.P_payment;
import com.sparta.temueats.payment.entity.PaymentStatus;
import com.sparta.temueats.payment.repository.PaymentRepository;
import com.sparta.temueats.user.entity.P_user;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public void createPayments(P_user user) {

        // 1. User 의 주문 내역 찾아오기
        P_order order = orderRepository.findByUserId(user.getId());

        // 2. Payment 가격과 결제 상태 넣어서 생성
        paymentRepository.save(P_payment.builder()
                        .paymentStatus(PaymentStatus.READY)
                        .price(order.getAmount())
                        .order(order)
                        .build());

        // 3. 해당 user 의 장바구니 물품들 전부 삭제
        List<P_cart> cartList = cartRepository.findAllByUserId(user.getId());
        cartRepository.deleteAll(cartList);

    }
}
