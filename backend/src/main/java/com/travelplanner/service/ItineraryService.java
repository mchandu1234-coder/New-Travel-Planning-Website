package com.travelplanner.service;

import com.travelplanner.dto.ItineraryDTOs.*;
import com.travelplanner.entity.ItineraryDay;
import com.travelplanner.entity.ItineraryItem;
import com.travelplanner.entity.Trip;
import com.travelplanner.exception.ResourceNotFoundException;
import com.travelplanner.repository.ItineraryDayRepository;
import com.travelplanner.repository.ItineraryItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItineraryService {

    private static final Logger log = LoggerFactory.getLogger(ItineraryService.class);

    private final ItineraryDayRepository itineraryDayRepository;
    private final ItineraryItemRepository itineraryItemRepository;
    private final TripService tripService;
    private final SimpMessagingTemplate messagingTemplate;

    public ItineraryService(ItineraryDayRepository itineraryDayRepository, ItineraryItemRepository itineraryItemRepository, TripService tripService, SimpMessagingTemplate messagingTemplate) {
        this.itineraryDayRepository = itineraryDayRepository;
        this.itineraryItemRepository = itineraryItemRepository;
        this.tripService = tripService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional(readOnly = true)
    public List<ItineraryDayResponse> getTripItinerary(Long tripId, Long userId) {
        List<ItineraryDay> days = itineraryDayRepository.findByTripIdOrderByDayNumberAsc(tripId);
        return days.stream().map(day -> {
            ItineraryDayResponse response = ItineraryDayResponse.fromEntity(day);
            response.setConflicts(detectConflicts(day.getItems()));
            return response;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ItineraryItemResponse addItem(Long userId, CreateItineraryItemRequest request) {
        ItineraryDay day = itineraryDayRepository.findById(request.getDayId())
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary day not found"));

        Trip trip = day.getTrip();
        tripService.validateEditorAccess(trip, userId);

        List<ItineraryItem> existingItems = itineraryItemRepository.findByDayIdOrderByDisplayOrderAscStartTimeAsc(day.getId());
        int nextOrder = existingItems.size();
        if (request.getDisplayOrder() != null) {
            nextOrder = request.getDisplayOrder();
        }

        ItineraryItem item = ItineraryItem.builder()
                .day(day)
                .itemType(request.getItemType() != null ? request.getItemType() : ItineraryItem.ItemType.ACTIVITY)
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .locationName(request.getLocationName())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .estimatedCost(request.getEstimatedCost())
                .currency(request.getCurrency() != null ? request.getCurrency() : "USD")
                .bookingReference(request.getBookingReference())
                .displayOrder(nextOrder)
                .status(ItineraryItem.ItemStatus.PLANNED)
                .build();

        item = itineraryItemRepository.save(item);

        broadcastItineraryUpdate(trip.getId(), "ITEM_ADDED", item.getId());

        return ItineraryItemResponse.fromEntity(item);
    }

    @Transactional
    public ItineraryItemResponse updateItem(Long itemId, Long userId, UpdateItineraryItemRequest request) {
        ItineraryItem item = itineraryItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary item not found"));

        Trip trip = item.getDay().getTrip();
        tripService.validateEditorAccess(trip, userId);

        if (request.getTitle() != null) item.setTitle(request.getTitle().trim());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getItemType() != null) item.setItemType(request.getItemType());
        if (request.getLocationName() != null) item.setLocationName(request.getLocationName());
        if (request.getAddress() != null) item.setAddress(request.getAddress());
        if (request.getLatitude() != null) item.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) item.setLongitude(request.getLongitude());
        if (request.getStartTime() != null) item.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) item.setEndTime(request.getEndTime());
        if (request.getEstimatedCost() != null) item.setEstimatedCost(request.getEstimatedCost());
        if (request.getCurrency() != null) item.setCurrency(request.getCurrency());
        if (request.getBookingReference() != null) item.setBookingReference(request.getBookingReference());
        if (request.getStatus() != null) item.setStatus(request.getStatus());
        if (request.getDisplayOrder() != null) item.setDisplayOrder(request.getDisplayOrder());

        item = itineraryItemRepository.save(item);

        broadcastItineraryUpdate(trip.getId(), "ITEM_UPDATED", item.getId());

        return ItineraryItemResponse.fromEntity(item);
    }

    @Transactional
    public void deleteItem(Long itemId, Long userId) {
        ItineraryItem item = itineraryItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary item not found"));

        Trip trip = item.getDay().getTrip();
        tripService.validateEditorAccess(trip, userId);

        Long tripId = trip.getId();
        itineraryItemRepository.delete(item);

        broadcastItineraryUpdate(tripId, "ITEM_DELETED", itemId);
    }

    @Transactional
    public void reorderItem(Long userId, ReorderItemRequest request) {
        ItineraryItem item = itineraryItemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary item not found"));

        ItineraryDay targetDay = itineraryDayRepository.findById(request.getTargetDayId())
                .orElseThrow(() -> new ResourceNotFoundException("Target itinerary day not found"));

        Trip trip = targetDay.getTrip();
        tripService.validateEditorAccess(trip, userId);

        item.setDay(targetDay);
        item.setDisplayOrder(request.getTargetOrder());
        itineraryItemRepository.save(item);

        List<ItineraryItem> items = itineraryItemRepository.findByDayIdOrderByDisplayOrderAscStartTimeAsc(targetDay.getId());
        int order = 0;
        for (ItineraryItem it : items) {
            it.setDisplayOrder(order++);
            itineraryItemRepository.save(it);
        }

        broadcastItineraryUpdate(trip.getId(), "ITEM_REORDERED", item.getId());
    }

    public List<ItineraryConflictDTO> detectConflicts(List<ItineraryItem> items) {
        List<ItineraryConflictDTO> conflicts = new ArrayList<>();
        if (items == null || items.size() < 2) return conflicts;

        List<ItineraryItem> timedItems = items.stream()
                .filter(i -> i.getStartTime() != null)
                .sorted(Comparator.comparing(ItineraryItem::getStartTime))
                .collect(Collectors.toList());

        for (int i = 0; i < timedItems.size() - 1; i++) {
            ItineraryItem current = timedItems.get(i);
            ItineraryItem next = timedItems.get(i + 1);

            LocalTime currentEnd = current.getEndTime() != null ? current.getEndTime() : current.getStartTime().plusHours(1);
            LocalTime nextStart = next.getStartTime();

            if (currentEnd.isAfter(nextStart)) {
                conflicts.add(ItineraryConflictDTO.builder()
                        .type("TIME_OVERLAP")
                        .itemAId(current.getId())
                        .itemATitle(current.getTitle())
                        .itemBId(next.getId())
                        .itemBTitle(next.getTitle())
                        .message("Schedule overlap between '" + current.getTitle() + "' and '" + next.getTitle() + "'")
                        .build());
            } else if (currentEnd.plusMinutes(15).isAfter(nextStart)) {
                conflicts.add(ItineraryConflictDTO.builder()
                        .type("TIGHT_BUFFER")
                        .itemAId(current.getId())
                        .itemATitle(current.getTitle())
                        .itemBId(next.getId())
                        .itemBTitle(next.getTitle())
                        .message("Tight transit window (< 15 mins) between '" + current.getTitle() + "' and '" + next.getTitle() + "'")
                        .build());
            }
        }

        return conflicts;
    }

    private void broadcastItineraryUpdate(Long tripId, String action, Long itemId) {
        try {
            messagingTemplate.convertAndSend("/topic/trips/" + tripId + "/itinerary", 
                    java.util.Map.of("action", action, "itemId", itemId, "timestamp", System.currentTimeMillis()));
        } catch (Exception ex) {
            log.warn("Failed to broadcast WebSocket itinerary update: {}", ex.getMessage());
        }
    }
}
