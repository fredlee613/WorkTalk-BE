package com.golfzonTech4.worktalk.repository.reservation.redis;

import com.golfzonTech4.worktalk.domain.TempRedisReservation;
import org.springframework.data.repository.CrudRepository;

public interface TempReservationRedisRepository extends CrudRepository<TempRedisReservation, String> {
    Iterable<TempRedisReservation> findByRoomId(String roomId);
}
