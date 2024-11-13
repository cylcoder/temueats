package com.sparta.temueats.coupon.repository;

import com.sparta.temueats.coupon.entity.P_coupon;
import com.sparta.temueats.user.entity.P_user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<P_coupon, UUID> {

    List<P_coupon> findAllByOwnerAndStatus(P_user user, boolean b);

    List<P_coupon> findAllByStatusAndExpiredAtBefore(boolean b, LocalDate now);

    @Query("select c from P_COUPON c where c.order.orderId = :orderId")
    Optional<P_coupon> findCouponByOrderId(UUID orderId);
}
