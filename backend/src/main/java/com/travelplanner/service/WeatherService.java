package com.travelplanner.service;

import com.travelplanner.dto.DestinationDTOs.DailyForecastDTO;
import com.travelplanner.dto.DestinationDTOs.WeatherForecastDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    @Cacheable(value = "weather", key = "#city.toLowerCase()")
    public WeatherForecastDTO getWeatherForecastForCity(String city, Double latitude, Double longitude) {
        log.info("Fetching weather forecast for city: {}", city);

        double baseTemp = 22.0;
        String defaultCondition = "Sunny";
        String defaultIcon = "sun";

        if (city != null) {
            String lower = city.toLowerCase();
            if (lower.contains("tokyo") || lower.contains("kyoto")) {
                baseTemp = 19.5;
                defaultCondition = "Clear Skies";
                defaultIcon = "sun";
            } else if (lower.contains("paris") || lower.contains("london") || lower.contains("amsterdam")) {
                baseTemp = 17.0;
                defaultCondition = "Partly Cloudy";
                defaultIcon = "cloud-sun";
            } else if (lower.contains("bali") || lower.contains("ubud") || lower.contains("maldives") || lower.contains("hawaii") || lower.contains("maui")) {
                baseTemp = 29.5;
                defaultCondition = "Tropical Sun & Ocean Breeze";
                defaultIcon = "sun";
            } else if (lower.contains("amalfi") || lower.contains("positano") || lower.contains("santorini") || lower.contains("rome") || lower.contains("barcelona") || lower.contains("venice")) {
                baseTemp = 25.0;
                defaultCondition = "Sunny Mediterranean";
                defaultIcon = "sun";
            } else if (lower.contains("reykjavik") || lower.contains("iceland") || lower.contains("banff") || lower.contains("swiss") || lower.contains("interlaken")) {
                baseTemp = 9.0;
                defaultCondition = "Crisp Mountain Air";
                defaultIcon = "cloud";
            } else if (lower.contains("dubai") || lower.contains("cairo") || lower.contains("petra") || lower.contains("marrakech")) {
                baseTemp = 32.0;
                defaultCondition = "Warm & Golden Skies";
                defaultIcon = "sun";
            } else if (lower.contains("cape town") || lower.contains("sydney") || lower.contains("rio")) {
                baseTemp = 23.5;
                defaultCondition = "Sunny Ocean Breeze";
                defaultIcon = "sun";
            } else if (lower.contains("singapore") || lower.contains("bangkok") || lower.contains("seoul") || lower.contains("agra")) {
                baseTemp = 28.0;
                defaultCondition = "Warm & Bright";
                defaultIcon = "sun";
            } else if (latitude != null) {
                double absLat = Math.abs(latitude);
                baseTemp = Math.max(5.0, 32.0 - (absLat * 0.45));
                defaultCondition = baseTemp > 24 ? "Sunny" : (baseTemp > 14 ? "Mild & Pleasant" : "Cool Breeze");
            }
        }

        List<DailyForecastDTO> dailyList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        String[] sampleConditions = {defaultCondition, "Partly Cloudy", "Sunny", "Mild Showers", "Clear Skies"};
        String[] sampleIcons = {defaultIcon, "cloud-sun", "sun", "cloud-rain", "sun"};

        for (int i = 0; i < 5; i++) {
            LocalDate date = today.plusDays(i);
            double dailyMax = Math.round((baseTemp + (i % 3) - 1.0) * 10.0) / 10.0;
            double dailyMin = Math.round((dailyMax - 6.5) * 10.0) / 10.0;

            dailyList.add(DailyForecastDTO.builder()
                    .date(date.toString())
                    .dayOfWeek(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
                    .maxTempC(dailyMax)
                    .minTempC(dailyMin)
                    .condition(sampleConditions[i % sampleConditions.length])
                    .icon(sampleIcons[i % sampleIcons.length])
                    .build());
        }

        double currentC = Math.round(baseTemp * 10.0) / 10.0;
        double currentF = Math.round((baseTemp * 9.0 / 5.0 + 32.0) * 10.0) / 10.0;

        return WeatherForecastDTO.builder()
                .cityName(city)
                .currentTempC(currentC)
                .currentTempF(currentF)
                .condition(defaultCondition)
                .icon(defaultIcon)
                .humidity(62)
                .windSpeedKmh(14.5)
                .forecast(dailyList)
                .build();
    }
}
