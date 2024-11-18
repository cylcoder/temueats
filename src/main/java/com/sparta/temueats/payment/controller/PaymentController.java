package com.sparta.temueats.payment.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.payment.dto.PaymentGetResponseDto;
import com.sparta.temueats.payment.dto.PaymentModifyRequestDto;
import com.sparta.temueats.payment.service.PaymentService;
import com.sparta.temueats.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="결제 생성/조회/수정")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 생성")
    @PostMapping("/payments")
    public ResponseDto<?> createPaymentResponseDto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        paymentService.createPayments(userDetails.getUser());
        return new ResponseDto<>(1, "결제 생성이 완료되었습니다.", null);
    }

    @Operation(summary = "결제 목록 조회")
    @GetMapping("/payments")
    public ResponseDto<?> getPaymentResponseDto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PaymentGetResponseDto> paymentGetResponseDtoList = paymentService.getPayments(userDetails.getUser());
        return new ResponseDto<>(1, "결제 목록 조회에 성공했습니다.", paymentGetResponseDtoList);
    }

    @Operation(summary = "결제 상태 변경")
    @PutMapping("/payments/{paymentId}")
    public ResponseDto<?> modifyPaymentResponseDto(@RequestBody PaymentModifyRequestDto paymentmodifyRequestDto,
                                                   @PathVariable UUID paymentId) {
        paymentService.modifyPayment(paymentmodifyRequestDto, paymentId);
        return new ResponseDto<>(1, "결제가 완료되었습니다.", null);
    }
}
