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
 'https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1499856871958-5b9627545d1a?auto=format&fit=crop&w=800&q=80',
 'The iconic City of Light, renowned for legendary haute cuisine, high fashion, world-class art museums, and charming café-lined boulevards.',
 'Romantic,Art,History,Foodie,Architecture', 210.00, 'EUR', 48.8566, 2.3522, 4.88, 2450,
 'Eiffel Tower, Louvre Museum, Notre-Dame, Montmartre & Sacré-Cœur, Seine River Cruise', 'April - June & September - October', true),

(3, 'Bali Tropical Sanctuary', 'Ubud & Seminyak', 'Indonesia', 'Asia',
 'https://images.unsplash.com/photo-1537996194471-e657df975ab4?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1518548419970-58e3b4079ab2?auto=format&fit=crop&w=800&q=80,https://images.unsplash.com/photo-1555400038-63f5ba517a47?auto=format&fit=crop&w=800&q=80',
 'An enchanting Indonesian paradise with emerald rice terraces, sacred sea temples, surf beaches, lush wellness resorts, and vibrant arts.',
 'Relaxed,Nature,Beach,Wellness,Adventure', 85.00, 'IDR', -8.4095, 115.1889, 4.82, 1980,
 'Tegallalang Rice Terraces, Uluwatu Temple, Sacred Monkey Forest, Mount Batur Sunrise Hike, Nusa Penida', 'April - October', true),

(4, 'Amalfi Coast & Positano', 'Amalfi', 'Italy', 'Europe',
 'https://images.unsplash.com/photo-1533105079780-92b9be482077?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1516483638261-f4dbaf036963?auto=format&fit=crop&w=800&q=80',
 'Dramatic Mediterranean cliffs, pastel fishing villages clinging to cliffsides, azure waters, lemon groves, and authentic Southern Italian dining.',
 'Romantic,Scenic,Luxury,Foodie,Beach', 260.00, 'EUR', 40.6340, 14.6027, 4.91, 890,
 'Positano Cliffside Walk, Path of the Gods, Villa Rufolo Ravello, Capri Island Boat Tour, Amalfi Cathedral', 'May - September', true),

(5, 'Reykjavik & Wild Iceland', 'Reykjavik', 'Iceland', 'Europe',
 'https://images.unsplash.com/photo-1504893524553-b855bce32c67?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1529963183134-61a90db47eaf?auto=format&fit=crop&w=800&q=80',
 'The land of fire and ice, boasting mystical Northern Lights, geothermal lagoons, roaring waterfalls, black sand beaches, and glaciers.',
 'Adventure,Nature,Scenic,Photography,Wellness', 240.00, 'ISK', 64.1466, -21.9426, 4.89, 1120,
 'Blue Lagoon, Golden Circle, Gullfoss Waterfall, Reynisfjara Black Sand Beach, Jokulsarlon Glacier Lagoon', 'September - March (Aurora) & June - August (Midnight Sun)', true),

(6, 'Rome & Ancient Wonders', 'Rome', 'Italy', 'Europe',
 'https://images.unsplash.com/photo-1552832230-c0197dd311b5?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1531572753322-ad063cecc140?auto=format&fit=crop&w=800&q=80',
 'The Eternal City showcasing ancient gladiatorial arenas, Vatican treasures, Renaissance fountains, and timeless pasta trattorias.',
 'Historical,Culture,Art,Foodie,Romantic', 190.00, 'EUR', 41.9028, 12.4964, 4.93, 3100,
 'Colosseum, Vatican Museums & St. Peter''s, Trevi Fountain, Pantheon, Roman Forum', 'April - June & September - October', true),

(7, 'New York City Skyline', 'New York', 'United States', 'North America',
 'https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1534430480872-3498386e7856?auto=format&fit=crop&w=800&q=80',
 'The vibrant capital of culture, Broadway theater, world-class museums, Central Park serenity, and iconic skyscraper vistas.',
 'Urban,Modern,Culture,Shopping,Nightlife', 280.00, 'USD', 40.7128, -74.0060, 4.90, 4200,
 'Central Park, Empire State Building, Times Square, Statue of Liberty, High Line & Summit One Vanderbilt', 'April - June & September - December', true),

