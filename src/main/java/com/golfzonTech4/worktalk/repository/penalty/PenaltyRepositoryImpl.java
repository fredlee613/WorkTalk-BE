package com.golfzonTech4.worktalk.repository.penalty;

import com.golfzonTech4.worktalk.domain.PenaltyType;
import com.golfzonTech4.worktalk.domain.QPenalty;
import com.golfzonTech4.worktalk.dto.penalty.PenaltyDto;
import com.golfzonTech4.worktalk.dto.penalty.QPenaltyDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.golfzonTech4.worktalk.domain.QPenalty.penalty;

@Slf4j
public class PenaltyRepositoryImpl implements PenaltyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PenaltyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<PenaltyDto> findPenalties() {
        log.info("findPenalties");
        return queryFactory.select(new QPenaltyDto(penalty.penaltyId, penalty.member.id, penalty.member.name,
                        penalty.penaltyReason, penalty.penaltyType, penalty.penaltyDate))
                .from(penalty).fetch();
    }
}
