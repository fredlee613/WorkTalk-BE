package com.golfzonTech4.worktalk.repository.pay;

import com.golfzonTech4.worktalk.domain.Pay;
import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.QPay;
import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.pay.QPayInsertDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.golfzonTech4.worktalk.domain.QPay.pay;

@Slf4j
public class PayRepositoryImpl implements PayRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PayRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<PayInsertDto> findAlByReserveId(Long reserveId) {
        log.info("findAlByReserveId : {}", reserveId);
        return queryFactory.select(new QPayInsertDto(
                        pay.payId, pay.reservation.reserveId, pay.customerUid,
                        pay.impUid, pay.merchantUid, pay.payStatus, pay.payAmount
                ))
                .from(pay)
                .where(pay.reservation.reserveId.eq(reserveId))
                .fetch();
    }

    @Override
    public Optional<PayInsertDto> findByCUid(String customerUid) {
        log.info("findByCustomerUid : {}", customerUid);
        PayInsertDto result = queryFactory.select(new QPayInsertDto(
                        pay.payId, pay.reservation.reserveId, pay.customerUid,
                        pay.impUid, pay.merchantUid, pay.payStatus, pay.payAmount
                ))
                .from(pay)
                .where(pay.customerUid.eq(customerUid) // 같은 customerUid를 갖는 값 중
                        .and(pay.payStatus.eq(PaymentStatus.POSTPAID_BOOKED))) // 보증금 내역을 호출
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
