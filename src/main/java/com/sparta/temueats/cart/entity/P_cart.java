package com.sparta.temueats.cart.entity;

import com.sparta.temueats.cart.dto.CartUpdateRequestDto;
import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.order.entity.P_order;
import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_CART")
public class P_cart extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID cartId;

    @Column(nullable = false)
    @Min(value = 1, message = "수량 입력은 최소 1이상 가능합니다.")
    @Max(value = 50, message = "수량 입력은 최대 50까지 가능합니다.")
    private Long quantity;

    @Column(nullable = false)
    private boolean selectYn;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private P_user user;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id_1")
    private P_menu menu;

    @Column(name = "menu_id")
    private UUID menuId;

    @Column(nullable = false)
    private boolean deletedYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private P_order order;

    @Builder
    public P_cart(Long quantity, boolean selectYn, Long userId, UUID menuId, boolean deletedYn) {
        this.quantity = quantity;
        this.selectYn = selectYn;
        this.userId = userId;
        this.menuId = menuId;
        this.deletedYn = deletedYn;
    }

    public void update(CartUpdateRequestDto cartUpdateRequestDto) {
        this.quantity =cartUpdateRequestDto.getQuantity();
    }

    public void delete() {
        this.deletedYn = true;
    }

    public void changeSelect() {
        this.selectYn = !selectYn;
    }
}