package com.example.forex_app.UnitTests;

import com.example.forex_app.external.ExternalAPIClient;
import com.example.forex_app.model.CurrencyConversion;
import com.example.forex_app.model.ExchangeRate;
import com.example.forex_app.repository.CurrencyConversionRepository;
import com.example.forex_app.service.impl.ExchangeRateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExchangeRateServiceImplTest {

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Mock
    private ExternalAPIClient externalAPIClient;

    @Mock
    private CurrencyConversionRepository currencyConversionRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetExchangeRate_ShouldReturnExchangeRate() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal expectedRate = new BigDecimal("0.85");
        when(externalAPIClient.getExchangeRate(fromCurrency, toCurrency)).thenReturn(expectedRate);

        ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);

        assertNotNull(exchangeRate);
        assertEquals(fromCurrency, exchangeRate.getFromCurrency());
        assertEquals(toCurrency, exchangeRate.getToCurrency());
        assertEquals(expectedRate, exchangeRate.getRate());
    }

    @Test
    public void testGetExchangeRate_ShouldThrowException_WhenRateIsNull() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        when(externalAPIClient.getExchangeRate(fromCurrency, toCurrency)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> exchangeRateService.getExchangeRate(fromCurrency, toCurrency));
        assertEquals("Error fetching exchange rate: Failed to fetch exchange rate from USD to EUR", exception.getMessage());
    }

    @Test
    public void testConvertCurrency_ShouldReturnCurrencyConversion() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal amount = new BigDecimal("100");
        BigDecimal exchangeRate = new BigDecimal("0.85");
        when(externalAPIClient.getExchangeRate(fromCurrency, toCurrency)).thenReturn(exchangeRate);

        CurrencyConversion savedConversion = new CurrencyConversion();
        savedConversion.setTransactionId(UUID.randomUUID().toString());
        savedConversion.setFromCurrency(fromCurrency);
        savedConversion.setToCurrency(toCurrency);
        savedConversion.setAmount(amount);
        savedConversion.setConvertedAmount(amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP));
        savedConversion.setExchangeRate(exchangeRate);
        savedConversion.setTransactionDate(LocalDateTime.now());
        when(currencyConversionRepository.save(any(CurrencyConversion.class))).thenReturn(savedConversion);

        CurrencyConversion conversion = exchangeRateService.convertCurrency(amount, fromCurrency, toCurrency);

        assertNotNull(conversion);
        assertEquals(fromCurrency, conversion.getFromCurrency());
        assertEquals(toCurrency, conversion.getToCurrency());
        assertEquals(amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP), conversion.getConvertedAmount());
        assertEquals(exchangeRate, conversion.getExchangeRate());
    }

    @Test
    public void testConvertCurrency_ShouldThrowException_WhenExchangeRateIsNull() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal amount = new BigDecimal("100");
        when(externalAPIClient.getExchangeRate(fromCurrency, toCurrency)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> exchangeRateService.convertCurrency(amount, fromCurrency, toCurrency));
        assertEquals("Error during currency conversion: Exchange rate not available for USD to EUR", exception.getMessage());
    }
}