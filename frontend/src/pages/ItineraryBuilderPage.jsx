import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { itineraryApi, tripApi } from '../api/client';
import { useWebSocket } from '../context/WebSocketContext';
import ConflictAlert from '../components/ConflictAlert';
import { 
  Calendar, Clock, Plus, Trash2, GripVertical, AlertTriangle, 
  MapPin, DollarSign, Sparkles, Plane, Hotel, Camera, Utensils, 
  ArrowLeft, X, CheckCircle2, Circle 
} from 'lucide-react';

export default function ItineraryBuilderPage() {
  const { id } = useParams();
  const [days, setDays] = useState([]);
  const [activeDayIndex, setActiveDayIndex] = useState(0);
  const [conflicts, setConflicts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAddModal, setShowAddModal] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [modalError, setModalError] = useState('');
  const { subscribeToTrip, sendItineraryUpdate } = useWebSocket();

  // Completed items tracking
  const [completedItems, setCompletedItems] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem(`trip_${id}_completed`) || '[]');
    } catch {
      return [];
    }
  });

  const toggleComplete = (itemId) => {
    setCompletedItems((prev) => {
      const next = prev.includes(itemId) ? prev.filter((i) => i !== itemId) : [...prev, itemId];
      localStorage.setItem(`trip_${id}_completed`, JSON.stringify(next));
      return next;
    });
  };

  // Form state for new item
  const [title, setTitle] = useState('');
  const [itemType, setItemType] = useState('ACTIVITY');
  const [startTime, setStartTime] = useState('10:00');
  const [endTime, setEndTime] = useState('12:00');
  const [locationName, setLocationName] = useState('');
  const [cost, setCost] = useState(0);

  const applyPreset = (presetTitle, type, start, end, presetCost) => {
    setTitle(presetTitle);
    setItemType(type);
    setStartTime(start);
    setEndTime(end);
    setCost(presetCost);
  };

  useEffect(() => {
    fetchItinerary();
    
    // Subscribe to STOMP WebSocket updates for this trip
    const unsubscribe = subscribeToTrip(id, (updatedItinerary) => {
      if (Array.isArray(updatedItinerary)) {
        setDays(updatedItinerary);
        checkConflicts();
      }
    });

    return () => {
      if (unsubscribe) unsubscribe();
    };
  }, [id]);

  const fetchItinerary = () => {
    setLoading(true);
    itineraryApi.getDays(id)
      .then((res) => {
        if (res.data.success) {
          setDays(res.data.data);
          checkConflicts();
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  const checkConflicts = () => {
    tripApi.checkConflicts(id)
      .then((res) => {
        if (res.data.success) setConflicts(res.data.data);
      })
      .catch(console.error);
  };

  const handleAddItem = async (e) => {
    e.preventDefault();
    const currentDay = days[activeDayIndex];
    if (!currentDay) return;

    setIsSubmitting(true);
    setModalError('');

    const numCost = parseFloat(cost) || 0;
    const cleanType = itemType === 'SIGHTSEEING' ? 'ACTIVITY' : (itemType === 'HOTEL' ? 'ACCOMMODATION' : itemType);

    try {
      const res = await itineraryApi.addItem(id, {
        dayId: currentDay.id,
        title: title.trim(),
        itemType: cleanType,
        startTime,
        endTime,
        locationName,
        estimatedCost: numCost,
        cost: numCost,
      });

      if (res.data && (res.data.success || res.status === 200)) {
        setShowAddModal(false);
        setTitle('');
        setLocationName('');
        setCost(0);
        fetchItinerary();
        sendItineraryUpdate(id, 'ADD_ITEM', res.data.data);
      } else {
        setModalError(res.data?.message || 'Failed to add event');
      }
    } catch (err) {
      console.error('Failed to add itinerary item:', err);
      setModalError(err.response?.data?.message || err.message || 'Failed to save event. Please check connection and try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeleteItem = async (itemId) => {
    try {
      const res = await itineraryApi.deleteItem(id, itemId);
      if (res.data.success) {
        fetchItinerary();
        sendItineraryUpdate(id, 'DELETE_ITEM', { itemId });
      }
    } catch (err) {
      console.error(err);
    }
  };

  const getItemIcon = (type) => {
    switch (type) {
      case 'FLIGHT': return <Plane className="w-5 h-5 text-sky-400" />;
      case 'HOTEL':
      case 'ACCOMMODATION': return <Hotel className="w-5 h-5 text-indigo-400" />;
      case 'RESTAURANT': return <Utensils className="w-5 h-5 text-amber-400" />;
      case 'SIGHTSEEING':
      case 'ACTIVITY': return <Camera className="w-5 h-5 text-purple-400" />;
      default: return <Sparkles className="w-5 h-5 text-cyan-400" />;
    }
  };

  const currentDay = days[activeDayIndex];

  return (
    <div className="min-h-screen bg-[#f7faf9] bg-mesh text-slate-900 py-10 pb-20">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <Link to={`/trips/${id}`} className="text-xs font-bold text-cyan-700 hover:underline flex items-center space-x-1 mb-2">
              <ArrowLeft className="w-3.5 h-3.5" />
              <span>Back to Trip Command Center</span>
            </Link>
            <h1 className="text-3xl font-black text-blue-950 font-heading">
              Day-by-Day Itinerary Planner
            </h1>
          </div>

          <button
            onClick={() => setShowAddModal(true)}
            className="px-6 py-3 bg-gradient-to-r from-cyan-500 via-sky-500 to-indigo-600 hover:opacity-95 text-slate-950 font-black rounded-2xl text-xs shadow-xl shadow-cyan-500/20 transition flex items-center space-x-2"
          >
            <Plus className="w-4 h-4" />
            <span>Add Event to Day {activeDayIndex + 1}</span>
          </button>
        </div>

        {/* Conflicts Alert */}
        <ConflictAlert conflicts={conflicts} />

        {/* Days Tab Switcher */}
        {days.length > 0 && (
          <div className="flex space-x-2 overflow-x-auto pb-2 scrollbar-none">
            {days.map((day, idx) => (
              <button
                key={day.id}
                onClick={() => setActiveDayIndex(idx)}
                className={`px-5 py-3 rounded-2xl text-xs font-extrabold transition flex-shrink-0 flex items-center space-x-2 ${
                  activeDayIndex === idx
                    ? 'bg-gradient-to-r from-cyan-500 to-indigo-600 text-slate-950 shadow-lg shadow-cyan-500/20'
                    : 'bg-white border border-slate-200 text-slate-700 hover:text-slate-950 hover:bg-slate-100 shadow-sm'
                }`}
              >
                <Calendar className="w-4 h-4" />
                <span>Day {day.dayNumber} ({day.date})</span>
              </button>
            ))}
          </div>
        )}

        {/* Active Day Timeline Container */}
        {loading ? (
          <div className="flex justify-center py-20">
            <div className="w-10 h-10 border-4 border-cyan-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : !currentDay || currentDay.items.length === 0 ? (
          <div className="glass-panel p-12 rounded-3xl text-center space-y-4 border border-slate-200 shadow-sm">
            <Calendar className="w-12 h-12 text-slate-400 mx-auto" />
            <h3 className="text-xl font-bold text-blue-950 font-heading">No events added for Day {activeDayIndex + 1} yet</h3>
            <p className="text-xs text-slate-600 max-w-md mx-auto font-medium">
              Click "Add Event" to schedule flights, hotels, attractions, or dining reservations for this day.
            </p>
            <button
              onClick={() => setShowAddModal(true)}
              className="px-6 py-2.5 bg-teal-600 text-white hover:bg-teal-700 rounded-xl text-xs font-bold transition shadow-sm"
            >
              + Add First Activity
            </button>
          </div>
        ) : (
          <div className="space-y-4">
            {/* Interactive Day Completion Progress Bar */}
            {(() => {
              const currentItems = currentDay.items;
              const completedCount = currentItems.filter(i => completedItems.includes(i.id)).length;
              const pct = currentItems.length > 0 ? Math.round((completedCount / currentItems.length) * 100) : 0;
              return (
                <div className="bg-white p-3.5 px-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col sm:flex-row sm:items-center justify-between gap-3">
                  <div className="flex items-center space-x-2">
                    <CheckCircle2 className={`w-4 h-4 ${pct === 100 ? 'text-emerald-600' : 'text-teal-600'}`} />
                    <span className="text-xs font-bold text-slate-800">
                      Day {activeDayIndex + 1} Checklist: <span className="text-teal-700 font-extrabold">{completedCount} of {currentItems.length} completed</span> ({pct}%)
                    </span>
                  </div>
                  <div className="w-full sm:w-48 bg-slate-100 rounded-full h-2 overflow-hidden border border-slate-200">
                    <div 
                      style={{ width: `${pct}%` }} 
                      className={`h-full rounded-full transition-all duration-500 ${pct === 100 ? 'bg-emerald-500' : 'bg-teal-600'}`} 
                    />
                  </div>
                </div>
              );
            })()}

            {currentDay.items.map((item, idx) => {
              const isDone = completedItems.includes(item.id);
              return (
                <div
                  key={item.id}
                  className={`glass-panel p-5 rounded-3xl border transition flex items-center justify-between group shadow-sm ${
                    isDone ? 'bg-slate-50/80 border-emerald-300 opacity-85' : 'border-slate-200 hover:border-teal-500/50'
                  }`}
                >
                  <div className="flex items-center space-x-3.5">
                    {/* Interactive Completion Toggle */}
                    <button
                      type="button"
                      onClick={() => toggleComplete(item.id)}
                      title={isDone ? 'Mark as pending' : 'Mark as completed'}
                      className="p-1 text-slate-400 hover:text-emerald-600 transition"
                    >
                      {isDone ? (
                        <CheckCircle2 className="w-5 h-5 fill-emerald-100 text-emerald-600" />
                      ) : (
                        <Circle className="w-5 h-5 text-slate-300 hover:text-slate-500" />
                      )}
                    </button>

                    <div className="p-2.5 bg-slate-100 rounded-2xl border border-slate-200 flex-shrink-0">
                      {getItemIcon(item.itemType)}
                    </div>

                    <div>
                      <div className="flex items-center space-x-2">
                        <span className="text-xs font-black text-cyan-800 uppercase tracking-wider">
                          {item.startTime} - {item.endTime}
                        </span>
                        <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-slate-100 border border-slate-200 text-slate-800">
                          {item.itemType}
                        </span>
                        {isDone && (
                          <span className="text-[9px] font-bold px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-800 border border-emerald-200">
                            Completed ✓
                          </span>
                        )}
                      </div>

                      <h4 className={`text-base font-bold text-slate-900 font-heading mt-0.5 ${isDone ? 'line-through text-slate-500' : ''}`}>
                        {item.title}
                      </h4>

                      {item.locationName && (
                        <div className="flex items-center space-x-1 text-xs text-slate-600 mt-1 font-medium">
                          <MapPin className="w-3.5 h-3.5 text-rose-500" />
                          <span>{item.locationName}</span>
                        </div>
                      )}
                    </div>
                  </div>

                  <div className="flex items-center space-x-4">
                    {item.cost > 0 && (
                      <span className="text-sm font-black text-emerald-800">${item.cost}</span>
                    )}
                    <button
                      onClick={() => handleDeleteItem(item.id)}
                      className="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-xl transition"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}

      </div>

      {/* Add Event Modal */}
      {showAddModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm animate-in fade-in duration-150">
          <div className="bg-white w-full max-w-lg rounded-3xl p-6 border border-slate-200 shadow-2xl relative">
            <button
              onClick={() => setShowAddModal(false)}
              className="absolute top-5 right-5 p-2 text-slate-400 hover:text-slate-800 rounded-full hover:bg-slate-100"
            >
              <X className="w-5 h-5" />
            </button>

            <h3 className="text-xl font-bold text-blue-950 font-heading mb-3">
              Add Event to Day {activeDayIndex + 1}
            </h3>

            {/* Quick Presets Row */}
            <div className="mb-5 p-3 bg-slate-50 rounded-2xl border border-slate-200/80">
              <span className="block text-[10px] font-extrabold uppercase tracking-wider text-slate-700 mb-2">
                ⚡ Quick Presets
              </span>
              <div className="flex flex-wrap gap-1.5">
                <button
                  type="button"
                  onClick={() => applyPreset('Morning Breakfast & Coffee', 'RESTAURANT', '08:30', '09:30', 20)}
                  className="px-2.5 py-1 rounded-xl bg-white border border-slate-200 hover:bg-teal-50 hover:text-teal-800 text-[11px] font-bold text-slate-700 transition shadow-sm"
                >
                  ☕ Breakfast ($20)
                </button>
                <button
                  type="button"
                  onClick={() => applyPreset('Iconic Museum & Landmark Tour', 'ACTIVITY', '10:00', '12:30', 35)}
                  className="px-2.5 py-1 rounded-xl bg-white border border-slate-200 hover:bg-teal-50 hover:text-teal-800 text-[11px] font-bold text-slate-700 transition shadow-sm"
                >
                  🏛 Museum Tour ($35)
                </button>
                <button
                  type="button"
                  onClick={() => applyPreset('Local Food Market Lunch', 'RESTAURANT', '13:00', '14:30', 30)}
                  className="px-2.5 py-1 rounded-xl bg-white border border-slate-200 hover:bg-teal-50 hover:text-teal-800 text-[11px] font-bold text-slate-700 transition shadow-sm"
                >
                  🍽 Lunch ($30)
                </button>
                <button
                  type="button"
                  onClick={() => applyPreset('Scenic Sunset Viewpoint Walk', 'ACTIVITY', '18:00', '19:30', 10)}
                  className="px-2.5 py-1 rounded-xl bg-white border border-slate-200 hover:bg-teal-50 hover:text-teal-800 text-[11px] font-bold text-slate-700 transition shadow-sm"
                >
                  🌅 Sunset Walk ($10)
                </button>
                <button
                  type="button"
                  onClick={() => applyPreset('Evening Chef Dinner & Wine', 'RESTAURANT', '20:00', '22:00', 65)}
                  className="px-2.5 py-1 rounded-xl bg-white border border-slate-200 hover:bg-teal-50 hover:text-teal-800 text-[11px] font-bold text-slate-700 transition shadow-sm"
                >
                  🍷 Dinner ($65)
                </button>
              </div>
            </div>

            {modalError && (
              <div className="p-3 mb-4 bg-rose-50 border border-rose-200 rounded-xl text-xs text-rose-800 font-semibold flex items-center space-x-2">
                <AlertTriangle className="w-4 h-4 shrink-0 text-rose-600" />
                <span>{modalError}</span>
              </div>
            )}

            <form onSubmit={handleAddItem} className="space-y-4">
              <div>
                <label className="block text-xs font-extrabold text-slate-800 mb-1">Event Title</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Louvre Museum Guided Tour"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  className="w-full glass-input text-xs text-slate-900"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-1">Category Type</label>
                  <select
                    value={itemType}
                    onChange={(e) => setItemType(e.target.value)}
                    className="w-full glass-input text-xs cursor-pointer text-slate-900"
                  >
                    <option value="ACTIVITY">Sightseeing / Activity</option>
                    <option value="FLIGHT">Flight Slot</option>
                    <option value="ACCOMMODATION">Hotel / Stay</option>
                    <option value="RESTAURANT">Dining / Restaurant</option>
                    <option value="TRANSIT">Transit / Commute</option>
                    <option value="CUSTOM">Custom Event</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-1">Estimated Cost ($)</label>
                  <input
                    type="number"
                    min="0"
                    value={cost}
                    onChange={(e) => setCost(e.target.value)}
                    className="w-full glass-input text-xs text-slate-900"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-1">Start Time</label>
                  <input
                    type="time"
                    required
                    value={startTime}
                    onChange={(e) => setStartTime(e.target.value)}
                    className="w-full glass-input text-xs text-slate-900"
                  />
                </div>
                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-1">End Time</label>
                  <input
                    type="time"
                    required
                    value={endTime}
                    onChange={(e) => setEndTime(e.target.value)}
                    className="w-full glass-input text-xs text-slate-900"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-extrabold text-slate-800 mb-1">Location Name</label>
                <input
                  type="text"
                  placeholder="e.g. Rue de Rivoli, 75001 Paris"
                  value={locationName}
                  onChange={(e) => setLocationName(e.target.value)}
                  className="w-full glass-input text-xs text-slate-900"
                />
              </div>

              <button
                type="submit"
                disabled={isSubmitting}
                className="w-full mt-4 py-3 bg-gradient-to-r from-cyan-500 via-sky-500 to-indigo-600 hover:opacity-95 text-slate-950 font-black rounded-xl text-xs shadow-xl shadow-cyan-500/20 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center space-x-2"
              >
                {isSubmitting ? (
                  <span>Saving Event to Itinerary...</span>
                ) : (
                  <span>Add Event to Itinerary</span>
                )}
              </button>
            </form>
          </div>
        </div>
      )}

    </div>
  );
}