(8, 'London Royal Heritage', 'London', 'United Kingdom', 'Europe',
 'https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1520986606214-8b456906c813?auto=format&fit=crop&w=800&q=80',
 'Rich British royalty, historic Thames riverbanks, world-renowned free museums, West End musicals, and royal parks.',
 'Historical,Culture,Modern,Foodie,Shopping', 230.00, 'GBP', 51.5074, -0.1278, 4.87, 3500,
 'Big Ben & Westminster, British Museum, Tower Bridge, Buckingham Palace, London Eye', 'May - September', true),

(9, 'Kyoto Imperial Temples', 'Kyoto', 'Japan', 'Asia',
 'https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80',
 'Ancient Japanese capital of thousands of vermilion torii gates, Zen stone gardens, bamboo groves, and traditional geisha districts.',
 'Cultural,Historical,Nature,Romantic,Foodie', 160.00, 'JPY', 35.0116, 135.7681, 4.96, 1890,
 'Fushimi Inari Shrine, Arashiyama Bamboo Grove, Kinkaku-ji Golden Pavilion, Gion District, Kiyomizu-dera', 'March - May & October - November', true),

(10, 'Dubai Desert & Luxury Oasis', 'Dubai', 'United Arab Emirates', 'Asia',
 'https://images.unsplash.com/photo-1512453979798-5ea266f8880c?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1518684079-3c830dcef090?auto=format&fit=crop&w=800&q=80',
 'Futuristic skyline towering above Arabian golden dunes with luxury resorts, mega malls, and world-record marvels.',
 'Luxury,Modern,Urban,Adventure,Shopping', 270.00, 'AED', 25.2048, 55.2708, 4.92, 2100,
 'Burj Khalifa, Dubai Mall & Fountain, Palm Jumeirah, Desert Dune Safari, Museum of the Future', 'November - March', true),

(11, 'Santorini Aegean Sunsets', 'Santorini', 'Greece', 'Europe',
 'https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1533105079780-92b9be482077?auto=format&fit=crop&w=800&q=80',
 'Whitewashed cliffside villas, cobalt-blue domes, volcanic vineyards, and world-famous golden sunsets over the Aegean caldera.',
 'Romantic,Scenic,Luxury,Beach,Relaxed', 250.00, 'EUR', 36.3932, 25.4615, 4.94, 1750,
 'Oia Sunset Viewpoint, Fira to Oia Caldera Hike, Red Beach, Akrotiri Ruins, Ammoudi Bay', 'May - October', true),

(12, 'Barcelona & Catalan Art', 'Barcelona', 'Spain', 'Europe',
 'https://images.unsplash.com/photo-1583422409516-2895a77efded?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1539037116277-4db20889f2d4?auto=format&fit=crop&w=800&q=80',
 'Gaudí architectural wonderland, Mediterranean beach promenades, tapas bars, and lively Gothic Quarter alleyways.',
 'Cultural,Art,Foodie,Beach,Architecture', 175.00, 'EUR', 41.3879, 2.1699, 4.88, 2800,
 'Sagrada Família, Park Güell, Casa Batlló, Gothic Quarter, Barceloneta Beach', 'May - June & September - October', true),

(13, 'Swiss Alps & Interlaken', 'Interlaken & Zermatt', 'Switzerland', 'Europe',
 'https://images.unsplash.com/photo-1530122037265-a5f1f91d3b99?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80',
 'Snowcapped Alpine peaks, turquoise glacial lakes, scenic cogwheel mountain trains, and picturesque chalet villages.',
 'Mountain,Scenic,Adventure,Nature,Luxury', 290.00, 'CHF', 46.6863, 7.8632, 4.97, 1420,
 'Jungfraujoch Top of Europe, Matterhorn Glacier Paradise, Lake Brienz, Lauterbrunnen Valley, Grindelwald First', 'December - March (Ski) & June - September (Hiking)', true),

