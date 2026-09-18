package com.travelplanner.service;

import com.travelplanner.dto.BudgetDTOs.ExchangeRatesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);

    private static final Map<String, BigDecimal> USD_BASE_RATES = new HashMap<>();

    static {
        USD_BASE_RATES.put("USD", BigDecimal.valueOf(1.00));
        USD_BASE_RATES.put("EUR", BigDecimal.valueOf(0.92));
        USD_BASE_RATES.put("GBP", BigDecimal.valueOf(0.79));
        USD_BASE_RATES.put("JPY", BigDecimal.valueOf(155.50));
        USD_BASE_RATES.put("CAD", BigDecimal.valueOf(1.36));
        USD_BASE_RATES.put("AUD", BigDecimal.valueOf(1.52));
        USD_BASE_RATES.put("INR", BigDecimal.valueOf(83.50));
        USD_BASE_RATES.put("SGD", BigDecimal.valueOf(1.34));
        USD_BASE_RATES.put("CHF", BigDecimal.valueOf(0.90));
        USD_BASE_RATES.put("IDR", BigDecimal.valueOf(16200.00));
        USD_BASE_RATES.put("ZAR", BigDecimal.valueOf(18.25));
        USD_BASE_RATES.put("ISK", BigDecimal.valueOf(138.00));
    }

    @Cacheable(value = "exchangeRates", key = "#baseCurrency.toUpperCase()")
    public ExchangeRatesDTO getRates(String baseCurrency) {
        String base = (baseCurrency != null) ? baseCurrency.toUpperCase() : "USD";
        BigDecimal baseToUsd = USD_BASE_RATES.getOrDefault(base, BigDecimal.ONE);

        Map<String, BigDecimal> relativeRates = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entry : USD_BASE_RATES.entrySet()) {
            BigDecimal targetRate = entry.getValue().divide(baseToUsd, 4, RoundingMode.HALF_UP);
            relativeRates.put(entry.getKey(), targetRate);
        }

        return ExchangeRatesDTO.builder()
                .baseCurrency(base)
                .rates(relativeRates)
                .lastUpdated(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .build();
    }

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        String from = (fromCurrency != null) ? fromCurrency.toUpperCase() : "USD";
        String to = (toCurrency != null) ? toCurrency.toUpperCase() : "USD";

        if (from.equals(to)) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal fromRate = USD_BASE_RATES.getOrDefault(from, BigDecimal.ONE);
        BigDecimal toRate = USD_BASE_RATES.getOrDefault(to, BigDecimal.ONE);

        BigDecimal amountInUsd = amount.divide(fromRate, 6, RoundingMode.HALF_UP);
        return amountInUsd.multiply(toRate).setScale(2, RoundingMode.HALF_UP);
    }
}
