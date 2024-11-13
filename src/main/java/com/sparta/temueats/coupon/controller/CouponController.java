package com.sparta.temueats.coupon.controller;

import com.sparta.temueats.coupon.dto.CouponRequestDto;
import com.sparta.temueats.coupon.service.CouponService;
import com.sparta.temueats.global.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("")
    public ResponseDto createCoupon(@RequestBody CouponRequestDto couponRequestDto) {

        return couponService.createCoupon(couponRequestDto);
    }

    @GetMapping("")
    public ResponseDto getCouponList() {

        return couponService.getCouponList();
    }

}
