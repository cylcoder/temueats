package com.sparta.temueats.store.util;

import com.sparta.temueats.user.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sparta.temueats.store.util.AuthUtils.AuthStatus.*;

@Component
public class AuthUtils {

    @Getter
    @AllArgsConstructor
    public enum AuthStatus {
        UNAUTHENTICATED("로그인하지 않은 사용자입니다."),
        UNAUTHORIZED("권한이 없습니다."),
        AUTHORIZED("권한이 있는 사용자입니다.");

        private final String msg;
    }

    public AuthStatus validate(List<UserRoleEnum> allowedAuthorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return UNAUTHENTICATED;
        }

        boolean hasAuthority = authentication.getAuthorities().stream()
                .anyMatch(userAuth -> allowedAuthorities.stream()
                        .anyMatch(allowedAuth -> userAuth.getAuthority().equals(allowedAuth.getAuthority())));

        if (!hasAuthority) {
            return UNAUTHORIZED;
        }

        return AUTHORIZED;
    }

}
