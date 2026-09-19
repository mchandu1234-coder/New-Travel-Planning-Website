package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.entity.Activity;
import com.travelplanner.entity.Restaurant;
import com.travelplanner.entity.Review;
import com.travelplanner.security.UserPrincipal;
import com.travelplanner.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalog")
@Tag(name = "Catalog & Discovery", description = "Endpoints for activities, restaurants, and user reviews")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping({"/activities", "/destinations/{destId}/activities"})
    @Operation(summary = "Get all activities and sights for a destination")
    public ResponseEntity<ApiResponse<List<Activity>>> getActivities(
            @PathVariable(required = false) Long destId,
            @RequestParam(required = false) Long destinationId,
            @RequestParam(required = false) String category) {
        Long targetId = destId != null ? destId : destinationId;
        List<Activity> activities = catalogService.getActivitiesByDestination(targetId, category);
        return ResponseEntity.ok(ApiResponse.ok(activities));
    }

    @GetMapping({"/restaurants", "/destinations/{destId}/restaurants"})
    @Operation(summary = "Get recommended restaurants for a destination")
    public ResponseEntity<ApiResponse<List<Restaurant>>> getRestaurants(
            @PathVariable(required = false) Long destId,
            @RequestParam(required = false) Long destinationId,
            @RequestParam(required = false) String cuisine) {
        Long targetId = destId != null ? destId : destinationId;
        List<Restaurant> restaurants = catalogService.getRestaurantsByDestination(targetId, cuisine);
        return ResponseEntity.ok(ApiResponse.ok(restaurants));
    }

    @GetMapping("/reviews/{type}/{id}")
    @Operation(summary = "Get reviews for a destination, activity, or restaurant")
    public ResponseEntity<ApiResponse<List<Review>>> getReviews(
            @PathVariable Review.EntityType type,
            @PathVariable Long id) {
        List<Review> reviews = catalogService.getReviews(type, id);
        return ResponseEntity.ok(ApiResponse.ok(reviews));
    }

    @PostMapping("/reviews")
    @Operation(summary = "Post a review with rating and comments")
    public ResponseEntity<ApiResponse<Review>> addReview(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody Map<String, Object> body) {
        Review.EntityType type = Review.EntityType.valueOf((String) body.get("entityType"));
        Long entityId = Long.valueOf(String.valueOf(body.get("entityId")));
        int rating = Integer.parseInt(String.valueOf(body.get("rating")));
        String comment = (String) body.get("comment");

        Review review = catalogService.addReview(principal.getId(), type, entityId, rating, comment);
        return ResponseEntity.ok(ApiResponse.ok("Review posted", review));
    }
}
