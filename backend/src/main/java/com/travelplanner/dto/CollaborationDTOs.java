package com.travelplanner.dto;

import com.travelplanner.entity.TripCollaborator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CollaborationDTOs {

    public static class InviteCollaboratorRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;
        private TripCollaborator.CollaboratorRole role = TripCollaborator.CollaboratorRole.EDITOR;

        public InviteCollaboratorRequest() {}
        public InviteCollaboratorRequest(String email, TripCollaborator.CollaboratorRole role) {
            this.email = email;
            this.role = role != null ? role : TripCollaborator.CollaboratorRole.EDITOR;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public TripCollaborator.CollaboratorRole getRole() { return role; }
        public void setRole(TripCollaborator.CollaboratorRole role) { this.role = role; }
    }

    public static class UpdateRoleRequest {
        private TripCollaborator.CollaboratorRole role;

        public UpdateRoleRequest() {}
        public UpdateRoleRequest(TripCollaborator.CollaboratorRole role) { this.role = role; }
        public TripCollaborator.CollaboratorRole getRole() { return role; }
        public void setRole(TripCollaborator.CollaboratorRole role) { this.role = role; }
    }

    public static class ChatMessageDTO {
        private String id;
        private Long tripId;
        private Long senderId;
        private String senderName;
        private String senderAvatar;
        private String message;
        private String timestamp;
        private String type; // CHAT, ACTIVITY, SYSTEM

        public ChatMessageDTO() {}
        public ChatMessageDTO(String id, Long tripId, Long senderId, String senderName, String senderAvatar, String message, String timestamp, String type) {
            this.id = id;
            this.tripId = tripId;
            this.senderId = senderId;
            this.senderName = senderName;
            this.senderAvatar = senderAvatar;
            this.message = message;
            this.timestamp = timestamp;
            this.type = type;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public Long getTripId() { return tripId; }
        public void setTripId(Long tripId) { this.tripId = tripId; }
        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }
        public String getSenderName() { return senderName; }
        public void setSenderName(String senderName) { this.senderName = senderName; }
        public String getSenderAvatar() { return senderAvatar; }
        public void setSenderAvatar(String senderAvatar) { this.senderAvatar = senderAvatar; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    public static class PresenceDTO {
        private Long tripId;
        private Long userId;
        private String userName;
        private String userAvatar;
        private String currentAction; // VIEWING, EDITING_DAY_1, etc.
        private String status; // ONLINE, IDLE, LEFT

        public PresenceDTO() {}
        public PresenceDTO(Long tripId, Long userId, String userName, String userAvatar, String currentAction, String status) {
            this.tripId = tripId;
            this.userId = userId;
            this.userName = userName;
            this.userAvatar = userAvatar;
            this.currentAction = currentAction;
            this.status = status;
        }

        public Long getTripId() { return tripId; }
        public void setTripId(Long tripId) { this.tripId = tripId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getUserAvatar() { return userAvatar; }
        public void setUserAvatar(String userAvatar) { this.userAvatar = userAvatar; }
        public String getCurrentAction() { return currentAction; }
        public void setCurrentAction(String currentAction) { this.currentAction = currentAction; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
