-- =========================================================
-- V1: Initial Database Schema for Travel Planning Platform
-- =========================================================

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    avatar_url TEXT,
    bio TEXT,
    home_airport VARCHAR(10) DEFAULT 'JFK',
    preferred_currency VARCHAR(10) DEFAULT 'USD',
    travel_style VARCHAR(50) DEFAULT 'BALANCED',
    travel_interests TEXT, -- Comma-separated tags
    budget_tier VARCHAR(50) DEFAULT 'MID_RANGE',
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- 2. Destinations Table
CREATE TABLE IF NOT EXISTS destinations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    continent VARCHAR(50) NOT NULL,
    hero_image_url TEXT NOT NULL,
    gallery_images TEXT, -- JSON or comma-separated URLs
    description TEXT NOT NULL,
    vibe_tags TEXT NOT NULL, -- Comma-separated: Romantic, Adventure, Foodie, History, Beach
    average_daily_cost DECIMAL(10, 2) NOT NULL DEFAULT 150.00,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    rating DECIMAL(3, 2) NOT NULL DEFAULT 4.8,
    review_count INT NOT NULL DEFAULT 0,
    popular_sights TEXT, -- JSON or comma-separated
    best_time_to_visit VARCHAR(150) NOT NULL,
    is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_destinations_continent ON destinations(continent);
CREATE INDEX IF NOT EXISTS idx_destinations_name ON destinations(name);

