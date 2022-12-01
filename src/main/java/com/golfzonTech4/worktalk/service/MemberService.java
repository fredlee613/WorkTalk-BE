package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.dto.member.MemberDetailDto;
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
    public Long join(Member member) {
        log.info("signup : {}", member);
        member.setPw(passwordEncoder.encode(member.getPw())); // 비밀번호 인코딩
        member.setImgName("profill.png"); // 프로필 이미지 => 기본 이미지로 설정

        findDuplicatesName(member); // 회원명 중복 검증 => 중복 회원 존재할 경우 예외 처리

        return memberRepository.save(member).getId();
    }

    /**
     * 회원명 중복 확인 서비스 로직
     * 중복 시 예외 처리
     */
    public void findDuplicatesName(Member member) {
        log.info("findDuplicatesName : {}", member);
        Optional<Member> result = memberRepository.findByName(member.getName());
        if (!result.isEmpty()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
    }

//    /**
//     * 회원명 중복 확인 비동기 처리 로직
//     * 중복 시 1, 비중복 시 0 반환
//     */
//    public Integer checkName(Member member) {
//        log.info("checkName : {}", member);
//        Optional<Member> result = memberRepositoryJpa.findByName(member.getName());
//        if (!findMembers.isEmpty()) {
//            return 1;
//        } else {
//            return 0;
//        }
//    }

    /**
     * 회원 단건 조회(이름 기준)
     */
    public Member findByName(String name) {
        log.info("findByName : {}", name);
        return memberRepository.findByName(name).get();
    }


    /**
     * 회원 이메일 중복 확인 서비스 로직
     * 중복 시 예외 처리
     */
    public void findDuplicatesEmail(Member member) {
        log.info("findDuplicatesName : {}", member);
        Optional<Member> result = memberRepository.findByEmail(member.getEmail());
        if (!result.isEmpty()) {
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

        Optional<Member> result = memberRepository.findByName(username);

        MemberDetailDto dto = getMemberDetailDto(result.get());

        return dto;
    }


    /**
     * SecurityContext에 저장된 username의 정보만 가져온다.
     */
    public MemberDetailDto getMyUserWithAuthorities() {
        log.info("getMyUserWithAuthorities");

        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        if (currentUsername.isEmpty()) throw new NotFoundMemberException("Member not found");
        return getMemberDetailDto(memberRepository.findByName(currentUsername.get()).get());
    }

    // member -> memberDetailDto
    private static MemberDetailDto getMemberDetailDto(Member member) {
        MemberDetailDto memberDetailDto = new MemberDetailDto();
        
        // 추후 생성자로 수정
        memberDetailDto.setId(member.getId());
        memberDetailDto.setEmail(member.getEmail());
        memberDetailDto.setPw(member.getPw());
        memberDetailDto.setName(member.getName());
        memberDetailDto.setTel(member.getTel());
        memberDetailDto.setMemberType(member.getMemberType());
        memberDetailDto.setImgName(member.getImgName());

        return memberDetailDto;
    }
}
