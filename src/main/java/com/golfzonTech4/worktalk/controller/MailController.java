package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.member.MemberDto;
import com.golfzonTech4.worktalk.dto.member.MemberMailDto;
import com.golfzonTech4.worktalk.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MailController", description = "메일 관련 api입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MailController {
    private final MailService mailService;

    /**
     * 인증메일 발송 요청
     */
    @Operation(summary = "인증메일 발송 요청", description = "입력된 정보로 중복 여부를 확인 후 인증코드가 담긴 메일을 발송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "이미 사용 중인 이메일입니다.")
    })
    @PostMapping("/mailCheck")
    public ResponseEntity<String> mailCheck(@RequestBody MemberMailDto request) {
        log.info("mailCheck: {}", request);
        String validCode = mailService.joinMail(request.getEmail());
        return ResponseEntity.ok(validCode);
    }

}
