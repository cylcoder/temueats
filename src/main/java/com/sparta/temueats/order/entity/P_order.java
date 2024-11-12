package com.sparta.temueats.order.entity;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_ORDER")
public class P_order extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID orderId;

    @Column(nullable = false)
    private UUID orderUId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private boolean IsDelivery;

    @Column(nullable = false)
    private OrderState orderState;

    @Column
    private int discountPrice;

    @Column
    @Size(max = 50, message = "요청 사항은 50글자 이내만 가능합니다.")
    private String customerRequest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private P_cart cart;


}
