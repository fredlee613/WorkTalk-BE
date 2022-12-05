package com.golfzonTech4.worktalk.repository.reservation;

import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.domain.Reservation;
import com.golfzonTech4.worktalk.domain.ReserveStatus;
import com.golfzonTech4.worktalk.domain.RoomType;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationSimpleRepository extends JpaRepository<Reservation, Long>, ReservationSimpleRepositoryCustom {

    // JPQL + Spring DATA JPA를 활용한 회원 예약 리스트 조회 (연관관계 초기화 이슈 없음.)

//    @Query("select " +
//            "new com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto" +
//            "(ro.roomName, r.paid, r.reserveId,m.id, ro.roomId, b, m.name, r.reserveStatus, r.paymentStatus, ro.roomType, r.reserveAmount)" +
//            "from Reservation r " +
//            "join r.member m " +
//            "join r.bookDate b " +
//            "join r.room ro " +
//            "where r.member.name = :name " +
//            "order by r.reserveId desc ")
//    List<ReserveSimpleDto> findAllByUser(@Param("name") String name);

    @Query("select " +
            "new com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto" +
            "(ro.roomName, r.paid, r.reserveId,m.id, ro.roomId, b, m.name, r.reserveStatus, r.paymentStatus, ro.roomType, r.reserveAmount)" +
            "from Reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where s.member.name = :name " +
            "order by r.reserveId desc"
    )
    List<ReserveSimpleDto> findAllByHost(@Param("name") String name);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto" +
            "(ro.roomName, r.paid, r.reserveId,m.id, ro.roomId, b, m.name, r.reserveStatus, r.paymentStatus, ro.roomType, r.reserveAmount)" +
            "from Reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where s.member.name = :name " +
            "order by r.reserveId desc ",
            countQuery = "select " +
                    "count(r)" +
                    "from Reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where s.member.name = :name ")
//    List<ReserveSimpleDto> findAllByHostPage(@Param("name") String name, PageRequest pageRequest);
    Page<ReserveSimpleDto> findAllByHostPage(@Param("name") String name, PageRequest pageRequest);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto" +
            "(ro.roomName, r.paid, r.reserveId,m.id, ro.roomId, b, m.name, r.reserveStatus, r.paymentStatus, ro.roomType, r.reserveAmount)" +
            "from Reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where s.member.name = :name " +
            "and r.paid = :paid " +
            "and r.paymentStatus = :paymentStatus " +
            "order by r.reserveId desc ",
            countQuery = "select " +
                    "count(r)" +
                    "from Reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where s.member.name = :name " +
                    "and r.paid = :paid " +
                    "and r.paymentStatus = :paymentStatus")
    Page<ReserveSimpleDto> findAllByHostPagePaid(
//    List<ReserveSimpleDto> findAllByHostPagePaid(
            @Param("name") String name,
            @Param("paid") Integer paid,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            PageRequest pageRequest);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto" +
            "(ro.roomName, r.paid, r.reserveId,m.id, ro.roomId, b, m.name, r.reserveStatus, r.paymentStatus, ro.roomType, r.reserveAmount)" +
            "from Reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where s.member.name = :name " +
            "and ro.roomType = :roomType " +
            "order by r.reserveId desc ",
            countQuery = "select " +
                    "count(r)" +
                    "from Reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where s.member.name = :name " +
                    "and ro.roomType = :roomType")
//    List<ReserveSimpleDto> findAllByHostPageRoom(
    Page<ReserveSimpleDto> findAllByHostPageRoom(
            @Param("name") String name,
            @Param("roomType") RoomType roomType,
            PageRequest pageRequest);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto" +
            "(ro.roomName, r.paid, r.reserveId,m.id, ro.roomId, b, m.name, r.reserveStatus, r.paymentStatus, ro.roomType, r.reserveAmount)" +
            "from Reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where s.member.name = :name " +
            "and r.paid = :paid " +
            "and r.paymentStatus = :paymentStatus " +
            "and ro.roomType = :roomType " +
            "order by r.reserveId desc ",
            countQuery = "select " +
                    "count(r)" +
                    "from Reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where s.member.name = :name " +
                    "and r.paid = :paid " +
                    "and r.paymentStatus = :paymentStatus " +
                    "and ro.roomType = :roomType")
    Page<ReserveSimpleDto> findAllByHostPageBoth(
//    List<ReserveSimpleDto> findAllByHostPageBoth(
            @Param("name") String name,
            @Param("paid") Integer paid,
            @Param("roomType") RoomType roomType,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            PageRequest pageRequest);

    // QueryDsl를 활용한 회원 예약 리스트 조회 (연관관계 초기화 이슈 존재 => 연관관계 depth >= 3일 경우 @QueryInit 필요)
    List<ReserveSimpleDto> findAllByTime();

    Long countNoShow(Long memberId, ReserveStatus reserveStatus);
}
