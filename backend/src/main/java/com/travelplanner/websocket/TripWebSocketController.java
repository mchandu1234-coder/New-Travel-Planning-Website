package com.travelplanner.websocket;

import com.travelplanner.dto.CollaborationDTOs.ChatMessageDTO;
import com.travelplanner.dto.CollaborationDTOs.PresenceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
public class TripWebSocketController {

    private static final Logger log = LoggerFactory.getLogger(TripWebSocketController.class);

    private final SimpMessagingTemplate messagingTemplate;

    public TripWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/trips/{tripId}/chat")
    @SendTo("/topic/trips/{tripId}/chat")
    public ChatMessageDTO handleChatMessage(
            @DestinationVariable Long tripId,
            @Payload ChatMessageDTO message) {
        log.info("Received chat message on trip {}: {}", tripId, message.getMessage());
        if (message.getId() == null) {
            message.setId(UUID.randomUUID().toString());
        }
        if (message.getTimestamp() == null) {
            message.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }
        message.setTripId(tripId);
        return message;
    }

    @MessageMapping("/trips/{tripId}/presence")
    @SendTo("/topic/trips/{tripId}/presence")
    public PresenceDTO handlePresenceUpdate(
            @DestinationVariable Long tripId,
            @Payload PresenceDTO presence) {
        log.info("User {} presence on trip {}: {}", presence.getUserName(), tripId, presence.getCurrentAction());
        presence.setTripId(tripId);
        return presence;
    }
}
