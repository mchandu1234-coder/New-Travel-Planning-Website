-- =========================================================
-- V2: Seed Destinations, Activities, Restaurants, Users & Sample Trips
-- =========================================================

-- 1. Seed Users (password: 'password123')
-- BCrypt for 'password123': $2a$10$eACCYoNOHEqgkmlK5X0v1.A6bLzPvd2l7GvJk73aT9c14uB7zKgeK
INSERT INTO users (id, email, password_hash, full_name, avatar_url, bio, home_airport, preferred_currency, travel_style, travel_interests, budget_tier, role)
VALUES 
(1, 'demo@wanderlust.com', '$2a$10$eACCYoNOHEqgkmlK5X0v1.A6bLzPvd2l7GvJk73aT9c14uB7zKgeK', 'Alex Morgan', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80', 'Passionate globetrotter, photographer, and coffee enthusiast.', 'SFO', 'USD', 'BALANCED', 'Culture,Food,Nature,Photography', 'MID_RANGE', 'ROLE_USER'),
(2, 'sarah.travels@world.com', '$2a$10$eACCYoNOHEqgkmlK5X0v1.A6bLzPvd2l7GvJk73aT9c14uB7zKgeK', 'Sarah Jenkins', 'https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80', 'Adventure seeker & hiker exploring mountain trails across Europe & Asia.', 'LHR', 'EUR', 'ADVENTURE', 'Hiking,Architecture,Local Food,Diving', 'LUXURY', 'ROLE_USER'),
(3, 'marcus.chen@explorer.io', '$2a$10$eACCYoNOHEqgkmlK5X0v1.A6bLzPvd2l7GvJk73aT9c14uB7zKgeK', 'Marcus Chen', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80', 'Architectural historian and street food connoisseur.', 'SIN', 'SGD', 'CULTURE', 'Museums,History,Nightlife,Art', 'BALANCED', 'ROLE_USER')
ON CONFLICT (id) DO NOTHING;

-- 2. Seed Destinations
INSERT INTO destinations (id, name, city, country, continent, hero_image_url, gallery_images, description, vibe_tags, average_daily_cost, currency, latitude, longitude, rating, review_count, popular_sights, best_time_to_visit, is_featured)
VALUES 
(1, 'Tokyo Metropolis', 'Tokyo', 'Japan', 'Asia', 
 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1542051841857-5f90071e7989?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1536098561742-ca998e48cbcc?auto=format&fit=crop&w=800&q=80',
 'A dazzling fusion of ultra-modern skyscrapers, neon-lit nightlife, historic temples, and world-class culinary innovation.',
 'Modern,Foodie,Culture,Shopping,Nightlife', 180.00, 'JPY', 35.6762, 139.6503, 4.95, 1280,
 'Shibuya Crossing, Senso-ji Temple, Shinjuku Gyoen, TeamLab Planets, Akihabara Electric Town', 'March - May & September - November', true),

(2, 'Paris Romance & Heritage', 'Paris', 'France', 'Europe',
 'https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1499856871958-5b9627545d1a?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1471623320832-752e8bbf8413?auto=format&fit=crop&w=800&q=80',
 'The iconic City of Light, renowned for legendary haute cuisine, high fashion, world-class art museums, and charming café-lined boulevards.',
 'Romantic,Art,History,Foodie,Architecture', 210.00, 'EUR', 48.8566, 2.3522, 4.88, 2450,
 'Eiffel Tower, Louvre Museum, Notre-Dame, Montmartre & Sacré-Cœur, Seine River Cruise', 'April - June & September - October', true),

(3, 'Bali Tropical Sanctuary', 'Ubud & Seminyak', 'Indonesia', 'Asia',
 'https://images.unsplash.com/photo-1537996194471-e657df975ab4?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1518548419970-58e3b4079ab2?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1555400038-63f5ba517a47?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=800&q=80',
 'An enchanting Indonesian paradise with emerald rice terraces, sacred sea temples, surf beaches, lush wellness resorts, and vibrant arts.',
 'Relaxed,Nature,Beach,Wellness,Adventure', 85.00, 'IDR', -8.4095, 115.1889, 4.82, 1980,
 'Tegallalang Rice Terraces, Uluwatu Temple, Sacred Monkey Forest, Mount Batur Sunrise Hike, Nusa Penida', 'April - October', true),

(4, 'Amalfi Coast & Positano', 'Amalfi', 'Italy', 'Europe',
 'https://images.unsplash.com/photo-1533105079780-92b9be482077?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1516483638261-f4dbaf036963?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80',
 'Dramatic Mediterranean cliffs, pastel fishing villages clinging to cliffsides, azure waters, lemon groves, and authentic Southern Italian dining.',
 'Romantic,Scenic,Luxury,Foodie,Beach', 260.00, 'EUR', 40.6340, 14.6027, 4.91, 890,
 'Positano Cliffside Walk, Path of the Gods, Villa Rufolo Ravello, Capri Island Boat Tour, Amalfi Cathedral', 'May - September', true),

(5, 'Reykjavik & Wild Iceland', 'Reykjavik', 'Iceland', 'Europe',
 'https://images.unsplash.com/photo-1504893524553-b855bce32c67?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1529963183134-61a90db47eaf?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=800&q=80',
 'The land of fire and ice, boasting mystical Northern Lights, geothermal lagoons, roaring waterfalls, black sand beaches, and glaciers.',
 'Adventure,Nature,Scenic,Photography,Wellness', 240.00, 'ISK', 64.1466, -21.9426, 4.89, 1120,
 'Blue Lagoon, Golden Circle, Gullfoss Waterfall, Reynisfjara Black Sand Beach, Jokulsarlon Glacier Lagoon', 'September - March (Aurora) & June - August (Midnight Sun)', true),

(6, 'Cape Town Coastal Majesty', 'Cape Town', 'South Africa', 'Africa',
 'https://images.unsplash.com/photo-1580618672591-eb180b1a973f?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1576485290814-1c72aa4bbb8e?auto=format&fit=crop&w=800&q=80',
 'A spectacular melting pot of rugged ocean cliffs, iconic Table Mountain peaks, wine valleys, penguin colonies, and vibrant culture.',
 'Adventure,Scenic,Foodie,Wildlife,Nature', 110.00, 'ZAR', -33.9249, 18.4241, 4.86, 740,
 'Table Mountain Cable Car, Cape Point & Cape of Good Hope, Boulders Penguin Beach, Stellenbosch Wine Route, Camps Bay', 'November - March', false)
ON CONFLICT (id) DO NOTHING;

-- 3. Seed Activities
INSERT INTO activities (id, destination_id, name, category, description, duration_hours, price, currency, rating, review_count, image_url, address, opening_hours, booking_required)
VALUES
(1, 1, 'Shibuya Sky & Harajuku Culture Walk', 'CULTURE', 'Panoramic 360-degree observation deck followed by a guided immersion into Takeshita street fashion and Meiji Shrine.', 3.5, 45.00, 'USD', 4.9, 320, 'https://images.unsplash.com/photo-1542051841857-5f90071e7989?auto=format&fit=crop&w=600&q=80', '2 Chome-24-12 Shibuya, Tokyo', '10:00 - 22:30', true),
(2, 1, 'Tsukiji Outer Market Food Safari', 'CULTURE', 'Taste fresh sashimi, tamagoyaki, wagyu skewers, and matcha sweets with a local chef guide.', 2.5, 65.00, 'USD', 4.95, 410, 'https://images.unsplash.com/photo-1553621042-f6e147245754?auto=format&fit=crop&w=600&q=80', '4 Chome-16-2 Tsukiji, Chuo City, Tokyo', '08:00 - 14:00', false),
(3, 1, 'TeamLab Planets Immersive Digital Art', 'MUSEUM', 'Walk through water and interactive crystal universes in this world-famous digital museum.', 2.0, 38.00, 'USD', 4.88, 890, 'https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=600&q=80', '6 Chome-1-16 Toyosu, Koto City, Tokyo', '09:00 - 22:00', true),
(4, 2, 'Louvre Museum Masterpieces with Art Historian', 'MUSEUM', 'Skip-the-line VIP access to the Mona Lisa, Venus de Milo, and Winged Victory with in-depth stories.', 3.0, 75.00, 'USD', 4.92, 620, 'https://images.unsplash.com/photo-1499856871958-5b9627545d1a?auto=format&fit=crop&w=600&q=80', 'Rue de Rivoli, 75001 Paris', '09:00 - 18:00', true),
(5, 2, 'Sunset Champagne Seine River Cruise', 'SIGHTSEEING', 'Glaze through illuminated Parisian bridges and the sparkling Eiffel Tower with French champagne.', 1.5, 35.00, 'USD', 4.85, 950, 'https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?auto=format&fit=crop&w=600&q=80', 'Port de la Bourdonnais, 75007 Paris', '18:00 - 23:00', true),
(6, 3, 'Mount Batur Sunrise Trek & Hot Springs', 'ADVENTURE', 'Early morning volcanic hike with breathtaking caldera sunrise breakfast and thermal spring soak.', 6.0, 55.00, 'USD', 4.91, 540, 'https://images.unsplash.com/photo-1518548419970-58e3b4079ab2?auto=format&fit=crop&w=600&q=80', 'Kintamani, Bangli Regency, Bali', '03:00 - 12:00', true)
ON CONFLICT (id) DO NOTHING;

-- 4. Seed Restaurants
INSERT INTO restaurants (id, destination_id, name, cuisine_type, price_range, rating, review_count, address, opening_hours, specialties, image_url, reservation_url)
VALUES
(1, 1, 'Afuri Ramen & Craft Brews', 'Japanese Ramen', '$$', 4.8, 480, '1 Chome-1-7 Ebisu, Shibuya City, Tokyo', '11:00 - 23:00', 'Yuzu Shio Ramen, Charcoal Grilled Pork, Vegan Ramen', 'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=600&q=80', 'https://afuri.com'),
(2, 1, 'Sushi Dai Omakase', 'Sushi & Seafood', '$$$$', 4.95, 310, 'Toyosu Market Block 6, Koto City, Tokyo', '06:00 - 14:00', 'Otoro, Uni, Botan Ebi, Fresh Daily Catch', 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?auto=format&fit=crop&w=600&q=80', 'https://sushidai.jp'),
(3, 2, 'Le Coupe-Chou Latin Quarter', 'Traditional French', '$$$', 4.7, 340, '11 Rue de Laveau-le-Vicomte, 75005 Paris', '12:00 - 23:00', 'Boeuf Bourguignon, Duck Confit, Soufflé au Chocolat', 'https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=600&q=80', 'https://lecoupechou.com'),
(4, 3, 'Locavore Herbivore Ubud', 'Modern Indonesian Farm-to-Table', '$$$', 4.9, 290, 'Jl. Dewi Sita No.10, Ubud, Bali', '12:00 - 22:00', 'Smoked Heirloom Tomatoes, Bali Truffle Rice, Fermented Mango Glaze', 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=600&q=80', 'https://locavorenext.com')
ON CONFLICT (id) DO NOTHING;

-- 5. Seed Sample Trip
INSERT INTO trips (id, owner_id, destination_id, title, description, cover_image_url, start_date, end_date, total_days, traveler_count, traveler_type, target_budget, actual_spend, currency, status, privacy, share_code)
VALUES
(1, 1, 1, 'Tokyo Sakura Discovery 2026', 'An unforgettable 5-day journey through Tokyo futuristic sights, cherry blossoms, historic shrines, and culinary legends.', 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=1200&q=80', '2026-10-10', '2026-10-14', 5, 2, 'COUPLE', 3500.00, 1420.00, 'USD', 'PLANNING', 'SHARED', 'tokyo-exp-2026-xyz')
ON CONFLICT (id) DO NOTHING;

-- Collaborators for trip 1
INSERT INTO trip_collaborators (id, trip_id, user_id, role, invite_status)
VALUES
(1, 1, 1, 'OWNER', 'ACCEPTED'),
(2, 1, 2, 'EDITOR', 'ACCEPTED')
ON CONFLICT (id) DO NOTHING;

-- Seed Itinerary Days for Trip 1
INSERT INTO itinerary_days (id, trip_id, day_number, date, title, notes)
VALUES
(1, 1, 1, '2026-10-10', 'Arrival & Neon Shinjuku', 'Check in at hotel, evening city exploration, ramen dinner.'),
(2, 1, 2, '2026-10-11', 'Historic Asakusa & Digital Dreams', 'Morning Senso-ji temple, afternoon TeamLab digital art.'),
(3, 1, 3, '2026-10-12', 'Shibuya Energy & Harajuku Vibes', 'Crossing views, Meiji Shrine tranquility, and shopping.'),
(4, 1, 4, '2026-10-13', 'Tsukiji Market & Mount Fuji Day Tour', 'Fresh seafood morning and express train towards Hakone & Fuji views.'),
(5, 1, 5, '2026-10-14', 'Ginza Elegance & Farewell Souvenirs', 'Last-minute specialty gifts, matcha tea ceremony, and departure.')
ON CONFLICT (id) DO NOTHING;

-- Seed Itinerary Items for Day 1
INSERT INTO itinerary_items (id, day_id, item_type, title, description, location_name, address, start_time, end_time, estimated_cost, currency, display_order, status)
VALUES
(1, 1, 'FLIGHT', 'Flight JL001: SFO to NRT', 'Direct flight arriving in Narita Tokyo at 14:30.', 'Narita International Airport', 'Narita, Chiba', '14:30:00', '16:00:00', 850.00, 'USD', 0, 'BOOKED'),
(2, 1, 'ACCOMMODATION', 'Check-in: Park Hyatt Tokyo', 'Iconic Shinjuku high-rise hotel overlooking the city skyline.', 'Park Hyatt Tokyo', '3-7-1-2 Nishi-Shinjuku, Tokyo', '17:00:00', '18:00:00', 320.00, 'USD', 1, 'BOOKED'),
(3, 1, 'RESTAURANT', 'Dinner at Afuri Ebisu', 'Savor world-class refreshing Yuzu Shio Ramen.', 'Afuri Ebisu', '1 Chome-1-7 Ebisu, Shibuya City, Tokyo', '19:30:00', '21:00:00', 35.00, 'USD', 2, 'PLANNED')
ON CONFLICT (id) DO NOTHING;

-- Seed Itinerary Items for Day 2
INSERT INTO itinerary_items (id, day_id, item_type, title, description, location_name, address, start_time, end_time, estimated_cost, currency, display_order, status)
VALUES
(4, 2, 'ACTIVITY', 'Senso-ji Temple & Nakamise Street', 'Tokyo oldest ancient Buddhist temple and traditional street crafts.', 'Senso-ji Temple', '2 Chome-3-1 Asakusa, Taito City, Tokyo', '09:00:00', '11:30:00', 0.00, 'USD', 0, 'PLANNED'),
(5, 2, 'RESTAURANT', 'Lunch at Asakusa Imahan', 'Classic Sukiyaki and Wagyu hot pot.', 'Asakusa Imahan', '3 Chome-1-12 Nishiasakusa, Taito City, Tokyo', '12:00:00', '13:30:00', 45.00, 'USD', 1, 'PLANNED'),
(6, 2, 'ACTIVITY', 'TeamLab Planets Experience', 'Immersive walk-through water digital art sensory journey.', 'TeamLab Planets', '6 Chome-1-16 Toyosu, Koto City, Tokyo', '15:00:00', '17:30:00', 38.00, 'USD', 2, 'BOOKED')
ON CONFLICT (id) DO NOTHING;

-- Seed Sample Expenses for Trip 1
INSERT INTO expenses (id, trip_id, title, category, amount, currency, converted_amount, paid_by_user_id, split_type, date, notes)
VALUES
(1, 1, 'Flight Tickets (x2 SFO-NRT)', 'FLIGHTS', 1700.00, 'USD', 1700.00, 1, 'EQUAL', '2026-10-01', 'Direct booking on JAL'),
(2, 1, 'Park Hyatt Deposit', 'STAYS', 640.00, 'USD', 640.00, 2, 'EQUAL', '2026-10-02', 'First 2 nights deposit'),
(3, 1, 'TeamLab Planets VIP Tickets', 'ACTIVITIES', 76.00, 'USD', 76.00, 1, 'EQUAL', '2026-10-05', 'Advance booking')
ON CONFLICT (id) DO NOTHING;

-- Splits for expenses
INSERT INTO expense_splits (id, expense_id, user_id, amount_owed, is_paid)
VALUES
(1, 1, 1, 850.00, true),
(2, 1, 2, 850.00, false),
(3, 2, 1, 320.00, false),
(4, 2, 2, 320.00, true),
(5, 3, 1, 38.00, true),
(6, 3, 2, 38.00, false)
ON CONFLICT (id) DO NOTHING;

-- Update serial sequences
SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 1) FROM users));
SELECT setval('destinations_id_seq', (SELECT COALESCE(MAX(id), 1) FROM destinations));
SELECT setval('trips_id_seq', (SELECT COALESCE(MAX(id), 1) FROM trips));
SELECT setval('trip_collaborators_id_seq', (SELECT COALESCE(MAX(id), 1) FROM trip_collaborators));
SELECT setval('itinerary_days_id_seq', (SELECT COALESCE(MAX(id), 1) FROM itinerary_days));
SELECT setval('itinerary_items_id_seq', (SELECT COALESCE(MAX(id), 1) FROM itinerary_items));
SELECT setval('activities_id_seq', (SELECT COALESCE(MAX(id), 1) FROM activities));
SELECT setval('restaurants_id_seq', (SELECT COALESCE(MAX(id), 1) FROM restaurants));
SELECT setval('expenses_id_seq', (SELECT COALESCE(MAX(id), 1) FROM expenses));
SELECT setval('expense_splits_id_seq', (SELECT COALESCE(MAX(id), 1) FROM expense_splits));
