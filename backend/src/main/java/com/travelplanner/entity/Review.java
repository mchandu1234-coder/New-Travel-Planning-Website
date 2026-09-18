package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    public enum EntityType {
        DESTINATION,
        ACTIVITY,
        RESTAURANT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 50)
    private EntityType entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash"})
    private User user;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    public Review() {}

    public Review(Long id, EntityType entityType, Long entityId, User user, Integer rating, String comment, ZonedDateTime createdAt) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public static ReviewBuilder builder() {
        return new ReviewBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EntityType getEntityType() { return entityType; }
    public void setEntityType(EntityType entityType) { this.entityType = entityType; }
    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public static class ReviewBuilder {
        private Long id;
        private EntityType entityType;
        private Long entityId;
        private User user;
        private Integer rating;
        private String comment;
        private ZonedDateTime createdAt;

        public ReviewBuilder id(Long id) { this.id = id; return this; }
        public ReviewBuilder entityType(EntityType entityType) { this.entityType = entityType; return this; }
        public ReviewBuilder entityId(Long entityId) { this.entityId = entityId; return this; }
        public ReviewBuilder user(User user) { this.user = user; return this; }
        public ReviewBuilder rating(Integer rating) { this.rating = rating; return this; }
        public ReviewBuilder comment(String comment) { this.comment = comment; return this; }
        public ReviewBuilder createdAt(ZonedDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Review build() {
            return new Review(id, entityType, entityId, user, rating, comment, createdAt);
        }
    }
}
