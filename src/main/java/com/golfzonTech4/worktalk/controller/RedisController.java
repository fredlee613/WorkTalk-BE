package com.golfzonTech4.worktalk.controller;

import com.golfzonTech4.worktalk.domain.TempRedisReservation;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveTempDto;
import com.golfzonTech4.worktalk.facade.RedissonFacade;
import com.golfzonTech4.worktalk.service.TempRedisReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "RedisController", description = "레디스 테스트 관련 api입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class RedisController {

    private final TempRedisReservationService service;
    private final RedissonFacade facade;

    /**
     * 예약 날짜 및 시간 선택
     */
    @PostMapping("/redis/reservation/choose")
    public ResponseEntity choose(@RequestBody ReserveTempDto dto) throws InterruptedException {

        log.info("choose: {}", dto);
        String result = facade.chooseRoom(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * 해당 임시 예약건 삭제 요청
     */
    @GetMapping("/redis/reservation/delete/{tempReserveId}")
    public ResponseEntity<Long> delete(@PathVariable String tempRedisReserveId)  {

        log.info("delete: {}", tempRedisReserveId);
        service.deleteTemp(tempRedisReserveId);
        return ResponseEntity.ok().build();
    }

    /**
     * 해당 임시 예약 단건 조회 요청
     */
    @GetMapping("/redis/reservation/findById")
    public ResponseEntity<TempRedisReservation> findById(@RequestParam String tempRedisReserveId)  {

        log.info("findById: {}", tempRedisReserveId);
        TempRedisReservation result = service.findById(tempRedisReserveId);
        return ResponseEntity.ok(result);
    }

    /**
     * 해당 임시예약건 전체 조회
     */
    @GetMapping("/redis/reservation/findAll")
    public ResponseEntity<List<TempRedisReservation>> findAll()  {

        log.info("findAll");
        List<TempRedisReservation> result = service.findAll();
        return ResponseEntity.ok(result);
    }


    /**
     * 중복예약 검증 로직
     */
    @GetMapping("/redis/reservation/checkRooms")
    public ResponseEntity checkRooms(@ModelAttribute ReserveTempDto dto)  {
        log.info("checkRooms");
        service.checkBookRoom(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 임시 데이터 조회
     */
    @GetMapping("/redis/reservation/findTempRooms")
    public ResponseEntity<List<ReserveCheckDto>> findByRoom(@ModelAttribute ReserveTempDto dto)  {
        log.info("findTempRooms");
        List<ReserveCheckDto> result = service.findTempRooms(dto.getRoomId(), dto.getSpaceType(), dto.getCheckInDate(), dto.getCheckOutDate(), dto.getCheckInTime(), dto.getCheckOutTime());
        return ResponseEntity.ok(result);
    }
}
