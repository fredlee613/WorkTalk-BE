package com.golfzonTech4.worktalk.repository.reservation;

import com.golfzonTech4.worktalk.domain.ReserveStatus;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;

import java.time.LocalDate;
import java.util.List;

public interface ReservationSimpleRepositoryCustom {

    List<ReserveSimpleDto> findAllByUserQuery(String name);
    List<ReserveSimpleDto> findAllByTime();
    Long countNoShow(Long memberId, ReserveStatus reserveStatus);

    List<ReserveCheckDto> findBookedOffice(Long roomId, LocalDate initDate, LocalDate endDate);
    List<ReserveCheckDto> findBookedRoom(Long roomId, LocalDate initDate, int initTime, int endTime);

}
