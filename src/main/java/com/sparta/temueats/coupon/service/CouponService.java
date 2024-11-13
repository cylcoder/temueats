package com.sparta.temueats.coupon.service;

import com.sparta.temueats.coupon.dto.CouponListResponseDto;
import com.sparta.temueats.coupon.dto.CouponRequestDto;
import com.sparta.temueats.coupon.dto.UnusableCouponListResponseDto;
import com.sparta.temueats.coupon.dto.UsableCouponListResponseDto;
import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.coupon.repository.CouponRepository;
import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public ResponseDto createCoupon(CouponRequestDto couponRequestDto) {

        // 사용자 검증
        P_user issuer = userService.getUser();
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

        // 쿠폰 리스트 생성
        List<P_coupon> coupons = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            P_coupon coupon = P_coupon.builder()
                    .discountAmount(discountAmount)
                    .name(name)
                    .expiredAt(expiredDate)
                    .issuer(issuer)
                    .owner(owner)
                    .status(true)
                    .build();
            coupons.add(coupon);
        }

        // 쿠폰 리스트를 한 번에 저장
        couponRepository.saveAll(coupons);

        return new ResponseDto<>(1, "쿠폰 생성 완료", null);
    }

    // 쿠폰 조회
    public P_coupon findCouponById(UUID couponId) {

        P_coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null) {
            throw new CustomApiException("해당하는 쿠폰이 없습니다");
        }
        if (!coupon.getStatus()) {
            throw new CustomApiException("이미 사용되거나 만료된 쿠폰입니다");
        }
        return coupon;
    }

    // 쿠폰 사용
    @Transactional
    public void useCoupon(UUID couponId, P_order order) {

        P_coupon coupon = findCouponById(couponId);

        // 사용 처리
        coupon.setStatus(false);
        coupon.setUsedAt(LocalDateTime.now());
        coupon.setOrder(order);
        couponRepository.save(coupon);

    }

    // 쿠폰 사용 취소
    @Transactional
    public void cancelCoupon(UUID couponId) {

        P_coupon coupon = findCouponById(couponId);

        coupon.setStatus(true);
        coupon.setUsedAt(null);
        coupon.setOrder(null);
        couponRepository.save(coupon);
    }

    // 쿠폰 리스트 조회
    public ResponseDto getCouponList() {

        P_user user = userService.getUser();
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
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void expireCoupon() {

        List<P_coupon> expiredCoupons = couponRepository.findAllByStatusAndExpiredAtBefore(true, LocalDate.now());

        expiredCoupons.forEach(coupon -> coupon.setStatus(false));

        couponRepository.saveAll(expiredCoupons);
    }
}
