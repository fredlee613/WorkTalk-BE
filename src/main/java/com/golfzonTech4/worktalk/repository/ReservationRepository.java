package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
