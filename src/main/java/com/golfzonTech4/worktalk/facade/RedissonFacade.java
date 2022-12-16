package com.golfzonTech4.worktalk.facade;

import com.golfzonTech4.worktalk.dto.reservation.ReserveTempDto;
import com.golfzonTech4.worktalk.service.TempRedisReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedissonFacade {
    private final RedissonClient redissonClient;
    private final TempRedisReservationService redisReservationService;
    public final String LOCK_KEY = "LOCK_KEY";

    @Transactional
    public String chooseRoom(ReserveTempDto dto) {
        RLock lock = redissonClient.getLock(LOCK_KEY);
        List<String> result = new ArrayList<>();
        try {
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return null;
            }
            String findKey = redisReservationService.reserveTemp(dto);
            result.add(findKey);
            log.info("findKey: {}", findKey);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return result.get(0);
    }

}
