import React, { useState } from 'react';
import { Cloud, Sun, CloudRain, CloudSnow, Wind, Droplets, Compass } from 'lucide-react';

export default function WeatherWidget({ weatherData, cityName }) {
  const [unit, setUnit] = useState('C'); // 'C' or 'F'

  if (!weatherData) return null;

  const tempC = weatherData.currentTempC ?? weatherData.tempCelsius ?? 22.0;
  const tempF = weatherData.currentTempF ?? Math.round((tempC * 9) / 5 + 32);
  const humidity = weatherData.humidity ?? 60;
  const windSpeed = weatherData.windSpeedKmh ?? weatherData.windSpeedKmH ?? 12.0;
  const condition = weatherData.condition || 'Sunny & Clear';
  const forecastList = weatherData.forecast || [];

  const displayTemp = (celsius, fahrenheit) => {
    if (unit === 'C') {
      return `${Math.round(celsius)}°C`;
    }
    return `${Math.round(fahrenheit ?? ((celsius * 9) / 5 + 32))}°F`;
  };

  const getWeatherIcon = (condStr) => {
    const cond = (condStr || '').toLowerCase();
    if (cond.includes('sun') || cond.includes('clear')) return <Sun className="w-8 h-8 text-amber-400" />;
    if (cond.includes('rain') || cond.includes('shower')) return <CloudRain className="w-8 h-8 text-sky-400" />;
    if (cond.includes('snow')) return <CloudSnow className="w-8 h-8 text-indigo-300" />;
    return <Cloud className="w-8 h-8 text-slate-400" />;
  };

  return (
    <div className="glass-panel rounded-3xl p-5 border border-slate-700/60 shadow-xl relative overflow-hidden">
      {/* Background glow */}
      <div className="absolute -right-10 -bottom-10 w-32 h-32 bg-sky-500/10 rounded-full blur-2xl pointer-events-none" />

      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center space-x-2">
          <Compass className="w-4 h-4 text-cyan-400" />
          <h4 className="text-sm font-bold text-slate-200">Weather in {cityName || weatherData.cityName || 'Destination'}</h4>
        </div>

        {/* Unit Toggle */}
        <div className="flex bg-slate-950 p-1 rounded-xl border border-slate-800">
          <button
            onClick={() => setUnit('C')}
            className={`px-2 py-0.5 text-[10px] font-extrabold rounded-lg transition ${
              unit === 'C' ? 'bg-cyan-500 text-slate-950' : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            °C
          </button>
          <button
            onClick={() => setUnit('F')}
            className={`px-2 py-0.5 text-[10px] font-extrabold rounded-lg transition ${
              unit === 'F' ? 'bg-cyan-500 text-slate-950' : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            °F
          </button>
        </div>
      </div>

      {/* Current Temp Main Card */}
      <div className="flex items-center justify-between bg-slate-950/60 p-4 rounded-2xl border border-slate-800/80 mb-4">
        <div className="flex items-center space-x-4">
          <div className="p-3 bg-slate-900 rounded-2xl border border-slate-800">
            {getWeatherIcon(condition)}
          </div>
          <div>
            <div className="text-3xl font-black text-slate-100 font-heading">
              {displayTemp(tempC, tempF)}
            </div>
            <p className="text-xs font-semibold text-slate-400 capitalize">{condition}</p>
          </div>
        </div>

        <div className="text-right space-y-1 text-xs text-slate-400">
          <div className="flex items-center space-x-1 justify-end">
            <Droplets className="w-3.5 h-3.5 text-cyan-400" />
            <span>{humidity}% Humidity</span>
          </div>
          <div className="flex items-center space-x-1 justify-end">
            <Wind className="w-3.5 h-3.5 text-indigo-400" />
            <span>{windSpeed} km/h</span>
          </div>
        </div>
      </div>

      {/* 5-Day Forecast Grid */}
      {forecastList.length > 0 && (
        <div>
          <p className="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-2">5-Day Forecast</p>
          <div className="grid grid-cols-5 gap-2">
            {forecastList.map((day, idx) => {
              const dayName = day.dayOfWeek || day.day || `Day ${idx + 1}`;
              const high = day.maxTempC ?? day.high ?? 24;
              const low = day.minTempC ?? day.low ?? 15;
              return (
                <div key={idx} className="bg-slate-900/40 p-2 rounded-xl border border-slate-800/60 text-center">
                  <p className="text-[10px] font-bold text-slate-400 truncate">{dayName.slice(0, 3)}</p>
                  <div className="my-1 flex justify-center">
                    {getWeatherIcon(day.condition)}
                  </div>
                  <p className="text-xs font-extrabold text-slate-200">{displayTemp(high)}</p>
                  <p className="text-[10px] font-semibold text-slate-500">{displayTemp(low)}</p>
                </div>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
}
