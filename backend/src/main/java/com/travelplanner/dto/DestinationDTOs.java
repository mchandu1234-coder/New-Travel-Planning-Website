package com.travelplanner.dto;

import com.travelplanner.entity.Destination;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DestinationDTOs {

    public static class DestinationResponse {
        private Long id;
        private String name;
        private String city;
        private String country;
        private String continent;
        private String heroImageUrl;
        private List<String> galleryImages;
        private String description;
        private List<String> vibeTags;
        private BigDecimal averageDailyCost;
        private String currency;
        private Double latitude;
        private Double longitude;
        private BigDecimal rating;
        private Integer reviewCount;
        private List<String> popularSights;
        private String bestTimeToVisit;
        private boolean isFeatured;
        private WeatherForecastDTO liveWeather;

        public DestinationResponse() {}

        public DestinationResponse(Long id, String name, String city, String country, String continent, String heroImageUrl, List<String> galleryImages, String description, List<String> vibeTags, BigDecimal averageDailyCost, String currency, Double latitude, Double longitude, BigDecimal rating, Integer reviewCount, List<String> popularSights, String bestTimeToVisit, boolean isFeatured, WeatherForecastDTO liveWeather) {
            this.id = id;
            this.name = name;
            this.city = city;
            this.country = country;
            this.continent = continent;
            this.heroImageUrl = heroImageUrl;
            this.galleryImages = galleryImages;
            this.description = description;
            this.vibeTags = vibeTags;
            this.averageDailyCost = averageDailyCost;
            this.currency = currency;
            this.latitude = latitude;
            this.longitude = longitude;
            this.rating = rating;
            this.reviewCount = reviewCount;
            this.popularSights = popularSights;
            this.bestTimeToVisit = bestTimeToVisit;
            this.isFeatured = isFeatured;
            this.liveWeather = liveWeather;
        }

        public static DestinationResponse fromEntity(Destination d) {
            if (d == null) return null;
            return DestinationResponse.builder()
                    .id(d.getId())
                    .name(d.getName())
                    .city(d.getCity())
                    .country(d.getCountry())
                    .continent(d.getContinent())
                    .heroImageUrl(d.getHeroImageUrl())
                    .galleryImages(d.getGalleryImages() != null ? Arrays.asList(d.getGalleryImages().split(",")) : Collections.emptyList())
                    .description(d.getDescription())
                    .vibeTags(d.getVibeTags() != null ? Arrays.asList(d.getVibeTags().split(",")) : Collections.emptyList())
                    .averageDailyCost(d.getAverageDailyCost())
                    .currency(d.getCurrency())
                    .latitude(d.getLatitude())
                    .longitude(d.getLongitude())
                    .rating(d.getRating())
                    .reviewCount(d.getReviewCount())
                    .popularSights(d.getPopularSights() != null ? Arrays.asList(d.getPopularSights().split(",")) : Collections.emptyList())
                    .bestTimeToVisit(d.getBestTimeToVisit())
                    .isFeatured(d.isFeatured())
                    .build();
        }

        public static DestinationResponseBuilder builder() { return new DestinationResponseBuilder(); }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getContinent() { return continent; }
        public void setContinent(String continent) { this.continent = continent; }
        public String getHeroImageUrl() { return heroImageUrl; }
        public void setHeroImageUrl(String heroImageUrl) { this.heroImageUrl = heroImageUrl; }
        public List<String> getGalleryImages() { return galleryImages; }
        public void setGalleryImages(List<String> galleryImages) { this.galleryImages = galleryImages; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<String> getVibeTags() { return vibeTags; }
        public void setVibeTags(List<String> vibeTags) { this.vibeTags = vibeTags; }
        public BigDecimal getAverageDailyCost() { return averageDailyCost; }
        public void setAverageDailyCost(BigDecimal averageDailyCost) { this.averageDailyCost = averageDailyCost; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        public BigDecimal getRating() { return rating; }
        public void setRating(BigDecimal rating) { this.rating = rating; }
        public Integer getReviewCount() { return reviewCount; }
        public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
        public List<String> getPopularSights() { return popularSights; }
        public void setPopularSights(List<String> popularSights) { this.popularSights = popularSights; }
        public String getBestTimeToVisit() { return bestTimeToVisit; }
        public void setBestTimeToVisit(String bestTimeToVisit) { this.bestTimeToVisit = bestTimeToVisit; }
        public boolean isFeatured() { return isFeatured; }
        public void setFeatured(boolean featured) { isFeatured = featured; }
        public WeatherForecastDTO getLiveWeather() { return liveWeather; }
        public void setLiveWeather(WeatherForecastDTO liveWeather) { this.liveWeather = liveWeather; }

        public static class DestinationResponseBuilder {
            private Long id;
            private String name;
            private String city;
            private String country;
            private String continent;
            private String heroImageUrl;
            private List<String> galleryImages;
            private String description;
            private List<String> vibeTags;
            private BigDecimal averageDailyCost;
            private String currency;
            private Double latitude;
            private Double longitude;
            private BigDecimal rating;
            private Integer reviewCount;
            private List<String> popularSights;
            private String bestTimeToVisit;
            private boolean isFeatured;
            private WeatherForecastDTO liveWeather;

            public DestinationResponseBuilder id(Long id) { this.id = id; return this; }
            public DestinationResponseBuilder name(String name) { this.name = name; return this; }
            public DestinationResponseBuilder city(String city) { this.city = city; return this; }
            public DestinationResponseBuilder country(String country) { this.country = country; return this; }
            public DestinationResponseBuilder continent(String continent) { this.continent = continent; return this; }
            public DestinationResponseBuilder heroImageUrl(String heroImageUrl) { this.heroImageUrl = heroImageUrl; return this; }
            public DestinationResponseBuilder galleryImages(List<String> galleryImages) { this.galleryImages = galleryImages; return this; }
            public DestinationResponseBuilder description(String description) { this.description = description; return this; }
            public DestinationResponseBuilder vibeTags(List<String> vibeTags) { this.vibeTags = vibeTags; return this; }
            public DestinationResponseBuilder averageDailyCost(BigDecimal averageDailyCost) { this.averageDailyCost = averageDailyCost; return this; }
            public DestinationResponseBuilder currency(String currency) { this.currency = currency; return this; }
            public DestinationResponseBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
            public DestinationResponseBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
            public DestinationResponseBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
            public DestinationResponseBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
            public DestinationResponseBuilder popularSights(List<String> popularSights) { this.popularSights = popularSights; return this; }
            public DestinationResponseBuilder bestTimeToVisit(String bestTimeToVisit) { this.bestTimeToVisit = bestTimeToVisit; return this; }
            public DestinationResponseBuilder isFeatured(boolean isFeatured) { this.isFeatured = isFeatured; return this; }
            public DestinationResponseBuilder liveWeather(WeatherForecastDTO liveWeather) { this.liveWeather = liveWeather; return this; }

            public DestinationResponse build() {
                return new DestinationResponse(id, name, city, country, continent, heroImageUrl, galleryImages, description, vibeTags, averageDailyCost, currency, latitude, longitude, rating, reviewCount, popularSights, bestTimeToVisit, isFeatured, liveWeather);
            }
        }
    }

    public static class WeatherForecastDTO {
        private String cityName;
        private Double currentTempC;
        private Double currentTempF;
        private String condition;
        private String icon;
        private Integer humidity;
        private Double windSpeedKmh;
        private List<DailyForecastDTO> forecast;

        public WeatherForecastDTO() {}
        public WeatherForecastDTO(String cityName, Double currentTempC, Double currentTempF, String condition, String icon, Integer humidity, Double windSpeedKmh, List<DailyForecastDTO> forecast) {
            this.cityName = cityName;
            this.currentTempC = currentTempC;
            this.currentTempF = currentTempF;
            this.condition = condition;
            this.icon = icon;
            this.humidity = humidity;
            this.windSpeedKmh = windSpeedKmh;
            this.forecast = forecast;
        }

        public static WeatherForecastDTOBuilder builder() { return new WeatherForecastDTOBuilder(); }

        public String getCityName() { return cityName; }
        public void setCityName(String cityName) { this.cityName = cityName; }
        public Double getCurrentTempC() { return currentTempC; }
        public void setCurrentTempC(Double currentTempC) { this.currentTempC = currentTempC; }
        public Double getCurrentTempF() { return currentTempF; }
        public void setCurrentTempF(Double currentTempF) { this.currentTempF = currentTempF; }
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public Integer getHumidity() { return humidity; }
        public void setHumidity(Integer humidity) { this.humidity = humidity; }
        public Double getWindSpeedKmh() { return windSpeedKmh; }
        public void setWindSpeedKmh(Double windSpeedKmh) { this.windSpeedKmh = windSpeedKmh; }
        public List<DailyForecastDTO> getForecast() { return forecast; }
        public void setForecast(List<DailyForecastDTO> forecast) { this.forecast = forecast; }

        public static class WeatherForecastDTOBuilder {
            private String cityName;
            private Double currentTempC;
            private Double currentTempF;
            private String condition;
            private String icon;
            private Integer humidity;
            private Double windSpeedKmh;
            private List<DailyForecastDTO> forecast;

            public WeatherForecastDTOBuilder cityName(String cityName) { this.cityName = cityName; return this; }
            public WeatherForecastDTOBuilder currentTempC(Double currentTempC) { this.currentTempC = currentTempC; return this; }
            public WeatherForecastDTOBuilder currentTempF(Double currentTempF) { this.currentTempF = currentTempF; return this; }
            public WeatherForecastDTOBuilder condition(String condition) { this.condition = condition; return this; }
            public WeatherForecastDTOBuilder icon(String icon) { this.icon = icon; return this; }
            public WeatherForecastDTOBuilder humidity(Integer humidity) { this.humidity = humidity; return this; }
            public WeatherForecastDTOBuilder windSpeedKmh(Double windSpeedKmh) { this.windSpeedKmh = windSpeedKmh; return this; }
            public WeatherForecastDTOBuilder forecast(List<DailyForecastDTO> forecast) { this.forecast = forecast; return this; }

            public WeatherForecastDTO build() {
                return new WeatherForecastDTO(cityName, currentTempC, currentTempF, condition, icon, humidity, windSpeedKmh, forecast);
            }
        }
    }

    public static class DailyForecastDTO {
        private String date;
        private String dayOfWeek;
        private Double maxTempC;
        private Double minTempC;
        private String condition;
        private String icon;

        public DailyForecastDTO() {}
        public DailyForecastDTO(String date, String dayOfWeek, Double maxTempC, Double minTempC, String condition, String icon) {
            this.date = date;
            this.dayOfWeek = dayOfWeek;
            this.maxTempC = maxTempC;
            this.minTempC = minTempC;
            this.condition = condition;
            this.icon = icon;
        }

        public static DailyForecastDTOBuilder builder() { return new DailyForecastDTOBuilder(); }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public Double getMaxTempC() { return maxTempC; }
        public void setMaxTempC(Double maxTempC) { this.maxTempC = maxTempC; }
        public Double getMinTempC() { return minTempC; }
        public void setMinTempC(Double minTempC) { this.minTempC = minTempC; }
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }

        public static class DailyForecastDTOBuilder {
            private String date;
            private String dayOfWeek;
            private Double maxTempC;
            private Double minTempC;
            private String condition;
            private String icon;

            public DailyForecastDTOBuilder date(String date) { this.date = date; return this; }
            public DailyForecastDTOBuilder dayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; return this; }
            public DailyForecastDTOBuilder maxTempC(Double maxTempC) { this.maxTempC = maxTempC; return this; }
            public DailyForecastDTOBuilder minTempC(Double minTempC) { this.minTempC = minTempC; return this; }
            public DailyForecastDTOBuilder condition(String condition) { this.condition = condition; return this; }
            public DailyForecastDTOBuilder icon(String icon) { this.icon = icon; return this; }

            public DailyForecastDTO build() {
                return new DailyForecastDTO(date, dayOfWeek, maxTempC, minTempC, condition, icon);
            }
        }
    }
}
