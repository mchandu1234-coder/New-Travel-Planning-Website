package com.travelplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "trip_collaborators")
public class TripCollaborator {

    public enum CollaboratorRole {
        OWNER,
        EDITOR,
        VIEWER
    }

    public enum InviteStatus {
        INVITED,
        ACCEPTED,
        DECLINED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @JsonIgnore
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash"})
    private User user;

    @Column(name = "invited_email")
    private String invitedEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CollaboratorRole role = CollaboratorRole.EDITOR;

    @Enumerated(EnumType.STRING)
    @Column(name = "invite_status", nullable = false, length = 50)
    private InviteStatus inviteStatus = InviteStatus.ACCEPTED;

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false)
    private ZonedDateTime joinedAt;

    public TripCollaborator() {}

    public TripCollaborator(Long id, Trip trip, User user, String invitedEmail, CollaboratorRole role, InviteStatus inviteStatus, ZonedDateTime joinedAt) {
        this.id = id;
        this.trip = trip;
        this.user = user;
        this.invitedEmail = invitedEmail;
        this.role = role != null ? role : CollaboratorRole.EDITOR;
        this.inviteStatus = inviteStatus != null ? inviteStatus : InviteStatus.ACCEPTED;
        this.joinedAt = joinedAt;
    }

    public static TripCollaboratorBuilder builder() {
        return new TripCollaboratorBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getInvitedEmail() { return invitedEmail; }
    public void setInvitedEmail(String invitedEmail) { this.invitedEmail = invitedEmail; }
    public CollaboratorRole getRole() { return role; }
    public void setRole(CollaboratorRole role) { this.role = role; }
    public InviteStatus getInviteStatus() { return inviteStatus; }
    public void setInviteStatus(InviteStatus inviteStatus) { this.inviteStatus = inviteStatus; }
    public ZonedDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(ZonedDateTime joinedAt) { this.joinedAt = joinedAt; }

    public static class TripCollaboratorBuilder {
        private Long id;
        private Trip trip;
        private User user;
        private String invitedEmail;
        private CollaboratorRole role = CollaboratorRole.EDITOR;
        private InviteStatus inviteStatus = InviteStatus.ACCEPTED;
        private ZonedDateTime joinedAt;

        public TripCollaboratorBuilder id(Long id) { this.id = id; return this; }
        public TripCollaboratorBuilder trip(Trip trip) { this.trip = trip; return this; }
        public TripCollaboratorBuilder user(User user) { this.user = user; return this; }
        public TripCollaboratorBuilder invitedEmail(String invitedEmail) { this.invitedEmail = invitedEmail; return this; }
        public TripCollaboratorBuilder role(CollaboratorRole role) { this.role = role; return this; }
        public TripCollaboratorBuilder inviteStatus(InviteStatus inviteStatus) { this.inviteStatus = inviteStatus; return this; }
        public TripCollaboratorBuilder joinedAt(ZonedDateTime joinedAt) { this.joinedAt = joinedAt; return this; }

        public TripCollaborator build() {
            return new TripCollaborator(id, trip, user, invitedEmail, role, inviteStatus, joinedAt);
        }
    }
}
