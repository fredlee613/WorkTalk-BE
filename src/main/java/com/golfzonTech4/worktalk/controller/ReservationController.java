package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.MemberType;
import com.golfzonTech4.worktalk.domain.TempRedisReservation;
import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveOrderSearch;
import com.golfzonTech4.worktalk.dto.reservation.ReserveTempDto;
import com.golfzonTech4.worktalk.facade.RedissonFacade;
import com.golfzonTech4.worktalk.repository.ListResult;
import com.golfzonTech4.worktalk.service.ReservationService;
import com.golfzonTech4.worktalk.service.TempRedisReservationService;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "ReservationController", description = "예약 관련 api입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;
    private final TempRedisReservationService redisReservationService;
    private final RedissonFacade facade;

    /**
     * 예약 날짜 및 시간 선택
     */
    @Operation(summary = "예약 일자 선택", description = "해당 공간에 예약 여부를 확인 후 없을 시 임시 예약이 진행됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/reservation/choose")
    public ResponseEntity<String> choose(@RequestBody ReserveTempDto dto) throws InterruptedException {

        log.info("choose: {}", dto);
        String result = facade.chooseRoom(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * 해당 임시 예약 단건 조회 요청
     */
    @GetMapping("/reservation/findById")
    public ResponseEntity<TempRedisReservation> findById(@RequestParam String tempRedisReserveId)  {

        log.info("findById: {}", tempRedisReserveId);
        TempRedisReservation result = redisReservationService.findById(tempRedisReserveId);
        return ResponseEntity.ok(result);
    }

    /**
     * 해당 임시 예약건 삭제 요청
     */
    @Operation(summary = "해당 임시 예약건 삭제 요청", description = "해당 공간에 대한 임시 예약 데이터를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "이미 예약된 오피스/데스트/회의실입니다.")
    })
    @GetMapping("/reservation/delete/{tempReserveId}")
    public ResponseEntity<Long> delete(@PathVariable String tempReserveId)  {

        log.info("delete: {}", tempReserveId);
        redisReservationService.deleteTemp(tempReserveId);
        return ResponseEntity.ok().build();
    }


    /**
     * 예약 및 결제 요청
     */
    @Operation(summary = "예약 및 결제 요청", description = "해당 예약건에 대한 결제를 진행한 후 해당 데이터들을 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/reservation/reserve")
    public ResponseEntity<Long> reserve(@RequestBody PayInsertDto payInsertDto) throws IamportResponseException, IOException, IllegalAccessException {

        log.info("reserve: {}", payInsertDto);
        Long result = reservationService.reserve(payInsertDto);
        return ResponseEntity.ok(result);
    }

    /**
     * 예약 취소 및 결제 취소 요청
     */
    @Operation(summary = "예약 취소 및 결제 취소 요청", description = "해당 예약건과 그에 연관되는 결제/ 마일리지 내역을 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
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
    @Operation(summary = "사용 종료 요청", description = "호스트에 의하여 해당 예약견의 상태를 이용완료로 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/reservation/end/{reserveId}")
    public ResponseEntity end(@PathVariable("reserveId") Long reserveId) {
        log.info("end: {}", reserveId);
        reservationService.end(reserveId);
        return ResponseEntity.ok().build();
    }

    /**
     * 예약 리스트 조회
     */
    @Operation(summary = "예약 리스트 조회", description = "접속된 회원을 기준으로 예약 내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ListResult.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/reservation/find")
    public ResponseEntity<ListResult> findByName(@ModelAttribute ReserveOrderSearch dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), 10);
        return ResponseEntity.ok(reservationService.findAllByName(dto, pageRequest));
    }

    /**
     * 해당 공간의 예약된 리스트 조회 요청(선택된 기간/시간 기준)
     */
    @Operation(summary = "해당 공간의 예약된 리스트 조회 요청(선택된 기간/시간 기준)", description = "사용자가 선택한 기간에 대한 예약 내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/reservations/isBooked")
    public ResponseEntity<List<ReserveCheckDto>> findBookedRoom(@ModelAttribute ReserveCheckDto reserveCheckDto) {
        log.info("findBookedRoom : {}", reserveCheckDto);
        return ResponseEntity.ok(reservationService.findBookedReservation(reserveCheckDto));
    }
}
