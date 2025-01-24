package com.example.forex_app.web;

import com.example.forex_app.model.ConversionResponse;
import com.example.forex_app.model.ExchangeRate;
import com.example.forex_app.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
    public ResponseEntity<ConversionResponse> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {

        ConversionResponse conversionResponse = exchangeRateService.convertCurrency(amount, fromCurrency, toCurrency);

        return ResponseEntity.ok(conversionResponse);
    }
}