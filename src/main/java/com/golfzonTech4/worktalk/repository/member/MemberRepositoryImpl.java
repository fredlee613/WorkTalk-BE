package com.golfzonTech4.worktalk.repository.member;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.QMember;
import com.golfzonTech4.worktalk.domain.QPenalty;
import com.golfzonTech4.worktalk.dto.member.MemberDto;
import com.golfzonTech4.worktalk.dto.member.MemberPenaltyDto;
import com.golfzonTech4.worktalk.dto.member.QMemberDto;
import com.golfzonTech4.worktalk.dto.member.QMemberPenaltyDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

import static com.golfzonTech4.worktalk.domain.QMember.member;
import static com.golfzonTech4.worktalk.domain.QPenalty.penalty;

@Slf4j
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<MemberDto> findDeactMemeber() {
        return queryFactory
                .select(new QMemberDto(member.id, member.email, member.name, member.tel, member.memberType, member.activated))
                .from(member)
                .where(member.activated.eq(0))
                .fetch();
    }

    public List<MemberPenaltyDto> findNoshowMember() {
        return queryFactory
                .select(new QMemberPenaltyDto(member.id, member.email, member.name, member.tel, member.memberType, member.activated,
                        penalty.penaltyId, penalty.penaltyReason, penalty.penaltyType, penalty.penaltyDate
                ))
                .from(member)
                .leftJoin(penalty)
                .on(member.id.eq(penalty.member.id))
                .fetch();
    }

}
