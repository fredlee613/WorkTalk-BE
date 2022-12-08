package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.dto.member.MemberDetailDto;
import com.golfzonTech4.worktalk.dto.member.MemberSerachDto;
import com.golfzonTech4.worktalk.dto.member.MemberUpdateDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

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

        return ResponseEntity.ok(memberService.join(request));
    }

    /**
     * MemberDetailDto 파라미터로 받아서 MemberService의 signup메서드를 호출
     */
    @PostMapping("/update")
    public ResponseEntity<Long> update(
            @Valid @RequestBody MemberUpdateDto dto) {
        log.info("update: {}", dto);
        return ResponseEntity.ok(memberService.update(dto));
    }

    /**
     * 회원 탈퇴 요청
     */
    @PostMapping("/leave/{memberId}")
    public ResponseEntity<Long> leave(
            @PathVariable(value = "memberId") Long memberId) {
        log.info("update: {}", memberId);
        // 예약/ 결제건이 있을 경우 예외 발생
        memberService.leave(memberId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/nameCheck")
    public ResponseEntity findDuplicated(@RequestBody MemberDetailDto request) {
        log.info("findDuplicated: {}", request);
        Member member = new Member();
        member.setName(request.getName());

        memberService.findDuplicatesName(member); // 중복 회원 존재 시 Exception 발생

        return ResponseEntity.ok().build(); // 종복 회원이 존재하지 않을 시 성공 메세지
    }

    @GetMapping("/member/deactivated")
    public ResponseEntity<ListResult> findDeactMemeber(@ModelAttribute MemberSerachDto dto) {
        log.info("findDeactMemeber");
        return ResponseEntity.ok(memberService.findDeactMemeber(dto));
    }
    @GetMapping("/member/approve/{memberId}")
    public ResponseEntity<Integer> approve(@PathVariable(value = "memberId") Long memberId) {
        log.info("approve : {}", memberId);
        return ResponseEntity.ok(memberService.approve(memberId));
    }
}
