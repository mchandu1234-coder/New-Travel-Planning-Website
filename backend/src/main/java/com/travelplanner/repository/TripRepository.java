package com.travelplanner.repository;

import com.travelplanner.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findByIdAndIsDeletedFalse(Long id);

    Optional<Trip> findByShareCodeAndIsDeletedFalse(String shareCode);

    @Query("SELECT DISTINCT t FROM Trip t LEFT JOIN t.collaborators c " +
           "WHERE (t.owner.id = :userId OR c.user.id = :userId) " +
           "AND t.isDeleted = false ORDER BY t.startDate DESC")
    List<Trip> findAllAccessibleByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Trip t WHERE t.privacy = 'PUBLIC' AND t.isDeleted = false ORDER BY t.createdAt DESC")
    Page<Trip> findPublicTrips(Pageable pageable);
}
