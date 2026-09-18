package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @JsonIgnore
    private Trip trip;

    @Column(nullable = false, length = 100)
    private String airline;

    @Column(name = "flight_number", nullable = false, length = 50)
    private String flightNumber;

    @Column(name = "departure_airport", nullable = false, length = 10)
    private String departureAirport;

    @Column(name = "arrival_airport", nullable = false, length = 10)
    private String arrivalAirport;

    @Column(name = "departure_time", nullable = false)
    private ZonedDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private ZonedDateTime arrivalTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer stops = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 10)
    private String currency = "USD";

    @Column(name = "seat_class", length = 50)
    private String seatClass = "ECONOMY";

    @Column(name = "booking_reference", length = 100)
    private String bookingReference;

    @Column(name = "booking_status", length = 50)
    private String bookingStatus = "CONFIRMED";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    public Flight() {}

    public Flight(Long id, Trip trip, String airline, String flightNumber, String departureAirport, String arrivalAirport, ZonedDateTime departureTime, ZonedDateTime arrivalTime, Integer durationMinutes, Integer stops, BigDecimal price, String currency, String seatClass, String bookingReference, String bookingStatus, ZonedDateTime createdAt) {
        this.id = id;
        this.trip = trip;
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.durationMinutes = durationMinutes;
        this.stops = stops != null ? stops : 0;
        this.price = price;
        this.currency = currency != null ? currency : "USD";
        this.seatClass = seatClass != null ? seatClass : "ECONOMY";
        this.bookingReference = bookingReference;
        this.bookingStatus = bookingStatus != null ? bookingStatus : "CONFIRMED";
        this.createdAt = createdAt;
    }

    public static FlightBuilder builder() {
        return new FlightBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }
    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public String getDepartureAirport() { return departureAirport; }
    public void setDepartureAirport(String departureAirport) { this.departureAirport = departureAirport; }
    public String getArrivalAirport() { return arrivalAirport; }
    public void setArrivalAirport(String arrivalAirport) { this.arrivalAirport = arrivalAirport; }
    public ZonedDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(ZonedDateTime departureTime) { this.departureTime = departureTime; }
    public ZonedDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(ZonedDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public Integer getStops() { return stops; }
    public void setStops(Integer stops) { this.stops = stops; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getSeatClass() { return seatClass; }
    public void setSeatClass(String seatClass) { this.seatClass = seatClass; }
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public static class FlightBuilder {
        private Long id;
        private Trip trip;
        private String airline;
        private String flightNumber;
        private String departureAirport;
        private String arrivalAirport;
        private ZonedDateTime departureTime;
        private ZonedDateTime arrivalTime;
        private Integer durationMinutes;
        private Integer stops = 0;
        private BigDecimal price;
        private String currency = "USD";
        private String seatClass = "ECONOMY";
        private String bookingReference;
        private String bookingStatus = "CONFIRMED";
        private ZonedDateTime createdAt;

        public FlightBuilder id(Long id) { this.id = id; return this; }
        public FlightBuilder trip(Trip trip) { this.trip = trip; return this; }
        public FlightBuilder airline(String airline) { this.airline = airline; return this; }
        public FlightBuilder flightNumber(String flightNumber) { this.flightNumber = flightNumber; return this; }
        public FlightBuilder departureAirport(String departureAirport) { this.departureAirport = departureAirport; return this; }
        public FlightBuilder arrivalAirport(String arrivalAirport) { this.arrivalAirport = arrivalAirport; return this; }
        public FlightBuilder departureTime(ZonedDateTime departureTime) { this.departureTime = departureTime; return this; }
        public FlightBuilder arrivalTime(ZonedDateTime arrivalTime) { this.arrivalTime = arrivalTime; return this; }
        public FlightBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public FlightBuilder stops(Integer stops) { this.stops = stops; return this; }
        public FlightBuilder price(BigDecimal price) { this.price = price; return this; }
        public FlightBuilder currency(String currency) { this.currency = currency; return this; }
        public FlightBuilder seatClass(String seatClass) { this.seatClass = seatClass; return this; }
        public FlightBuilder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public FlightBuilder bookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; return this; }
        public FlightBuilder createdAt(ZonedDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Flight build() {
            return new Flight(id, trip, airline, flightNumber, departureAirport, arrivalAirport, departureTime, arrivalTime, durationMinutes, stops, price, currency, seatClass, bookingReference, bookingStatus, createdAt);
        }
    }
}
