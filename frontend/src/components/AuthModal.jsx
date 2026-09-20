import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { X, Mail, Lock, User, Sparkles, ArrowRight } from 'lucide-react';
import { RetentionBarIcon } from './BrandLogo';

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
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm animate-in fade-in duration-200">
      <div className="relative w-full max-w-md bg-white rounded-3xl p-6 sm:p-8 border border-slate-200 shadow-2xl overflow-hidden">
        
        {/* Close button */}
        <button
          onClick={onClose}
          className="btn-interactive absolute top-5 right-5 p-2 text-slate-500 hover:text-slate-900 rounded-full hover:bg-slate-100 transition"
        >
          <X className="w-5 h-5" />
        </button>

        {/* Modal Header */}
        <div className="text-center mb-6">
          <div className="w-12 h-12 rounded-2xl bg-teal-50 border border-teal-100 flex items-center justify-center mx-auto mb-3 shadow-inner">
            <RetentionBarIcon size="sm" />
          </div>
          <h3 className="text-2xl font-black text-slate-900 font-heading">
            {activeTab === 'login' ? 'Welcome Back' : 'Create an Account'}
          </h3>
          <p className="text-xs text-slate-600 font-medium mt-1">
            Access 100+ destinations, conflict checks, and group budgets.
          </p>
        </div>

        {/* Quick Demo Button with Shimmer & Vibrant Theme */}
        <button
          type="button"
          onClick={fillDemoUser}
          className="btn-interactive w-full mb-5 py-3 px-4 bg-gradient-to-r from-teal-500/15 via-cyan-500/20 to-sky-500/15 hover:from-teal-500/25 hover:to-sky-500/25 border-2 border-teal-500/40 rounded-2xl text-teal-950 text-xs font-black flex items-center justify-center space-x-2 transition shadow-sm"
        >
          <Sparkles className="w-4 h-4 text-teal-600 animate-pulse" />
          <span>⚡ One-Click Demo Explorer Access</span>
        </button>

        {/* Tab switch */}
        <div className="flex bg-slate-100 p-1.5 rounded-2xl mb-6 shadow-inner">
          <button
            type="button"
            onClick={() => { setActiveTab('login'); setError(''); }}
            className={`btn-interactive flex-1 py-2 text-xs font-black rounded-xl transition ${
              activeTab === 'login' ? 'bg-white text-teal-900 shadow-md' : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Log In
          </button>
          <button
            type="button"
            onClick={() => { setActiveTab('register'); setError(''); }}
            className={`btn-interactive flex-1 py-2 text-xs font-black rounded-xl transition ${
              activeTab === 'register' ? 'bg-white text-teal-900 shadow-md' : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Register
          </button>
        </div>

        {/* Error message */}
        {error && (
          <div className="mb-4 p-3 bg-rose-50 border border-rose-200 rounded-xl text-rose-700 text-xs font-bold">
            {error}
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          {activeTab === 'register' && (
            <div>
              <label className="block text-xs font-extrabold text-slate-800 mb-1.5">Full Name</label>
              <div className="relative">
                <User className="w-4 h-4 text-slate-500 absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  required
                  placeholder="Alex Morgan"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  className="w-full glass-input pl-10 text-xs font-bold text-slate-900 placeholder:text-slate-400"
                />
              </div>
            </div>
          )}

          <div>
            <label className="block text-xs font-extrabold text-slate-800 mb-1.5">Email Address</label>
            <div className="relative">
              <Mail className="w-4 h-4 text-slate-500 absolute left-3.5 top-1/2 -translate-y-1/2" />
              <input
                type="email"
                required
                placeholder="demo@wanderlust.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full glass-input pl-10 text-xs font-bold text-slate-900 placeholder:text-slate-400"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-extrabold text-slate-800 mb-1.5">Password</label>
            <div className="relative">
              <Lock className="w-4 h-4 text-slate-500 absolute left-3.5 top-1/2 -translate-y-1/2" />
              <input
                type="password"
                required
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full glass-input pl-10 text-xs font-bold text-slate-900 placeholder:text-slate-400"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="btn-interactive btn-action-teal w-full mt-2 py-3 px-4 text-xs font-black shadow-md flex items-center justify-center space-x-2"
          >
            {loading ? (
              <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
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
