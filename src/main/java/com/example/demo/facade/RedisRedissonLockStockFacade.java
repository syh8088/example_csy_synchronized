package com.example.demo.facade;

import com.example.demo.service.StockService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisRedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;

    public void decrease(Long productId, Long quantity) {

        RLock lock = redissonClient.getLock(productId.toString());

        try {
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 확득 실패");
                return;
            }

            stockService.decrease(productId, quantity);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
    }
}
