package com.example.forex_app.service.impl;

import com.example.forex_app.model.ExchangeRate;
import com.example.forex_app.repository.ExchangeRateRepository;
import com.example.forex_app.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) {
        Optional<ExchangeRate> rate = exchangeRateRepository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency);
        if (rate.isPresent()) {
            return rate.get();
        } else {
            BigDecimal externalRate = fetchExternalExchangeRate(fromCurrency, toCurrency);
            ExchangeRate newRate = new ExchangeRate();
            newRate.setFromCurrency(fromCurrency);
            newRate.setToCurrency(toCurrency);
            newRate.setRate(externalRate);
            exchangeRateRepository.save(newRate);
            return newRate;
        }
    }

    @Override
    public BigDecimal fetchExternalExchangeRate(String fromCurrency, String toCurrency) {
        return BigDecimal.valueOf(1.15); // TODO: Now: Mock external API call, TO fetch exchange rate from an external API
    }
}