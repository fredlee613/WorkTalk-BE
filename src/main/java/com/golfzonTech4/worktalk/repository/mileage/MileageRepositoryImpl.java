package com.golfzonTech4.worktalk.repository.mileage;

import com.golfzonTech4.worktalk.domain.Mileage_status;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

import static com.golfzonTech4.worktalk.domain.QMileage.mileage;

@Slf4j
public class MileageRepositoryImpl implements MileageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MileageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 적립된 마일리지의 총 합
     */
    @Override
    public int getTotalSave(Long memberId) {
        return queryFactory.select(mileage.mileageAmount.sum())
                .from(mileage)
                .where(mileage.member.id.eq(memberId)
                        .and(mileage.status.eq(Mileage_status.SAVED)))
                .fetchOne();
    }

    /**
     * 사용된 마일리지의 총 합
     */
    @Override
    public int getTotalUse(Long memberId) {
        return queryFactory.select(mileage.mileageAmount.sum())
                .from(mileage)
                .where(mileage.member.id.eq(memberId)
                        .and(mileage.status.eq(Mileage_status.USED)))
                .fetchOne();
    }

}
