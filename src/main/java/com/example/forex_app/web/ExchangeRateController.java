package com.example.forex_app.web;

import com.example.forex_app.model.ExchangeRate;
import com.example.forex_app.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/exchange-rate")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<ExchangeRate> getExchangeRate(@RequestParam String fromCurrency, @RequestParam String toCurrency) {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        return ResponseEntity.ok(exchangeRate);
    }

    @GetMapping("/external")
    public ResponseEntity<BigDecimal> fetchExternalExchangeRate(@RequestParam String fromCurrency, @RequestParam String toCurrency) {
        BigDecimal rate = exchangeRateService.fetchExternalExchangeRate(fromCurrency, toCurrency);
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {

        BigDecimal exchangeRate = exchangeRateService.fetchExternalExchangeRate(fromCurrency, toCurrency);
        BigDecimal convertedAmount = amount.multiply(exchangeRate);

        // Prepare response data
        Map<String, Object> conversionResponse = new HashMap<>();
        conversionResponse.put("amount", amount);
        conversionResponse.put("fromCurrency", fromCurrency);
        conversionResponse.put("toCurrency", toCurrency);
        conversionResponse.put("convertedAmount", convertedAmount);
        conversionResponse.put("exchangeRate", exchangeRate);

        return ResponseEntity.ok(conversionResponse);
    }
}