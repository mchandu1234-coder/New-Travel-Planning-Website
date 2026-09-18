package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.BookingDTOs.*;
import com.travelplanner.security.UserPrincipal;
import com.travelplanner.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Bookings", description = "Endpoints for searching and booking flights and accommodations with Stripe simulation")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/flights/search")
    @Operation(summary = "Search flights across airlines")
    public ResponseEntity<ApiResponse<List<FlightOfferDTO>>> searchFlights(@Valid @RequestBody FlightSearchRequest request) {
        List<FlightOfferDTO> offers = bookingService.searchFlights(request);
        return ResponseEntity.ok(ApiResponse.ok(offers));
    }

    @PostMapping("/hotels/search")
    @Operation(summary = "Search accommodations & hotels")
    public ResponseEntity<ApiResponse<List<HotelOfferDTO>>> searchHotels(@Valid @RequestBody HotelSearchRequest request) {
        List<HotelOfferDTO> hotels = bookingService.searchHotels(request);
        return ResponseEntity.ok(ApiResponse.ok(hotels));
    }

    @PostMapping("/flights/book")
    @Operation(summary = "Book a flight and link directly to trip itinerary and budget expenses")
    public ResponseEntity<ApiResponse<BookingConfirmationDTO>> bookFlight(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody BookFlightRequest request) {
        BookingConfirmationDTO confirmation = bookingService.bookFlight(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Flight booked successfully", confirmation));
    }

    @PostMapping("/hotels/book")
    @Operation(summary = "Book a hotel/stay and link directly to trip itinerary and budget expenses")
    public ResponseEntity<ApiResponse<BookingConfirmationDTO>> bookHotel(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody BookHotelRequest request) {
        BookingConfirmationDTO confirmation = bookingService.bookHotel(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Hotel booked successfully", confirmation));
    }
}
