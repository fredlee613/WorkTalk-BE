package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.BookDate;
import com.golfzonTech4.worktalk.domain.TempRedisReservation;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveTempDto;
import com.golfzonTech4.worktalk.repository.reservation.ReservationSimpleRepository;
import com.golfzonTech4.worktalk.repository.reservation.redis.TempReservationRedisRepository;
import com.golfzonTech4.worktalk.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TempRedisReservationService {
    private final TempReservationRedisRepository tempReservationRedisRepository;

    @Transactional
    public String reserveTemp(ReserveTempDto dto) {
        log.info("reserveTemp: {}", dto);
        BookDate bookDate = new BookDate(LocalDateTime.now(), dto.getCheckInDate(), dto.getCheckOutDate(), dto.getCheckInTime(), dto.getCheckOutTime());

        checkBookRoom(dto); // 중복 예약 있을 시 예외 처리

        TempRedisReservation tempRedis = TempRedisReservation.makeTempReservation(dto.getMemberId(), dto.getRoomId(), bookDate);
        String findId = tempReservationRedisRepository.save(tempRedis).getTempReserveId();
        return findId;
    }

    @Transactional
    public void deleteTemp(String tempId) {
        log.info("deleteTemp : {}", tempId);
        tempReservationRedisRepository.deleteById(tempId);
    }

    public List<TempRedisReservation> findAll() {
        log.info("findAll");
        List<TempRedisReservation> result = new ArrayList();
        Iterable<TempRedisReservation> all = tempReservationRedisRepository.findAll();
        for (TempRedisReservation temp : all) {
            result.add(temp);
        }
        return result;
    }

    public TempRedisReservation findById(String tempReserveId) {
        log.info("findOne : {}", tempReserveId);
        TempRedisReservation result = tempReservationRedisRepository.findById(tempReserveId).get();
        log.info("result: {}", result);
        return result;
    }

    public List<ReserveCheckDto> findTempRooms(Long roomId, Long spaceType, LocalDate initDate, LocalDate endDate, int initTime, int endTime ) {
        log.info("findTempRooms : {}, {}, {}, {}, {}", roomId, initDate, endDate, initTime, endTime);
        List<ReserveCheckDto> result = new ArrayList();
        Iterable<TempRedisReservation> all = tempReservationRedisRepository.findByRoomId(String.valueOf(roomId));
        for (TempRedisReservation temp : all) {
            log.info("temp : {}", temp);
            if (spaceType == 1) { // 오피스의 경우 입실/퇴실날짜가 예약 기간에 포함되는 경우
                if (isBetweenDate(temp.getBookDate().getCheckInDate(), temp.getBookDate().getCheckOutDate(), initDate, endDate)) {
                    if (temp != null) {
                        result.add(new ReserveCheckDto(temp.getRoomId(), temp.getBookDate().getCheckInDate(), temp.getBookDate().getCheckOutDate(), temp.getBookDate().getCheckInTime(), temp.getBookDate().getCheckOutTime()));
                    }
                }
            } else { // 회의실, 데스크의 경우 입실/퇴실 시간이 예약 기간에 포함되는 경우
                if (isBetweenTime(temp.getBookDate().getCheckInDate(), temp.getBookDate().getCheckInTime(), temp.getBookDate().getCheckOutTime(), initDate, initTime, endTime)) {
                    if (temp != null) {
                        result.add(new ReserveCheckDto(temp.getRoomId(), temp.getBookDate().getCheckInDate(), temp.getBookDate().getCheckOutDate(), temp.getBookDate().getCheckInTime(), temp.getBookDate().getCheckOutTime()));
                    }
                }
            }
        }

        return result;
    }


    public void checkBookRoom(ReserveTempDto dto) {
        log.info("findBookedRoom : {}", dto);
        List<TempRedisReservation> result = new ArrayList();
        Iterable<TempRedisReservation> all = tempReservationRedisRepository.findByRoomId(String.valueOf(dto.getRoomId()));
        if (all != null) {
            for (TempRedisReservation temp : all) {
                log.info("temp : {}", temp);
                if (dto.getSpaceType() == 1) { // 오피스의 경우 입실/퇴실날짜가 예약 기간에 포함되는 경우
                    if (isBetweenDate(temp.getBookDate().getCheckInDate(), temp.getBookDate().getCheckOutDate(), dto.getCheckInDate(), dto.getCheckOutDate())) {
                        throw new RuntimeException("이미 예약된 오피스입니다.");
                    }
                } else { // 회의실, 데스크의 경우 입실/퇴실 시간이 예약 기간에 포함되는 경우
                    if (isBetweenTime(temp.getBookDate().getCheckInDate(), temp.getBookDate().getCheckInTime(), temp.getBookDate().getCheckOutTime(), dto.getCheckInDate(), dto.getCheckInTime(), dto.getCheckOutTime())) {
                        throw new RuntimeException("이미 예약된 회의실/데스크입니다.");
                    }
                }
            }
        }
    }

    static Boolean isBetweenDate(LocalDate checkInDate, LocalDate checkOutDate, LocalDate initDate, LocalDate endDate) {
        if ((checkInDate.isAfter(initDate.minusDays(1)) && checkInDate.isBefore(endDate.plusDays(1)))
                || (checkOutDate.isAfter(initDate.minusDays(1)) && checkOutDate.isBefore(endDate.plusDays(1)))) {
            return true;
        } else return false;
    }

    static Boolean isBetweenTime(LocalDate checkInDate, int checkInTime, int checkOutTime, LocalDate initDate, int initTime, int endTime) {
        if (checkInDate.isEqual(initDate) && ((checkInTime >= initTime && checkInTime < endTime) ||
                (checkOutTime > initTime && checkOutTime <= endTime))) {
            return true;
        } else return false;
    }
}
