package com.travelplanner.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 50)
    private String continent;

    @Column(name = "hero_image_url", nullable = false, columnDefinition = "TEXT")
    private String heroImageUrl;

    @Column(name = "gallery_images", columnDefinition = "TEXT")
    private String galleryImages;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "vibe_tags", nullable = false, columnDefinition = "TEXT")
    private String vibeTags;

    @Column(name = "average_daily_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal averageDailyCost = BigDecimal.valueOf(150.00);

    @Column(nullable = false, length = 10)
    private String currency = "USD";

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.valueOf(4.8);

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount = 0;

    @Column(name = "popular_sights", columnDefinition = "TEXT")
    private String popularSights;

    @Column(name = "best_time_to_visit", nullable = false, length = 150)
    private String bestTimeToVisit;

    @Column(name = "is_featured", nullable = false)
    private boolean isFeatured = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    public Destination() {}

    public Destination(Long id, String name, String city, String country, String continent, String heroImageUrl, String galleryImages, String description, String vibeTags, BigDecimal averageDailyCost, String currency, Double latitude, Double longitude, BigDecimal rating, Integer reviewCount, String popularSights, String bestTimeToVisit, boolean isFeatured, ZonedDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.continent = continent;
        this.heroImageUrl = heroImageUrl;
        this.galleryImages = galleryImages;
        this.description = description;
        this.vibeTags = vibeTags;
        this.averageDailyCost = averageDailyCost != null ? averageDailyCost : BigDecimal.valueOf(150.00);
        this.currency = currency != null ? currency : "USD";
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating != null ? rating : BigDecimal.valueOf(4.8);
        this.reviewCount = reviewCount != null ? reviewCount : 0;
        this.popularSights = popularSights;
        this.bestTimeToVisit = bestTimeToVisit;
        this.isFeatured = isFeatured;
        this.createdAt = createdAt;
    }

    public static DestinationBuilder builder() {
        return new DestinationBuilder();
    }

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
    public String getGalleryImages() { return galleryImages; }
    public void setGalleryImages(String galleryImages) { this.galleryImages = galleryImages; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVibeTags() { return vibeTags; }
    public void setVibeTags(String vibeTags) { this.vibeTags = vibeTags; }
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
    public String getPopularSights() { return popularSights; }
    public void setPopularSights(String popularSights) { this.popularSights = popularSights; }
    public String getBestTimeToVisit() { return bestTimeToVisit; }
    public void setBestTimeToVisit(String bestTimeToVisit) { this.bestTimeToVisit = bestTimeToVisit; }
    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public static class DestinationBuilder {
        private Long id;
        private String name;
        private String city;
        private String country;
        private String continent;
        private String heroImageUrl;
        private String galleryImages;
        private String description;
        private String vibeTags;
        private BigDecimal averageDailyCost = BigDecimal.valueOf(150.00);
        private String currency = "USD";
        private Double latitude;
        private Double longitude;
        private BigDecimal rating = BigDecimal.valueOf(4.8);
        private Integer reviewCount = 0;
        private String popularSights;
        private String bestTimeToVisit;
        private boolean isFeatured = false;
        private ZonedDateTime createdAt;

        public DestinationBuilder id(Long id) { this.id = id; return this; }
        public DestinationBuilder name(String name) { this.name = name; return this; }
        public DestinationBuilder city(String city) { this.city = city; return this; }
        public DestinationBuilder country(String country) { this.country = country; return this; }
        public DestinationBuilder continent(String continent) { this.continent = continent; return this; }
        public DestinationBuilder heroImageUrl(String heroImageUrl) { this.heroImageUrl = heroImageUrl; return this; }
        public DestinationBuilder galleryImages(String galleryImages) { this.galleryImages = galleryImages; return this; }
        public DestinationBuilder description(String description) { this.description = description; return this; }
        public DestinationBuilder vibeTags(String vibeTags) { this.vibeTags = vibeTags; return this; }
        public DestinationBuilder averageDailyCost(BigDecimal averageDailyCost) { this.averageDailyCost = averageDailyCost; return this; }
        public DestinationBuilder currency(String currency) { this.currency = currency; return this; }
        public DestinationBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public DestinationBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public DestinationBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
        public DestinationBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
        public DestinationBuilder popularSights(String popularSights) { this.popularSights = popularSights; return this; }
        public DestinationBuilder bestTimeToVisit(String bestTimeToVisit) { this.bestTimeToVisit = bestTimeToVisit; return this; }
        public DestinationBuilder isFeatured(boolean isFeatured) { this.isFeatured = isFeatured; return this; }
        public DestinationBuilder createdAt(ZonedDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Destination build() {
            return new Destination(id, name, city, country, continent, heroImageUrl, galleryImages, description, vibeTags, averageDailyCost, currency, latitude, longitude, rating, reviewCount, popularSights, bestTimeToVisit, isFeatured, createdAt);
        }
    }
}
