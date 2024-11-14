package com.sparta.temueats.order.repository;

import com.sparta.temueats.order.entity.OrderState;
import com.sparta.temueats.order.entity.P_order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<P_order, UUID> {

    @Query("select o from P_order o where o.customerId = :userId AND o.deletedYn = false")
    List<P_order> findAllByCustomerId(Long userId);

    @Query("select o from P_order o where o.ownerId = :userId AND o.deletedYn = false")
    List<P_order> findAllByOwnerId(Long userId);


    @Query("select o from P_order o where o.customerId = :userId OR o.ownerId = :userId")
    P_order findByUserId(Long userId);


    // paymentId로 P_order를 찾는 JPQL 쿼리 작성
    @Query("SELECT o FROM P_order o WHERE o.payment.paymentId = :paymentId")
    Optional<P_order> findByPaymentId(UUID paymentId);

    @Query("select o from P_order o where o.orderState = :state AND (o.customerId = :userId OR o.ownerId = :userId)")
    List<P_order> findAllByUserIdIsIng(Long userId, OrderState state);

}
