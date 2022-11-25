package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.dto.member.MemberDetailDto;
import com.golfzonTech4.worktalk.dto.member.MemberMailDto;
import com.golfzonTech4.worktalk.service.MailService;
import com.golfzonTech4.worktalk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/host")
@RequiredArgsConstructor
@Slf4j
public class HostController {
    private final MemberService userService;
    private final MailService mailService;


    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    /**
     * 회원 전체 리스트 요청
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_HOST', 'ROLE_MASTER')")
    @GetMapping("/members/")
    public ResponseEntity<List<Member>> findAll() {
        log.info("findAll");
        List<Member> findMembers = userService.findAll();

        return ResponseEntity.ok(findMembers);
    }

}