(14, 'Sydney Harbour & Beaches', 'Sydney', 'Australia', 'Oceania',
 'https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1523482580672-f109ba8cb9be?auto=format&fit=crop&w=800&q=80',
 'Iconic Opera House sails, sun-kissed Bondi surf, glittering harbor cruises, and coastal cliffside walks.',
 'Beach,Urban,Scenic,Modern,Adventure', 220.00, 'AUD', -33.8688, 151.2093, 4.89, 1650,
 'Sydney Opera House, Sydney Harbour Bridge Climb, Bondi to Coogee Coastal Walk, Manly Ferry, Taronga Zoo', 'September - November & March - May', true),

(15, 'Cairo & Giza Pyramids', 'Cairo', 'Egypt', 'Africa',
 'https://images.unsplash.com/photo-1503177119275-0aa32b3a9368?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1539650116574-8efeb43e2750?auto=format&fit=crop&w=800&q=80',
 '5,000 years of civilization, the Great Pyramids of Giza, the mysterious Sphinx, bustling Khan el-Khalili souks, and Nile cruises.',
 'Historical,Cultural,Adventure,Photography', 95.00, 'EGP', 30.0444, 31.2357, 4.85, 2300,
 'Great Pyramid of Giza & Sphinx, Grand Egyptian Museum, Khan el-Khalili Bazaar, Nile River Felucca Ride', 'October - April', true),

(16, 'Cape Town Coastal Majesty', 'Cape Town', 'South Africa', 'Africa',
 'https://images.unsplash.com/photo-1580618672591-eb180b1a973f?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1576485290814-1c72aa4bbb8e?auto=format&fit=crop&w=800&q=80',
 'A spectacular melting pot of rugged ocean cliffs, iconic Table Mountain peaks, wine valleys, penguin colonies, and vibrant culture.',
 'Adventure,Scenic,Foodie,Wildlife,Nature', 110.00, 'ZAR', -33.9249, 18.4241, 4.86, 740,
 'Table Mountain Cable Car, Cape Point & Cape of Good Hope, Boulders Penguin Beach, Stellenbosch Wine Route, Camps Bay', 'November - March', true),

(17, 'Rio de Janeiro & Copacabana', 'Rio de Janeiro', 'Brazil', 'South America',
 'https://images.unsplash.com/photo-1483729558449-99ef09a8c325?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1516306580123-e6e52b1b7b5f?auto=format&fit=crop&w=800&q=80',
 'Breathtaking seaside metropolis with Christ the Redeemer atop Corcovado, Sugarloaf Mountain cable cars, and samba beats.',
 'Beach,Cultural,Scenic,Adventure,Nightlife', 120.00, 'BRL', -22.9068, -43.1729, 4.84, 1490,
 'Christ the Redeemer, Sugarloaf Mountain, Copacabana Beach, Ipanema Beach, Selarón Steps', 'December - March', true),

(18, 'Machu Picchu & Sacred Valley', 'Cusco & Machu Picchu', 'Peru', 'South America',
 'https://images.unsplash.com/photo-1526392060635-9d6019884377?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1589802829985-817e51171b92?auto=format&fit=crop&w=800&q=80',
 'The legendary 15th-century Inca citadel nestled high in the Andean cloud forest, surrounded by mystical mountain peaks.',
 'Historical,Adventure,Mountain,Nature,Cultural', 135.00, 'PEN', -13.1631, -72.5450, 4.98, 2600,
 'Machu Picchu Citadel, Huayna Picchu Climb, Sacred Valley of the Incas, Cusco Historic Plaza, Rainbow Mountain', 'May - September', true),

(19, 'Bangkok Temples & Street Food', 'Bangkok', 'Thailand', 'Asia',
 'https://images.unsplash.com/photo-1508009603885-50cf7c579365?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1563492065599-3520f775eeed?auto=format&fit=crop&w=800&q=80',
 'Vibrant Thai capital of shimmering golden palaces, buzzing night markets, Michelin-starred street stalls, and Chao Phraya river life.',
 'Foodie,Culture,Historical,Urban,Nightlife', 75.00, 'THB', 13.7563, 100.5018, 4.86, 3100,
 'Grand Palace & Emerald Buddha, Wat Arun (Temple of Dawn), Chatuchak Weekend Market, Chinatown Food Street, Floating Markets', 'November - February', true),

