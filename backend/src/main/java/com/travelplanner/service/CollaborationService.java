package com.travelplanner.service;

import com.travelplanner.dto.CollaborationDTOs.*;
import com.travelplanner.dto.TripDTOs.CollaboratorDTO;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.TripCollaborator;
import com.travelplanner.entity.User;
import com.travelplanner.exception.BadRequestException;
import com.travelplanner.exception.ResourceNotFoundException;
import com.travelplanner.exception.UnauthorizedException;
import com.travelplanner.repository.TripCollaboratorRepository;
import com.travelplanner.repository.TripRepository;
import com.travelplanner.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollaborationService {

    private static final Logger log = LoggerFactory.getLogger(CollaborationService.class);

    private final TripRepository tripRepository;
    private final TripCollaboratorRepository tripCollaboratorRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public CollaborationService(TripRepository tripRepository, TripCollaboratorRepository tripCollaboratorRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.tripRepository = tripRepository;
        this.tripCollaboratorRepository = tripCollaboratorRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional(readOnly = true)
    public List<CollaboratorDTO> getTripCollaborators(Long tripId) {
        return tripCollaboratorRepository.findByTripId(tripId).stream()
                .map(CollaboratorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CollaboratorDTO inviteCollaborator(Long tripId, Long invitingUserId, InviteCollaboratorRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (!trip.getOwner().getId().equals(invitingUserId)) {
            throw new UnauthorizedException("Only trip owners can invite collaborators.");
        }

        String email = request.getEmail().toLowerCase().trim();
        User invitedUser = userRepository.findByEmail(email).orElse(null);

        if (invitedUser != null) {
            if (tripCollaboratorRepository.findByTripIdAndUserId(tripId, invitedUser.getId()).isPresent()) {
                throw new BadRequestException("User is already a collaborator on this trip.");
            }
        }

        TripCollaborator collab = TripCollaborator.builder()
                .trip(trip)
                .user(invitedUser)
                .invitedEmail(email)
                .role(request.getRole() != null ? request.getRole() : TripCollaborator.CollaboratorRole.EDITOR)
                .inviteStatus(invitedUser != null ? TripCollaborator.InviteStatus.ACCEPTED : TripCollaborator.InviteStatus.INVITED)
                .build();

        collab = tripCollaboratorRepository.save(collab);

        broadcastCollaboratorEvent(tripId, "COLLABORATOR_JOINED", CollaboratorDTO.fromEntity(collab));

        return CollaboratorDTO.fromEntity(collab);
    }

    @Transactional
    public void updateRole(Long tripId, Long collaboratorId, Long requestingUserId, UpdateRoleRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (!trip.getOwner().getId().equals(requestingUserId)) {
            throw new UnauthorizedException("Only the trip owner can change collaborator roles.");
        }

        TripCollaborator collab = tripCollaboratorRepository.findById(collaboratorId)
                .orElseThrow(() -> new ResourceNotFoundException("Collaborator not found"));

        if (collab.getRole() == TripCollaborator.CollaboratorRole.OWNER) {
            throw new BadRequestException("Cannot change the owner's role.");
        }

        collab.setRole(request.getRole());
        tripCollaboratorRepository.save(collab);

        broadcastCollaboratorEvent(tripId, "ROLE_UPDATED", CollaboratorDTO.fromEntity(collab));
    }

    @Transactional
    public void removeCollaborator(Long tripId, Long collaboratorId, Long requestingUserId) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        TripCollaborator collab = tripCollaboratorRepository.findById(collaboratorId)
                .orElseThrow(() -> new ResourceNotFoundException("Collaborator not found"));

        if (collab.getRole() == TripCollaborator.CollaboratorRole.OWNER) {
            throw new BadRequestException("Cannot remove the trip owner.");
        }

        boolean isSelf = (collab.getUser() != null && collab.getUser().getId().equals(requestingUserId));
        boolean isOwner = trip.getOwner().getId().equals(requestingUserId);

        if (!isSelf && !isOwner) {
            throw new UnauthorizedException("You do not have permission to remove this collaborator.");
        }

        tripCollaboratorRepository.delete(collab);
        broadcastCollaboratorEvent(tripId, "COLLABORATOR_REMOVED", collaboratorId);
    }

    public void broadcastCollaboratorEvent(Long tripId, String action, Object payload) {
        try {
            messagingTemplate.convertAndSend("/topic/trips/" + tripId + "/collaboration",
                    java.util.Map.of("action", action, "data", payload, "timestamp", System.currentTimeMillis()));
        } catch (Exception e) {
            log.warn("Failed to broadcast collaboration event: {}", e.getMessage());
        }
    }
}
