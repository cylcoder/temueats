package com.sparta.temueats.payment.repository;

import com.sparta.temueats.payment.entity.P_payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<P_payment, UUID> {

}
