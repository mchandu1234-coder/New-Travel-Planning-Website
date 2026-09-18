package com.travelplanner.service;

import com.travelplanner.dto.TripDTOs.*;
import com.travelplanner.entity.Role;
import com.travelplanner.entity.Trip;
import com.travelplanner.entity.User;
import com.travelplanner.exception.BadRequestException;
import com.travelplanner.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DestinationRepository destinationRepository;

    @Mock
    private TripCollaboratorRepository tripCollaboratorRepository;

    @Mock
    private ItineraryDayRepository itineraryDayRepository;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private TripService tripService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .email("alex@example.com")
                .fullName("Alex Morgan")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void createTrip_Success() {
        CreateTripRequest request = CreateTripRequest.builder()
                .title("Trip to Japan")
                .startDate(LocalDate.of(2026, 11, 1))
                .endDate(LocalDate.of(2026, 11, 7))
                .targetBudget(BigDecimal.valueOf(2500))
                .currency("USD")
                .travelerCount(2)
                .build();

        Trip savedTrip = Trip.builder()
                .id(10L)
                .owner(sampleUser)
                .title("Trip to Japan")
                .startDate(LocalDate.of(2026, 11, 1))
                .endDate(LocalDate.of(2026, 11, 7))
                .totalDays(7)
                .targetBudget(BigDecimal.valueOf(2500))
                .currency("USD")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(tripRepository.save(any(Trip.class))).thenReturn(savedTrip);

        TripSummaryResponse response = tripService.createTrip(1L, request);

        assertNotNull(response);
        assertEquals("Trip to Japan", response.getTitle());
        assertEquals(7, response.getTotalDays());
        verify(itineraryDayRepository, times(7)).save(any());
    }

    @Test
    void createTrip_InvalidDates_ThrowsBadRequestException() {
        CreateTripRequest request = CreateTripRequest.builder()
                .title("Invalid Date Trip")
                .startDate(LocalDate.of(2026, 11, 10))
                .endDate(LocalDate.of(2026, 11, 5)) // End before start
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        assertThrows(BadRequestException.class, () -> tripService.createTrip(1L, request));
        verify(tripRepository, never()).save(any(Trip.class));
    }
}
