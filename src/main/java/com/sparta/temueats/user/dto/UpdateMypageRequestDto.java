package com.sparta.temueats.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateMypageRequestDto {

    private double lat;
    private double lng;
    private String address;
    private String nickname;
    private String phone;
    private String imageProfile;
}
