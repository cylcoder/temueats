package com.sparta.temueats.menu.dto;

import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MenuCreateDto {

    @NotEmpty(message = "메뉴 이름은 필수입니다.")
    private String name;

    private UUID storeId;

    private String description;

    @NotNull(message = "가격은 필수입니다.")
    @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    private String image;

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    @NotNull(message = "대표메뉴 여부는 필수입니다.")
    private Boolean signatureYn;

    public P_menu toEntity(P_store store) {
        return P_menu.builder()
                .store(store)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .image(this.image)
                .category(this.category)
                .signatureYn(this.signatureYn)
                .sellState(SellState.SALE)
                .build();
    }

}
