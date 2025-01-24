package com.example.forex_app.service.impl;

import com.example.forex_app.model.CurrencyConversion;
import com.example.forex_app.model.ExchangeRate;
import com.example.forex_app.repository.CurrencyConversionRepository;
import com.example.forex_app.service.CurrencyConversionService;
import com.example.forex_app.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        try {
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
            if (exchangeRate == null) {
                throw new RuntimeException("Exchange rate not found for " + fromCurrency + " to " + toCurrency);
            }
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
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error during currency conversion: " + ex.getMessage());
        }
    }

    @Override
    public CurrencyConversion getConversionByTransactionId(String transactionId) {
        return currencyConversionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));
    }

    @Override
    public List<Map<String, Object>> getConversionHistory(String transactionId, String transactionDate, int page, int size) {
        try {
            if (transactionDate == null || !transactionDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new RuntimeException("Invalid date format. Expected format: yyyy-MM-dd");
            }

            PageRequest pageRequest = PageRequest.of(page, size);

            LocalDateTime parsedDate = LocalDateTime.parse(transactionDate + "T00:00:00");
            Page<CurrencyConversion> conversions = currencyConversionRepository.findByTransactionDateBetween(
                    parsedDate,
                    parsedDate.plusDays(1).minusSeconds(1),
                    pageRequest
            );

            return conversions.getContent().stream()
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
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching conversion history: " + ex.getMessage());
        }
    }
}