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
    public ResponseDto createCoupon(@RequestBody CouponRequestDto couponRequestDto, HttpServletRequest req) {

        return couponService.createCoupon(couponRequestDto, req);
    }

    @GetMapping("")
    public ResponseDto getCouponList(HttpServletRequest req) {

        return couponService.getCouponList(req);
    }

}
