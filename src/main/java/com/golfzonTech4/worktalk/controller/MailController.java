package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.member.MemberMailDto;
import com.golfzonTech4.worktalk.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MailController {
    private final MailService mailService;

    /**
     * 인증메일 발송 요청
     */
    @PostMapping("/mailCheck")
    public ResponseEntity<String> mailCheck(@RequestBody MemberMailDto request) {
        log.info("mailCheck: {}", request);
        String validCode = mailService.joinMail(request.getEmail());
        return ResponseEntity.ok(validCode);
    }

}
