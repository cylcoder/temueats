package com.sparta.temueats.coupon.service;

import com.sparta.temueats.coupon.dto.CouponListResponseDto;
import com.sparta.temueats.coupon.dto.CouponRequestDto;
import com.sparta.temueats.coupon.dto.UnusableCouponListResponseDto;
import com.sparta.temueats.coupon.dto.UsableCouponListResponseDto;
import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.coupon.repository.CouponRepository;
import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    private final UserService userService;

    public CouponService(CouponRepository couponRepository, UserService userService) {
        this.couponRepository = couponRepository;
        this.userService = userService;
    }

    // 쿠폰 발행
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
        P_user owner = userService.findUserById(couponRequestDto.getOwner());
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
    // 쿠폰 사용
    public void useCoupon(UUID couponId) {

        P_coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null) {
            throw new CustomApiException("해당하는 쿠폰이 없습니다");
        }
        if (coupon.getStatus()==false) {
            throw new CustomApiException("이미 사용되거나 만료된 쿠폰입니다");
        }
        // 사용 처리
        coupon.setStatus(false);
        couponRepository.save(coupon);

    }

    // 쿠폰 리스트 조회
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

    // 사용기한 지난 쿠폰 만료 처리
    @Scheduled(cron = "0 0 0 * * ?")
    public void expireCoupon() {

        List<P_coupon> expiredCoupons = couponRepository.findAllByStatusAndExpiredAtBefore(true, LocalDate.now());

        expiredCoupons.forEach(coupon -> coupon.setStatus(false));

        couponRepository.saveAll(expiredCoupons);
    }
}
