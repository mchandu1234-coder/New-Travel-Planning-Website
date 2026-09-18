import React, { useState, useEffect, useRef } from 'react';
import { useParams, Link } from 'react-router-dom';
import { collaborationApi } from '../api/client';
import { useWebSocket } from '../context/WebSocketContext';
import { useAuth } from '../context/AuthContext';
import { 
  Users, MessageSquare, Send, UserPlus, ArrowLeft, 
  ShieldCheck, Sparkles, Circle, Trash2 
} from 'lucide-react';

export default function CollaborationRoomPage() {
  const { id } = useParams();
  const { user } = useAuth();
  const { connected, subscribeToTrip, sendChatMessage } = useWebSocket();

  const [collaborators, setCollaborators] = useState([]);
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState('');
  const [showInviteModal, setShowInviteModal] = useState(false);
  const [inviteEmail, setInviteEmail] = useState('');
  const [inviteRole, setInviteRole] = useState('EDITOR');
  const chatEndRef = useRef(null);

  useEffect(() => {
    fetchCollaborators();
    fetchChatHistory();

    const unsubscribe = subscribeToTrip(id, null, (newMessage) => {
      setMessages((prev) => [...prev, newMessage]);
      scrollToBottom();
    });

    return () => {
      if (unsubscribe) unsubscribe();
    };
  }, [id]);

  const fetchCollaborators = () => {
    collaborationApi.getCollaborators(id)
      .then((res) => { if (res.data.success) setCollaborators(res.data.data); })
      .catch(console.error);
  };

  const fetchChatHistory = () => {
    collaborationApi.getChatHistory(id)
      .then((res) => {
        if (res.data.success) {
          setMessages(res.data.data);
          scrollToBottom();
        }
      })
      .catch(console.error);
  };

  const scrollToBottom = () => {
    setTimeout(() => {
      chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, 100);
  };

  const handleSendMessage = (e) => {
    e.preventDefault();
    if (!inputMessage.trim()) return;

    sendChatMessage(id, inputMessage);
    setInputMessage('');
  };

  const handleInvite = async (e) => {
    e.preventDefault();
    if (!inviteEmail) return;

    try {
      const res = await collaborationApi.addCollaborator(id, {
        userEmail: inviteEmail,
        role: inviteRole,
      });

      if (res.data.success) {
        setShowInviteModal(false);
        setInviteEmail('');
        fetchCollaborators();
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 py-10 bg-mesh pb-20">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <Link to={`/trips/${id}`} className="text-xs font-bold text-cyan-400 hover:underline flex items-center space-x-1 mb-2">
              <ArrowLeft className="w-3.5 h-3.5" />
              <span>Back to Trip Command Center</span>
            </Link>
            <h1 className="text-3xl font-black text-slate-100 font-heading">
              Real-Time Collaboration Room
            </h1>
          </div>

          <div className="flex items-center space-x-3">
            <div className={`flex items-center space-x-1.5 px-3 py-1.5 rounded-full border text-xs font-bold ${
              connected ? 'bg-emerald-500/10 border-emerald-500/30 text-emerald-400' : 'bg-amber-500/10 border-amber-500/30 text-amber-400'
            }`}>
              <Circle className={`w-2.5 h-2.5 fill-current ${connected ? 'animate-pulse' : ''}`} />
              <span>{connected ? 'WebSocket Connected' : 'Connecting...'}</span>
            </div>

            <button
              onClick={() => setShowInviteModal(true)}
              className="px-5 py-2.5 bg-gradient-to-r from-cyan-500 to-indigo-600 hover:opacity-95 text-slate-950 font-black rounded-xl text-xs shadow-lg shadow-cyan-500/20 transition flex items-center space-x-1.5"
            >
              <UserPlus className="w-4 h-4" />
              <span>Invite Collaborator</span>
            </button>
          </div>
        </div>

        {/* Layout Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Chat Stream (2 Cols) */}
          <div className="lg:col-span-2 glass-panel rounded-3xl border border-slate-800 flex flex-col h-[520px]">
            <div className="p-4 border-b border-slate-800/80 flex items-center space-x-2 text-slate-200 font-bold text-sm">
              <MessageSquare className="w-4 h-4 text-cyan-400" />
              <span>Live Group Chat</span>
            </div>

            {/* Messages Scroll View */}
            <div className="flex-1 p-4 overflow-y-auto space-y-4">
              {messages.map((msg, index) => {
                const isMe = msg.sender?.email === user?.email;
                return (
                  <div key={index} className={`flex flex-col ${isMe ? 'items-end' : 'items-start'}`}>
                    <div className="flex items-center space-x-2 mb-1">
                      <span className="text-[10px] font-bold text-slate-400">{msg.sender?.fullName || 'Traveler'}</span>
                      <span className="text-[9px] text-slate-600">{msg.sentAt || 'Just now'}</span>
                    </div>
                    <div className={`p-3 rounded-2xl text-xs max-w-md ${
                      isMe ? 'bg-cyan-500 text-slate-950 font-medium rounded-tr-none' : 'bg-slate-900 border border-slate-800 text-slate-200 rounded-tl-none'
                    }`}>
                      {msg.content}
                    </div>
                  </div>
                );
              })}
              <div ref={chatEndRef} />
            </div>

            {/* Input Form */}
            <form onSubmit={handleSendMessage} className="p-3 border-t border-slate-800 flex items-center space-x-2">
              <input
                type="text"
                placeholder="Type a live message..."
                value={inputMessage}
                onChange={(e) => setInputMessage(e.target.value)}
                className="flex-1 glass-input text-xs"
              />
              <button
                type="submit"
                className="p-3 bg-cyan-500 text-slate-950 hover:bg-cyan-400 rounded-xl transition"
              >
                <Send className="w-4 h-4" />
              </button>
            </form>
          </div>

          {/* Member Roster (1 Col) */}
          <div className="glass-panel p-6 rounded-3xl border border-slate-800 space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="text-base font-bold text-slate-100 font-heading">Trip Collaborators</h3>
              <span className="text-xs font-black text-cyan-400">{collaborators.length}</span>
            </div>

            <div className="space-y-3">
              {collaborators.map((c) => (
                <div key={c.id} className="p-3 bg-slate-950/60 rounded-2xl border border-slate-800 flex items-center justify-between">
                  <div className="flex items-center space-x-3">
                    <img
                      src={c.user?.avatarUrl || `https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80`}
                      alt={c.user?.fullName}
                      className="w-8 h-8 rounded-xl object-cover border border-slate-700"
                    />
                    <div>
                      <h5 className="font-bold text-xs text-slate-100">{c.user?.fullName}</h5>
                      <span className="text-[10px] text-slate-400">{c.user?.email}</span>
                    </div>
                  </div>

                  <span className={`text-[10px] font-extrabold px-2 py-0.5 rounded border ${
                    c.role === 'ADMIN' ? 'bg-purple-500/10 border-purple-500/30 text-purple-400' :
                    c.role === 'EDITOR' ? 'bg-cyan-500/10 border-cyan-500/30 text-cyan-400' : 'bg-slate-800 text-slate-400'
                  }`}>
                    {c.role}
                  </span>
                </div>
              ))}
            </div>
          </div>

        </div>

      </div>

      {/* Invite Modal */}
      {showInviteModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-md">
          <div className="glass-panel w-full max-w-md rounded-3xl p-6 border border-slate-700/60 shadow-2xl relative">
            <h3 className="text-xl font-bold text-slate-100 font-heading mb-4">Invite Friend to Trip</h3>
            <form onSubmit={handleInvite} className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-slate-300 mb-1">User Email</label>
                <input
                  type="email"
                  required
                  placeholder="friend@wanderlust.com"
                  value={inviteEmail}
                  onChange={(e) => setInviteEmail(e.target.value)}
                  className="w-full glass-input text-xs"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-300 mb-1">Permission Role</label>
                <select
                  value={inviteRole}
                  onChange={(e) => setInviteRole(e.target.value)}
                  className="w-full glass-input text-xs cursor-pointer"
                >
                  <option value="EDITOR">Editor (Can add/edit items)</option>
                  <option value="VIEWER">Viewer (Read-only access)</option>
                  <option value="ADMIN">Admin (Full control)</option>
                </select>
              </div>

              <div className="flex space-x-2 pt-2">
                <button
                  type="button"
                  onClick={() => setShowInviteModal(false)}
                  className="flex-1 py-2.5 bg-slate-900 text-slate-300 font-bold rounded-xl text-xs"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="flex-1 py-2.5 bg-cyan-500 text-slate-950 font-black rounded-xl text-xs shadow-md"
                >
                  Send Invite
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

    </div>
  );
}
