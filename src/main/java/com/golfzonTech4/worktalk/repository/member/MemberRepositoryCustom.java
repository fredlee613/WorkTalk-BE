package com.golfzonTech4.worktalk.repository.member;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.dto.member.MemberDto;

import java.util.List;

public interface MemberRepositoryCustom {
    public List<MemberDto> findDeactMemeber();
}
