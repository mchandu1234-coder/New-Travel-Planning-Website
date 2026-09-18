package com.travelplanner.repository;

import com.travelplanner.entity.TripCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripCollaboratorRepository extends JpaRepository<TripCollaborator, Long> {
    List<TripCollaborator> findByTripId(Long tripId);
    Optional<TripCollaborator> findByTripIdAndUserId(Long tripId, Long userId);
    Optional<TripCollaborator> findByTripIdAndInvitedEmail(Long tripId, String invitedEmail);
    void deleteByTripIdAndUserId(Long tripId, Long userId);
}
