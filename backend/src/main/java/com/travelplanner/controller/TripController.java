package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.TripDTOs.*;
import com.travelplanner.security.UserPrincipal;
import com.travelplanner.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
@Tag(name = "Trips", description = "Endpoints for creating and managing trips, wizards, and sharing")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    @Operation(summary = "Create a new trip via wizard", description = "Generates trip, day-by-day itineraries, and collaborator record")
    public ResponseEntity<ApiResponse<TripSummaryResponse>> createTrip(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateTripRequest request) {
        TripSummaryResponse response = tripService.createTrip(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Trip created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all trips for the authenticated user")
    public ResponseEntity<ApiResponse<List<TripSummaryResponse>>> getUserTrips(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<TripSummaryResponse> trips = tripService.getUserTrips(principal.getId());
        return ResponseEntity.ok(ApiResponse.ok(trips));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get full trip details with day-by-day itinerary and collaborators")
    public ResponseEntity<ApiResponse<TripDetailResponse>> getTripDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        TripDetailResponse trip = tripService.getTripDetails(id, principal != null ? principal.getId() : null);
        return ResponseEntity.ok(ApiResponse.ok(trip));
    }

    @GetMapping("/shared/{shareCode}")
    @Operation(summary = "Access trip via public share link or QR code")
    public ResponseEntity<ApiResponse<TripDetailResponse>> getTripByShareCode(@PathVariable String shareCode) {
        TripDetailResponse trip = tripService.getTripByShareCode(shareCode);
        return ResponseEntity.ok(ApiResponse.ok(trip));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update trip information")
    public ResponseEntity<ApiResponse<TripSummaryResponse>> updateTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateTripRequest request) {
        TripSummaryResponse trip = tripService.updateTrip(id, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Trip updated successfully", trip));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a trip")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        tripService.deleteTrip(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Trip deleted successfully", null));
    }
}
