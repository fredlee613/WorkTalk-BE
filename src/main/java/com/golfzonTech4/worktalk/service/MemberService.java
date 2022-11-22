package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입 서비즈 로직
     * 로그인 성공 시 회원 번호 반환
     * 실패 시 0 반환
     */
    @Transactional
    public Long save(Member member) {
        log.info("save : {}", member);
        member.setMemberType(MemberType.USER); // 회원 타입 => 일반 유저 설정
        member.setPenalty(0); // 페널티 여부 0으로 초기화
        member.setImgName("profill.png"); // 프로필 이미지 => 기본 이미지로 설정

        log.info("member: {}", member);
        findDuplicatesName(member);
        memberRepository.save(member);
        return memberRepository.findOne(member).getId();
    }

    /**
     * 회원명 중복 확인 서비스 로직
     * 중복 시 예외 처리
     */
    public void findDuplicatesName(Member member) {
        log.info("findDuplicatesName : {}", member);
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원 이름입니다.");
        }
    }

    /**
     * 회원 이메일 중복 확인 서비스 로직
     * 중복 시 예외 처리
     */
    public void findDuplicatesEmail(Member member) {
        log.info("findDuplicatesName : {}", member);
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 사용중인 이메일입니다.");
        }
    }

    public List<Member> findAll() {
        log.info("findAll");
        return memberRepository.findAll();
    }
}
