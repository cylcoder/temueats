package com.sparta.temueats.store.dto;

import com.sparta.temueats.store.entity.StoreReqState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreReqUpdateDto {

    private UUID storeReqId;
    private StoreReqState storeReqState;

}
