package com.sparta.temueats.menu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AiReqDto {

    @NotBlank(message = "요청 메시지는 필수입니다.")
    private String request;

}
