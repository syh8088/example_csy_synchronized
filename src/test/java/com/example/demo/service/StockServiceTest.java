package com.example.demo.service;

import com.example.demo.domain.Stock;
import com.example.demo.facade.NamedLockStockFacade;
import com.example.demo.facade.OptimisticLockStockFacade;
import com.example.demo.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {

	@Autowired
	private StockService stockService;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private OptimisticLockStockFacade optimisticLockStockFacade;

	@Autowired
	private NamedLockStockFacade namedLockStockFacade;

	@BeforeEach
	public void before() {
		Stock stock = new Stock(1L, 100L);
		stockRepository.saveAndFlush(stock);
	}

	@AfterEach
	public void after() {
		stockRepository.deleteAll();
	}

	@Test
	public void stock_decrease() {
		stockService.decrease(1L, 1L);

		Stock stock = stockRepository.findByProductId(1L);

		assertEquals(99, stock.getQuantity());
	}

	@Test
	public void 동시에_100개_요청() throws InterruptedException {
		int threadCount = 100;

		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					stockService.decrease(1L, 1L);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		Stock stock = stockRepository.findByProductId(1L);

		assertEquals(1, stock.getQuantity());
	}

	@Test
	public void 동시에_100개_요청_Synchronized() throws InterruptedException {
		int threadCount = 100;

		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					stockService.decreaseOfSynchronized(1L, 1L);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		Stock stock = stockRepository.findByProductId(1L);

		assertEquals(0, stock.getQuantity());
	}

	@Test
	public void 동시에_100개_요청_PessimisticLock() throws InterruptedException {
		int threadCount = 100;

		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					stockService.decreaseOfPessimisticLock(1L, 1L);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		Stock stock = stockRepository.findByProductId(1L);

		assertEquals(0, stock.getQuantity());
	}

	@Test
	public void 동시에_100개_요청_OptimisticLock() throws InterruptedException {
		int threadCount = 100;

		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					optimisticLockStockFacade.decrease(1L, 1L);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		Stock stock = stockRepository.findByProductId(1L);

		assertEquals(0, stock.getQuantity());
	}

	@Test
	public void 동시에_100개_요청_NamedLock() throws InterruptedException {
		int threadCount = 100;

		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					namedLockStockFacade.decrease(1L, 1L);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		Stock stock = stockRepository.findByProductId(1L);

		assertEquals(0, stock.getQuantity());
	}
}