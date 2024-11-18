package com.sparta.temueats.order.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.order.dto.DeliveryOrderCreateRequestDto;
import com.sparta.temueats.order.dto.OrderGetResponseDto;
import com.sparta.temueats.order.dto.TakeOutOrderCreateRequestDto;
import com.sparta.temueats.order.service.OrderCustomerService;
import com.sparta.temueats.order.service.OrderOwnerService;
import com.sparta.temueats.order.service.OrderService;
import com.sparta.temueats.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="주문 생성/조회/수정/삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final OrderCustomerService orderCustomerService;
    private final OrderOwnerService orderOwnerService;

    @Operation(summary = "주문 생성 (CUSTOMER)")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MASTER')")
    @PostMapping("/orders/customer")
    public ResponseDto<?> orderCreateResponseCustomerDto(@RequestBody @Valid DeliveryOrderCreateRequestDto deliveryOrderCreateRequestDto, BindingResult bindingResult,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderCustomerService.createDeliveryOrders(deliveryOrderCreateRequestDto, userDetails.getUser());
        return new ResponseDto<>(1, "주문이 정상적으로 생성되었습니다.", null);
    }

    @Operation(summary = "주문 생성 (OWNER)")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER')")
    @PostMapping("/orders/owner")
    public ResponseDto<?> orderCreateResponseOwnerDto(@RequestBody @Valid TakeOutOrderCreateRequestDto takeOutOrderCreateRequestDto, BindingResult bindingResult,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderOwnerService.createTakeOutOrders(takeOutOrderCreateRequestDto, userDetails.getUser());
        return new ResponseDto<>(1, "주문이 정상적으로 생성되었습니다.", null);
    }

    @Operation(summary = "주문 목록 조회 (CUSTOMER)")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MASTER')")
     @GetMapping("/orders/customer")
    public ResponseDto<?> orderGetCustomerResponseListDto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
         List<OrderGetResponseDto> customerOrders = orderCustomerService.getCustomerOrders(userDetails.getUser());
         return new ResponseDto<>(1, "주문 목록 조회에 성공했습니다.", customerOrders);
     }

    @Operation(summary = "주문 목록 조회 (OWNER)")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER')")
     @GetMapping("/orders/owner")
    public ResponseDto<?> orderGetOwnerResponseListDto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderGetResponseDto> ownerOrders = orderOwnerService.getOwnerOrders(userDetails.getUser());
        return new ResponseDto<>(1, "주문 목록 조회에 성공했습니다.", ownerOrders);
     }

    @Operation(summary = "주문 상세 조회 (CUSTOMER)")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MASTER')")
    @GetMapping("/orders/customer/{orderId}")
    public ResponseDto<?> orderGetCustomerResponseDto(@PathVariable UUID orderId) {
        OrderGetResponseDto customerOrder = orderService.getOrder(orderId);
        return new ResponseDto<>(1, "주문 상세 조회에 성공했습니다.", customerOrder);
    }

    @Operation(summary = "주문 상세 조회 (OWNER)")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER')")
    @GetMapping("/orders/owner/{orderId}")
    public ResponseDto<?> orderGetOwnerResponseDto(@PathVariable UUID orderId) {
        OrderGetResponseDto ownerOrder = orderService.getOrder(orderId);
        return new ResponseDto<>(1, "주문 상세 조회에 성공했습니다.", ownerOrder);
    }


    @Operation(summary = "주문 취소 (CUSTOMER)")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MASTER')")
    @PutMapping("/orders/{orderId}/customer")
    public ResponseDto<?> orderCancelCustomerResponseDto(@PathVariable UUID orderId) {
        orderCustomerService.cancelCustomerOrder(orderId);
        return new ResponseDto<>(1, "주문이 취소되었습니다.", null);
    }

    @Operation(summary = "주문 취소 (OWNER)")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER')")
    @PutMapping("/orders/{orderId}/owner")
    public ResponseDto<?> orderCancelOwnerResponseDto(@PathVariable UUID orderId) {
        orderOwnerService.cancelOwnerOrder(orderId);
        return new ResponseDto<>(1, "주문이 취소되었습니다.", null);
    }

}
