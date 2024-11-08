package com.sparta.temueats.coupon.repository;

import com.sparta.temueats.coupon.entity.P_coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponRepository extends JpaRepository<P_coupon, UUID> {

}
