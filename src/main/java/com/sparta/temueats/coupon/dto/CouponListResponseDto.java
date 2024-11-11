package com.sparta.temueats.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CouponListResponseDto {

    private List<UsableCouponListResponseDto> usable;
    private List<UnusableCouponListResponseDto> unusable;

}
