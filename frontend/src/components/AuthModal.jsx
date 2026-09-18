import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { X, Mail, Lock, User, Sparkles, ArrowRight } from 'lucide-react';

export default function AuthModal({ mode = 'login', onClose, onSwitchMode }) {
  const { login, register } = useAuth();
  const [activeTab, setActiveTab] = useState(mode);
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      if (activeTab === 'login') {
        await login(email, password);
      } else {
        await register(fullName, email, password);
      }
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Authentication failed');
    } finally {
      setLoading(false);
    }
  };

  const fillDemoUser = async () => {
    setEmail('demo@wanderlust.com');
    setPassword('password123');
    try {
      setLoading(true);
      await login('demo@wanderlust.com', 'password123');
      onClose();
    } catch (err) {
      setError('Failed to login with demo credentials');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-md animate-in fade-in duration-200">
      <div className="relative w-full max-w-md glass-panel rounded-3xl p-6 sm:p-8 border border-slate-700/60 shadow-2xl overflow-hidden">
        {/* Decorative background glow */}
        <div className="absolute -top-24 -right-24 w-48 h-48 bg-cyan-500/20 rounded-full blur-3xl pointer-events-none" />
        <div className="absolute -bottom-24 -left-24 w-48 h-48 bg-indigo-500/20 rounded-full blur-3xl pointer-events-none" />

        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-5 right-5 p-2 text-slate-400 hover:text-slate-100 rounded-full hover:bg-slate-800 transition"
        >
          <X className="w-5 h-5" />
        </button>

        {/* Modal Header */}
        <div className="text-center mb-6">
          <div className="w-12 h-12 rounded-2xl bg-cyan-500/10 border border-cyan-500/20 flex items-center justify-center mx-auto mb-3 text-cyan-400">
            <Sparkles className="w-6 h-6" />
          </div>
          <h3 className="text-2xl font-black text-slate-100 font-heading">
            {activeTab === 'login' ? 'Welcome Back' : 'Create an Account'}
          </h3>
          <p className="text-xs text-slate-400 mt-1">
            Unlock seamless travel planning, live collaboration, and smart itineraries.
          </p>
        </div>

        {/* Quick Demo Button */}
        <button
          type="button"
          onClick={fillDemoUser}
          className="w-full mb-5 py-2.5 px-4 bg-gradient-to-r from-amber-500/20 to-orange-500/20 hover:from-amber-500/30 hover:to-orange-500/30 border border-amber-500/30 rounded-xl text-amber-300 text-xs font-bold flex items-center justify-center space-x-2 transition"
        >
          <Sparkles className="w-4 h-4 text-amber-400 animate-pulse" />
          <span>⚡ One-Click Demo Explorer Access</span>
        </button>

        {/* Tab switch */}
        <div className="flex bg-slate-950/80 p-1 rounded-xl border border-slate-800/80 mb-6">
          <button
            type="button"
            onClick={() => { setActiveTab('login'); setError(''); }}
            className={`flex-1 py-2 text-xs font-bold rounded-lg transition ${
              activeTab === 'login' ? 'bg-slate-800 text-cyan-400 shadow' : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            Log In
          </button>
          <button
            type="button"
            onClick={() => { setActiveTab('register'); setError(''); }}
            className={`flex-1 py-2 text-xs font-bold rounded-lg transition ${
              activeTab === 'register' ? 'bg-slate-800 text-cyan-400 shadow' : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            Register
          </button>
        </div>

        {/* Error message */}
        {error && (
          <div className="mb-4 p-3 bg-rose-500/10 border border-rose-500/30 rounded-xl text-rose-400 text-xs font-medium">
            {error}
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          {activeTab === 'register' && (
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1.5">Full Name</label>
              <div className="relative">
                <User className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  required
                  placeholder="Alex Rivera"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  className="w-full glass-input pl-10 text-xs"
                />
              </div>
            </div>
          )}

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1.5">Email Address</label>
            <div className="relative">
              <Mail className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
              <input
                type="email"
                required
                placeholder="alex@wanderlust.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full glass-input pl-10 text-xs"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1.5">Password</label>
            <div className="relative">
              <Lock className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
              <input
                type="password"
                required
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full glass-input pl-10 text-xs"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full mt-2 py-3 px-4 bg-gradient-to-r from-cyan-500 via-sky-500 to-indigo-600 hover:opacity-95 text-slate-950 font-black rounded-xl text-sm shadow-xl shadow-cyan-500/20 transition flex items-center justify-center space-x-2"
          >
            {loading ? (
              <div className="w-5 h-5 border-2 border-slate-950 border-t-transparent rounded-full animate-spin" />
            ) : (
              <>
                <span>{activeTab === 'login' ? 'Sign In to Account' : 'Create Free Account'}</span>
                <ArrowRight className="w-4 h-4" />
              </>
            )}
          </button>
        </form>

      </div>
    </div>
  );
}
