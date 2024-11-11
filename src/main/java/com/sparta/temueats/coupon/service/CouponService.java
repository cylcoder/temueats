package com.sparta.temueats.coupon.service;

import com.sparta.temueats.coupon.dto.CouponListResponseDto;
import com.sparta.temueats.coupon.dto.CouponRequestDto;
import com.sparta.temueats.coupon.dto.UnusableCouponListResponseDto;
import com.sparta.temueats.coupon.dto.UsableCouponListResponseDto;
import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.coupon.repository.CouponRepository;
import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    private final UserService userService;

    public CouponService(CouponRepository couponRepository, UserService userService) {
        this.couponRepository = couponRepository;
        this.userService = userService;
    }

    public ResponseDto createCoupon(CouponRequestDto couponRequestDto, HttpServletRequest req) {

        // 사용자 검증
        P_user issuer = userService.validateTokenAndGetUser(req).orElse(null);
        if (issuer == null) {
            return new ResponseDto<>(-1, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다", null);
        }
        // 권한 검증
        if(issuer.getRole() == UserRoleEnum.CUSTOMER || issuer.getRole() == UserRoleEnum.OWNER) {
            return new ResponseDto<>(-1, "권한이 없는 사용자입니다", null);
        }
        // 수신자 검증
        P_user owner = userService.getUserById(couponRequestDto.getOwner());
        if (owner == null) {
            return new ResponseDto<>(-1, "유효하지 않은 수신자입니다", null);
        }
        // 만료 기한(6개월)
        LocalDate expiredDate = LocalDate.now().plusMonths(6);

        // 쿠폰 정보 생성
        int discountAmount = couponRequestDto.getDiscountAmount();
        String name = couponRequestDto.getName();
        int quantity = couponRequestDto.getQuantity();

        for (int i = 0; i < quantity; i++) {
            P_coupon coupon = P_coupon.builder()
                    .discountAmount(discountAmount)
                    .name(name)
                    .expiredAt(expiredDate)
                    .issuer(issuer)
                    .owner(owner)
                    .status(true)
                    .build();

            // 쿠폰을 DB에 저장
            couponRepository.save(coupon);
        }

        return new ResponseDto<>(1, "쿠폰 생성 완료", null);
    }

    public ResponseDto getCouponList(HttpServletRequest req) {
        // 사용자 조회
        P_user user = userService.validateTokenAndGetUser(req).orElse(null);
        if (user == null) {
            return new ResponseDto<>(-1, "유효하지 않은 토큰이거나 존재하지 않는 사용자입니다", null);
        }
        // 사용 가능 쿠폰 조회
        List<P_coupon> useableCouponList = couponRepository.findAllByOwnerAndStatus(user, true);
        List<UsableCouponListResponseDto> usable = useableCouponList.stream()
                .map(UsableCouponListResponseDto::new)
                .toList();
        // 사용되거나 만료된 쿠폰 조회
        List<P_coupon> unusableCouponList = couponRepository.findAllByOwnerAndStatus(user, false);
        List<UnusableCouponListResponseDto> unusable = unusableCouponList.stream()
                .map(UnusableCouponListResponseDto::new)
                .toList();

        return new ResponseDto<>(1, "쿠폰리스트", new CouponListResponseDto(usable, unusable));

    }
}
