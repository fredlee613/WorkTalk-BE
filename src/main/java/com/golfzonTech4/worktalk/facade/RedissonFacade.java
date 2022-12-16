package com.golfzonTech4.worktalk.facade;

import com.golfzonTech4.worktalk.dto.reservation.ReserveTempDto;
import com.golfzonTech4.worktalk.service.TempRedisReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
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
    public String chooseRoom(ReserveTempDto dto) throws InterruptedException {
        log.info("chooseRoom : {}", dto);
        RLock lock = redissonClient.getLock(LOCK_KEY);
        log.info("getLock....");
        List<String> result = new ArrayList<>();
        try {
            log.info("Try Lock ....");
            boolean available = lock.tryLock(10, 5, TimeUnit.SECONDS);
            log.info("Try Lock Done : available : {}", available);

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
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                log.info("try unlock....");
                lock.unlock();
                log.info("unlock success....");
            }
        }
        return result.get(0);
    }
//    @Transactional
//    public String chooseRoom(ReserveTempDto dto) throws InterruptedException {
//        log.info("chooseRoom: {}", dto);
//        RSemaphore semaphore = redissonClient.getSemaphore("mySemaphore");
//        log.info("get Semaphore...");
//        // 싱글 스레드 permit & 5초 대기
//        boolean res = semaphore.tryAcquire(5, 5, TimeUnit.SECONDS);
//        log.info("try acquire Semaphore...");
//        log.info("res: {}", res);
//        List<String> result = new ArrayList<>();
//
//        if (res) {
//            try {
//                log.info("try....");
//                String findKey = redisReservationService.reserveTemp(dto);
//                result.add(findKey);
//                log.info("findKey: {}", findKey);
//            } finally {
//                semaphore.release();
//            }
//        }
//        return result.get(0);
//    }

}
