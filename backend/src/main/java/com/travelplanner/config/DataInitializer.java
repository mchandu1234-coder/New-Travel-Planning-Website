package com.travelplanner.config;

import com.travelplanner.entity.*;
import com.travelplanner.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final ActivityRepository activityRepository;
    private final RestaurantRepository restaurantRepository;
    private final TripRepository tripRepository;
    private final TripCollaboratorRepository tripCollaboratorRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final ItineraryItemRepository itineraryItemRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, DestinationRepository destinationRepository, ActivityRepository activityRepository, RestaurantRepository restaurantRepository, TripRepository tripRepository, TripCollaboratorRepository tripCollaboratorRepository, ItineraryDayRepository itineraryDayRepository, ItineraryItemRepository itineraryItemRepository, ExpenseRepository expenseRepository, ExpenseSplitRepository expenseSplitRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
        this.activityRepository = activityRepository;
        this.restaurantRepository = restaurantRepository;
        this.tripRepository = tripRepository;
        this.tripCollaboratorRepository = tripCollaboratorRepository;
        this.itineraryDayRepository = itineraryDayRepository;
        this.itineraryItemRepository = itineraryItemRepository;
        this.expenseRepository = expenseRepository;
        this.expenseSplitRepository = expenseSplitRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already contains data, skipping seed initializer.");
            return;
        }

        log.info("Seeding initial travel platform data...");

        User alex = User.builder()
                .email("demo@wanderlust.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .fullName("Alex Morgan")
                .avatarUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80")
                .bio("Passionate globetrotter, photographer, and coffee enthusiast.")
                .homeAirport("SFO")
                .preferredCurrency("USD")
                .travelStyle("BALANCED")
                .travelInterests("Culture,Food,Nature,Photography")
                .budgetTier("MID_RANGE")
                .role(Role.ROLE_USER)
                .build();
        alex = userRepository.save(alex);

        User sarah = User.builder()
                .email("sarah.travels@world.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .fullName("Sarah Jenkins")
                .avatarUrl("https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80")
                .bio("Adventure seeker & hiker exploring mountain trails across Europe & Asia.")
                .homeAirport("LHR")
                .preferredCurrency("EUR")
                .travelStyle("ADVENTURE")
                .travelInterests("Hiking,Architecture,Local Food,Diving")
                .budgetTier("LUXURY")
                .role(Role.ROLE_USER)
                .build();
        sarah = userRepository.save(sarah);

        User marcus = User.builder()
                .email("marcus.chen@explorer.io")
                .passwordHash(passwordEncoder.encode("password123"))
                .fullName("Marcus Chen")
                .avatarUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80")
                .bio("Architectural historian and street food connoisseur.")
                .homeAirport("SIN")
                .preferredCurrency("SGD")
                .travelStyle("CULTURE")
                .travelInterests("Museums,History,Nightlife,Art")
                .budgetTier("BALANCED")
                .role(Role.ROLE_USER)
                .build();
        marcus = userRepository.save(marcus);

        Destination tokyo = Destination.builder()
                .name("Tokyo Metropolis")
                .city("Tokyo")
                .country("Japan")
                .continent("Asia")
                .heroImageUrl("https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=1600&q=80")
                .galleryImages("https://images.unsplash.com/photo-1542051841857-5f90071e7989?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1536098561742-ca998e48cbcc?auto=format&fit=crop&w=800&q=80")
                .description("A dazzling fusion of ultra-modern skyscrapers, neon-lit nightlife, historic temples, and world-class culinary innovation.")
                .vibeTags("Modern,Foodie,Culture,Shopping,Nightlife")
                .averageDailyCost(BigDecimal.valueOf(180.00))
                .currency("JPY")
                .latitude(35.6762)
                .longitude(139.6503)
                .rating(BigDecimal.valueOf(4.95))
                .reviewCount(1280)
                .popularSights("Shibuya Crossing, Senso-ji Temple, Shinjuku Gyoen, TeamLab Planets, Akihabara Electric Town")
                .bestTimeToVisit("March - May & September - November")
                .isFeatured(true)
                .build();
        tokyo = destinationRepository.save(tokyo);

        Destination paris = Destination.builder()
                .name("Paris Romance & Heritage")
                .city("Paris")
                .country("France")
                .continent("Europe")
                .heroImageUrl("https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=1600&q=80")
                .galleryImages("https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1499856871958-5b9627545d1a?auto=format&fit=crop&w=800&q=80")
                .description("The iconic City of Light, renowned for legendary haute cuisine, high fashion, world-class art museums, and charming café-lined boulevards.")
                .vibeTags("Romantic,Art,History,Foodie,Architecture")
                .averageDailyCost(BigDecimal.valueOf(210.00))
                .currency("EUR")
                .latitude(48.8566)
                .longitude(2.3522)
                .rating(BigDecimal.valueOf(4.88))
                .reviewCount(2450)
                .popularSights("Eiffel Tower, Louvre Museum, Notre-Dame, Montmartre & Sacré-Cœur, Seine River Cruise")
                .bestTimeToVisit("April - June & September - October")
                .isFeatured(true)
                .build();
        destinationRepository.save(paris);

        Destination bali = Destination.builder()
                .name("Bali Tropical Sanctuary")
                .city("Ubud & Seminyak")
                .country("Indonesia")
                .continent("Asia")
                .heroImageUrl("https://images.unsplash.com/photo-1537996194471-e657df975ab4?auto=format&fit=crop&w=1600&q=80")
                .galleryImages("https://images.unsplash.com/photo-1518548419970-58e3b4079ab2?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1555400038-63f5ba517a47?auto=format&fit=crop&w=800&q=80")
                .description("An enchanting Indonesian paradise with emerald rice terraces, sacred sea temples, surf beaches, lush wellness resorts, and vibrant arts.")
                .vibeTags("Relaxed,Nature,Beach,Wellness,Adventure")
                .averageDailyCost(BigDecimal.valueOf(85.00))
                .currency("IDR")
                .latitude(-8.4095)
                .longitude(115.1889)
                .rating(BigDecimal.valueOf(4.82))
                .reviewCount(1980)
                .popularSights("Tegallalang Rice Terraces, Uluwatu Temple, Sacred Monkey Forest, Mount Batur Sunrise Hike")
                .bestTimeToVisit("April - October")
                .isFeatured(true)
                .build();
        destinationRepository.save(bali);

        Destination amalfi = Destination.builder()
                .name("Amalfi Coast & Positano")
                .city("Amalfi")
                .country("Italy")
                .continent("Europe")
                .heroImageUrl("https://images.unsplash.com/photo-1533105079780-92b9be482077?auto=format&fit=crop&w=1600&q=80")
                .galleryImages("https://images.unsplash.com/photo-1516483638261-f4dbaf036963?auto=format&fit=crop&w=800&q=80")
                .description("Dramatic Mediterranean cliffs, pastel fishing villages clinging to cliffsides, azure waters, lemon groves, and authentic Southern Italian dining.")
                .vibeTags("Romantic,Scenic,Luxury,Foodie,Beach")
                .averageDailyCost(BigDecimal.valueOf(260.00))
                .currency("EUR")
                .latitude(40.6340)
                .longitude(14.6027)
                .rating(BigDecimal.valueOf(4.91))
                .reviewCount(890)
                .popularSights("Positano Cliffside Walk, Path of the Gods, Villa Rufolo Ravello, Capri Island Boat Tour")
                .bestTimeToVisit("May - September")
                .isFeatured(true)
                .build();
        destinationRepository.save(amalfi);

        Destination iceland = Destination.builder()
                .name("Reykjavik & Wild Iceland")
                .city("Reykjavik")
                .country("Iceland")
                .continent("Europe")
                .heroImageUrl("https://images.unsplash.com/photo-1504893524553-b855bce32c67?auto=format&fit=crop&w=1600&q=80")
                .galleryImages("https://images.unsplash.com/photo-1529963183134-61a90db47eaf?auto=format&fit=crop&w=800&q=80")
                .description("The land of fire and ice, boasting mystical Northern Lights, geothermal lagoons, roaring waterfalls, black sand beaches, and glaciers.")
                .vibeTags("Adventure,Nature,Scenic,Photography,Wellness")
                .averageDailyCost(BigDecimal.valueOf(240.00))
                .currency("ISK")
                .latitude(64.1466)
                .longitude(-21.9426)
                .rating(BigDecimal.valueOf(4.89))
                .reviewCount(1120)
                .popularSights("Blue Lagoon, Golden Circle, Gullfoss Waterfall, Reynisfjara Black Sand Beach")
                .bestTimeToVisit("September - March (Aurora) & June - August (Midnight Sun)")
                .isFeatured(true)
                .build();
        destinationRepository.save(iceland);

        Activity act1 = Activity.builder()
                .destination(tokyo)
                .name("Shibuya Sky & Harajuku Culture Walk")
                .category("CULTURE")
                .description("Panoramic 360-degree observation deck followed by a guided immersion into Takeshita street fashion and Meiji Shrine.")
                .durationHours(BigDecimal.valueOf(3.5))
                .price(BigDecimal.valueOf(45.00))
                .currency("USD")
                .rating(BigDecimal.valueOf(4.9))
                .reviewCount(320)
                .imageUrl("https://images.unsplash.com/photo-1542051841857-5f90071e7989?auto=format&fit=crop&w=600&q=80")
                .address("2 Chome-24-12 Shibuya, Tokyo")
                .openingHours("10:00 - 22:30")
                .bookingRequired(true)
                .build();
        activityRepository.save(act1);

        Activity act2 = Activity.builder()
                .destination(tokyo)
                .name("TeamLab Planets Immersive Digital Art")
                .category("MUSEUM")
                .description("Walk through water and interactive crystal universes in this world-famous digital museum.")
                .durationHours(BigDecimal.valueOf(2.0))
                .price(BigDecimal.valueOf(38.00))
                .currency("USD")
                .rating(BigDecimal.valueOf(4.88))
                .reviewCount(890)
                .imageUrl("https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=600&q=80")
                .address("6 Chome-1-16 Toyosu, Koto City, Tokyo")
                .openingHours("09:00 - 22:00")
                .bookingRequired(true)
                .build();
        activityRepository.save(act2);

        Restaurant rest1 = Restaurant.builder()
                .destination(tokyo)
                .name("Afuri Ramen & Craft Brews")
                .cuisineType("Japanese Ramen")
                .priceRange("$$")
                .rating(BigDecimal.valueOf(4.8))
                .reviewCount(480)
                .address("1 Chome-1-7 Ebisu, Shibuya City, Tokyo")
                .openingHours("11:00 - 23:00")
                .specialties("Yuzu Shio Ramen, Charcoal Grilled Pork, Vegan Ramen")
                .imageUrl("https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=600&q=80")
                .reservationUrl("https://afuri.com")
                .build();
        restaurantRepository.save(rest1);

        Trip sampleTrip = Trip.builder()
                .owner(alex)
                .destination(tokyo)
                .title("Tokyo Sakura Discovery 2026")
                .description("An unforgettable 5-day journey through Tokyo futuristic sights, cherry blossoms, historic shrines, and culinary legends.")
                .coverImageUrl("https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=1200&q=80")
                .startDate(LocalDate.of(2026, 10, 10))
                .endDate(LocalDate.of(2026, 10, 14))
                .totalDays(5)
                .travelerCount(2)
                .travelerType("COUPLE")
                .targetBudget(BigDecimal.valueOf(3500.00))
                .actualSpend(BigDecimal.valueOf(1420.00))
                .currency("USD")
                .status(Trip.TripStatus.PLANNING)
                .privacy(Trip.TripPrivacy.SHARED)
                .shareCode("tokyo-exp-2026-xyz")
                .build();
        sampleTrip = tripRepository.save(sampleTrip);

        TripCollaborator collabOwner = TripCollaborator.builder()
                .trip(sampleTrip)
                .user(alex)
                .role(TripCollaborator.CollaboratorRole.OWNER)
                .inviteStatus(TripCollaborator.InviteStatus.ACCEPTED)
                .build();
        tripCollaboratorRepository.save(collabOwner);

        TripCollaborator collabSarah = TripCollaborator.builder()
                .trip(sampleTrip)
                .user(sarah)
                .role(TripCollaborator.CollaboratorRole.EDITOR)
                .inviteStatus(TripCollaborator.InviteStatus.ACCEPTED)
                .build();
        tripCollaboratorRepository.save(collabSarah);

        for (int i = 1; i <= 5; i++) {
            ItineraryDay day = ItineraryDay.builder()
                    .trip(sampleTrip)
                    .dayNumber(i)
                    .date(LocalDate.of(2026, 10, 9 + i))
                    .title("Day " + i + " Adventure")
                    .notes("Explore Tokyo highlights and local cuisine")
                    .build();
            day = itineraryDayRepository.save(day);

            if (i == 1) {
                ItineraryItem flightItem = ItineraryItem.builder()
                        .day(day)
                        .itemType(ItineraryItem.ItemType.FLIGHT)
                        .title("Flight JL001: SFO to NRT")
                        .description("Direct flight arriving in Narita Tokyo at 14:30.")
                        .locationName("Narita International Airport")
                        .address("Narita, Chiba")
                        .startTime(LocalTime.of(14, 30))
                        .endTime(LocalTime.of(16, 0))
                        .estimatedCost(BigDecimal.valueOf(850.00))
                        .currency("USD")
                        .displayOrder(0)
                        .status(ItineraryItem.ItemStatus.BOOKED)
                        .build();
                itineraryItemRepository.save(flightItem);

                ItineraryItem hotelItem = ItineraryItem.builder()
                        .day(day)
                        .itemType(ItineraryItem.ItemType.ACCOMMODATION)
                        .title("Check-in: Park Hyatt Tokyo")
                        .description("Iconic Shinjuku high-rise hotel overlooking the city skyline.")
                        .locationName("Park Hyatt Tokyo")
                        .address("3-7-1-2 Nishi-Shinjuku, Tokyo")
                        .startTime(LocalTime.of(17, 0))
                        .endTime(LocalTime.of(18, 0))
                        .estimatedCost(BigDecimal.valueOf(320.00))
                        .currency("USD")
                        .displayOrder(1)
                        .status(ItineraryItem.ItemStatus.BOOKED)
                        .build();
                itineraryItemRepository.save(hotelItem);
            } else if (i == 2) {
                ItineraryItem sightItem = ItineraryItem.builder()
                        .day(day)
                        .itemType(ItineraryItem.ItemType.ACTIVITY)
                        .title("Senso-ji Temple & Nakamise Street")
                        .description("Tokyo oldest ancient Buddhist temple and traditional street crafts.")
                        .locationName("Senso-ji Temple")
                        .address("2 Chome-3-1 Asakusa, Taito City, Tokyo")
                        .startTime(LocalTime.of(9, 0))
                        .endTime(LocalTime.of(11, 30))
                        .estimatedCost(BigDecimal.ZERO)
                        .currency("USD")
                        .displayOrder(0)
                        .status(ItineraryItem.ItemStatus.PLANNED)
                        .build();
                itineraryItemRepository.save(sightItem);
            }
        }

        Expense exp1 = Expense.builder()
                .trip(sampleTrip)
                .title("Flight Tickets (x2 SFO-NRT)")
                .category(Expense.ExpenseCategory.FLIGHTS)
                .amount(BigDecimal.valueOf(1700.00))
                .currency("USD")
                .convertedAmount(BigDecimal.valueOf(1700.00))
                .paidByUser(alex)
                .splitType(Expense.SplitType.EQUAL)
                .date(LocalDate.of(2026, 10, 1))
                .notes("Direct booking on JAL")
                .build();
        exp1 = expenseRepository.save(exp1);

        ExpenseSplit split1A = ExpenseSplit.builder().expense(exp1).user(alex).amountOwed(BigDecimal.valueOf(850.00)).isPaid(true).build();
        ExpenseSplit split1B = ExpenseSplit.builder().expense(exp1).user(sarah).amountOwed(BigDecimal.valueOf(850.00)).isPaid(false).build();
        expenseSplitRepository.saveAll(List.of(split1A, split1B));

        log.info("Initial data seed completed successfully!");
    }
}
