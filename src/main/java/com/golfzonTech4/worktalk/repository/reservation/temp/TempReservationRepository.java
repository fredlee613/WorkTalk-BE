package com.golfzonTech4.worktalk.repository.reservation.temp;

import com.golfzonTech4.worktalk.domain.TempReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempReservationRepository extends JpaRepository<TempReservation, Long>, TempReservationRepositoryCustom {
}
