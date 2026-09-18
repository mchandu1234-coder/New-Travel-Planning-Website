import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { tripApi, catalogApi, itineraryApi } from '../api/client';
import { 
  Camera, Utensils, Star, Plus, ArrowLeft, 
  MapPin, CheckCircle2, Compass, Sparkles 
} from 'lucide-react';

export default function ActivitiesPage() {
  const { id } = useParams();
  const [trip, setTrip] = useState(null);
  const [activities, setActivities] = useState([]);
  const [restaurants, setRestaurants] = useState([]);
  const [activeTab, setActiveTab] = useState('activities');
  const [loading, setLoading] = useState(true);
  const [addedItemName, setAddedItemName] = useState(null);

  useEffect(() => {
    tripApi.getById(id)
      .then((res) => {
        if (res.data.success) {
          const tripData = res.data.data;
          setTrip(tripData);
          if (tripData.destination?.id) {
            catalogApi.getActivities(tripData.destination.id)
              .then((aRes) => { if (aRes.data.success) setActivities(aRes.data.data); });
            catalogApi.getRestaurants(tripData.destination.id)
              .then((rRes) => { if (rRes.data.success) setRestaurants(rRes.data.data); });
          }
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [id]);

  const handleQuickAdd = async (item, type) => {
    const days = trip?.days || trip?.itineraryDays || [];
    if (days.length === 0) return;
    const firstDay = days[0];

    try {
      await itineraryApi.addItem(id, {
        dayId: firstDay.id,
        title: item.name,
        itemType: type === 'activity' ? 'ACTIVITY' : 'RESTAURANT',
        startTime: type === 'activity' ? '11:00' : '19:30',
        endTime: type === 'activity' ? '13:00' : '21:00',
        locationName: item.address || item.name || trip.destination?.name,
        estimatedCost: item.cost || item.price || 0,
      });

      setAddedItemName(item.name);
      setTimeout(() => setAddedItemName(null), 2500);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 py-10 bg-mesh pb-20">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Header */}
        <div>
          <Link to={`/trips/${id}`} className="text-xs font-bold text-cyan-400 hover:underline flex items-center space-x-1 mb-2">
            <ArrowLeft className="w-3.5 h-3.5" />
            <span>Back to Trip Command Center</span>
          </Link>
          <h1 className="text-3xl font-black text-slate-100 font-heading">
            Attractions & Local Dining Explorer
          </h1>
        </div>

        {addedItemName && (
          <div className="p-4 bg-emerald-500/10 border border-emerald-500/30 rounded-2xl text-emerald-300 text-xs font-bold flex items-center space-x-2 animate-in fade-in duration-300">
            <CheckCircle2 className="w-4 h-4 text-emerald-400" />
            <span>Added "{addedItemName}" to your itinerary Day 1!</span>
          </div>
        )}

        {/* Tab Switcher */}
        <div className="flex bg-slate-900/80 p-1.5 rounded-2xl border border-slate-800 max-w-md">
          <button
            onClick={() => setActiveTab('activities')}
            className={`flex-1 py-3 text-xs font-bold rounded-xl transition flex items-center justify-center space-x-2 ${
              activeTab === 'activities' ? 'bg-cyan-500 text-slate-950 shadow-md' : 'text-slate-400 hover:text-slate-100'
            }`}
          >
            <Camera className="w-4 h-4" />
            <span>Top Sightseeing ({activities.length})</span>
          </button>
          <button
            onClick={() => setActiveTab('dining')}
            className={`flex-1 py-3 text-xs font-bold rounded-xl transition flex items-center justify-center space-x-2 ${
              activeTab === 'dining' ? 'bg-amber-500 text-slate-950 shadow-md' : 'text-slate-400 hover:text-slate-100'
            }`}
          >
            <Utensils className="w-4 h-4" />
            <span>Dining & Restaurants ({restaurants.length})</span>
          </button>
        </div>

        {/* Activities Grid */}
        {activeTab === 'activities' && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {activities.map((act) => (
              <div key={act.id} className="glass-panel p-5 rounded-3xl border border-slate-800 space-y-4 hover:border-cyan-500/50 transition">
                <img src={act.imageUrl} alt={act.name} className="w-full h-44 object-cover rounded-2xl" />
                <div>
                  <h4 className="text-base font-bold text-slate-100 font-heading">{act.name}</h4>
                  <p className="text-xs text-slate-400 line-clamp-2 mt-1">{act.description}</p>
                </div>
                <div className="flex justify-between items-center pt-3 border-t border-slate-800">
                  <span className="text-sm font-black text-emerald-400">${act.cost}</span>
                  <button
                    onClick={() => handleQuickAdd(act, 'activity')}
                    className="px-4 py-2 bg-slate-800 hover:bg-cyan-500 hover:text-slate-950 text-cyan-400 rounded-xl text-xs font-bold transition flex items-center space-x-1"
                  >
                    <Plus className="w-3.5 h-3.5" />
                    <span>Add to Itinerary</span>
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Dining Grid */}
        {activeTab === 'dining' && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {restaurants.map((rest) => (
              <div key={rest.id} className="glass-panel p-5 rounded-3xl border border-slate-800 space-y-4 hover:border-amber-500/50 transition">
                <img src={rest.imageUrl} alt={rest.name} className="w-full h-44 object-cover rounded-2xl" />
                <div>
                  <h4 className="text-base font-bold text-slate-100 font-heading">{rest.name}</h4>
                  <p className="text-xs text-slate-400 line-clamp-2 mt-1">{rest.description}</p>
                </div>
                <div className="flex justify-between items-center pt-3 border-t border-slate-800">
                  <span className="text-xs font-black text-amber-400 font-heading">🍽 {rest.cuisine}</span>
                  <button
                    onClick={() => handleQuickAdd(rest, 'dining')}
                    className="px-4 py-2 bg-slate-800 hover:bg-amber-500 hover:text-slate-950 text-amber-400 rounded-xl text-xs font-bold transition flex items-center space-x-1"
                  >
                    <Plus className="w-3.5 h-3.5" />
                    <span>Add to Itinerary</span>
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

      </div>
    </div>
  );
}
