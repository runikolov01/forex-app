package com.example.forex_app.service;

import com.example.forex_app.model.ExchangeRate;

import java.math.BigDecimal;

public interface ExchangeRateService {
    ExchangeRate getExchangeRate(String fromCurrency, String toCurrency);
    BigDecimal fetchExternalExchangeRate(String fromCurrency, String toCurrency);
}