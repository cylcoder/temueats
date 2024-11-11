package com.sparta.temueats.store.dto;

import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.StoreState;
import lombok.*;
import org.springframework.data.geo.Point;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreResDto {

    private String storeId;
    private String name;
    private String image;
    private String number;
    private StoreState state;
    private Integer leastPrice;
    private Integer deliveryPrice;
    private Category category;
    private Point latLng;
    private String address;
    private Double rating;
    private Integer reviewCount;

    public StoreResDto(P_store store, Double rating, Integer reviewCount) {
        storeId = store.getStoreId().toString();
        name = store.getName();
        image = store.getImage();
        number = store.getNumber();
        state = store.getState();
        leastPrice = store.getLeastPrice();
        deliveryPrice = store.getDeliveryPrice();
        category = store.getCategory();
        latLng = store.getLatLng();
        address = store.getAddress();
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

}
