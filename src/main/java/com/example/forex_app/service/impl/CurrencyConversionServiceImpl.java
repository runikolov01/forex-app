package com.example.forex_app.service.impl;

import com.example.forex_app.model.CurrencyConversion;
import com.example.forex_app.model.ExchangeRate;
import com.example.forex_app.repository.CurrencyConversionRepository;
import com.example.forex_app.service.CurrencyConversionService;
import com.example.forex_app.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    @Autowired
    private CurrencyConversionRepository currencyConversionRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    public CurrencyConversion convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate());

        String transactionId = UUID.randomUUID().toString();

        CurrencyConversion conversion = new CurrencyConversion();
        conversion.setAmount(amount);
        conversion.setFromCurrency(fromCurrency);
        conversion.setToCurrency(toCurrency);
        conversion.setConvertedAmount(convertedAmount);
        conversion.setTransactionId(transactionId);
        conversion.setTransactionDate(LocalDateTime.now());

        currencyConversionRepository.save(conversion);

        return conversion;
    }

    @Override
    public CurrencyConversion getConversionByTransactionId(String transactionId) {
        return currencyConversionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<CurrencyConversion> getConversionsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return currencyConversionRepository.findByTransactionDateBetween(startDate, endDate);
    }
}