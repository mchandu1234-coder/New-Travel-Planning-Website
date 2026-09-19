import React from 'react';
import { AlertTriangle, Clock, ShieldAlert } from 'lucide-react';

export default function ConflictAlert({ conflicts = [] }) {
  if (!conflicts || conflicts.length === 0) return null;

  return (
    <div className="bg-amber-50 border border-amber-200 rounded-2xl p-4 mb-6 shadow-sm animate-in fade-in duration-300">
      <div className="flex items-center space-x-2 text-amber-900 font-bold text-sm mb-2 font-heading">
        <AlertTriangle className="w-5 h-5 text-amber-600 flex-shrink-0 animate-bounce" />
        <span>Schedule Optimization & Conflict Warnings ({conflicts.length})</span>
      </div>
      <div className="space-y-2">
        {conflicts.map((conflict, index) => (
          <div
            key={index}
            className={`p-3 rounded-xl text-xs flex items-start space-x-2 border ${
              conflict.type === 'OVERLAP'
                ? 'bg-rose-50 border-rose-200 text-rose-900 font-medium'
                : 'bg-amber-100/70 border-amber-200 text-amber-950 font-medium'
            }`}
          >
            {conflict.type === 'OVERLAP' ? (
              <ShieldAlert className="w-4 h-4 text-rose-600 flex-shrink-0 mt-0.5" />
            ) : (
              <Clock className="w-4 h-4 text-amber-600 flex-shrink-0 mt-0.5" />
            )}
            <div>
              <span className="font-bold uppercase tracking-wider text-[10px] mr-2 px-1.5 py-0.5 rounded bg-white border border-slate-200 text-slate-900 shadow-sm">
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
