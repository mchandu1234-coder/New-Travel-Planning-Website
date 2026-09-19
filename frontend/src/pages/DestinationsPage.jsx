import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { destinationApi } from '../api/client';
import { Search, MapPin, Star, Compass } from 'lucide-react';

const CONTINENTS = ['All', 'Europe', 'Asia', 'North America', 'South America', 'Africa', 'Oceania'];
const VIBES = ['All', 'Cultural', 'Tropical', 'Urban', 'Mountain', 'Historical', 'Romance', 'Adventure', 'Modern'];

export default function DestinationsPage() {
  const [searchParams] = useSearchParams();
  const [destinations, setDestinations] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const querySearch = searchParams.get('search') || '';
  const queryContinent = searchParams.get('continent') || 'All';
  const queryVibe = searchParams.get('vibe') || 'All';

  const [search, setSearch] = useState(querySearch);
  const [selectedContinent, setSelectedContinent] = useState(queryContinent);
  const [selectedVibe, setSelectedVibe] = useState(queryVibe);
  const [maxBudget, setMaxBudget] = useState(500);

  useEffect(() => {
    fetchDestinations();
  }, [selectedContinent, selectedVibe]);

  const fetchDestinations = () => {
    setLoading(true);
    const params = { size: 100 };
    if (selectedContinent !== 'All') params.continent = selectedContinent;

    destinationApi.getAll(params)
      .then((res) => {
        if (res.data.success) {
          const items = Array.isArray(res.data.data) ? res.data.data : (res.data.data?.content || []);
          setDestinations(items);
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  const filteredDestinations = destinations.filter((dest) => {
    const cost = dest.averageDailyCost || dest.avgCostPerDay || 100;
    const matchesSearch = !search || 
      dest.name.toLowerCase().includes(search.toLowerCase()) ||
      dest.country.toLowerCase().includes(search.toLowerCase()) ||
      (dest.city && dest.city.toLowerCase().includes(search.toLowerCase()));
    
    const matchesBudget = cost <= maxBudget;

    let matchesVibe = true;
    if (selectedVibe !== 'All') {
      const vibeStr = Array.isArray(dest.vibeTags) ? dest.vibeTags.join(' ') : (dest.vibeTags || dest.vibe || '');
      matchesVibe = vibeStr.toLowerCase().includes(selectedVibe.toLowerCase());
    }

    return matchesSearch && matchesBudget && matchesVibe;
  });

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 py-10 bg-mesh">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header */}
        <div className="text-center max-w-2xl mx-auto mb-10 space-y-3">
          <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-cyan-500/10 border border-cyan-500/20 text-cyan-400 text-xs font-bold">
            <Compass className="w-3.5 h-3.5" />
            <span>Global Destinations Catalog</span>
          </div>
          <h1 className="text-3xl sm:text-5xl font-black text-slate-100 font-heading">
            Discover Your Next Escapade
          </h1>
          <p className="text-sm text-slate-400">
            Filter through world-famous cities, tropical islands, and cultural gems.
          </p>
        </div>

        {/* Filter Controls Bar */}
        <div className="glass-panel p-6 rounded-3xl border border-slate-800/80 mb-10 space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            
            {/* Search Input */}
            <div className="relative md:col-span-2">
              <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                placeholder="Search city, country, or keyword..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="w-full glass-input pl-10 text-xs"
              />
            </div>

            {/* Price Slider */}
            <div className="bg-slate-950/60 p-3 rounded-2xl border border-slate-800/80">
              <div className="flex justify-between items-center text-xs font-bold mb-1">
                <span className="text-slate-400">Max Daily Budget</span>
                <span className="text-cyan-400 font-black">${maxBudget} / day</span>
              </div>
              <input
                type="range"
                min="50"
                max="500"
                step="25"
                value={maxBudget}
                onChange={(e) => setMaxBudget(Number(e.target.value))}
                className="w-full accent-cyan-400 cursor-pointer"
              />
            </div>

          </div>

          {/* Continent Pills */}
          <div>
            <label className="block text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-2">Continent</label>
            <div className="flex flex-wrap gap-2">
              {CONTINENTS.map((c) => (
                <button
                  key={c}
                  onClick={() => setSelectedContinent(c)}
                  className={`px-4 py-2 rounded-xl text-xs font-bold transition ${
                    selectedContinent === c
                      ? 'bg-cyan-500 text-slate-950 shadow-md shadow-cyan-500/20'
                      : 'bg-slate-900 text-slate-400 hover:text-slate-100 hover:bg-slate-800'
                  }`}
                >
                  {c}
                </button>
              ))}
            </div>
          </div>

          {/* Vibe Pills */}
          <div>
            <label className="block text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-2">Vibe & Atmosphere</label>
            <div className="flex flex-wrap gap-2">
              {VIBES.map((v) => (
                <button
                  key={v}
                  onClick={() => setSelectedVibe(v)}
                  className={`px-4 py-2 rounded-xl text-xs font-bold transition ${
                    selectedVibe === v
                      ? 'bg-indigo-500 text-slate-100 shadow-md shadow-indigo-500/20'
                      : 'bg-slate-900 text-slate-400 hover:text-slate-100 hover:bg-slate-800'
                  }`}
                >
                  {v}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Results Count */}
        <div className="flex justify-between items-center mb-6">
          <p className="text-xs font-bold text-slate-400">
            Showing <span className="text-cyan-400">{filteredDestinations.length}</span> destinations
          </p>
        </div>

        {/* Grid */}
        {loading ? (
          <div className="flex justify-center items-center py-20">
            <div className="w-10 h-10 border-4 border-cyan-400 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : filteredDestinations.length === 0 ? (
          <div className="text-center py-16 glass-panel rounded-3xl">
            <Compass className="w-12 h-12 text-slate-600 mx-auto mb-3" />
            <h3 className="text-lg font-bold text-slate-200">No destinations found</h3>
            <p className="text-xs text-slate-400 mt-1">Try relaxing your search terms or filters.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {filteredDestinations.map((dest) => {
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
                    
                    {/* Rating */}
                    <div className="absolute top-4 right-4 bg-slate-950/80 backdrop-blur-md px-3 py-1 rounded-full border border-slate-800 flex items-center space-x-1 text-amber-400 text-xs font-bold">
                      <Star className="w-3.5 h-3.5 fill-amber-400" />
                      <span>{dest.rating || 4.8}</span>
                    </div>

                    {/* Vibe */}
                    <div className="absolute top-4 left-4 bg-cyan-500/20 backdrop-blur-md border border-cyan-500/30 text-cyan-300 px-3 py-1 rounded-full text-xs font-extrabold uppercase tracking-wider">
                      {vibe}
                    </div>

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
                        <span className="text-slate-500 text-[10px] uppercase block font-bold">Best Season</span>
                        <span className="text-slate-200 font-bold">{dest.bestTimeToVisit || dest.bestSeason || 'Spring & Fall'}</span>
                      </div>
                      <div className="text-right">
                        <span className="text-slate-500 text-[10px] uppercase block font-bold">Est. Daily</span>
                        <span className="text-emerald-400 font-black text-sm">${cost}</span>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}

      </div>
    </div>
  );
}
