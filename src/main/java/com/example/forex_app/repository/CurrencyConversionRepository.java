package com.example.forex_app.repository;

import com.example.forex_app.model.CurrencyConversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, Long> {
    Optional<CurrencyConversion> findByTransactionId(String transactionId);

    List<CurrencyConversion> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}