(20, 'Singapore Futuristic Garden City', 'Singapore', 'Singapore', 'Asia',
 'https://images.unsplash.com/photo-1525625293386-3f8f99389edd?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1506351421178-63b52a2d2562?auto=format&fit=crop&w=800&q=80',
 'Ultra-modern metropolis blending futuristic Supertrees, Marina Bay infinity views, lush botanic gardens, and multicultural hawker centers.',
 'Modern,Foodie,Urban,Nature,Luxury', 210.00, 'SGD', 1.3521, 103.8198, 4.91, 2400,
 'Gardens by the Bay, Marina Bay Sands SkyPark, Jewel Changi Rain Vortex, Sentosa Island, Lau Pa Sat Hawker Market', 'Year-round (Best: November - January)', true),

(21, 'Maui & Hawaiian Tropical Islands', 'Maui & Honolulu', 'United States', 'North America',
 'https://images.unsplash.com/photo-1542259009477-d625272157b7?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80',
 'Polynesian paradise of golden sand beaches, Road to Hana waterfalls, Haleakala volcano sunrises, and sea turtle snorkeling.',
 'Beach,Nature,Adventure,Relaxed,Romantic', 265.00, 'USD', 20.7984, -156.3319, 4.94, 1800,
 'Haleakala Crater Sunrise, Road to Hana, Kaanapali Beach, Molokini Crater Snorkel, Wailea Sunset', 'April - May & September - November', true),

(22, 'Amsterdam Canals & Art', 'Amsterdam', 'Netherlands', 'Europe',
 'https://images.unsplash.com/photo-1512470876302-972faa2aa9a4?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1534351590666-13e3e96b5017?auto=format&fit=crop&w=800&q=80',
 'Charming 17th-century canal rings, Van Gogh and Rembrandt masterpieces, bicycle-lined bridges, and historic townhouse architecture.',
 'Cultural,Art,Historical,Urban,Romantic', 195.00, 'EUR', 52.3676, 4.9041, 4.87, 2150,
 'Rijksmuseum, Van Gogh Museum, Anne Frank House, Jordaan Canal District, Keukenhof Tulip Gardens', 'April - May & September - November', true),

(23, 'Venice Floating Romance', 'Venice', 'Italy', 'Europe',
 'https://images.unsplash.com/photo-1514890547357-a9ee288728e0?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1523906834658-6e24ef2386f9?auto=format&fit=crop&w=800&q=80',
 'Timeless floating sanctuary built upon 118 islands, connected by stone bridges, gondola-gliding canals, and Venetian Gothic palaces.',
 'Romantic,Historical,Art,Scenic,Cultural', 235.00, 'EUR', 45.4408, 12.3155, 4.89, 1920,
 'Piazza San Marco & Basilica, Doge''s Palace, Grand Canal Gondola Ride, Rialto Bridge, Burano Colorful Island', 'April - June & September - October', true),

(24, 'Prague Bohemian Splendor', 'Prague', 'Czech Republic', 'Europe',
 'https://images.unsplash.com/photo-1541849546-216549ae216d?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1519671482749-fd09be7ccebf?auto=format&fit=crop&w=800&q=80',
 'The City of a Hundred Spires featuring fairy-tale gothic castles, the medieval Astronomical Clock, and cobblestone charm.',
 'Historical,Romantic,Culture,Architecture', 115.00, 'CZK', 50.0755, 14.4378, 4.88, 1780,
 'Charles Bridge at Dawn, Prague Castle & St. Vitus, Old Town Square & Astronomical Clock, Petrin Hill', 'May - September', true),

(25, 'Marrakech & Sahara Wonders', 'Marrakech', 'Morocco', 'Africa',
 'https://images.unsplash.com/photo-1597212618440-806262de4f6b?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1548013146-72479768bada?auto=format&fit=crop&w=800&q=80',
 'Vibrant North African jewel of labyrinthine spice medinas, cobalt Majorelle gardens, luxury riads, and golden desert stargazing.',
 'Cultural,Adventure,Historical,Foodie,Romantic', 90.00, 'MAD', 31.6295, -7.9811, 4.83, 1430,
 'Jemaa el-Fnaa Square, Jardin Majorelle, Bahia Palace, Koutoubia Mosque, Sahara Desert Glamping Tour', 'March - May & September - November', true),

