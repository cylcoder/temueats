package com.sparta.temueats.order.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.order.dto.OrderCreateRequestDto;
import com.sparta.temueats.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    static final Long USER_ID = 1L;

    private final OrderService orderService;

    // todo customer 접근 가능 권한 설정
    // todo owner 접근 가능 권한 설정

    // 주문 생성 (Customer)
    @PostMapping("/orders/customer")
    public ResponseDto<?> orderCreateResponseCustomerDto(@RequestBody @Valid OrderCreateRequestDto orderCreateRequestDto, BindingResult bindingResult) {
        orderService.createDeliveryOrders(orderCreateRequestDto, USER_ID);
        return new ResponseDto<>(1, "주문이 정상적으로 생성되었습니다.", null);
    }

    // 주문 생성 (Owner)
    @PostMapping("/orders/owner")
    public ResponseDto<?> orderCreateResponseOwnerDto(@RequestBody @Valid OrderCreateRequestDto orderCreateRequestDto, BindingResult bindingResult) {
        orderService.createTakeOutOrders(orderCreateRequestDto, USER_ID);
        return new ResponseDto<>(1, "주문이 정상적으로 생성되었습니다.", null);
    }

    // 주문 목록 조회 (Customer)
//     @GetMapping("/orders/customer")
//    public ResponseDto<?> orderGetCustomerResponseListDto() {
//        orderService.getCustomerOrders();
//     }

    // 주문 목록 조회 (Owner)
    // @GetMapping("/orders/Owner")

    // 주문 상세 조회 (Customer)

    // 주문 취소 (Customer)
}
