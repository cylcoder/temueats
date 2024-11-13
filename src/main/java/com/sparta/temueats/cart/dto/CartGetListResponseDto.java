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
    private boolean selectYn;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private LocalDateTime deletedAt;
//    private String createdBy;
//    private String updatedBy;
//    private String deletedBy;
//    private boolean deletedYn;

    public CartGetListResponseDto(P_cart pCart) {
        this.cartId = pCart.getCartId();
        this.menuId = pCart.getMenu().getMenuId();
        this.quantity = pCart.getQuantity();
        this.selectYn = pCart.isSelectYn();
//        this.createdAt = pCart.getCreatedAt();
//        this.updatedAt = pCart.getUpdatedAt();
//        this.deletedAt = pCart.getDeletedAt();
//        this.createdBy = pCart.getCreatedBy();
//        this.updatedBy = pCart.getUpdatedBy();
//        this.deletedBy = pCart.getDeletedBy();
//        this.deletedYn = pCart.getDeletedYn();
    }
}
