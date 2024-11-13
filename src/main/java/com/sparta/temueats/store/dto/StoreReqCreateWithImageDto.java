package com.sparta.temueats.store.dto;

import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.store.entity.P_storeReq;
import com.sparta.temueats.store.entity.StoreReqState;
import com.sparta.temueats.store.util.GeoUtils;
import com.sparta.temueats.user.entity.P_user;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreReqCreateWithImageDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "가게 이름은 2자 이상 50자 이하로 입력해주세요.")
    private String name;

    private MultipartFile image;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Size(min = 10, max = 15, message = "전화번호는 최소 10자 이상, 15자 이하로 입력해주세요.")
    private String number;

    @NotNull(message = "최소 주문 금액은 필수입니다.")
    @Positive(message = "최소 주문 금액은 0보다 커야 합니다.")
    private Integer leastPrice;

    @NotNull(message = "배달비는 필수입니다.")
    @Positive(message = "배달비는 0보다 커야 합니다.")
    private Integer deliveryPrice;

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    private Double latitude;

    private Double longitude;

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 50, message = "주소는 최대 50자입니다.")
    private String address;

    public P_storeReq toEntity(P_user user, String image) {
        return P_storeReq.builder()
                .requestedBy(user)
                .name(name)
                .image(image)
                .number(number)
                .leastPrice(leastPrice)
                .deliveryPrice(deliveryPrice)
                .category(category)
                .latLng(GeoUtils.toPoint(latitude, longitude))
                .address(address)
                .state(StoreReqState.PENDING)
                .build();
    }

}
