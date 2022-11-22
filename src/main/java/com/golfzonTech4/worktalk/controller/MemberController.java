package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.dto.member.MemberDetailDto;
import com.golfzonTech4.worktalk.dto.member.MemberMailDto;
import com.golfzonTech4.worktalk.service.MailService;
import com.golfzonTech4.worktalk.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;

    @PostMapping("/members/save")
    public Result save(@RequestBody @Valid MemberDetailDto request) {
        log.info("save: {}", request);
        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPw(request.getPw());
        member.setName(request.getName());
        member.setTel(request.getTel());
        Long saveResult = memberService.save(member);
        return new Result(saveResult);
    }

    @PostMapping("/members/mailCheck")
    public Result mailCheck(@RequestBody MemberMailDto request) {
        log.info("save: {}", request);
        String validCode = mailService.joinMail(request.getEmail());
        return new Result(validCode);
    }

    @GetMapping("/members/")
    public Result findAll() {
        log.info("findAll");
        List<Member> findMembers = memberService.findAll();
        return new Result(findMembers);
    }

    @Data
    @AllArgsConstructor
    private class Result<T> {
        private T data;
    }
}
