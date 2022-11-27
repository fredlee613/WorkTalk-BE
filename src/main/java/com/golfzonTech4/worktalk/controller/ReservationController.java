package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.domain.Reservation;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import com.golfzonTech4.worktalk.service.ReservationService;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    /**
     * 예약 요청
     */
    @PostMapping("/reserve")
    public ResponseEntity<Long> reserve(@RequestBody ReserveDto reserveDto) {

        log.info("reserve: {}", reserveDto);
        Long result = reservationService.reserve(reserveDto);
        return ResponseEntity.ok(result);
    }

    /**
     * 예약 취소 요청
     */
    @PostMapping("/reservation/cancel")
    public ResponseEntity<LocalDateTime> cancelByUser(@RequestBody ReserveDto reserveDto) {
        String currentUserRole = SecurityUtil.getCurrentUserRole().get();
        if (currentUserRole.equals(MemberType.ROLE_USER)) {
            log.info("cancelBy{}: {}, {}",currentUserRole, reserveDto.getReserveId(), reserveDto.getCancelReason());
            return ResponseEntity.ok(reservationService.cancelByUser(reserveDto.getReserveId(), reserveDto.getCancelReason()));
        } else {
            log.info("cancelBy{}: {}, {}",currentUserRole, reserveDto.getReserveId(), reserveDto.getCancelReason());
            return ResponseEntity.ok(reservationService.cancelByHost(reserveDto.getReserveId(), reserveDto.getCancelReason()));
        }
    }

//    /**
//     * 호스트 예약 취소 요청
//     */
//    @PostMapping("/host/cancel")
//    public ResponseEntity<LocalDateTime> cancelByHost(@RequestBody ReserveDto reserveDto) {
//        log.info("cancel: {}, {}", reserveDto.getReserveId(), reserveDto.getCancelReason());
//    }

    /**
     * 예약 리스트 조회 요청
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> findAllByName() {
        log.info("findAllByName");
        return ResponseEntity.ok(reservationService.findAllByName());
    }

    /**
     * 예약 리스트 조회 요청 (Spring Data JPA + JPQL)
     */
    @GetMapping("/reservations/user")
    public ResponseEntity<List<ReserveSimpleDto>> findAllByUser() {
        log.info("findAllByUser");
        return ResponseEntity.ok(reservationService.findAllByUser());
    }

    @GetMapping("/reservations/host")
    public ResponseEntity<List<ReserveSimpleDto>> findAllByHost() {
        log.info("findAllByUser");
        return ResponseEntity.ok(reservationService.findAllByHost());
    }

    /**
     * 예약 리스트 조회 요청(QueryDsl)
     */
    @GetMapping("/reservations/query")
    public ResponseEntity<List<ReserveSimpleDto>> findAllByUserQuery() {
        log.info("findAllByUser");
        return ResponseEntity.ok(reservationService.findAllByUserQuery());
    }

    /**
     * 예약 리스트 조회 요청(선택된 기간/시간 기준)
     */
    @GetMapping("/reservations/isBooked")
    public ResponseEntity<List<ReserveCheckDto>> findBookedRoom(@RequestBody ReserveCheckDto reserveCheckDto) {
        log.info("findBookedRoom : {}", reserveCheckDto);
        return ResponseEntity.ok(reservationService.findBookedReservation(reserveCheckDto));
    }

}
