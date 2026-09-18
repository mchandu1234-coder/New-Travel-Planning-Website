package com.travelplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public class BookingDTOs {

    public static class FlightSearchRequest {
        @NotBlank
        private String origin = "JFK";
        @NotBlank
        private String destination = "CDG";
        private LocalDate departureDate;
        private LocalDate returnDate;
        private Integer adults = 1;
        private String travelClass = "ECONOMY";

        public FlightSearchRequest() {}

        public String getOrigin() { return origin != null ? origin : "JFK"; }
        public void setOrigin(String origin) { this.origin = origin; }
        public String getDestination() { return destination != null ? destination : "CDG"; }
        public void setDestination(String destination) { this.destination = destination; }
        public LocalDate getDepartureDate() { return departureDate != null ? departureDate : LocalDate.now().plusDays(14); }
        public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }
        public void setDate(LocalDate date) { if (this.departureDate == null) this.departureDate = date; }
        public LocalDate getReturnDate() { return returnDate; }
        public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
        public Integer getAdults() { return adults != null ? adults : 1; }
        public void setAdults(Integer adults) { this.adults = adults; }
        public String getTravelClass() { return travelClass != null ? travelClass : "ECONOMY"; }
        public void setTravelClass(String travelClass) { this.travelClass = travelClass; }
    }

    public static class FlightOfferDTO {
        private String id;
        private String airline;
        private String airlineLogo;
        private String flightNumber;
        private String originCode;
        private String originAirport;
        private String destinationCode;
        private String destinationAirport;
        private ZonedDateTime departureTime;
        private ZonedDateTime arrivalTime;
        private Integer durationMinutes;
        private Integer stops;
        private BigDecimal price;
        private String currency;
        private String cabinClass;

        public FlightOfferDTO() {}

        public static FlightOfferDTOBuilder builder() { return new FlightOfferDTOBuilder(); }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getAirline() { return airline; }
        public void setAirline(String airline) { this.airline = airline; }
        public String getAirlineLogo() { return airlineLogo; }
        public void setAirlineLogo(String airlineLogo) { this.airlineLogo = airlineLogo; }
        public String getFlightNumber() { return flightNumber; }
        public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
        public String getOriginCode() { return originCode; }
        public void setOriginCode(String originCode) { this.originCode = originCode; }
        public String getOriginAirport() { return originAirport; }
        public void setOriginAirport(String originAirport) { this.originAirport = originAirport; }
        public String getDestinationCode() { return destinationCode; }
        public void setDestinationCode(String destinationCode) { this.destinationCode = destinationCode; }
        public String getDestinationAirport() { return destinationAirport; }
        public void setDestinationAirport(String destinationAirport) { this.destinationAirport = destinationAirport; }
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
        public String getCabinClass() { return cabinClass; }
        public void setCabinClass(String cabinClass) { this.cabinClass = cabinClass; }

        public static class FlightOfferDTOBuilder {
            private String id;
            private String airline;
            private String airlineLogo;
            private String flightNumber;
            private String originCode;
            private String originAirport;
            private String destinationCode;
            private String destinationAirport;
            private ZonedDateTime departureTime;
            private ZonedDateTime arrivalTime;
            private Integer durationMinutes;
            private Integer stops;
            private BigDecimal price;
            private String currency;
            private String cabinClass;

            public FlightOfferDTOBuilder id(String id) { this.id = id; return this; }
            public FlightOfferDTOBuilder airline(String airline) { this.airline = airline; return this; }
            public FlightOfferDTOBuilder airlineLogo(String airlineLogo) { this.airlineLogo = airlineLogo; return this; }
            public FlightOfferDTOBuilder flightNumber(String flightNumber) { this.flightNumber = flightNumber; return this; }
            public FlightOfferDTOBuilder originCode(String originCode) { this.originCode = originCode; return this; }
            public FlightOfferDTOBuilder originAirport(String originAirport) { this.originAirport = originAirport; return this; }
            public FlightOfferDTOBuilder destinationCode(String destinationCode) { this.destinationCode = destinationCode; return this; }
            public FlightOfferDTOBuilder destinationAirport(String destinationAirport) { this.destinationAirport = destinationAirport; return this; }
            public FlightOfferDTOBuilder departureTime(ZonedDateTime departureTime) { this.departureTime = departureTime; return this; }
            public FlightOfferDTOBuilder arrivalTime(ZonedDateTime arrivalTime) { this.arrivalTime = arrivalTime; return this; }
            public FlightOfferDTOBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
            public FlightOfferDTOBuilder stops(Integer stops) { this.stops = stops; return this; }
            public FlightOfferDTOBuilder price(BigDecimal price) { this.price = price; return this; }
            public FlightOfferDTOBuilder currency(String currency) { this.currency = currency; return this; }
            public FlightOfferDTOBuilder cabinClass(String cabinClass) { this.cabinClass = cabinClass; return this; }

            public FlightOfferDTO build() {
                FlightOfferDTO dto = new FlightOfferDTO();
                dto.id = id; dto.airline = airline; dto.airlineLogo = airlineLogo; dto.flightNumber = flightNumber;
                dto.originCode = originCode; dto.originAirport = originAirport; dto.destinationCode = destinationCode;
                dto.destinationAirport = destinationAirport; dto.departureTime = departureTime; dto.arrivalTime = arrivalTime;
                dto.durationMinutes = durationMinutes; dto.stops = stops; dto.price = price; dto.currency = currency;
                dto.cabinClass = cabinClass;
                return dto;
            }
        }
    }

    public static class BookFlightRequest {
        @NotNull
        private Long tripId;
        private Long dayId;
        private FlightOfferDTO flightOffer;
        private String passengerName;
        private String passengerEmail;
        private String paymentMethodId;

        public BookFlightRequest() {}
        public Long getTripId() { return tripId; }
        public void setTripId(Long tripId) { this.tripId = tripId; }
        public Long getDayId() { return dayId; }
        public void setDayId(Long dayId) { this.dayId = dayId; }
        public FlightOfferDTO getFlightOffer() { return flightOffer; }
        public void setFlightOffer(FlightOfferDTO flightOffer) { this.flightOffer = flightOffer; }
        public String getPassengerName() { return passengerName; }
        public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
        public String getPassengerEmail() { return passengerEmail; }
        public void setPassengerEmail(String passengerEmail) { this.passengerEmail = passengerEmail; }
        public String getPaymentMethodId() { return paymentMethodId; }
        public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    }

    public static class HotelSearchRequest {
        @NotBlank
        private String destinationCity;
        @NotNull
        private LocalDate checkInDate;
        @NotNull
        private LocalDate checkOutDate;
        private Integer guests = 1;
        private Integer minRating = 3;

        public HotelSearchRequest() {}
        public String getDestinationCity() { return destinationCity; }
        public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }
        public LocalDate getCheckInDate() { return checkInDate; }
        public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
        public LocalDate getCheckOutDate() { return checkOutDate; }
        public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
        public Integer getGuests() { return guests; }
        public void setGuests(Integer guests) { this.guests = guests; }
        public Integer getMinRating() { return minRating; }
        public void setMinRating(Integer minRating) { this.minRating = minRating; }
    }

    public static class HotelOfferDTO {
        private String id;
        private String name;
        private String type;
        private String address;
        private BigDecimal rating;
        private Integer reviewCount;
        private BigDecimal pricePerNight;
        private BigDecimal totalPrice;
        private String currency;
        private String imageUrl;
        private List<String> amenities;
        private Double latitude;
        private Double longitude;

        public HotelOfferDTO() {}
        public static HotelOfferDTOBuilder builder() { return new HotelOfferDTOBuilder(); }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public BigDecimal getRating() { return rating; }
        public void setRating(BigDecimal rating) { this.rating = rating; }
        public Integer getReviewCount() { return reviewCount; }
        public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
        public BigDecimal getPricePerNight() { return pricePerNight; }
        public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }
        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public List<String> getAmenities() { return amenities; }
        public void setAmenities(List<String> amenities) { this.amenities = amenities; }
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }

        public static class HotelOfferDTOBuilder {
            private String id;
            private String name;
            private String type;
            private String address;
            private BigDecimal rating;
            private Integer reviewCount;
            private BigDecimal pricePerNight;
            private BigDecimal totalPrice;
            private String currency;
            private String imageUrl;
            private List<String> amenities;
            private Double latitude;
            private Double longitude;

            public HotelOfferDTOBuilder id(String id) { this.id = id; return this; }
            public HotelOfferDTOBuilder name(String name) { this.name = name; return this; }
            public HotelOfferDTOBuilder type(String type) { this.type = type; return this; }
            public HotelOfferDTOBuilder address(String address) { this.address = address; return this; }
            public HotelOfferDTOBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
            public HotelOfferDTOBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
            public HotelOfferDTOBuilder pricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; return this; }
            public HotelOfferDTOBuilder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }
            public HotelOfferDTOBuilder currency(String currency) { this.currency = currency; return this; }
            public HotelOfferDTOBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
            public HotelOfferDTOBuilder amenities(List<String> amenities) { this.amenities = amenities; return this; }
            public HotelOfferDTOBuilder latitude(Double latitude) { this.latitude = latitude; return this; }
            public HotelOfferDTOBuilder longitude(Double longitude) { this.longitude = longitude; return this; }

            public HotelOfferDTO build() {
                HotelOfferDTO h = new HotelOfferDTO();
                h.id = id; h.name = name; h.type = type; h.address = address; h.rating = rating;
                h.reviewCount = reviewCount; h.pricePerNight = pricePerNight; h.totalPrice = totalPrice;
                h.currency = currency; h.imageUrl = imageUrl; h.amenities = amenities;
                h.latitude = latitude; h.longitude = longitude;
                return h;
            }
        }
    }

    public static class BookHotelRequest {
        @NotNull
        private Long tripId;
        private Long dayId;
        private HotelOfferDTO hotelOffer;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private String guestName;
        private String guestEmail;
        private String paymentMethodId;

        public BookHotelRequest() {}
        public Long getTripId() { return tripId; }
        public void setTripId(Long tripId) { this.tripId = tripId; }
        public Long getDayId() { return dayId; }
        public void setDayId(Long dayId) { this.dayId = dayId; }
        public HotelOfferDTO getHotelOffer() { return hotelOffer; }
        public void setHotelOffer(HotelOfferDTO hotelOffer) { this.hotelOffer = hotelOffer; }
        public LocalDate getCheckInDate() { return checkInDate; }
        public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
        public LocalDate getCheckOutDate() { return checkOutDate; }
        public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
        public String getGuestName() { return guestName; }
        public void setGuestName(String guestName) { this.guestName = guestName; }
        public String getGuestEmail() { return guestEmail; }
        public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }
        public String getPaymentMethodId() { return paymentMethodId; }
        public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    }

    public static class BookingConfirmationDTO {
        private String bookingReference;
        private String bookingType;
        private String status;
        private BigDecimal totalPaid;
        private String currency;
        private String paymentIntentId;
        private String receiptNumber;
        private Object details;
        private Long itineraryItemId;
        private Long expenseId;

        public BookingConfirmationDTO() {}
        public static BookingConfirmationDTOBuilder builder() { return new BookingConfirmationDTOBuilder(); }

        public String getBookingReference() { return bookingReference; }
        public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
        public String getBookingType() { return bookingType; }
        public void setBookingType(String bookingType) { this.bookingType = bookingType; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public BigDecimal getTotalPaid() { return totalPaid; }
        public void setTotalPaid(BigDecimal totalPaid) { this.totalPaid = totalPaid; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public String getPaymentIntentId() { return paymentIntentId; }
        public void setPaymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; }
        public String getReceiptNumber() { return receiptNumber; }
        public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
        public Object getDetails() { return details; }
        public void setDetails(Object details) { this.details = details; }
        public Long getItineraryItemId() { return itineraryItemId; }
        public void setItineraryItemId(Long itineraryItemId) { this.itineraryItemId = itineraryItemId; }
        public Long getExpenseId() { return expenseId; }
        public void setExpenseId(Long expenseId) { this.expenseId = expenseId; }

        public static class BookingConfirmationDTOBuilder {
            private String bookingReference;
            private String bookingType;
            private String status;
            private BigDecimal totalPaid;
            private String currency;
            private String paymentIntentId;
            private String receiptNumber;
            private Object details;
            private Long itineraryItemId;
            private Long expenseId;

            public BookingConfirmationDTOBuilder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
            public BookingConfirmationDTOBuilder bookingType(String bookingType) { this.bookingType = bookingType; return this; }
            public BookingConfirmationDTOBuilder status(String status) { this.status = status; return this; }
            public BookingConfirmationDTOBuilder totalPaid(BigDecimal totalPaid) { this.totalPaid = totalPaid; return this; }
            public BookingConfirmationDTOBuilder currency(String currency) { this.currency = currency; return this; }
            public BookingConfirmationDTOBuilder paymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; return this; }
            public BookingConfirmationDTOBuilder receiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; return this; }
            public BookingConfirmationDTOBuilder details(Object details) { this.details = details; return this; }
            public BookingConfirmationDTOBuilder itineraryItemId(Long itineraryItemId) { this.itineraryItemId = itineraryItemId; return this; }
            public BookingConfirmationDTOBuilder expenseId(Long expenseId) { this.expenseId = expenseId; return this; }

            public BookingConfirmationDTO build() {
                BookingConfirmationDTO dto = new BookingConfirmationDTO();
                dto.bookingReference = bookingReference; dto.bookingType = bookingType; dto.status = status;
                dto.totalPaid = totalPaid; dto.currency = currency; dto.paymentIntentId = paymentIntentId;
                dto.receiptNumber = receiptNumber; dto.details = details; dto.itineraryItemId = itineraryItemId;
                dto.expenseId = expenseId;
                return dto;
            }
        }
    }
}
