package com.golfzonTech4.worktalk.repository.reservation;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.Reservation;
import com.golfzonTech4.worktalk.domain.ReserveStatus;
import com.golfzonTech4.worktalk.domain.RoomType;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;

public interface ReservationSimpleRepository extends JpaRepository<Reservation, Long>, ReservationSimpleRepositoryCustom {


    @Query("select " +
            "new com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto" +
            "(ro.roomName, r.paid, r.reserveId,m.id, ro.roomId, b, m.name, r.reserveStatus, r.paymentStatus, ro.roomType, r.reserveAmount, r.cancelReason)" +
            "from Reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where s.member.name = :name " +
            "and b.checkOutDate > :date " +
            "and r.reserveStatus = 'BOOKED'" +
            "order by r.reserveId desc"
    )
    List<ReserveSimpleDto> findAllByHost(@Param("name") String name, @Param("date") LocalDate date);

    Long countNoShow(Long memberId, ReserveStatus reserveStatus);
}
