package com.sparta.temueats.store.util;

import com.sparta.temueats.user.entity.P_user;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    public P_user createMockUser() {
        String nickname = "user" + (System.currentTimeMillis() % 1000);
        String email = nickname + "@temueats.com";

        P_user user = P_user.builder()
                .email(email)
                .password("password")
                .nickname(nickname)
                .phone("010-1234-5678")
                .birth(new Date(100, Calendar.JANUARY, 1))
                .use_yn(true)
                .role(UserRoleEnum.CUSTOMER)
                .imageProfile("https://s3.com/john.jpg")
                .latLng(GeoUtils.toPoint(126.978, 37.5665))
                .address("Hotel Casa Amsterdam")
                .build();

        return userRepository.save(user);
    }

}
