package com.travelplanner.repository;

import com.travelplanner.entity.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    List<Destination> findByIsFeaturedTrue();

    @Query("SELECT d FROM Destination d WHERE " +
           "(:query IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           " OR LOWER(d.city) LIKE LOWER(CONCAT('%', :query, '%')) " +
           " OR LOWER(d.country) LIKE LOWER(CONCAT('%', :query, '%')) " +
           " OR LOWER(d.vibeTags) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:continent IS NULL OR LOWER(d.continent) = LOWER(:continent)) " +
           "AND (:maxCost IS NULL OR d.averageDailyCost <= :maxCost) " +
           "AND (:minRating IS NULL OR d.rating >= :minRating)")
    Page<Destination> searchDestinations(
            @Param("query") String query,
            @Param("continent") String continent,
            @Param("maxCost") BigDecimal maxCost,
            @Param("minRating") BigDecimal minRating,
            Pageable pageable);
}
