package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.member.KakaoProfileDto;
import com.golfzonTech4.worktalk.dto.member.TokenDto;
import com.golfzonTech4.worktalk.jwt.JwtFilter;
import com.golfzonTech4.worktalk.jwt.TokenProvider;
import com.golfzonTech4.worktalk.service.KakaoJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final KakaoJoinService oAuthService;

//    @PostMapping("/kakaoJoin") // 카카오 간편회원 아닐 경우 -> 회원가입
//    public ResponseEntity<TokenDto> kakaoJoin(@Valid @RequestBody OAuth2KakaoDto dto) {
//        log.info("OAuth2KakaoDto : {}", dto);
//
//        String jwt = oAuthService.join(dto);
//
//        HttpHeaders httpHeaders = new HttpHeaders(); // response header 저장
//        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//
//        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK); // response body에도 저장
//    }

    @PostMapping("/kakaoLogin")
    public ResponseEntity<TokenDto> kakaoLogin(@Valid @RequestBody KakaoProfileDto dto) {
        log.info("OAuth2KakaoDto : {}", dto);

        if(dto.isMember() == false) oAuthService.join(dto);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPw()); // 토큰 생성

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication); // Authentication Token => context 저장

        String jwt = tokenProvider.createToken(authentication); // Jwt 토큰 생성

        HttpHeaders httpHeaders = new HttpHeaders(); // response header 저장
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK); // response body에도 저장
    }


}
