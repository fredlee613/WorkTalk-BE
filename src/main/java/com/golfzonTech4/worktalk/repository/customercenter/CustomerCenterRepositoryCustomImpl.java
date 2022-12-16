package com.golfzonTech4.worktalk.repository.customercenter;

import com.golfzonTech4.worktalk.domain.CcType;
import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.dto.customercenter.CustomerCenterDetailDto;
import com.golfzonTech4.worktalk.dto.customercenter.QCustomerCenterDetailDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

import static com.golfzonTech4.worktalk.domain.QCustomerCenter.customerCenter;
import static com.golfzonTech4.worktalk.domain.QCustomerComment.customerComment;

@Slf4j
public class CustomerCenterRepositoryCustomImpl implements CustomerCenterRepositoryCustom {

    private JPAQueryFactory queryFactory; // 동적 쿼리 생성 위한 클래스

    // JPAQueryFactory 생성자로 EntityManager 넣어줌
    public CustomerCenterRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CustomerCenterDetailDto> customerManagePage(MemberType memberType, CcType ccType) {
        log.info("memberType:{}",memberType);
        return queryFactory
                .select(
                        new QCustomerCenterDetailDto(
                                customerCenter.ccId,
                                customerCenter.member.id,
                                customerCenter.title,
                                customerCenter.content,
                                customerCenter.type,
                                customerCenter.lastModifiedDate,
                                customerComment.ccCommentId,
                                customerComment.content,
                                customerComment.lastModifiedDate
                                )
                )
                .from(customerCenter)
                .leftJoin(customerComment)
                .on(customerCenter.ccId.eq(customerComment.ccCommentId))
                .where(eqMemberType(memberType), eqccType(ccType))
                .orderBy(customerCenter.ccId.desc())
                .fetch();
    }

    private BooleanExpression eqMemberType(MemberType memberType) {
        if (memberType == null) {
            return null;
        }
        return customerCenter.member.memberType.eq(memberType);
    }

    private BooleanExpression eqccType(CcType ccType) {
        if (ccType == null) {
            return null;
        }
        return customerCenter.type.eq(ccType);
    }

}
