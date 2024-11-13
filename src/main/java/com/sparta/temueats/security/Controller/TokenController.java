package com.sparta.temueats.security.Controller;

import com.sparta.temueats.security.util.JwtUtil;
import com.sparta.temueats.user.entity.UserRoleEnum;
import com.sparta.temueats.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
public class TokenController {


    private final JwtUtil jwtUtil;
    private final UserService userService;

    public TokenController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.getTokenFromHeader(request, "Refresh-Token");
        String email = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
        UserRoleEnum role = userService.findRoleByEmail(email);

        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {

            String newAccessToken = jwtUtil.createAccessToken(email, role.name());

            jwtUtil.addJwtToHeader(newAccessToken, response, "Authorization");
            return ResponseEntity.ok("New access token issued");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}