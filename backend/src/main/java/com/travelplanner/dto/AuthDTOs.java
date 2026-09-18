package com.travelplanner.dto;

import com.travelplanner.entity.Role;
import com.travelplanner.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDTOs {

    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        public LoginRequest() {}
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public static LoginRequestBuilder builder() { return new LoginRequestBuilder(); }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public static class LoginRequestBuilder {
            private String email;
            private String password;
            public LoginRequestBuilder email(String email) { this.email = email; return this; }
            public LoginRequestBuilder password(String password) { this.password = password; return this; }
            public LoginRequest build() { return new LoginRequest(email, password); }
        }
    }

    public static class RegisterRequest {
        @NotBlank(message = "Full name is required")
        private String fullName;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        private String preferredCurrency;
        private String travelStyle;
        private String homeAirport;

        public RegisterRequest() {}
        public RegisterRequest(String fullName, String email, String password, String preferredCurrency, String travelStyle, String homeAirport) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.preferredCurrency = preferredCurrency;
            this.travelStyle = travelStyle;
            this.homeAirport = homeAirport;
        }

        public static RegisterRequestBuilder builder() { return new RegisterRequestBuilder(); }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getPreferredCurrency() { return preferredCurrency; }
        public void setPreferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; }
        public String getTravelStyle() { return travelStyle; }
        public void setTravelStyle(String travelStyle) { this.travelStyle = travelStyle; }
        public String getHomeAirport() { return homeAirport; }
        public void setHomeAirport(String homeAirport) { this.homeAirport = homeAirport; }

        public static class RegisterRequestBuilder {
            private String fullName;
            private String email;
            private String password;
            private String preferredCurrency;
            private String travelStyle;
            private String homeAirport;
            public RegisterRequestBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public RegisterRequestBuilder email(String email) { this.email = email; return this; }
            public RegisterRequestBuilder password(String password) { this.password = password; return this; }
            public RegisterRequestBuilder preferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; return this; }
            public RegisterRequestBuilder travelStyle(String travelStyle) { this.travelStyle = travelStyle; return this; }
            public RegisterRequestBuilder homeAirport(String homeAirport) { this.homeAirport = homeAirport; return this; }
            public RegisterRequest build() { return new RegisterRequest(fullName, email, password, preferredCurrency, travelStyle, homeAirport); }
        }
    }

    public static class AuthResponse {
        private String token;
        private String tokenType;
        private Long expiresInMs;
        private UserSummaryDTO user;

        public AuthResponse() {}
        public AuthResponse(String token, String tokenType, Long expiresInMs, UserSummaryDTO user) {
            this.token = token;
            this.tokenType = tokenType;
            this.expiresInMs = expiresInMs;
            this.user = user;
        }

        public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
        public Long getExpiresInMs() { return expiresInMs; }
        public void setExpiresInMs(Long expiresInMs) { this.expiresInMs = expiresInMs; }
        public UserSummaryDTO getUser() { return user; }
        public void setUser(UserSummaryDTO user) { this.user = user; }

        public static class AuthResponseBuilder {
            private String token;
            private String tokenType;
            private Long expiresInMs;
            private UserSummaryDTO user;
            public AuthResponseBuilder token(String token) { this.token = token; return this; }
            public AuthResponseBuilder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
            public AuthResponseBuilder expiresInMs(Long expiresInMs) { this.expiresInMs = expiresInMs; return this; }
            public AuthResponseBuilder user(UserSummaryDTO user) { this.user = user; return this; }
            public AuthResponse build() { return new AuthResponse(token, tokenType, expiresInMs, user); }
        }
    }

    public static class UpdateProfileRequest {
        private String fullName;
        private String avatarUrl;
        private String bio;
        private String homeAirport;
        private String preferredCurrency;
        private String travelStyle;
        private String travelInterests;
        private String budgetTier;

        public UpdateProfileRequest() {}
        public UpdateProfileRequest(String fullName, String avatarUrl, String bio, String homeAirport, String preferredCurrency, String travelStyle, String travelInterests, String budgetTier) {
            this.fullName = fullName;
            this.avatarUrl = avatarUrl;
            this.bio = bio;
            this.homeAirport = homeAirport;
            this.preferredCurrency = preferredCurrency;
            this.travelStyle = travelStyle;
            this.travelInterests = travelInterests;
            this.budgetTier = budgetTier;
        }

        public static UpdateProfileRequestBuilder builder() { return new UpdateProfileRequestBuilder(); }
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

        public static class UpdateProfileRequestBuilder {
            private String fullName;
            private String avatarUrl;
            private String bio;
            private String homeAirport;
            private String preferredCurrency;
            private String travelStyle;
            private String travelInterests;
            private String budgetTier;
            public UpdateProfileRequestBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public UpdateProfileRequestBuilder avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }
            public UpdateProfileRequestBuilder bio(String bio) { this.bio = bio; return this; }
            public UpdateProfileRequestBuilder homeAirport(String homeAirport) { this.homeAirport = homeAirport; return this; }
            public UpdateProfileRequestBuilder preferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; return this; }
            public UpdateProfileRequestBuilder travelStyle(String travelStyle) { this.travelStyle = travelStyle; return this; }
            public UpdateProfileRequestBuilder travelInterests(String travelInterests) { this.travelInterests = travelInterests; return this; }
            public UpdateProfileRequestBuilder budgetTier(String budgetTier) { this.budgetTier = budgetTier; return this; }
            public UpdateProfileRequest build() { return new UpdateProfileRequest(fullName, avatarUrl, bio, homeAirport, preferredCurrency, travelStyle, travelInterests, budgetTier); }
        }
    }

    public static class UserSummaryDTO {
        private Long id;
        private String email;
        private String fullName;
        private String avatarUrl;
        private String bio;
        private String homeAirport;
        private String preferredCurrency;
        private String travelStyle;
        private String travelInterests;
        private String budgetTier;
        private Role role;

        public UserSummaryDTO() {}
        public UserSummaryDTO(Long id, String email, String fullName, String avatarUrl, String bio, String homeAirport, String preferredCurrency, String travelStyle, String travelInterests, String budgetTier, Role role) {
            this.id = id;
            this.email = email;
            this.fullName = fullName;
            this.avatarUrl = avatarUrl;
            this.bio = bio;
            this.homeAirport = homeAirport;
            this.preferredCurrency = preferredCurrency;
            this.travelStyle = travelStyle;
            this.travelInterests = travelInterests;
            this.budgetTier = budgetTier;
            this.role = role;
        }

        public static UserSummaryDTO fromEntity(User user) {
            if (user == null) return null;
            return new UserSummaryDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getAvatarUrl(),
                    user.getBio(),
                    user.getHomeAirport(),
                    user.getPreferredCurrency(),
                    user.getTravelStyle(),
                    user.getTravelInterests(),
                    user.getBudgetTier(),
                    user.getRole()
            );
        }

        public static UserSummaryDTOBuilder builder() { return new UserSummaryDTOBuilder(); }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
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

        public static class UserSummaryDTOBuilder {
            private Long id;
            private String email;
            private String fullName;
            private String avatarUrl;
            private String bio;
            private String homeAirport;
            private String preferredCurrency;
            private String travelStyle;
            private String travelInterests;
            private String budgetTier;
            private Role role;
            public UserSummaryDTOBuilder id(Long id) { this.id = id; return this; }
            public UserSummaryDTOBuilder email(String email) { this.email = email; return this; }
            public UserSummaryDTOBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public UserSummaryDTOBuilder avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }
            public UserSummaryDTOBuilder bio(String bio) { this.bio = bio; return this; }
            public UserSummaryDTOBuilder homeAirport(String homeAirport) { this.homeAirport = homeAirport; return this; }
            public UserSummaryDTOBuilder preferredCurrency(String preferredCurrency) { this.preferredCurrency = preferredCurrency; return this; }
            public UserSummaryDTOBuilder travelStyle(String travelStyle) { this.travelStyle = travelStyle; return this; }
            public UserSummaryDTOBuilder travelInterests(String travelInterests) { this.travelInterests = travelInterests; return this; }
            public UserSummaryDTOBuilder budgetTier(String budgetTier) { this.budgetTier = budgetTier; return this; }
            public UserSummaryDTOBuilder role(Role role) { this.role = role; return this; }
            public UserSummaryDTO build() { return new UserSummaryDTO(id, email, fullName, avatarUrl, bio, homeAirport, preferredCurrency, travelStyle, travelInterests, budgetTier, role); }
        }
    }
}
