package com.example.demo.service;

import com.example.demo.domain.Stock;
import com.example.demo.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;

	public void decrease(Long productId, Long quantity) {
		Stock stock = stockRepository.findByProductId(productId);
		stock.decrease(quantity);

		stockRepository.saveAndFlush(stock);
	}

	public synchronized void decreaseOfSynchronized(Long productId, Long quantity) {
		Stock stock = stockRepository.findByProductId(productId);
		stock.decrease(quantity);

		stockRepository.saveAndFlush(stock);
	}

	public void decreaseOfPessimisticLock(Long productId, Long quantity) {
		Stock stock = stockRepository.findByProductIdOfPessimisticLock(productId);
		stock.decrease(quantity);

		stockRepository.saveAndFlush(stock);
	}
}
