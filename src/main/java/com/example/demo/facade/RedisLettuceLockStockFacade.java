package com.example.demo.facade;

import com.example.demo.repository.RedisLockRepository;
import com.example.demo.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLettuceLockStockFacade {

    private final StockService stockService;
    private final RedisLockRepository redisLockRepository;

    public void decrease(Long productId, Long quantity) throws InterruptedException {

        while (!redisLockRepository.lock(productId)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(productId, quantity);
        }
        finally {
            redisLockRepository.unLock(productId);
        }

    }
}
