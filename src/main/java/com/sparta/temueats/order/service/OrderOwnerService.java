package com.sparta.temueats.order.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.coupon.repository.CouponRepository;
import com.sparta.temueats.coupon.service.CouponService;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.order.dto.OrderGetResponseDto;
import com.sparta.temueats.order.dto.TakeOutOrderCreateRequestDto;
import com.sparta.temueats.order.entity.OrderState;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderOwnerService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public void createTakeOutOrders(TakeOutOrderCreateRequestDto takeOutOrderCreateRequestDto, P_user user) {
        // 1. 주문 생성 시 장바구니에서 선택된 물품들 가져오기
        List<P_cart> allBySelect = cartRepository.findAllBySelectAndUserId(user.getId());


        if (allBySelect.isEmpty()) {
            throw new CustomApiException("장바구니에서 주문할 메뉴를 하나 이상 선택해주세요.");
        }

        Long total = 0L;
        // 2. 주인이 금액 입력 (쿠폰 사용은 안됨)
        for (P_cart cart : allBySelect) {
            total += cart.getMenu().getPrice().longValue();
        }

        // 3. Customer 정보 저장을 위해 찾아오기
        Optional<P_user> customer = userRepository.findByEmail(takeOutOrderCreateRequestDto.getEmail());

        // 4. 저장
        orderRepository.save(P_order.builder()
                .orderUId(UUID.randomUUID())
                .amount(total)
                .IsDelivery(false)
                .orderState(OrderState.PROGRESS)
                .discountPrice(0L)
                .customerRequest(null)
                .cancelYn(true)
                .cartList(allBySelect)
                .customerId(customer.orElseThrow().getId())
                .ownerId(user.getId())
                .build());
    }

    public List<OrderGetResponseDto> getOwnerOrders(P_user user) {
        List<P_order> orderList = orderRepository.findAllByOwnerId(user.getId());
        List<OrderGetResponseDto> responseDtoList = new ArrayList<>();

        for (P_order order : orderList) {
            responseDtoList.add(new OrderGetResponseDto(order));
        }

        return responseDtoList;
    }

    @Transactional
    public void cancelOwnerOrder(UUID orderId) {
        // 1. 주문 테이블에서 해당 orderId 를 찾아옴
        P_order order = orderRepository.findById(orderId).orElseThrow(() ->
                new CustomApiException("해당 주문 내역을 찾을 수 없습니다."));

        // 2. 해당 orderId의 cancelYn 체크
        if (!order.isCancelYn()) {
            throw new CustomApiException("결제 후 5분이 지나 주문을 취소할 수 없습니다.");
        }

        // 2-1. 주문 상태 변경 후 취소 정보 추가
        order.updateStatus(OrderState.FAIL);

        // 3. 결제 상태를 canceled 로 설정하고 취소일시, 취소자에 정보 추가
        // todo 결제 기능 개발 후 추가

    }
}
