import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { WebSocketProvider } from './context/WebSocketContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import FloatingQuickDock from './components/FloatingQuickDock';

// Pages
import HomePage from './pages/HomePage';
import DestinationsPage from './pages/DestinationsPage';
import DestinationDetailPage from './pages/DestinationDetailPage';
import TripWizardPage from './pages/TripWizardPage';
import TripDashboardPage from './pages/TripDashboardPage';
import ItineraryBuilderPage from './pages/ItineraryBuilderPage';
import BookingsHubPage from './pages/BookingsHubPage';
import ActivitiesPage from './pages/ActivitiesPage';
import BudgetTrackerPage from './pages/BudgetTrackerPage';
import CollaborationRoomPage from './pages/CollaborationRoomPage';
import ExportSharePage from './pages/ExportSharePage';

// My Trips Dashboard Page
import { tripApi } from './api/client';
import { Calendar, MapPin, ArrowRight, Plus } from 'lucide-react';

function Dashboard() {
  const [trips, setTrips] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    tripApi.getMyTrips()
      .then((res) => {
        if (res.data.success) setTrips(res.data.data);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen bg-[#f7faf9] flex justify-center items-center">
        <div className="w-10 h-10 border-4 border-teal-600 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-mesh text-slate-900 py-10 pb-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        <div className="flex justify-between items-center">
          <div>
            <span className="text-[11px] font-bold text-blue-700 uppercase tracking-wider block mb-1">
              TRIP MANAGEMENT
            </span>
            <h1 className="text-3xl font-black text-blue-950 font-heading">My Travel Plans</h1>
          </div>
          <button
            onClick={() => navigate('/trips/new')}
            className="btn-teal px-6 py-2.5 text-xs font-bold shadow-md flex items-center space-x-2"
          >
            <Plus className="w-4 h-4" />
            <span>Create New Trip</span>
          </button>
        </div>

        {trips.length === 0 ? (
          <div className="bg-white rounded-3xl p-12 text-center space-y-4 border border-slate-200/80 shadow-sm">
            <Calendar className="w-12 h-12 text-slate-500 mx-auto" />
            <h3 className="text-xl font-black text-slate-900 font-heading">No active trips planned yet</h3>
            <p className="text-xs text-slate-700 font-medium">Launch the trip wizard to pick a destination and build your itinerary.</p>
            <button
              onClick={() => navigate('/trips/new')}
              className="btn-teal px-6 py-2.5 text-xs font-bold"
            >
              Start Planning
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {trips.map((trip) => (
              <div
                key={trip.id}
                onClick={() => navigate(`/trips/${trip.id}`)}
                className="bg-white p-6 rounded-3xl border border-slate-200/80 hover:border-teal-500/50 hover:shadow-md transition cursor-pointer space-y-4 group"
              >
                <div className="flex items-center space-x-2 text-xs text-teal-700 font-bold">
                  <MapPin className="w-4 h-4" />
                  <span>{trip.destination?.name || trip.destinationName || 'Global Destination'}{trip.destinationCountry || trip.destination?.country ? `, ${trip.destinationCountry || trip.destination?.country}` : ''}</span>
                </div>
                <h3 className="text-xl font-black text-slate-900 font-heading">{trip.title}</h3>
                <div className="text-xs text-slate-700 font-bold">
                  📅 {trip.startDate} — {trip.endDate} ({trip.totalDays || trip.durationDays || 1} Days)
                </div>
                <div className="flex justify-between items-center pt-3 border-t border-slate-100 text-xs">
                  <span className="text-teal-700 font-bold">${trip.targetBudget || trip.totalBudget || 0} Budget</span>
                  <span className="text-slate-900 font-bold group-hover:text-teal-700 group-hover:translate-x-1 transition">
                    Open Hub →
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <WebSocketProvider>
        <Router>
          <div className="min-h-screen flex flex-col bg-[#f7faf9]">
            <Navbar />
            <main className="flex-1">
              <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/destinations" element={<DestinationsPage />} />
                <Route path="/destinations/:id" element={<DestinationDetailPage />} />
                <Route path="/trips/new" element={<TripWizardPage />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/trips/:id" element={<TripDashboardPage />} />
                <Route path="/trips/:id/itinerary" element={<ItineraryBuilderPage />} />
                <Route path="/trips/:id/bookings" element={<BookingsHubPage />} />
                <Route path="/trips/:id/activities" element={<ActivitiesPage />} />
                <Route path="/trips/:id/budget" element={<BudgetTrackerPage />} />
                <Route path="/trips/:id/collaboration" element={<CollaborationRoomPage />} />
                <Route path="/trips/:id/export" element={<ExportSharePage />} />
              </Routes>
            </main>
            <Footer />
            <FloatingQuickDock />
          </div>
        </Router>
      </WebSocketProvider>
    </AuthProvider>
  );
}
