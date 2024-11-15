package com.sparta.temueats.user.entity;

import com.sparta.temueats.global.BaseEntity;
import com.sparta.temueats.user.dto.UpdateMypageRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;

@Entity(name="P_USER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class P_user extends BaseEntity {

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

    @NotBlank
    @Pattern(regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    @Column(nullable = false)
    private String phone;

    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    @Temporal(TemporalType.DATE)
    private LocalDate birth;

    @NotNull
    private boolean use_yn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role;

    @Size(max = 255, message = "이미지 URL은 최대 500자 이하이어야 합니다.")
    private String imageProfile;

    @NotNull
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point latLng;

    @Size(max = 50, message = "주소는 최대 50자 이하이어야 합니다.")
    private String address;

    @Column(unique = true)
    private Long kakaoId;  // 카카오 로그인 사용자를 구분할 ID

    @Column(nullable = false)
    private String socialProvider = "NONE"; // NONE, KAKAO 등


    public void updateUserInfo(UpdateMypageRequestDto request, GeometryFactory geometryFactory) {
        this.latLng = geometryFactory.createPoint(new Coordinate(request.getLng(), request.getLat()));
        this.address = request.getAddress();
        this.nickname = request.getNickname();
        this.phone = request.getPhone();
        this.imageProfile = request.getImageProfile();
    }

}
