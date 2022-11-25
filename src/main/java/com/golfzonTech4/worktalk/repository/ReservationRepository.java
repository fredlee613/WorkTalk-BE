package com.golfzonTech4.worktalk.repository;

import com.golfzonTech4.worktalk.domain.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationRepository {

    private final EntityManager em;

    public void save(Reservation reservation) {
        log.info("save : {}", reservation);
        em.persist(reservation);
    }

    public void cancel(Reservation reservation) {
        log.info("cancel : {}", reservation);
        em.remove(reservation);
    }

    public Reservation findOne(Long id) {
        log.info("findOne : {}", id);
        Reservation findReserve = em.find(Reservation.class, id);
        return findReserve;
    }

}
