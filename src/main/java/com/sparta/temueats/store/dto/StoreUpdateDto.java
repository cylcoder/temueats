package com.sparta.temueats.store.dto;

import com.sparta.temueats.store.entity.StoreState;
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
    private StoreState storeState;

}
