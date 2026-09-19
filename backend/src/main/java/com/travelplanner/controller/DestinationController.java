package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.DestinationDTOs.DestinationResponse;
import com.travelplanner.service.DestinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/destinations")
@Tag(name = "Destinations", description = "Endpoints for discovering destinations, search, filters, and live weather")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured destinations with live weather & sights")
    public ResponseEntity<ApiResponse<List<DestinationResponse>>> getFeatured() {
        List<DestinationResponse> destinations = destinationService.getFeaturedDestinations();
        return ResponseEntity.ok(ApiResponse.ok(destinations));
    }

    @GetMapping
    @Operation(summary = "Search and filter destinations")
    public ResponseEntity<ApiResponse<Page<DestinationResponse>>> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String continent,
            @RequestParam(required = false) BigDecimal maxCost,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        Page<DestinationResponse> destinations = destinationService.searchDestinations(query, continent, maxCost, minRating, page, size);
        return ResponseEntity.ok(ApiResponse.ok(destinations));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get detailed destination info by ID")
    public ResponseEntity<ApiResponse<DestinationResponse>> getById(@PathVariable Long id) {
        DestinationResponse destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(ApiResponse.ok(destination));
    }
}
