package com.travelplanner.repository;

import com.travelplanner.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(Review.EntityType entityType, Long entityId);
}
