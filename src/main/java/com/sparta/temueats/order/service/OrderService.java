package com.sparta.temueats.order.service;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.order.dto.OrderGetResponseDto;
import com.sparta.temueats.order.entity.OrderState;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.payment.entity.P_payment;
import com.sparta.temueats.payment.entity.PaymentStatus;
import com.sparta.temueats.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public OrderGetResponseDto getOrder(UUID orderId) {
        P_order order = orderRepository.findById(orderId).orElseThrow(() ->
                new CustomApiException("해당 주문을 찾을 수 없습니다."));
        return new OrderGetResponseDto(order);
    }

    // 결제 상태가 PAID 로 변경되고 주문 상태도 SUCCESS 로 변경하고, 5분 타이머 시작하는 로직 추가 (결제 상태 수정에서 사용)
    public void checkPayment(UUID paymentId) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(() -> {
            //결제,주문 불러오기
            P_order order = orderRepository.findByPaymentId(paymentId).orElseThrow(() ->
                    new CustomApiException("해당 주문을 찾을 수 없습니다."));
            P_payment payment=paymentRepository.findById(paymentId).orElseThrow(()->
                    new CustomApiException("해당 결제를 찾을 수 없습니다."));
            //상태 확인 후 변경 (5분동안 취소 없는 상태)
            if (payment.getPaymentStatus() == PaymentStatus.PAID && order.isCancelYn()) {
                order.changeCancleYn();
            }else if(order.getOrderState()==OrderState.FAIL){//취소 메서드 발생시
                payment.setStatus(PaymentStatus.CANCELED);
            }
            //변경내용 저장
            orderRepository.save(order);
            paymentRepository.save(payment);

            executorService.shutdown();
        }, 5, TimeUnit.MINUTES); // 5분 후에 작업을 실행
    }


}
