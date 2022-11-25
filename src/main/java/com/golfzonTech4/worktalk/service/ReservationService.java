package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.BookDate;
import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.domain.Reservation;
import com.golfzonTech4.worktalk.domain.Room;
import com.golfzonTech4.worktalk.dto.reservation.ReserveDto;
import com.golfzonTech4.worktalk.exception.NotFoundMemberException;
import com.golfzonTech4.worktalk.repository.MyRoomRepository;
import com.golfzonTech4.worktalk.repository.ReservationRepository;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final MemberService memberService;
    private final MyRoomRepository myRoomRepository;
    private final ReservationRepository reservationRepository;


    @Transactional
    public Long reserve(ReserveDto reserveDto) {
        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        if (currentUsername.isEmpty()) throw new NotFoundMemberException("Member not found");

        Member findMember = memberService.findByName(currentUsername.get());
        log.info("findMember : {}", findMember);

        Room findRoom = myRoomRepository.findOne(reserveDto.getRoom_id());
        log.info("findRoom : {}", findRoom);

        BookDate bookDate = new BookDate(
                LocalDateTime.now(),
                reserveDto.getCheckInDate(),
                reserveDto.getCheckOutDate(),
                reserveDto.getCheckInTime(),
                reserveDto.getCheckOutTime());

        Reservation reservation = Reservation.makeReservation(findMember, findRoom, bookDate);
//        reservationRepository.save(reservation);

        return reservationRepository.save(reservation).getReserveId();
    }

    @Transactional
    public LocalDateTime cancel(Long reserveId, String cancelReason) {
//        Reservation findReservation = reservationRepository.findOne(reserveId);

        // 해당 값이 Null일 경우 NoSuchElementException 발생
        Reservation findReservation = reservationRepository.findById(reserveId).get();

        LocalDate checkInDate = findReservation.getBookDate().getCheckInDate();
        Integer checkInTime = findReservation.getBookDate().getCheckInTime();
        LocalDateTime initTime = BookDate.getInitTime(checkInDate, checkInTime);

        int periodMinutes = BookDate.getPeriodMinutes(LocalDateTime.now(), initTime);
        log.info("periodMinutes : {}", periodMinutes);

        // 사용 시간 전부터는 예약 취소 불가
        if (periodMinutes < 60) {
            throw new IllegalStateException("취소가 가능한 시간을 지났습니다.");
        }

        findReservation.getBookDate().setCancelDate(LocalDateTime.now());
        findReservation.setCancelReason(cancelReason);

        return findReservation.getBookDate().getCancelDate();
    }
}
