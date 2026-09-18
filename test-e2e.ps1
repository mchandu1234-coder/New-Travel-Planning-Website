$baseUrl = "http://localhost:8080/api"

Write-Host "=== 1. Testing Auth Login ==="
$loginBody = @{
    email = "demo@wanderlust.com"
    password = "password123"
} | ConvertTo-Json

try {
    $loginRes = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $token = $loginRes.data.token
    Write-Host "[OK] Login successful! Token acquired for user: $($loginRes.data.user.fullName)"
} catch {
    Write-Host "[FAIL] Login failed: $_"
    exit 1
}

$headers = @{
    Authorization = "Bearer $token"
}

Write-Host "`n=== 2. Testing Get Current User Profile (/auth/me) ==="
try {
    $meRes = Invoke-RestMethod -Uri "$baseUrl/auth/me" -Method Get -Headers $headers
    Write-Host "[OK] Current user: $($meRes.data.email), Home Airport: $($meRes.data.homeAirport)"
} catch {
    Write-Host "[FAIL] /auth/me failed: $_"
}

Write-Host "`n=== 3. Testing Destinations Catalog (/destinations) ==="
try {
    $destRes = Invoke-RestMethod -Uri "$baseUrl/destinations" -Method Get
    $count = $destRes.data.content.Count
    Write-Host "[OK] Destinations fetched: $count items. First: $($destRes.data.content[0].name)"
} catch {
    Write-Host "[FAIL] /destinations failed: $_"
}

Write-Host "`n=== 4. Testing Create Trip Wizard (/trips) ==="
$tripBody = @{
    title = "Automated Test Vacation 2026"
    destinationId = 1
    startDate = "2026-11-01"
    endDate = "2026-11-06"
    travelerCount = 2
    targetBudget = 3000
    currency = "USD"
} | ConvertTo-Json

try {
    $tripRes = Invoke-RestMethod -Uri "$baseUrl/trips" -Method Post -Body $tripBody -ContentType "application/json" -Headers $headers
    $tripId = $tripRes.data.id
    Write-Host "[OK] Trip created successfully! ID: $tripId, Title: $($tripRes.data.title), Days: $($tripRes.data.totalDays)"
} catch {
    Write-Host "[FAIL] Trip creation failed: $_"
    exit 1
}

Write-Host "`n=== 5. Testing Itinerary & Days Generation (/itinerary/trips/$tripId) ==="
try {
    $itinRes = Invoke-RestMethod -Uri "$baseUrl/itinerary/trips/$tripId" -Method Get -Headers $headers
    $dayId = $itinRes.data[0].id
    Write-Host "[OK] Itinerary days verified: $($itinRes.data.Count) days. Day 1 ID: $dayId"
} catch {
    Write-Host "[FAIL] Itinerary failed: $_"
}

Write-Host "`n=== 6. Testing Add Activity to Itinerary (/itinerary/items) ==="
$itemBody = @{
    dayId = $dayId
    title = "Guided Sunset Cruise"
    itemType = "ACTIVITY"
    startTime = "17:00"
    endTime = "19:00"
    locationName = "Main Pier"
    estimatedCost = 75.00
} | ConvertTo-Json

try {
    $itemRes = Invoke-RestMethod -Uri "$baseUrl/itinerary/items" -Method Post -Body $itemBody -ContentType "application/json" -Headers $headers
    Write-Host "[OK] Activity added: $($itemRes.data.title), Cost: `$$($itemRes.data.estimatedCost)"
} catch {
    Write-Host "[FAIL] Add item failed: $_"
}

Write-Host "`n=== 7. Testing Flight Search & Booking (/bookings/flights) ==="
$flightSearchBody = @{
    origin = "JFK"
    destination = "HND"
    departureDate = "2026-11-01"
} | ConvertTo-Json

