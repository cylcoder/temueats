package com.sparta.temueats.order.dto;

import com.sparta.temueats.cart.entity.P_cart;
import com.sparta.temueats.order.entity.OrderState;
import com.sparta.temueats.order.entity.P_order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderGetListResponseDto {
    private UUID orderId;
    private UUID orderUId;
    private Long amount;
    private boolean IsDelivery;
    private OrderState orderState;
    private Long discountPrice;
    private String customerRequest;
    private boolean cancelYn;
    private List<P_cart> cartList;
    private Long customerId;
    private Long ownerId;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private LocalDateTime deletedAt;
//    private String createdBy;
//    private String updatedBy;
//    private String deletedBy;
//    private boolean deletedYn;

    public OrderGetListResponseDto(P_order order) {
        this.orderId = order.getOrderId();
        this.orderUId = order.getOrderUId();
        this.amount = order.getAmount();
        this.IsDelivery = order.isIsDelivery();
        this.orderState = order.getOrderState();
        this.discountPrice = order.getDiscountPrice();
        this.customerRequest = order.getCustomerRequest();
        this.cancelYn = order.isCancelYn();
        this.cartList = order.getCartList();
        this.customerId = order.getCustomerId();
        this.ownerId = order.getOwnerId();
//        this.createdAt = order.getCreatedAt();
//        this.updatedAt = order.getUpdatedAt();
//        this.deletedAt = order.getDeletedAt();
//        this.createdBy = order.getCreatedBy();
//        this.updatedBy = order.getUpdatedBy();
//        this.deletedBy = order.getDeletedBy();
//        this.deletedYn = order.getDeletedYn();
    }

}
