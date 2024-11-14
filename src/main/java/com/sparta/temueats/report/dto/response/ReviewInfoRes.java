package com.sparta.temueats.report.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewInfoRes {
    private String nickname;
    private LocalDateTime createdAt;

    @Builder
    public ReviewInfoRes(String nickname, LocalDateTime createdAt) {
        this.nickname = nickname;
        this.createdAt = createdAt;
    }
}
