package com.sparta.temueats.report.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreInfoRes {
    private String storeName;
    private String content;

    @Builder
    public StoreInfoRes(String storeName, String content) {
        this.storeName = storeName;
        this.content = content;
    }
}
