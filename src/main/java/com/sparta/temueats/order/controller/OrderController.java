package com.sparta.temueats.order.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.order.dto.DeliveryOrderCreateRequestDto;
import com.sparta.temueats.order.dto.OrderGetResponseDto;
import com.sparta.temueats.order.dto.TakeOutOrderCreateRequestDto;
import com.sparta.temueats.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseDto<?> orderCreateResponseCustomerDto(@RequestBody @Valid DeliveryOrderCreateRequestDto deliveryOrderCreateRequestDto, BindingResult bindingResult) {
        orderService.createDeliveryOrders(deliveryOrderCreateRequestDto, USER_ID);
        return new ResponseDto<>(1, "주문이 정상적으로 생성되었습니다.", null);
    }

    // 주문 생성 (Owner)
    @PostMapping("/orders/owner")
    public ResponseDto<?> orderCreateResponseOwnerDto(@RequestBody @Valid TakeOutOrderCreateRequestDto takeOutOrderCreateRequestDto, BindingResult bindingResult) {
        orderService.createTakeOutOrders(takeOutOrderCreateRequestDto, USER_ID);
        return new ResponseDto<>(1, "주문이 정상적으로 생성되었습니다.", null);
    }

    // 주문 목록 조회 (Customer)
     @GetMapping("/orders/customer")
    public ResponseDto<?> orderGetCustomerResponseListDto() {
         List<OrderGetResponseDto> customerOrders = orderService.getCustomerOrders();
         return new ResponseDto<>(1, "주문 목록 조회에 성공했습니다.", customerOrders);
     }

    // 주문 목록 조회 (Owner)
     @GetMapping("/orders/owner")
    public ResponseDto<?> orderGetOwnerResponseListDto() {
        List<OrderGetResponseDto> ownerOrders = orderService.getOwnerOrders();
        return new ResponseDto<>(1, "주문 목록 조회에 성공했습니다.", ownerOrders);
     }

    // 주문 상세 조회 (Customer)
    @GetMapping("/orders/customer/{orderId}")
    public ResponseDto<?> orderGetCusomterResponseDto(@PathVariable UUID orderId) {
        OrderGetResponseDto customerOrder = orderService.getOrder(orderId);
        return new ResponseDto<>(1, "주문 상세 조회에 성공했습니다.", customerOrder);
    }

    // 주문 상세 조회 (Owner)
    @GetMapping("/orders/owner/{orderId}")
    public ResponseDto<?> orderGetOwnerResponseDto(@PathVariable UUID orderId) {
        OrderGetResponseDto ownerOrder = orderService.getOrder(orderId);
        return new ResponseDto<>(1, "주문 상세 조회에 성공했습니다.", ownerOrder);
    }


    // 주문 취소 (Customer)
}
