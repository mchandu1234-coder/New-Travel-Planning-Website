import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { destinationApi, catalogApi } from '../api/client';
import WeatherWidget from '../components/WeatherWidget';
import { 
  MapPin, Star, Calendar, DollarSign, Sparkles, 
  Utensils, Camera, Compass, ArrowRight, ShieldCheck 
} from 'lucide-react';

export default function DestinationDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [destination, setDestination] = useState(null);
  const [activities, setActivities] = useState([]);
  const [restaurants, setRestaurants] = useState([]);
  const [activeTab, setActiveTab] = useState('overview');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    destinationApi.getById(id)
      .then((res) => {
        if (res.data.success) {
          setDestination(res.data.data);
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));

    catalogApi.getActivities(id)
      .then((res) => {
        if (res.data.success) {
          const acts = Array.isArray(res.data.data) ? res.data.data : (res.data.data?.content || []);
          setActivities(acts);
        }
      })
      .catch(console.error);

    catalogApi.getRestaurants(id)
      .then((res) => {
        if (res.data.success) {
          const rests = Array.isArray(res.data.data) ? res.data.data : (res.data.data?.content || []);
          setRestaurants(rests);
        }
      })
      .catch(console.error);
  }, [id]);

  if (loading || !destination) {
    return (
      <div className="min-h-screen bg-slate-950 flex justify-center items-center">
        <div className="w-10 h-10 border-4 border-cyan-400 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  const displayImage = destination.heroImageUrl || destination.imageUrl || 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=1600&q=80';
  const cost = destination.averageDailyCost || destination.avgCostPerDay || 150;
  const vibe = Array.isArray(destination.vibeTags) ? destination.vibeTags.join(', ') : (destination.vibeTags || destination.vibe || 'Cultural Destination');
  const weather = destination.liveWeather || destination.weather;

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 bg-mesh pb-20">
      
      {/* Hero Header Banner */}
      <div className="relative h-[480px] overflow-hidden">
        <img
          src={displayImage}
          alt={destination.name}
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-slate-950/60 to-transparent" />

        <div className="absolute bottom-10 left-0 right-0 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
            <div className="space-y-3">
              <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-cyan-500/20 backdrop-blur-md border border-cyan-500/30 text-cyan-300 text-xs font-bold uppercase tracking-wider">
                <Compass className="w-3.5 h-3.5" />
                <span>{vibe}</span>
              </div>
              <h1 className="text-4xl sm:text-6xl font-black text-slate-100 font-heading">
                {destination.name}
              </h1>
              <div className="flex items-center space-x-4 text-sm font-semibold text-slate-300">
                <div className="flex items-center space-x-1">
                  <MapPin className="w-4 h-4 text-cyan-400" />
                  <span>{destination.city ? `${destination.city}, ` : ''}{destination.country}, {destination.continent}</span>
                </div>
                <div className="flex items-center space-x-1 text-amber-400">
                  <Star className="w-4 h-4 fill-amber-400" />
                  <span>{destination.rating || 4.8} / 5.0</span>
                </div>
              </div>
            </div>

            {/* CTA Button */}
            <button
              onClick={() => navigate(`/trips/new?destinationId=${destination.id}`)}
              className="px-8 py-4 bg-gradient-to-r from-cyan-500 via-sky-500 to-indigo-600 hover:opacity-95 text-slate-950 font-black rounded-2xl text-base shadow-2xl shadow-cyan-500/30 transition transform hover:scale-105 flex items-center space-x-3"
            >
              <Sparkles className="w-5 h-5" />
              <span>Plan Trip to {destination.name}</span>
              <ArrowRight className="w-5 h-5" />
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-10">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
          
          {/* Main Content Area */}
          <div className="lg:col-span-2 space-y-8">
            
            {/* Nav Tabs */}
            <div className="flex bg-slate-900/80 p-1.5 rounded-2xl border border-slate-800">
              {[
                { id: 'overview', label: 'Overview', icon: Compass },
                { id: 'activities', label: `Attractions (${activities.length})`, icon: Camera },
                { id: 'dining', label: `Dining (${restaurants.length})`, icon: Utensils },
              ].map((tab) => {
                const Icon = tab.icon;
                return (
                  <button
                    key={tab.id}
                    onClick={() => setActiveTab(tab.id)}
                    className={`flex-1 py-3 px-4 rounded-xl text-xs font-bold transition flex items-center justify-center space-x-2 ${
                      activeTab === tab.id
                        ? 'bg-cyan-500 text-slate-950 shadow-lg shadow-cyan-500/20'
                        : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'
                    }`}
                  >
                    <Icon className="w-4 h-4" />
                    <span>{tab.label}</span>
                  </button>
                );
              })}
            </div>

            {/* Tab: Overview */}
            {activeTab === 'overview' && (
              <div className="space-y-6 animate-in fade-in duration-300">
                <div className="glass-panel p-6 rounded-3xl border border-slate-800/80 space-y-4">
                  <h3 className="text-xl font-bold text-slate-100 font-heading">About {destination.name}</h3>
                  <p className="text-sm text-slate-300 leading-relaxed">
                    {destination.description}
                  </p>

                  <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 pt-4 border-t border-slate-800">
                    <div className="bg-slate-950/60 p-3 rounded-2xl border border-slate-800">
                      <span className="text-[10px] font-bold uppercase text-slate-400">Best Season</span>
                      <p className="text-sm font-bold text-cyan-400">{destination.bestTimeToVisit || destination.bestSeason || 'Year-round'}</p>
                    </div>
                    <div className="bg-slate-950/60 p-3 rounded-2xl border border-slate-800">
                      <span className="text-[10px] font-bold uppercase text-slate-400">Avg. Daily Budget</span>
                      <p className="text-sm font-bold text-emerald-400">${cost} / day</p>
                    </div>
                    <div className="bg-slate-950/60 p-3 rounded-2xl border border-slate-800">
                      <span className="text-[10px] font-bold uppercase text-slate-400">Currency</span>
                      <p className="text-sm font-bold text-indigo-400">{destination.currency || 'USD'}</p>
                    </div>
                  </div>
                </div>
              </div>
            )}

            {/* Tab: Activities */}
            {activeTab === 'activities' && (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 animate-in fade-in duration-300">
                {activities.map((act) => (
                  <div key={act.id} className="glass-card p-5 rounded-3xl border border-slate-800 space-y-3">
                    {act.imageUrl && (
                      <img src={act.imageUrl} alt={act.name} className="w-full h-40 object-cover rounded-2xl" />
                    )}
                    <div className="flex justify-between items-start">
                      <h4 className="text-base font-bold text-slate-100 font-heading">{act.name}</h4>
                      <span className="text-xs font-black text-emerald-400">${act.price || act.cost || 0}</span>
                    </div>
                    <p className="text-xs text-slate-400 line-clamp-2">{act.description}</p>
                    <div className="flex justify-between items-center text-[11px] text-slate-400 pt-2 border-t border-slate-800">
                      <span>⏱ {act.durationHours ? `${act.durationHours} hrs` : `${act.durationMinutes || 90} mins`}</span>
                      <span className="text-cyan-400 font-bold">{act.category}</span>
                    </div>
                  </div>
                ))}
              </div>
            )}

            {/* Tab: Dining */}
            {activeTab === 'dining' && (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 animate-in fade-in duration-300">
                {restaurants.map((rest) => (
                  <div key={rest.id} className="glass-card p-5 rounded-3xl border border-slate-800 space-y-3">
                    {rest.imageUrl && (
                      <img src={rest.imageUrl} alt={rest.name} className="w-full h-40 object-cover rounded-2xl" />
                    )}
                    <div className="flex justify-between items-start">
                      <h4 className="text-base font-bold text-slate-100 font-heading">{rest.name}</h4>
                      <span className="text-xs font-black text-amber-400">{rest.priceRange || '$$$'}</span>
                    </div>
                    <p className="text-xs text-slate-400 line-clamp-2">{rest.description || rest.specialties}</p>
                    <div className="flex justify-between items-center text-[11px] text-slate-400 pt-2 border-t border-slate-800">
                      <span>🍽 {rest.cuisineType || rest.cuisine || 'Local'} Cuisine</span>
                      <span className="text-amber-400 font-bold">★ {rest.rating || 4.8}</span>
                    </div>
                  </div>
                ))}
              </div>
            )}

          </div>

          {/* Sidebar: Live Weather */}
          <div className="space-y-6">
            <WeatherWidget weatherData={weather} cityName={destination.name} />

            <div className="glass-panel p-6 rounded-3xl border border-slate-800/80 space-y-4">
              <h4 className="text-sm font-bold text-slate-200 uppercase tracking-wider font-heading">
                Traveler Highlights
              </h4>
              <ul className="space-y-3 text-xs text-slate-300">
                <li className="flex items-center space-x-2">
                  <ShieldCheck className="w-4 h-4 text-emerald-400" />
                  <span>Rated safe for solo & group travelers</span>
                </li>
                <li className="flex items-center space-x-2">
                  <Sparkles className="w-4 h-4 text-cyan-400" />
                  <span>Smart auto-itinerary templates available</span>
                </li>
              </ul>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
}
