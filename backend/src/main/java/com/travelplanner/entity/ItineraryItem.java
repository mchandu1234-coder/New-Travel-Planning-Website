package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "itinerary_items")
public class ItineraryItem {

    public enum ItemType {
        FLIGHT,
        ACCOMMODATION,
        ACTIVITY,
        RESTAURANT,
        TRANSIT,
        CUSTOM;

        @com.fasterxml.jackson.annotation.JsonCreator
        public static ItemType fromString(String value) {
            if (value == null) return ACTIVITY;
            String upper = value.trim().toUpperCase();
            if (upper.equals("SIGHTSEEING") || upper.equals("ATTRACTION") || upper.equals("SIGHT") || upper.equals("TOUR")) return ACTIVITY;
            if (upper.equals("HOTEL") || upper.equals("STAY") || upper.equals("LODGING")) return ACCOMMODATION;
            if (upper.equals("DINING") || upper.equals("FOOD") || upper.equals("MEAL")) return RESTAURANT;
            if (upper.equals("PLANE") || upper.equals("AIRLINE")) return FLIGHT;
            if (upper.equals("TRANSPORT") || upper.equals("TRAIN") || upper.equals("BUS")) return TRANSIT;
            try {
                return ItemType.valueOf(upper);
            } catch (Exception e) {
                return ACTIVITY;
            }
        }
    }

    public enum ItemStatus {
        PLANNED,
        BOOKED,
        COMPLETED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id", nullable = false)
    @JsonIgnore
    private ItineraryDay day;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false, length = 50)
    private ItemType itemType = ItemType.ACTIVITY;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "location_name", length = 200)
    private String locationName;

    @Column(columnDefinition = "TEXT")
    private String address;

    private Double latitude;
    private Double longitude;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost = BigDecimal.ZERO;

    @Column(length = 10)
    private String currency = "USD";

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "booking_reference", length = 100)
    private String bookingReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ItemStatus status = ItemStatus.PLANNED;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public ItineraryItem() {}

    public ItineraryItem(Long id, ItineraryDay day, ItemType itemType, String title, String description, String locationName, String address, Double latitude, Double longitude, LocalTime startTime, LocalTime endTime, BigDecimal estimatedCost, String currency, Integer displayOrder, String bookingReference, ItemStatus status, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.day = day;
        this.itemType = itemType != null ? itemType : ItemType.ACTIVITY;
        this.title = title;
        this.description = description;
        this.locationName = locationName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.estimatedCost = estimatedCost != null ? estimatedCost : BigDecimal.ZERO;
        this.currency = currency != null ? currency : "USD";
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.bookingReference = bookingReference;
        this.status = status != null ? status : ItemStatus.PLANNED;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ItineraryItemBuilder builder() {
        return new ItineraryItemBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ItineraryDay getDay() { return day; }
    public void setDay(ItineraryDay day) { this.day = day; }
    public ItemType getItemType() { return itemType; }
    public void setItemType(ItemType itemType) { this.itemType = itemType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class ItineraryItemBuilder {
        private Long id;
        private ItineraryDay day;
        private ItemType itemType = ItemType.ACTIVITY;
        private String title;
        private String description;
        private String locationName;
        private String address;
        private Double latitude;
        private Double longitude;
        private LocalTime startTime;
        private LocalTime endTime;
        private BigDecimal estimatedCost = BigDecimal.ZERO;
        private String currency = "USD";
        private Integer displayOrder = 0;
        private String bookingReference;
        private ItemStatus status = ItemStatus.PLANNED;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        public ItineraryItemBuilder id(Long id) { this.id = id; return this; }
        public ItineraryItemBuilder day(ItineraryDay day) { this.day = day; return this; }
        public ItineraryItemBuilder itemType(ItemType itemType) { this.itemType = itemType; return this; }
        public ItineraryItemBuilder title(String title) { this.title = title; return this; }
        public ItineraryItemBuilder description(String description) { this.description = description; return this; }
        public ItineraryItemBuilder locationName(String locationName) { this.locationName = locationName; return this; }
        public ItineraryItemBuilder address(String address) { this.address = address; return this; }
        public ItineraryItemBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
        public ItineraryItemBuilder longitude(Double longitude) { this.longitude = longitude; return this; }
        public ItineraryItemBuilder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public ItineraryItemBuilder endTime(LocalTime endTime) { this.endTime = endTime; return this; }
        public ItineraryItemBuilder estimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; return this; }
        public ItineraryItemBuilder currency(String currency) { this.currency = currency; return this; }
        public ItineraryItemBuilder displayOrder(Integer displayOrder) { this.displayOrder = displayOrder; return this; }
        public ItineraryItemBuilder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public ItineraryItemBuilder status(ItemStatus status) { this.status = status; return this; }
        public ItineraryItemBuilder createdAt(ZonedDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ItineraryItemBuilder updatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ItineraryItem build() {
            return new ItineraryItem(id, day, itemType, title, description, locationName, address, latitude, longitude, startTime, endTime, estimatedCost, currency, displayOrder, bookingReference, status, createdAt, updatedAt);
        }
    }
}
