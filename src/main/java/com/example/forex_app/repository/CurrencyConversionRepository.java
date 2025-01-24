package com.example.forex_app.repository;

import com.example.forex_app.model.CurrencyConversion;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, Long> {
    Optional<CurrencyConversion> findByTransactionId(String transactionId);

    Page<CurrencyConversion> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}