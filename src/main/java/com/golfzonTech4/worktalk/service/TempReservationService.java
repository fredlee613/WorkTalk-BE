package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.BookDate;
import com.golfzonTech4.worktalk.domain.TempReservation;
import com.golfzonTech4.worktalk.dto.reservation.ReserveTempDto;
import com.golfzonTech4.worktalk.repository.reservation.ReservationSimpleRepository;
import com.golfzonTech4.worktalk.repository.reservation.temp.TempReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TempReservationService {
    private final TempReservationRepository repository;
    private final ReservationSimpleRepository reservationSimpleRepository;

    public Long reserveTemp(ReserveTempDto dto) {
        log.info("reserveTemp: {}", dto);
        BookDate bookDate = new BookDate(LocalDateTime.now(), dto.getCheckInDate(), dto.getCheckOutDate(), dto.getCheckInTime(), dto.getCheckOutTime());
        TempReservation tempReservation = TempReservation.makeTempReservation(dto.getMemberId(), dto.getRoomId(), bookDate);
        Long id = repository.save(tempReservation).getTempReserveId();
        return id;
    }

    public void deleteTemp(Long tempId) {
        log.info("deleteTemp : {}", tempId);
        Optional<TempReservation> findTemp = repository.findById(tempId);
        if (findTemp.isPresent()) repository.delete(findTemp.get());
    }
}
