package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Destination destination;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 50)
    private String category; // SIGHTSEEING, ADVENTURE, MUSEUM, NATURE, CULTURE, NIGHTLIFE

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_hours", nullable = false, precision = 3, scale = 1)
    private BigDecimal durationHours = BigDecimal.valueOf(2.0);

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(length = 10)
    private String currency = "USD";

    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.valueOf(4.7);

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "opening_hours", length = 100)
    private String openingHours;

    @Column(name = "booking_required")
    private Boolean bookingRequired = false;

    public Activity() {}

    public Activity(Long id, Destination destination, String name, String category, String description, BigDecimal durationHours, BigDecimal price, String currency, BigDecimal rating, Integer reviewCount, String imageUrl, String address, String openingHours, Boolean bookingRequired) {
        this.id = id;
        this.destination = destination;
        this.name = name;
        this.category = category;
        this.description = description;
        this.durationHours = durationHours != null ? durationHours : BigDecimal.valueOf(2.0);
        this.price = price != null ? price : BigDecimal.ZERO;
        this.currency = currency != null ? currency : "USD";
        this.rating = rating != null ? rating : BigDecimal.valueOf(4.7);
        this.reviewCount = reviewCount != null ? reviewCount : 0;
        this.imageUrl = imageUrl;
        this.address = address;
        this.openingHours = openingHours;
        this.bookingRequired = bookingRequired != null ? bookingRequired : false;
    }

    public static ActivityBuilder builder() {
        return new ActivityBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Destination getDestination() { return destination; }
    public void setDestination(Destination destination) { this.destination = destination; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getDurationHours() { return durationHours; }
    public void setDurationHours(BigDecimal durationHours) { this.durationHours = durationHours; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }
    public Boolean getBookingRequired() { return bookingRequired; }
    public void setBookingRequired(Boolean bookingRequired) { this.bookingRequired = bookingRequired; }

    public static class ActivityBuilder {
        private Long id;
        private Destination destination;
        private String name;
        private String category;
        private String description;
        private BigDecimal durationHours = BigDecimal.valueOf(2.0);
        private BigDecimal price = BigDecimal.ZERO;
        private String currency = "USD";
        private BigDecimal rating = BigDecimal.valueOf(4.7);
        private Integer reviewCount = 0;
        private String imageUrl;
        private String address;
        private String openingHours;
        private Boolean bookingRequired = false;

        public ActivityBuilder id(Long id) { this.id = id; return this; }
        public ActivityBuilder destination(Destination destination) { this.destination = destination; return this; }
        public ActivityBuilder name(String name) { this.name = name; return this; }
        public ActivityBuilder category(String category) { this.category = category; return this; }
        public ActivityBuilder description(String description) { this.description = description; return this; }
        public ActivityBuilder durationHours(BigDecimal durationHours) { this.durationHours = durationHours; return this; }
        public ActivityBuilder price(BigDecimal price) { this.price = price; return this; }
        public ActivityBuilder currency(String currency) { this.currency = currency; return this; }
        public ActivityBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
        public ActivityBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
        public ActivityBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ActivityBuilder address(String address) { this.address = address; return this; }
        public ActivityBuilder openingHours(String openingHours) { this.openingHours = openingHours; return this; }
        public ActivityBuilder bookingRequired(Boolean bookingRequired) { this.bookingRequired = bookingRequired; return this; }

        public Activity build() {
            return new Activity(id, destination, name, category, description, durationHours, price, currency, rating, reviewCount, imageUrl, address, openingHours, bookingRequired);
        }
    }
}
