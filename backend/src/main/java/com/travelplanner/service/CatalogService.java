package com.travelplanner.service;

import com.travelplanner.entity.Activity;
import com.travelplanner.entity.Restaurant;
import com.travelplanner.entity.Review;
import com.travelplanner.entity.User;
import com.travelplanner.exception.ResourceNotFoundException;
import com.travelplanner.repository.ActivityRepository;
import com.travelplanner.repository.RestaurantRepository;
import com.travelplanner.repository.ReviewRepository;
import com.travelplanner.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogService {

    private static final Logger log = LoggerFactory.getLogger(CatalogService.class);

    private final ActivityRepository activityRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public CatalogService(ActivityRepository activityRepository, RestaurantRepository restaurantRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public List<Activity> getActivitiesByDestination(Long destinationId, String category) {
        if (destinationId == null) {
            return activityRepository.findAll();
        }
        if (category != null && !category.isBlank()) {
            return activityRepository.findByDestinationIdAndCategoryIgnoreCase(destinationId, category);
        }
        return activityRepository.findByDestinationId(destinationId);
    }

    public List<Activity> getActivitiesByDestination(Long destinationId) {
        return getActivitiesByDestination(destinationId, null);
    }

    public List<Restaurant> getRestaurantsByDestination(Long destinationId, String cuisine) {
        if (destinationId == null) {
            return restaurantRepository.findAll();
        }
        if (cuisine != null && !cuisine.isBlank()) {
            return restaurantRepository.findByDestinationIdAndCuisineTypeContainingIgnoreCase(destinationId, cuisine);
        }
        return restaurantRepository.findByDestinationId(destinationId);
    }

    public List<Restaurant> getRestaurantsByDestination(Long destinationId) {
        return getRestaurantsByDestination(destinationId, null);
    }

    public List<Review> getReviews(Review.EntityType entityType, Long entityId) {
        return reviewRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId);
    }

    @Transactional
    public Review addReview(Long userId, Review.EntityType entityType, Long entityId, int rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = Review.builder()
                .user(user)
                .entityType(entityType)
                .entityId(entityId)
                .rating(Math.max(1, Math.min(5, rating)))
                .comment(comment.trim())
                .build();

        return reviewRepository.save(review);
    }
}
