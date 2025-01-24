package com.example.forex_app.web;

import com.example.forex_app.model.CurrencyConversion;
import com.example.forex_app.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/currency-conversion")
public class CurrencyConversionController {

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversion> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {

        CurrencyConversion conversion = currencyConversionService.convertCurrency(amount, fromCurrency, toCurrency);
        return ResponseEntity.ok(conversion);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<CurrencyConversion> getConversionByTransactionId(@PathVariable String transactionId) {
        CurrencyConversion conversion = currencyConversionService.getConversionByTransactionId(transactionId);
        return ResponseEntity.ok(conversion);
    }

    @GetMapping("/history")
    public ResponseEntity<List<CurrencyConversion>> getConversionsByDate(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        LocalDateTime start = (startDate != null) ? LocalDateTime.parse(startDate) : LocalDateTime.now().minusDays(30);
        LocalDateTime end = (endDate != null) ? LocalDateTime.parse(endDate) : LocalDateTime.now();

        List<CurrencyConversion> conversions = currencyConversionService.getConversionsByDate(start, end);
        return ResponseEntity.ok(conversions);
    }
}