package com.sparta.temueats.cart.entity;

import com.sparta.temueats.cart.dto.CartUpdateRequestDto;
import com.sparta.temueats.order.entity.P_order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_CART")
public class P_cart {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID cartId;

    @Column(nullable = false)
    @Min(value = 1, message = "수량 입력은 최소 1이상 가능합니다.")
    @Max(value = 50, message = "수량 입력은 최대 50까지 가능합니다.")
    private Long quantity;

    @Column(nullable = false)
    private boolean paidYn;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private P_user user;

    @Column(name = "user_id")
    private Long userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "menu_id")
//    private P_menu menu;

    @Column(name = "menu_id")
    private UUID menuId;

    @Column(nullable = false)
    private boolean deleted_yn;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cart")
    private P_order order;


    public P_cart(CartUpdateRequestDto cartUpdateRequestDto, Long userId, UUID menuId) {
        this.quantity = cartUpdateRequestDto.getQuantity();
        this.paidYn = false;
        this.userId = userId;
        this.menuId = menuId;
    }

    public void update(CartUpdateRequestDto cartUpdateRequestDto) {
        this.quantity =cartUpdateRequestDto.getQuantity();
    }

    public void delete() {
        this.deleted_yn = true;
    }
}