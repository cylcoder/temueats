package com.sparta.temueats.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.Date;

@Entity(name="P_USER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class P_user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Size(min = 2, max = 30, message = "닉네임은 2자 이상, 50자 이하이어야 합니다.")
    private String nickname;

    @NotBlank()
    @Pattern(regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    @Column(nullable = false)
    private String phone;

    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    @Temporal(TemporalType.DATE)
    private Date birth;

    @NotNull
    private boolean use_yn;

    @Size(max = 255, message = "이미지 URL은 최대 500자 이하이어야 합니다.")
    private String imageProfile;

    @NotNull
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point latLng;

    @Size(max = 50, message = "주소는 최대 50자 이하이어야 합니다.")
    private String address;

}
