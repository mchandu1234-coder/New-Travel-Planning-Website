package com.travelplanner.repository;

import com.travelplanner.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByTripIdOrderByCheckInDateAsc(Long tripId);
}
