import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  Compass, Calendar, LogOut, Sparkles, ChevronDown, Plus 
} from 'lucide-react';
import AuthModal from './AuthModal';
import BrandLogo from './BrandLogo';

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
      <nav className="sticky top-0 z-40 bg-white/95 backdrop-blur-xl border-b border-slate-200/90 shadow-sm transition-all">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-18 py-3">
            {/* Logo */}
            <BrandLogo />

            {/* Nav Links */}
            <div className="hidden md:flex items-center space-x-1.5 bg-slate-100/90 p-1.5 rounded-full border border-slate-200/90 shadow-inner">
              {navLinks.map((link) => {
                const Icon = link.icon;
                const isActive = location.pathname === link.path;
                return (
                  <Link
                    key={link.path}
                    to={link.path}
                    className={`btn-interactive flex items-center space-x-1.5 px-4 py-1.5 rounded-full text-xs font-black transition-all duration-200 ${
                      isActive
                        ? 'btn-action-cyan shadow-md shadow-cyan-500/25'
                        : 'text-slate-700 hover:text-cyan-800 hover:bg-white'
                    }`}
                  >
                    <Icon className="w-3.5 h-3.5" />
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
                  className="btn-interactive bg-white border border-slate-200 text-slate-800 text-xs font-black rounded-full px-3.5 py-1.5 outline-none focus:border-teal-500 cursor-pointer appearance-none pr-7 hover:border-teal-300 hover:shadow-sm transition"
                >
                  {CURRENCIES.map((c) => (
                    <option key={c.code} value={c.code}>
                      {c.symbol} {c.code}
                    </option>
                  ))}
                </select>
                <ChevronDown className="w-3.5 h-3.5 text-slate-600 absolute right-2.5 top-1/2 -translate-y-1/2 pointer-events-none" />
              </div>

              {/* Auth state */}
              {isAuthenticated ? (
                <div className="relative">
                  <button
                    onClick={() => setShowUserMenu(!showUserMenu)}
                    className="btn-interactive flex items-center space-x-2.5 bg-white hover:bg-slate-50 border border-slate-200/90 rounded-full p-1 pr-3.5 transition-all shadow-sm"
                  >
                    <img
                      src={user?.avatarUrl || `https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80`}
                      alt={user?.fullName}
                      className="w-7 h-7 rounded-full object-cover border border-teal-500/40"
                    />
                    <span className="text-xs font-black text-slate-900 hidden sm:inline">
                      {user?.fullName?.split(' ')[0]}
                    </span>
                    <ChevronDown className="w-3.5 h-3.5 text-slate-600" />
                  </button>

                  {/* Dropdown Menu */}
                  {showUserMenu && (
                    <div className="absolute right-0 mt-2 w-56 bg-white rounded-2xl p-2 z-50 shadow-xl border border-slate-200/90 animate-in fade-in duration-200">
                      <div className="px-3 py-2 border-b border-slate-100 mb-1">
                        <p className="text-xs font-black text-slate-900">{user?.fullName}</p>
                        <p className="text-[11px] text-slate-600 font-medium truncate">{user?.email}</p>
                      </div>
                      <Link
                        to="/dashboard"
                        onClick={() => setShowUserMenu(false)}
                        className="btn-interactive w-full flex items-center space-x-2 px-3 py-2 rounded-xl text-xs font-bold text-slate-700 hover:text-teal-700 hover:bg-teal-50 transition"
                      >
                        <Calendar className="w-3.5 h-3.5" />
                        <span>My Dashboard</span>
                      </Link>
                      <button
                        onClick={() => {
                          setShowUserMenu(false);
                          logout();
                          navigate('/');
                        }}
                        className="btn-interactive w-full flex items-center space-x-2 px-3 py-2 rounded-xl text-xs font-bold text-rose-600 hover:bg-rose-50 transition"
                      >
                        <LogOut className="w-3.5 h-3.5" />
                        <span>Sign Out</span>
                      </button>
                    </div>
                  )}
                </div>
              ) : (
                <div className="flex items-center space-x-2">
                  <button
                    onClick={() => openAuth('login')}
                    className="btn-interactive px-4 py-1.5 text-xs font-black text-slate-700 hover:text-slate-950 transition"
                  >
                    Log In
                  </button>
                  <button
                    onClick={() => openAuth('register')}
                    className="btn-interactive btn-action-teal px-5 py-2 text-white font-black rounded-full text-xs shadow-md"
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
