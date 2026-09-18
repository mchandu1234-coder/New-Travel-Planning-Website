package com.travelplanner.repository;

import com.travelplanner.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByTripIdOrderByDepartureTimeAsc(Long tripId);
}
