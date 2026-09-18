import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  Compass, MapPin, Calendar, DollarSign, Users, Download, 
  LogOut, User, Sparkles, ChevronDown, DollarSign as CurrencyIcon 
} from 'lucide-react';
import AuthModal from './AuthModal';

const CURRENCIES = [
  { code: 'USD', symbol: '$' },
  { code: 'EUR', symbol: '€' },
  { code: 'GBP', symbol: '£' },
  { code: 'JPY', symbol: '¥' },
  { code: 'INR', symbol: '₹' },
];

export default function Navbar() {
  const { user, logout, isAuthenticated, selectedCurrency, setSelectedCurrency } = useAuth();
  const [showAuthModal, setShowAuthModal] = useState(false);
  const [authMode, setAuthMode] = useState('login');
  const [showUserMenu, setShowUserMenu] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  const openAuth = (mode) => {
    setAuthMode(mode);
    setShowAuthModal(true);
  };

  const navLinks = [
    { label: 'Explore', path: '/destinations', icon: Compass },
    { label: 'Plan Trip', path: '/trips/new', icon: Sparkles },
    ...(isAuthenticated ? [
      { label: 'My Trips', path: '/dashboard', icon: Calendar },
    ] : []),
  ];

  return (
    <>
      <nav className="sticky top-0 z-40 bg-slate-950/80 backdrop-blur-xl border-b border-slate-800/80 transition-all">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-20">
            {/* Logo */}
            <Link to="/" className="flex items-center space-x-3 group">
              <div className="w-11 h-11 rounded-2xl bg-gradient-to-tr from-cyan-500 via-sky-400 to-indigo-500 p-0.5 shadow-lg shadow-cyan-500/20 group-hover:scale-105 transition-transform duration-300">
                <div className="w-full h-full bg-slate-950 rounded-[14px] flex items-center justify-center">
                  <Compass className="w-6 h-6 text-cyan-400 group-hover:rotate-45 transition-transform duration-500" />
                </div>
              </div>
              <div className="flex flex-col">
                <span className="text-2xl font-black tracking-tight text-slate-100 font-heading">
                  Wander<span className="text-cyan-400">Lust</span>
                </span>
                <span className="text-[10px] uppercase font-bold tracking-widest text-slate-400 -mt-1">
                  AI Trip Suite
                </span>
              </div>
            </Link>

            {/* Nav Links */}
            <div className="hidden md:flex items-center space-x-1 bg-slate-900/60 p-1.5 rounded-full border border-slate-800/80">
              {navLinks.map((link) => {
                const Icon = link.icon;
                const isActive = location.pathname === link.path;
                return (
                  <Link
                    key={link.path}
                    to={link.path}
                    className={`flex items-center space-x-2 px-4 py-2 rounded-full text-sm font-semibold transition-all duration-200 ${
                      isActive
                        ? 'bg-gradient-to-r from-cyan-500 to-blue-600 text-white shadow-md shadow-cyan-500/20'
                        : 'text-slate-300 hover:text-white hover:bg-slate-800/60'
                    }`}
                  >
                    <Icon className="w-4 h-4" />
                    <span>{link.label}</span>
                  </Link>
                );
              })}
            </div>

            {/* Right Actions */}
            <div className="flex items-center space-x-3">
              {/* Currency Selector */}
              <div className="relative group">
                <select
                  value={selectedCurrency}
                  onChange={(e) => setSelectedCurrency(e.target.value)}
                  className="bg-slate-900 border border-slate-800 text-slate-200 text-xs font-bold rounded-xl px-3 py-2 outline-none focus:border-cyan-500 cursor-pointer appearance-none pr-7 hover:border-slate-700 transition"
                >
                  {CURRENCIES.map((c) => (
                    <option key={c.code} value={c.code}>
                      {c.symbol} {c.code}
                    </option>
                  ))}
                </select>
                <ChevronDown className="w-3.5 h-3.5 text-slate-400 absolute right-2 top-1/2 -translate-y-1/2 pointer-events-none" />
              </div>

              {/* Auth state */}
              {isAuthenticated ? (
                <div className="relative">
                  <button
                    onClick={() => setShowUserMenu(!showUserMenu)}
                    className="flex items-center space-x-3 bg-slate-900/80 hover:bg-slate-800 border border-slate-800/90 rounded-2xl p-1.5 pr-4 transition-all"
                  >
                    <img
                      src={user?.avatarUrl || `https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80`}
                      alt={user?.fullName}
                      className="w-8 h-8 rounded-xl object-cover border border-cyan-500/30"
                    />
                    <span className="text-sm font-semibold text-slate-200 hidden sm:inline">
                      {user?.fullName?.split(' ')[0]}
                    </span>
                    <ChevronDown className="w-4 h-4 text-slate-400" />
                  </button>

                  {/* Dropdown Menu */}
                  {showUserMenu && (
                    <div className="absolute right-0 mt-2 w-56 glass-panel rounded-2xl p-2 z-50 animate-in fade-in slide-in-from-top-2 duration-200">
                      <div className="px-3 py-2 border-b border-slate-800/80 mb-1">
                        <p className="text-sm font-bold text-slate-100">{user?.fullName}</p>
                        <p className="text-xs text-slate-400 truncate">{user?.email}</p>
                      </div>
                      <Link
                        to="/dashboard"
                        onClick={() => setShowUserMenu(false)}
                        className="flex items-center space-x-2 px-3 py-2.5 rounded-xl text-sm font-medium text-slate-300 hover:text-cyan-400 hover:bg-slate-800/60 transition"
                      >
                        <Calendar className="w-4 h-4" />
                        <span>My Dashboard</span>
                      </Link>
                      <button
                        onClick={() => {
                          setShowUserMenu(false);
                          logout();
                          navigate('/');
                        }}
                        className="w-full flex items-center space-x-2 px-3 py-2.5 rounded-xl text-sm font-medium text-rose-400 hover:bg-rose-500/10 transition"
                      >
                        <LogOut className="w-4 h-4" />
                        <span>Sign Out</span>
                      </button>
                    </div>
                  )}
                </div>
              ) : (
                <div className="flex items-center space-x-2">
                  <button
                    onClick={() => openAuth('login')}
                    className="px-4 py-2 text-sm font-semibold text-slate-300 hover:text-white transition"
                  >
                    Log In
                  </button>
                  <button
                    onClick={() => openAuth('register')}
                    className="px-4 py-2 bg-gradient-to-r from-cyan-500 to-sky-500 hover:from-cyan-400 hover:to-sky-400 text-slate-950 font-bold rounded-xl text-sm shadow-lg shadow-cyan-500/20 hover:shadow-cyan-500/30 transition-all transform active:scale-95"
                  >
                    Get Started
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>

      {/* Auth Modal */}
      {showAuthModal && (
        <AuthModal
          mode={authMode}
          onClose={() => setShowAuthModal(false)}
          onSwitchMode={(mode) => setAuthMode(mode)}
        />
      )}
    </>
  );
}
