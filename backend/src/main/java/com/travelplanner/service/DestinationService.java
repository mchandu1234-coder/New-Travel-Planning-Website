package com.travelplanner.service;

import com.travelplanner.dto.DestinationDTOs.DestinationResponse;
import com.travelplanner.dto.DestinationDTOs.WeatherForecastDTO;
import com.travelplanner.entity.Destination;
import com.travelplanner.exception.ResourceNotFoundException;
import com.travelplanner.repository.DestinationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DestinationService {

    private static final Logger log = LoggerFactory.getLogger(DestinationService.class);

    private final DestinationRepository destinationRepository;
    private final WeatherService weatherService;

    public DestinationService(DestinationRepository destinationRepository, WeatherService weatherService) {
        this.destinationRepository = destinationRepository;
        this.weatherService = weatherService;
    }

    @Transactional(readOnly = true)
    public List<DestinationResponse> getFeaturedDestinations() {
        return destinationRepository.findByIsFeaturedTrue().stream()
                .map(d -> {
                    DestinationResponse resp = DestinationResponse.fromEntity(d);
                    WeatherForecastDTO weather = weatherService.getWeatherForecastForCity(d.getCity(), d.getLatitude(), d.getLongitude());
                    resp.setLiveWeather(weather);
                    return resp;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DestinationResponse> searchDestinations(String query, String continent, BigDecimal maxCost, BigDecimal minRating, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<Destination> destinations = destinationRepository.searchDestinations(
                (query != null && !query.isBlank()) ? query.trim() : null,
                (continent != null && !continent.isBlank() && !continent.equalsIgnoreCase("ALL")) ? continent.trim() : null,
                maxCost,
                minRating,
                pageRequest
        );

        return destinations.map(d -> {
            DestinationResponse resp = DestinationResponse.fromEntity(d);
            WeatherForecastDTO weather = weatherService.getWeatherForecastForCity(d.getCity(), d.getLatitude(), d.getLongitude());
            resp.setLiveWeather(weather);
            return resp;
        });
    }

    @Transactional(readOnly = true)
    public DestinationResponse getDestinationById(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id: " + id));

        DestinationResponse resp = DestinationResponse.fromEntity(destination);
        WeatherForecastDTO weather = weatherService.getWeatherForecastForCity(destination.getCity(), destination.getLatitude(), destination.getLongitude());
        resp.setLiveWeather(weather);
        return resp;
    }
}
