package com.travelplanner.service;

import com.travelplanner.dto.BookingDTOs.*;
import com.travelplanner.entity.*;
import com.travelplanner.exception.ResourceNotFoundException;
import com.travelplanner.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final FlightRepository flightRepository;
    private final AccommodationRepository accommodationRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final ItineraryItemRepository itineraryItemRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final TripService tripService;
    private final CurrencyService currencyService;

    public BookingService(FlightRepository flightRepository, AccommodationRepository accommodationRepository, TripRepository tripRepository, UserRepository userRepository, ItineraryDayRepository itineraryDayRepository, ItineraryItemRepository itineraryItemRepository, ExpenseRepository expenseRepository, ExpenseSplitRepository expenseSplitRepository, TripService tripService, CurrencyService currencyService) {
        this.flightRepository = flightRepository;
        this.accommodationRepository = accommodationRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.itineraryDayRepository = itineraryDayRepository;
        this.itineraryItemRepository = itineraryItemRepository;
        this.expenseRepository = expenseRepository;
        this.expenseSplitRepository = expenseSplitRepository;
        this.tripService = tripService;
        this.currencyService = currencyService;
    }

    public List<FlightOfferDTO> searchFlights(FlightSearchRequest request) {
        log.info("Searching flights from {} to {} on {}", request.getOrigin(), request.getDestination(), request.getDepartureDate());

        List<FlightOfferDTO> offers = new ArrayList<>();
        String dest = request.getDestination().toUpperCase();
        LocalDate dept = request.getDepartureDate();

        String[] airlines = {"Air France", "Japan Airlines", "Delta Air Lines", "Emirates", "Lufthansa", "Singapore Airlines"};
        String[] flightPrefixes = {"AF", "JL", "DL", "EK", "LH", "SQ"};
        int[] basePrices = {650, 780, 590, 920, 710, 840};

        for (int i = 0; i < airlines.length; i++) {
            ZonedDateTime departure = dept.atTime(8 + (i * 2), (i * 15) % 60).atZone(java.time.ZoneId.of("UTC"));
            int duration = 480 + (i * 45);
            ZonedDateTime arrival = departure.plusMinutes(duration);
            BigDecimal price = BigDecimal.valueOf(basePrices[i] * (request.getTravelClass().equalsIgnoreCase("BUSINESS") ? 2.5 : 1.0));

            offers.add(FlightOfferDTO.builder()
                    .id("flt-" + UUID.randomUUID().toString().substring(0, 8))
                    .airline(airlines[i])
                    .airlineLogo("https://images.unsplash.com/photo-1436491865332-7a61a109cc05?auto=format&fit=crop&w=100&q=80")
                    .flightNumber(flightPrefixes[i] + (100 + i * 42))
                    .originCode(request.getOrigin().toUpperCase())
                    .originAirport(request.getOrigin().toUpperCase() + " International Airport")
                    .destinationCode(dest)
                    .destinationAirport(dest + " Airport")
                    .departureTime(departure)
                    .arrivalTime(arrival)
                    .durationMinutes(duration)
                    .stops(i % 2 == 0 ? 0 : 1)
                    .price(price)
                    .currency("USD")
                    .cabinClass(request.getTravelClass())
                    .build());
        }

        return offers;
    }

    public List<HotelOfferDTO> searchHotels(HotelSearchRequest request) {
        log.info("Searching hotels in {} for dates {} to {}", request.getDestinationCity(), request.getCheckInDate(), request.getCheckOutDate());

        List<HotelOfferDTO> hotels = new ArrayList<>();
        String city = request.getDestinationCity();

        String[] hotelNames = {
                "Grand Palace Hotel & Spa " + city,
                "The Ritz Reserve " + city,
                "Boutique Haven Resort " + city,
                "Urban Loft Suites " + city,
                "Sunset Panorama Hotel " + city
        };
        String[] types = {"LUXURY_HOTEL", "RESORT", "BOUTIQUE", "APARTMENT", "HOTEL"};
        double[] ratings = {4.9, 4.8, 4.7, 4.6, 4.5};
        int[] reviewCounts = {520, 890, 310, 440, 290};
        double[] nightPrices = {320.0, 450.0, 180.0, 140.0, 210.0};
        String[] images = {
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=600&q=80",
                "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=600&q=80",
                "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=600&q=80",
                "https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=600&q=80",
                "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=600&q=80"
        };

        long nights = Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate()));

        for (int i = 0; i < hotelNames.length; i++) {
            BigDecimal perNight = BigDecimal.valueOf(nightPrices[i]);
            BigDecimal total = perNight.multiply(BigDecimal.valueOf(nights));

            hotels.add(HotelOfferDTO.builder()
                    .id("htl-" + UUID.randomUUID().toString().substring(0, 8))
                    .name(hotelNames[i])
                    .type(types[i])
                    .address("Central District, " + city)
                    .rating(BigDecimal.valueOf(ratings[i]))
                    .reviewCount(reviewCounts[i])
                    .pricePerNight(perNight)
                    .totalPrice(total)
                    .currency("USD")
                    .imageUrl(images[i])
                    .amenities(List.of("Free High-Speed Wi-Fi", "Infinity Pool", "Gourmet Breakfast", "Spa & Wellness", "Airport Shuttle"))
                    .latitude(40.0 + (i * 0.01))
                    .longitude(10.0 + (i * 0.01))
                    .build());
        }

        return hotels;
    }

    @Transactional
    public BookingConfirmationDTO bookFlight(Long userId, BookFlightRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(request.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        tripService.validateEditorAccess(trip, userId);

        String bookingRef = "FL-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        FlightOfferDTO offer = request.getFlightOffer();

        Flight flight = Flight.builder()
                .trip(trip)
                .airline(offer.getAirline())
                .flightNumber(offer.getFlightNumber())
                .departureAirport(offer.getOriginCode())
                .arrivalAirport(offer.getDestinationCode())
                .departureTime(offer.getDepartureTime())
                .arrivalTime(offer.getArrivalTime())
                .durationMinutes(offer.getDurationMinutes())
                .stops(offer.getStops())
                .price(offer.getPrice())
                .currency(offer.getCurrency())
                .seatClass(offer.getCabinClass())
                .bookingReference(bookingRef)
                .bookingStatus("CONFIRMED")
                .build();
        flight = flightRepository.save(flight);

        ItineraryDay day = null;
        if (request.getDayId() != null) {
            day = itineraryDayRepository.findById(request.getDayId()).orElse(null);
        }
        if (day == null && !trip.getDays().isEmpty()) {
            day = trip.getDays().get(0);
        }

        Long itemId = null;
        if (day != null) {
            ItineraryItem item = ItineraryItem.builder()
                    .day(day)
                    .itemType(ItineraryItem.ItemType.FLIGHT)
                    .title("Flight " + offer.getFlightNumber() + " (" + offer.getOriginCode() + " -> " + offer.getDestinationCode() + ")")
                    .description(offer.getAirline() + " - " + offer.getCabinClass())
                    .locationName(offer.getOriginAirport())
                    .startTime(offer.getDepartureTime().toLocalTime())
                    .endTime(offer.getArrivalTime().toLocalTime())
                    .estimatedCost(offer.getPrice())
                    .currency(offer.getCurrency())
                    .bookingReference(bookingRef)
                    .status(ItineraryItem.ItemStatus.BOOKED)
                    .displayOrder(0)
                    .build();
            item = itineraryItemRepository.save(item);
            itemId = item.getId();
        }

        BigDecimal converted = currencyService.convert(offer.getPrice(), offer.getCurrency(), trip.getCurrency());
        Expense expense = Expense.builder()
                .trip(trip)
                .title("Flight " + offer.getFlightNumber() + " - " + offer.getAirline())
                .category(Expense.ExpenseCategory.FLIGHTS)
                .amount(offer.getPrice())
                .currency(offer.getCurrency())
                .convertedAmount(converted)
                .paidByUser(user)
                .splitType(Expense.SplitType.EQUAL)
                .date(offer.getDepartureTime().toLocalDate())
                .notes("Auto-booked flight reference: " + bookingRef)
                .build();
        expense = expenseRepository.save(expense);

        ExpenseSplit split = ExpenseSplit.builder()
                .expense(expense)
                .user(user)
                .amountOwed(converted)
                .isPaid(true)
                .build();
        expenseSplitRepository.save(split);

        trip.setActualSpend(trip.getActualSpend().add(converted));
        tripRepository.save(trip);

        return BookingConfirmationDTO.builder()
                .bookingReference(bookingRef)
                .bookingType("FLIGHT")
                .status("CONFIRMED")
                .totalPaid(offer.getPrice())
                .currency(offer.getCurrency())
                .paymentIntentId("pi_mock_" + UUID.randomUUID().toString().substring(0, 12))
                .receiptNumber("REC-FL-" + System.currentTimeMillis())
                .details(flight)
                .itineraryItemId(itemId)
                .expenseId(expense.getId())
                .build();
    }

    @Transactional
    public BookingConfirmationDTO bookHotel(Long userId, BookHotelRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(request.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        tripService.validateEditorAccess(trip, userId);

        String bookingRef = "HT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        HotelOfferDTO offer = request.getHotelOffer();

        Accommodation accommodation = Accommodation.builder()
                .trip(trip)
                .name(offer.getName())
                .type(offer.getType())
                .address(offer.getAddress())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .rating(offer.getRating())
                .pricePerNight(offer.getPricePerNight())
                .totalCost(offer.getTotalPrice())
                .currency(offer.getCurrency())
                .imageUrl(offer.getImageUrl())
                .amenities(String.join(", ", offer.getAmenities()))
                .bookingReference(bookingRef)
                .bookingStatus("CONFIRMED")
                .latitude(offer.getLatitude())
                .longitude(offer.getLongitude())
                .build();
        accommodation = accommodationRepository.save(accommodation);

        ItineraryDay day = null;
        if (request.getDayId() != null) {
            day = itineraryDayRepository.findById(request.getDayId()).orElse(null);
        }
        if (day == null && !trip.getDays().isEmpty()) {
            day = trip.getDays().get(0);
        }

        Long itemId = null;
        if (day != null) {
            ItineraryItem item = ItineraryItem.builder()
                    .day(day)
                    .itemType(ItineraryItem.ItemType.ACCOMMODATION)
                    .title("Check-in: " + offer.getName())
                    .description("Reservation for " + request.getCheckInDate() + " to " + request.getCheckOutDate())
                    .locationName(offer.getName())
                    .address(offer.getAddress())
                    .startTime(LocalTime.of(15, 0))
                    .endTime(LocalTime.of(16, 0))
                    .estimatedCost(offer.getTotalPrice())
                    .currency(offer.getCurrency())
                    .bookingReference(bookingRef)
                    .status(ItineraryItem.ItemStatus.BOOKED)
                    .displayOrder(1)
                    .build();
            item = itineraryItemRepository.save(item);
            itemId = item.getId();
        }

        BigDecimal converted = currencyService.convert(offer.getTotalPrice(), offer.getCurrency(), trip.getCurrency());
        Expense expense = Expense.builder()
                .trip(trip)
                .title("Stay: " + offer.getName())
                .category(Expense.ExpenseCategory.STAYS)
                .amount(offer.getTotalPrice())
                .currency(offer.getCurrency())
                .convertedAmount(converted)
                .paidByUser(user)
                .splitType(Expense.SplitType.EQUAL)
                .date(request.getCheckInDate())
                .notes("Auto-booked hotel reference: " + bookingRef)
                .build();
        expense = expenseRepository.save(expense);

        ExpenseSplit split = ExpenseSplit.builder()
                .expense(expense)
                .user(user)
                .amountOwed(converted)
                .isPaid(true)
                .build();
        expenseSplitRepository.save(split);

        trip.setActualSpend(trip.getActualSpend().add(converted));
        tripRepository.save(trip);

        return BookingConfirmationDTO.builder()
                .bookingReference(bookingRef)
                .bookingType("HOTEL")
                .status("CONFIRMED")
                .totalPaid(offer.getTotalPrice())
                .currency(offer.getCurrency())
                .paymentIntentId("pi_mock_" + UUID.randomUUID().toString().substring(0, 12))
                .receiptNumber("REC-HT-" + System.currentTimeMillis())
                .details(accommodation)
                .itineraryItemId(itemId)
                .expenseId(expense.getId())
                .build();
    }
}
