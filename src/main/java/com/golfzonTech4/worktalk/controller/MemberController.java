package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.dto.member.MemberDetailDto;
import com.golfzonTech4.worktalk.dto.member.MemberDto;
import com.golfzonTech4.worktalk.dto.member.MemberSerachDto;
import com.golfzonTech4.worktalk.dto.member.MemberUpdateDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.repository.member.MemberRepository;
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
    @PostMapping("/member/join")
    public ResponseEntity<Long> join(
            @Valid @RequestBody MemberDetailDto request) {
        log.info("signupUser: {}", request);

        return ResponseEntity.ok(memberService.join(request));
    }

    /**
     * 회원 단건 조회 요청 (접속자 기준)
     */
    @GetMapping("/member/profile")
    public ResponseEntity<MemberDto> profile() {
        log.info("profile");
        return ResponseEntity.ok(memberService.findProfile());
    }

    /**
     * 회원 정보 수정 요청
     */
    @PostMapping("/member/update")
    public ResponseEntity<Long> update(
            @Valid @RequestBody MemberUpdateDto dto) {
        log.info("update: {}", dto);
        return ResponseEntity.ok(memberService.update(dto));
    }

    /**
     * 회원 탈퇴 요청
     */
    @PostMapping("/member/leave/{memberId}")
    public ResponseEntity<Long> leave(
            @PathVariable(value = "memberId") Long memberId) {
        log.info("update: {}", memberId);
        // 예약/ 결제건이 있을 경우 예외 발생
        memberService.leave(memberId);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 명 중복 확인 요청
     */
    @PostMapping("/member/nameCheck")
    public ResponseEntity findDuplicated(@RequestBody MemberDetailDto request) {
        log.info("findDuplicated: {}", request);
        Member member = new Member();
        member.setName(request.getName());

        memberService.findDuplicatesName(member); // 중복 회원 존재 시 Exception 발생

        return ResponseEntity.ok().build(); // 종복 회원이 존재하지 않을 시 성공 메세지
    }

    /**
     * 비 승인 호스트 내역 조회 요청
     */
    @GetMapping("/member/deactivated")
    public ResponseEntity<ListResult> findDeactMemeber(@ModelAttribute MemberSerachDto dto) {
        log.info("findDeactMemeber");
        return ResponseEntity.ok(memberService.findDeactMemeber(dto));
    }

    /**
     * 호스트 승인 요청
     */
    @GetMapping("/member/approve/{memberId}")
    public ResponseEntity<Integer> approve(@PathVariable(value = "memberId") Long memberId) {
        log.info("approve : {}", memberId);
        return ResponseEntity.ok(memberService.approve(memberId));
    }

    /**
     * 메일 기준 회원 조회 요청
     */
    @GetMapping("/member/findEmail")
    public ResponseEntity findEmail(@RequestParam(value = "email") String email) {
        log.info("findEmail : {}", email);
        // 가입된 정보가 없을 경우 예외 처리
        memberService.findEmail(email);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 찾기 요청
     */
    @GetMapping("/member/findPw")
    public ResponseEntity findPw(@RequestParam(value = "email") String email) {
        log.info("findPw : {}", email);
        // 가입된 정보가 없을 경우 예외 처리
        memberService.changePw(email);
        return ResponseEntity.ok().build();
    }
}