-- 3. Trips Table
CREATE TABLE IF NOT EXISTS trips (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    destination_id BIGINT REFERENCES destinations(id) ON DELETE SET NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    cover_image_url TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_days INT NOT NULL DEFAULT 1,
    traveler_count INT NOT NULL DEFAULT 1,
    traveler_type VARCHAR(50) DEFAULT 'SOLO',
    target_budget DECIMAL(12, 2) NOT NULL DEFAULT 2000.00,
    actual_spend DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    status VARCHAR(50) NOT NULL DEFAULT 'PLANNING',
    privacy VARCHAR(50) NOT NULL DEFAULT 'PRIVATE',
    share_code VARCHAR(64) UNIQUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_trips_owner ON trips(owner_id);
CREATE INDEX IF NOT EXISTS idx_trips_share_code ON trips(share_code);

-- 4. Trip Collaborators Table
CREATE TABLE IF NOT EXISTS trip_collaborators (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL REFERENCES trips(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    invited_email VARCHAR(255),
    role VARCHAR(50) NOT NULL DEFAULT 'EDITOR',
    invite_status VARCHAR(50) NOT NULL DEFAULT 'ACCEPTED',
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_trip_user UNIQUE (trip_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_collaborators_trip ON trip_collaborators(trip_id);

-- 5. Itinerary Days Table
CREATE TABLE IF NOT EXISTS itinerary_days (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL REFERENCES trips(id) ON DELETE CASCADE,
    day_number INT NOT NULL,
    date DATE NOT NULL,
    title VARCHAR(150),
    notes TEXT,
    CONSTRAINT uq_trip_day UNIQUE (trip_id, day_number)
);

CREATE INDEX IF NOT EXISTS idx_itinerary_days_trip ON itinerary_days(trip_id);

-- 6. Itinerary Items Table
CREATE TABLE IF NOT EXISTS itinerary_items (
    id BIGSERIAL PRIMARY KEY,
    day_id BIGINT NOT NULL REFERENCES itinerary_days(id) ON DELETE CASCADE,
    item_type VARCHAR(50) NOT NULL DEFAULT 'ACTIVITY', -- FLIGHT, ACCOMMODATION, ACTIVITY, RESTAURANT, TRANSIT, CUSTOM
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location_name VARCHAR(200),
    address TEXT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    start_time TIME,
    end_time TIME,
    estimated_cost DECIMAL(10, 2) DEFAULT 0.00,
    currency VARCHAR(10) DEFAULT 'USD',
    display_order INT NOT NULL DEFAULT 0,
    booking_reference VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'PLANNED',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_itinerary_items_day ON itinerary_items(day_id);

-- 7. Flights Table
CREATE TABLE IF NOT EXISTS flights (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL REFERENCES trips(id) ON DELETE CASCADE,
    airline VARCHAR(100) NOT NULL,
    flight_number VARCHAR(50) NOT NULL,
    departure_airport VARCHAR(10) NOT NULL,
    arrival_airport VARCHAR(10) NOT NULL,
    departure_time TIMESTAMP WITH TIME ZONE NOT NULL,
    arrival_time TIMESTAMP WITH TIME ZONE NOT NULL,
    duration_minutes INT NOT NULL,
    stops INT NOT NULL DEFAULT 0,
    price DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    seat_class VARCHAR(50) DEFAULT 'ECONOMY',
    booking_reference VARCHAR(100),
    booking_status VARCHAR(50) DEFAULT 'CONFIRMED',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_flights_trip ON flights(trip_id);

-- 8. Accommodations Table
CREATE TABLE IF NOT EXISTS accommodations (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL REFERENCES trips(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(50) DEFAULT 'HOTEL',
    address TEXT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    rating DECIMAL(3, 2) DEFAULT 4.5,
    price_per_night DECIMAL(10, 2) NOT NULL,
    total_cost DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'USD',
    image_url TEXT,
    amenities TEXT,
    booking_reference VARCHAR(100),
    booking_status VARCHAR(50) DEFAULT 'CONFIRMED',
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_accommodations_trip ON accommodations(trip_id);

-- 9. Activities & Attractions Catalog Table
CREATE TABLE IF NOT EXISTS activities (
    id BIGSERIAL PRIMARY KEY,
    destination_id BIGINT NOT NULL REFERENCES destinations(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL, -- SIGHTSEEING, ADVENTURE, MUSEUM, NATURE, CULTURE, NIGHTLIFE
    description TEXT NOT NULL,
    duration_hours DECIMAL(3, 1) NOT NULL DEFAULT 2.0,
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(10) DEFAULT 'USD',
    rating DECIMAL(3, 2) DEFAULT 4.7,
    review_count INT DEFAULT 0,
    image_url TEXT,
    address TEXT,
    opening_hours VARCHAR(100),
    booking_required BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_activities_dest ON activities(destination_id);

-- 10. Restaurants Catalog Table
CREATE TABLE IF NOT EXISTS restaurants (
    id BIGSERIAL PRIMARY KEY,
    destination_id BIGINT NOT NULL REFERENCES destinations(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    cuisine_type VARCHAR(100) NOT NULL,
    price_range VARCHAR(10) DEFAULT '$$', -- $, $$, $$$, $$$$
    rating DECIMAL(3, 2) DEFAULT 4.6,
    review_count INT DEFAULT 0,
    address TEXT NOT NULL,
    opening_hours VARCHAR(100),
    specialties TEXT,
    image_url TEXT,
    reservation_url TEXT
);

CREATE INDEX IF NOT EXISTS idx_restaurants_dest ON restaurants(destination_id);

-- 11. Expenses Table
CREATE TABLE IF NOT EXISTS expenses (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL REFERENCES trips(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL, -- STAYS, FLIGHTS, FOOD_DRINK, ACTIVITIES, TRANSPORT, SHOPPING, MISC
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    converted_amount DECIMAL(10, 2) NOT NULL,
    paid_by_user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    split_type VARCHAR(50) DEFAULT 'EQUAL', -- EQUAL, EXACT, PERCENTAGE
    receipt_url TEXT,
    date DATE NOT NULL,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_expenses_trip ON expenses(trip_id);

-- 12. Expense Splits Table
CREATE TABLE IF NOT EXISTS expense_splits (
    id BIGSERIAL PRIMARY KEY,
    expense_id BIGINT NOT NULL REFERENCES expenses(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    amount_owed DECIMAL(10, 2) NOT NULL,
    is_paid BOOLEAN NOT NULL DEFAULT FALSE,
    paid_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_expense_splits_expense ON expense_splits(expense_id);

-- 13. Reviews Table
CREATE TABLE IF NOT EXISTS reviews (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL, -- DESTINATION, ACTIVITY, RESTAURANT
    entity_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_reviews_entity ON reviews(entity_type, entity_id);
