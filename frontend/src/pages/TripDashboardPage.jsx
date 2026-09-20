import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { tripApi } from '../api/client';
import WeatherWidget from '../components/WeatherWidget';
import ConflictAlert from '../components/ConflictAlert';
import { 
  Calendar, MapPin, DollarSign, Users, Sparkles, 
  Plane, Hotel, Camera, PieChart, MessageSquare, Download, Clock, ShieldCheck, ArrowRight 
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
        <div className="glass-panel p-8 rounded-3xl border border-slate-200/90 shadow-sm relative overflow-hidden">
          <div className="absolute top-0 right-0 w-96 h-96 bg-cyan-500/10 rounded-full blur-3xl pointer-events-none" />

          <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-6 relative z-10">
            <div className="space-y-3">
              <div className="flex flex-wrap items-center gap-2">
                <span className="px-3.5 py-1 bg-gradient-to-r from-blue-500/15 to-indigo-500/15 text-blue-950 border border-blue-200/90 rounded-full text-xs font-black uppercase tracking-wider shadow-sm">
                  Active Trip Hub
                </span>
                <span className="px-3.5 py-1 bg-emerald-100/90 text-emerald-900 border border-emerald-300/80 rounded-full text-xs font-extrabold shadow-sm">
                  {daysUntilTrip > 0 ? `⏳ ${daysUntilTrip} Days to Departure` : '✈️ Trip In Progress'}
                </span>
              </div>
              <h1 className="text-3xl sm:text-5xl font-black text-blue-950 font-heading tracking-tight">
                {trip.title}
              </h1>
              <div className="flex flex-wrap items-center gap-4 text-xs font-bold text-slate-800">
                <div className="flex items-center space-x-1.5 text-cyan-800 bg-cyan-50 px-2.5 py-1 rounded-lg border border-cyan-100">
                  <MapPin className="w-4 h-4 text-cyan-600" />
                  <span>{destName}{destCountry ? `, ${destCountry}` : ''}</span>
                </div>
                <div className="flex items-center space-x-1.5 text-slate-800 bg-indigo-50 px-2.5 py-1 rounded-lg border border-indigo-100">
                  <Calendar className="w-4 h-4 text-indigo-600" />
                  <span>{trip.startDate} — {trip.endDate} ({totalDays} Days)</span>
                </div>
                <div className="flex items-center space-x-1.5 text-slate-800 bg-amber-50 px-2.5 py-1 rounded-lg border border-amber-100">
                  <Users className="w-4 h-4 text-amber-600" />
                  <span>{travelers} Travelers</span>
                </div>
              </div>
            </div>

            {/* Quick Action Navigation Buttons */}
            <div className="flex flex-wrap items-center gap-3">
              <Link
                to={`/trips/${id}/itinerary`}
                className="btn-interactive btn-action-cyan px-6 py-3 rounded-xl text-xs font-extrabold shadow-md"
              >
                <Calendar className="w-4 h-4" />
                <span>Itinerary Timeline</span>
              </Link>
              <Link
                to={`/trips/${id}/budget`}
                className="btn-interactive btn-action-emerald px-6 py-3 rounded-xl text-xs font-extrabold shadow-md"
              >
                <PieChart className="w-4 h-4" />
                <span>Budget & Expenses</span>
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
            
            {/* Box 1: Itinerary Timeline */}
            <Link
              to={`/trips/${id}/itinerary`}
              className="dashboard-box dashboard-box-cyan group"
            >
              <div className="space-y-4">
                <div className="w-13 h-13 w-12 h-12 rounded-2xl bg-gradient-to-tr from-cyan-500 to-sky-400 text-white flex items-center justify-center shadow-lg shadow-cyan-500/30 group-hover:scale-110 group-hover:rotate-3 transition-transform duration-300">
                  <Calendar className="w-6 h-6" />
                </div>
                <div>
                  <h3 className="text-xl font-black text-blue-950 font-heading group-hover:text-cyan-800 transition">
                    Day-by-Day Itinerary
                  </h3>
                  <p className="text-xs text-slate-600 mt-1.5 font-medium leading-relaxed">
                    Drag & drop activities, manage time slots, and prevent overlapping schedules.
                  </p>
                </div>
              </div>
              <div className="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
                <span className="btn-interactive text-xs font-black px-4 py-2 rounded-xl bg-cyan-50 text-cyan-800 border border-cyan-200/80 group-hover:bg-cyan-500 group-hover:text-white group-hover:border-cyan-500 group-hover:shadow-md group-hover:shadow-cyan-500/30 transition-all duration-200">
                  Open Timeline <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
                </span>
                <span className="text-[11px] font-bold text-slate-500 uppercase tracking-wider">
                  {totalDays} Days
                </span>
              </div>
            </Link>

            {/* Box 2: Flight & Hotel Bookings */}
            <Link
              to={`/trips/${id}/bookings`}
              className="dashboard-box dashboard-box-sky group"
            >
              <div className="space-y-4">
                <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-sky-500 to-blue-600 text-white flex items-center justify-center shadow-lg shadow-sky-500/30 group-hover:scale-110 group-hover:rotate-3 transition-transform duration-300">
                  <Plane className="w-6 h-6" />
                </div>
                <div>
                  <h3 className="text-xl font-black text-blue-950 font-heading group-hover:text-sky-800 transition">
                    Bookings & Reservations
                  </h3>
                  <p className="text-xs text-slate-600 mt-1.5 font-medium leading-relaxed">
                    Search flights, hotels, and checkout with simulated payment engine.
                  </p>
                </div>
              </div>
              <div className="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
                <span className="btn-interactive text-xs font-black px-4 py-2 rounded-xl bg-sky-50 text-sky-800 border border-sky-200/80 group-hover:bg-sky-500 group-hover:text-white group-hover:border-sky-500 group-hover:shadow-md group-hover:shadow-sky-500/30 transition-all duration-200">
                  Manage Bookings <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
                </span>
                <span className="text-[11px] font-bold text-slate-500 uppercase tracking-wider">
                  Hub
                </span>
              </div>
            </Link>

            {/* Box 3: Budget Tracker & Split Expenses */}
            <Link
              to={`/trips/${id}/budget`}
              className="dashboard-box dashboard-box-emerald group"
            >
              <div className="space-y-4">
                <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-emerald-500 to-teal-600 text-white flex items-center justify-center shadow-lg shadow-emerald-500/30 group-hover:scale-110 group-hover:rotate-3 transition-transform duration-300">
                  <PieChart className="w-6 h-6" />
                </div>
                <div>
                  <h3 className="text-xl font-black text-blue-950 font-heading group-hover:text-emerald-800 transition">
                    Budget & Debt Splitter
                  </h3>
                  <p className="text-xs text-slate-600 mt-1.5 font-medium leading-relaxed">
                    Track expenses, view breakdown graphs, and calculate who owes whom.
                  </p>
                </div>
              </div>
              <div className="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
                <span className="btn-interactive text-xs font-black px-4 py-2 rounded-xl bg-emerald-50 text-emerald-800 border border-emerald-200/80 group-hover:bg-emerald-500 group-hover:text-white group-hover:border-emerald-500 group-hover:shadow-md group-hover:shadow-emerald-500/30 transition-all duration-200">
                  Manage Expenses <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
                </span>
                <span className="text-[11px] font-bold text-slate-500 uppercase tracking-wider">
                  ${budget}
                </span>
              </div>
            </Link>

            {/* Box 4: Collaboration & Live Chat */}
            <Link
              to={`/trips/${id}/collaboration`}
              className="dashboard-box dashboard-box-indigo group"
            >
              <div className="space-y-4">
                <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-indigo-500 to-purple-600 text-white flex items-center justify-center shadow-lg shadow-indigo-500/30 group-hover:scale-110 group-hover:rotate-3 transition-transform duration-300">
                  <MessageSquare className="w-6 h-6" />
                </div>
                <div>
                  <h3 className="text-xl font-black text-blue-950 font-heading group-hover:text-indigo-800 transition">
                    Live Collaboration Room
                  </h3>
                  <p className="text-xs text-slate-600 mt-1.5 font-medium leading-relaxed">
                    Real-time WebSocket chat and member role permissions (Viewer/Editor).
                  </p>
                </div>
              </div>
              <div className="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
                <span className="btn-interactive text-xs font-black px-4 py-2 rounded-xl bg-indigo-50 text-indigo-800 border border-indigo-200/80 group-hover:bg-indigo-500 group-hover:text-white group-hover:border-indigo-500 group-hover:shadow-md group-hover:shadow-indigo-500/30 transition-all duration-200">
                  Enter Chat Room <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
                </span>
                <span className="text-[11px] font-bold text-emerald-600 flex items-center gap-1">
                  <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" /> Live
                </span>
              </div>
            </Link>

            {/* Box 5: Sights & Dining Catalog */}
            <Link
              to={`/trips/${id}/activities`}
              className="dashboard-box dashboard-box-purple group"
            >
              <div className="space-y-4">
                <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-purple-500 to-pink-500 text-white flex items-center justify-center shadow-lg shadow-purple-500/30 group-hover:scale-110 group-hover:rotate-3 transition-transform duration-300">
                  <Camera className="w-6 h-6" />
                </div>
                <div>
                  <h3 className="text-xl font-black text-blue-950 font-heading group-hover:text-purple-800 transition">
                    Activities & Dining
                  </h3>
                  <p className="text-xs text-slate-600 mt-1.5 font-medium leading-relaxed">
                    Explore sights & local food, 1-click add directly into itinerary slots.
                  </p>
                </div>
              </div>
              <div className="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
                <span className="btn-interactive text-xs font-black px-4 py-2 rounded-xl bg-purple-50 text-purple-800 border border-purple-200/80 group-hover:bg-purple-500 group-hover:text-white group-hover:border-purple-500 group-hover:shadow-md group-hover:shadow-purple-500/30 transition-all duration-200">
                  Explore Attractions <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
                </span>
                <span className="text-[11px] font-bold text-slate-500 uppercase tracking-wider">
                  Catalog
                </span>
              </div>
            </Link>

            {/* Box 6: Export & Calendar Sync */}
            <Link
              to={`/trips/${id}/export`}
              className="dashboard-box dashboard-box-amber group"
            >
              <div className="space-y-4">
                <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-amber-500 to-orange-500 text-white flex items-center justify-center shadow-lg shadow-amber-500/30 group-hover:scale-110 group-hover:rotate-3 transition-transform duration-300">
                  <Download className="w-6 h-6" />
                </div>
                <div>
                  <h3 className="text-xl font-black text-blue-950 font-heading group-hover:text-amber-800 transition">
                    PDF & iCal Export
                  </h3>
                  <p className="text-xs text-slate-600 mt-1.5 font-medium leading-relaxed">
                    Download printable PDF plans, sync iCalendar files, and share QR codes.
                  </p>
                </div>
              </div>
              <div className="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
                <span className="btn-interactive text-xs font-black px-4 py-2 rounded-xl bg-amber-50 text-amber-800 border border-amber-200/80 group-hover:bg-amber-500 group-hover:text-white group-hover:border-amber-500 group-hover:shadow-md group-hover:shadow-amber-500/30 transition-all duration-200">
                  Export Options <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
                </span>
                <span className="text-[11px] font-bold text-slate-500 uppercase tracking-wider">
                  PDF / iCal
                </span>
              </div>
            </Link>

          </div>

          {/* Right Column: Destination Weather Widget */}
          <div className="space-y-6">
            <WeatherWidget
              weatherData={trip.destination?.weather || trip.weather}
              cityName={destName}
            />

            {/* Trip Details Summary Card */}
            <div className="glass-panel p-6 rounded-3xl border border-slate-200/85 space-y-4 shadow-sm">
              <h4 className="text-xs font-black text-blue-950 uppercase tracking-wider font-heading flex items-center gap-2">
                <ShieldCheck className="w-4 h-4 text-teal-600" />
                Trip Logistics
              </h4>
              <div className="space-y-3 text-xs text-slate-800">
                <div className="flex justify-between py-2 border-b border-slate-100">
                  <span className="text-slate-500 font-bold">Destination:</span>
                  <span className="font-extrabold text-cyan-900">{destName}</span>
                </div>
                <div className="flex justify-between py-2 border-b border-slate-100">
                  <span className="text-slate-500 font-bold">Duration:</span>
                  <span className="font-extrabold text-slate-900">{totalDays} Days</span>
                </div>
                <div className="flex justify-between py-2 border-b border-slate-100">
                  <span className="text-slate-500 font-bold">Target Budget:</span>
                  <span className="font-extrabold text-emerald-800">{trip.currency || 'USD'} ${budget}</span>
                </div>
                <div className="flex justify-between py-2">
                  <span className="text-slate-500 font-bold">Travelers:</span>
                  <span className="font-extrabold text-indigo-900">{travelers} Person(s)</span>
                </div>
              </div>
            </div>
          </div>

        </div>

      </div>
    </div>
  );
}
