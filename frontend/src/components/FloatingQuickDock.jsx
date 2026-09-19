import React, { useState, useEffect } from 'react';
import { Calculator, Search, X, ArrowRightLeft, Sparkles, MapPin } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { destinationApi } from '../api/client';

const RATES = {
  USD: 1.0,
  EUR: 0.92,
  GBP: 0.79,
  JPY: 154.5,
  AUD: 1.52,
  CAD: 1.36,
  CHF: 0.89,
};

export default function FloatingQuickDock() {
  const navigate = useNavigate();
  const [openConverter, setOpenConverter] = useState(false);
  const [openSpotlight, setOpenSpotlight] = useState(false);

  // Converter state
  const [amount, setAmount] = useState('100');
  const [fromCurr, setFromCurr] = useState('USD');
  const [toCurr, setToCurr] = useState('EUR');

  // Spotlight state
  const [searchQuery, setSearchQuery] = useState('');
  const [destinations, setDestinations] = useState([]);

  useEffect(() => {
    // Load destinations once for quick spotlight search
    destinationApi.getAll()
      .then(res => {
        if (res.data.success && Array.isArray(res.data.data)) {
          setDestinations(res.data.data);
        }
      })
      .catch(() => {});

    // Listen for '/' key to open spotlight search
    const handleKeyDown = (e) => {
      if (e.key === '/' && !['INPUT', 'TEXTAREA'].includes(document.activeElement.tagName)) {
        e.preventDefault();
        setOpenSpotlight(true);
      }
      if (e.key === 'Escape') {
        setOpenSpotlight(false);
        setOpenConverter(false);
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  // Conversion math
  const numAmount = parseFloat(amount) || 0;
  const inUSD = numAmount / (RATES[fromCurr] || 1);
  const converted = (inUSD * (RATES[toCurr] || 1)).toFixed(2);

  const filteredSpotlight = searchQuery.trim()
    ? destinations.filter(d => 
        d.name?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        d.country?.toLowerCase().includes(searchQuery.toLowerCase()) ||
        d.continent?.toLowerCase().includes(searchQuery.toLowerCase())
      ).slice(0, 6)
    : destinations.slice(0, 5);

  return (
    <>
      {/* Floating Action Pill Dock (Bottom Right) */}
      <aside 
        aria-label="Quick travel utilities dock" 
        className="fixed bottom-6 right-6 z-40 flex items-center bg-white/95 backdrop-blur-md p-1.5 rounded-full border border-slate-200/90 shadow-xl transition-all duration-300 hover:shadow-2xl"
      >
        <button
          onClick={() => setOpenSpotlight(true)}
          title="Quick Search (Press /)"
          className="flex items-center space-x-2 px-3.5 py-2 rounded-full text-xs font-bold text-slate-800 hover:bg-slate-100 transition"
        >
          <Search className="w-3.5 h-3.5 text-teal-600" />
          <span className="hidden sm:inline">Quick Jump</span>
          <kbd className="hidden sm:inline bg-slate-200/80 text-slate-700 px-1.5 py-0.5 rounded text-[10px] font-mono">/</kbd>
        </button>

        <div className="w-px h-5 bg-slate-200 mx-1" />

        <button
          onClick={() => setOpenConverter(!openConverter)}
          title="Currency Converter Calculator"
          className="flex items-center space-x-2 px-3.5 py-2 rounded-full text-xs font-bold text-slate-800 hover:bg-teal-50 hover:text-teal-800 transition"
        >
          <Calculator className="w-3.5 h-3.5 text-teal-600" />
          <span className="hidden sm:inline">Converter</span>
        </button>
      </aside>

      {/* Currency Converter Modal */}
      {openConverter && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm animate-in fade-in duration-150">
          <div className="bg-white rounded-3xl p-6 w-full max-w-sm border border-slate-200 shadow-2xl relative">
            <button
              onClick={() => setOpenConverter(false)}
              className="absolute top-4 right-4 p-1.5 text-slate-500 hover:text-slate-800 rounded-full hover:bg-slate-100"
            >
              <X className="w-4 h-4" />
            </button>

            <div className="flex items-center space-x-2 mb-4">
              <div className="w-8 h-8 rounded-xl bg-teal-50 flex items-center justify-center text-teal-700 font-black">
                <ArrowRightLeft className="w-4 h-4" />
              </div>
              <div>
                <h3 className="text-base font-black text-slate-900 font-heading">Travel Currency Converter</h3>
                <p className="text-[11px] text-slate-600 font-medium">Live market estimation rates</p>
              </div>
            </div>

            <div className="space-y-3">
              <div>
                <label className="block text-[11px] font-bold text-slate-700 mb-1">Amount</label>
                <input
                  type="number"
                  min="1"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  className="w-full glass-input text-sm font-bold text-slate-900"
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-[11px] font-bold text-slate-700 mb-1">From</label>
                  <select
                    value={fromCurr}
                    onChange={(e) => setFromCurr(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl px-3 py-2 text-xs font-bold text-slate-800"
                  >
                    {Object.keys(RATES).map((c) => (
                      <option key={c} value={c}>{c}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-[11px] font-bold text-slate-700 mb-1">To</label>
                  <select
                    value={toCurr}
                    onChange={(e) => setToCurr(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl px-3 py-2 text-xs font-bold text-slate-800"
                  >
                    {Object.keys(RATES).map((c) => (
                      <option key={c} value={c}>{c}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="p-4 rounded-2xl bg-teal-50/80 border border-teal-200/80 text-center mt-2">
                <span className="text-[11px] font-bold text-teal-900 uppercase tracking-wider block">Estimated Result</span>
                <span className="text-2xl font-black text-teal-950 font-heading">
                  {converted} <span className="text-sm font-bold text-teal-800">{toCurr}</span>
                </span>
                <p className="text-[10px] text-teal-800 font-medium mt-1">
                  1 {fromCurr} ≈ {((RATES[toCurr] || 1) / (RATES[fromCurr] || 1)).toFixed(4)} {toCurr}
                </p>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Spotlight Quick Jump Modal */}
      {openSpotlight && (
        <div 
          onClick={() => setOpenSpotlight(false)} 
          className="fixed inset-0 z-50 flex items-start justify-center pt-24 p-4 bg-slate-900/40 backdrop-blur-sm animate-in fade-in duration-150"
        >
          <div 
            onClick={(e) => e.stopPropagation()} 
            className="bg-white rounded-3xl p-5 w-full max-w-lg border border-slate-200 shadow-2xl overflow-hidden"
          >
            <div className="relative mb-3">
              <Search className="w-5 h-5 text-slate-600 absolute left-3.5 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                autoFocus
                placeholder="Search any destination, country, or continent..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-11 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-2xl text-xs sm:text-sm font-bold text-slate-900 placeholder:text-slate-500 outline-none focus:border-teal-600 focus:bg-white transition"
              />
            </div>

            <div className="text-[10px] font-extrabold uppercase tracking-wider text-slate-600 px-1 py-1">
              {searchQuery ? `Matching Results (${filteredSpotlight.length})` : 'Popular Destinations'}
            </div>

            <div className="max-h-72 overflow-y-auto space-y-1.5 mt-1 pr-1 custom-scrollbar">
              {filteredSpotlight.length === 0 ? (
                <div className="p-6 text-center text-xs font-semibold text-slate-600">
                  No destinations match "{searchQuery}"
                </div>
              ) : (
                filteredSpotlight.map((dest) => (
                  <div
                    key={dest.id}
                    onClick={() => {
                      setOpenSpotlight(false);
                      navigate(`/destinations/${dest.id}`);
                    }}
                    className="flex items-center justify-between p-2.5 rounded-xl hover:bg-teal-50 cursor-pointer transition group"
                  >
                    <div className="flex items-center space-x-3">
                      <img 
                        src={dest.heroImageUrl || dest.imageUrl || 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=120&q=80'} 
                        alt={dest.name} 
                        className="w-10 h-10 rounded-lg object-cover" 
                      />
                      <div>
                        <div className="text-xs font-bold text-slate-900 group-hover:text-teal-800">{dest.name}</div>
                        <div className="text-[10px] font-medium text-slate-600 flex items-center space-x-1">
                          <MapPin className="w-3 h-3 text-teal-600" />
                          <span>{dest.country} • {dest.continent}</span>
                        </div>
                      </div>
                    </div>
                    <span className="text-[11px] font-bold text-teal-700">
                      ${dest.averageDailyCost || 150}/day →
                    </span>
                  </div>
                ))
              )}
            </div>

            <div className="pt-3 border-t border-slate-100 flex items-center justify-between text-[10px] text-slate-600 font-medium px-1">
              <span>Press <kbd className="bg-slate-100 px-1 rounded border">Esc</kbd> to close</span>
              <span>Click to view full destination analytics</span>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
