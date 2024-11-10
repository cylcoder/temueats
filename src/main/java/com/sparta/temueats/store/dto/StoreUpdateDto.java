package com.sparta.temueats.store.dto;

import com.sparta.temueats.store.entity.StoreState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreUpdateDto {

    private UUID storeId;

    @Size(max = 500, message = "이미지 URL은 최대 500자입니다.")
    private String image;

    private Double latitude;

    private Double longitude;

    @Size(max = 50, message = "주소는 최대 50자입니다.")
    private String address;

    @Positive(message = "최소 주문 금액은 0보다 커야 합니다.")
    private Integer leastPrice;

    @Positive(message = "배달비는 0보다 커야 합니다.")
    private Integer deliveryPrice;

    private StoreState storeState;

}
