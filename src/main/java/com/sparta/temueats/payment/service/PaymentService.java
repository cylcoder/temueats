package com.sparta.temueats.payment.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.payment.dto.PaymentGetResponseDto;
import com.sparta.temueats.payment.dto.PaymentModifyRequestDto;
import com.sparta.temueats.payment.entity.P_payment;
import com.sparta.temueats.payment.entity.PaymentStatus;
import com.sparta.temueats.payment.repository.PaymentRepository;
import com.sparta.temueats.user.entity.P_user;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.Provider;
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
                new CustomApiException("결제 조회에 실패했습니다."));

        // 2. 해당 주문의 결제 내역 리턴
        return new PaymentGetResponseDto(payment);
    }

    public void modifyPayment(PaymentModifyRequestDto paymentmodifyRequestDto, UUID paymentId) {
        // 1. 결제 테이블에서 해당 결제 내역 찾아오기
        P_payment payment = paymentRepository.findById(paymentId).orElseThrow(() ->
                new CustomApiException("해당 결제 내역이 존재하지 않습니다."));

        // 2. request 가 1이면 PAID, 0 이면 FAIL
        if (paymentmodifyRequestDto.getPaymentStatus() == 1) {
            updatePaymentStatus(payment, PaymentStatus.PAID);
            // todo 여기에 주문 상태 변경 로직 추가
            //
        } else if (paymentmodifyRequestDto.getPaymentStatus() == 0) {
            updatePaymentStatus(payment, PaymentStatus.FAIL);
            throw new CustomApiException("결제가 실패 되었습니다.");
        } else {
            throw new CustomApiException("결제 여부 정보가 잘못되었습니다.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 독립적인 트랜잭션 시작
    public void updatePaymentStatus(P_payment payment, PaymentStatus status) {
        payment.setStatus(status);
        paymentRepository.save(payment);
    }
}