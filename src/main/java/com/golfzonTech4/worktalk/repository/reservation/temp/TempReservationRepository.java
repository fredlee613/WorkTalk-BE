package com.golfzonTech4.worktalk.repository.reservation.temp;

import com.golfzonTech4.worktalk.domain.TempReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

public interface TempReservationRepository extends JpaRepository<TempReservation, Long>, TempReservationRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    <S extends TempReservation> S save(S entity);
}
