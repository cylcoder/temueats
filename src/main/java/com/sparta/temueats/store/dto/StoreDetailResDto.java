package com.sparta.temueats.store.dto;

import com.sparta.temueats.menu.dto.MenuResDto;
import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.StoreState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreDetailResDto {

    private String storeId;
    private String name;
    private String image;
    private String number;
    private StoreState state;
    private Integer leastPrice;
    private Integer deliveryPrice;
    private Category category;
    private Double latitude;
    private Double longitude;
    private String address;
    private Double rating;
    private Integer reviewCount;
    private Boolean isFavorite;
    private List<MenuResDto> menu;

    public StoreDetailResDto(P_store store) {
        storeId = store.getStoreId().toString();
        name = store.getName();
        image = store.getImage();
        number = store.getNumber();
        state = store.getState();
        leastPrice = store.getLeastPrice();
        deliveryPrice = store.getDeliveryPrice();
        category = store.getCategory();
        latitude = store.getLatLng().getX();
        longitude = store.getLatLng().getY();
        address = store.getAddress();
    }

    public StoreDetailResDto(P_store store, Double rating, Integer reviewCount, Boolean isFavorite) {
        storeId = store.getStoreId().toString();
        name = store.getName();
        image = store.getImage();
        number = store.getNumber();
        state = store.getState();
        leastPrice = store.getLeastPrice();
        deliveryPrice = store.getDeliveryPrice();
        category = store.getCategory();
        latitude = store.getLatLng().getX();
        longitude = store.getLatLng().getY();
        address = store.getAddress();
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.isFavorite = isFavorite;
    }

}
