package com.example.forex_app.service;

import com.example.forex_app.model.CurrencyConversion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CurrencyConversionService {
    CurrencyConversion convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency);

    CurrencyConversion getConversionByTransactionId(String transactionId);

    List<CurrencyConversion> getConversionsByDate(LocalDateTime startDate, LocalDateTime endDate);
}