# WanderLust — Next-Gen AI Travel Planning Suite

WanderLust is a full-stack, enterprise-grade travel planning web application built with **Spring Boot 3.3.4 (Java 17/24)** on the backend and **React 18 + Tailwind CSS** on the frontend.

## 🌟 Key Features

1. **Destination Explorer & Live Weather Widget**:
   - Discover destinations filtered by continent, vibe (tropical, cultural, urban, mountain), price range, and ratings.
   - Interactive live weather widget supporting °C/°F switching, humidity, wind, and 5-day forecasts.
2. **Multi-Step Trip Creator Wizard**:
   - 4-step guided wizard for setting trip destination, start/end dates, group size, budget targets, and preferred currency.
3. **Interactive Day-by-Day Itinerary Builder**:
   - Timeline planning for flights, hotel check-ins, attractions, dining, and sightseeing.
   - Real-time **schedule conflict detection** alerting travelers to overlapping times and tight transit buffers.
4. **Flight & Hotel Booking Hub**:
   - Search flights and hotels with price and rating filters.
   - 1-click booking checkout simulated via a modal, automatically generating itinerary slots and logging budget expenses.
5. **Multi-Currency Budget Tracker & Debt Settlement**:
   - Expense logger with real-time currency conversion (`USD`, `EUR`, `GBP`, `JPY`, `INR`).
   - Category expense breakdown visualization via interactive Recharts.
   - **Who Owes Whom (Simplified Debt Settlement)** graph algorithm to optimize split-expense reimbursements.
6. **Real-Time WebSocket Collaboration Room**:
   - Live chat stream powered by STOMP over WebSocket (`/ws-travel`).
   - Collaborator role management (`ADMIN`, `EDITOR`, `VIEWER`).
7. **Export & Sharing Suite**:
   - Printable PDF document preview.
   - One-click `.ics` iCalendar export compatible with Apple Calendar, Google Calendar, and Outlook.
   - Instant shareable link and live QR code generator.

---

## 🛠 Tech Stack

- **Backend**: Java 17+, Spring Boot 3.3.4, Spring Data JPA, Spring Security, Spring WebSocket (STOMP), Flyway, JJWT, H2 (dev) / PostgreSQL (prod), Redis/Caffeine caching, SpringDoc OpenAPI (Swagger UI).
- **Frontend**: React 18, Vite, Tailwind CSS, Lucide Icons, Recharts, StompJS, SockJS, Canvas Confetti, QRCode.
- **DevOps**: Maven, npm, Docker & Docker Compose.

---

## 🚀 Quick Start Guide

### Option 1: Local Development (H2 In-Memory DB)

#### 1. Start Spring Boot Backend
```bash
cd backend
C:\Maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run
```
- OpenAPI / Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- Initial seed data automatically populates on startup.

#### 2. Start React Frontend
```bash
cd frontend
npm run dev
```
- Open application at: `http://localhost:5173`
- Use the **One-Click Demo Explorer Access** button on the login modal to log in immediately with `demo@wanderlust.com`.

---

### Option 2: Docker Compose (PostgreSQL + Redis + Backend + Frontend)

```bash
docker-compose up --build
```
- Frontend app: `http://localhost`
- Backend REST API: `http://localhost:8080/api`

