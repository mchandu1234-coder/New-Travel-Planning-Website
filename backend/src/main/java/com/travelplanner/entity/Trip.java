package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {

    public enum TripStatus {
        PLANNING,
        CONFIRMED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    public enum TripPrivacy {
        PRIVATE,
        SHARED,
        PUBLIC
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash"})
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Destination destination;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays = 1;

    @Column(name = "traveler_count", nullable = false)
    private Integer travelerCount = 1;

    @Column(name = "traveler_type", length = 50)
    private String travelerType = "SOLO";

    @Column(name = "target_budget", nullable = false, precision = 12, scale = 2)
    private BigDecimal targetBudget = BigDecimal.valueOf(2000.00);

    @Column(name = "actual_spend", nullable = false, precision = 12, scale = 2)
    private BigDecimal actualSpend = BigDecimal.ZERO;

    @Column(nullable = false, length = 10)
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TripStatus status = TripStatus.PLANNING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TripPrivacy privacy = TripPrivacy.PRIVATE;

    @Column(name = "share_code", length = 64, unique = true)
    private String shareCode;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripCollaborator> collaborators = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayNumber ASC")
    private List<ItineraryDay> days = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public Trip() {}

    public Trip(Long id, User owner, Destination destination, String title, String description, String coverImageUrl, LocalDate startDate, LocalDate endDate, Integer totalDays, Integer travelerCount, String travelerType, BigDecimal targetBudget, BigDecimal actualSpend, String currency, TripStatus status, TripPrivacy privacy, String shareCode, boolean isDeleted, List<TripCollaborator> collaborators, List<ItineraryDay> days, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.owner = owner;
        this.destination = destination;
        this.title = title;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays != null ? totalDays : 1;
        this.travelerCount = travelerCount != null ? travelerCount : 1;
        this.travelerType = travelerType != null ? travelerType : "SOLO";
        this.targetBudget = targetBudget != null ? targetBudget : BigDecimal.valueOf(2000.00);
        this.actualSpend = actualSpend != null ? actualSpend : BigDecimal.ZERO;
        this.currency = currency != null ? currency : "USD";
        this.status = status != null ? status : TripStatus.PLANNING;
        this.privacy = privacy != null ? privacy : TripPrivacy.PRIVATE;
        this.shareCode = shareCode;
        this.isDeleted = isDeleted;
        this.collaborators = collaborators != null ? collaborators : new ArrayList<>();
        this.days = days != null ? days : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static TripBuilder builder() {
        return new TripBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public Destination getDestination() { return destination; }
    public void setDestination(Destination destination) { this.destination = destination; }
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
    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }
    public TripPrivacy getPrivacy() { return privacy; }
    public void setPrivacy(TripPrivacy privacy) { this.privacy = privacy; }
    public String getShareCode() { return shareCode; }
    public void setShareCode(String shareCode) { this.shareCode = shareCode; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public List<TripCollaborator> getCollaborators() { return collaborators; }
    public void setCollaborators(List<TripCollaborator> collaborators) { this.collaborators = collaborators; }
    public List<ItineraryDay> getDays() { return days; }
    public void setDays(List<ItineraryDay> days) { this.days = days; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class TripBuilder {
        private Long id;
        private User owner;
        private Destination destination;
        private String title;
        private String description;
        private String coverImageUrl;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer totalDays = 1;
        private Integer travelerCount = 1;
        private String travelerType = "SOLO";
        private BigDecimal targetBudget = BigDecimal.valueOf(2000.00);
        private BigDecimal actualSpend = BigDecimal.ZERO;
        private String currency = "USD";
        private TripStatus status = TripStatus.PLANNING;
        private TripPrivacy privacy = TripPrivacy.PRIVATE;
        private String shareCode;
        private boolean isDeleted = false;
        private List<TripCollaborator> collaborators = new ArrayList<>();
        private List<ItineraryDay> days = new ArrayList<>();
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        public TripBuilder id(Long id) { this.id = id; return this; }
        public TripBuilder owner(User owner) { this.owner = owner; return this; }
        public TripBuilder destination(Destination destination) { this.destination = destination; return this; }
        public TripBuilder title(String title) { this.title = title; return this; }
        public TripBuilder description(String description) { this.description = description; return this; }
        public TripBuilder coverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; return this; }
        public TripBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public TripBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public TripBuilder totalDays(Integer totalDays) { this.totalDays = totalDays; return this; }
        public TripBuilder travelerCount(Integer travelerCount) { this.travelerCount = travelerCount; return this; }
        public TripBuilder travelerType(String travelerType) { this.travelerType = travelerType; return this; }
        public TripBuilder targetBudget(BigDecimal targetBudget) { this.targetBudget = targetBudget; return this; }
        public TripBuilder actualSpend(BigDecimal actualSpend) { this.actualSpend = actualSpend; return this; }
        public TripBuilder currency(String currency) { this.currency = currency; return this; }
        public TripBuilder status(TripStatus status) { this.status = status; return this; }
        public TripBuilder privacy(TripPrivacy privacy) { this.privacy = privacy; return this; }
        public TripBuilder shareCode(String shareCode) { this.shareCode = shareCode; return this; }
        public TripBuilder isDeleted(boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public TripBuilder collaborators(List<TripCollaborator> collaborators) { this.collaborators = collaborators; return this; }
        public TripBuilder days(List<ItineraryDay> days) { this.days = days; return this; }
        public TripBuilder createdAt(ZonedDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TripBuilder updatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Trip build() {
            return new Trip(id, owner, destination, title, description, coverImageUrl, startDate, endDate, totalDays, travelerCount, travelerType, targetBudget, actualSpend, currency, status, privacy, shareCode, isDeleted, collaborators, days, createdAt, updatedAt);
        }
    }
}
