package com.example.forex_app.model;

import java.math.BigDecimal;

public class ConversionResponse {
    private String transactionId;
    private BigDecimal convertedAmount;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal originalAmount;
    private BigDecimal exchangeRate;

    public ConversionResponse(String transactionId, BigDecimal convertedAmount, String fromCurrency, String toCurrency, BigDecimal originalAmount, BigDecimal exchangeRate) {
        this.transactionId = transactionId;
        this.convertedAmount = convertedAmount;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.originalAmount = originalAmount;
        this.exchangeRate = exchangeRate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
}
