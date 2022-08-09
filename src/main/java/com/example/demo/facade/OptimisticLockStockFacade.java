package com.example.demo.facade;

import com.example.demo.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    public final StockService stockService;

    public void decrease(Long productId, Long quantity) throws InterruptedException {

        while (true) {
            try {
                stockService.decreaseOfOptimisticLock(productId, quantity);
                break;
            }
            catch (Exception e) {
                Thread.sleep(50);
            }
        }

    }
}
