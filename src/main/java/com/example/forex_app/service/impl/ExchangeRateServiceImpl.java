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
    private CurrencyConversionRepository currencyConversionRepository;  // To save conversion data

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
    public CurrencyConversion convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        BigDecimal exchangeRate = externalAPIClient.getExchangeRate(fromCurrency, toCurrency);
        BigDecimal convertedAmount = amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);

        String transactionId = UUID.randomUUID().toString();  // Generate unique transaction ID
        LocalDateTime transactionDate = LocalDateTime.now();  // Capture transaction date/time

        // Create and populate the CurrencyConversion entity
        CurrencyConversion conversion = new CurrencyConversion();
        conversion.setTransactionId(transactionId);
        conversion.setFromCurrency(fromCurrency);
        conversion.setToCurrency(toCurrency);
        conversion.setAmount(amount);
        conversion.setConvertedAmount(convertedAmount);
        conversion.setExchangeRate(exchangeRate);
        conversion.setTransactionDate(transactionDate);

        // Save the conversion to the database (optional step if needed)
        currencyConversionRepository.save(conversion);

        return conversion;  // Return the CurrencyConversion object instead of ConversionResponse
    }
}