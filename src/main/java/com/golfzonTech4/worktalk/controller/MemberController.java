package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.dto.member.MemberDetailDto;
import com.golfzonTech4.worktalk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * MemberDetailDto 파라미터로 받아서 MemberService의 signup메서드를 호출
     */
    @PostMapping("/join")
    public ResponseEntity<Long> join(
            @Valid @RequestBody MemberDetailDto request) {
        log.info("signupUser: {}", request);

        Member member = new Member();

        member.setEmail(request.getEmail());
        member.setPw(request.getPw());
        member.setName(request.getName());
        member.setTel(request.getTel());

        // request의 role 값에 따라 회원 구분을 다르게 설정
        if (request.getRole() == 0) {
            member.setMemberType(MemberType.ROLE_USER);
        } else if(request.getRole() == 1){
            member.setMemberType(MemberType.ROLE_HOST);
        } else {
            member.setMemberType(MemberType.ROLE_MASTER);
        }

        return ResponseEntity.ok(memberService.join(member));
    }

    @PostMapping("/nameCheck")
    public ResponseEntity findDuplicated(@RequestBody MemberDetailDto request) {
        log.info("findDuplicated: {}", request);
        Member member = new Member();
        member.setName(request.getName());

        memberService.findDuplicatesName(member); // 중복 회원 존재 시 Exception 발생

        return ResponseEntity.ok().build(); // 종복 회원이 존재하지 않을 시 성공 메세지
    }
}
