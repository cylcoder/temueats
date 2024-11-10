package com.sparta.temueats.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.util.Date;

@Entity(name="P_USER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class P_user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Size(min = 2, max = 30, message = "닉네임은 2자 이상, 50자 이하이어야 합니다.")
    private String nickname;

    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    @Temporal(TemporalType.DATE)
    private Date birth;

    @NotNull
    private boolean use_yn;

    @Size(max = 255, message = "이미지 URL은 최대 500자 이하이어야 합니다.")
    private String imageProfile;

    @NotNull
    private Point latLng;

    @Size(max = 50, message = "주소는 최대 50자 이하이어야 합니다.")
    private String address;

}
