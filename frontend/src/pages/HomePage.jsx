import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  Compass, Search, Calendar, Users, MapPin, 
  ArrowRight, ShieldCheck, Zap, Globe, Star, Sparkles 
} from 'lucide-react';
import { destinationApi } from '../api/client';
import { useAuth } from '../context/AuthContext';
import { RetentionBarIcon } from '../components/BrandLogo';

export default function HomePage() {
  const [destinations, setDestinations] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    destinationApi.getAll({ size: 10 })
      .then((res) => {
        if (res.data.success) {
          const items = Array.isArray(res.data.data) ? res.data.data : (res.data.data?.content || []);
          setDestinations(items.slice(0, 6));
        }
      })
      .catch(console.error);
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/destinations?search=${encodeURIComponent(searchQuery)}`);
    }
  };

  // Interactive Journey Feasibility & Conflict Simulator state
  const [simActivities, setSimActivities] = useState(3);
  const [simBuffer, setSimBuffer] = useState(45);
  const [simTravelers, setSimTravelers] = useState(2);

  const rawScore = 100 - (simActivities * 8) + (simBuffer * 0.42) - (simTravelers > 3 ? (simTravelers - 3) * 5 : 0);
  const feasibilityScore = Math.max(28, Math.min(99, Math.round(rawScore)));
  const isSafe = feasibilityScore >= 80;
  const isWarning = feasibilityScore >= 60 && feasibilityScore < 80;
  const isDanger = feasibilityScore < 60;

  const setScenario = (act, buf, trav) => {
    setSimActivities(act);
    setSimBuffer(buf);
    setSimTravelers(trav);
  };

  return (
    <div className="min-h-screen bg-mesh text-slate-900 pb-20">
      
      {/* Hero Section matching Reference Image */}
      <section className="pt-8 pb-16 md:pt-14 md:pb-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Brand Header inside Hero */}
        <div className="flex items-center space-x-3 mb-8">
          <div className="w-10 h-10 rounded-2xl bg-white border border-slate-200/90 shadow-sm flex items-center justify-center p-1">
            <RetentionBarIcon size="sm" />
          </div>
          <div className="flex flex-col">
            <span className="text-[11px] uppercase font-extrabold tracking-widest text-slate-700 -mb-0.5">
              TRAVEL ANALYTICS SUITE
            </span>
            <span className="text-xl font-black tracking-tight text-slate-900 font-heading">
              Wander<span className="text-teal-600">Lust Intelligence</span>
            </span>
          </div>
        </div>

        {/* 2-Column Hero Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-10 items-center">
          
          {/* Left Hero Column */}
          <div className="lg:col-span-7 space-y-6">
            <h1 className="text-4xl sm:text-5xl lg:text-[56px] font-black text-blue-950 tracking-tight leading-[1.08] font-heading">
              Travel Planning and <br />
              <span className="text-blue-800">Itinerary Analytics</span> <br />
              System
            </h1>

            <p className="text-slate-700 text-sm sm:text-base leading-relaxed max-w-xl font-medium">
              Detect itinerary conflicts early. Score journey feasibility, flag transit and scheduling risk, and deliver personalized itineraries to keep travelers on track.
            </p>

            {/* Action Buttons */}
            <div className="flex flex-wrap items-center gap-3 pt-2">
              <button
                onClick={() => navigate(isAuthenticated ? '/trips/new' : '/trips/new')}
                className="btn-interactive btn-action-teal px-8 py-3.5 text-sm font-black rounded-full shadow-md shadow-teal-600/25"
              >
                <Sparkles className="w-4 h-4" />
                <span>{isAuthenticated ? 'Plan New Trip' : 'Create Account / Plan'}</span>
              </button>
              
              <button
                onClick={() => navigate('/destinations')}
                className="btn-interactive btn-dark px-8 py-3.5 text-sm font-black rounded-full shadow-md"
              >
                <Compass className="w-4 h-4 text-cyan-400" />
                <span>Explore 100+ Destinations</span>
              </button>
            </div>

            {/* Stat Pills Row */}
            <div className="pt-4 flex flex-wrap gap-3">
              <div className="bg-white/95 rounded-2xl p-3 px-5 border border-slate-200/80 shadow-sm min-w-[130px]">
                <div className="text-[10px] uppercase font-extrabold tracking-wider text-blue-800">
                  DESTINATIONS
                </div>
                <div className="text-lg font-black text-blue-950 font-heading">
                  100+ Curated
                </div>
              </div>

              <div className="bg-white/95 rounded-2xl p-3 px-5 border border-slate-200/80 shadow-sm min-w-[130px]">
                <div className="text-[10px] uppercase font-extrabold tracking-wider text-rose-600">
                  CONFLICT SIGNALS
                </div>
                <div className="text-lg font-black text-rose-600 font-heading">
                  0 Overlaps
                </div>
              </div>

              <div className="bg-white/95 rounded-2xl p-3 px-5 border border-slate-200/80 shadow-sm min-w-[130px]">
                <div className="text-[10px] uppercase font-extrabold tracking-wider text-teal-800">
                  ACTIONABLE INSIGHTS
                </div>
                <div className="text-lg font-black text-blue-950 font-heading">
                  Realtime
                </div>
              </div>
            </div>
          </div>

          {/* Right Hero: Interactive Journey Feasibility & Conflict Lab */}
          <div className="lg:col-span-5 flex justify-center">
            <div className="w-full max-w-md bg-white rounded-[32px] p-6 sm:p-7 border border-slate-200/90 shadow-[0_20px_50px_rgba(0,0,0,0.06)] flex flex-col justify-between transition-all">
              
              {/* Header Badge & Dynamic Score */}
              <div className="flex items-center justify-between pb-4 border-b border-slate-100">
                <div className="flex items-center space-x-2">
                  <span className="w-2.5 h-2.5 rounded-full bg-teal-500 animate-pulse" />
                  <span className="text-[11px] font-extrabold uppercase tracking-wider text-slate-800">
                    Feasibility Engine
                  </span>
                </div>
                <span className={`text-xs font-extrabold px-3 py-1 rounded-full border shadow-sm ${
                  isSafe ? 'bg-emerald-50 text-emerald-800 border-emerald-200' :
                  isWarning ? 'bg-amber-50 text-amber-800 border-amber-200' :
                  'bg-rose-50 text-rose-800 border-rose-200'
                }`}>
                  {feasibilityScore}% Feasibility
                </span>
              </div>

              {/* Quick Preset Scenario Pills */}
              <div className="py-4 space-y-2">
                <span className="text-[10px] font-extrabold uppercase text-slate-500 tracking-wider">
                  Test Travel Scenarios:
                </span>
                <div className="grid grid-cols-3 gap-1.5">
                  <button
                    type="button"
                    onClick={() => setScenario(2, 60, 2)}
                    className="btn-interactive py-1.5 px-2 bg-slate-50 hover:bg-teal-50 text-[11px] font-bold text-slate-800 hover:text-teal-900 rounded-xl border border-slate-200 text-center"
                  >
                    Relaxed
                  </button>
                  <button
                    type="button"
                    onClick={() => setScenario(4, 30, 3)}
                    className="btn-interactive py-1.5 px-2 bg-slate-50 hover:bg-sky-50 text-[11px] font-bold text-slate-800 hover:text-sky-900 rounded-xl border border-slate-200 text-center"
                  >
                    Standard
                  </button>
                  <button
                    type="button"
                    onClick={() => setScenario(6, 15, 5)}
                    className="btn-interactive py-1.5 px-2 bg-slate-50 hover:bg-rose-50 text-[11px] font-bold text-slate-800 hover:text-rose-900 rounded-xl border border-slate-200 text-center"
                  >
                    Fast-Paced
                  </button>
                </div>
              </div>

              {/* Sliders */}
              <div className="space-y-4 py-2">
                <div>
                  <div className="flex justify-between text-xs font-bold text-slate-800 mb-1">
                    <span>Daily Activities</span>
                    <span className="text-teal-700 font-black">{simActivities} stops</span>
                  </div>
                  <input
                    type="range"
                    min="1"
                    max="8"
                    value={simActivities}
                    onChange={(e) => setSimActivities(Number(e.target.value))}
                    className="w-full accent-teal-600 cursor-pointer h-1.5"
                  />
                </div>

                <div>
                  <div className="flex justify-between text-xs font-bold text-slate-800 mb-1">
                    <span>Transit Buffer</span>
                    <span className="text-teal-700 font-black">{simBuffer} mins</span>
                  </div>
                  <input
                    type="range"
                    min="10"
                    max="90"
                    step="5"
                    value={simBuffer}
                    onChange={(e) => setSimBuffer(Number(e.target.value))}
                    className="w-full accent-teal-600 cursor-pointer h-1.5"
                  />
                </div>

                <div>
                  <div className="flex justify-between text-xs font-bold text-slate-800 mb-1">
                    <span>Group Size</span>
                    <span className="text-teal-700 font-black">{simTravelers} travelers</span>
                  </div>
                  <input
                    type="range"
                    min="1"
                    max="6"
                    value={simTravelers}
                    onChange={(e) => setSimTravelers(Number(e.target.value))}
                    className="w-full accent-teal-600 cursor-pointer h-1.5"
                  />
                </div>
              </div>

              {/* Live AI Diagnostic Signal */}
              <div className={`p-3 rounded-2xl border text-xs font-medium my-2 ${
                isSafe 
                  ? 'bg-teal-50/80 border-teal-200 text-teal-900' 
                  : isWarning 
                  ? 'bg-amber-50/80 border-amber-200 text-amber-950' 
                  : 'bg-rose-50/80 border-rose-200 text-rose-950'
              }`}>
                {isSafe && "✓ Safe schedule: Plentiful buffer protects against subway and check-in delays."}
                {isWarning && "⚠ Moderate caution: Buffer under 40m may cause tight connection during peak transit."}
                {isDanger && "🚨 High Collision Signal: 5+ activities with tight buffer creates elevated overlap risk."}
              </div>

              {/* Direct CTA */}
              <button
                onClick={() => navigate('/trips/new')}
                className="btn-interactive btn-action-teal w-full py-3 text-xs font-black rounded-2xl shadow-md"
              >
                <span>Plan Custom Itinerary</span>
                <ArrowRight className="w-3.5 h-3.5" />
              </button>

            </div>
          </div>

        </div>

        {/* 3-Card Feature Grid */}
        <div className="mt-12 grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white/95 rounded-[24px] p-6 border border-slate-200/80 shadow-sm space-y-2 hover:border-cyan-500/50 hover:shadow-md transition">
            <h4 className="text-base font-black text-blue-950 font-heading">
              Itinerary Scoring
            </h4>
            <p className="text-xs text-slate-700 leading-relaxed font-medium">
              Explainable scheduling across flights, stays, activities, and transit buffer usage.
            </p>
          </div>

          <div className="bg-white/95 rounded-[24px] p-6 border border-slate-200/80 shadow-sm space-y-2 hover:border-rose-500/50 hover:shadow-md transition">
            <h4 className="text-base font-black text-blue-950 font-heading">
              At-Risk Conflict Detection
            </h4>
            <p className="text-xs text-slate-700 leading-relaxed font-medium">
              Rule-based thresholds highlight travel overlaps needing early itinerary adjustments.
            </p>
          </div>

          <div className="bg-white/95 rounded-[24px] p-6 border border-slate-200/80 shadow-sm space-y-2 hover:border-teal-500/50 hover:shadow-md transition">
            <h4 className="text-base font-black text-blue-950 font-heading">
              Analytics Dashboard
            </h4>
            <p className="text-xs text-slate-700 leading-relaxed font-medium">
              Charts, multi-currency tables, and printable reports for group travel decision-making.
            </p>
          </div>
        </div>

      </section>

      {/* Search & Destination Discovery Bar */}
      <section className="py-6 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <form onSubmit={handleSearch} className="max-w-3xl mx-auto">
          <div className="bg-white p-2.5 rounded-full border border-slate-200/90 shadow-md flex items-center space-x-3">
            <Search className="w-5 h-5 text-teal-600 ml-4 flex-shrink-0" />
            <input
              type="text"
              placeholder="Search across 100+ destinations by city, vibe, or continent..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full bg-transparent text-slate-800 placeholder-slate-400 outline-none text-sm font-bold"
            />
            <button
              type="submit"
              className="btn-interactive btn-action-teal px-6 py-2.5 text-xs font-bold rounded-full shadow-sm"
            >
              Search
            </button>
          </div>
        </form>
      </section>

      {/* Featured Destinations Section */}
      <section className="py-12 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between mb-8">
          <div>
            <div className="text-[11px] font-bold text-blue-700 uppercase tracking-wider mb-1">
              CURATED GLOBAL SPOTS
            </div>
            <h2 className="text-2xl sm:text-3xl font-black text-blue-950 font-heading">
              Featured Destinations
            </h2>
          </div>
          <Link
            to="/destinations"
            className="btn-interactive mt-3 sm:mt-0 px-4 py-2 bg-white rounded-full border border-slate-200 text-xs font-black text-teal-700 hover:text-white hover:bg-teal-600 hover:border-teal-600 shadow-sm transition"
          >
            <span>View All 100+ Destinations</span>
            <ArrowRight className="w-4 h-4" />
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {destinations.map((dest) => {
            const displayImage = dest.heroImageUrl || dest.imageUrl || 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80';
            const cost = dest.averageDailyCost || dest.avgCostPerDay || 150;
            const vibe = Array.isArray(dest.vibeTags) ? dest.vibeTags[0] : (typeof dest.vibeTags === 'string' ? dest.vibeTags.split(',')[0] : (dest.vibe || 'Cultural'));

            return (
              <div
                key={dest.id}
                onClick={() => navigate(`/destinations/${dest.id}`)}
                className="group bg-white rounded-3xl overflow-hidden border border-slate-200/90 hover:border-teal-400 hover:shadow-xl transition-all duration-300 cursor-pointer"
              >
                <div className="relative h-56 overflow-hidden">
                  <img
                    src={displayImage}
                    alt={dest.name}
                    className="w-full h-full object-cover group-hover:scale-108 transition-transform duration-500"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-slate-950/75 via-transparent to-transparent" />
                  
                  {/* Rating Badge */}
                  <div className="absolute top-3.5 right-3.5 bg-white/95 backdrop-blur-md px-2.5 py-1 rounded-full border border-white/50 flex items-center space-x-1 text-slate-800 text-xs font-black shadow-sm">
                    <Star className="w-3.5 h-3.5 fill-amber-400 text-amber-400" />
                    <span>{dest.rating || 4.9}</span>
                  </div>

                  {/* Vibe Tag */}
                  <div className="absolute top-3.5 left-3.5 bg-gradient-to-r from-teal-600 to-emerald-600 text-white px-3 py-1 rounded-full text-[11px] font-black uppercase tracking-wider shadow-sm">
                    {vibe}
                  </div>

                  <div className="absolute bottom-3.5 left-4 right-4">
                    <h3 className="text-xl font-black text-white font-heading tracking-tight">{dest.name}</h3>
                    <div className="flex items-center space-x-1 text-slate-200 text-xs font-bold mt-0.5">
                      <MapPin className="w-3.5 h-3.5 text-teal-300" />
                      <span>{dest.country}</span>
                    </div>
                  </div>
                </div>

                <div className="p-5 space-y-3">
                  <p className="text-xs text-slate-700 line-clamp-2 leading-relaxed font-medium">
                    {dest.description}
                  </p>

                  <div className="flex items-center justify-between pt-3 border-t border-slate-100 text-xs font-bold text-slate-800">
                    <div>
                      <span className="text-slate-500 text-[10px] uppercase block font-extrabold">Avg. Daily Budget</span>
                      <span className="text-teal-700 font-black text-sm">${cost}</span> / day
                    </div>
                    <button className="btn-interactive btn-action-teal px-4 py-1.5 text-xs font-bold rounded-full shadow-sm">
                      View Details
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </section>

    </div>
  );
}
