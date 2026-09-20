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
            doc.setFontSize(8);
            doc.setTextColor(120);
            doc.text(`  Location: ${item.locationName}`, 28, y);
            y += 5;
            doc.setFontSize(10);
            doc.setTextColor(40);
          }
        });
      }
      y += 4;
    });

    doc.save(`${(trip.title || 'trip').replace(/\s+/g, '_')}_itinerary.pdf`);
  };

  const handleCopyLink = () => {
    const currentUrl = window.location.origin + `/trips/${id}`;
    navigator.clipboard.writeText(currentUrl);
    setCopied(true);
    setTimeout(() => setCopied(false), 2500);
  };

  const handlePrint = () => {
    window.print();
  };

  if (loading || !exportData) {
    return (
      <div className="min-h-screen bg-slate-950 flex justify-center items-center">
        <div className="w-10 h-10 border-4 border-amber-400 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  const trip = exportData.trip || exportData;
  const days = trip.days || exportData.days || [];

  return (
    <div className="min-h-screen bg-[#f7faf9] bg-mesh text-slate-900 py-10 pb-20">
      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Header */}
        <div>
          <Link to={`/trips/${id}`} className="btn-interactive text-xs font-bold text-cyan-700 hover:text-cyan-900 flex items-center space-x-1 mb-2">
            <ArrowLeft className="w-3.5 h-3.5" />
            <span>Back to Trip Command Center</span>
          </Link>
          <h1 className="text-3xl font-black text-blue-950 font-heading">
            Export & Share Trip Plan
          </h1>
        </div>

        {/* Action Cards Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          
          {/* iCal Download */}
          <div className="dashboard-box dashboard-box-cyan space-y-4">
            <div className="space-y-4">
              <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-cyan-500 to-sky-400 text-white flex items-center justify-center shadow-md shadow-cyan-500/25">
                <CalendarIcon className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-base font-black text-slate-900 font-heading">iCalendar (.ics)</h3>
                <p className="text-xs text-slate-600 font-medium mt-1 leading-relaxed">
                  Sync all flight slots and activities into Apple, Google, or Outlook Calendar.
                </p>
              </div>
            </div>
            <button
              onClick={handleDownloadICal}
              className="btn-interactive btn-action-cyan w-full py-3 text-white font-black rounded-xl text-xs shadow-md flex items-center justify-center space-x-2"
            >
              <Download className="w-4 h-4" />
              <span>Download .ics</span>
            </button>
          </div>

          {/* Direct PDF Download */}
          <div className="dashboard-box dashboard-box-amber space-y-4">
            <div className="space-y-4">
              <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-amber-500 to-orange-500 text-white flex items-center justify-center shadow-md shadow-amber-500/25">
                <FileText className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-base font-black text-slate-900 font-heading">Download PDF</h3>
                <p className="text-xs text-slate-600 font-medium mt-1 leading-relaxed">
                  Export formatted standalone PDF document for offline travel itineraries.
                </p>
              </div>
            </div>
            <button
              onClick={handleDownloadPDF}
              className="btn-interactive btn-action-amber w-full py-3 text-white font-black rounded-xl text-xs shadow-md flex items-center justify-center space-x-2"
            >
              <Download className="w-4 h-4" />
              <span>Download .pdf</span>
            </button>
          </div>

          {/* Share & QR Code */}
          <div className="dashboard-box dashboard-box-indigo space-y-4">
            <div className="space-y-4">
              <div className="w-12 h-12 rounded-2xl bg-gradient-to-tr from-indigo-500 to-purple-600 text-white flex items-center justify-center shadow-md shadow-indigo-500/25">
                <Share2 className="w-6 h-6" />
              </div>
              <div>
                <h3 className="text-base font-black text-slate-900 font-heading">QR Code & Link</h3>
                <p className="text-xs text-slate-600 font-medium mt-1 leading-relaxed">
                  Scan the QR code or copy shareable link for travel companions.
                </p>
              </div>
              {qrCodeUrl && (
                <div className="flex justify-center">
                  <img src={qrCodeUrl} alt="Trip QR Code" className="w-20 h-20 rounded-xl border border-slate-200 bg-white p-1 shadow-sm" />
                </div>
              )}
            </div>
            <button
              onClick={handleCopyLink}
              className="btn-interactive w-full py-3 bg-indigo-50 border border-indigo-200 hover:bg-indigo-500 hover:text-white hover:border-transparent text-indigo-800 font-black rounded-xl text-xs transition flex items-center justify-center space-x-2 shadow-sm"
            >
              {copied ? <Check className="w-4 h-4 text-emerald-600" /> : <Copy className="w-4 h-4 text-indigo-700" />}
              <span>{copied ? 'Link Copied!' : 'Copy Link'}</span>
            </button>
          </div>

        </div>

        {/* Formatted PDF Document Preview */}
        <div className="glass-panel p-8 rounded-3xl border border-slate-200 space-y-6 shadow-sm">
          <div className="flex justify-between items-center pb-4 border-b border-slate-100">
            <div>
              <span className="text-[10px] font-extrabold uppercase tracking-wider text-slate-500 block">Print Preview</span>
              <h2 className="text-2xl font-black text-blue-950 font-heading">{trip.title}</h2>
            </div>
            <button
              onClick={handlePrint}
              className="btn-interactive px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-800 rounded-xl text-xs font-bold transition flex items-center space-x-1.5 shadow-sm"
            >
              <Printer className="w-4 h-4 text-slate-700" />
              <span>Print Plan</span>
            </button>
          </div>

          <div className="space-y-6 text-xs text-slate-800">
            {days.map((day) => (
              <div key={day.id} className="p-4 bg-slate-50/80 rounded-2xl border border-slate-200/80 space-y-2">
                <h4 className="font-black text-slate-900 font-heading text-sm">
                  Day {day.dayNumber}: {day.title || day.date}
                </h4>
                {(day.items || []).length === 0 ? (
                  <p className="text-slate-500 italic">No scheduled activities recorded.</p>
                ) : (
                  <ul className="space-y-1.5 pl-2">
                    {(day.items || []).map((item) => (
                      <li key={item.id} className="flex items-center space-x-2">
                        <span className="w-1.5 h-1.5 rounded-full bg-cyan-600" />
                        <span className="font-bold text-slate-700">[{item.startTime || 'All Day'}]</span>
                        <span className="text-slate-900 font-semibold">{item.title}</span>
                        {item.estimatedCost > 0 && (
                          <span className="text-emerald-700 font-bold">(${item.estimatedCost})</span>
                        )}
                      </li>
                    ))}
                  </ul>
                )}
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
}
