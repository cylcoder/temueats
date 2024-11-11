package com.sparta.temueats.cart.dto;

import com.sparta.temueats.cart.entity.P_cart;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class CartGetListResponseDto {
    private UUID cartId;
    private UUID menuId;
    private Long quantity;
    private boolean paid_yn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String createdBy;
    private String updatedBy;
    private String deletedBy;
    private boolean deletedYn;

    public CartGetListResponseDto(P_cart pCart) {
        this.cartId = pCart.getCartId();
        this.menuId = pCart.getMenuId();
        this.quantity = pCart.getQuantity();
        this.paid_yn = pCart.isPaidYn();
        // todo BaseEntity pull 받으면 수정할 예정
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.deletedAt = deletedAt;
//        this.createdBy = createdBy;
//        this.updatedBy = updatedBy;
//        this.deletedBy = deletedBy;
//        this.deletedYn = deletedYn;
    }
}
