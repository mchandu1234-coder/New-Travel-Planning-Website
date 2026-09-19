import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { bookingApi, tripApi } from '../api/client';
import { useAuth } from '../context/AuthContext';
import { 
  Plane, Hotel, Search, Star, DollarSign, ArrowLeft, 
  ShieldCheck, CreditCard, Sparkles, CheckCircle2, X, Calendar 
} from 'lucide-react';
import confetti from 'canvas-confetti';

const CITY_TO_AIRPORT = {
  Tokyo: 'HND',
  Paris: 'CDG',
  Bali: 'DPS',
  Rome: 'FCO',
  Naples: 'NAP',
  Amalfi: 'NAP',
  Reykjavik: 'KEF',
  London: 'LHR',
  NewYork: 'JFK',
};

export default function BookingsHubPage() {
  const { id } = useParams();
  const { user } = useAuth();
  const [trip, setTrip] = useState(null);
  const [activeTab, setActiveTab] = useState('flights'); // 'flights' or 'hotels'
  const [flights, setFlights] = useState([]);
  const [hotels, setHotels] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedBooking, setSelectedBooking] = useState(null); // For Stripe checkout modal
  const [bookingSuccess, setBookingSuccess] = useState(false);

  // Search parameters
  const [origin, setOrigin] = useState('JFK');
  const [destinationCode, setDestinationCode] = useState('CDG');
  const [date, setDate] = useState('2026-10-15');

  useEffect(() => {
    tripApi.getById(id)
      .then((res) => {
        if (res.data.success) {
          const t = res.data.data;
          setTrip(t);
          if (t.startDate) setDate(t.startDate);
          const city = t.destination?.city || t.destination?.name || t.destinationName;
          if (city && CITY_TO_AIRPORT[city]) {
            setDestinationCode(CITY_TO_AIRPORT[city]);
          } else if (city) {
            setDestinationCode(city);
          }
        }
      })
      .catch(console.error);
  }, [id]);

  useEffect(() => {
    if (activeTab === 'flights') {
      searchFlights();
    } else {
      searchHotels();
    }
  }, [activeTab, destinationCode]);

  const searchFlights = () => {
    setLoading(true);
    bookingApi.searchFlights({ 
      origin: origin || 'JFK', 
      destination: destinationCode || 'CDG', 
      departureDate: date || '2026-10-15',
      date: date || '2026-10-15',
    })
      .then((res) => {
        if (res.data.success && Array.isArray(res.data.data)) {
          setFlights(res.data.data);
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  const searchHotels = () => {
    setLoading(true);
    const destCity = trip?.destination?.city || trip?.destination?.name || trip?.destinationName || destinationCode || 'Paris';
    bookingApi.searchHotels({ 
      destinationCity: destCity,
      checkInDate: date || '2026-10-15',
      checkOutDate: '2026-10-22',
      guests: 1,
      minRating: 3
    })
      .then((res) => {
        if (res.data.success && Array.isArray(res.data.data)) {
          setHotels(res.data.data);
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  const handleConfirmBooking = async () => {
    if (!selectedBooking) return;

    try {
      setLoading(true);
      let res;
      const passengerName = user?.fullName || 'Traveler';
      const passengerEmail = user?.email || 'traveler@wanderlust.com';

      if (activeTab === 'flights') {
        res = await bookingApi.bookFlight({
          tripId: Number(id),
          flightOffer: selectedBooking,
          passengerName,
          passengerEmail,
        });
      } else {
        res = await bookingApi.bookHotel({
          tripId: Number(id),
          hotelOffer: selectedBooking,
          checkInDate: date || '2026-10-15',
          checkOutDate: '2026-10-22',
          guestName: passengerName,
          guestEmail: passengerEmail,
        });
      }

      if (res.data.success) {
        setBookingSuccess(true);
        confetti({ particleCount: 120, spread: 80, origin: { y: 0.6 } });
        setTimeout(() => {
          setSelectedBooking(null);
          setBookingSuccess(false);
        }, 2000);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#f7faf9] bg-mesh text-slate-900 py-10 pb-20">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Header */}
        <div>
          <Link to={`/trips/${id}`} className="text-xs font-bold text-cyan-700 hover:underline flex items-center space-x-1 mb-2">
            <ArrowLeft className="w-3.5 h-3.5" />
            <span>Back to Trip Command Center</span>
          </Link>
          <h1 className="text-3xl font-black text-blue-950 font-heading">
            Flights & Hotels Booking Engine
          </h1>
          <p className="text-xs text-slate-700 mt-1 font-medium">
            Browse real-time simulated flight deals and luxury hotels, with automatic sync to your itinerary and budget expenses.
          </p>
        </div>

        {/* Tab Switcher */}
        <div className="flex bg-slate-100 p-1.5 rounded-2xl border border-slate-200 max-w-md">
          <button
            onClick={() => setActiveTab('flights')}
            className={`flex-1 py-3 text-xs font-bold rounded-xl transition flex items-center justify-center space-x-2 ${
              activeTab === 'flights' ? 'bg-cyan-600 text-white shadow-md' : 'text-slate-700 hover:text-slate-950'
            }`}
          >
            <Plane className="w-4 h-4" />
            <span>Flight Deals</span>
          </button>
          <button
            onClick={() => setActiveTab('hotels')}
            className={`flex-1 py-3 text-xs font-bold rounded-xl transition flex items-center justify-center space-x-2 ${
              activeTab === 'hotels' ? 'bg-sky-600 text-white shadow-md' : 'text-slate-700 hover:text-slate-950'
            }`}
          >
            <Hotel className="w-4 h-4" />
            <span>Luxury Accommodations</span>
          </button>
        </div>

        {/* Interactive Flight Search Bar */}
        {activeTab === 'flights' && (
          <div className="glass-panel p-5 rounded-3xl border border-slate-200 flex flex-wrap gap-4 items-end shadow-sm">
            <div className="flex-1 min-w-[140px]">
              <label className="block text-[11px] font-extrabold text-slate-800 mb-1">Departure Airport</label>
              <input
                type="text"
                value={origin}
                onChange={(e) => setOrigin(e.target.value.toUpperCase())}
                placeholder="JFK"
                className="w-full glass-input text-xs uppercase text-slate-900"
              />
            </div>

            <div className="flex-1 min-w-[140px]">
              <label className="block text-[11px] font-extrabold text-slate-800 mb-1">Destination Airport</label>
              <input
                type="text"
                value={destinationCode}
                onChange={(e) => setDestinationCode(e.target.value.toUpperCase())}
                placeholder="CDG"
                className="w-full glass-input text-xs uppercase text-slate-900"
              />
            </div>

            <div className="flex-1 min-w-[160px]">
              <label className="block text-[11px] font-extrabold text-slate-800 mb-1">Flight Date</label>
              <input
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                className="w-full glass-input text-xs text-slate-900"
              />
            </div>

            <button
              onClick={searchFlights}
              className="px-6 py-3 bg-cyan-600 hover:bg-cyan-700 text-white font-black rounded-xl text-xs flex items-center space-x-2 shadow-lg shadow-cyan-600/20 transition"
            >
              <Search className="w-4 h-4" />
              <span>Search Flights</span>
            </button>
          </div>
        )}

        {/* Flights List */}
        {activeTab === 'flights' && (
          <div className="space-y-4">
            {loading ? (
              <div className="flex justify-center py-20">
                <div className="w-10 h-10 border-4 border-cyan-600 border-t-transparent rounded-full animate-spin" />
              </div>
            ) : flights.length === 0 ? (
              <div className="glass-panel p-12 rounded-3xl text-center space-y-3 border border-slate-200 shadow-sm">
                <Plane className="w-10 h-10 text-slate-400 mx-auto" />
                <h4 className="text-base font-bold text-blue-950">No flights found for this route</h4>
                <p className="text-xs text-slate-600">Try searching from JFK, SFO, or LHR to CDG, HND, or DPS.</p>
              </div>
            ) : (
              flights.map((f, i) => (
                <div
                  key={i}
                  className="glass-panel p-6 rounded-3xl border border-slate-200 flex flex-col md:flex-row md:items-center justify-between gap-6 hover:border-cyan-500/50 transition shadow-sm"
                >
                  <div className="flex items-center space-x-4">
                    <div className="w-12 h-12 rounded-2xl bg-cyan-50 border border-cyan-200 flex items-center justify-center text-cyan-700">
                      <Plane className="w-6 h-6" />
                    </div>
                    <div>
                      <div className="flex items-center space-x-2">
                        <span className="text-base font-bold text-slate-900">{f.airline}</span>
                        <span className="text-xs text-slate-600 font-bold">({f.flightNumber})</span>
                      </div>
                      <p className="text-xs text-slate-600 mt-1 font-medium">
                        {f.originCode || f.origin || origin} ➔ {f.destinationCode || f.destination || destinationCode} • Non-stop • {f.durationMinutes ? `${Math.floor(f.durationMinutes / 60)}h ${f.durationMinutes % 60}m` : (f.duration || '2h 45m')}
                      </p>
                    </div>
                  </div>

                  <div className="flex items-center space-x-6">
                    <div className="text-right">
                      <div className="text-xs text-slate-600 font-bold">Price per ticket</div>
                      <div className="text-2xl font-black text-emerald-800">${f.price}</div>
                    </div>

                    <button
                      onClick={() => setSelectedBooking(f)}
                      className="px-6 py-3 bg-gradient-to-r from-cyan-500 to-sky-500 hover:opacity-95 text-slate-950 font-black rounded-2xl text-xs shadow-lg shadow-cyan-500/20 transition"
                    >
                      Book & Add to Trip
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        )}

        {/* Hotels List */}
        {activeTab === 'hotels' && (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {loading ? (
              <div className="flex justify-center py-20 col-span-2">
                <div className="w-10 h-10 border-4 border-sky-600 border-t-transparent rounded-full animate-spin" />
              </div>
            ) : (
              hotels.map((h, i) => (
                <div
                  key={i}
                  className="glass-panel p-5 rounded-3xl border border-slate-200 space-y-4 hover:border-sky-500/50 transition shadow-sm"
                >
                  <img src={h.imageUrl} alt={h.name} className="w-full h-48 object-cover rounded-2xl" />
                  <div className="flex justify-between items-start">
                    <div>
                      <h4 className="text-lg font-bold text-slate-900 font-heading">{h.name}</h4>
                      <p className="text-xs text-slate-600 mt-0.5">{h.address}</p>
                    </div>
                    <div className="flex items-center space-x-1 text-amber-500 text-xs font-bold">
                      <Star className="w-4 h-4 fill-amber-400" />
                      <span>{h.rating}</span>
                    </div>
                  </div>

                  <div className="flex justify-between items-center pt-3 border-t border-slate-100">
                    <div>
                      <span className="text-[10px] text-slate-600 uppercase block font-extrabold">Price / Night</span>
                      <span className="text-xl font-black text-emerald-800">${h.pricePerNight}</span>
                    </div>
                    <button
                      onClick={() => setSelectedBooking(h)}
                      className="px-5 py-2.5 bg-sky-600 hover:bg-sky-700 text-white font-extrabold rounded-xl text-xs transition shadow-sm"
                    >
                      Reserve Room
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        )}

      </div>

      {/* Simulated Stripe Checkout Modal */}
      {selectedBooking && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm">
          <div className="bg-white w-full max-w-md rounded-3xl p-6 border border-slate-200 shadow-2xl relative">
            <button
              onClick={() => setSelectedBooking(null)}
              className="absolute top-5 right-5 p-2 text-slate-400 hover:text-slate-800 rounded-full hover:bg-slate-100"
            >
              <X className="w-5 h-5" />
            </button>

            {bookingSuccess ? (
              <div className="text-center py-8 space-y-3">
                <CheckCircle2 className="w-16 h-16 text-emerald-600 mx-auto animate-bounce" />
                <h3 className="text-2xl font-black text-blue-950 font-heading">Reservation Confirmed!</h3>
                <p className="text-xs text-slate-700 font-medium">
                  Payment processed via Stripe. Added directly to your itinerary & budget tracker.
                </p>
              </div>
            ) : (
              <div className="space-y-6">
                <div className="flex items-center space-x-3 text-cyan-700">
                  <CreditCard className="w-6 h-6" />
                  <h3 className="text-xl font-black font-heading text-blue-950">Stripe Instant Checkout</h3>
                </div>

                <div className="bg-slate-50 p-4 rounded-2xl border border-slate-200 space-y-2 text-xs">
                  <div className="flex justify-between text-slate-700 font-medium">
                    <span>Booking item:</span>
                    <span className="font-bold text-slate-900">{selectedBooking.airline || selectedBooking.name}</span>
                  </div>
                  <div className="flex justify-between text-slate-700 font-medium">
                    <span>Total Price:</span>
                    <span className="font-black text-emerald-800 text-sm">
                      ${selectedBooking.price || selectedBooking.pricePerNight}
                    </span>
                  </div>
                </div>

                <div className="space-y-3">
                  <input
                    type="text"
                    disabled
                    value="•••• •••• •••• 4242 (Simulated Demo Card)"
                    className="w-full glass-input text-xs text-slate-700 bg-slate-50 border-slate-200"
                  />
                  <div className="flex space-x-2">
                    <input type="text" disabled value="12/28" className="w-1/2 glass-input text-xs text-slate-700 bg-slate-50 border-slate-200" />
                    <input type="text" disabled value="888" className="w-1/2 glass-input text-xs text-slate-700 bg-slate-50 border-slate-200" />
                  </div>
                </div>

                <button
                  onClick={handleConfirmBooking}
                  disabled={loading}
                  className="w-full py-3.5 bg-gradient-to-r from-emerald-500 to-teal-600 text-white font-black rounded-xl text-sm shadow-xl shadow-emerald-500/20 transition flex items-center justify-center space-x-2"
                >
                  {loading ? (
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
                  ) : (
                    <>
                      <ShieldCheck className="w-4 h-4" />
                      <span>Confirm & Pay Now</span>
                    </>
                  )}
                </button>
              </div>
            )}
          </div>
        </div>
      )}

    </div>
  );
}
