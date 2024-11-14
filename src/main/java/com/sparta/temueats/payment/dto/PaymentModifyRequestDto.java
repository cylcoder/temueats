package com.sparta.temueats.payment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class PaymentModifyRequestDto {

    @Min(0)
    @Max(1)
    private int paymentStatus;

}
