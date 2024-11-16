package com.sparta.temueats.payment.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.order.entity.OrderState;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.order.service.OrderService;
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
import java.util.ArrayList;
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
    private final OrderService orderService;

    public void createPayments(P_user user) {

        // 1. User 의 주문 내역 찾아오기
        List<P_order> order = orderRepository.findByUserId(user.getId());

        // 2. Payment 가격과 결제 상태 넣어서 생성
        List<P_payment> paymentList = new ArrayList<>();
        for (P_order pOrder : order) {
            P_payment saved = paymentRepository.save(P_payment.builder()
                    .paymentStatus(PaymentStatus.READY)
                    .price(pOrder.getAmount())
                    .order(pOrder)
                    .build());
            paymentList.add(saved);
            // order 의 payment 쪽에도 넣어줘야 함.
            pOrder.setPayment(saved);
        }

        // 3. 해당 user 의 장바구니 물품들 전부 삭제
        List<P_cart> cartList = cartRepository.findAllByUserId(user.getId());
        cartRepository.deleteAll(cartList);
    }

    public List<PaymentGetResponseDto> getPayments(P_user user) {
        // 1. User 의 주문 내역 찾아오기
        List<P_order> order = orderRepository.findByUserId(user.getId());

        // 2. 해당 주문의 결제 내역 조회
        List<P_payment> paymentList = new ArrayList<>();
        List<PaymentGetResponseDto> paymentGetResponseDtoList = new ArrayList<>();
        for (P_order pOrder : order) {
            P_payment payment = paymentRepository.findById(pOrder.getPayment().getPaymentId()).orElseThrow(() ->
                    new CustomApiException("결제 조회에 실패했습니다."));
            paymentList.add(payment);
            PaymentGetResponseDto paymentGetResponseDto = new PaymentGetResponseDto(payment);
            paymentGetResponseDtoList.add(paymentGetResponseDto);
        }


        // 2. 해당 주문의 결제 내역 리턴
        return paymentGetResponseDtoList;
    }

    public void modifyPayment(PaymentModifyRequestDto paymentmodifyRequestDto, UUID paymentId) {
        // 1. 결제 테이블에서 해당 결제 내역 찾아오기
        P_payment payment = paymentRepository.findById(paymentId).orElseThrow(() ->
                new CustomApiException("해당 결제 내역이 존재하지 않습니다."));

        // 2. request 가 1이면 PAID, 0 이면 FAIL
        if (paymentmodifyRequestDto.getPaymentStatus() == 1) {
            payment.getOrder().changeOrderState(OrderState.PROGRESS);
            updatePaymentStatus(payment, PaymentStatus.PAID);
            //5분 후 결제 메서드 추가
            orderService.checkPayment(payment.getPaymentId());

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
