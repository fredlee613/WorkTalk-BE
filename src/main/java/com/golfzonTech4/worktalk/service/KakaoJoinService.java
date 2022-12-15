package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.dto.member.KakaoProfileDto;
import com.golfzonTech4.worktalk.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.golfzonTech4.worktalk.domain.MemberType.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoJoinService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(KakaoProfileDto dto) {
        log.info("signup : {}", dto);

        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setPw(dto.getPw());
        member.setName(dto.getName());
        member.setMemberType(ROLE_USER);
        member.setImgName("profill.png");
        member.setKakaoYn("Y");
        member.setPw(passwordEncoder.encode(member.getPw()));

        return memberRepository.save(member).getId();
    }

}