(26, 'Banff & Canadian Rockies', 'Banff & Lake Louise', 'Canada', 'North America',
 'https://images.unsplash.com/photo-1503614472-8c93d56e92ce?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1517411032315-54ef2cb783bb?auto=format&fit=crop&w=800&q=80',
 'Emerald glacial lakes, towering limestone Rockies peaks, wild grizzly habitats, and pristine wilderness hiking trails.',
 'Nature,Mountain,Scenic,Adventure,Photography', 210.00, 'CAD', 51.1784, -115.5708, 4.96, 1560,
 'Lake Louise, Moraine Lake & Valley of Ten Peaks, Icefields Parkway, Banff Gondola, Johnston Canyon', 'June - September (Hiking) & December - April (Ski)', true),

(27, 'Queenstown & Southern Alps', 'Queenstown', 'New Zealand', 'Oceania',
 'https://images.unsplash.com/photo-1507699622108-4be3abd695ad?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1589871134015-38102a0a2dfa?auto=format&fit=crop&w=800&q=80',
 'The Adventure Capital of the World, framed by dramatic Lake Wakatipu, The Remarkables mountain range, and fjord cruises.',
 'Adventure,Mountain,Scenic,Nature,Wine', 195.00, 'NZD', -45.0312, 168.6626, 4.95, 1290,
 'Milford Sound Cruise, Skyline Gondola & Luge, Lake Wakatipu Cruise, Shotover River Jet, Cardrona Alpine Resort', 'December - February (Summer) & June - August (Ski)', true),

(28, 'Maldives Overwater Sanctuary', 'Male & North Ari Atoll', 'Maldives', 'Asia',
 'https://images.unsplash.com/photo-1514282401047-d79a71a590e8?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80',
 'Pure turquoise lagoons, secluded private overwater bungalows, manta ray coral reefs, and world-class barefoot luxury.',
 'Beach,Luxury,Romantic,Relaxed,Wellness', 380.00, 'USD', 3.2028, 73.2207, 4.97, 1180,
 'Overwater Villa Villa Stays, Manta Ray & Whale Shark Snorkel, Sunset Dolphin Cruise, Sandbank Dining', 'November - April', true),

(29, 'Agra & Taj Mahal Heritage', 'Agra', 'India', 'Asia',
 'https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1548013146-72479768bada?auto=format&fit=crop&w=800&q=80',
 'Home of the Taj Mahal — the world''s most breathtaking ivory-white marble monument to eternal love and Mughal architecture.',
 'Historical,Culture,Romantic,Architecture', 65.00, 'INR', 27.1767, 78.0081, 4.93, 3800,
 'Taj Mahal Sunrise View, Agra Fort, Mehtab Bagh Gardens, Fatehpur Sikri', 'October - March', true),

(30, 'Petra Ancient Rose City', 'Wadi Musa & Petra', 'Jordan', 'Asia',
 'https://images.unsplash.com/photo-1579606032834-d922576b97b0?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1548013146-72479768bada?auto=format&fit=crop&w=800&q=80',
 'One of the New 7 Wonders of the World — an ancient Nabataean metropolis hand-carved into pink sandstone canyon cliffs.',
 'Historical,Adventure,Cultural,Photography', 140.00, 'JOD', 30.3285, 35.4444, 4.96, 1650,
 'The Treasury (Al-Khazneh), The Siq Gorge, The Monastery (Ad Deir), Petra by Night, High Place of Sacrifice', 'March - May & September - November', true),

(31, 'Seoul K-Culture & Palaces', 'Seoul', 'South Korea', 'Asia',
 'https://images.unsplash.com/photo-1538485399081-7191377e8241?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1517154421773-0529f29ea451?auto=format&fit=crop&w=800&q=80',
 'Dynamic epicenter of K-pop, cutting-edge technology, Hanok heritage villages, royal Joseon palaces, and Korean BBQ.',
 'Modern,Foodie,Culture,Shopping,Nightlife', 145.00, 'KRW', 37.5665, 126.9780, 4.90, 2100,
 'Gyeongbokgung Palace, Bukchon Hanok Village, Myeongdong Street Market, N Seoul Tower, Hongdae Youth District', 'March - May & September - November', true),

