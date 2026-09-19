import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { destinationApi, tripApi } from '../api/client';
import { useAuth } from '../context/AuthContext';
import { 
  Sparkles, Calendar, Users, DollarSign, MapPin, 
  ArrowRight, ArrowLeft, Check, Compass, AlertCircle 
} from 'lucide-react';
import confetti from 'canvas-confetti';

export default function TripWizardPage() {
  const [searchParams] = useSearchParams();
  const preselectedDestId = searchParams.get('destinationId');
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  const [step, setStep] = useState(1);
  const [destinations, setDestinations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Form State
  const [title, setTitle] = useState('');
  const [destinationId, setDestinationId] = useState(preselectedDestId || '');
  const [destSearch, setDestSearch] = useState('');
  const [destContinent, setDestContinent] = useState('All');
  const [startDate, setStartDate] = useState('2026-10-15');
  const [endDate, setEndDate] = useState('2026-10-22');
  const [travelersCount, setTravelersCount] = useState(2);
  const [totalBudget, setTotalBudget] = useState(2500);
  const [currency, setCurrency] = useState('USD');

  const CONTINENT_OPTIONS = ['All', 'Europe', 'Asia', 'North America', 'South America', 'Africa', 'Oceania'];

  useEffect(() => {
    destinationApi.getAll({ size: 100 })
      .then((res) => {
        if (res.data.success) {
          const list = Array.isArray(res.data.data) ? res.data.data : (res.data.data?.content || []);
          setDestinations(list);
          if (!destinationId && list.length > 0) {
            setDestinationId(list[0].id);
          }
        }
      })
      .catch(console.error);
  }, []);

  const selectedDestObj = destinations.find((d) => String(d.id) === String(destinationId));

  const filteredDestinations = destinations.filter((dest) => {
    const term = destSearch.trim().toLowerCase();
    const matchesSearch = !term ||
      dest.name?.toLowerCase().includes(term) ||
      dest.country?.toLowerCase().includes(term) ||
      (dest.city && dest.city.toLowerCase().includes(term));
    const matchesContinent = destContinent === 'All' || dest.continent?.toLowerCase() === destContinent.toLowerCase();
    return matchesSearch && matchesContinent;
  });

  const handleNext = () => {
    setError('');
    if (step === 1 && !title.trim()) {
      setError('Please provide a name for your trip');
      return;
    }
    if (step === 1 && !destinationId) {
      setError('Please select a travel destination');
      return;
    }
    if (step === 2) {
      if (new Date(startDate) >= new Date(endDate)) {
        setError('End date must be after start date');
        return;
      }
    }
    setStep(step + 1);
  };

  const handlePrev = () => {
    setError('');
    setStep(step - 1);
  };

  const handleSubmit = async () => {
    if (!isAuthenticated) {
      setError('Please log in or click "One-Click Demo Access" in the top bar to create a trip.');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const res = await tripApi.create({
        title,
        destinationId: Number(destinationId),
        startDate,
        endDate,
        travelerCount: Number(travelersCount),
        targetBudget: Number(totalBudget),
        currency,
      });

      if (res.data.success) {
        confetti({
          particleCount: 100,
          spread: 70,
          origin: { y: 0.6 },
        });
        const createdTrip = res.data.data;
        navigate(`/trips/${createdTrip.id}`);
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to create trip');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#f7faf9] bg-mesh text-slate-900 py-12">
      <div className="max-w-3xl mx-auto px-4 sm:px-6">
        
        {/* Header */}
        <div className="text-center mb-8 space-y-2">
          <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-teal-50 border border-teal-200 text-teal-800 text-xs font-bold">
            <Sparkles className="w-3.5 h-3.5" />
            <span>Multi-Step Trip Creator</span>
          </div>
          <h1 className="text-3xl font-black text-blue-950 font-heading">
            Design Your Custom Itinerary
          </h1>
        </div>

        {/* Progress Bar */}
        <div className="mb-8 flex items-center justify-between relative px-2">
          <div className="absolute top-1/2 left-0 right-0 h-1 bg-slate-200 -translate-y-1/2 -z-0" />
          {[1, 2, 3, 4].map((s) => (
            <div
              key={s}
              className={`w-10 h-10 rounded-2xl flex items-center justify-center font-black text-sm z-10 transition-all ${
                s <= step
                  ? 'bg-gradient-to-tr from-cyan-500 to-indigo-500 text-slate-950 shadow-lg shadow-cyan-500/30'
                  : 'bg-white border border-slate-300 text-slate-600'
              }`}
            >
              {s < step ? <Check className="w-5 h-5 text-slate-950" /> : s}
            </div>
          ))}
        </div>

        {/* Error Notification */}
        {error && (
          <div className="mb-6 p-4 bg-rose-50 border border-rose-200 rounded-2xl text-rose-800 text-xs font-bold flex items-center space-x-2">
            <AlertCircle className="w-4 h-4 flex-shrink-0" />
            <span>{error}</span>
          </div>
        )}

        {/* Card Content */}
        <div className="glass-panel p-8 rounded-3xl border border-slate-200/80 shadow-sm relative">
          
          {/* Step 1: Destination & Title */}
          {step === 1 && (
            <div className="space-y-6 animate-in fade-in duration-300">
              <h3 className="text-xl font-bold text-blue-950 font-heading">1. Select Destination & Trip Title</h3>

              <div>
                <label className="block text-xs font-extrabold text-slate-800 mb-2">Trip Title</label>
                <input
                  type="text"
                  placeholder="e.g. Summer Escape to Paris & Riviera"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  className="w-full glass-input text-sm text-slate-900"
                />
              </div>

              <div className="space-y-3">
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
                  <label className="block text-xs font-extrabold text-slate-800">
                    Select Destination <span className="text-teal-700 font-bold">({destinations.length} global locations)</span>
                  </label>
                  
                  {/* Inline Destination Search */}
                  <div className="relative w-full sm:w-64">
                    <input
                      type="text"
                      placeholder="Search country or city..."
                      value={destSearch}
                      onChange={(e) => setDestSearch(e.target.value)}
                      className="w-full bg-white border border-slate-300 rounded-xl px-3 py-1.5 text-xs text-slate-900 placeholder-slate-400 outline-none focus:border-teal-600 focus:ring-1 focus:ring-teal-600"
                    />
                    {destSearch && (
                      <button
                        onClick={() => setDestSearch('')}
                        className="absolute right-2.5 top-1/2 -translate-y-1/2 text-[10px] text-slate-700 hover:text-slate-950 font-bold"
                      >
                        ✕
                      </button>
                    )}
                  </div>
                </div>

                {/* Continent Quick-Filter Pills */}
                <div className="flex flex-wrap gap-1.5 pb-1">
                  {CONTINENT_OPTIONS.map((c) => (
                    <button
                      key={c}
                      type="button"
                      onClick={() => setDestContinent(c)}
                      className={`px-2.5 py-1 rounded-lg text-[11px] font-bold transition ${
                        destContinent === c
                          ? 'bg-slate-900 text-white shadow-sm'
                          : 'bg-slate-100 border border-slate-200 text-slate-700 hover:text-slate-950 hover:bg-slate-200'
                      }`}
                    >
                      {c}
                    </button>
                  ))}
                </div>

                {/* Destinations Scrollable Grid */}
                {filteredDestinations.length === 0 ? (
                  <div className="p-8 text-center rounded-2xl bg-slate-50 border border-slate-200 text-slate-700 text-xs font-medium">
                    No destinations match "<span className="text-teal-700 font-bold">{destSearch}</span>" in {destContinent}.
                    <button
                      type="button"
                      onClick={() => { setDestSearch(''); setDestContinent('All'); }}
                      className="block mx-auto mt-2 text-teal-700 font-bold hover:underline"
                    >
                      Reset filter
                    </button>
                  </div>
                ) : (
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 max-h-72 overflow-y-auto pr-1.5 custom-scrollbar">
                    {filteredDestinations.map((dest) => {
                      const img = dest.heroImageUrl || dest.imageUrl || 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=300&q=80';
                      const isSelected = Number(destinationId) === Number(dest.id);
                      return (
                        <div
                          key={dest.id}
                          onClick={() => setDestinationId(dest.id)}
                          className={`p-3 rounded-2xl border flex items-center justify-between space-x-3 cursor-pointer transition relative ${
                            isSelected
                              ? 'bg-teal-50 border-teal-600 text-teal-950 ring-1 ring-teal-600 shadow-sm'
                              : 'bg-white border-slate-200 text-slate-800 hover:border-teal-500 hover:bg-teal-50/30 shadow-sm'
                          }`}
                        >
                          <div className="flex items-center space-x-3 min-w-0">
                            <img src={img} alt={dest.name} className="w-12 h-12 rounded-xl object-cover flex-shrink-0" />
                            <div className="min-w-0">
                              <div className="font-bold text-xs truncate text-slate-900">{dest.name}</div>
                              <div className="text-[10px] text-slate-800 font-medium truncate">{dest.country} • <span className="text-slate-700 font-bold">{dest.continent}</span></div>
                            </div>
                          </div>
                          {isSelected && (
                            <div className="w-6 h-6 rounded-full bg-teal-600 text-white flex items-center justify-center flex-shrink-0 shadow-sm">
                              <Check className="w-3.5 h-3.5 stroke-[3]" />
                            </div>
                          )}
                        </div>
                      );
                    })}
                  </div>
                )}

                {/* Selected Destination Active Banner */}
                {selectedDestObj && (
                  <div className="mt-3 p-3 bg-teal-50 border border-teal-200 rounded-2xl flex items-center justify-between text-xs">
                    <div className="flex items-center space-x-2.5">
                      <Compass className="w-4 h-4 text-teal-700 flex-shrink-0" />
                      <span className="text-slate-800">
                        Selected: <strong className="text-teal-900">{selectedDestObj.name}</strong> ({selectedDestObj.country})
                      </span>
                    </div>
                    <span className="text-teal-800 font-bold">~${selectedDestObj.averageDailyCost || 150}/day</span>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* Step 2: Dates & Travelers */}
          {step === 2 && (
            <div className="space-y-6 animate-in fade-in duration-300">
              <h3 className="text-xl font-bold text-blue-950 font-heading">2. Trip Dates & Group Size</h3>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-2">Start Date</label>
                  <input
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    className="w-full glass-input text-xs text-slate-900"
                  />
                </div>
                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-2">End Date</label>
                  <input
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                    className="w-full glass-input text-xs text-slate-900"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-extrabold text-slate-800 mb-2">Number of Travelers</label>
                <input
                  type="number"
                  min="1"
                  max="20"
                  value={travelersCount}
                  onChange={(e) => setTravelersCount(e.target.value)}
                  className="w-full glass-input text-xs text-slate-900"
                />
              </div>
            </div>
          )}

          {/* Step 3: Budget & Currency */}
          {step === 3 && (
            <div className="space-y-6 animate-in fade-in duration-300">
              <h3 className="text-xl font-bold text-blue-950 font-heading">3. Set Budget Allocation</h3>

              <div>
                <label className="block text-xs font-extrabold text-slate-800 mb-2">Total Budget Target</label>
                <input
                  type="number"
                  step="100"
                  value={totalBudget}
                  onChange={(e) => setTotalBudget(e.target.value)}
                  className="w-full glass-input text-xs text-slate-900"
                />
              </div>

              <div>
                <label className="block text-xs font-extrabold text-slate-800 mb-2">Currency</label>
                <select
                  value={currency}
                  onChange={(e) => setCurrency(e.target.value)}
                  className="w-full glass-input text-xs cursor-pointer text-slate-900"
                >
                  <option value="USD">USD ($)</option>
                  <option value="EUR">EUR (€)</option>
                  <option value="GBP">GBP (£)</option>
                  <option value="JPY">JPY (¥)</option>
                  <option value="INR">INR (₹)</option>
                </select>
              </div>
            </div>
          )}

          {/* Step 4: Final Confirmation */}
          {step === 4 && (
            <div className="space-y-6 animate-in fade-in duration-300">
              <h3 className="text-xl font-bold text-blue-950 font-heading">4. Review & Build Smart Itinerary</h3>

              <div className="bg-white p-5 rounded-2xl border border-slate-200 space-y-3 text-xs shadow-sm">
                <div className="flex justify-between py-1 border-b border-slate-100">
                  <span className="text-slate-600 font-semibold">Title:</span>
                  <span className="font-bold text-slate-900">{title}</span>
                </div>
                <div className="flex justify-between py-1 border-b border-slate-100">
                  <span className="text-slate-600 font-semibold">Destination:</span>
                  <span className="font-bold text-teal-800">{selectedDestObj?.name}, {selectedDestObj?.country}</span>
                </div>
                <div className="flex justify-between py-1 border-b border-slate-100">
                  <span className="text-slate-600 font-semibold">Dates:</span>
                  <span className="font-bold text-slate-900">{startDate} to {endDate}</span>
                </div>
                <div className="flex justify-between py-1 border-b border-slate-100">
                  <span className="text-slate-600 font-semibold">Group Size:</span>
                  <span className="font-bold text-slate-900">{travelersCount} Travelers</span>
                </div>
                <div className="flex justify-between py-1">
                  <span className="text-slate-600 font-semibold">Budget Target:</span>
                  <span className="font-bold text-emerald-800">{currency} {totalBudget}</span>
                </div>
              </div>
            </div>
          )}

          {/* Navigation Controls */}
          <div className="flex justify-between items-center mt-8 pt-6 border-t border-slate-200">
            {step > 1 ? (
              <button
                onClick={handlePrev}
                className="px-5 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-800 font-bold rounded-xl text-xs flex items-center space-x-2 transition"
              >
                <ArrowLeft className="w-4 h-4" />
                <span>Back</span>
              </button>
            ) : <div />}

            {step < 4 ? (
              <button
                onClick={handleNext}
                className="px-6 py-2.5 bg-gradient-to-r from-cyan-500 to-sky-500 hover:opacity-95 text-slate-950 font-black rounded-xl text-xs flex items-center space-x-2 shadow-lg shadow-cyan-500/20 transition"
              >
                <span>Continue</span>
                <ArrowRight className="w-4 h-4" />
              </button>
            ) : (
              <button
                onClick={handleSubmit}
                disabled={loading}
                className="px-8 py-3 bg-gradient-to-r from-cyan-500 via-sky-500 to-indigo-600 hover:opacity-95 text-slate-950 font-black rounded-xl text-sm shadow-xl shadow-cyan-500/30 transition flex items-center space-x-2"
              >
                {loading ? (
                  <div className="w-5 h-5 border-2 border-slate-950 border-t-transparent rounded-full animate-spin" />
                ) : (
                  <>
                    <Sparkles className="w-4 h-4" />
                    <span>Generate Complete Itinerary</span>
                  </>
                )}
              </button>
            )}
          </div>

        </div>

      </div>
    </div>
  );
}
