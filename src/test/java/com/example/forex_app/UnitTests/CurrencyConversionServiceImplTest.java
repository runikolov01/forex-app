package com.example.forex_app.UnitTests;

import com.example.forex_app.model.CurrencyConversion;
import com.example.forex_app.model.ExchangeRate;
import com.example.forex_app.repository.CurrencyConversionRepository;
import com.example.forex_app.service.ExchangeRateService;
import com.example.forex_app.service.impl.CurrencyConversionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyConversionServiceImplTest {

    @InjectMocks
    private CurrencyConversionServiceImpl currencyConversionService;

    @Mock
    private CurrencyConversionRepository currencyConversionRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    private ExchangeRate exchangeRate;
    private CurrencyConversion currencyConversion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exchangeRate = new ExchangeRate();
        exchangeRate.setFromCurrency("USD");
        exchangeRate.setToCurrency("EUR");
        exchangeRate.setRate(BigDecimal.valueOf(0.85));
        currencyConversion = new CurrencyConversion();
        currencyConversion.setTransactionId(UUID.randomUUID().toString());
        currencyConversion.setAmount(BigDecimal.valueOf(100));
        currencyConversion.setFromCurrency("USD");
        currencyConversion.setToCurrency("EUR");
        currencyConversion.setConvertedAmount(BigDecimal.valueOf(85));
        currencyConversion.setTransactionDate(LocalDateTime.now());
        currencyConversion.setExchangeRate(BigDecimal.valueOf(0.85));
    }

    @Test
    void testConvertCurrency_Success() {
        when(exchangeRateService.getExchangeRate("USD", "EUR")).thenReturn(exchangeRate);
        when(currencyConversionRepository.save(any(CurrencyConversion.class))).thenReturn(currencyConversion);

        CurrencyConversion result = currencyConversionService.convertCurrency(BigDecimal.valueOf(100), "USD", "EUR");

        assertNotNull(result);
        assertEquals("USD", result.getFromCurrency());
        assertEquals("EUR", result.getToCurrency());
        assertEquals(0, result.getConvertedAmount().compareTo(BigDecimal.valueOf(85.00)));
        verify(currencyConversionRepository, times(1)).save(any(CurrencyConversion.class));
    }

    @Test
    void testConvertCurrency_ExchangeRateNotFound() {
        when(exchangeRateService.getExchangeRate("USD", "EUR")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                currencyConversionService.convertCurrency(BigDecimal.valueOf(100), "USD", "EUR")
        );

        assertEquals("Error during currency conversion: Exchange rate not found for USD to EUR", exception.getMessage());
    }

    @Test
    void testGetConversionByTransactionId_Success() {
        when(currencyConversionRepository.findByTransactionId(anyString())).thenReturn(Optional.of(currencyConversion));

        CurrencyConversion result = currencyConversionService.getConversionByTransactionId(currencyConversion.getTransactionId());

        assertNotNull(result);
        assertEquals(currencyConversion.getTransactionId(), result.getTransactionId());
        assertEquals("USD", result.getFromCurrency());
    }

    @Test
    void testGetConversionByTransactionId_NotFound() {
        when(currencyConversionRepository.findByTransactionId(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                currencyConversionService.getConversionByTransactionId("invalid-id")
        );

        assertEquals("Transaction not found with ID: invalid-id", exception.getMessage());
    }

    @Test
    void testGetConversionHistory_Success() {
        List<CurrencyConversion> conversions = Collections.singletonList(currencyConversion);
        Page<CurrencyConversion> page = new PageImpl<>(conversions);
        when(currencyConversionRepository.findByTransactionDateBetween(any(), any(), any(PageRequest.class))).thenReturn(page);

        List<Map<String, Object>> result = currencyConversionService.getConversionHistory(
                "transaction-id", "2025-01-01", 0, 10
        );

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).containsKey("transactionId"));
    }

    @Test
    void testGetConversionHistory_InvalidDateFormat() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                currencyConversionService.getConversionHistory("transaction-id", "invalid-date", 0, 10)
        );

        assertEquals("Error fetching conversion history: Invalid date format. Expected format: yyyy-MM-dd", exception.getMessage());
    }

    @Test
    void testGetConversionHistory_Exception() {
        when(currencyConversionRepository.findByTransactionDateBetween(any(), any(), any(PageRequest.class)))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                currencyConversionService.getConversionHistory("transaction-id", "2025-01-01", 0, 10)
        );

        assertEquals("Error fetching conversion history: Database error", exception.getMessage());
    }
}