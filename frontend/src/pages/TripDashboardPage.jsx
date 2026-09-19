import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { tripApi } from '../api/client';
import WeatherWidget from '../components/WeatherWidget';
import ConflictAlert from '../components/ConflictAlert';
import { 
  Calendar, MapPin, DollarSign, Users, Sparkles, 
  Plane, Hotel, Camera, PieChart, MessageSquare, Download, Clock, ShieldCheck 
} from 'lucide-react';

export default function TripDashboardPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [trip, setTrip] = useState(null);
  const [conflicts, setConflicts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTripDetails();
  }, [id]);

  const fetchTripDetails = () => {
    setLoading(true);
    tripApi.getById(id)
      .then((res) => {
        if (res.data.success) {
          const tripData = res.data.data;
          setTrip(tripData);
          // Aggregate conflicts from days if available
          const dayConflicts = (tripData.days || []).flatMap((d) => d.conflicts || []);
          if (dayConflicts.length > 0) {
            setConflicts(dayConflicts);
          }
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));

    tripApi.checkConflicts(id)
      .then((res) => {
        if (res.data.success && Array.isArray(res.data.data)) {
          const allConflicts = res.data.data.flatMap((d) => d.conflicts || []);
          if (allConflicts.length > 0) setConflicts(allConflicts);
        }
      })
      .catch((err) => console.warn('Non-blocking conflict check:', err));
  };

  if (loading || !trip) {
    return (
      <div className="min-h-screen bg-slate-950 flex justify-center items-center">
        <div className="w-10 h-10 border-4 border-cyan-400 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  const destName = trip.destination?.name || trip.destinationName || 'Global Destination';
  const destCountry = trip.destination?.country || trip.destinationCountry || '';
  const totalDays = trip.totalDays || trip.durationDays || 1;
  const travelers = trip.travelerCount || trip.travelersCount || 1;
  const budget = trip.targetBudget || trip.totalBudget || 0;

  // Calculate Countdown Days
  const daysUntilTrip = Math.max(0, Math.ceil((new Date(trip.startDate) - new Date()) / (1000 * 60 * 60 * 24)));

  return (
    <div className="min-h-screen bg-[#f7faf9] bg-mesh text-slate-900 py-10 pb-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Top Banner Header */}
        <div className="glass-panel p-8 rounded-3xl border border-slate-200/80 shadow-sm relative overflow-hidden">
          <div className="absolute top-0 right-0 w-96 h-96 bg-cyan-500/10 rounded-full blur-3xl pointer-events-none" />

          <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-6 relative z-10">
            <div className="space-y-3">
              <div className="flex flex-wrap items-center gap-2">
                <span className="px-3 py-1 bg-blue-100 text-blue-900 border border-blue-200 rounded-full text-xs font-bold uppercase tracking-wider">
                  Active Trip Hub
                </span>
                <span className="px-3 py-1 bg-emerald-100 text-emerald-900 border border-emerald-200 rounded-full text-xs font-bold">
                  {daysUntilTrip > 0 ? `⏳ ${daysUntilTrip} Days to Departure` : '✈️ Trip In Progress'}
                </span>
              </div>
              <h1 className="text-3xl sm:text-5xl font-black text-blue-950 font-heading">
                {trip.title}
              </h1>
              <div className="flex flex-wrap items-center gap-4 text-xs font-bold text-slate-800">
                <div className="flex items-center space-x-1 text-cyan-800">
                  <MapPin className="w-4 h-4" />
                  <span>{destName}{destCountry ? `, ${destCountry}` : ''}</span>
                </div>
                <div className="flex items-center space-x-1 text-slate-800">
                  <Calendar className="w-4 h-4 text-indigo-700" />
                  <span>{trip.startDate} — {trip.endDate} ({totalDays} Days)</span>
                </div>
                <div className="flex items-center space-x-1 text-slate-800">
                  <Users className="w-4 h-4 text-amber-700" />
                  <span>{travelers} Travelers</span>
                </div>
              </div>
            </div>

            {/* Quick Action Navigation Buttons */}
            <div className="flex flex-wrap items-center gap-2">
              <Link
                to={`/trips/${id}/itinerary`}
                className="px-5 py-3 bg-gradient-to-r from-cyan-500 to-sky-500 text-slate-950 font-black rounded-xl text-xs shadow-lg shadow-cyan-500/20 hover:opacity-95 transition"
              >
                Itinerary Timeline
              </Link>
              <Link
                to={`/trips/${id}/budget`}
                className="px-5 py-3 bg-slate-900 border border-slate-800 text-emerald-400 hover:bg-slate-800 font-bold rounded-xl text-xs transition"
              >
                Budget & Expenses
              </Link>
            </div>
          </div>
        </div>

        {/* Schedule Conflict Alert */}
        <ConflictAlert conflicts={conflicts} />

        {/* Main Dashboard Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Sub Feature Cards (2 Cols) */}
          <div className="lg:col-span-2 grid grid-cols-1 sm:grid-cols-2 gap-6">
            
            {/* Itinerary Timeline */}
            <Link
              to={`/trips/${id}/itinerary`}
              className="glass-panel p-6 rounded-3xl border border-slate-200/80 hover:border-cyan-500/50 transition-all duration-300 group space-y-4 shadow-sm"
            >
              <div className="w-12 h-12 rounded-2xl bg-cyan-500/10 border border-cyan-500/20 flex items-center justify-center text-cyan-700 group-hover:scale-110 transition">
                <Calendar className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-blue-950 font-heading">Day-by-Day Itinerary</h3>
                <p className="text-xs text-slate-700 mt-1 font-medium leading-relaxed">
                  Drag & drop activities, manage time slots, and prevent overlapping schedules.
                </p>
              </div>
              <span className="inline-flex items-center text-xs font-bold text-cyan-800 group-hover:translate-x-1 transition">
                Open Timeline →
              </span>
            </Link>

            {/* Flight & Hotel Bookings */}
            <Link
              to={`/trips/${id}/bookings`}
              className="glass-panel p-6 rounded-3xl border border-slate-200/80 hover:border-sky-500/50 transition-all duration-300 group space-y-4 shadow-sm"
            >
              <div className="w-12 h-12 rounded-2xl bg-sky-500/10 border border-sky-500/20 flex items-center justify-center text-sky-700 group-hover:scale-110 transition">
                <Plane className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-blue-950 font-heading">Bookings & Reservations</h3>
                <p className="text-xs text-slate-700 mt-1 font-medium leading-relaxed">
                  Search flights, hotels, and checkout with simulated payment engine.
                </p>
              </div>
              <span className="inline-flex items-center text-xs font-bold text-sky-800 group-hover:translate-x-1 transition">
                Manage Bookings →
              </span>
            </Link>

            {/* Budget Tracker & Split Expenses */}
            <Link
              to={`/trips/${id}/budget`}
              className="glass-panel p-6 rounded-3xl border border-slate-200/80 hover:border-emerald-500/50 transition-all duration-300 group space-y-4 shadow-sm"
            >
              <div className="w-12 h-12 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-700 group-hover:scale-110 transition">
                <PieChart className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-blue-950 font-heading">Budget & Debt Splitter</h3>
                <p className="text-xs text-slate-700 mt-1 font-medium leading-relaxed">
                  Track expenses, view breakdown graphs, and calculate who owes whom.
                </p>
              </div>
              <span className="inline-flex items-center text-xs font-bold text-emerald-800 group-hover:translate-x-1 transition">
                Manage Expenses →
              </span>
            </Link>

            {/* Collaboration & Live Chat */}
            <Link
              to={`/trips/${id}/collaboration`}
              className="glass-panel p-6 rounded-3xl border border-slate-200/80 hover:border-indigo-500/50 transition-all duration-300 group space-y-4 shadow-sm"
            >
              <div className="w-12 h-12 rounded-2xl bg-indigo-500/10 border border-indigo-500/20 flex items-center justify-center text-indigo-700 group-hover:scale-110 transition">
                <MessageSquare className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-blue-950 font-heading">Live Collaboration Room</h3>
                <p className="text-xs text-slate-700 mt-1 font-medium leading-relaxed">
                  Real-time WebSocket chat and member role permissions (Viewer/Editor).
                </p>
              </div>
              <span className="inline-flex items-center text-xs font-bold text-indigo-800 group-hover:translate-x-1 transition">
                Enter Chat Room →
              </span>
            </Link>

            {/* Sights & Dining Catalog */}
            <Link
              to={`/trips/${id}/activities`}
              className="glass-panel p-6 rounded-3xl border border-slate-200/80 hover:border-purple-500/50 transition-all duration-300 group space-y-4 shadow-sm"
            >
              <div className="w-12 h-12 rounded-2xl bg-purple-500/10 border border-purple-500/20 flex items-center justify-center text-purple-700 group-hover:scale-110 transition">
                <Camera className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-blue-950 font-heading">Activities & Dining</h3>
                <p className="text-xs text-slate-700 mt-1 font-medium leading-relaxed">
                  Explore sights & local food, 1-click add directly into itinerary slots.
                </p>
              </div>
              <span className="inline-flex items-center text-xs font-bold text-purple-800 group-hover:translate-x-1 transition">
                Explore Attractions →
              </span>
            </Link>

            {/* Export & Calendar Sync */}
            <Link
              to={`/trips/${id}/export`}
              className="glass-panel p-6 rounded-3xl border border-slate-200/80 hover:border-amber-500/50 transition-all duration-300 group space-y-4 shadow-sm"
            >
              <div className="w-12 h-12 rounded-2xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center text-amber-700 group-hover:scale-110 transition">
                <Download className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-blue-950 font-heading">PDF & iCal Export</h3>
                <p className="text-xs text-slate-700 mt-1 font-medium leading-relaxed">
                  Download printable PDF plans, sync iCalendar files, and share QR codes.
                </p>
              </div>
              <span className="inline-flex items-center text-xs font-bold text-amber-800 group-hover:translate-x-1 transition">
                Export Options →
              </span>
            </Link>

          </div>

          {/* Right Column: Destination Weather Widget */}
          <div className="space-y-6">
            <WeatherWidget
              weatherData={trip.destination?.weather || trip.weather}
              cityName={destName}
            />

            {/* Trip Details Summary Card */}
            <div className="glass-panel p-6 rounded-3xl border border-slate-200/80 space-y-4 shadow-sm">
              <h4 className="text-xs font-bold text-blue-900 uppercase tracking-wider font-heading">
                Trip Logistics
              </h4>
              <div className="space-y-3 text-xs text-slate-800">
                <div className="flex justify-between py-1.5 border-b border-slate-200">
                  <span className="text-slate-600 font-semibold">Destination:</span>
                  <span className="font-bold text-cyan-800">{destName}</span>
                </div>
                <div className="flex justify-between py-1.5 border-b border-slate-200">
                  <span className="text-slate-600 font-semibold">Duration:</span>
                  <span className="font-bold text-slate-900">{totalDays} Days</span>
                </div>
                <div className="flex justify-between py-1.5 border-b border-slate-200">
                  <span className="text-slate-600 font-semibold">Target Budget:</span>
                  <span className="font-bold text-emerald-800">{trip.currency || 'USD'} ${budget}</span>
                </div>
              </div>
            </div>
          </div>

        </div>

      </div>
    </div>
  );
}
