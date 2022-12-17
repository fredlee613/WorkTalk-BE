package com.golfzonTech4.worktalk.repository.member;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.dto.member.MemberDto;
import com.golfzonTech4.worktalk.dto.member.MemberPenaltyDto;
import com.golfzonTech4.worktalk.dto.member.MemberSerachDto;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {
    public List<MemberDto> findDeactMemeber(MemberSerachDto dto);

    public List<MemberPenaltyDto> findNoshowMember(Integer activated);

    Optional<Member> findByWorkTalk(String email); // 회원 단건 조회(이메일 기준, 사이트 경로 회원가입한 회원만 )
}
