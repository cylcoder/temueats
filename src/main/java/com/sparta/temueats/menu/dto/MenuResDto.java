package com.sparta.temueats.menu.dto;

import com.sparta.temueats.menu.entity.Category;
import com.sparta.temueats.menu.entity.P_menu;
import com.sparta.temueats.store.entity.SellState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MenuResDto {

    private UUID menuId;
    private String name;
    private String description;
    private Integer price;
    private String image;
    private Category category;
    private SellState sellState;
    private Boolean signatureYn;

    public MenuResDto(P_menu pMenu) {
        this.menuId = pMenu.getMenuId();
        this.name = pMenu.getName();
        this.description = pMenu.getDescription();
        this.price = pMenu.getPrice();
        this.image = pMenu.getImage();
        this.category = pMenu.getCategory();
        this.sellState = pMenu.getSellState();
        this.signatureYn = pMenu.getSignatureYn();
    }

}