package com.sparta.temueats.store.dto;

import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.store.entity.P_storeReq;
import com.sparta.temueats.store.entity.StoreReqState;
import com.sparta.temueats.user.entity.P_user;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreReqDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "가게 이름은 2자 이상 50자 이하로 입력해주세요.")
    private String name;

    @Size(max = 500, message = "이미지 URL은 최대 500자입니다.")
    private String image;

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

    public StoreReqDto(P_storeReq storeReq) {
        this.name = storeReq.getName();
        this.image = storeReq.getImage();
        this.number = storeReq.getNumber();
        this.leastPrice = storeReq.getLeastPrice();
        this.deliveryPrice = storeReq.getDeliveryPrice();
        this.category = storeReq.getCategory();
        this.latitude = storeReq.getLatLng().getX();
        this.longitude = storeReq.getLatLng().getY();
        this.address = storeReq.getAddress();
    }

    public P_storeReq toEntity(P_user user) {
        return P_storeReq.builder()
                .requestedBy(user)
                .name(this.name)
                .image(this.image)
                .number(this.number)
                .leastPrice(this.leastPrice)
                .deliveryPrice(this.deliveryPrice)
                .category(this.category)
                .latLng(new Point(this.latitude, this.longitude))
                .address(this.address)
                .status(StoreReqState.PENDING)
                .build();
    }

}
