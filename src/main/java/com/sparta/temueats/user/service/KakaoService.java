package com.sparta.temueats.user.service;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.security.util.JwtUtil;
import com.sparta.temueats.user.dto.KakaoUserDto;
import com.sparta.temueats.user.dto.LoginResponseDto;
import com.sparta.temueats.user.entity.P_user;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${spring.kakao.api.key}")
    private String kakaoApiKey;
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public ResponseEntity<ResponseDto> processKakaoLogin(String code) throws ParseException {

        System.out.println("processKakaoLogin entered");
        // 토큰 가져오기
        Map<String, String> tokens = getKakaoTokens(code);
        String kakaoAccessToken = tokens.get("access_token");
        String kakoRefreshToken = tokens.get("refresh_token");

        // 유저 정보 가져오기
        KakaoUserDto kakaoUserDto = getUserInfo(kakaoAccessToken);

        P_user user = userService.findByEmail(kakaoUserDto.getEmail());

        // 없을 경우 추가정보 입력 필요 응답
        if (user == null) {
            return ResponseEntity.ok(new ResponseDto(ResponseDto.SUCCESS, "신규회원, 추가정보 필요", kakaoUserDto));
        }
        // 로그인 처리
        String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        LoginResponseDto loginResponse = new LoginResponseDto(user.getEmail(), user.getNickname(), user.getRole().name());

        ResponseDto response = new ResponseDto(ResponseDto.SUCCESS, "로그인이 성공적으로 완료되었습니다", loginResponse);

        // 헤더, 쿠키 설정
        HttpHeaders headers = jwtUtil.createAccessTokenHeader(accessToken);
        headers.add(HttpHeaders.SET_COOKIE, jwtUtil.createRefreshTokenCookie(refreshToken).toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }

    private Map<String, String> getKakaoTokens(String code) {
        System.out.println("getKakaoTokens entered, " + code);
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);
        params.add("redirect_uri", "http://localhost:8080/api/members/auth/kakao-login");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        Map<String, Object> responseBody = response.getBody();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", (String) responseBody.get("access_token"));
        tokens.put("refresh_token", (String) responseBody.get("refresh_token"));

        return tokens;
    }

    // 엑세스 토큰으로 유저 정보 요청, 반환
    private KakaoUserDto getUserInfo(String accessToken) throws ParseException {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        Map<String, Object> responseBody = response.getBody();

        Date birth = null;

        Long kakaoId = ((Number) responseBody.get("id")).longValue();
        Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");
        String birthYear = ((String) responseBody.get("birthyear"));
        String birthday = ((String) responseBody.get("birthday"));
        if(birthday != null && birthYear != null){
            String birthString = birthYear + "-" + birthday.substring(0,2) + "-" + birthday.substring(2, 4);
            SimpleDateFormat dateFormat = new SimpleDateFormat(("yyyy-MM-dd"));
            birth = dateFormat.parse(birthString);
        }
        String phone = (String) kakaoAccount.get("phone_number");
        phone = phone.replaceAll("[^0-9]", "");
        if (phone.startsWith("82")) {
            phone = "0" + phone.substring(2);
        }

        // 원하는 형식으로 조합
        if (phone.length() == 11) {
            phone = phone.replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1-$2-$3");
        } else if (phone.length() == 10) {
            phone = phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
        } else {
            phone = null;
        }
        return new KakaoUserDto(kakaoId, email, nickname, profileImageUrl, birth, phone);
    }

}