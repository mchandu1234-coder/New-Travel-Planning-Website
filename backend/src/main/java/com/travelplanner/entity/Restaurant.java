package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Destination destination;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "cuisine_type", nullable = false, length = 100)
    private String cuisineType;

    @Column(name = "price_range", length = 10)
    private String priceRange = "$$";

    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.valueOf(4.6);

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "opening_hours", length = 100)
    private String openingHours;

    @Column(columnDefinition = "TEXT")
    private String specialties;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "reservation_url", columnDefinition = "TEXT")
    private String reservationUrl;

    public Restaurant() {}

    public Restaurant(Long id, Destination destination, String name, String cuisineType, String priceRange, BigDecimal rating, Integer reviewCount, String address, String openingHours, String specialties, String imageUrl, String reservationUrl) {
        this.id = id;
        this.destination = destination;
        this.name = name;
        this.cuisineType = cuisineType;
        this.priceRange = priceRange != null ? priceRange : "$$";
        this.rating = rating != null ? rating : BigDecimal.valueOf(4.6);
        this.reviewCount = reviewCount != null ? reviewCount : 0;
        this.address = address;
        this.openingHours = openingHours;
        this.specialties = specialties;
        this.imageUrl = imageUrl;
        this.reservationUrl = reservationUrl;
    }

    public static RestaurantBuilder builder() {
        return new RestaurantBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Destination getDestination() { return destination; }
    public void setDestination(Destination destination) { this.destination = destination; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }
    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }
    public String getSpecialties() { return specialties; }
    public void setSpecialties(String specialties) { this.specialties = specialties; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getReservationUrl() { return reservationUrl; }
    public void setReservationUrl(String reservationUrl) { this.reservationUrl = reservationUrl; }

    public static class RestaurantBuilder {
        private Long id;
        private Destination destination;
        private String name;
        private String cuisineType;
        private String priceRange = "$$";
        private BigDecimal rating = BigDecimal.valueOf(4.6);
        private Integer reviewCount = 0;
        private String address;
        private String openingHours;
        private String specialties;
        private String imageUrl;
        private String reservationUrl;

        public RestaurantBuilder id(Long id) { this.id = id; return this; }
        public RestaurantBuilder destination(Destination destination) { this.destination = destination; return this; }
        public RestaurantBuilder name(String name) { this.name = name; return this; }
        public RestaurantBuilder cuisineType(String cuisineType) { this.cuisineType = cuisineType; return this; }
        public RestaurantBuilder priceRange(String priceRange) { this.priceRange = priceRange; return this; }
        public RestaurantBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
        public RestaurantBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
        public RestaurantBuilder address(String address) { this.address = address; return this; }
        public RestaurantBuilder openingHours(String openingHours) { this.openingHours = openingHours; return this; }
        public RestaurantBuilder specialties(String specialties) { this.specialties = specialties; return this; }
        public RestaurantBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public RestaurantBuilder reservationUrl(String reservationUrl) { this.reservationUrl = reservationUrl; return this; }

        public Restaurant build() {
            return new Restaurant(id, destination, name, cuisineType, priceRange, rating, reviewCount, address, openingHours, specialties, imageUrl, reservationUrl);
        }
    }
}
