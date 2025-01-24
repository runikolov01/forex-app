package com.example.forex_app.service.impl;

import com.example.forex_app.external.ExternalAPIClient;
import com.example.forex_app.model.ConversionResponse;
import com.example.forex_app.model.ExchangeRate;
import com.example.forex_app.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private ExternalAPIClient externalAPIClient;

    @Override
    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) {
        BigDecimal rate = externalAPIClient.getExchangeRate(fromCurrency, toCurrency);

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setFromCurrency(fromCurrency);
        exchangeRate.setToCurrency(toCurrency);
        exchangeRate.setRate(rate);

        return exchangeRate;
    }

    @Override
    public BigDecimal fetchExternalExchangeRate(String fromCurrency, String toCurrency) {
        return externalAPIClient.getExchangeRate(fromCurrency, toCurrency);
    }

    @Override
    public ConversionResponse convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        BigDecimal exchangeRate = externalAPIClient.getExchangeRate(fromCurrency, toCurrency);
        BigDecimal convertedAmount = amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);

        String transactionId = UUID.randomUUID().toString();

        return new ConversionResponse(
                transactionId,
                convertedAmount,
                fromCurrency,
                toCurrency,
                amount,
                exchangeRate
        );
    }
}