package com.travelplanner.controller;

import com.travelplanner.dto.ApiResponse;
import com.travelplanner.dto.CollaborationDTOs.*;
import com.travelplanner.dto.TripDTOs.CollaboratorDTO;
import com.travelplanner.security.UserPrincipal;
import com.travelplanner.service.CollaborationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collaboration")
@Tag(name = "Collaboration", description = "Endpoints for managing trip members, invites, and permissions")
public class CollaborationController {

    private final CollaborationService collaborationService;

    public CollaborationController(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }

    @GetMapping("/trips/{tripId}/collaborators")
    @Operation(summary = "Get all collaborators for a trip")
    public ResponseEntity<ApiResponse<List<CollaboratorDTO>>> getCollaborators(@PathVariable Long tripId) {
        List<CollaboratorDTO> collaborators = collaborationService.getTripCollaborators(tripId);
        return ResponseEntity.ok(ApiResponse.ok(collaborators));
    }

    @PostMapping("/trips/{tripId}/invite")
    @Operation(summary = "Invite a collaborator to a trip via email")
    public ResponseEntity<ApiResponse<CollaboratorDTO>> inviteCollaborator(
            @PathVariable Long tripId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody InviteCollaboratorRequest request) {
        CollaboratorDTO collaborator = collaborationService.inviteCollaborator(tripId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Collaborator invited", collaborator));
    }

    @PutMapping("/trips/{tripId}/collaborators/{collaboratorId}/role")
    @Operation(summary = "Update collaborator permissions (OWNER, EDITOR, VIEWER)")
    public ResponseEntity<ApiResponse<Void>> updateRole(
            @PathVariable Long tripId,
            @PathVariable Long collaboratorId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateRoleRequest request) {
        collaborationService.updateRole(tripId, collaboratorId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Role updated", null));
    }

    @DeleteMapping("/trips/{tripId}/collaborators/{collaboratorId}")
    @Operation(summary = "Remove a collaborator or leave a trip")
    public ResponseEntity<ApiResponse<Void>> removeCollaborator(
            @PathVariable Long tripId,
            @PathVariable Long collaboratorId,
            @AuthenticationPrincipal UserPrincipal principal) {
        collaborationService.removeCollaborator(tripId, collaboratorId, principal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Collaborator removed", null));
    }
}
