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
    destinationApi.getAll()
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
                className="btn-teal px-8 py-3.5 text-sm font-bold rounded-full shadow-md shadow-teal-600/20 hover:shadow-teal-600/30 transition flex items-center space-x-2"
              >
                <span>{isAuthenticated ? 'Plan New Trip' : 'Create Account / Plan'}</span>
              </button>
              
              <button
                onClick={() => navigate('/destinations')}
                className="btn-dark px-8 py-3.5 text-sm font-bold rounded-full shadow-md transition flex items-center space-x-2"
              >
                <span>Explore Destinations</span>
              </button>
            </div>

            {/* Stat Pills Row (Directly matching Image) */}
            <div className="pt-4 flex flex-wrap gap-3">
              <div className="bg-white/95 rounded-2xl p-3 px-5 border border-slate-200/80 shadow-sm min-w-[130px]">
                <div className="text-[10px] uppercase font-extrabold tracking-wider text-blue-800">
                  DESTINATIONS
                </div>
                <div className="text-lg font-black text-blue-950 font-heading">
                  32+ Curated
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
                <div className="flex items-center space-x-2.5">
                  <div className="w-9 h-9 rounded-xl bg-teal-50 border border-teal-100 flex items-center justify-center p-1.5">
                    <RetentionBarIcon size="sm" />
                  </div>
                  <div>
                    <span className="text-[10px] font-extrabold uppercase tracking-wider text-slate-700 block">Live Stress-Test</span>
                    <h3 className="text-sm font-black text-blue-950 font-heading">Journey Feasibility</h3>
                  </div>
                </div>

                <div className="text-right">
                  <span className={`text-2xl font-black font-heading ${
                    isSafe ? 'text-teal-700' : isWarning ? 'text-amber-600' : 'text-rose-600'
                  }`}>
                    {feasibilityScore}%
                  </span>
                  <span className={`text-[10px] font-extrabold block uppercase tracking-wider ${
                    isSafe ? 'text-teal-800' : isWarning ? 'text-amber-700' : 'text-rose-700'
                  }`}>
                    {isSafe ? 'High Feasibility' : isWarning ? 'Moderate Risk' : 'Conflict Alert'}
                  </span>
                </div>
              </div>

              {/* Dynamic Retention Progress Bars */}
              <div className="py-4 space-y-3">
                <div className="flex items-end justify-center space-x-2 h-14 bg-slate-50 rounded-2xl p-2 border border-slate-100">
                  <div 
                    style={{ height: `${Math.max(20, Math.min(100, feasibilityScore * 0.85))}%` }} 
                    className={`w-3 rounded-full transition-all duration-300 ${
                      isSafe ? 'bg-teal-500' : isWarning ? 'bg-amber-400' : 'bg-rose-400'
                    }`} 
                  />
                  <div 
                    style={{ height: `${Math.max(25, Math.min(100, feasibilityScore))}%` }} 
                    className={`w-3 rounded-full transition-all duration-300 ${
                      isSafe ? 'bg-teal-600' : isWarning ? 'bg-amber-500' : 'bg-rose-500'
                    }`} 
                  />
                  <div 
                    style={{ height: `${Math.max(18, Math.min(100, feasibilityScore * 0.7))}%` }} 
                    className={`w-3 rounded-full transition-all duration-300 ${
                      isSafe ? 'bg-cyan-500' : isWarning ? 'bg-amber-400' : 'bg-rose-400'
                    }`} 
                  />
                </div>

                {/* Scenario Quick Buttons */}
                <div className="flex items-center justify-between gap-1.5 pt-1">
                  <button
                    type="button"
                    onClick={() => setScenario(2, 60, 1)}
                    className="flex-1 py-1.5 px-2 bg-slate-100 hover:bg-teal-50 hover:text-teal-800 rounded-xl text-[11px] font-bold text-slate-700 transition"
                  >
                    Chill Solo
                  </button>
                  <button
                    type="button"
                    onClick={() => setScenario(3, 45, 3)}
                    className="flex-1 py-1.5 px-2 bg-slate-100 hover:bg-teal-50 hover:text-teal-800 rounded-xl text-[11px] font-bold text-slate-700 transition"
                  >
                    Balanced Group
                  </button>
                  <button
                    type="button"
                    onClick={() => setScenario(5, 20, 5)}
                    className="flex-1 py-1.5 px-2 bg-slate-100 hover:bg-rose-50 hover:text-rose-800 rounded-xl text-[11px] font-bold text-slate-700 transition"
                  >
                    Rush Packed
                  </button>
                </div>

                {/* Interactive Sliders */}
                <div className="space-y-2.5 pt-2">
                  <div className="bg-slate-50 p-2.5 rounded-xl border border-slate-200/80">
                    <div className="flex justify-between text-xs font-bold mb-1">
                      <span className="text-slate-700">Daily Events:</span>
                      <span className="text-teal-800 font-extrabold">{simActivities} activities/day</span>
                    </div>
                    <input
                      type="range"
                      min="1"
                      max="6"
                      value={simActivities}
                      onChange={(e) => setSimActivities(Number(e.target.value))}
                      className="w-full accent-teal-600 cursor-pointer h-1.5"
                    />
                  </div>

                  <div className="bg-slate-50 p-2.5 rounded-xl border border-slate-200/80">
                    <div className="flex justify-between text-xs font-bold mb-1">
                      <span className="text-slate-700">Transit Buffer:</span>
                      <span className="text-teal-800 font-extrabold">{simBuffer} minutes</span>
                    </div>
                    <input
                      type="range"
                      min="15"
                      max="75"
                      step="5"
                      value={simBuffer}
                      onChange={(e) => setSimBuffer(Number(e.target.value))}
                      className="w-full accent-teal-600 cursor-pointer h-1.5"
                    />
                  </div>

                  <div className="bg-slate-50 p-2.5 rounded-xl border border-slate-200/80">
                    <div className="flex justify-between text-xs font-bold mb-1">
                      <span className="text-slate-700">Group Size:</span>
                      <span className="text-teal-800 font-extrabold">{simTravelers} {simTravelers === 1 ? 'traveler' : 'travelers'}</span>
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
                <div className={`p-3 rounded-2xl border text-xs font-medium ${
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
              </div>

              {/* Direct CTA */}
              <button
                onClick={() => navigate('/trips/new')}
                className="w-full py-2.5 btn-teal text-xs font-bold rounded-2xl shadow-sm flex items-center justify-center space-x-2 transition"
              >
                <span>Plan Custom Itinerary</span>
                <ArrowRight className="w-3.5 h-3.5" />
              </button>

            </div>
          </div>

        </div>

        {/* 3-Card Feature Grid matching Bottom Row in Image */}
        <div className="mt-12 grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white/95 rounded-[24px] p-6 border border-slate-200/80 shadow-sm space-y-2 hover:border-teal-500/40 hover:shadow-md transition">
            <h4 className="text-base font-black text-blue-950 font-heading">
              Itinerary Scoring
            </h4>
            <p className="text-xs text-slate-700 leading-relaxed font-medium">
              Explainable scheduling across flights, stays, activities, and transit buffer usage.
            </p>
          </div>

          <div className="bg-white/95 rounded-[24px] p-6 border border-slate-200/80 shadow-sm space-y-2 hover:border-teal-500/40 hover:shadow-md transition">
            <h4 className="text-base font-black text-blue-950 font-heading">
              At-Risk Conflict Detection
            </h4>
            <p className="text-xs text-slate-700 leading-relaxed font-medium">
              Rule-based thresholds highlight travel overlaps needing early itinerary adjustments.
            </p>
          </div>

          <div className="bg-white/95 rounded-[24px] p-6 border border-slate-200/80 shadow-sm space-y-2 hover:border-teal-500/40 hover:shadow-md transition">
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
          <div className="bg-white p-2.5 rounded-full border border-slate-200/90 shadow-sm flex items-center space-x-3">
            <Search className="w-5 h-5 text-teal-600 ml-4 flex-shrink-0" />
            <input
              type="text"
              placeholder="Search by city, vibe, or continent (e.g., Tokyo, Paris, Bali, Rome)..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full bg-transparent text-slate-800 placeholder-slate-400 outline-none text-sm font-medium"
            />
            <button
              type="submit"
              className="px-6 py-2.5 bg-teal-600 hover:bg-teal-700 text-white font-bold rounded-full text-xs transition"
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
            className="mt-3 sm:mt-0 flex items-center space-x-1.5 text-xs font-bold text-teal-700 hover:text-teal-800 transition"
          >
            <span>View All 32 Destinations</span>
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
                className="group bg-white rounded-3xl overflow-hidden border border-slate-200/80 hover:border-teal-500/50 hover:shadow-lg transition-all duration-300 cursor-pointer"
              >
                <div className="relative h-56 overflow-hidden">
                  <img
                    src={displayImage}
                    alt={dest.name}
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-slate-950/70 via-transparent to-transparent" />
                  
                  {/* Rating Badge */}
                  <div className="absolute top-3.5 right-3.5 bg-white/90 backdrop-blur-md px-2.5 py-1 rounded-full border border-white/50 flex items-center space-x-1 text-slate-800 text-xs font-bold shadow-sm">
                    <Star className="w-3.5 h-3.5 fill-amber-400 text-amber-400" />
                    <span>{dest.rating || 4.9}</span>
                  </div>

                  {/* Vibe Tag */}
                  <div className="absolute top-3.5 left-3.5 bg-teal-600 text-white px-3 py-1 rounded-full text-[11px] font-bold uppercase tracking-wider shadow-sm">
                    {vibe}
                  </div>

                  <div className="absolute bottom-3.5 left-4 right-4">
                    <h3 className="text-xl font-black text-white font-heading">{dest.name}</h3>
                    <div className="flex items-center space-x-1 text-slate-200 text-xs font-medium">
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
                      <span className="text-slate-700 text-[10px] uppercase block font-extrabold">Avg. Daily Budget</span>
                      <span className="text-teal-700 font-black text-sm">${cost}</span> / day
                    </div>
                    <button className="px-4 py-1.5 bg-slate-900 hover:bg-teal-600 text-white rounded-full text-xs font-bold transition">
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

