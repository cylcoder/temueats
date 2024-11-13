package com.sparta.temueats.menu.dto;

import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MenuCreateWithImageDto {

    private UUID storeId;

    @NotEmpty(message = "메뉴 이름은 필수입니다.")
    private String name;

    private String description;

    @NotNull(message = "가격은 필수입니다.")
    @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    private MultipartFile image;

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    private SellState sellState;

    @NotNull(message = "대표메뉴 여부는 필수입니다.")
    private Boolean signatureYn;

    public P_menu toEntity(P_store store, String image) {
        return P_menu.builder()
                .store(store)
                .name(name)
                .description(description)
                .price(price)
                .image(image)
                .category(category)
                .signatureYn(signatureYn)
                .sellState(SellState.SALE)
                .build();
    }

}
