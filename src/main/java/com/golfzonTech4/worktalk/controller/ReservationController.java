package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveOrderSearch;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.PayService;
import com.golfzonTech4.worktalk.service.ReservationService;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;
    private final PayService payService;

    /**
     * 예약 요청
     */
    @PostMapping("/reservation/reserve")
    public ResponseEntity<Long> reserve(@RequestBody ReserveDto reserveDto) {

        log.info("reserve: {}", reserveDto);
        Long result = reservationService.reserve(reserveDto);
        return ResponseEntity.ok(result);
    }

    /**
     * 예약 취소 및 결제 취소 요청
     */
    @PostMapping("/reservation/cancel")
    public ResponseEntity<Integer> cancel(@RequestBody ReserveDto reserveDto) throws IamportResponseException, IOException {
        String currentUserRole = SecurityUtil.getCurrentUserRole().get();
        Long reservedId = 0L;
        int flag = 0;
        int count = 0;
        // 계정 종류에 따라 분기
        if (currentUserRole.equals(MemberType.ROLE_USER.toString())) { // 유저 취소 경우
            log.info("cancelByUser : {}: {}, {}", currentUserRole, reserveDto.getReserveId(), reserveDto.getCancelReason());
            // 예약 취소 요청 (예약번호, 보증금 취소 가능 여부, 결제 종류 반환)
            Map<String, Object> result = reservationService.cancelByUser(reserveDto.getReserveId(), reserveDto.getCancelReason());
            // 결제 요청에 따른 분기 처리 (선/후결제)
            String status = (String) result.get("status");
            log.info("status : {}", status);
            if (status.equals(PaymentStatus.PREPAID.toString()))
                count = payService.cancelPrepaid((Long) result.get("reserveId"), (int) result.get("flag"));
            else count = payService.cancelPostPaid((Long) result.get("reserveId"), (int) result.get("flag"));
        } else { // 호스트 취소 경우
            log.info("cancelByHost : {}: {}, {}", currentUserRole, reserveDto.getReserveId(), reserveDto.getCancelReason());
            // 예약 취소 요청 (예약번호, 결제 종류 반환)
            Map<String, Object> result = reservationService.cancelByHost(reserveDto.getReserveId(), reserveDto.getCancelReason());
            // 결제 유형에 따른 분기 처리 (선/후결제)
            if (String.valueOf(result.get("status")).equals(PaymentStatus.PREPAID.toString()))
                count = payService.cancelPrepaid((Long) result.get("reserveId"), 0);
            else count = payService.cancelPostPaid((Long) result.get("reserveId"), 0);
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
    @GetMapping("/reservation/{pageNum}")
    public ResponseEntity<ListResult> findByName(
            @PathVariable(value = "pageNum") int pageNum,
            @RequestBody ReserveOrderSearch dto) {
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        return ResponseEntity.ok(reservationService.findAllByName(dto, pageRequest));
    }

    /**
     * 해당 공간의 예약된 리스트 조회 요청(선택된 기간/시간 기준)
     */
    @GetMapping("/reservations/isBooked")
    public ResponseEntity<List<ReserveCheckDto>> findBookedRoom(@RequestBody ReserveCheckDto reserveCheckDto) {
        log.info("findBookedRoom : {}", reserveCheckDto);
        return ResponseEntity.ok(reservationService.findBookedReservation(reserveCheckDto));
    }
}
