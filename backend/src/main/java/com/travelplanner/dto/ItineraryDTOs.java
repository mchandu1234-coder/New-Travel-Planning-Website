package com.travelplanner.dto;

import com.travelplanner.entity.ItineraryDay;
import com.travelplanner.entity.ItineraryItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ItineraryDTOs {

    public static class CreateItineraryItemRequest {
        @NotNull(message = "Day ID is required")
        private Long dayId;
        private ItineraryItem.ItemType itemType = ItineraryItem.ItemType.ACTIVITY;
        @NotBlank(message = "Title is required")
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
        private String bookingReference;
        private Integer displayOrder;

        public CreateItineraryItemRequest() {}

        public Long getDayId() { return dayId; }
        public void setDayId(Long dayId) { this.dayId = dayId; }
        public ItineraryItem.ItemType getItemType() { return itemType; }
        public void setItemType(ItineraryItem.ItemType itemType) { this.itemType = itemType; }
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
        public void setCost(BigDecimal cost) {
            if (cost != null) this.estimatedCost = cost;
        }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public String getBookingReference() { return bookingReference; }
        public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
        public Integer getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    }

    public static class UpdateItineraryItemRequest {
        private String title;
        private String description;
        private ItineraryItem.ItemType itemType;
        private String locationName;
        private String address;
        private Double latitude;
        private Double longitude;
        private LocalTime startTime;
        private LocalTime endTime;
        private BigDecimal estimatedCost;
        private String currency;
        private String bookingReference;
        private ItineraryItem.ItemStatus status;
        private Integer displayOrder;

        public UpdateItineraryItemRequest() {}

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public ItineraryItem.ItemType getItemType() { return itemType; }
        public void setItemType(ItineraryItem.ItemType itemType) { this.itemType = itemType; }
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
        public String getBookingReference() { return bookingReference; }
        public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
        public ItineraryItem.ItemStatus getStatus() { return status; }
        public void setStatus(ItineraryItem.ItemStatus status) { this.status = status; }
        public Integer getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    }

    public static class ReorderItemRequest {
        @NotNull
        private Long itemId;
        @NotNull
        private Long targetDayId;
        @NotNull
        private Integer targetOrder;

        public ReorderItemRequest() {}
        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        public Long getTargetDayId() { return targetDayId; }
        public void setTargetDayId(Long targetDayId) { this.targetDayId = targetDayId; }
        public Integer getTargetOrder() { return targetOrder; }
        public void setTargetOrder(Integer targetOrder) { this.targetOrder = targetOrder; }
    }

    public static class ItineraryDayResponse {
        private Long id;
        private Long tripId;
        private Integer dayNumber;
        private LocalDate date;
        private String title;
        private String notes;
        private List<ItineraryItemResponse> items;
        private List<ItineraryConflictDTO> conflicts;

        public ItineraryDayResponse() {}

        public static ItineraryDayResponse fromEntity(ItineraryDay day) {
            if (day == null) return null;
            ItineraryDayResponse r = new ItineraryDayResponse();
            r.id = day.getId();
            r.tripId = day.getTrip() != null ? day.getTrip().getId() : null;
            r.dayNumber = day.getDayNumber();
            r.date = day.getDate();
            r.title = day.getTitle();
            r.notes = day.getNotes();
            r.items = day.getItems() != null ? 
                    day.getItems().stream().map(ItineraryItemResponse::fromEntity).collect(Collectors.toList()) : 
                    List.of();
            return r;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getTripId() { return tripId; }
        public void setTripId(Long tripId) { this.tripId = tripId; }
        public Integer getDayNumber() { return dayNumber; }
        public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public List<ItineraryItemResponse> getItems() { return items; }
        public void setItems(List<ItineraryItemResponse> items) { this.items = items; }
        public List<ItineraryConflictDTO> getConflicts() { return conflicts; }
        public void setConflicts(List<ItineraryConflictDTO> conflicts) { this.conflicts = conflicts; }
    }

    public static class ItineraryItemResponse {
        private Long id;
        private Long dayId;
        private ItineraryItem.ItemType itemType;
        private String title;
        private String description;
        private String locationName;
        private String address;
        private Double latitude;
        private Double longitude;
        private LocalTime startTime;
        private LocalTime endTime;
        private BigDecimal estimatedCost;
        private String currency;
        private Integer displayOrder;
        private String bookingReference;
        private ItineraryItem.ItemStatus status;

        public ItineraryItemResponse() {}

        public static ItineraryItemResponse fromEntity(ItineraryItem item) {
            if (item == null) return null;
            ItineraryItemResponse r = new ItineraryItemResponse();
            r.id = item.getId();
            r.dayId = item.getDay() != null ? item.getDay().getId() : null;
            r.itemType = item.getItemType();
            r.title = item.getTitle();
            r.description = item.getDescription();
            r.locationName = item.getLocationName();
            r.address = item.getAddress();
            r.latitude = item.getLatitude();
            r.longitude = item.getLongitude();
            r.startTime = item.getStartTime();
            r.endTime = item.getEndTime();
            r.estimatedCost = item.getEstimatedCost();
            r.currency = item.getCurrency();
            r.displayOrder = item.getDisplayOrder();
            r.bookingReference = item.getBookingReference();
            r.status = item.getStatus();
            return r;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getDayId() { return dayId; }
        public void setDayId(Long dayId) { this.dayId = dayId; }
        public ItineraryItem.ItemType getItemType() { return itemType; }
        public void setItemType(ItineraryItem.ItemType itemType) { this.itemType = itemType; }
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
        public ItineraryItem.ItemStatus getStatus() { return status; }
        public void setStatus(ItineraryItem.ItemStatus status) { this.status = status; }
    }

    public static class ItineraryConflictDTO {
        private String type;
        private Long itemAId;
        private String itemATitle;
        private Long itemBId;
        private String itemBTitle;
        private String message;

        public ItineraryConflictDTO() {}
        public ItineraryConflictDTO(String type, Long itemAId, String itemATitle, Long itemBId, String itemBTitle, String message) {
            this.type = type;
            this.itemAId = itemAId;
            this.itemATitle = itemATitle;
            this.itemBId = itemBId;
            this.itemBTitle = itemBTitle;
            this.message = message;
        }

        public static ItineraryConflictDTOBuilder builder() { return new ItineraryConflictDTOBuilder(); }
        public String getType() { return type; }
        public Long getItemAId() { return itemAId; }
        public String getItemATitle() { return itemATitle; }
        public Long getItemBId() { return itemBId; }
        public String getItemBTitle() { return itemBTitle; }
        public String getMessage() { return message; }

        public static class ItineraryConflictDTOBuilder {
            private String type;
            private Long itemAId;
            private String itemATitle;
            private Long itemBId;
            private String itemBTitle;
            private String message;
            public ItineraryConflictDTOBuilder type(String type) { this.type = type; return this; }
            public ItineraryConflictDTOBuilder itemAId(Long itemAId) { this.itemAId = itemAId; return this; }
            public ItineraryConflictDTOBuilder itemATitle(String itemATitle) { this.itemATitle = itemATitle; return this; }
            public ItineraryConflictDTOBuilder itemBId(Long itemBId) { this.itemBId = itemBId; return this; }
            public ItineraryConflictDTOBuilder itemBTitle(String itemBTitle) { this.itemBTitle = itemBTitle; return this; }
            public ItineraryConflictDTOBuilder message(String message) { this.message = message; return this; }
            public ItineraryConflictDTO build() { return new ItineraryConflictDTO(type, itemAId, itemATitle, itemBId, itemBTitle, message); }
        }
    }
}
