package com.golfzonTech4.worktalk.repository.member;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.dto.member.MemberDto;
import com.golfzonTech4.worktalk.dto.member.MemberPenaltyDto;

import java.util.List;

public interface MemberRepositoryCustom {
    public List<MemberDto> findDeactMemeber();

    public List<MemberPenaltyDto> findNoshowMember();
}
