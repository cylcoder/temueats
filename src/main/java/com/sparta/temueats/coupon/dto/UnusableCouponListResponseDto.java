package com.sparta.temueats.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sparta.temueats.coupon.entity.P_coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UnusableCouponListResponseDto {

    private UUID id;
    private String name;
    private int discountAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime usedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expiredAt;

    public UnusableCouponListResponseDto(P_coupon coupon) {
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.discountAmount = coupon.getDiscountAmount();
        this.usedAt = coupon.getUsedAt();
        this.expiredAt = coupon.getExpiredAt();
    }
}
