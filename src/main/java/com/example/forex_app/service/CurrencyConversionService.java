package com.example.forex_app.service;

import com.example.forex_app.model.CurrencyConversion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CurrencyConversionService {
    CurrencyConversion convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency);

    CurrencyConversion getConversionByTransactionId(String transactionId);

    List<Map<String, Object>> getConversionHistory(String transactionId, String transactionDate);

}