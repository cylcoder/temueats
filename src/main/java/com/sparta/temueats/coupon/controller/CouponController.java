package com.sparta.temueats.coupon.controller;

import com.sparta.temueats.coupon.dto.CouponRequestDto;
import com.sparta.temueats.coupon.service.CouponService;
import com.sparta.temueats.global.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name="쿠폰 관리")
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @Operation(summary = "쿠폰 발행")
    @PostMapping("")
    public ResponseDto createCoupon(@RequestBody CouponRequestDto couponRequestDto) {

        return couponService.createCoupon(couponRequestDto);
    }

    @Operation(summary = "쿠폰 목록 조회")
    @GetMapping("")
    public ResponseDto getCouponList() {

        return couponService.getCouponList();
    }

}
