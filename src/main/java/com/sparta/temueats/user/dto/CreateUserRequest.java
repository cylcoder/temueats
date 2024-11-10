package com.sparta.temueats.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@Getter
@ToString
public class CreateUserRequest {

    private String email;

    private String password;

    private String nickname;

    private String phone;

    private Date birth;

    private String imageProfile;

    private float lat;

    private float lng;

    private String address;


}
