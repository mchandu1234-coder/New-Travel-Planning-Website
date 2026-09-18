package com.travelplanner.service;

import com.travelplanner.dto.AuthDTOs.*;
import com.travelplanner.entity.Role;
import com.travelplanner.entity.User;
import com.travelplanner.exception.BadRequestException;
import com.travelplanner.exception.ResourceNotFoundException;
import com.travelplanner.repository.UserRepository;
import com.travelplanner.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new BadRequestException("An account with this email already exists.");
        }

        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .homeAirport(request.getHomeAirport() != null ? request.getHomeAirport().toUpperCase().trim() : "JFK")
                .preferredCurrency(request.getPreferredCurrency() != null ? request.getPreferredCurrency().toUpperCase().trim() : "USD")
                .travelStyle(request.getTravelStyle() != null ? request.getTravelStyle() : "BALANCED")
                .budgetTier("MID_RANGE")
                .avatarUrl("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=300&q=80")
                .role(Role.ROLE_USER)
                .build();

        user = userRepository.save(user);

        String token = tokenProvider.generateTokenForUser(user.getId(), user.getEmail(), user.getFullName());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresInMs(86400000L)
                .user(UserSummaryDTO.fromEntity(user))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresInMs(86400000L)
                .user(UserSummaryDTO.fromEntity(user))
                .build();
    }

    @Transactional(readOnly = true)
    public UserSummaryDTO getCurrentUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return UserSummaryDTO.fromEntity(user);
    }

    @Transactional
    public UserSummaryDTO updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (request.getFullName() != null) user.setFullName(request.getFullName().trim());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getHomeAirport() != null) user.setHomeAirport(request.getHomeAirport().toUpperCase().trim());
        if (request.getPreferredCurrency() != null) user.setPreferredCurrency(request.getPreferredCurrency().toUpperCase().trim());
        if (request.getTravelStyle() != null) user.setTravelStyle(request.getTravelStyle());
        if (request.getTravelInterests() != null) user.setTravelInterests(request.getTravelInterests());
        if (request.getBudgetTier() != null) user.setBudgetTier(request.getBudgetTier());

        user = userRepository.save(user);
        return UserSummaryDTO.fromEntity(user);
    }
}
