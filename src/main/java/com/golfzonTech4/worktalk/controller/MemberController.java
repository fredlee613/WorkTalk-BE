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
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
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
     * MemberDetailDto 파라미터로 받아서 MemberService의 signup메서드를 호출
     */
    @PostMapping("/signin/user")
    public ResponseEntity<MemberDetailDto> signupUser(
            @Valid @RequestBody MemberDetailDto request) {
        log.info("signupUser: {}", request);

        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPw(request.getPw());
        member.setName(request.getName());
        member.setTel(request.getTel());
        member.setMemberType(MemberType.ROLE_USER);
        return ResponseEntity.ok(userService.signup(member));
    }

    /**
     * MemberDetailDto 파라미터로 받아서 MemberService의 signup메서드를 호출
     */
    @PostMapping("/signin/host")
    public ResponseEntity<MemberDetailDto> signupHost(
            @Valid @RequestBody MemberDetailDto request) {
        log.info("signupHost: {}", request);

        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPw(request.getPw());
        member.setName(request.getName());
        member.setTel(request.getTel());
        member.setMemberType(MemberType.ROLE_HOST);
        return ResponseEntity.ok(userService.signup(member));
    }

    /**
     * 권한 모두 허용
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_HOST', 'ROLE_MASTER')")
    public ResponseEntity<MemberDetailDto> getMyUserInfo(HttpServletRequest request) {
        log.info("getMyUserInfo: {}", request);
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    /**
     * HOST 권한만 허용
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ROLE_HOST')")
    public ResponseEntity<MemberDetailDto> getUserInfo(@PathVariable String username) {
        log.info("getUserInfo: {}", username);
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }

    /**
     * 회원 전체 리스트 요청
     */
    @PostMapping("/members/mailCheck")
    public ResponseEntity<String> mailCheck(@RequestBody MemberMailDto request) {
        log.info("save: {}", request);
        String validCode = mailService.joinMail(request.getEmail());
        return ResponseEntity.ok(validCode);
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
