package com.golfzonTech4.worktalk.repository.mileage;

import com.golfzonTech4.worktalk.domain.Mileage;
import com.golfzonTech4.worktalk.domain.Mileage_status;
import com.golfzonTech4.worktalk.dto.mileage.MileageFindDto;
import com.golfzonTech4.worktalk.dto.reservation.ReserveSimpleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MileageRepository extends JpaRepository<Mileage, Long>, MileageRepositoryCustom {

    @Query("select " +
            "new com.golfzonTech4.worktalk.dto.mileage.MileageFindDto" +
            "(m.mileageId, ro.roomName,p.payId, m.status, m.mileageAmount, m.mileageDate)" +
            "from Mileage m " +
            "join m.pay p " +
            "join p.reservation r " +
            "join r.room ro " +
            "where m.member.name = :name " +
            "order by r.reserveId desc ")
    List<MileageFindDto> findAllByName(@Param("name") String name);

    List<Mileage> findAllByPay(Long payId);

    @Query("select m from Mileage m where m.pay.payId = :payId and m.status = :status")
    Optional<Mileage> findByPay(@Param("payId") Long payId, @Param("status") Mileage_status status);
}
