import React from 'react';
import { AlertTriangle, Clock, ShieldAlert } from 'lucide-react';

export default function ConflictAlert({ conflicts = [] }) {
  if (!conflicts || conflicts.length === 0) return null;

  return (
    <div className="bg-amber-500/10 border border-amber-500/30 rounded-2xl p-4 mb-6 animate-in fade-in duration-300">
      <div className="flex items-center space-x-2 text-amber-400 font-bold text-sm mb-2 font-heading">
        <AlertTriangle className="w-5 h-5 text-amber-400 flex-shrink-0 animate-bounce" />
        <span>Schedule Optimization & Conflict Warnings ({conflicts.length})</span>
      </div>
      <div className="space-y-2">
        {conflicts.map((conflict, index) => (
          <div
            key={index}
            className={`p-3 rounded-xl text-xs flex items-start space-x-2 border ${
              conflict.type === 'OVERLAP'
                ? 'bg-rose-500/10 border-rose-500/30 text-rose-300'
                : 'bg-amber-500/10 border-amber-500/30 text-amber-300'
            }`}
          >
            {conflict.type === 'OVERLAP' ? (
              <ShieldAlert className="w-4 h-4 text-rose-400 flex-shrink-0 mt-0.5" />
            ) : (
              <Clock className="w-4 h-4 text-amber-400 flex-shrink-0 mt-0.5" />
            )}
            <div>
              <span className="font-bold uppercase tracking-wider text-[10px] mr-2 px-1.5 py-0.5 rounded bg-slate-900/60">
                {conflict.type}
              </span>
              <span>{conflict.message}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
