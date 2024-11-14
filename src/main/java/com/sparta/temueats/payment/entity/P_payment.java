package com.sparta.temueats.payment.entity;


import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.order.entity.P_order;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_PAYMENT")
public class P_payment extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID paymentID;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private Long price;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private P_order order;

    @Builder
    public P_payment(PaymentStatus paymentStatus, Long price, P_order order) {
        this.paymentStatus = paymentStatus;
        this.price = price;
        this.order = order;
    }
}
