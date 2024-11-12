package com.sparta.temueats.order.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.order.dto.OrderCreateRequestDto;
import com.sparta.temueats.order.dto.OrderGetListResponseDto;
import com.sparta.temueats.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     @GetMapping("/orders/customer")
    public ResponseDto<?> orderGetCustomerResponseListDto() {
         List<OrderGetListResponseDto> customerOrders = orderService.getCustomerOrders();
         return new ResponseDto<>(1, "주문 목록 조회에 성공했습니다.", customerOrders);
     }

    // 주문 목록 조회 (Owner)
     @GetMapping("/orders/owner")
    public ResponseDto<?> orderGetOwnerResponseListDto() {
        List<OrderGetListResponseDto> ownerOrders = orderService.getOwnerOrders();
        return new ResponseDto<>(1, "주문 목록 조회에 성공했습니다.", ownerOrders);
     }

    // 주문 상세 조회 (Customer)

    // 주문 취소 (Customer)
}
