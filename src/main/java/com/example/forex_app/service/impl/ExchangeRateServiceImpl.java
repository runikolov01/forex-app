package com.example.forex_app.service.impl;

import com.example.forex_app.external.ExternalAPIClient;
import com.example.forex_app.model.CurrencyConversion;
import com.example.forex_app.model.ExchangeRate;
import com.example.forex_app.repository.CurrencyConversionRepository;
import com.example.forex_app.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private ExternalAPIClient externalAPIClient;

    @Autowired
    private CurrencyConversionRepository currencyConversionRepository;

    @Override
    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            BigDecimal rate = externalAPIClient.getExchangeRate(fromCurrency, toCurrency);

            if (rate == null) {
                throw new RuntimeException("Failed to fetch exchange rate from " + fromCurrency + " to " + toCurrency);
            }

            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setFromCurrency(fromCurrency);
            exchangeRate.setToCurrency(toCurrency);
            exchangeRate.setRate(rate);

            return exchangeRate;
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching exchange rate: " + ex.getMessage());
        }
    }

    @Override
    public BigDecimal fetchExternalExchangeRate(String fromCurrency, String toCurrency) {
        try {
            BigDecimal rate = externalAPIClient.getExchangeRate(fromCurrency, toCurrency);

            if (rate == null) {
                throw new RuntimeException("Failed to fetch exchange rate from " + fromCurrency + " to " + toCurrency);
            }

            return rate;
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching external exchange rate: " + ex.getMessage());
        }
    }

    @Override
    public CurrencyConversion convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        try {
            BigDecimal exchangeRate = externalAPIClient.getExchangeRate(fromCurrency, toCurrency);

            if (exchangeRate == null) {
                throw new RuntimeException("Exchange rate not available for " + fromCurrency + " to " + toCurrency);
            }

            BigDecimal convertedAmount = amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);

            String transactionId = UUID.randomUUID().toString();
            LocalDateTime transactionDate = LocalDateTime.now();

            CurrencyConversion conversion = new CurrencyConversion();
            conversion.setTransactionId(transactionId);
            conversion.setFromCurrency(fromCurrency);
            conversion.setToCurrency(toCurrency);
            conversion.setAmount(amount);
            conversion.setConvertedAmount(convertedAmount);
            conversion.setExchangeRate(exchangeRate);
            conversion.setTransactionDate(transactionDate);

            currencyConversionRepository.save(conversion);

            return conversion;
        } catch (Exception ex) {
            throw new RuntimeException("Error during currency conversion: " + ex.getMessage());
        }
    }
}