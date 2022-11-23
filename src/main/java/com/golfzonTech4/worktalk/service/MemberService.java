package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.dto.member.MemberDetailDto;
import com.golfzonTech4.worktalk.exception.DuplicateMemberException;
import com.golfzonTech4.worktalk.exception.NotFoundMemberException;
import com.golfzonTech4.worktalk.repository.MemberRepository;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDetailDto signup(Member member) {
        log.info("signup : {}", member);
        member.setPw(passwordEncoder.encode(member.getPw())); // 비밀번호 인코딩
//        member.setMemberType(MemberType.ROLE_HOST); // 회원 타입 => 일반 유저 설정
        member.setPenalty(0); // 페널티 여부 0으로 초기화
        member.setImgName("profill.png"); // 프로필 이미지 => 기본 이미지로 설정

        findDuplicatesName(member); // 회원명 중복 검증

        memberRepository.save(member);
        Member findMember = memberRepository.findOneByName(member.getName());

        MemberDetailDto dto = getMemberDetailDto(findMember);

        return dto;
    }

    /**
     * 회원명 중복 확인 서비스 로직
     * 중복 시 예외 처리
     */
    public void findDuplicatesName(Member member) {
        log.info("findDuplicatesName : {}", member);
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new DuplicateMemberException();
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

    /**
     * 전체 회원 리스트 조회
     */
    public List<Member> findAll() {
        log.info("findAll");
        return memberRepository.findAll();
    }

    /**
     * username을 기준으로 정보를 가져오는 메서드
     */

    public MemberDetailDto getUserWithAuthorities(String username) {
        log.info("getUserWithAuthorities : {}", username);

        Member findMember = memberRepository.findOneByName(username);

        MemberDetailDto dto = getMemberDetailDto(findMember);

        return dto;
    }


    /**
     * SecurityContext에 저장된 username의 정보만 가져온다.
     */
    public MemberDetailDto getMyUserWithAuthorities() {
        log.info("getMyUserWithAuthorities");

        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        if (currentUsername.isEmpty()) throw new NotFoundMemberException("Member not found");
        Member findMember = memberRepository.findOneByName(currentUsername.get());
        return getMemberDetailDto(findMember);
    }

    // member -> memberDetailDto
    private static MemberDetailDto getMemberDetailDto(Member member) {
        MemberDetailDto memberDetailDto = new MemberDetailDto();

        memberDetailDto.setId(member.getId());
        memberDetailDto.setEmail(member.getEmail());
        memberDetailDto.setPw(member.getPw());
        memberDetailDto.setName(member.getName());
        memberDetailDto.setTel(member.getTel());
        memberDetailDto.setPenalty(member.getPenalty());
        memberDetailDto.setMemberType(member.getMemberType());
        memberDetailDto.setImgName(member.getImgName());

        return memberDetailDto;
    }
}
