import React from 'react';
import { Heart, Globe, Shield, Sparkles, Mail, Github, Twitter } from 'lucide-react';
import { Link } from 'react-router-dom';
import BrandLogo from './BrandLogo';

export default function Footer() {
  return (
    <footer className="bg-white border-t border-slate-200/80 pt-16 pb-12 text-slate-700">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-10 pb-12 border-b border-slate-100">
          
          {/* Brand Info */}
          <div className="space-y-4 md:col-span-1">
            <BrandLogo />
            <p className="text-xs text-slate-700 leading-relaxed font-medium">
              Travel analytics and itinerary intelligence platform. Create custom itineraries, book flights & hotels, track expenses, and collaborate live.
            </p>
            <div className="flex space-x-2 pt-1">
              <a href="#" className="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center hover:bg-teal-600 hover:text-white text-slate-700 transition">
                <Twitter className="w-3.5 h-3.5" />
              </a>
              <a href="#" className="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center hover:bg-teal-600 hover:text-white text-slate-700 transition">
                <Github className="w-3.5 h-3.5" />
              </a>
              <a href="#" className="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center hover:bg-teal-600 hover:text-white text-slate-700 transition">
                <Mail className="w-3.5 h-3.5" />
              </a>
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-slate-900 font-bold text-xs uppercase tracking-wider mb-4 font-heading">Destinations</h4>
            <ul className="space-y-2 text-xs font-semibold">
              <li><Link to="/destinations?continent=Europe" className="text-slate-700 hover:text-teal-600 transition">Europe Exploration</Link></li>
              <li><Link to="/destinations?continent=Asia" className="text-slate-700 hover:text-teal-600 transition">Asia Wonders</Link></li>
              <li><Link to="/destinations?continent=North America" className="text-slate-700 hover:text-teal-600 transition">North America Escapes</Link></li>
              <li><Link to="/destinations?vibe=Beach" className="text-slate-700 hover:text-teal-600 transition">Tropical Beaches</Link></li>
              <li><Link to="/destinations?vibe=Mountain" className="text-slate-700 hover:text-teal-600 transition">Alpine Adventures</Link></li>
            </ul>
          </div>

          {/* Features */}
          <div>
            <h4 className="text-slate-900 font-bold text-xs uppercase tracking-wider mb-4 font-heading">Intelligence Suite</h4>
            <ul className="space-y-2 text-xs font-semibold">
              <li><Link to="/trips/new" className="text-slate-700 hover:text-teal-600 transition">Multi-Step Trip Creator</Link></li>
              <li><span className="text-slate-700 hover:text-teal-600 cursor-pointer">Live WebSocket Chat</span></li>
              <li><span className="text-slate-700 hover:text-teal-600 cursor-pointer">Split Expense Debt Calculator</span></li>
              <li><span className="text-slate-700 hover:text-teal-600 cursor-pointer">Flight & Hotel Simulator</span></li>
              <li><span className="text-slate-700 hover:text-teal-600 cursor-pointer">PDF & iCal Export</span></li>
            </ul>
          </div>

          {/* Trust Badges */}
          <div className="space-y-3">
            <h4 className="text-slate-900 font-bold text-xs uppercase tracking-wider mb-4 font-heading">Platform Highlights</h4>
            <div className="flex items-center space-x-2 text-xs text-slate-800 font-semibold">
              <Globe className="w-4 h-4 text-teal-600 flex-shrink-0" />
              <span>Coverage for 32+ global destinations</span>
            </div>
            <div className="flex items-center space-x-2 text-xs text-slate-800 font-semibold">
              <Shield className="w-4 h-4 text-teal-600 flex-shrink-0" />
              <span>Real-time conflict detection & alerts</span>
            </div>
            <div className="flex items-center space-x-2 text-xs text-slate-800 font-semibold">
              <Sparkles className="w-4 h-4 text-teal-600 flex-shrink-0" />
              <span>Automated multi-currency split engine</span>
            </div>
          </div>

        </div>

        {/* Bottom Bar */}
        <div className="pt-6 flex flex-col md:flex-row items-center justify-between text-[11px] text-slate-700 font-medium">
          <p>© 2026 WanderLust Travel Intelligence Suite. All rights reserved.</p>
          <div className="flex items-center space-x-1 mt-3 md:mt-0">
            <span>Crafted with</span>
            <Heart className="w-3 h-3 text-rose-500 fill-rose-500 inline" />
            <span>for modern global travelers.</span>
          </div>
        </div>
      </div>
    </footer>
  );
}

