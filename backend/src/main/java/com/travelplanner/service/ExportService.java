package com.travelplanner.service;

import com.travelplanner.dto.BookingDTOs.FlightOfferDTO;
import com.travelplanner.dto.BookingDTOs.HotelOfferDTO;
import com.travelplanner.dto.BudgetDTOs.BudgetSummaryDTO;
import com.travelplanner.dto.BudgetDTOs.ExpenseResponse;
import com.travelplanner.dto.ExportDTOs.TripExportDataDTO;
import com.travelplanner.dto.ItineraryDTOs.ItineraryDayResponse;
import com.travelplanner.dto.ItineraryDTOs.ItineraryItemResponse;
import com.travelplanner.dto.TripDTOs.TripDetailResponse;
import com.travelplanner.entity.Accommodation;
import com.travelplanner.entity.Flight;
import com.travelplanner.repository.AccommodationRepository;
import com.travelplanner.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportService {

    private static final Logger log = LoggerFactory.getLogger(ExportService.class);

    private final TripService tripService;
    private final BudgetService budgetService;
    private final FlightRepository flightRepository;
    private final AccommodationRepository accommodationRepository;

    public ExportService(TripService tripService, BudgetService budgetService, FlightRepository flightRepository, AccommodationRepository accommodationRepository) {
        this.tripService = tripService;
        this.budgetService = budgetService;
        this.flightRepository = flightRepository;
        this.accommodationRepository = accommodationRepository;
    }

    @Transactional(readOnly = true)
    public TripExportDataDTO getTripExportData(Long tripId, Long userId) {
        TripDetailResponse tripDetail = tripService.getTripDetails(tripId, userId);
        BudgetSummaryDTO budget = budgetService.getBudgetSummary(tripId, userId);
        List<ExpenseResponse> expenses = budgetService.getTripExpenses(tripId, userId);

        List<FlightOfferDTO> flights = flightRepository.findByTripIdOrderByDepartureTimeAsc(tripId).stream()
                .map(f -> FlightOfferDTO.builder()
                        .id(String.valueOf(f.getId()))
                        .airline(f.getAirline())
                        .flightNumber(f.getFlightNumber())
                        .originCode(f.getDepartureAirport())
                        .destinationCode(f.getArrivalAirport())
                        .departureTime(f.getDepartureTime())
                        .arrivalTime(f.getArrivalTime())
                        .durationMinutes(f.getDurationMinutes())
                        .price(f.getPrice())
                        .currency(f.getCurrency())
                        .cabinClass(f.getSeatClass())
                        .build())
                .collect(Collectors.toList());

        List<HotelOfferDTO> stays = accommodationRepository.findByTripIdOrderByCheckInDateAsc(tripId).stream()
                .map(a -> HotelOfferDTO.builder()
                        .id(String.valueOf(a.getId()))
                        .name(a.getName())
                        .type(a.getType())
                        .address(a.getAddress())
                        .pricePerNight(a.getPricePerNight())
                        .totalPrice(a.getTotalCost())
                        .currency(a.getCurrency())
                        .build())
                .collect(Collectors.toList());

        return TripExportDataDTO.builder()
                .trip(tripDetail)
                .bookedFlights(flights)
                .bookedAccommodations(stays)
                .budget(budget)
                .expenses(expenses)
                .generatedAt(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .shareUrl("/trips/shared/" + tripDetail.getShareCode())
                .emergencyContacts("Local Emergency: 112 / 911 | Embassy: +1-800-TRAVEL")
                .build();
    }

    @Transactional(readOnly = true)
    public String generateIcsCalendar(Long tripId, Long userId) {
        TripDetailResponse trip = tripService.getTripDetails(tripId, userId);

        StringBuilder ics = new StringBuilder();
        ics.append("BEGIN:VCALENDAR\r\n");
        ics.append("VERSION:2.0\r\n");
        ics.append("PRODID:-//TravelPlanner//Itinerary//EN\r\n");
        ics.append("CALSCALE:GREGORIAN\r\n");
        ics.append("METHOD:PUBLISH\r\n");
        ics.append("X-WR-CALNAME:").append(escapeIcs(trip.getTitle())).append("\r\n");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

        if (trip.getDays() != null) {
            for (ItineraryDayResponse day : trip.getDays()) {
                if (day.getItems() != null) {
                    for (ItineraryItemResponse item : day.getItems()) {
                        ics.append("BEGIN:VEVENT\r\n");
                        ics.append("UID:item-").append(item.getId()).append("@travelplanner.io\r\n");

                        LocalDate itemDate = day.getDate();
                        LocalTime startTime = item.getStartTime() != null ? item.getStartTime() : LocalTime.of(9, 0);
                        LocalTime endTime = item.getEndTime() != null ? item.getEndTime() : startTime.plusHours(2);

                        ZonedDateTime startZdt = itemDate.atTime(startTime).atZone(java.time.ZoneOffset.UTC);
                        ZonedDateTime endZdt = itemDate.atTime(endTime).atZone(java.time.ZoneOffset.UTC);

                        ics.append("DTSTAMP:").append(ZonedDateTime.now().format(dtf)).append("\r\n");
                        ics.append("DTSTART:").append(startZdt.format(dtf)).append("\r\n");
                        ics.append("DTEND:").append(endZdt.format(dtf)).append("\r\n");
                        ics.append("SUMMARY:").append(escapeIcs(item.getTitle())).append("\r\n");

                        if (item.getDescription() != null) {
                            ics.append("DESCRIPTION:").append(escapeIcs(item.getDescription())).append("\r\n");
                        }
                        if (item.getLocationName() != null || item.getAddress() != null) {
                            String loc = (item.getLocationName() != null ? item.getLocationName() : "") +
                                    (item.getAddress() != null ? ", " + item.getAddress() : "");
                            ics.append("LOCATION:").append(escapeIcs(loc)).append("\r\n");
                        }

                        ics.append("STATUS:CONFIRMED\r\n");
                        ics.append("END:VEVENT\r\n");
                    }
                }
            }
        }

        ics.append("END:VCALENDAR\r\n");
        return ics.toString();
    }

    private String escapeIcs(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace(";", "\\;")
                   .replace(",", "\\,")
                   .replace("\n", "\\n");
    }
}
