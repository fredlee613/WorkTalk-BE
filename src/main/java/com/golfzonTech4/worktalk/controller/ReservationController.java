package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveOrderSearch;
import com.golfzonTech4.worktalk.dto.reservation.ReserveTempDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.ReservationService;
import com.golfzonTech4.worktalk.service.TempReservationService;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;
    private final TempReservationService tempReservationService;

    /**
     * 예약 날짜 및 시간 선택
     */
    @PostMapping("/reservation/choose")
    public ResponseEntity<Long> choose(@RequestBody ReserveTempDto dto)  {

        log.info("choose: {}", dto);
        Long result = tempReservationService.reserveTemp(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * 예약 및 결제 요청
     */
    @PostMapping("/reservation/reserve")
    public ResponseEntity<Long> reserve(@RequestBody PayInsertDto payInsertDto) throws IamportResponseException, IOException, IllegalAccessException {

        log.info("reserve: {}", payInsertDto);
        Long result = reservationService.reserve(payInsertDto);
        return ResponseEntity.ok(result);
    }

    /**
     * 예약 취소 및 결제 취소 요청
     */
    @PostMapping("/reservation/cancel")
    public ResponseEntity<Integer> cancel(@RequestBody ReserveDto dto) throws IamportResponseException, IOException {
        String currentUserRole = SecurityUtil.getCurrentUserRole().get();
        int count = 0;
        // 계정 종류에 따라 분기
        if (currentUserRole.equals(MemberType.ROLE_USER.toString())) { // 유저 취소 경우
            count = reservationService.cancelByUser(dto.getReserveId(), dto.getCancelReason());
        } else { // 호스트 취소 경우
            count = reservationService.cancelByHost(dto.getReserveId(), dto.getCancelReason());
        }
        // 성공 시 200 반환
        return ResponseEntity.ok(count);
    }

    /**
     * 사용 종료 요청
     */
    @GetMapping("/reservation/end/{reserveId}")
    public ResponseEntity end(@PathVariable("reserveId") Long reserveId) {
        log.info("end: {}", reserveId);
        reservationService.end(reserveId);
        return ResponseEntity.ok().build();
    }

    /**
     * 예약 리스트 조회
     */
    @GetMapping("/reservation/find/{pageNum}")
    public ResponseEntity<ListResult> findByName(@RequestBody ReserveOrderSearch dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 10);
        return ResponseEntity.ok(reservationService.findAllByName(dto, pageRequest));
    }

    /**
     * 해당 공간의 예약된 리스트 조회 요청(선택된 기간/시간 기준)
     */
    @GetMapping("/reservations/isBooked")
    public ResponseEntity<List<ReserveCheckDto>> findBookedRoom(@RequestParam ReserveCheckDto reserveCheckDto) {
        log.info("findBookedRoom : {}", reserveCheckDto);
        return ResponseEntity.ok(reservationService.findBookedReservation(reserveCheckDto));
    }
}
