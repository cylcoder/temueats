package com.sparta.temueats.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@Getter
@ToString
public class CreateUserRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    private String phone;

    private Date birth;

    private String imageProfile;

    @NotNull
    private float lat;

    @NotNull
    private float lng;

    @NotBlank
    private String address;


}
