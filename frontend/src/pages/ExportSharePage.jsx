import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { exportApi, tripApi } from '../api/client';
import { 
  Download, Calendar as CalendarIcon, Share2, 
  Printer, ArrowLeft, Check, Copy, Sparkles, QrCode as QrIcon, FileText 
} from 'lucide-react';
import QRCode from 'qrcode';
import { jsPDF } from 'jspdf';

export default function ExportSharePage() {
  const { id } = useParams();
  const [exportData, setExportData] = useState(null);
  const [qrCodeUrl, setQrCodeUrl] = useState('');
  const [copied, setCopied] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    exportApi.getExportData(id)
      .then((res) => {
        if (res.data.success) {
          setExportData(res.data.data);
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));

    // Generate QR Code for trip share URL
    const currentUrl = window.location.origin + `/trips/${id}`;
    QRCode.toDataURL(currentUrl)
      .then((url) => setQrCodeUrl(url))
      .catch(console.error);
  }, [id]);

  const handleDownloadICal = () => {
    exportApi.downloadICal(id)
      .then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', `trip_${id}_itinerary.ics`);
        document.body.appendChild(link);
        link.click();
        link.remove();
      })
      .catch(console.error);
  };

  const handleDownloadPDF = () => {
    const trip = exportData?.trip || exportData || {};
    const days = trip.days || exportData?.days || [];

    const doc = new jsPDF();
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(22);
    doc.text(trip.title || 'WanderLust Trip Itinerary', 20, 25);

    doc.setFontSize(11);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(80);
    doc.text(`Destination: ${trip.destinationName || trip.destination?.name || 'Global Destination'}`, 20, 34);
    doc.text(`Dates: ${trip.startDate || ''} to ${trip.endDate || ''}`, 20, 41);
    doc.text(`Total Travelers: ${trip.travelerCount || 1}`, 20, 48);

    doc.setLineWidth(0.5);
    doc.line(20, 52, 190, 52);

    let y = 62;
    days.forEach((day) => {
      if (y > 255) {
        doc.addPage();
        y = 20;
      }
      doc.setFont('helvetica', 'bold');
      doc.setFontSize(13);
      doc.setTextColor(0, 150, 200);
      doc.text(`Day ${day.dayNumber}: ${day.title || day.date || ''}`, 20, y);
      y += 8;

      const items = day.items || [];
      if (items.length === 0) {
        doc.setFont('helvetica', 'italic');
        doc.setFontSize(10);
        doc.setTextColor(130);
        doc.text('No scheduled activities', 25, y);
        y += 7;
      } else {
        items.forEach((item) => {
          if (y > 265) {
            doc.addPage();
            y = 20;
          }
          doc.setFont('helvetica', 'normal');
          doc.setFontSize(10);
          doc.setTextColor(40);
          const time = item.startTime || item.time || 'All Day';
          const cost = item.estimatedCost ? ` ($${item.estimatedCost})` : '';
          doc.text(`• [${time}] ${item.title}${cost}`, 25, y);
          y += 6;
          if (item.locationName) {
            doc.setTextColor(120);
            doc.text(`   Location: ${item.locationName}`, 25, y);
            y += 6;
          }
        });
      }
      y += 4;
    });

    doc.save(`trip_${id}_itinerary.pdf`);
  };

  const handleCopyLink = () => {
    navigator.clipboard.writeText(window.location.origin + `/trips/${id}`);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  if (loading || !exportData) {
    return (
      <div className="min-h-screen bg-slate-950 flex justify-center items-center">
        <div className="w-10 h-10 border-4 border-amber-400 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 py-10 bg-mesh pb-20">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Header */}
        <div>
          <Link to={`/trips/${id}`} className="text-xs font-bold text-cyan-400 hover:underline flex items-center space-x-1 mb-2">
            <ArrowLeft className="w-3.5 h-3.5" />
            <span>Back to Trip Command Center</span>
          </Link>
          <h1 className="text-3xl font-black text-slate-100 font-heading">
            Export & Share Trip Plan
          </h1>
        </div>

        {/* Action Cards Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          
          {/* iCal Download */}
          <div className="glass-panel p-6 rounded-3xl border border-slate-800 space-y-4 flex flex-col justify-between">
            <div className="space-y-4">
              <div className="w-12 h-12 rounded-2xl bg-cyan-500/10 border border-cyan-500/20 flex items-center justify-center text-cyan-400">
                <CalendarIcon className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-base font-bold text-slate-100 font-heading">iCalendar (.ics)</h3>
                <p className="text-xs text-slate-400 mt-1">
                  Sync all flight slots and activities into Apple, Google, or Outlook Calendar.
                </p>
              </div>
            </div>
            <button
              onClick={handleDownloadICal}
              className="w-full py-3 bg-gradient-to-r from-cyan-500 to-sky-500 text-slate-950 font-black rounded-xl text-xs shadow-lg shadow-cyan-500/20 hover:opacity-95 transition flex items-center justify-center space-x-2"
            >
              <Download className="w-4 h-4" />
              <span>Download .ics</span>
            </button>
          </div>

          {/* Direct PDF Download */}
          <div className="glass-panel p-6 rounded-3xl border border-slate-800 space-y-4 flex flex-col justify-between">
            <div className="space-y-4">
              <div className="w-12 h-12 rounded-2xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center text-amber-400">
                <FileText className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-base font-bold text-slate-100 font-heading">Download PDF</h3>
                <p className="text-xs text-slate-400 mt-1">
                  Export formatted standalone PDF document for offline travel itineraries.
                </p>
              </div>
            </div>
            <button
              onClick={handleDownloadPDF}
              className="w-full py-3 bg-gradient-to-r from-amber-400 to-amber-500 text-slate-950 font-black rounded-xl text-xs shadow-lg shadow-amber-500/20 hover:opacity-95 transition flex items-center justify-center space-x-2"
            >
              <Download className="w-4 h-4" />
              <span>Download .pdf</span>
            </button>
          </div>

          {/* Share & QR Code */}
          <div className="glass-panel p-6 rounded-3xl border border-slate-800 space-y-4 flex flex-col justify-between">
            <div className="space-y-4">
              <div className="w-12 h-12 rounded-2xl bg-purple-500/10 border border-purple-500/20 flex items-center justify-center text-purple-400">
                <Share2 className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-base font-bold text-slate-100 font-heading">QR Code & Link</h3>
                <p className="text-xs text-slate-400 mt-1">
                  Scan the QR code or copy shareable link for travel companions.
                </p>
              </div>
              {qrCodeUrl && (
                <div className="flex justify-center">
                  <img src={qrCodeUrl} alt="Trip QR Code" className="w-20 h-20 rounded-xl border border-slate-700 bg-white p-1" />
                </div>
              )}
            </div>
            <button
              onClick={handleCopyLink}
              className="w-full py-3 bg-slate-900 border border-slate-800 hover:bg-slate-800 text-slate-200 font-bold rounded-xl text-xs transition flex items-center justify-center space-x-2"
            >
              {copied ? <Check className="w-4 h-4 text-emerald-400" /> : <Copy className="w-4 h-4 text-cyan-400" />}
              <span>{copied ? 'Link Copied!' : 'Copy Link'}</span>
            </button>
          </div>

        </div>

        {/* Formatted PDF Document Preview */}
        <div className="glass-panel p-8 rounded-3xl border border-slate-800 space-y-6">
          <div className="flex justify-between items-center pb-4 border-b border-slate-800">
            <div>
              <span className="text-xs font-bold text-amber-400 uppercase tracking-widest">Document Preview</span>
              <h3 className="text-xl font-black text-slate-100 font-heading">Printable Itinerary Plan</h3>
            </div>
            <div className="flex items-center space-x-3">
              <button
                onClick={handleDownloadPDF}
                className="px-4 py-2 bg-amber-500/10 border border-amber-500/30 hover:bg-amber-500/20 text-amber-300 font-bold rounded-xl text-xs flex items-center space-x-2 transition"
              >
                <Download className="w-4 h-4" />
                <span>Save PDF</span>
              </button>
              <button
                onClick={() => window.print()}
                className="px-4 py-2 bg-slate-900 border border-slate-800 hover:bg-slate-800 text-slate-200 font-bold rounded-xl text-xs flex items-center space-x-2 transition"
              >
                <Printer className="w-4 h-4 text-cyan-400" />
                <span>Print</span>
              </button>
            </div>
          </div>

          {/* Render Itinerary Content */}
          {(() => {
            const trip = exportData.trip || exportData;
            const days = trip.days || exportData.days || [];
            return (
              <div className="bg-slate-950 p-6 rounded-2xl border border-slate-800 space-y-6 text-xs">
                <div className="text-center space-y-1 pb-4 border-b border-slate-900">
                  <h2 className="text-2xl font-black text-slate-100 font-heading">{trip.title || trip.tripTitle || 'Trip Itinerary'}</h2>
                  <p className="text-cyan-400 font-bold">{trip.destinationName || trip.destination?.name || 'Destination'}</p>
                  <p className="text-slate-400">{trip.startDate} — {trip.endDate}</p>
                </div>

                {days.map((day, idx) => (
                  <div key={idx} className="space-y-3">
                    <h4 className="text-sm font-bold text-slate-200 font-heading">
                      Day {day.dayNumber} ({day.date}) {day.title ? `- ${day.title}` : ''}
                    </h4>
                    <div className="space-y-2 pl-3 border-l-2 border-slate-800">
                      {(day.items || []).map((item, itemIdx) => (
                        <div key={itemIdx} className="p-3 bg-slate-900/60 rounded-xl border border-slate-800/80 flex justify-between">
                          <div>
                            <span className="font-bold text-cyan-400 mr-2">{item.startTime || item.time || 'All Day'}</span>
                            <span className="text-slate-200 font-bold">{item.title}</span>
                            {item.locationName && <span className="text-slate-500 text-[10px] ml-2">📍 {item.locationName}</span>}
                          </div>
                          <span className="text-emerald-400 font-bold">${item.estimatedCost ?? item.cost ?? 0}</span>
                        </div>
                      ))}
                      {(!day.items || day.items.length === 0) && (
                        <p className="text-slate-500 italic text-[11px]">No activities scheduled for this day.</p>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            );
          })()}
        </div>

      </div>
    </div>
  );
}
