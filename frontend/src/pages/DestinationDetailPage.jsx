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
      <div className="min-h-screen bg-[#f7faf9] flex justify-center items-center">
        <div className="w-10 h-10 border-4 border-teal-600 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  const displayImage = destination.heroImageUrl || destination.imageUrl || 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=1600&q=80';
  const cost = destination.averageDailyCost || destination.avgCostPerDay || 150;
  const vibe = Array.isArray(destination.vibeTags) ? destination.vibeTags.join(', ') : (destination.vibeTags || destination.vibe || 'Cultural Destination');
  const weather = destination.liveWeather || destination.weather;

  return (
    <div className="min-h-screen bg-mesh text-slate-900 pb-20">
      
      {/* Hero Header Banner */}
      <div className="relative h-[440px] overflow-hidden">
        <img
          src={displayImage}
          alt={destination.name}
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-slate-950/80 via-slate-950/30 to-transparent" />

        <div className="absolute bottom-10 left-0 right-0 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
            <div className="space-y-3">
              <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-teal-600 text-white text-[11px] font-bold uppercase tracking-wider shadow-sm">
                <Compass className="w-3.5 h-3.5" />
                <span>{vibe}</span>
              </div>
              <h1 className="text-4xl sm:text-6xl font-black text-white font-heading">
                {destination.name}
              </h1>
              <div className="flex items-center space-x-4 text-sm font-semibold text-slate-200">
                <div className="flex items-center space-x-1">
                  <MapPin className="w-4 h-4 text-teal-300" />
                  <span>{destination.city ? `${destination.city}, ` : ''}{destination.country}, {destination.continent}</span>
                </div>
                <div className="flex items-center space-x-1 text-amber-300">
                  <Star className="w-4 h-4 fill-amber-300" />
                  <span>{destination.rating || 4.8} / 5.0</span>
                </div>
              </div>
            </div>

            {/* CTA Button */}
            <button
              onClick={() => navigate(`/trips/new?destinationId=${destination.id}`)}
              className="btn-teal px-8 py-3.5 text-sm font-bold shadow-lg flex items-center space-x-2"
            >
              <Sparkles className="w-4 h-4" />
              <span>Plan Trip to {destination.name}</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-10">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
          
          {/* Main Content Area */}
          <div className="lg:col-span-2 space-y-8">
            
            {/* Nav Tabs */}
            <div className="flex bg-white p-1.5 rounded-full border border-slate-200/80 shadow-sm">
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
                    className={`flex-1 py-2.5 px-4 rounded-full text-xs font-bold transition flex items-center justify-center space-x-2 ${
                      activeTab === tab.id
                        ? 'bg-slate-900 text-white shadow-sm'
                        : 'text-slate-700 hover:text-slate-950 font-bold'
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
                <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm space-y-4">
                  <h3 className="text-xl font-bold text-slate-900 font-heading">About {destination.name}</h3>
                  <p className="text-sm text-slate-800 leading-relaxed font-medium">
                    {destination.description}
                  </p>

                  <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 pt-4 border-t border-slate-100">
                    <div className="bg-slate-50 p-3.5 rounded-2xl border border-slate-200">
                      <span className="text-[10px] font-extrabold uppercase text-slate-700">Best Season</span>
                      <p className="text-sm font-extrabold text-teal-800">{destination.bestTimeToVisit || destination.bestSeason || 'Year-round'}</p>
                    </div>
                    <div className="bg-slate-50 p-3.5 rounded-2xl border border-slate-200">
                      <span className="text-[10px] font-extrabold uppercase text-slate-700">Avg. Daily Budget</span>
                      <p className="text-sm font-extrabold text-teal-700">${cost} / day</p>
                    </div>
                    <div className="bg-slate-50 p-3.5 rounded-2xl border border-slate-200">
                      <span className="text-[10px] font-extrabold uppercase text-slate-700">Currency</span>
                      <p className="text-sm font-extrabold text-slate-900">{destination.currency || 'USD'}</p>
                    </div>
                  </div>
                </div>
              </div>
            )}

            {/* Tab: Activities */}
            {activeTab === 'activities' && (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 animate-in fade-in duration-300">
                {activities.map((act) => (
                  <div key={act.id} className="bg-white p-5 rounded-3xl border border-slate-200/80 shadow-sm space-y-3 hover:border-teal-500/40 transition">
                    {act.imageUrl && (
                      <img src={act.imageUrl} alt={act.name} className="w-full h-40 object-cover rounded-2xl" />
                    )}
                    <div className="flex justify-between items-start">
                      <h4 className="text-base font-bold text-slate-900 font-heading">{act.name}</h4>
                      <span className="text-xs font-black text-teal-700">${act.price || act.cost || 0}</span>
                    </div>
                    <p className="text-xs text-slate-700 line-clamp-2 font-medium">{act.description}</p>
                    <div className="flex justify-between items-center text-[11px] text-slate-700 font-semibold pt-2 border-t border-slate-100">
                      <span>⏱ {act.durationHours ? `${act.durationHours} hrs` : `${act.durationMinutes || 90} mins`}</span>
                      <span className="text-teal-700 font-bold">{act.category}</span>
                    </div>
                  </div>
                ))}
              </div>
            )}

            {/* Tab: Dining */}
            {activeTab === 'dining' && (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 animate-in fade-in duration-300">
                {restaurants.map((rest) => (
                  <div key={rest.id} className="bg-white p-5 rounded-3xl border border-slate-200/80 shadow-sm space-y-3 hover:border-amber-500/40 transition">
                    {rest.imageUrl && (
                      <img src={rest.imageUrl} alt={rest.name} className="w-full h-40 object-cover rounded-2xl" />
                    )}
                    <div className="flex justify-between items-start">
                      <h4 className="text-base font-bold text-slate-900 font-heading">{rest.name}</h4>
                      <span className="text-xs font-black text-amber-600">{rest.priceRange || '$$$'}</span>
                    </div>
                    <p className="text-xs text-slate-700 line-clamp-2 font-medium">{rest.description || rest.specialties}</p>
                    <div className="flex justify-between items-center text-[11px] text-slate-700 font-semibold pt-2 border-t border-slate-100">
                      <span>🍽 {rest.cuisineType || rest.cuisine || 'Local'} Cuisine</span>
                      <span className="text-amber-500 font-bold">★ {rest.rating || 4.8}</span>
                    </div>
                  </div>
                ))}
              </div>
            )}

          </div>

          {/* Sidebar: Live Weather */}
          <div className="space-y-6">
            <WeatherWidget weatherData={weather} cityName={destination.name} />

            <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm space-y-4">
              <h4 className="text-xs font-bold text-slate-900 uppercase tracking-wider font-heading">
                Traveler Highlights
              </h4>
              <ul className="space-y-3 text-xs text-slate-800 font-semibold">
                <li className="flex items-center space-x-2">
                  <ShieldCheck className="w-4 h-4 text-teal-600" />
                  <span>Rated safe for solo & group travelers</span>
                </li>
                <li className="flex items-center space-x-2">
                  <Sparkles className="w-4 h-4 text-teal-600" />
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