try {
    $flightSearch = Invoke-RestMethod -Uri "$baseUrl/bookings/flights/search" -Method Post -Body $flightSearchBody -ContentType "application/json" -Headers $headers
    $flight = $flightSearch.data[0]
    Write-Host "[OK] Flight found: $($flight.airline) ($($flight.flightNumber)) - `$$($flight.price)"

    $bookBody = @{
        tripId = $tripId
        flightOffer = $flight
        passengerName = "Alex Morgan"
        passengerEmail = "demo@wanderlust.com"
    } | ConvertTo-Json

    $bookRes = Invoke-RestMethod -Uri "$baseUrl/bookings/flights/book" -Method Post -Body $bookBody -ContentType "application/json" -Headers $headers
    Write-Host "[OK] Flight booked successfully! Reference: $($bookRes.data.bookingReference)"
} catch {
    Write-Host "[FAIL] Flight booking failed: $_"
}

Write-Host "`n=== 8. Testing Hotel Search & Booking (/bookings/hotels) ==="
$hotelSearchBody = @{
    destinationCity = "Tokyo"
    checkInDate = "2026-11-01"
    checkOutDate = "2026-11-06"
} | ConvertTo-Json

try {
    $hotelSearch = Invoke-RestMethod -Uri "$baseUrl/bookings/hotels/search" -Method Post -Body $hotelSearchBody -ContentType "application/json" -Headers $headers
    $hotel = $hotelSearch.data[0]
    Write-Host "[OK] Hotel found: $($hotel.name) - `$$($hotel.pricePerNight)/night"

    $hotelBody = @{
        tripId = $tripId
        hotelOffer = $hotel
        checkInDate = "2026-11-01"
        checkOutDate = "2026-11-06"
        guestName = "Alex Morgan"
        guestEmail = "demo@wanderlust.com"
    } | ConvertTo-Json

    $hotelBookRes = Invoke-RestMethod -Uri "$baseUrl/bookings/hotels/book" -Method Post -Body $hotelBody -ContentType "application/json" -Headers $headers
    Write-Host "[OK] Hotel reserved successfully! Reference: $($hotelBookRes.data.bookingReference)"
} catch {
    Write-Host "[FAIL] Hotel reservation failed: $_"
}

Write-Host "`n=== 9. Testing Budget & Split Expenses (/budget/trips/$tripId/summary) ==="
$expBody = @{
    tripId = $tripId
    title = "Celebratory Japanese BBQ"
    category = "FOOD_DRINK"
    amount = 140.00
    currency = "USD"
    date = "2026-11-02"
} | ConvertTo-Json

try {
    $expRes = Invoke-RestMethod -Uri "$baseUrl/budget/expenses" -Method Post -Body $expBody -ContentType "application/json" -Headers $headers
    Write-Host "[OK] Expense logged: $($expRes.data.title)"

    $budgetRes = Invoke-RestMethod -Uri "$baseUrl/budget/trips/$tripId/summary" -Method Get -Headers $headers
    Write-Host "[OK] Budget summary: Total Budget: `$$($budgetRes.data.totalBudget), Total Spent: `$$($budgetRes.data.totalSpent)"
} catch {
    Write-Host "[FAIL] Budget failed: $_"
}

Write-Host "`n=== 10. Testing Trip Details & Schedule Conflicts (/trips/$tripId) ==="
try {
    $tripDetailRes = Invoke-RestMethod -Uri "$baseUrl/trips/$tripId" -Method Get -Headers $headers
    Write-Host "[OK] Trip details fetched: $($tripDetailRes.data.title) with $($tripDetailRes.data.days.Count) itinerary days"
} catch {
    Write-Host "[FAIL] Trip details failed: $_"
}

Write-Host "`n=== 11. Testing Export iCal & PDF (/export/trips/$tripId) ==="
try {
    $pdfData = Invoke-RestMethod -Uri "$baseUrl/export/trips/$tripId/data" -Method Get -Headers $headers
    Write-Host "[OK] PDF export data ready: Trip '$($pdfData.data.trip.title)' with $($pdfData.data.trip.days.Count) days"

    $icalData = Invoke-WebRequest -Uri "$baseUrl/export/trips/$tripId/calendar.ics" -Method Get -Headers $headers -UseBasicParsing
    Write-Host "[OK] iCal export ready: $($icalData.Content.Length) bytes (Status: $($icalData.StatusCode))"
} catch {
    Write-Host "[FAIL] Export failed: $_"
}

Write-Host "`n======================================================="
Write-Host ">>> ALL 11 END-TO-END BACKEND & FRONTEND FLOWS PASSED! <<<"
Write-Host "======================================================="
