package com.golfzonTech4.worktalk.repository.reservation;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.ReserveStatus;
import com.golfzonTech4.worktalk.domain.RoomType;
import com.golfzonTech4.worktalk.dto.reservation.QReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.QReserveSimpleDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.golfzonTech4.worktalk.domain.QReservation.reservation;

@Slf4j
public class ReservationSimpleRepositoryImpl implements ReservationSimpleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReservationSimpleRepositoryImpl(EntityManager em, EntityManager em1) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ListResult findAllByUser(String name, Integer paid, PaymentStatus paymentStatus, ReserveStatus reserveStatus) {
        log.info("findAllByUser : {}", name);

        List<ReserveSimpleDto> result = queryFactory.select(new QReserveSimpleDto(
                        reservation.room.roomName,
                        reservation.paid,
                        reservation.reserveId,
                        reservation.member.id,
                        reservation.room.roomId,
                        reservation.bookDate,
                        reservation.member.name,
                        reservation.reserveStatus,
                        reservation.paymentStatus,
                        reservation.room.roomType,
                        reservation.reserveAmount)
                )
                .from(reservation)
                .where(reservation.member.name.eq(name), eqPaid(paid), eqPayStatus(paymentStatus), eqReserveStatus(reserveStatus))
                .fetch();

        return new ListResult((long) result.size(), result);
    }

    @Override
    public PageImpl<ReserveSimpleDto> findAllByUserPage(String name, PageRequest pageRequest, Integer paid, PaymentStatus paymentStatus) {
        log.info("findAllByUser : {}, {}, {}, {}", name, pageRequest, paid, paymentStatus);

        List<ReserveSimpleDto> content = queryFactory.select(new QReserveSimpleDto(
                        reservation.room.roomName,
                        reservation.paid,
                        reservation.reserveId,
                        reservation.member.id,
                        reservation.room.roomId,
                        reservation.bookDate,
                        reservation.member.name,
                        reservation.reserveStatus,
                        reservation.paymentStatus,
                        reservation.room.roomType,
                        reservation.reserveAmount)
                )
                .from(reservation)
                .where(reservation.member.name.eq(name), eqPaid(paid), eqPayStatus(paymentStatus))
                .orderBy(reservation.reserveId.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(reservation.member.name.eq(name), eqPaid(paid), eqPayStatus(paymentStatus))
                .fetchOne();


        return new PageImpl<>(content, pageRequest, count);
    }

    @Override
    public List<ReserveSimpleDto> findAllByTime() {
        log.info("findAllByTime");
        int hour = LocalDateTime.now().getHour() + 1; // 현재 시간에서 1시간을 뺀 값: 매 시간 30분에 조회되기에 시간만 비교하면됨.
        log.info("hour: {}", hour);
        // 예시: 12시 30분 조회 시 => 13시의 예약 데이터들을 조회.
        List<ReserveSimpleDto> result = queryFactory.select(new QReserveSimpleDto(
                        reservation.reserveId,
                        reservation.member.id)
                )
                .from(reservation)
                .where(reservation.bookDate.checkInTime.eq(hour) // 이용시간이 임박한 예약건 들 중
                        .and(reservation.paymentStatus.eq(PaymentStatus.PREPAID)) // 선결제 중
                        .and(reservation.paid.eq(0))) // 결제가 되지 않은 예약
                .fetch();

        log.info("result.size() : {}", result.size());

        return result;
    }

    @Override
    public Long countNoShow(Long memberId, ReserveStatus reserveStatus) {
        log.info("findAllByTime : {}, {}", memberId, reserveStatus);

        return queryFactory.select(new QReserveSimpleDto(
                        reservation.reserveId,
                        reservation.member.id)
                )
                .from(reservation)
                .where(reservation.member.id.eq(memberId) // 해당 접속자의 예약 건들 중
                        .and(reservation.reserveStatus.eq(reserveStatus))) // 예약 상태가 NoShow인 예약들
                .fetch().stream().count(); // 의 횟수를 집계하여 반환
    }

    @Override
    public List<ReserveCheckDto> findBookedOffice(Long roomId, LocalDate initDate, LocalDate endDate) {
        log.info("findBookedOffice : {}, {}, {}, {}", roomId, initDate, endDate);
        return queryFactory.select(new QReserveCheckDto(
                        reservation.room.roomId,
                        reservation.bookDate.checkInDate,
                        reservation.bookDate.checkOutDate,
                        reservation.bookDate.checkInTime,
                        reservation.bookDate.checkOutTime
                ))
                .from(reservation)
                .where(reservation.room.roomType.eq(RoomType.OFFICE)
                        .and(reservation.room.roomId.eq(roomId))
                        .and(reservation.bookDate.checkInDate.between(initDate, endDate)
                                .or(reservation.bookDate.checkOutDate.between(initDate, endDate))))
                .fetch();
    }

    @Override
    public List<ReserveCheckDto> findBookedRoom(Long roomId, LocalDate initDate, int initTime, int endTime) {
        log.info("findBookedOffice : {}, {}, {}, {}", roomId, initDate, initTime, endTime);
        return queryFactory.select(new QReserveCheckDto(
                        reservation.room.roomId,
                        reservation.bookDate.checkInDate,
                        reservation.bookDate.checkOutDate,
                        reservation.bookDate.checkInTime,
                        reservation.bookDate.checkOutTime
                ))
                .from(reservation)
                .where(reservation.bookDate.checkInDate.eq(initDate)
                        .and(reservation.room.roomId.eq(roomId))
                        .and(reservation.room.roomType.ne(RoomType.OFFICE)))
                .fetch();
    }

    @Override
    public Optional<ReserveSimpleDto> findRoomName(Long reserveId) {
        log.info("findRoomName : {}", reserveId);
        ReserveSimpleDto result = queryFactory.select(new QReserveSimpleDto(reservation.room.roomName, reservation.bookDate))
                .from(reservation)
                .where(reservation.reserveId.eq(reserveId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    // 예약의 결제 여부를 나타내는 BooleanExpression
    private BooleanExpression eqPaid(Integer paid) {
        if (paid == null) {
            return null;
        }
        return reservation.paid.eq(paid);
    }

    private BooleanExpression eqPayStatus(PaymentStatus paymentStatus) {
        if (paymentStatus == null) {
            return null;
        }
        return reservation.paymentStatus.eq(paymentStatus);
    }

    private BooleanExpression eqReserveStatus(ReserveStatus status) {
        if (status == null) {
            return null;
        }
        return reservation.reserveStatus.eq(status);
    }

}
