package com.sparta.temueats.cart.dto;

import com.sparta.temueats.cart.entity.P_cart;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CartUpdateResponseDto {
    private UUID cartId;
    private UUID menuId;
    private Long quantity;

    public CartUpdateResponseDto(P_cart pCart) {
        this.cartId = pCart.getCartId();
        this.menuId = pCart.getMenuId();
        this.quantity = pCart.getQuantity();
    }
}
