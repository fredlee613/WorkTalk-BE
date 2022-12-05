package com.golfzonTech4.worktalk.repository.pay;

import com.golfzonTech4.worktalk.domain.Pay;
import com.golfzonTech4.worktalk.domain.PaymentStatus;
import com.golfzonTech4.worktalk.dto.pay.PaySimpleDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long>, PayRepositoryCustom {
    Optional<Pay> findByCustomerUid(String customerUid);

    @Query("select " +
            "new com.golfzonTech4.worktalk.dto.pay.PaySimpleDto" +
            "(b.reserveDate, s.spaceName, ro.roomName, p.payAmount, p.payStatus, r.reserveStatus, m.name) " +
            "from Pay p " +
            "join p.reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where m.name = :name " +
            "order by b.reserveDate desc ")
    List<PaySimpleDto> findAllByUser(@Param("name") String name);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.pay.PaySimpleDto" +
            "(b.reserveDate, s.spaceName, ro.roomName, p.payAmount, p.payStatus, r.reserveStatus, m.name) " +
            "from Pay p " +
            "join p.reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where m.name = :name " +
            "order by b.reserveDate desc ",
            countQuery = "select " +
                    "count(p)" +
                    "from Pay p " +
                    "join p.reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where m.name = :name "
    )
    Page<PaySimpleDto> findAllByUser(@Param("name") String name, PageRequest pageRequest);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.pay.PaySimpleDto" +
            "(b.reserveDate, s.spaceName, ro.roomName, p.payAmount, p.payStatus, r.reserveStatus, m.name) " +
            "from Pay p " +
            "join p.reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where m.name = :name " +
            "and b.reserveDate >= :time " +
            "order by b.reserveDate desc ",
            countQuery = "select " +
                    "count(p)" +
                    "from Pay p " +
                    "join p.reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where m.name = :name " +
                    "and b.reserveDate >= :time "
    )
    Page<PaySimpleDto> findAllByUser(@Param("name") String name, @Param("time") LocalDateTime time, PageRequest pageRequest);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.pay.PaySimpleDto" +
            "(b.reserveDate, s.spaceName, ro.roomName, p.payAmount, p.payStatus, r.reserveStatus, m.name) " +
            "from Pay p " +
            "join p.reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where m.name = :name " +
            "and p.payStatus = :payStatus " +
            "order by b.reserveDate desc ",
            countQuery = "select " +
                    "count(p)" +
                    "from Pay p " +
                    "join p.reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where m.name = :name " +
                    "and p.payStatus = :payStatus"
    )
    Page<PaySimpleDto> findAllByUser(@Param("name") String name, @Param("payStatus") PaymentStatus payStatus, PageRequest pageRequest);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.pay.PaySimpleDto" +
            "(b.reserveDate, s.spaceName, ro.roomName, p.payAmount, p.payStatus, r.reserveStatus, m.name) " +
            "from Pay p " +
            "join p.reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where m.name = :name " +
            "and b.reserveDate >= :time " +
            "and p.payStatus = :payStatus " +
            "order by b.reserveDate desc ",
            countQuery = "select " +
                    "count(p)" +
                    "from Pay p " +
                    "join p.reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where m.name = :name " +
                    "and b.reserveDate >= :time " +
                    "and p.payStatus = :payStatus")
    Page<PaySimpleDto> findAllByUser(@Param("name") String name,
                                     @Param("time") LocalDateTime time,
                                     @Param("payStatus") PaymentStatus payStatus,
                                     PageRequest pageRequest);

    @Query("select " +
            "new com.golfzonTech4.worktalk.dto.pay.PaySimpleDto" +
            "(b.reserveDate, s.spaceName, ro.roomName, p.payAmount, p.payStatus, r.reserveStatus, m.name) " +
            "from Pay p " +
            "join p.reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where s.member.name = :name " +
            "order by b.reserveDate desc ")
    List<PaySimpleDto> findAllByHost(@Param("name") String name);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.pay.PaySimpleDto" +
            "(b.reserveDate, s.spaceName, ro.roomName, p.payAmount, p.payStatus, r.reserveStatus, m.name) " +
            "from Pay p " +
            "join p.reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where s.member.name = :name " +
            "order by b.reserveDate desc ",
            countQuery = "select " +
                    "count(p)" +
                    "from Pay p " +
                    "join p.reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where s.member.name = :name ")
    Page<PaySimpleDto> findAllByHost(@Param("name") String name, PageRequest pageRequest);

    @Query(value = "select " +
            "new com.golfzonTech4.worktalk.dto.pay.PaySimpleDto" +
            "(b.reserveDate, s.spaceName, ro.roomName, p.payAmount, p.payStatus, r.reserveStatus, m.name) " +
            "from Pay p " +
            "join p.reservation r " +
            "join r.member m " +
            "join r.bookDate b " +
            "join r.room ro " +
            "join ro.space s " +
            "where s.member.name = :name " +
            "and b.reserveDate >= :time " +
            "order by b.reserveDate desc ",
            countQuery = "select " +
                    "count(p)" +
                    "from Pay p " +
                    "join p.reservation r " +
                    "join r.member m " +
                    "join r.bookDate b " +
                    "join r.room ro " +
                    "join ro.space s " +
                    "where s.member.name = :name " +
                    "and b.reserveDate >= :time ")
    Page<PaySimpleDto> findAllByHost(@Param("name") String name, @Param("time") LocalDateTime time, PageRequest pageRequest);

}
