package com.golfzonTech4.worktalk.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.golfzonTech4.worktalk.dto.member.TokenDto;
import com.golfzonTech4.worktalk.jwt.JwtFilter;
import com.golfzonTech4.worktalk.jwt.TokenProvider;
import com.golfzonTech4.worktalk.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
//
@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/user/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // 카카오 서버로부터 받은 인가 코드, JWT 토큰
        String jwt = kakaoLoginService.kakaoLogin(code);
        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create("http://3.36.148.54/login?token="+jwt));
        headers.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        log.info("headers:{}",headers);
        log.info("jwt:{}",jwt);
        return new ResponseEntity<>(new TokenDto(jwt), headers, HttpStatus.FOUND);
    }

}
