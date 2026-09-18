package com.travelplanner.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "home_airport", length = 10)
    private String homeAirport = "JFK";

    @Column(name = "preferred_currency", length = 10)
    private String preferredCurrency = "USD";

    @Column(name = "travel_style", length = 50)
    private String travelStyle = "BALANCED";

    @Column(name = "travel_interests", columnDefinition = "TEXT")
    private String travelInterests;

    @Column(name = "budget_tier", length = 50)
    private String budgetTier = "MID_RANGE";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role = Role.ROLE_USER;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public User() {}

    public User(Long id, String email, String passwordHash, String fullName, String avatarUrl, String bio, String homeAirport, String preferredCurrency, String travelStyle, String travelInterests, String budgetTier, Role role, boolean isDeleted, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.homeAirport = homeAirport != null ? homeAirport : "JFK";
        this.preferredCurrency = preferredCurrency != null ? preferredCurrency : "USD";
        this.travelStyle = travelStyle != null ? travelStyle : "BALANCED";
        this.travelInterests = travelInterests;
        this.budgetTier = budgetTier != null ? budgetTier : "MID_RANGE";
        this.role = role != null ? role : Role.ROLE_USER;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getHomeAirport() { return homeAirport; }
    public void setHomeAirport(String homeAirport) { this.homeAirport = homeAirport; }
    public String getPreferredCurrency() { return preferredCurrency; }
    public void setPreferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; }
    public String getTravelStyle() { return travelStyle; }
    public void setTravelStyle(String travelStyle) { this.travelStyle = travelStyle; }
    public String getTravelInterests() { return travelInterests; }
    public void setTravelInterests(String travelInterests) { this.travelInterests = travelInterests; }
    public String getBudgetTier() { return budgetTier; }
    public void setBudgetTier(String budgetTier) { this.budgetTier = budgetTier; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class UserBuilder {
        private Long id;
        private String email;
        private String passwordHash;
        private String fullName;
        private String avatarUrl;
        private String bio;
        private String homeAirport = "JFK";
        private String preferredCurrency = "USD";
        private String travelStyle = "BALANCED";
        private String travelInterests;
        private String budgetTier = "MID_RANGE";
        private Role role = Role.ROLE_USER;
        private boolean isDeleted = false;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        public UserBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserBuilder avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }
        public UserBuilder bio(String bio) { this.bio = bio; return this; }
        public UserBuilder homeAirport(String homeAirport) { this.homeAirport = homeAirport; return this; }
        public UserBuilder preferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; return this; }
        public UserBuilder travelStyle(String travelStyle) { this.travelStyle = travelStyle; return this; }
        public UserBuilder travelInterests(String travelInterests) { this.travelInterests = travelInterests; return this; }
        public UserBuilder budgetTier(String budgetTier) { this.budgetTier = budgetTier; return this; }
        public UserBuilder role(Role role) { this.role = role; return this; }
        public UserBuilder isDeleted(boolean isDeleted) { this.isDeleted = isDeleted; return this; }
        public UserBuilder createdAt(ZonedDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserBuilder updatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public User build() {
            return new User(id, email, passwordHash, fullName, avatarUrl, bio, homeAirport, preferredCurrency, travelStyle, travelInterests, budgetTier, role, isDeleted, createdAt, updatedAt);
        }
    }
}
