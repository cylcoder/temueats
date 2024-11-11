package com.sparta.temueats.order.service;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.cart.repository.CartRepository;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.order.dto.OrderCreateRequestDto;
import com.sparta.temueats.order.entity.OrderState;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    static final Long USER_ID_CUSTOMER = 1L;
    private final Long USER_ID_OWNER = 2L;

    private final Long AMOUNT = 9000L;
    private final Long DISCOUNT_PRICE = 1000L;

    public void createDeliveryOrders(OrderCreateRequestDto orderCreateRequestDto, Long userId) {
        // 1. 주문 생성 시 장바구니에서 선택된 물품들 가져오기
        List<P_cart> allBySelect = cartRepository.findAllBySelectAndUserId(USER_ID_CUSTOMER);

        if (allBySelect.isEmpty()) {
            throw new CustomApiException("장바구니에서 주문할 메뉴를 하나 이상 선택해주세요.");
        }

        // 2. 가져와서 가격 취합 후 db에 저장
        Long total = 0L;
        for (P_cart cart : allBySelect) {
//            amount += cart.getMenu().getPrice(); ???
            total += AMOUNT; // 임시
        }

        // 3. 쿠폰 사용
        Long finalTotal = total - DISCOUNT_PRICE;

        // 4. 저장
        orderRepository.save(P_order.builder()
                        .orderUId(UUID.randomUUID())
                        .amount(finalTotal)
                        .IsDelivery(true)
                        .orderState(OrderState.STANDBY)
                        .discountPrice(DISCOUNT_PRICE)
                        .customerRequest(orderCreateRequestDto.getCustomerRequest())
                        .cancelYn(false)
                        .cartList(allBySelect)
                        .customerId(USER_ID_CUSTOMER)
                        .ownerId(USER_ID_OWNER)
                .build());

    }

    public void createTakeOutOrders(OrderCreateRequestDto orderCreateRequestDto, Long userId) {
        // 1. 주문 생성 시 장바구니에서 선택된 물품들 가져오기
        List<P_cart> allBySelect = cartRepository.findAllBySelectAndUserId(USER_ID_CUSTOMER);

        if (allBySelect.isEmpty()) {
            throw new CustomApiException("장바구니에서 주문할 메뉴를 하나 이상 선택해주세요.");
        }

        // 2. 주인이 금액 입력 (쿠폰 사용은 안됨)
        Long finalTotal = AMOUNT;

        // 3. 저장
        orderRepository.save(P_order.builder()
                .orderUId(UUID.randomUUID())
                .amount(finalTotal)
                .IsDelivery(false)
                .orderState(OrderState.PROGRESS)
                .discountPrice(0L)
                .customerRequest(orderCreateRequestDto.getCustomerRequest())
                .cancelYn(false)
                .cartList(allBySelect)
                .customerId(USER_ID_CUSTOMER)
                .ownerId(USER_ID_OWNER)
                .build());
    }

//    public List<OrderCustomerResponseDto> getCustomerOrders() {
//    }
}
