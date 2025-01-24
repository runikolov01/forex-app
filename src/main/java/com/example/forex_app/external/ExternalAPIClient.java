package com.example.forex_app.external;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class ExternalAPIClient {

    private final RestTemplate restTemplate;
    private final String apiKey = "19a056a841177d47bffcc335c23bc52d";
    private final String apiUrl = "http://data.fixer.io/api/latest";

    public ExternalAPIClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("access_key", apiKey)
                .queryParam("symbols", fromCurrency + "," + toCurrency)
                .build()
                .toString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("rates")) {
            Map<String, Object> rates = (Map<String, Object>) response.get("rates");

            Object fromRateObject = rates.get(fromCurrency);
            Object toRateObject = rates.get(toCurrency);

            if (fromRateObject != null && toRateObject != null) {
                BigDecimal fromRate = convertToBigDecimal(fromRateObject);
                BigDecimal toRate = convertToBigDecimal(toRateObject);

                return toRate.divide(fromRate, 4, RoundingMode.HALF_UP);
            }
        }

        throw new RuntimeException("Failed to fetch exchange rate from external API");
    }

    private BigDecimal convertToBigDecimal(Object rateObject) {
        return switch (rateObject) {
            case Double v -> BigDecimal.valueOf(v);
            case BigDecimal bigDecimal -> bigDecimal;
            case Integer i -> BigDecimal.valueOf(i);
            case null, default ->
                    throw new IllegalArgumentException("Invalid rate value type: " + rateObject.getClass().getName());
        };
    }
}