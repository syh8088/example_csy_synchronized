package com.example.demo.facade;

import com.example.demo.repository.NamedLockRepository;
import com.example.demo.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {

    private final StockService stockService;
    private final NamedLockRepository namedLockRepository;

    public void decrease(Long productId, Long quantity) {

        try {
            namedLockRepository.getLock(productId.toString());
            stockService.decrease(productId, quantity);
        }
        finally {
            namedLockRepository.releaseLock(productId.toString());
        }
    }
}
