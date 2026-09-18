package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.ExportDTOs.TripExportDataDTO;
import com.travelplanner.security.UserPrincipal;
import com.travelplanner.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/export")
@Tag(name = "Export & Share", description = "Endpoints for exporting itineraries to PDF dataset and standard iCalendar (.ics) files")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/trips/{tripId}/data")
    @Operation(summary = "Get full trip export payload for client-side formatted PDF generation")
    public ResponseEntity<ApiResponse<TripExportDataDTO>> getExportData(
            @PathVariable Long tripId,
            @AuthenticationPrincipal UserPrincipal principal) {
        TripExportDataDTO data = exportService.getTripExportData(tripId, principal != null ? principal.getId() : null);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @GetMapping("/public/trips/{tripId}/data")
    @Operation(summary = "Get public trip export payload")
    public ResponseEntity<ApiResponse<TripExportDataDTO>> getPublicExportData(@PathVariable Long tripId) {
        TripExportDataDTO data = exportService.getTripExportData(tripId, null);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @GetMapping("/trips/{tripId}/calendar.ics")
    @Operation(summary = "Download standard iCalendar (.ics) file importable into Google / Apple / Outlook Calendar")
    public ResponseEntity<String> downloadCalendarIcs(
            @PathVariable Long tripId,
            @AuthenticationPrincipal UserPrincipal principal) {
        String icsContent = exportService.generateIcsCalendar(tripId, principal != null ? principal.getId() : null);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"trip-" + tripId + "-itinerary.ics\"")
                .contentType(MediaType.parseMediaType("text/calendar; charset=UTF-8"))
                .body(icsContent);
    }
}
