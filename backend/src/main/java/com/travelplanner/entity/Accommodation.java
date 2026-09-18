package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "accommodations")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @JsonIgnore
    private Trip trip;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 50)
    private String type = "HOTEL";

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.valueOf(4.5);

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(length = 10)
    private String currency = "USD";

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String amenities;

    @Column(name = "booking_reference", length = 100)
    private String bookingReference;

    @Column(name = "booking_status", length = 50)
    private String bookingStatus = "CONFIRMED";

    private Double latitude;
    private Double longitude;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    public Accommodation() {}

    public Accommodation(Long id, Trip trip, String name, String type, String address, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal rating, BigDecimal pricePerNight, BigDecimal totalCost, String currency, String imageUrl, String amenities, String bookingReference, String bookingStatus, Double latitude, Double longitude, ZonedDateTime createdAt) {
        this.id = id;
        this.trip = trip;
        this.name = name;
        this.type = type != null ? type : "HOTEL";
        this.address = address;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.rating = rating != null ? rating : BigDecimal.valueOf(4.5);
        this.pricePerNight = pricePerNight;
        this.totalCost = totalCost;
        this.currency = currency != null ? currency : "USD";
        this.imageUrl = imageUrl;
        this.amenities = amenities;
        this.bookingReference = bookingReference;
        this.bookingStatus = bookingStatus != null ? bookingStatus : "CONFIRMED";
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
    }

    public static AccommodationBuilder builder() {
        return new AccommodationBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public static class AccommodationBuilder {
        private Long id;
        private Trip trip;
        private String name;
        private String type = "HOTEL";
        private String address;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private BigDecimal rating = BigDecimal.valueOf(4.5);
        private BigDecimal pricePerNight;
        private BigDecimal totalCost;
        private String currency = "USD";
        private String imageUrl;
        private String amenities;
        private String bookingReference;
        private String bookingStatus = "CONFIRMED";
        private Double latitude;
        private Double longitude;
        private ZonedDateTime createdAt;

        public AccommodationBuilder id(Long id) { this.id = id; return this; }
        public AccommodationBuilder trip(Trip trip) { this.trip = trip; return this; }
        public AccommodationBuilder name(String name) { this.name = name; return this; }
        public AccommodationBuilder type(String type) { this.type = type; return this; }
        public AccommodationBuilder address(String address) { this.address = address; return this; }
        public AccommodationBuilder checkInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; return this; }
        public AccommodationBuilder checkOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; return this; }
        public AccommodationBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
        public AccommodationBuilder pricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; return this; }
        public AccommodationBuilder totalCost(BigDecimal totalCost) { this.totalCost = totalCost; return this; }
        public AccommodationBuilder currency(String currency) { this.currency = currency; return this; }
        public AccommodationBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public AccommodationBuilder amenities(String amenities) { this.amenities = amenities; return this; }
        public AccommodationBuilder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public AccommodationBuilder bookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; return this; }
        public AccommodationBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public AccommodationBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public AccommodationBuilder createdAt(ZonedDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Accommodation build() {
            return new Accommodation(id, trip, name, type, address, checkInDate, checkOutDate, rating, pricePerNight, totalCost, currency, imageUrl, amenities, bookingReference, bookingStatus, latitude, longitude, createdAt);
        }
    }
}
