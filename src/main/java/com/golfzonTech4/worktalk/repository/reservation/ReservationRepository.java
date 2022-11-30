package com.golfzonTech4.worktalk.repository.reservation;

import com.golfzonTech4.worktalk.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    /**
     * 접속자가 가진 예약 리스트를 조회하는 쿼리 => 성능 문제 발생 (reservation 조회 => member 조회 => room 조회 => member 조회)
     */
    @Query("select r from Reservation r where r.member.name = :name order by r.reserveId desc ")
    public List<Reservation> findAllByName(@Param("name") String name);

    Reservation findByReserveId(Long reserveId); //예약번호 조회

}
