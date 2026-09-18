package com.travelplanner.dto;

import com.travelplanner.entity.Trip;
import com.travelplanner.entity.TripCollaborator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TripDTOs {

    public static class CreateTripRequest {
        @NotBlank(message = "Trip title is required")
        private String title;

        private Long destinationId;
        private String destinationName;
        private String description;
        private String coverImageUrl;

        @NotNull(message = "Start date is required")
        private LocalDate startDate;

        @NotNull(message = "End date is required")
        private LocalDate endDate;

        private Integer travelerCount = 1;
        private String travelerType = "SOLO";
        private BigDecimal targetBudget = BigDecimal.valueOf(2000.00);
        private String currency = "USD";
        private Trip.TripPrivacy privacy = Trip.TripPrivacy.PRIVATE;
        private List<String> inviteEmails;

        public CreateTripRequest() {}
        public CreateTripRequest(String title, Long destinationId, String destinationName, String description, String coverImageUrl, LocalDate startDate, LocalDate endDate, Integer travelerCount, String travelerType, BigDecimal targetBudget, String currency, Trip.TripPrivacy privacy, List<String> inviteEmails) {
            this.title = title;
            this.destinationId = destinationId;
            this.destinationName = destinationName;
            this.description = description;
            this.coverImageUrl = coverImageUrl;
            this.startDate = startDate;
            this.endDate = endDate;
            this.travelerCount = travelerCount != null ? travelerCount : 1;
            this.travelerType = travelerType != null ? travelerType : "SOLO";
            this.targetBudget = targetBudget != null ? targetBudget : BigDecimal.valueOf(2000.00);
            this.currency = currency != null ? currency : "USD";
            this.privacy = privacy != null ? privacy : Trip.TripPrivacy.PRIVATE;
            this.inviteEmails = inviteEmails;
        }

        public static CreateTripRequestBuilder builder() { return new CreateTripRequestBuilder(); }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Long getDestinationId() { return destinationId; }
        public void setDestinationId(Long destinationId) { this.destinationId = destinationId; }
        public String getDestinationName() { return destinationName; }
        public void setDestinationName(String destinationName) { this.destinationName = destinationName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCoverImageUrl() { return coverImageUrl; }
        public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public Integer getTravelerCount() { return travelerCount; }
        public void setTravelerCount(Integer travelerCount) { this.travelerCount = travelerCount; }
        public String getTravelerType() { return travelerType; }
        public void setTravelerType(String travelerType) { this.travelerType = travelerType; }
        public BigDecimal getTargetBudget() { return targetBudget; }
        public void setTargetBudget(BigDecimal targetBudget) { this.targetBudget = targetBudget; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public Trip.TripPrivacy getPrivacy() { return privacy; }
        public void setPrivacy(Trip.TripPrivacy privacy) { this.privacy = privacy; }
        public List<String> getInviteEmails() { return inviteEmails; }
        public void setInviteEmails(List<String> inviteEmails) { this.inviteEmails = inviteEmails; }

        public static class CreateTripRequestBuilder {
            private String title;
            private Long destinationId;
            private String destinationName;
            private String description;
            private String coverImageUrl;
            private LocalDate startDate;
            private LocalDate endDate;
            private Integer travelerCount = 1;
            private String travelerType = "SOLO";
            private BigDecimal targetBudget = BigDecimal.valueOf(2000.00);
            private String currency = "USD";
            private Trip.TripPrivacy privacy = Trip.TripPrivacy.PRIVATE;
            private List<String> inviteEmails;

            public CreateTripRequestBuilder title(String title) { this.title = title; return this; }
            public CreateTripRequestBuilder destinationId(Long destinationId) { this.destinationId = destinationId; return this; }
            public CreateTripRequestBuilder destinationName(String destinationName) { this.destinationName = destinationName; return this; }
            public CreateTripRequestBuilder description(String description) { this.description = description; return this; }
            public CreateTripRequestBuilder coverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; return this; }
            public CreateTripRequestBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
            public CreateTripRequestBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
            public CreateTripRequestBuilder travelerCount(Integer travelerCount) { this.travelerCount = travelerCount; return this; }
            public CreateTripRequestBuilder travelerType(String travelerType) { this.travelerType = travelerType; return this; }
            public CreateTripRequestBuilder targetBudget(BigDecimal targetBudget) { this.targetBudget = targetBudget; return this; }
            public CreateTripRequestBuilder currency(String currency) { this.currency = currency; return this; }
            public CreateTripRequestBuilder privacy(Trip.TripPrivacy privacy) { this.privacy = privacy; return this; }
            public CreateTripRequestBuilder inviteEmails(List<String> inviteEmails) { this.inviteEmails = inviteEmails; return this; }

            public CreateTripRequest build() {
                return new CreateTripRequest(title, destinationId, destinationName, description, coverImageUrl, startDate, endDate, travelerCount, travelerType, targetBudget, currency, privacy, inviteEmails);
            }
        }
    }

    public static class UpdateTripRequest {
        private String title;
        private String description;
        private String coverImageUrl;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer travelerCount;
        private String travelerType;
        private BigDecimal targetBudget;
        private String currency;
        private Trip.TripStatus status;
        private Trip.TripPrivacy privacy;

        public UpdateTripRequest() {}

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCoverImageUrl() { return coverImageUrl; }
        public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public Integer getTravelerCount() { return travelerCount; }
        public void setTravelerCount(Integer travelerCount) { this.travelerCount = travelerCount; }
        public String getTravelerType() { return travelerType; }
        public void setTravelerType(String travelerType) { this.travelerType = travelerType; }
        public BigDecimal getTargetBudget() { return targetBudget; }
        public void setTargetBudget(BigDecimal targetBudget) { this.targetBudget = targetBudget; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public Trip.TripStatus getStatus() { return status; }
        public void setStatus(Trip.TripStatus status) { this.status = status; }
        public Trip.TripPrivacy getPrivacy() { return privacy; }
        public void setPrivacy(Trip.TripPrivacy privacy) { this.privacy = privacy; }
    }

    public static class TripSummaryResponse {
        private Long id;
        private String title;
        private String description;
        private String coverImageUrl;
        private Long destinationId;
        private String destinationName;
        private String destinationCity;
        private String destinationCountry;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer totalDays;
        private Integer travelerCount;
        private String travelerType;
        private BigDecimal targetBudget;
        private BigDecimal actualSpend;
        private String currency;
        private Trip.TripStatus status;
        private Trip.TripPrivacy privacy;
        private String shareCode;
        private AuthDTOs.UserSummaryDTO owner;
        private Integer collaboratorCount;
        private String userRole;

        public TripSummaryResponse() {}

        public static TripSummaryResponse fromEntity(Trip trip, Long currentUserId) {
            if (trip == null) return null;
            String role = "VIEWER";
            if (trip.getOwner() != null && trip.getOwner().getId().equals(currentUserId)) {
                role = "OWNER";
            } else if (trip.getCollaborators() != null) {
                for (TripCollaborator c : trip.getCollaborators()) {
                    if (c.getUser() != null && c.getUser().getId().equals(currentUserId)) {
                        role = c.getRole().name();
                        break;
                    }
                }
            }

            TripSummaryResponse resp = new TripSummaryResponse();
            resp.id = trip.getId();
            resp.title = trip.getTitle();
            resp.description = trip.getDescription();
            resp.coverImageUrl = trip.getCoverImageUrl();
            resp.destinationId = trip.getDestination() != null ? trip.getDestination().getId() : null;
            resp.destinationName = trip.getDestination() != null ? trip.getDestination().getName() : null;
            resp.destinationCity = trip.getDestination() != null ? trip.getDestination().getCity() : null;
            resp.destinationCountry = trip.getDestination() != null ? trip.getDestination().getCountry() : null;
            resp.startDate = trip.getStartDate();
            resp.endDate = trip.getEndDate();
            resp.totalDays = trip.getTotalDays();
            resp.travelerCount = trip.getTravelerCount();
            resp.travelerType = trip.getTravelerType();
            resp.targetBudget = trip.getTargetBudget();
            resp.actualSpend = trip.getActualSpend();
            resp.currency = trip.getCurrency();
            resp.status = trip.getStatus();
            resp.privacy = trip.getPrivacy();
            resp.shareCode = trip.getShareCode();
            resp.owner = AuthDTOs.UserSummaryDTO.fromEntity(trip.getOwner());
            resp.collaboratorCount = trip.getCollaborators() != null ? trip.getCollaborators().size() : 0;
            resp.userRole = role;
            return resp;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCoverImageUrl() { return coverImageUrl; }
        public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
        public Long getDestinationId() { return destinationId; }
        public void setDestinationId(Long destinationId) { this.destinationId = destinationId; }
        public String getDestinationName() { return destinationName; }
        public void setDestinationName(String destinationName) { this.destinationName = destinationName; }
        public String getDestinationCity() { return destinationCity; }
        public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }
        public String getDestinationCountry() { return destinationCountry; }
        public void setDestinationCountry(String destinationCountry) { this.destinationCountry = destinationCountry; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public Integer getTotalDays() { return totalDays; }
        public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; }
        public Integer getTravelerCount() { return travelerCount; }
        public void setTravelerCount(Integer travelerCount) { this.travelerCount = travelerCount; }
        public String getTravelerType() { return travelerType; }
        public void setTravelerType(String travelerType) { this.travelerType = travelerType; }
        public BigDecimal getTargetBudget() { return targetBudget; }
        public void setTargetBudget(BigDecimal targetBudget) { this.targetBudget = targetBudget; }
        public BigDecimal getActualSpend() { return actualSpend; }
        public void setActualSpend(BigDecimal actualSpend) { this.actualSpend = actualSpend; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public Trip.TripStatus getStatus() { return status; }
        public void setStatus(Trip.TripStatus status) { this.status = status; }
        public Trip.TripPrivacy getPrivacy() { return privacy; }
        public void setPrivacy(Trip.TripPrivacy privacy) { this.privacy = privacy; }
        public String getShareCode() { return shareCode; }
        public void setShareCode(String shareCode) { this.shareCode = shareCode; }
        public AuthDTOs.UserSummaryDTO getOwner() { return owner; }
        public void setOwner(AuthDTOs.UserSummaryDTO owner) { this.owner = owner; }
        public Integer getCollaboratorCount() { return collaboratorCount; }
        public void setCollaboratorCount(Integer collaboratorCount) { this.collaboratorCount = collaboratorCount; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
    }

    public static class TripDetailResponse {
        private Long id;
        private String title;
        private String description;
        private String coverImageUrl;
        private DestinationDTOs.DestinationResponse destination;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer totalDays;
        private Integer travelerCount;
        private String travelerType;
        private BigDecimal targetBudget;
        private BigDecimal actualSpend;
        private String currency;
        private Trip.TripStatus status;
        private Trip.TripPrivacy privacy;
        private String shareCode;
        private AuthDTOs.UserSummaryDTO owner;
        private String userRole;
        private List<CollaboratorDTO> collaborators;
        private List<ItineraryDTOs.ItineraryDayResponse> days;
        private DestinationDTOs.WeatherForecastDTO weather;

        public TripDetailResponse() {}

        public static TripDetailResponseBuilder builder() { return new TripDetailResponseBuilder(); }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCoverImageUrl() { return coverImageUrl; }
        public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
        public DestinationDTOs.DestinationResponse getDestination() { return destination; }
        public void setDestination(DestinationDTOs.DestinationResponse destination) { this.destination = destination; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public Integer getTotalDays() { return totalDays; }
        public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; }
        public Integer getTravelerCount() { return travelerCount; }
        public void setTravelerCount(Integer travelerCount) { this.travelerCount = travelerCount; }
        public String getTravelerType() { return travelerType; }
        public void setTravelerType(String travelerType) { this.travelerType = travelerType; }
        public BigDecimal getTargetBudget() { return targetBudget; }
        public void setTargetBudget(BigDecimal targetBudget) { this.targetBudget = targetBudget; }
        public BigDecimal getActualSpend() { return actualSpend; }
        public void setActualSpend(BigDecimal actualSpend) { this.actualSpend = actualSpend; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public Trip.TripStatus getStatus() { return status; }
        public void setStatus(Trip.TripStatus status) { this.status = status; }
        public Trip.TripPrivacy getPrivacy() { return privacy; }
        public void setPrivacy(Trip.TripPrivacy privacy) { this.privacy = privacy; }
        public String getShareCode() { return shareCode; }
        public void setShareCode(String shareCode) { this.shareCode = shareCode; }
        public AuthDTOs.UserSummaryDTO getOwner() { return owner; }
        public void setOwner(AuthDTOs.UserSummaryDTO owner) { this.owner = owner; }
        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }
        public List<CollaboratorDTO> getCollaborators() { return collaborators; }
        public void setCollaborators(List<CollaboratorDTO> collaborators) { this.collaborators = collaborators; }
        public List<ItineraryDTOs.ItineraryDayResponse> getDays() { return days; }
        public void setDays(List<ItineraryDTOs.ItineraryDayResponse> days) { this.days = days; }
        public DestinationDTOs.WeatherForecastDTO getWeather() { return weather; }
        public void setWeather(DestinationDTOs.WeatherForecastDTO weather) { this.weather = weather; }

        public static class TripDetailResponseBuilder {
            private Long id;
            private String title;
            private String description;
            private String coverImageUrl;
            private DestinationDTOs.DestinationResponse destination;
            private LocalDate startDate;
            private LocalDate endDate;
            private Integer totalDays;
            private Integer travelerCount;
            private String travelerType;
            private BigDecimal targetBudget;
            private BigDecimal actualSpend;
            private String currency;
            private Trip.TripStatus status;
            private Trip.TripPrivacy privacy;
            private String shareCode;
            private AuthDTOs.UserSummaryDTO owner;
            private String userRole;
            private List<CollaboratorDTO> collaborators;
            private List<ItineraryDTOs.ItineraryDayResponse> days;
            private DestinationDTOs.WeatherForecastDTO weather;

            public TripDetailResponseBuilder id(Long id) { this.id = id; return this; }
            public TripDetailResponseBuilder title(String title) { this.title = title; return this; }
            public TripDetailResponseBuilder description(String description) { this.description = description; return this; }
            public TripDetailResponseBuilder coverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; return this; }
            public TripDetailResponseBuilder destination(DestinationDTOs.DestinationResponse destination) { this.destination = destination; return this; }
            public TripDetailResponseBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
            public TripDetailResponseBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
            public TripDetailResponseBuilder totalDays(Integer totalDays) { this.totalDays = totalDays; return this; }
            public TripDetailResponseBuilder travelerCount(Integer travelerCount) { this.travelerCount = travelerCount; return this; }
            public TripDetailResponseBuilder travelerType(String travelerType) { this.travelerType = travelerType; return this; }
            public TripDetailResponseBuilder targetBudget(BigDecimal targetBudget) { this.targetBudget = targetBudget; return this; }
            public TripDetailResponseBuilder actualSpend(BigDecimal actualSpend) { this.actualSpend = actualSpend; return this; }
            public TripDetailResponseBuilder currency(String currency) { this.currency = currency; return this; }
            public TripDetailResponseBuilder status(Trip.TripStatus status) { this.status = status; return this; }
            public TripDetailResponseBuilder privacy(Trip.TripPrivacy privacy) { this.privacy = privacy; return this; }
            public TripDetailResponseBuilder shareCode(String shareCode) { this.shareCode = shareCode; return this; }
            public TripDetailResponseBuilder owner(AuthDTOs.UserSummaryDTO owner) { this.owner = owner; return this; }
            public TripDetailResponseBuilder userRole(String userRole) { this.userRole = userRole; return this; }
            public TripDetailResponseBuilder collaborators(List<CollaboratorDTO> collaborators) { this.collaborators = collaborators; return this; }
            public TripDetailResponseBuilder days(List<ItineraryDTOs.ItineraryDayResponse> days) { this.days = days; return this; }
            public TripDetailResponseBuilder weather(DestinationDTOs.WeatherForecastDTO weather) { this.weather = weather; return this; }

            public TripDetailResponse build() {
                TripDetailResponse r = new TripDetailResponse();
                r.id = id; r.title = title; r.description = description; r.coverImageUrl = coverImageUrl;
                r.destination = destination; r.startDate = startDate; r.endDate = endDate; r.totalDays = totalDays;
                r.travelerCount = travelerCount; r.travelerType = travelerType; r.targetBudget = targetBudget;
                r.actualSpend = actualSpend; r.currency = currency; r.status = status; r.privacy = privacy;
                r.shareCode = shareCode; r.owner = owner; r.userRole = userRole; r.collaborators = collaborators;
                r.days = days; r.weather = weather;
                return r;
            }
        }
    }

    public static class CollaboratorDTO {
        private Long id;
        private Long userId;
        private String email;
        private String fullName;
        private String avatarUrl;
        private TripCollaborator.CollaboratorRole role;
        private TripCollaborator.InviteStatus inviteStatus;

        public CollaboratorDTO() {}
        public CollaboratorDTO(Long id, Long userId, String email, String fullName, String avatarUrl, TripCollaborator.CollaboratorRole role, TripCollaborator.InviteStatus inviteStatus) {
            this.id = id;
            this.userId = userId;
            this.email = email;
            this.fullName = fullName;
            this.avatarUrl = avatarUrl;
            this.role = role;
            this.inviteStatus = inviteStatus;
        }

        public static CollaboratorDTO fromEntity(TripCollaborator c) {
            if (c == null) return null;
            return new CollaboratorDTO(
                    c.getId(),
                    c.getUser() != null ? c.getUser().getId() : null,
                    c.getUser() != null ? c.getUser().getEmail() : c.getInvitedEmail(),
                    c.getUser() != null ? c.getUser().getFullName() : "Invited User",
                    c.getUser() != null ? c.getUser().getAvatarUrl() : null,
                    c.getRole(),
                    c.getInviteStatus()
            );
        }

        public static CollaboratorDTOBuilder builder() { return new CollaboratorDTOBuilder(); }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
        public TripCollaborator.CollaboratorRole getRole() { return role; }
        public void setRole(TripCollaborator.CollaboratorRole role) { this.role = role; }
        public TripCollaborator.InviteStatus getInviteStatus() { return inviteStatus; }
        public void setInviteStatus(TripCollaborator.InviteStatus inviteStatus) { this.inviteStatus = inviteStatus; }

        public static class CollaboratorDTOBuilder {
            private Long id;
            private Long userId;
            private String email;
            private String fullName;
            private String avatarUrl;
            private TripCollaborator.CollaboratorRole role;
            private TripCollaborator.InviteStatus inviteStatus;

            public CollaboratorDTOBuilder id(Long id) { this.id = id; return this; }
            public CollaboratorDTOBuilder userId(Long userId) { this.userId = userId; return this; }
            public CollaboratorDTOBuilder email(String email) { this.email = email; return this; }
            public CollaboratorDTOBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public CollaboratorDTOBuilder avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }
            public CollaboratorDTOBuilder role(TripCollaborator.CollaboratorRole role) { this.role = role; return this; }
            public CollaboratorDTOBuilder inviteStatus(TripCollaborator.InviteStatus inviteStatus) { this.inviteStatus = inviteStatus; return this; }

            public CollaboratorDTO build() {
                return new CollaboratorDTO(id, userId, email, fullName, avatarUrl, role, inviteStatus);
            }
        }
    }
}
