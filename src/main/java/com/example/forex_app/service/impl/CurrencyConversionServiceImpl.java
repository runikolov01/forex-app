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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        conversion.setExchangeRate(exchangeRate.getRate());

        currencyConversionRepository.save(conversion);

        return conversion;
    }

    @Override
    public CurrencyConversion getConversionByTransactionId(String transactionId) {
        return currencyConversionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<Map<String, Object>> getConversionHistory(String transactionId, String transactionDate) {
        // Assuming you want to fetch by date and/or transactionId
        List<CurrencyConversion> conversions = currencyConversionRepository.findByTransactionDateBetween(
                LocalDateTime.parse(transactionDate + "T00:00:00"),
                LocalDateTime.parse(transactionDate + "T23:59:59")
        );
        return conversions.stream()
                .map(conversion -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("transactionId", conversion.getTransactionId());
                    response.put("fromCurrency", conversion.getFromCurrency());
                    response.put("toCurrency", conversion.getToCurrency());
                    response.put("amount", conversion.getAmount());
                    response.put("convertedAmount", conversion.getConvertedAmount());
                    response.put("exchangeRate", conversion.getExchangeRate());
                    return response;
                })
                .collect(Collectors.toList());
    }
}