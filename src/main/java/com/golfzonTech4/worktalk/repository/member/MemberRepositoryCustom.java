package com.golfzonTech4.worktalk.repository.member;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.dto.member.HostDto;
import com.golfzonTech4.worktalk.dto.member.MemberDto;
import com.golfzonTech4.worktalk.dto.member.MemberPenaltyDto;
import com.golfzonTech4.worktalk.dto.member.MemberSerachDto;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    public List<MemberDto> findDeactMemeber(MemberSerachDto dto);

    public List<MemberPenaltyDto> findNoshowMember(Integer activated);

    public Optional<Member> findByWorkTalk(String email);

    public HostDto findActivated(String name);
}
