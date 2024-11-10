package com.sparta.temueats.cart.dto;

import com.sparta.temueats.cart.entity.P_cart;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CartAddResponseDto {
    private UUID cartId;
    private UUID menuId;
    private Long quantity;
    private boolean paidYn;

    public CartAddResponseDto(P_cart pCart) {
        this.cartId = pCart.getCartId();
        this.menuId = pCart.getMenuId();
        this.quantity = pCart.getQuantity();
        this.paidYn = false;
    }
}
