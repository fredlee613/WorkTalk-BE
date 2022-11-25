package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.dto.reservation.ReserveDto;
import com.golfzonTech4.worktalk.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<Long> reserve(@RequestBody ReserveDto reserveDto) {

        log.info("reserve: {}", reserveDto);
        Long result = reservationService.reserve(reserveDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cancel")
    public ResponseEntity<LocalDateTime> cancel(@RequestBody ReserveDto reserveDto) {
        log.info("cancel: {}, {}", reserveDto.getReserveId(), reserveDto.getCancelReason());
        return ResponseEntity.ok(reservationService.cancel(reserveDto.getReserveId(), reserveDto.getCancelReason()));
    }

}
