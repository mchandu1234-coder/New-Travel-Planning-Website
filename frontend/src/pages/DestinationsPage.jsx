import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { destinationApi } from '../api/client';
import { 
  Search, MapPin, Star, Compass, Heart, Eye, 
  ArrowUpDown, LayoutGrid, List, X, Sparkles, ArrowRight, Check 
} from 'lucide-react';
import { RetentionBarIcon } from '../components/BrandLogo';

const CONTINENTS = ['All', 'Saved', 'Europe', 'Asia', 'North America', 'South America', 'Africa', 'Oceania'];
const VIBES = ['All', 'Cultural', 'Tropical', 'Urban', 'Mountain', 'Historical', 'Romance', 'Adventure', 'Modern', 'Beach', 'Nature'];

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
  const [sortBy, setSortBy] = useState('recommended');
  const [viewMode, setViewMode] = useState('grid');
  const [quickPeekDest, setQuickPeekDest] = useState(null);

  // Favorites stored in localStorage
  const [favorites, setFavorites] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem('wanderlust_favs') || '[]');
    } catch {
      return [];
    }
  });

  const toggleFavorite = (e, destId) => {
    e.stopPropagation();
    setFavorites((prev) => {
      const exists = prev.includes(destId);
      const next = exists ? prev.filter((id) => id !== destId) : [...prev, destId];
      localStorage.setItem('wanderlust_favs', JSON.stringify(next));
      return next;
    });
  };

  useEffect(() => {
    fetchDestinations();
  }, [selectedContinent, selectedVibe]);

  const fetchDestinations = () => {
    setLoading(true);
    const params = { size: 200 };
    if (selectedContinent !== 'All' && selectedContinent !== 'Saved') {
      params.continent = selectedContinent;
    }

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

    let matchesContinent = true;
    if (selectedContinent === 'Saved') {
      matchesContinent = favorites.includes(dest.id);
    }

    let matchesVibe = true;
    if (selectedVibe !== 'All') {
      const vibeStr = Array.isArray(dest.vibeTags) ? dest.vibeTags.join(' ') : (dest.vibeTags || dest.vibe || '');
      matchesVibe = vibeStr.toLowerCase().includes(selectedVibe.toLowerCase());
    }

    return matchesSearch && matchesBudget && matchesContinent && matchesVibe;
  }).sort((a, b) => {
    const costA = a.averageDailyCost || a.avgCostPerDay || 100;
    const costB = b.averageDailyCost || b.avgCostPerDay || 100;
    const ratingA = a.rating || 4.5;
    const ratingB = b.rating || 4.5;

    if (sortBy === 'priceAsc') return costA - costB;
    if (sortBy === 'priceDesc') return costB - costA;
    if (sortBy === 'rating') return ratingB - ratingA;
    if (sortBy === 'name') return a.name.localeCompare(b.name);
    return 0;
  });

  return (
    <div className="min-h-screen bg-mesh text-slate-900 py-10 pb-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header */}
        <div className="text-center max-w-2xl mx-auto mb-10 space-y-3">
          <div className="inline-flex items-center space-x-2 px-3.5 py-1.5 rounded-full bg-gradient-to-r from-blue-50 to-indigo-50 border border-blue-200 text-blue-900 text-xs font-black shadow-sm">
            <Compass className="w-3.5 h-3.5 text-cyan-600 animate-spin" style={{ animationDuration: '8s' }} />
            <span>100+ WORLD FAMOUS DESTINATIONS</span>
          </div>
          <h1 className="text-3xl sm:text-5xl font-black text-blue-950 font-heading tracking-tight">
            Discover Your Next Escapade
          </h1>
          <p className="text-sm text-slate-700 font-medium">
            Explore 100+ iconic cities, tropical sanctuaries, alpine wonders, and cultural capitals across all continents.
          </p>
        </div>

        {/* Filter Controls Bar */}
        <div className="glass-panel p-6 rounded-3xl border border-slate-200/90 shadow-sm mb-10 space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            
            {/* Search Input */}
            <div className="relative md:col-span-2">
              <Search className="w-4 h-4 text-slate-600 absolute left-3.5 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                placeholder="Search city, country, monument, or keyword..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="w-full glass-input pl-10 text-xs sm:text-sm text-slate-900 placeholder:text-slate-500 font-bold"
              />
            </div>

            {/* Price Slider */}
            <div className="bg-slate-50/90 p-3.5 rounded-2xl border border-slate-200/90 shadow-inner">
              <div className="flex justify-between items-center text-xs font-bold mb-1.5">
                <span className="text-slate-700 font-bold">Max Daily Budget</span>
                <span className="text-teal-700 font-black px-2 py-0.5 bg-teal-50 rounded-lg border border-teal-200">${maxBudget} / day</span>
              </div>
              <input
                type="range"
                min="50"
                max="500"
                step="25"
                value={maxBudget}
                onChange={(e) => setMaxBudget(Number(e.target.value))}
                className="w-full accent-teal-600 cursor-pointer"
              />
            </div>

          </div>

          {/* Continent Pills */}
          <div>
            <label className="block text-[11px] font-black text-slate-700 uppercase tracking-wider mb-2.5">
              Filter by Continent
            </label>
            <div className="flex flex-wrap gap-2">
              {CONTINENTS.map((c) => (
                <button
                  key={c}
                  onClick={() => setSelectedContinent(c)}
                  className={`btn-interactive px-4 py-2 rounded-full text-xs font-black transition-all ${
                    selectedContinent === c
                      ? 'btn-action-cyan shadow-md shadow-cyan-500/25'
                      : 'bg-white text-slate-700 hover:text-cyan-800 hover:bg-cyan-50 hover:border-cyan-200 border border-slate-200'
                  }`}
                >
                  {c}
                </button>
              ))}
            </div>
          </div>

          {/* Vibe Pills */}
          <div>
            <label className="block text-[11px] font-black text-slate-700 uppercase tracking-wider mb-2.5">
              Filter by Travel Vibe & Atmosphere
            </label>
            <div className="flex flex-wrap gap-2">
              {VIBES.map((v) => (
                <button
                  key={v}
                  onClick={() => setSelectedVibe(v)}
                  className={`btn-interactive px-3.5 py-1.5 rounded-full text-xs font-extrabold transition-all ${
                    selectedVibe === v
                      ? 'btn-action-emerald shadow-md shadow-emerald-500/25'
                      : 'bg-white text-slate-700 hover:text-emerald-800 hover:bg-emerald-50 hover:border-emerald-200 border border-slate-200'
                  }`}
                >
                  {v}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Interactive Controls & View Bar */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6 pb-2">
          <div className="flex items-center space-x-2">
            <p className="text-xs font-extrabold text-slate-800">
              Showing <span className="text-teal-700 font-black px-1.5 py-0.5 bg-teal-50 rounded-md border border-teal-200">{filteredDestinations.length}</span> destinations
            </p>
            {favorites.length > 0 && (
              <span className="inline-flex items-center space-x-1 px-2.5 py-1 rounded-full bg-rose-50 text-rose-700 text-[11px] font-extrabold border border-rose-200 shadow-sm">
                <Heart className="w-3.5 h-3.5 fill-rose-500 text-rose-500" />
                <span>{favorites.length} Saved</span>
              </span>
            )}
          </div>

          <div className="flex items-center space-x-3">
            {/* Sort By Dropdown */}
            <div className="flex items-center space-x-2 bg-white px-3.5 py-2 rounded-xl border border-slate-200 shadow-sm text-xs">
              <ArrowUpDown className="w-3.5 h-3.5 text-slate-500" />
              <span className="text-slate-600 font-bold hidden sm:inline">Sort:</span>
              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
                className="bg-transparent font-bold text-slate-800 outline-none cursor-pointer"
              >
                <option value="recommended">Featured / Recommended</option>
                <option value="priceAsc">Budget: Low to High</option>
                <option value="priceDesc">Budget: High to Low</option>
                <option value="rating">Highest Rating</option>
                <option value="name">Alphabetical (A-Z)</option>
              </select>
            </div>

            {/* View Mode Toggle */}
            <div className="flex bg-white p-1 rounded-xl border border-slate-200 shadow-sm">
              <button
                onClick={() => setViewMode('grid')}
                title="Grid Card View"
                className={`p-1.5 rounded-lg transition ${
                  viewMode === 'grid' ? 'bg-slate-900 text-white shadow-sm' : 'text-slate-600 hover:text-slate-900'
                }`}
              >
                <LayoutGrid className="w-4 h-4" />
              </button>
              <button
                onClick={() => setViewMode('table')}
                title="Table Analytics View"
                className={`p-1.5 rounded-lg transition ${
                  viewMode === 'table' ? 'bg-slate-900 text-white shadow-sm' : 'text-slate-600 hover:text-slate-900'
                }`}
              >
                <List className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>

        {/* Grid or Table Results */}
        {loading ? (
          <div className="flex flex-col justify-center items-center py-24 space-y-4">
            <div className="w-12 h-12 border-4 border-cyan-500 border-t-transparent rounded-full animate-spin" />
            <p className="text-xs font-bold text-slate-600">Loading world destinations catalog...</p>
          </div>
        ) : filteredDestinations.length === 0 ? (
          <div className="text-center py-20 bg-white rounded-3xl border border-slate-200 shadow-sm">
            <Compass className="w-14 h-14 text-slate-400 mx-auto mb-3 animate-bounce" />
            <h3 className="text-xl font-black text-slate-900 font-heading">No destinations found</h3>
            <p className="text-xs text-slate-600 font-medium mt-1">Try relaxing your search query or price slider.</p>
            {selectedContinent === 'Saved' && (
              <button
                onClick={() => setSelectedContinent('All')}
                className="mt-4 btn-interactive btn-action-teal px-5 py-2 text-xs font-bold rounded-xl"
              >
                View all destinations
              </button>
            )}
          </div>
        ) : viewMode === 'grid' ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredDestinations.map((dest) => {
              const displayImage = dest.heroImageUrl || dest.imageUrl || 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80';
              const cost = dest.averageDailyCost || dest.avgCostPerDay || 150;
              const vibe = Array.isArray(dest.vibeTags) ? dest.vibeTags[0] : (typeof dest.vibeTags === 'string' ? dest.vibeTags.split(',')[0] : (dest.vibe || 'Cultural'));
              const isFav = favorites.includes(dest.id);

              return (
                <div
                  key={dest.id}
                  onClick={() => navigate(`/destinations/${dest.id}`)}
                  className="group bg-white rounded-3xl overflow-hidden border border-slate-200/90 hover:border-cyan-400 hover:shadow-2xl transition-all duration-300 cursor-pointer relative flex flex-col justify-between"
                >
                  <div className="relative h-56 overflow-hidden">
                    <img
                      src={displayImage}
                      alt={dest.name}
                      className="w-full h-full object-cover group-hover:scale-108 transition-transform duration-700"
                    />
                    <div className="absolute inset-0 bg-gradient-to-t from-slate-950/85 via-slate-950/20 to-transparent" />
                    
                    {/* Top Action Row: Vibe, Rating, Favorite, Quick Peek */}
                    <div className="absolute top-3.5 left-3.5 right-3.5 flex items-center justify-between">
                      <div className="bg-gradient-to-r from-teal-600 to-emerald-600 text-white px-3 py-1 rounded-full text-[11px] font-black uppercase tracking-wider shadow-md">
                        {vibe}
                      </div>

                      <div className="flex items-center space-x-1.5">
                        {/* Quick Peek Button */}
                        <button
                          type="button"
                          onClick={(e) => {
                            e.stopPropagation();
                            setQuickPeekDest(dest);
                          }}
                          title="Quick Peek"
                          className="btn-interactive bg-white/95 hover:bg-white text-slate-800 p-2 rounded-full shadow-md backdrop-blur-md transition hover:scale-110"
                        >
                          <Eye className="w-3.5 h-3.5 text-teal-700" />
                        </button>

                        {/* Favorite Heart Button */}
                        <button
                          type="button"
                          onClick={(e) => toggleFavorite(e, dest.id)}
                          title={isFav ? 'Remove from Saved' : 'Save Destination'}
                          className={`btn-interactive p-2 rounded-full shadow-md backdrop-blur-md transition hover:scale-110 ${
                            isFav ? 'bg-rose-500 text-white' : 'bg-white/95 hover:bg-white text-slate-700'
                          }`}
                        >
                          <Heart className={`w-3.5 h-3.5 ${isFav ? 'fill-white text-white' : 'text-slate-700'}`} />
                        </button>

                        {/* Rating */}
                        <div className="bg-white/95 backdrop-blur-md px-2.5 py-1 rounded-full border border-white/50 flex items-center space-x-1 text-slate-800 text-xs font-black shadow-sm">
                          <Star className="w-3.5 h-3.5 fill-amber-400 text-amber-400" />
                          <span>{dest.rating || 4.8}</span>
                        </div>
                      </div>
                    </div>

                    <div className="absolute bottom-3.5 left-4 right-4">
                      <h3 className="text-xl font-black text-white font-heading tracking-tight">{dest.name}</h3>
                      <div className="flex items-center space-x-1 text-slate-200 text-xs font-bold mt-0.5">
                        <MapPin className="w-3.5 h-3.5 text-teal-300" />
                        <span>{dest.city ? `${dest.city}, ` : ''}{dest.country}</span>
                      </div>
                    </div>
                  </div>

                  <div className="p-5 space-y-4 flex-1 flex flex-col justify-between">
                    <p className="text-xs text-slate-700 line-clamp-2 leading-relaxed font-medium">
                      {dest.description}
                    </p>

                    <div className="space-y-3 pt-2 border-t border-slate-100">
                      <div className="flex items-center justify-between text-xs font-bold text-slate-800">
                        <div>
                          <span className="text-slate-500 text-[10px] uppercase block font-extrabold">Best Season</span>
                          <span className="text-slate-900 font-extrabold">{dest.bestTimeToVisit || dest.bestSeason || 'Spring & Fall'}</span>
                        </div>
                        <div className="text-right">
                          <span className="text-slate-500 text-[10px] uppercase block font-extrabold">Est. Daily</span>
                          <span className="text-teal-700 font-black text-sm">${cost}</span>
                        </div>
                      </div>

                      {/* Interactive Action Button */}
                      <button
                        type="button"
                        onClick={(e) => {
                          e.stopPropagation();
                          navigate(`/destinations/${dest.id}`);
                        }}
                        className="btn-interactive w-full py-2.5 px-4 rounded-xl text-xs font-black bg-cyan-50 text-cyan-800 border border-cyan-200/90 group-hover:bg-gradient-to-r group-hover:from-cyan-500 group-hover:to-sky-500 group-hover:text-white group-hover:border-transparent group-hover:shadow-md transition-all duration-200 flex items-center justify-center gap-1.5"
                      >
                        <span>Explore & Plan</span>
                        <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-1 transition-transform" />
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        ) : (
          /* Table / List View */
          <div className="bg-white rounded-3xl border border-slate-200/90 shadow-sm overflow-hidden">
            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-200 text-[10px] font-extrabold uppercase tracking-wider text-slate-700">
                    <th className="py-3 px-4">Destination</th>
                    <th className="py-3 px-4">Continent</th>
                    <th className="py-3 px-4">Rating</th>
                    <th className="py-3 px-4">Best Season</th>
                    <th className="py-3 px-4">Est. Daily Cost</th>
                    <th className="py-3 px-4 text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 font-medium text-slate-800">
                  {filteredDestinations.map((dest) => {
                    const cost = dest.averageDailyCost || dest.avgCostPerDay || 150;
                    const isFav = favorites.includes(dest.id);
                    return (
                      <tr 
                        key={dest.id}
                        onClick={() => navigate(`/destinations/${dest.id}`)}
                        className="hover:bg-cyan-50/50 cursor-pointer transition"
                      >
                        <td className="py-3.5 px-4">
                          <div className="flex items-center space-x-3">
                            <img
                              src={dest.heroImageUrl || dest.imageUrl || 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=120&q=80'}
                              alt={dest.name}
                              className="w-11 h-11 rounded-xl object-cover"
                            />
                            <div>
                              <div className="font-extrabold text-slate-900">{dest.name}</div>
                              <div className="text-[11px] text-slate-600 font-semibold">{dest.country}</div>
                            </div>
                          </div>
                        </td>
                        <td className="py-3.5 px-4 text-slate-700 font-bold">{dest.continent}</td>
                        <td className="py-3.5 px-4">
                          <div className="flex items-center space-x-1 font-extrabold text-slate-900">
                            <Star className="w-3.5 h-3.5 fill-amber-400 text-amber-400" />
                            <span>{dest.rating || 4.8}</span>
                          </div>
                        </td>
                        <td className="py-3.5 px-4 text-slate-700 font-semibold">{dest.bestTimeToVisit || 'Spring & Fall'}</td>
                        <td className="py-3.5 px-4">
                          <span className="font-black text-teal-700 text-sm">${cost}</span> / day
                        </td>
                        <td className="py-3.5 px-4 text-right">
                          <div className="flex items-center justify-end space-x-2">
                            <button
                              type="button"
                              onClick={(e) => {
                                e.stopPropagation();
                                setQuickPeekDest(dest);
                              }}
                              className="btn-interactive p-2 rounded-lg hover:bg-slate-100 text-slate-600 transition"
                              title="Quick Peek"
                            >
                              <Eye className="w-4 h-4 text-teal-700" />
                            </button>
                            <button
                              type="button"
                              onClick={(e) => toggleFavorite(e, dest.id)}
                              className={`btn-interactive p-2 rounded-lg transition ${
                                isFav ? 'bg-rose-50 text-rose-600' : 'hover:bg-slate-100 text-slate-500'
                              }`}
                              title="Favorite"
                            >
                              <Heart className={`w-4 h-4 ${isFav ? 'fill-rose-500 text-rose-500' : ''}`} />
                            </button>
                            <button
                              type="button"
                              onClick={(e) => {
                                e.stopPropagation();
                                navigate(`/trips/new?destinationId=${dest.id}`);
                              }}
                              className="btn-interactive btn-action-teal px-3.5 py-1.5 text-xs font-bold rounded-xl shadow-sm"
                            >
                              Plan Trip
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        )}

      </div>

      {/* Interactive Quick Peek Drawer / Modal */}
      {quickPeekDest && (
        <div 
          onClick={() => setQuickPeekDest(null)}
          className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm animate-in fade-in duration-200"
        >
          <div 
            onClick={(e) => e.stopPropagation()}
            className="bg-white rounded-3xl w-full max-w-lg border border-slate-200 shadow-2xl overflow-hidden animate-in zoom-in-95 duration-200"
          >
            <div className="relative h-56 overflow-hidden">
              <img
                src={quickPeekDest.heroImageUrl || quickPeekDest.imageUrl || 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=1200&q=80'}
                alt={quickPeekDest.name}
                className="w-full h-full object-cover"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-slate-950/80 via-slate-950/20 to-transparent" />
              
              <button
                onClick={() => setQuickPeekDest(null)}
                className="absolute top-4 right-4 p-2 bg-slate-900/60 hover:bg-slate-900 text-white rounded-full transition"
              >
                <X className="w-4 h-4" />
              </button>

              <div className="absolute bottom-4 left-5 right-5">
                <div className="flex items-center space-x-2 text-xs font-semibold text-teal-300 mb-1">
                  <MapPin className="w-3.5 h-3.5" />
                  <span>{quickPeekDest.country} • {quickPeekDest.continent}</span>
                </div>
                <h3 className="text-2xl font-black text-white font-heading">{quickPeekDest.name}</h3>
              </div>
            </div>

            <div className="p-6 space-y-4">
              <div className="grid grid-cols-3 gap-3 text-center">
                <div className="bg-slate-50 p-2.5 rounded-2xl border border-slate-200/80">
                  <span className="text-[10px] font-extrabold uppercase text-slate-700 block">Est. Daily</span>
                  <span className="text-base font-black text-teal-700">${quickPeekDest.averageDailyCost || 150}</span>
                </div>
                <div className="bg-slate-50 p-2.5 rounded-2xl border border-slate-200/80">
                  <span className="text-[10px] font-extrabold uppercase text-slate-700 block">Rating</span>
                  <span className="text-base font-black text-amber-500">★ {quickPeekDest.rating || 4.8}</span>
                </div>
                <div className="bg-slate-50 p-2.5 rounded-2xl border border-slate-200/80">
                  <span className="text-[10px] font-extrabold uppercase text-slate-700 block">Best Season</span>
                  <span className="text-xs font-bold text-slate-900 truncate block mt-0.5">{quickPeekDest.bestTimeToVisit || 'Spring'}</span>
                </div>
              </div>

              <p className="text-xs text-slate-700 font-medium leading-relaxed">
                {quickPeekDest.description}
              </p>

              <div className="flex items-center space-x-3 pt-2">
                <button
                  onClick={() => {
                    const dest = quickPeekDest;
                    setQuickPeekDest(null);
                    navigate(`/trips/new?destinationId=${dest.id}`);
                  }}
                  className="btn-interactive btn-action-teal flex-1 py-3 text-xs font-bold rounded-2xl shadow-sm"
                >
                  <Sparkles className="w-4 h-4" />
                  <span>Plan Trip Here</span>
                </button>
                <button
                  onClick={() => {
                    const dest = quickPeekDest;
                    setQuickPeekDest(null);
                    navigate(`/destinations/${dest.id}`);
                  }}
                  className="btn-interactive flex-1 py-3 bg-slate-100 hover:bg-slate-200 text-slate-900 text-xs font-bold rounded-2xl transition"
                >
                  <span>Full Analytics</span>
                  <ArrowRight className="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
