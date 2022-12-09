package com.golfzonTech4.worktalk.repository.reservation.query;

import com.golfzonTech4.worktalk.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepositoryQuery extends JpaRepository<Reservation, Long>, ReservationRepositoryQueryCustom {

}
