package com.golfzonTech4.worktalk.repository.reservation.temp;

import com.golfzonTech4.worktalk.domain.TempReservation;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TempReservationRepositoryCustom{
    List<ReserveCheckDto> findBookedOffice(Long roomId, LocalDate initDate, LocalDate endDate);

    List<ReserveCheckDto> findBookedRoom(Long roomId, LocalDate initDate, int initTime, int endTime);
}
