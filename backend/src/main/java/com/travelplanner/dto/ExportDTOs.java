package com.travelplanner.dto;

import java.util.List;

public class ExportDTOs {

    public static class TripExportDataDTO {
        private TripDTOs.TripDetailResponse trip;
        private List<BookingDTOs.FlightOfferDTO> bookedFlights;
        private List<BookingDTOs.HotelOfferDTO> bookedAccommodations;
        private BudgetDTOs.BudgetSummaryDTO budget;
        private List<BudgetDTOs.ExpenseResponse> expenses;
        private String generatedAt;
        private String shareUrl;
        private String emergencyContacts;

        public TripExportDataDTO() {}
        public static TripExportDataDTOBuilder builder() { return new TripExportDataDTOBuilder(); }

        public TripDTOs.TripDetailResponse getTrip() { return trip; }
        public void setTrip(TripDTOs.TripDetailResponse trip) { this.trip = trip; }
        public List<BookingDTOs.FlightOfferDTO> getBookedFlights() { return bookedFlights; }
        public void setBookedFlights(List<BookingDTOs.FlightOfferDTO> bookedFlights) { this.bookedFlights = bookedFlights; }
        public List<BookingDTOs.HotelOfferDTO> getBookedAccommodations() { return bookedAccommodations; }
        public void setBookedAccommodations(List<BookingDTOs.HotelOfferDTO> bookedAccommodations) { this.bookedAccommodations = bookedAccommodations; }
        public BudgetDTOs.BudgetSummaryDTO getBudget() { return budget; }
        public void setBudget(BudgetDTOs.BudgetSummaryDTO budget) { this.budget = budget; }
        public List<BudgetDTOs.ExpenseResponse> getExpenses() { return expenses; }
        public void setExpenses(List<BudgetDTOs.ExpenseResponse> expenses) { this.expenses = expenses; }
        public String getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }
        public String getShareUrl() { return shareUrl; }
        public void setShareUrl(String shareUrl) { this.shareUrl = shareUrl; }
        public String getEmergencyContacts() { return emergencyContacts; }
        public void setEmergencyContacts(String emergencyContacts) { this.emergencyContacts = emergencyContacts; }

        public static class TripExportDataDTOBuilder {
            private TripDTOs.TripDetailResponse trip;
            private List<BookingDTOs.FlightOfferDTO> bookedFlights;
            private List<BookingDTOs.HotelOfferDTO> bookedAccommodations;
            private BudgetDTOs.BudgetSummaryDTO budget;
            private List<BudgetDTOs.ExpenseResponse> expenses;
            private String generatedAt;
            private String shareUrl;
            private String emergencyContacts;

            public TripExportDataDTOBuilder trip(TripDTOs.TripDetailResponse trip) { this.trip = trip; return this; }
            public TripExportDataDTOBuilder bookedFlights(List<BookingDTOs.FlightOfferDTO> bookedFlights) { this.bookedFlights = bookedFlights; return this; }
            public TripExportDataDTOBuilder bookedAccommodations(List<BookingDTOs.HotelOfferDTO> bookedAccommodations) { this.bookedAccommodations = bookedAccommodations; return this; }
            public TripExportDataDTOBuilder budget(BudgetDTOs.BudgetSummaryDTO budget) { this.budget = budget; return this; }
            public TripExportDataDTOBuilder expenses(List<BudgetDTOs.ExpenseResponse> expenses) { this.expenses = expenses; return this; }
            public TripExportDataDTOBuilder generatedAt(String generatedAt) { this.generatedAt = generatedAt; return this; }
            public TripExportDataDTOBuilder shareUrl(String shareUrl) { this.shareUrl = shareUrl; return this; }
            public TripExportDataDTOBuilder emergencyContacts(String emergencyContacts) { this.emergencyContacts = emergencyContacts; return this; }

            public TripExportDataDTO build() {
                TripExportDataDTO dto = new TripExportDataDTO();
                dto.trip = trip; dto.bookedFlights = bookedFlights; dto.bookedAccommodations = bookedAccommodations;
                dto.budget = budget; dto.expenses = expenses; dto.generatedAt = generatedAt;
                dto.shareUrl = shareUrl; dto.emergencyContacts = emergencyContacts;
                return dto;
            }
        }
    }
}
