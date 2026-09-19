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
    if (cond.includes('sun') || cond.includes('clear')) return <Sun className="w-7 h-7 text-amber-500" />;
    if (cond.includes('rain') || cond.includes('shower')) return <CloudRain className="w-7 h-7 text-sky-500" />;
    if (cond.includes('snow')) return <CloudSnow className="w-7 h-7 text-indigo-400" />;
    return <Cloud className="w-7 h-7 text-slate-400" />;
  };

  return (
    <div className="bg-white rounded-3xl p-5 border border-slate-200/80 shadow-sm relative overflow-hidden">
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center space-x-2">
          <Compass className="w-4 h-4 text-teal-600" />
          <h4 className="text-xs font-bold text-slate-800">Weather in {cityName || weatherData.cityName || 'Destination'}</h4>
        </div>

        {/* Unit Toggle */}
        <div className="flex bg-slate-100 p-1 rounded-xl">
          <button
            onClick={() => setUnit('C')}
            className={`px-2 py-0.5 text-[10px] font-extrabold rounded-lg transition ${
              unit === 'C' ? 'bg-white text-teal-800 shadow-sm' : 'text-slate-700 hover:text-slate-950'
            }`}
          >
            °C
          </button>
          <button
            onClick={() => setUnit('F')}
            className={`px-2 py-0.5 text-[10px] font-extrabold rounded-lg transition ${
              unit === 'F' ? 'bg-white text-teal-800 shadow-sm' : 'text-slate-700 hover:text-slate-950'
            }`}
          >
            °F
          </button>
        </div>
      </div>

      {/* Current Temp Main Card */}
      <div className="flex items-center justify-between bg-slate-50 p-4 rounded-2xl border border-slate-200 mb-4">
        <div className="flex items-center space-x-3">
          <div className="p-2.5 bg-white rounded-2xl border border-slate-200 shadow-sm">
            {getWeatherIcon(condition)}
          </div>
          <div>
            <div className="text-2xl font-black text-slate-950 font-heading">
              {displayTemp(tempC, tempF)}
            </div>
            <p className="text-[11px] font-bold text-slate-700 capitalize">{condition}</p>
          </div>
        </div>

        <div className="text-right space-y-1 text-xs text-slate-700 font-semibold">
          <div className="flex items-center space-x-1 justify-end">
            <Droplets className="w-3.5 h-3.5 text-teal-700" />
            <span>{humidity}% Humidity</span>
          </div>
          <div className="flex items-center space-x-1 justify-end">
            <Wind className="w-3.5 h-3.5 text-teal-700" />
            <span>{windSpeed} km/h</span>
          </div>
        </div>
      </div>

      {/* 5-Day Forecast Grid */}
      {forecastList.length > 0 && (
        <div>
          <p className="text-[10px] font-extrabold text-slate-800 uppercase tracking-wider mb-2">5-Day Forecast</p>
          <div className="grid grid-cols-5 gap-1.5">
            {forecastList.map((day, idx) => {
              const dayName = day.dayOfWeek || day.day || `Day ${idx + 1}`;
              const high = day.maxTempC ?? day.high ?? 24;
              const low = day.minTempC ?? day.low ?? 15;
              return (
                <div key={idx} className="bg-slate-50 p-2 rounded-xl border border-slate-200 text-center">
                  <p className="text-[10px] font-bold text-slate-800 truncate">{dayName.slice(0, 3)}</p>
                  <div className="my-1 flex justify-center">
                    {getWeatherIcon(day.condition)}
                  </div>
                  <p className="text-xs font-black text-slate-950">{displayTemp(high)}</p>
                  <p className="text-[10px] font-bold text-slate-600">{displayTemp(low)}</p>
                </div>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
}

