package com.sparta.temueats.user.dto;

import com.sparta.temueats.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequestDto {

    private Long id; // 권한 수정 받을 사용자 id
    private UserRoleEnum role;
}
