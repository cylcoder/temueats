package com.sparta.temueats.menu.dto;

import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.store.entity.P_store;
import com.sparta.temueats.store.entity.SellState;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MenuUpdateDto {

    private UUID menuId;

    private String name;

    private String description;

    @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    private String image;

    private Category category;

    private SellState sellState;

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
