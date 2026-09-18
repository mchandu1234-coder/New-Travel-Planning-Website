package com.travelplanner.service;

import com.travelplanner.dto.AuthDTOs.UserSummaryDTO;
import com.travelplanner.dto.DestinationDTOs.DestinationResponse;
import com.travelplanner.dto.DestinationDTOs.WeatherForecastDTO;
import com.travelplanner.dto.ItineraryDTOs.ItineraryDayResponse;
import com.travelplanner.dto.TripDTOs.*;
import com.travelplanner.entity.*;
import com.travelplanner.exception.BadRequestException;
import com.travelplanner.exception.ResourceNotFoundException;
import com.travelplanner.exception.UnauthorizedException;
import com.travelplanner.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TripService {

    private static final Logger log = LoggerFactory.getLogger(TripService.class);

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final TripCollaboratorRepository tripCollaboratorRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final WeatherService weatherService;

    public TripService(TripRepository tripRepository, UserRepository userRepository, DestinationRepository destinationRepository, TripCollaboratorRepository tripCollaboratorRepository, ItineraryDayRepository itineraryDayRepository, WeatherService weatherService) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
        this.tripCollaboratorRepository = tripCollaboratorRepository;
        this.itineraryDayRepository = itineraryDayRepository;
        this.weatherService = weatherService;
    }

    @Transactional
    public TripSummaryResponse createTrip(Long userId, CreateTripRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date.");
        }

        int totalDays = (int) ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        if (totalDays > 60) {
            throw new BadRequestException("Trip duration cannot exceed 60 days.");
        }

        Destination destination = null;
        if (request.getDestinationId() != null) {
            destination = destinationRepository.findById(request.getDestinationId()).orElse(null);
        }

        String shareCode = UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        String coverImage = request.getCoverImageUrl();
        if (coverImage == null || coverImage.isBlank()) {
            if (destination != null && destination.getHeroImageUrl() != null) {
                coverImage = destination.getHeroImageUrl();
            } else {
                coverImage = "https://images.unsplash.com/photo-1488646953014-85cb44e25828?auto=format&fit=crop&w=1200&q=80";
            }
        }

        Trip trip = Trip.builder()
                .owner(user)
                .destination(destination)
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .coverImageUrl(coverImage)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalDays(totalDays)
                .travelerCount(request.getTravelerCount() != null ? request.getTravelerCount() : 1)
                .travelerType(request.getTravelerType() != null ? request.getTravelerType() : "SOLO")
                .targetBudget(request.getTargetBudget())
                .currency(request.getCurrency() != null ? request.getCurrency() : "USD")
                .privacy(request.getPrivacy() != null ? request.getPrivacy() : Trip.TripPrivacy.PRIVATE)
                .shareCode(shareCode)
                .status(Trip.TripStatus.PLANNING)
                .build();

        trip = tripRepository.save(trip);

        TripCollaborator ownerCollab = TripCollaborator.builder()
                .trip(trip)
                .user(user)
                .role(TripCollaborator.CollaboratorRole.OWNER)
                .inviteStatus(TripCollaborator.InviteStatus.ACCEPTED)
                .build();
        tripCollaboratorRepository.save(ownerCollab);

        for (int i = 1; i <= totalDays; i++) {
            LocalDate dayDate = request.getStartDate().plusDays(i - 1);
            ItineraryDay day = ItineraryDay.builder()
                    .trip(trip)
                    .dayNumber(i)
                    .date(dayDate)
                    .title("Day " + i + " - " + (destination != null ? destination.getCity() : "Exploration"))
                    .notes("")
                    .build();
            itineraryDayRepository.save(day);
        }

        if (request.getInviteEmails() != null) {
            for (String email : request.getInviteEmails()) {
                if (email != null && !email.isBlank() && !email.equalsIgnoreCase(user.getEmail())) {
                    User invitedUser = userRepository.findByEmail(email.trim().toLowerCase()).orElse(null);
                    TripCollaborator invite = TripCollaborator.builder()
                            .trip(trip)
                            .user(invitedUser)
                            .invitedEmail(email.trim().toLowerCase())
                            .role(TripCollaborator.CollaboratorRole.EDITOR)
                            .inviteStatus(TripCollaborator.InviteStatus.INVITED)
                            .build();
                    tripCollaboratorRepository.save(invite);
                }
            }
        }

        return TripSummaryResponse.fromEntity(trip, userId);
    }

    @Transactional(readOnly = true)
    public List<TripSummaryResponse> getUserTrips(Long userId) {
        return tripRepository.findAllAccessibleByUserId(userId).stream()
                .map(t -> TripSummaryResponse.fromEntity(t, userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripDetailResponse getTripDetails(Long tripId, Long userId) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        validateUserAccess(trip, userId);

        String userRole = getUserRole(trip, userId);

        List<CollaboratorDTO> collaborators = tripCollaboratorRepository.findByTripId(tripId).stream()
                .map(CollaboratorDTO::fromEntity)
                .collect(Collectors.toList());

        List<ItineraryDayResponse> days = itineraryDayRepository.findByTripIdOrderByDayNumberAsc(tripId).stream()
                .map(ItineraryDayResponse::fromEntity)
                .collect(Collectors.toList());

        DestinationResponse destResp = null;
        WeatherForecastDTO weather = null;
        if (trip.getDestination() != null) {
            destResp = DestinationResponse.fromEntity(trip.getDestination());
            weather = weatherService.getWeatherForecastForCity(
                    trip.getDestination().getCity(),
                    trip.getDestination().getLatitude(),
                    trip.getDestination().getLongitude()
            );
        }

        return TripDetailResponse.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .description(trip.getDescription())
                .coverImageUrl(trip.getCoverImageUrl())
                .destination(destResp)
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .totalDays(trip.getTotalDays())
                .travelerCount(trip.getTravelerCount())
                .travelerType(trip.getTravelerType())
                .targetBudget(trip.getTargetBudget())
                .actualSpend(trip.getActualSpend())
                .currency(trip.getCurrency())
                .status(trip.getStatus())
                .privacy(trip.getPrivacy())
                .shareCode(trip.getShareCode())
                .owner(UserSummaryDTO.fromEntity(trip.getOwner()))
                .userRole(userRole)
                .collaborators(collaborators)
                .days(days)
                .weather(weather)
                .build();
    }

    @Transactional(readOnly = true)
    public TripDetailResponse getTripByShareCode(String shareCode) {
        Trip trip = tripRepository.findByShareCodeAndIsDeletedFalse(shareCode)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with share code: " + shareCode));

        List<CollaboratorDTO> collaborators = tripCollaboratorRepository.findByTripId(trip.getId()).stream()
                .map(CollaboratorDTO::fromEntity)
                .collect(Collectors.toList());

        List<ItineraryDayResponse> days = itineraryDayRepository.findByTripIdOrderByDayNumberAsc(trip.getId()).stream()
                .map(ItineraryDayResponse::fromEntity)
                .collect(Collectors.toList());

        DestinationResponse destResp = (trip.getDestination() != null) ? DestinationResponse.fromEntity(trip.getDestination()) : null;
        WeatherForecastDTO weather = (trip.getDestination() != null) ?
                weatherService.getWeatherForecastForCity(trip.getDestination().getCity(), trip.getDestination().getLatitude(), trip.getDestination().getLongitude()) : null;

        return TripDetailResponse.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .description(trip.getDescription())
                .coverImageUrl(trip.getCoverImageUrl())
                .destination(destResp)
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .totalDays(trip.getTotalDays())
                .travelerCount(trip.getTravelerCount())
                .travelerType(trip.getTravelerType())
                .targetBudget(trip.getTargetBudget())
                .actualSpend(trip.getActualSpend())
                .currency(trip.getCurrency())
                .status(trip.getStatus())
                .privacy(trip.getPrivacy())
                .shareCode(trip.getShareCode())
                .owner(UserSummaryDTO.fromEntity(trip.getOwner()))
                .userRole("VIEWER")
                .collaborators(collaborators)
                .days(days)
                .weather(weather)
                .build();
    }

    @Transactional
    public TripSummaryResponse updateTrip(Long tripId, Long userId, UpdateTripRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        validateEditorAccess(trip, userId);

        if (request.getTitle() != null) trip.setTitle(request.getTitle().trim());
        if (request.getDescription() != null) trip.setDescription(request.getDescription());
        if (request.getCoverImageUrl() != null) trip.setCoverImageUrl(request.getCoverImageUrl());
        if (request.getTravelerCount() != null) trip.setTravelerCount(request.getTravelerCount());
        if (request.getTravelerType() != null) trip.setTravelerType(request.getTravelerType());
        if (request.getTargetBudget() != null) trip.setTargetBudget(request.getTargetBudget());
        if (request.getCurrency() != null) trip.setCurrency(request.getCurrency());
        if (request.getStatus() != null) trip.setStatus(request.getStatus());
        if (request.getPrivacy() != null) trip.setPrivacy(request.getPrivacy());

        trip = tripRepository.save(trip);
        return TripSummaryResponse.fromEntity(trip, userId);
    }

    @Transactional
    public void deleteTrip(Long tripId, Long userId) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (!trip.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("Only the trip owner can delete this trip.");
        }

        trip.setDeleted(true);
        tripRepository.save(trip);
    }

    public void validateUserAccess(Trip trip, Long userId) {
        if (trip.getPrivacy() == Trip.TripPrivacy.PUBLIC) {
            return;
        }
        if (userId != null) {
            if (trip.getOwner().getId().equals(userId)) return;
            boolean isCollab = tripCollaboratorRepository.findByTripIdAndUserId(trip.getId(), userId).isPresent();
            if (isCollab) return;
        }
        throw new UnauthorizedException("You do not have access to this trip.");
    }

    public void validateEditorAccess(Trip trip, Long userId) {
        if (trip.getOwner().getId().equals(userId)) return;
        TripCollaborator collab = tripCollaboratorRepository.findByTripIdAndUserId(trip.getId(), userId)
                .orElseThrow(() -> new UnauthorizedException("You are not a collaborator on this trip."));
        if (collab.getRole() == TripCollaborator.CollaboratorRole.VIEWER) {
            throw new UnauthorizedException("You have read-only access to this trip.");
        }
    }

    private String getUserRole(Trip trip, Long userId) {
        if (trip.getOwner().getId().equals(userId)) return "OWNER";
        return tripCollaboratorRepository.findByTripIdAndUserId(trip.getId(), userId)
                .map(c -> c.getRole().name())
                .orElse("VIEWER");
    }
}
