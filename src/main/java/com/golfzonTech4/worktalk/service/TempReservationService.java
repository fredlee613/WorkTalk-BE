package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.BookDate;
import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.domain.RoomType;
import com.golfzonTech4.worktalk.domain.TempReservation;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveTempDto;
import com.golfzonTech4.worktalk.repository.reservation.ReservationSimpleRepository;
import com.golfzonTech4.worktalk.repository.reservation.temp.TempReservationRepository;
import com.golfzonTech4.worktalk.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TempReservationService {
    private final TempReservationRepository tempRepository;
    private final ReservationSimpleRepository reserveRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public Long reserveTemp(ReserveTempDto dto) {
        log.info("reserveTemp: {}", dto);
        BookDate bookDate = new BookDate(LocalDateTime.now(), dto.getCheckInDate(), dto.getCheckOutDate(), dto.getCheckInTime(), dto.getCheckOutTime());

        // 중복 여부 검증 전 DB 최신화
        tempRepository.deleteByTime(); // 작성한지 1분이 경과한 임시 데이터 삭제

        // 중복 여부 검증
        Room findRoom = roomRepository.findById(dto.getRoomId()).get();
        if (findRoom.getRoomType().equals(RoomType.OFFICE)) {
            List<ReserveCheckDto> reserveOffices = reserveRepository.findBookedOffice(dto.getRoomId(), dto.getCheckInDate(), dto.getCheckOutDate());
            List<ReserveCheckDto> tempOffices = tempRepository.findBookedOffice(dto.getRoomId(), dto.getCheckInDate(), dto.getCheckOutDate());
            if (!reserveOffices.isEmpty() || !tempOffices.isEmpty()) throw new IllegalStateException("이미 예약된 공간입니다.");
        } else {
            List<ReserveCheckDto> reserveRooms = reserveRepository.checkBookedRoom(dto.getRoomId(), RoomType.DESK, dto.getCheckInDate(), dto.getCheckInTime(), dto.getCheckOutTime());
            List<ReserveCheckDto> tempRooms = tempRepository.checkBookedRoom(dto.getRoomId(), dto.getCheckInDate(), dto.getCheckInTime(), dto.getCheckOutTime());
            if (!reserveRooms.isEmpty() || !tempRooms.isEmpty()) throw new IllegalStateException("이미 예약된 공간입니다.");
        }

        TempReservation tempReservation = TempReservation.makeTempReservation(dto.getMemberId(), dto.getRoomId(), bookDate);
        Long id = tempRepository.save(tempReservation).getTempReserveId();
        return id;
    }

    @Transactional
    public void deleteTemp(Long tempId) {
        log.info("deleteTemp : {}", tempId);
        Optional<TempReservation> findTemp = tempRepository.findById(tempId);
        if (findTemp.isPresent()) tempRepository.delete(findTemp.get());
        if (tempRepository.findById(tempId).isPresent()) {
            throw new IllegalStateException("해당 데이터가 삭제되지 않았습니다.");
        }
    }
}
