package com.example.demo.repository;

import com.example.demo.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NamedLockRepository extends JpaRepository<Stock, Long> {

	@Query(value = "select get_lock(:key, 1000)", nativeQuery = true)
	void getLock(@Param("key") String key);

	@Query(value = "select release_lock(:key)", nativeQuery = true)
	void releaseLock(@Param("key") String key);
}
