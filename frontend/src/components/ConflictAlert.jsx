import React, { useState } from 'react';
import { Sparkles, Clock, X, CheckCircle2 } from 'lucide-react';

export default function ConflictAlert({ conflicts = [], onResolve, tripId }) {
  const [dismissed, setDismissed] = useState(false);

  if (dismissed || !conflicts || conflicts.length === 0) return null;

  return (
    <div className="bg-gradient-to-r from-cyan-50/90 to-sky-50/90 border border-cyan-200/80 rounded-2xl p-4 mb-6 shadow-sm animate-in fade-in duration-300 relative transition-all">
      <div className="flex items-center justify-between mb-2">
        <div className="flex items-center space-x-2 text-cyan-950 font-bold text-sm font-heading">
          <Sparkles className="w-4 h-4 text-cyan-600 flex-shrink-0" />
          <span>Smart Schedule Assistant ({conflicts.length} time note{conflicts.length > 1 ? 's' : ''})</span>
        </div>
        <div className="flex items-center space-x-2">
          <button
            type="button"
            onClick={() => setDismissed(true)}
            className="flex items-center space-x-1 px-2.5 py-1 text-xs font-semibold text-slate-600 hover:text-slate-900 bg-white/80 hover:bg-white border border-slate-200 rounded-lg shadow-2xs transition"
            title="Dismiss suggestions"
          >
            <CheckCircle2 className="w-3.5 h-3.5 text-emerald-600" />
            <span>Mark All as OK</span>
          </button>
          <button
            type="button"
            onClick={() => setDismissed(true)}
            className="p-1 text-slate-400 hover:text-slate-700 rounded-lg hover:bg-white/80 transition"
            aria-label="Dismiss alert"
          >
            <X className="w-4 h-4" />
          </button>
        </div>
      </div>

      <div className="space-y-1.5">
        {conflicts.map((conflict, index) => (
          <div
            key={index}
            className="p-2.5 rounded-xl text-xs flex items-center justify-between border bg-white/70 border-cyan-100 text-slate-800"
          >
            <div className="flex items-center space-x-2">
              <Clock className="w-3.5 h-3.5 text-cyan-600 flex-shrink-0" />
              <span className="font-semibold text-slate-700">{conflict.message}</span>
            </div>
            <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-cyan-100/70 text-cyan-800 border border-cyan-200/50">
              Auto-Managed
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}

