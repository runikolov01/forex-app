package com.example.forex_app.web;

import com.example.forex_app.model.CurrencyConversion;
import com.example.forex_app.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<Map<String, Object>>> getConversionHistory(
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String transactionDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (transactionId == null && transactionDate == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Map<String, Object>> conversionHistory = currencyConversionService.getConversionHistory(transactionId, transactionDate, page, size);
        return ResponseEntity.ok(conversionHistory);
    }

}