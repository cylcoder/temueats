package com.sparta.temueats.payment.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.payment.dto.PaymentGetResponseDto;
import com.sparta.temueats.payment.entity.P_payment;
import com.sparta.temueats.payment.entity.PaymentStatus;
import com.sparta.temueats.payment.repository.PaymentRepository;
import com.sparta.temueats.user.entity.P_user;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        P_payment saved = paymentRepository.save(P_payment.builder()
                .paymentStatus(PaymentStatus.READY)
                .price(order.getAmount())
                .order(order)
                .build());

        // order 의 payment 쪽에도 넣어줘야 함.
        order.setPayment(saved);

        // 3. 해당 user 의 장바구니 물품들 전부 삭제
        List<P_cart> cartList = cartRepository.findAllByUserId(user.getId());
        cartRepository.deleteAll(cartList);

    }

    public PaymentGetResponseDto getPayments(P_user user) {
        // 1. User 의 주문 내역 찾아오기
        P_order order = orderRepository.findByUserId(user.getId());

        // 2. 해당 주문의 결재 내역 조회
        P_payment payment = paymentRepository.findById(order.getPayment().getPaymentId()).orElseThrow(() ->
                new CustomApiException("해당 주문 아이디가 존재하지 않습니다."));

        // 2. 해당 주문의 결제 내역 리턴
        return new PaymentGetResponseDto(payment);
    }
}