(32, 'Istanbul Crossroads of Continents', 'Istanbul', 'Turkey', 'Europe',
 'https://images.unsplash.com/photo-1524231757912-21f4fe3a7200?auto=format&fit=crop&w=1600&q=80',
 'https://images.unsplash.com/photo-1541432901042-2d8bd64b4a9b?auto=format&fit=crop&w=800&q=80',
 'Where East meets West across the Bosphorus Strait — glorious Byzantine basilicas, Ottoman minarets, and the Grand Bazaar.',
 'Historical,Cultural,Foodie,Architecture', 105.00, 'TRY', 41.0082, 28.9784, 4.91, 2950,
 'Hagia Sophia, Blue Mosque, Topkapi Palace, Bosphorus Sunset Cruise, Grand Bazaar & Spice Market', 'April - May & September - November', true)
ON CONFLICT (id) DO NOTHING;

-- 3. Seed Activities
INSERT INTO activities (id, destination_id, name, category, description, duration_hours, price, currency, rating, review_count, image_url, address, opening_hours, booking_required)
VALUES
(1, 1, 'Shibuya Sky & Harajuku Culture Walk', 'CULTURE', 'Panoramic 360-degree observation deck followed by a guided immersion into Takeshita street fashion and Meiji Shrine.', 3.5, 45.00, 'USD', 4.9, 320, 'https://images.unsplash.com/photo-1542051841857-5f90071e7989?auto=format&fit=crop&w=600&q=80', '2 Chome-24-12 Shibuya, Tokyo', '10:00 - 22:30', true),
(2, 1, 'Tsukiji Outer Market Food Safari', 'CULTURE', 'Taste fresh sashimi, tamagoyaki, wagyu skewers, and matcha sweets with a local chef guide.', 2.5, 65.00, 'USD', 4.95, 410, 'https://images.unsplash.com/photo-1553621042-f6e147245754?auto=format&fit=crop&w=600&q=80', '4 Chome-16-2 Tsukiji, Chuo City, Tokyo', '08:00 - 14:00', false),
(3, 1, 'TeamLab Planets Immersive Digital Art', 'MUSEUM', 'Walk through water and interactive crystal universes in this world-famous digital museum.', 2.0, 38.00, 'USD', 4.88, 890, 'https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=600&q=80', '6 Chome-1-16 Toyosu, Koto City, Tokyo', '09:00 - 22:00', true),
(4, 2, 'Louvre Museum Masterpieces with Art Historian', 'MUSEUM', 'Skip-the-line VIP access to the Mona Lisa, Venus de Milo, and Winged Victory with in-depth stories.', 3.0, 75.00, 'USD', 4.92, 620, 'https://images.unsplash.com/photo-1499856871958-5b9627545d1a?auto=format&fit=crop&w=600&q=80', 'Rue de Rivoli, 75001 Paris', '09:00 - 18:00', true),
(5, 2, 'Sunset Champagne Seine River Cruise', 'SIGHTSEEING', 'Glaze through illuminated Parisian bridges and the sparkling Eiffel Tower with French champagne.', 1.5, 35.00, 'USD', 4.85, 950, 'https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?auto=format&fit=crop&w=600&q=80', 'Port de la Bourdonnais, 75007 Paris', '18:00 - 23:00', true),
(6, 3, 'Mount Batur Sunrise Trek & Hot Springs', 'ADVENTURE', 'Early morning volcanic hike with breathtaking caldera sunrise breakfast and thermal spring soak.', 6.0, 55.00, 'USD', 4.91, 540, 'https://images.unsplash.com/photo-1518548419970-58e3b4079ab2?auto=format&fit=crop&w=600&q=80', 'Kintamani, Bangli Regency, Bali', '03:00 - 12:00', true),
(7, 6, 'Colosseum Underground & Gladiators Arena', 'HISTORICAL', 'Exclusive VIP underground tunnel access and Roman Forum exploration with archaeologist guide.', 3.0, 68.00, 'USD', 4.94, 1120, 'https://images.unsplash.com/photo-1552832230-c0197dd311b5?auto=format&fit=crop&w=600&q=80', 'Piazza del Colosseo, 1, 00184 Roma RM', '08:30 - 19:00', true),
(8, 7, 'Summit One Vanderbilt & High Line Sunset', 'SIGHTSEEING', 'Multisensory glass skydeck views overlooking Manhattan, followed by a scenic High Line park walk.', 2.5, 48.00, 'USD', 4.93, 1430, 'https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?auto=format&fit=crop&w=600&q=80', '45 E 42nd St, New York, NY 10017', '09:00 - 00:00', true),
(9, 18, 'Machu Picchu Citadel Sunrise Guided Tour', 'HISTORICAL', 'Witness sunrise over the lost city of the Incas with expert Andean cultural interpretation.', 4.0, 85.00, 'USD', 4.99, 2100, 'https://images.unsplash.com/photo-1526392060635-9d6019884377?auto=format&fit=crop&w=600&q=80', 'Machu Picchu Sanctuary, Aguas Calientes, Cusco', '06:00 - 17:30', true),
(10, 29, 'Taj Mahal Sunrise & Mughal Heritage Walk', 'HISTORICAL', 'Golden hour sunrise photography tour of the Taj Mahal followed by Mughal craft demonstration.', 3.5, 30.00, 'USD', 4.96, 1800, 'https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=600&q=80', 'Dharmapuri, Forest Colony, Tajganj, Agra, Uttar Pradesh', '06:00 - 18:30', true)
ON CONFLICT (id) DO NOTHING;

