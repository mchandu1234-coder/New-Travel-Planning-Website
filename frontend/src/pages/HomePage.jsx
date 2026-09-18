import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  Sparkles, Compass, Search, Calendar, Users, MapPin, 
  ArrowRight, CheckCircle2, ShieldCheck, Zap, Globe, Heart, Star 
} from 'lucide-react';
import { destinationApi } from '../api/client';

export default function HomePage() {
  const [destinations, setDestinations] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
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

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 bg-mesh">
      
      {/* Hero Section */}
      <section className="relative pt-12 pb-24 md:pt-20 md:pb-32 overflow-hidden">
        {/* Glow Spheres */}
        <div className="absolute top-1/4 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-cyan-500/15 rounded-full blur-[140px] pointer-events-none" />
        <div className="absolute top-1/3 right-10 w-[400px] h-[400px] bg-indigo-500/15 rounded-full blur-[120px] pointer-events-none" />

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
          <div className="text-center max-w-3xl mx-auto space-y-6">
            
            {/* Pill Badge */}
            <div className="inline-flex items-center space-x-2 px-4 py-2 rounded-full bg-slate-900/80 border border-slate-800 text-cyan-400 text-xs font-bold shadow-xl backdrop-blur-md">
              <Sparkles className="w-4 h-4 text-cyan-400 animate-pulse" />
              <span>Next-Gen Smart Travel Platform</span>
            </div>

            {/* Main Headline */}
            <h1 className="text-4xl sm:text-6xl md:text-7xl font-black text-slate-100 tracking-tight leading-none font-heading">
              Plan Dream Journeys <br />
              <span className="text-gradient">Without the Stress</span>
            </h1>

            {/* Subtitle */}
            <p className="text-lg sm:text-xl text-slate-400 leading-relaxed font-normal">
              Discover iconic places, craft day-by-day itineraries with conflict detection, 
              book flights & hotels, and settle split expenses in real-time.
            </p>

            {/* Search Bar */}
            <form onSubmit={handleSearch} className="pt-4 max-w-2xl mx-auto">
              <div className="glass-panel p-2 rounded-3xl border border-slate-700/60 shadow-2xl flex flex-col sm:flex-row items-center space-y-2 sm:space-y-0 sm:space-x-2">
                <div className="flex-1 flex items-center px-4 w-full">
                  <Search className="w-5 h-5 text-cyan-400 mr-3 flex-shrink-0" />
                  <input
                    type="text"
                    placeholder="Where do you want to travel? (e.g., Paris, Tokyo, Bali)"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="w-full bg-transparent text-slate-100 placeholder-slate-400 outline-none text-sm py-2 font-medium"
                  />
                </div>
                <button
                  type="submit"
                  className="w-full sm:w-auto px-6 py-3.5 bg-gradient-to-r from-cyan-500 via-sky-500 to-indigo-600 hover:opacity-95 text-slate-950 font-black rounded-2xl text-sm shadow-xl shadow-cyan-500/25 transition flex items-center justify-center space-x-2"
                >
                  <span>Explore Now</span>
                  <ArrowRight className="w-4 h-4" />
                </button>
              </div>
            </form>

            {/* Quick Stats */}
            <div className="pt-10 grid grid-cols-2 sm:grid-cols-4 gap-6 max-w-4xl mx-auto">
              {[
                { label: 'Destinations', value: '250+' },
                { label: 'Trips Planned', value: '14,200+' },
                { label: 'Live Travelers', value: '8,500+' },
                { label: 'Customer Rating', value: '4.9/5' },
              ].map((stat, i) => (
                <div key={i} className="glass-card p-4 rounded-2xl text-center">
                  <div className="text-2xl font-black text-cyan-400 font-heading">{stat.value}</div>
                  <div className="text-xs font-semibold text-slate-400 mt-0.5">{stat.label}</div>
                </div>
              ))}
            </div>

          </div>
        </div>
      </section>

      {/* Featured Destinations Carousel Grid */}
      <section className="py-16 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col md:flex-row md:items-end justify-between mb-10">
          <div>
            <div className="text-xs font-bold text-cyan-400 uppercase tracking-widest mb-1">Handpicked Escapes</div>
            <h2 className="text-3xl sm:text-4xl font-black text-slate-100 font-heading">
              Trending Destinations
            </h2>
          </div>
          <Link
            to="/destinations"
            className="mt-4 md:mt-0 flex items-center space-x-2 text-sm font-bold text-cyan-400 hover:text-cyan-300 transition"
          >
            <span>View All Destinations</span>
            <ArrowRight className="w-4 h-4" />
          </Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {destinations.map((dest) => {
            const displayImage = dest.heroImageUrl || dest.imageUrl || 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80';
            const cost = dest.averageDailyCost || dest.avgCostPerDay || 150;
            const vibe = Array.isArray(dest.vibeTags) ? dest.vibeTags[0] : (typeof dest.vibeTags === 'string' ? dest.vibeTags.split(',')[0] : (dest.vibe || 'Cultural'));

            return (
              <div
                key={dest.id}
                onClick={() => navigate(`/destinations/${dest.id}`)}
                className="group glass-panel rounded-3xl overflow-hidden border border-slate-800/80 hover:border-cyan-500/50 transition-all duration-500 cursor-pointer hover:-translate-y-2 hover:shadow-2xl hover:shadow-cyan-500/10"
              >
                <div className="relative h-64 overflow-hidden">
                  <img
                    src={displayImage}
                    alt={dest.name}
                    className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-slate-950/20 to-transparent" />
                  
                  {/* Rating Badge */}
                  <div className="absolute top-4 right-4 bg-slate-950/80 backdrop-blur-md px-3 py-1 rounded-full border border-slate-800 flex items-center space-x-1 text-amber-400 text-xs font-bold">
                    <Star className="w-3.5 h-3.5 fill-amber-400" />
                    <span>{dest.rating || 4.8}</span>
                  </div>

                  {/* Vibe Tag */}
                  <div className="absolute top-4 left-4 bg-cyan-500/20 backdrop-blur-md border border-cyan-500/30 text-cyan-300 px-3 py-1 rounded-full text-xs font-extrabold uppercase tracking-wider">
                    {vibe}
                  </div>

                  {/* Destination info inside image */}
                  <div className="absolute bottom-4 left-4 right-4">
                    <h3 className="text-2xl font-black text-slate-100 font-heading">{dest.name}</h3>
                    <div className="flex items-center space-x-1 text-slate-300 text-xs font-medium">
                      <MapPin className="w-3.5 h-3.5 text-cyan-400" />
                      <span>{dest.country}</span>
                    </div>
                  </div>
                </div>

                <div className="p-5 space-y-4">
                  <p className="text-xs text-slate-400 line-clamp-2 leading-relaxed">
                    {dest.description}
                  </p>

                  <div className="flex items-center justify-between pt-3 border-t border-slate-800/80 text-xs font-semibold text-slate-300">
                    <div>
                      <span className="text-slate-500 text-[10px] uppercase block font-bold">Avg. Daily Budget</span>
                      <span className="text-emerald-400 font-black text-sm">${cost}</span> / day
                    </div>
                    <button className="px-4 py-2 bg-slate-800 hover:bg-cyan-500 hover:text-slate-950 text-cyan-400 rounded-xl font-bold transition">
                      Explore
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </section>

      {/* Interactive Workflow Section */}
      <section className="py-20 bg-slate-900/40 border-y border-slate-800/80">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-2xl mx-auto mb-16 space-y-3">
            <span className="text-xs font-bold text-cyan-400 uppercase tracking-widest">Everything You Need</span>
            <h2 className="text-3xl sm:text-4xl font-black text-slate-100 font-heading">
              Four Steps to Your Perfect Journey
            </h2>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            {[
              {
                step: '01',
                title: 'Choose Destination',
                desc: 'Explore curations with live weather, budget insights, top sights, and local dining.',
                icon: Compass,
                color: 'from-cyan-500 to-sky-500',
              },
              {
                step: '02',
                title: 'Build Itinerary',
                desc: 'Drag & drop activities, flight slots, and hotel stays with live conflict detection.',
                icon: Calendar,
                color: 'from-sky-500 to-indigo-500',
              },
              {
                step: '03',
                title: 'Book & Settle Budget',
                desc: 'Simulate bookings and automatically calculate split-expense debts among friends.',
                icon: Zap,
                color: 'from-indigo-500 to-purple-500',
              },
              {
                step: '04',
                title: 'Live Sync & Export',
                desc: 'Collaborate live via WebSockets and export print-ready PDF & iCalendar files.',
                icon: Globe,
                color: 'from-purple-500 to-pink-500',
              },
            ].map((item, idx) => {
              const Icon = item.icon;
              return (
                <div key={idx} className="glass-panel p-6 rounded-3xl border border-slate-800/80 relative space-y-4">
                  <div className={`w-12 h-12 rounded-2xl bg-gradient-to-tr ${item.color} p-0.5 shadow-lg`}>
                    <div className="w-full h-full bg-slate-950 rounded-[14px] flex items-center justify-center">
                      <Icon className="w-6 h-6 text-cyan-400" />
                    </div>
                  </div>
                  <span className="text-xs font-black text-slate-500 font-heading">{item.step}</span>
                  <h3 className="text-xl font-bold text-slate-100 font-heading">{item.title}</h3>
                  <p className="text-xs text-slate-400 leading-relaxed">{item.desc}</p>
                </div>
              );
            })}
          </div>

          {/* Start CTA */}
          <div className="mt-16 text-center">
            <Link
              to="/trips/new"
              className="inline-flex items-center space-x-3 px-8 py-4 bg-gradient-to-r from-cyan-500 via-sky-500 to-indigo-600 hover:opacity-95 text-slate-950 font-black rounded-2xl text-base shadow-2xl shadow-cyan-500/25 transition transform hover:scale-105"
            >
              <Sparkles className="w-5 h-5" />
              <span>Launch Interactive Trip Wizard</span>
            </Link>
          </div>
        </div>
      </section>

    </div>
  );
}
