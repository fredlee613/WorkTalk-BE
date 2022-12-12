package com.golfzonTech4.worktalk.repository.reservation.temp;

import com.golfzonTech4.worktalk.dto.reservation.QReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.golfzonTech4.worktalk.domain.QTempReservation.tempReservation;

@Slf4j
public class TempReservationRepositoryImpl implements TempReservationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public TempReservationRepositoryImpl(EntityManager em, EntityManager em1) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ReserveCheckDto> findBookedOffice(Long roomId, LocalDate initDate, LocalDate endDate) {
        log.info("findBookedOffice : {}, {}, {}, {}", roomId, initDate, endDate);
        return queryFactory.select(new QReserveCheckDto(
                        tempReservation.roomId,
                        tempReservation.bookDate.checkInDate,
                        tempReservation.bookDate.checkOutDate,
                        tempReservation.bookDate.checkInTime,
                        tempReservation.bookDate.checkOutTime
                ))
                .from(tempReservation)
                .where(tempReservation.roomId.eq(roomId)
                        .and(tempReservation.bookDate.checkInDate.between(initDate, endDate)
                                .or(tempReservation.bookDate.checkOutDate.between(initDate, endDate))))
                .fetch();
    }

    @Override
    public List<ReserveCheckDto> findBookedRoom(Long roomId, LocalDate initDate) {
        log.info("findBookedOffice : {}, {}", roomId, initDate);
        return queryFactory.select(new QReserveCheckDto(
                        tempReservation.roomId,
                        tempReservation.bookDate.checkInDate,
                        tempReservation.bookDate.checkOutDate,
                        tempReservation.bookDate.checkInTime,
                        tempReservation.bookDate.checkOutTime
                ))
                .from(tempReservation)
                .where(tempReservation.bookDate.checkInDate.eq(initDate)
                        .and(tempReservation.roomId.eq(roomId)))
                .fetch();
    }

    @Override
    public List<ReserveCheckDto> checkBookedRoom(Long roomId, LocalDate initDate, Integer checkInTime, Integer checkOutTime) {
        log.info("checkBookedRoom : {}, {}, {}, {}", roomId, initDate, checkInTime, checkOutTime);
        return queryFactory.select(new QReserveCheckDto(
                        tempReservation.roomId,
                        tempReservation.bookDate.checkInDate,
                        tempReservation.bookDate.checkOutDate,
                        tempReservation.bookDate.checkInTime,
                        tempReservation.bookDate.checkOutTime
                ))
                .from(tempReservation)
                .where(tempReservation.bookDate.checkInDate.eq(initDate)
                        .and(tempReservation.roomId.eq(roomId))
                        .and(tempReservation.bookDate.checkInTime.between(checkInTime, checkOutTime)
                                .or(tempReservation.bookDate.checkInTime.loe(checkInTime)
                                        .and(tempReservation.bookDate.checkOutTime.goe(checkOutTime)))
                                .or(tempReservation.bookDate.checkInTime.loe(checkInTime)
                                        .and(tempReservation.bookDate.checkOutTime.between(checkInTime, checkOutTime)))
                                .or(tempReservation.bookDate.checkInTime.loe(checkInTime)
                                        .and(tempReservation.bookDate.checkOutTime.goe(checkOutTime))))
                )
                .fetch();
    }

}
