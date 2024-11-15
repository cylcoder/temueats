package com.sparta.temueats.coupon.service;

import com.sparta.temueats.coupon.dto.CouponRequestDto;
import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.coupon.repository.CouponRepository;
import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    void createCoupon_NoPermission() {
        // Given
        CouponRequestDto request = new CouponRequestDto(1L, 1000, "쿠폰 이름", 2);
        P_user issuer = new P_user();
        issuer.setRole(UserRoleEnum.CUSTOMER);
        when(userService.getUser()).thenReturn(issuer);

        // When
        ResponseDto response = couponService.createCoupon(request);

        // Then
        assertEquals(-1, response.getCode());
        assertEquals("권한이 없는 사용자입니다", response.getMsg());

    }

    @Test
    void createCoupon_NotExistUser() {
        // Given
        CouponRequestDto request = new CouponRequestDto(13L, 1000, "쿠폰 이름", 2);
        P_user issuer = new P_user();
        issuer.setRole(UserRoleEnum.MASTER);
        when(userService.getUser()).thenReturn(issuer);
        when(userService.findUserById(request.getOwner())).thenReturn(null);

        // When
        ResponseDto response = couponService.createCoupon(request);

        // Then
        assertEquals(-1, response.getCode());
        assertEquals("유효하지 않은 수신자입니다", response.getMsg());
    }

    @Test
    void createCoupon_Success() {
        // Given
        CouponRequestDto request = new CouponRequestDto(13L, 1000, "쿠폰 이름", 2);
        P_user issuer = new P_user();
        issuer.setRole(UserRoleEnum.MASTER);

        P_user owner = new P_user();
        owner.setId(2L);

        when(userService.getUser()).thenReturn(issuer);
        when(userService.findUserById(request.getOwner())).thenReturn(owner);

        when(couponRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        // When
        ResponseDto response = couponService.createCoupon(request);

        // Then
        assertEquals(1, response.getCode());
        assertEquals("쿠폰 생성 완료", response.getMsg());

        // Verify
        verify(couponRepository).saveAll(argThat(coupons -> {
            if (!(coupons instanceof Collection)) return false;
            Collection<P_coupon> couponCollection = (Collection<P_coupon>) coupons;
            if (couponCollection.size() != request.getQuantity()) return false;
            for (P_coupon coupon : coupons) {
                if (!coupon.getName().equals(request.getName())) return false;
                if (coupon.getDiscountAmount() != request.getDiscountAmount()) return false;
                if (!coupon.getIssuer().equals(issuer)) return false;
                if (!coupon.getOwner().equals(owner)) return false;
                if (!coupon.getExpiredAt().equals(LocalDate.now().plusMonths(6))) return false;
                if (!coupon.getStatus()) return false;
            }
            return true;
        }));
    }
}