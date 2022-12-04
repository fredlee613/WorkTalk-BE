package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.dto.reservation.ReserveOrderSearch;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.PayService;
import com.golfzonTech4.worktalk.service.ReservationService;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.AllArgsConstructor;
import lombok.Data;
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
        return ResponseEntity.ok(count);
    }

//    /**
//     * 예약 리스트 조회 요청 (유저 기준: Spring Data JPA + JPQL)
//     */
//    @GetMapping("/reservations/user")
//    public ResponseEntity<List<ReserveSimpleDto>> findAllByUser() {
//        log.info("findAllByUser");
//        return ResponseEntity.ok(reservationService.findAllByUser());
//    }

    /**
     * 예약 리스트 조회 요청(유저 기준: QueryDsl)
     * 전체 조회 개수와 리스트 반환
     */
    @GetMapping("/reservations/user")
    public ResponseEntity<ListResult> findAllByUser(
            @RequestBody() ReserveOrderSearch reserveOrderSearch) {
        log.info("findAllByUser");
        ListResult findReserves = reservationService.findAllByUser(reserveOrderSearch);
        return ResponseEntity.ok(findReserves);
    }

    /**
     * 예약 리스트 조회 페이징 요청(유저 기준: QueryDsl)
     */
    @GetMapping("/reservations/user/{pageNum}")
    public ResponseEntity<ListResult> findAllByUserPage(
            @PathVariable(name = "pageNum") int pageNum,
            @RequestBody ReserveOrderSearch reserveOrderSearch) {
        log.info("findAllByUser, {}, {}", pageNum, reserveOrderSearch);
        return ResponseEntity.ok(reservationService.findAllByUserPage(pageNum, reserveOrderSearch));
    }

    /**
     * 예약 리스트 조회 요청 (호스트 기준)
     */
    @GetMapping("/reservations/host")
    public ResponseEntity<ListResult> findAllByHost() {
        log.info("findAllByUser");
        ListResult findReserve = reservationService.findAllByHost();
        return ResponseEntity.ok(findReserve);
    }

    /**
     * 예약 리스트 조회 요청 (호스트 기준)
     */
    @GetMapping("/reservations/host/{pageNum}")
    public ResponseEntity<ListResult> findAllByHostPage(
            @PathVariable(name = "pageNum") int pageNum,
            @RequestBody ReserveOrderSearch reserveOrderSearch) {
        log.info("findAllByHostPage, {}, {}", pageNum, reserveOrderSearch);
        PageRequest pageRequest = PageRequest.of(pageNum, 10);
        ListResult findReserve = reservationService.findAllByHostPage(reserveOrderSearch, pageRequest);
        return ResponseEntity.ok(findReserve);
    }


    /**
     * 예약 리스트 조회 요청(선택된 기간/시간 기준)
     */
    @GetMapping("/reservations/isBooked")
    public ResponseEntity<List<ReserveCheckDto>> findBookedRoom(@RequestBody ReserveCheckDto reserveCheckDto) {
        log.info("findBookedRoom : {}", reserveCheckDto);
        return ResponseEntity.ok(reservationService.findBookedReservation(reserveCheckDto));
    }

    @Data
    @AllArgsConstructor
    private class Result<T> {
        private int count;
        private T data;
    }

}
