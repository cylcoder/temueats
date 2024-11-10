package com.sparta.temueats.store.dto;

import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.store.entity.P_storeReq;
import com.sparta.temueats.store.entity.StoreReqState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreResDto {

    private UUID storeReqId;
    private String name;
    private String image;
    private String number;
    private Integer leastPrice;
    private Integer deliveryPrice;
    private Category category;
    private Double latitude;
    private Double longitude;
    private String address;
    private StoreReqState status;

    public StoreResDto(P_storeReq storeReq) {
        this.storeReqId = storeReq.getStoreReqId();
        this.name = storeReq.getName();
        this.image = storeReq.getImage();
        this.number = storeReq.getNumber();
        this.leastPrice = storeReq.getLeastPrice();
        this.deliveryPrice = storeReq.getDeliveryPrice();
        this.category = storeReq.getCategory();
        this.latitude = storeReq.getLatLng().getX();
        this.longitude = storeReq.getLatLng().getY();
        this.address = storeReq.getAddress();
        this.status = storeReq.getState();
    }

}
