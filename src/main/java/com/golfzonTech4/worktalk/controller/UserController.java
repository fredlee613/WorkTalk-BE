package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.dto.member.MemberDetailDto;
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
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final MemberService memberService;


    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }


    /**
     * 권한 모두 허용
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_HOST', 'ROLE_MASTER')")
    public ResponseEntity<MemberDetailDto> getMyUserInfo(HttpServletRequest request) {
        log.info("getMyUserInfo: {}", request);
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities());
    }

    /**
     * HOST 권한만 허용
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ROLE_HOST')")
    public ResponseEntity<MemberDetailDto> getUserInfo(@PathVariable String username) {
        log.info("getUserInfo: {}", username);
        return ResponseEntity.ok(memberService.getUserWithAuthorities(username));
    }

    /**
     * 회원 전체 리스트 요청
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_HOST', 'ROLE_MASTER')")
    @GetMapping("/members/")
    public ResponseEntity<List<Member>> findAll() {
        log.info("findAll");
        List<Member> findMembers = memberService.findAll();

        return ResponseEntity.ok(findMembers);
    }

}