-- 4. Seed Restaurants
INSERT INTO restaurants (id, destination_id, name, cuisine_type, price_range, rating, review_count, address, opening_hours, specialties, image_url, reservation_url)
VALUES
(1, 1, 'Afuri Ramen & Craft Brews', 'Japanese Ramen', '$$', 4.8, 480, '1 Chome-1-7 Ebisu, Shibuya City, Tokyo', '11:00 - 23:00', 'Yuzu Shio Ramen, Charcoal Grilled Pork, Vegan Ramen', 'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=600&q=80', 'https://afuri.com'),
(2, 1, 'Sushi Dai Omakase', 'Sushi & Seafood', '$$$$', 4.95, 310, 'Toyosu Market Block 6, Koto City, Tokyo', '06:00 - 14:00', 'Otoro, Uni, Botan Ebi, Fresh Daily Catch', 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?auto=format&fit=crop&w=600&q=80', 'https://sushidai.jp'),
(3, 2, 'Le Coupe-Chou Latin Quarter', 'Traditional French', '$$$', 4.7, 340, '11 Rue de Laveau-le-Vicomte, 75005 Paris', '12:00 - 23:00', 'Boeuf Bourguignon, Duck Confit, Soufflé au Chocolat', 'https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=600&q=80', 'https://lecoupechou.com'),
(4, 3, 'Locavore Herbivore Ubud', 'Modern Indonesian Farm-to-Table', '$$$', 4.9, 290, 'Jl. Dewi Sita No.10, Ubud, Bali', '12:00 - 22:00', 'Smoked Heirloom Tomatoes, Bali Truffle Rice, Fermented Mango Glaze', 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=600&q=80', 'https://locavorenext.com'),
(5, 6, 'Trattoria Da Enzo al 29', 'Roman Classic', '$$', 4.88, 520, 'Via dei Vascellari, 29, 00153 Roma RM', '12:30 - 23:00', 'Cacio e Pepe, Carbonara, Artichokes Alla Giudia, Tiramisu', 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=600&q=80', 'https://daenzoal29.com'),
(6, 7, 'Gramercy Tavern Manhattan', 'Contemporary American', '$$$$', 4.91, 680, '42 E 20th St, New York, NY 10003', '11:30 - 23:00', 'Wood-Fired Duck Breast, Hand-rolled Pasta, Roasted Sea Bass', 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=600&q=80', 'https://gramercytavern.com')
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
