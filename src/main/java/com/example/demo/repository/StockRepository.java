package com.example.demo.repository;

import com.example.demo.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;

public interface StockRepository extends JpaRepository<Stock, Long> {

	Stock findByProductId(Long productId);

	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("select s from Stock s where s.productId = :productId")
	Stock findByProductIdOfPessimisticLock(@Param("productId") Long productId);

}
