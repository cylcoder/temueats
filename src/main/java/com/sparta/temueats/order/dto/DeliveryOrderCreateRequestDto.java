package com.sparta.temueats.order.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DeliveryOrderCreateRequestDto {

    @Size(max = 50, message = "요청 사항은 50글자 이내만 가능합니다.")
    private String customerRequest;

    private UUID couponId;

    public DeliveryOrderCreateRequestDto() {
    }

    @Builder
    public DeliveryOrderCreateRequestDto(String customerRequest, UUID couponId) {
        this.customerRequest = customerRequest;
        this.couponId = couponId;
    }

    @Builder
    public DeliveryOrderCreateRequestDto(String customerRequest) {
        this.customerRequest = customerRequest;
    }
}
