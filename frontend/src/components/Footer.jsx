import React from 'react';
import { Compass, Heart, Globe, Shield, Sparkles, Mail, Github, Twitter } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function Footer() {
  return (
    <footer className="bg-slate-950 border-t border-slate-800/80 pt-16 pb-12 text-slate-400">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-10 pb-12 border-b border-slate-900">
          
          {/* Brand Info */}
          <div className="space-y-4 md:col-span-1">
            <Link to="/" className="flex items-center space-x-3">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-cyan-500 to-indigo-500 p-0.5 shadow-md shadow-cyan-500/20">
                <div className="w-full h-full bg-slate-950 rounded-[10px] flex items-center justify-center">
                  <Compass className="w-5 h-5 text-cyan-400" />
                </div>
              </div>
              <span className="text-xl font-extrabold text-slate-100 font-heading">
                Wander<span className="text-cyan-400">Lust</span>
              </span>
            </Link>
            <p className="text-sm text-slate-400 leading-relaxed">
              Next-generation travel planning platform. Create custom itineraries, book flights & hotels, track expenses, and collaborate live.
            </p>
            <div className="flex space-x-3 pt-2">
              <a href="#" className="w-9 h-9 rounded-xl bg-slate-900 border border-slate-800 flex items-center justify-center hover:border-cyan-500 hover:text-cyan-400 transition">
                <Twitter className="w-4 h-4" />
              </a>
              <a href="#" className="w-9 h-9 rounded-xl bg-slate-900 border border-slate-800 flex items-center justify-center hover:border-cyan-500 hover:text-cyan-400 transition">
                <Github className="w-4 h-4" />
              </a>
              <a href="#" className="w-9 h-9 rounded-xl bg-slate-900 border border-slate-800 flex items-center justify-center hover:border-cyan-500 hover:text-cyan-400 transition">
                <Mail className="w-4 h-4" />
              </a>
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-slate-200 font-bold text-sm uppercase tracking-wider mb-4 font-heading">Destinations</h4>
            <ul className="space-y-2.5 text-sm">
              <li><Link to="/destinations?continent=Europe" className="hover:text-cyan-400 transition">Europe Exploration</Link></li>
              <li><Link to="/destinations?continent=Asia" className="hover:text-cyan-400 transition">Asia Wonders</Link></li>
              <li><Link to="/destinations?continent=North America" className="hover:text-cyan-400 transition">North America Escapes</Link></li>
              <li><Link to="/destinations?vibe=Beach" className="hover:text-cyan-400 transition">Tropical Beaches</Link></li>
              <li><Link to="/destinations?vibe=Mountain" className="hover:text-cyan-400 transition">Alpine Adventures</Link></li>
            </ul>
          </div>

          {/* Features */}
          <div>
            <h4 className="text-slate-200 font-bold text-sm uppercase tracking-wider mb-4 font-heading">Smart Features</h4>
            <ul className="space-y-2.5 text-sm">
              <li><Link to="/trips/new" className="hover:text-cyan-400 transition">Multi-Step Trip Creator</Link></li>
              <li><span className="text-slate-400 hover:text-cyan-400 cursor-pointer">Live WebSocket Chat</span></li>
              <li><span className="text-slate-400 hover:text-cyan-400 cursor-pointer">Split Expense Debt Calculator</span></li>
              <li><span className="text-slate-400 hover:text-cyan-400 cursor-pointer">Flight & Hotel Simulator</span></li>
              <li><span className="text-slate-400 hover:text-cyan-400 cursor-pointer">PDF & iCal Export</span></li>
            </ul>
          </div>

          {/* Trust Badges */}
          <div className="space-y-4">
            <h4 className="text-slate-200 font-bold text-sm uppercase tracking-wider mb-4 font-heading">Why Travel With Us</h4>
            <div className="flex items-center space-x-3 text-sm text-slate-300">
              <Globe className="w-5 h-5 text-cyan-400 flex-shrink-0" />
              <span>Coverage for over 250+ global destinations</span>
            </div>
            <div className="flex items-center space-x-3 text-sm text-slate-300">
              <Shield className="w-5 h-5 text-emerald-400 flex-shrink-0" />
              <span>Real-time conflict detection and schedule validation</span>
            </div>
            <div className="flex items-center space-x-3 text-sm text-slate-300">
              <Sparkles className="w-5 h-5 text-indigo-400 flex-shrink-0" />
              <span>Automated multi-currency debt settlement engine</span>
            </div>
          </div>

        </div>

        {/* Bottom Bar */}
        <div className="pt-8 flex flex-col md:flex-row items-center justify-between text-xs text-slate-400">
          <p>© 2026 WanderLust Travel Inc. All rights reserved.</p>
          <div className="flex items-center space-x-1 mt-4 md:mt-0">
            <span>Crafted with</span>
            <Heart className="w-3.5 h-3.5 text-rose-500 fill-rose-500 inline" />
            <span>for modern global travelers.</span>
          </div>
        </div>
      </div>
    </footer>
  );
}
