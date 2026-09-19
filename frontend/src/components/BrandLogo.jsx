import React from 'react';
import { Link } from 'react-router-dom';

export function RetentionBarIcon({ size = 'md', className = '' }) {
  const isLarge = size === 'lg';
  const isSmall = size === 'sm';

  return (
    <div className={`inline-flex items-end justify-center space-x-[3.5px] ${className}`}>
      <span className={`bg-gradient-to-t from-teal-500 to-cyan-400 rounded-full ${isLarge ? 'w-2.5 h-8' : isSmall ? 'w-1 h-3.5' : 'w-1.5 h-5'}`} />
      <span className={`bg-gradient-to-t from-teal-600 to-cyan-400 rounded-full ${isLarge ? 'w-2.5 h-12' : isSmall ? 'w-1 h-5' : 'w-1.5 h-7'}`} />
      <span className={`bg-gradient-to-t from-teal-500 to-cyan-400 rounded-full ${isLarge ? 'w-2.5 h-10' : isSmall ? 'w-1 h-4' : 'w-1.5 h-6'}`} />
    </div>
  );
}

export default function BrandLogo({ showSubtitle = true }) {
  return (
    <Link to="/" className="flex items-center space-x-3 group">
      <div className="w-10 h-10 rounded-2xl bg-white border border-slate-200/90 shadow-sm flex items-center justify-center p-1.5 group-hover:scale-105 transition-all">
        <RetentionBarIcon size="sm" />
      </div>
      <div className="flex flex-col text-left">
        {showSubtitle && (
          <span className="text-[10px] uppercase font-extrabold tracking-widest text-slate-700 -mb-0.5">
            TRAVEL ANALYTICS SUITE
          </span>
        )}
        <span className="text-xl font-black tracking-tight text-slate-900 font-heading">
          Wander<span className="text-teal-600">Lust</span>
        </span>
      </div>
    </Link>
  );
}
