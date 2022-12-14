package com.golfzonTech4.worktalk.repository.reservation.temp;

import com.golfzonTech4.worktalk.domain.TempReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.lang.annotation.Native;

public interface TempReservationRepository extends JpaRepository<TempReservation, Long>, TempReservationRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    <S extends TempReservation> S save(S entity);

    @Query(value = "delete from temp_reservation where reserve_date < now() - interval '1 minute'", nativeQuery = true)
    void deleteByTime();
}
