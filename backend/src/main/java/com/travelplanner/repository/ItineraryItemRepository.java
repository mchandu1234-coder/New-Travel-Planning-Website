package com.travelplanner.repository;

import com.travelplanner.entity.ItineraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryItemRepository extends JpaRepository<ItineraryItem, Long> {
    List<ItineraryItem> findByDayIdOrderByDisplayOrderAscStartTimeAsc(Long dayId);

    @Query("SELECT i FROM ItineraryItem i WHERE i.day.trip.id = :tripId ORDER BY i.day.dayNumber ASC, i.displayOrder ASC")
    List<ItineraryItem> findAllByTripId(@Param("tripId") Long tripId);
}
