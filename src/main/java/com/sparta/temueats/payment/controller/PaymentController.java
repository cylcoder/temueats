package com.sparta.temueats.payment.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.payment.dto.PaymentGetResponseDto;
import com.sparta.temueats.payment.service.PaymentService;
import com.sparta.temueats.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseDto<?> createPaymentResponseDto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        paymentService.createPayments(userDetails.getUser());
        return new ResponseDto<>(1, "결제 생성이 완료되었습니다.", null);
    }

    @GetMapping("/payments")
    public ResponseDto<?> getPaymentResponseDto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        PaymentGetResponseDto paymentGetResponseDto = paymentService.getPayments(userDetails.getUser());
        return new ResponseDto<>(1, "결제 조회에 성공했습니다.", paymentGetResponseDto);
    }
}
