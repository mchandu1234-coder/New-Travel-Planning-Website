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
    <div className="min-h-screen bg-[#f7faf9] bg-mesh text-slate-900 py-10 pb-20">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Header */}
        <div>
          <Link to={`/trips/${id}`} className="btn-interactive text-xs font-bold text-cyan-700 hover:text-cyan-900 flex items-center space-x-1 mb-2">
            <ArrowLeft className="w-3.5 h-3.5" />
            <span>Back to Trip Command Center</span>
          </Link>
          <h1 className="text-3xl font-black text-blue-950 font-heading">
            Attractions & Local Dining Explorer
          </h1>
        </div>

        {addedItemName && (
          <div className="p-4 bg-emerald-50 border border-emerald-200 rounded-2xl text-emerald-800 text-xs font-bold flex items-center space-x-2 animate-in fade-in duration-300 shadow-sm">
            <CheckCircle2 className="w-4 h-4 text-emerald-600" />
            <span>Added "{addedItemName}" to your itinerary Day 1!</span>
          </div>
        )}

        {/* Tab Switcher */}
        <div className="flex bg-slate-100 p-1.5 rounded-2xl border border-slate-200 max-w-md shadow-inner">
          <button
            onClick={() => setActiveTab('activities')}
            className={`btn-interactive flex-1 py-3 text-xs font-black rounded-xl transition flex items-center justify-center space-x-2 ${
              activeTab === 'activities' ? 'btn-action-cyan shadow-md shadow-cyan-500/25' : 'text-slate-700 hover:text-cyan-900 hover:bg-white'
            }`}
          >
            <Camera className="w-4 h-4" />
            <span>Top Sightseeing ({activities.length})</span>
          </button>
          <button
            onClick={() => setActiveTab('dining')}
            className={`btn-interactive flex-1 py-3 text-xs font-black rounded-xl transition flex items-center justify-center space-x-2 ${
              activeTab === 'dining' ? 'btn-action-amber shadow-md shadow-amber-500/25' : 'text-slate-700 hover:text-amber-900 hover:bg-white'
            }`}
          >
            <Utensils className="w-4 h-4" />
            <span>Dining & Food ({restaurants.length})</span>
          </button>
        </div>

        {/* Activities Grid */}
        {activeTab === 'activities' && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {activities.map((act) => (
              <div key={act.id} className="glass-panel p-5 rounded-3xl border border-slate-200 space-y-4 hover:border-cyan-400 hover:shadow-xl transition-all duration-300 shadow-sm">
                <img src={act.imageUrl} alt={act.name} className="w-full h-44 object-cover rounded-2xl" />
                <div>
                  <h4 className="text-base font-black text-slate-900 font-heading">{act.name}</h4>
                  <p className="text-xs text-slate-600 line-clamp-2 mt-1 font-medium">{act.description}</p>
                </div>
                <div className="flex justify-between items-center pt-3 border-t border-slate-100">
                  <span className="text-sm font-black text-emerald-800">${act.cost}</span>
                  <button
                    onClick={() => handleQuickAdd(act, 'activity')}
                    className="btn-interactive px-4 py-2 bg-cyan-50 hover:bg-gradient-to-r hover:from-cyan-500 hover:to-sky-500 hover:text-white text-cyan-800 border border-cyan-200/90 rounded-xl text-xs font-bold transition-all shadow-sm"
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
              <div key={rest.id} className="glass-panel p-5 rounded-3xl border border-slate-200 space-y-4 hover:border-amber-400 hover:shadow-xl transition-all duration-300 shadow-sm">
                <img src={rest.imageUrl} alt={rest.name} className="w-full h-44 object-cover rounded-2xl" />
                <div>
                  <h4 className="text-base font-black text-slate-900 font-heading">{rest.name}</h4>
                  <p className="text-xs text-slate-600 line-clamp-2 mt-1 font-medium">{rest.description}</p>
                </div>
                <div className="flex justify-between items-center pt-3 border-t border-slate-100">
                  <span className="text-xs font-black text-amber-800 font-heading">🍽 {rest.cuisine}</span>
                  <button
                    onClick={() => handleQuickAdd(rest, 'dining')}
                    className="btn-interactive px-4 py-2 bg-amber-50 hover:bg-gradient-to-r hover:from-amber-500 hover:to-orange-500 hover:text-white text-amber-800 border border-amber-200/90 rounded-xl text-xs font-bold transition-all shadow-sm"
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
