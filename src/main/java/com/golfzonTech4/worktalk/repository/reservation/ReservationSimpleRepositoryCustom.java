package com.golfzonTech4.worktalk.repository.reservation;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.Reservation;
import com.golfzonTech4.worktalk.domain.ReserveStatus;
import com.golfzonTech4.worktalk.dto.reservation.ReserveCheckDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import com.golfzonTech4.worktalk.repository.ListResult;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationSimpleRepositoryCustom {

    public ListResult findAllByUser(String name, Integer paid, PaymentStatus paymentStatus, ReserveStatus reserveStatus);
    PageImpl<ReserveSimpleDto> findAllByUserPage(String name, PageRequest pageRequest,
                                                 ReserveStatus reserveStatus, Integer spaceType);

    PageImpl<ReserveSimpleDto> findAllByHostPage(String name, PageRequest pageRequest,
                                                 ReserveStatus reserveStatus, Integer spaceType);
    List<ReserveSimpleDto> findAllByTime();
    Long countNoShow(Long memberId, ReserveStatus reserveStatus);

    List<ReserveCheckDto> findBookedOffice(Long roomId, LocalDate initDate, LocalDate endDate);
    List<ReserveCheckDto> findBookedRoom(Long roomId, LocalDate initDate);
    List<ReserveCheckDto> checkBookedRoom(Long roomId, LocalDate initDate, Integer checkInTime, Integer checkOutTime);

    List<Long> findByRooms(Long roomId);
    List<Long> findBySpaces(Long spaceId);


    Optional<ReserveSimpleDto> findRoomName(Long reserveId);
}
