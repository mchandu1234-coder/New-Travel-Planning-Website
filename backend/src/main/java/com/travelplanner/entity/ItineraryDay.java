package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itinerary_days", uniqueConstraints = {
    @UniqueConstraint(name = "uq_trip_day", columnNames = {"trip_id", "day_number"})
})
public class ItineraryDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @JsonIgnore
    private Trip trip;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC, startTime ASC")
    private List<ItineraryItem> items = new ArrayList<>();

    public ItineraryDay() {}

    public ItineraryDay(Long id, Trip trip, Integer dayNumber, LocalDate date, String title, String notes, List<ItineraryItem> items) {
        this.id = id;
        this.trip = trip;
        this.dayNumber = dayNumber;
        this.date = date;
        this.title = title;
        this.notes = notes;
        this.items = items != null ? items : new ArrayList<>();
    }

    public static ItineraryDayBuilder builder() {
        return new ItineraryDayBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }
    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<ItineraryItem> getItems() { return items; }
    public void setItems(List<ItineraryItem> items) { this.items = items; }

    public static class ItineraryDayBuilder {
        private Long id;
        private Trip trip;
        private Integer dayNumber;
        private LocalDate date;
        private String title;
        private String notes;
        private List<ItineraryItem> items = new ArrayList<>();

        public ItineraryDayBuilder id(Long id) { this.id = id; return this; }
        public ItineraryDayBuilder trip(Trip trip) { this.trip = trip; return this; }
        public ItineraryDayBuilder dayNumber(Integer dayNumber) { this.dayNumber = dayNumber; return this; }
        public ItineraryDayBuilder date(LocalDate date) { this.date = date; return this; }
        public ItineraryDayBuilder title(String title) { this.title = title; return this; }
        public ItineraryDayBuilder notes(String notes) { this.notes = notes; return this; }
        public ItineraryDayBuilder items(List<ItineraryItem> items) { this.items = items; return this; }

        public ItineraryDay build() {
            return new ItineraryDay(id, trip, dayNumber, date, title, notes, items);
        }
    }
}
