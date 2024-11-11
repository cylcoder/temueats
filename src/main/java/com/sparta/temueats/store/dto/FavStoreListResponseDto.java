package com.sparta.temueats.store.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.StoreState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FavStoreListResponseDto {

    private UUID storeId;
    private String name;
    private String image;
    private StoreState state;
    private String category;
    private double lat;
    private double lng;
    private String address;

    public FavStoreListResponseDto(P_store store) {
        this.storeId = store.getStoreId();
        this.name = store.getName();
        this.image = store.getImage();
        this.state = store.getState();
        this.category = store.getCategory().toString();
        this.lat = store.getLatLng().getX();
        this.lng = store.getLatLng().getY();
        this.address = store.getAddress();
    }

}
