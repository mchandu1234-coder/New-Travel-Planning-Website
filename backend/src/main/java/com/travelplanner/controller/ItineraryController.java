package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.ItineraryDTOs.*;
import com.travelplanner.security.UserPrincipal;
import com.travelplanner.service.ItineraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itinerary")
@Tag(name = "Itinerary", description = "Endpoints for day-by-day planner, drag-and-drop reordering, and conflict detection")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping("/trips/{tripId}")
    @Operation(summary = "Get full itinerary for a trip with detected schedule conflicts")
    public ResponseEntity<ApiResponse<List<ItineraryDayResponse>>> getTripItinerary(
            @PathVariable Long tripId,
            @AuthenticationPrincipal UserPrincipal principal) {
        List<ItineraryDayResponse> days = itineraryService.getTripItinerary(tripId, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok(days));
    }

    @PostMapping("/items")
    @Operation(summary = "Add an item (activity, meal, flight, stay) to an itinerary day")
    public ResponseEntity<ApiResponse<ItineraryItemResponse>> addItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateItineraryItemRequest request) {
        ItineraryItemResponse item = itineraryService.addItem(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Itinerary item added", item));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update an itinerary item")
    public ResponseEntity<ApiResponse<ItineraryItemResponse>> updateItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateItineraryItemRequest request) {
        ItineraryItemResponse item = itineraryService.updateItem(itemId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Itinerary item updated", item));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Delete an itinerary item")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal UserPrincipal principal) {
        itineraryService.deleteItem(itemId, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Itinerary item deleted", null));
    }

    @PostMapping("/items/reorder")
    @Operation(summary = "Reorder/move an item between days or within a day (Drag-and-Drop)")
    public ResponseEntity<ApiResponse<Void>> reorderItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ReorderItemRequest request) {
        itineraryService.reorderItem(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Itinerary item reordered", null));
    }
}
