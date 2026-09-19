import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { budgetApi, tripApi } from '../api/client';
import { 
  PieChart as PieIcon, DollarSign, Plus, ArrowLeft, 
  Trash2, RefreshCw, CheckCircle2, Users, ArrowRight 
} from 'lucide-react';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip, Legend } from 'recharts';

const CATEGORY_COLORS = {
  FLIGHTS: '#38bdf8',
  STAYS: '#818cf8',
  FOOD_DRINK: '#f59e0b',
  ACTIVITIES: '#c084fc',
  TRANSPORT: '#34d399',
  SHOPPING: '#f43f5e',
  MISC: '#94a3b8',
  FLIGHT: '#38bdf8',
  HOTEL: '#818cf8',
  FOOD: '#f59e0b',
  OTHER: '#94a3b8',
};

export default function BudgetTrackerPage() {
  const { id } = useParams();
  const [summary, setSummary] = useState(null);
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [targetCurrency, setTargetCurrency] = useState('USD');
  const [successMsg, setSuccessMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [submitting, setSubmitting] = useState(false);

  // Interactive Filter and Group Split Simulator state
  const [selectedCategory, setSelectedCategory] = useState('ALL');
  const [splitTravelers, setSplitTravelers] = useState(3);

  // Form state for logging expense
  const [title, setTitle] = useState('');
  const [category, setCategory] = useState('FOOD_DRINK');
  const [amount, setAmount] = useState('');
  const [expenseCurrency, setExpenseCurrency] = useState('USD');
  const [paidBy, setPaidBy] = useState('Me');
  const [expenseDate, setExpenseDate] = useState(new Date().toISOString().split('T')[0]);

  useEffect(() => {
    fetchBudgetSummary();
  }, [id, targetCurrency]);

  const fetchBudgetSummary = () => {
    setLoading(true);
    Promise.all([
      budgetApi.getSummary(id, targetCurrency),
      budgetApi.getExpenses(id)
    ])
      .then(([summaryRes, expRes]) => {
        if (summaryRes.data.success) {
          setSummary(summaryRes.data.data);
        }
        if (expRes.data.success) {
          setExpenses(expRes.data.data);
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  const handleAddExpense = async (e) => {
    e.preventDefault();
    if (!title || !amount) return;
    setErrorMsg('');
    setSuccessMsg('');
    setSubmitting(true);

    try {
      const res = await budgetApi.addExpense({
        tripId: Number(id),
        title,
        category,
        amount: Number(amount),
        currency: expenseCurrency,
        date: expenseDate || new Date().toISOString().split('T')[0],
      });

      if (res.data.success) {
        setSuccessMsg(`Logged "${title}" ($${amount}) successfully!`);
        setTitle('');
        setAmount('');
        fetchBudgetSummary();
        setTimeout(() => setSuccessMsg(''), 4000);
      }
    } catch (err) {
      console.error(err);
      setErrorMsg(err.response?.data?.message || err.message || 'Failed to log expense');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteExpense = async (expenseId) => {
    try {
      const res = await budgetApi.deleteExpense(expenseId);
      if (res.data.success) {
        fetchBudgetSummary();
      }
    } catch (err) {
      console.error(err);
    }
  };

  if (loading || !summary) {
    return (
      <div className="min-h-screen bg-slate-950 flex justify-center items-center">
        <div className="w-10 h-10 border-4 border-emerald-400 border-t-transparent rounded-full animate-spin" />
      </div>
    );
  }

  const pieChartData = summary.categoryBreakdown
    ? Object.entries(summary.categoryBreakdown)
        .filter(([_, amt]) => amt > 0)
        .map(([cat, amt]) => ({
          name: cat,
          value: amt,
        }))
    : [];

  const allocatedBudget = summary.targetBudget ?? summary.totalBudget ?? 2000;
  const percentageSpent = Math.min(100, Math.round((summary.totalSpent / (allocatedBudget || 1)) * 100));

  return (
    <div className="min-h-screen bg-[#f7faf9] bg-mesh text-slate-900 py-10 pb-20">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <Link to={`/trips/${id}`} className="text-xs font-bold text-cyan-700 hover:underline flex items-center space-x-1 mb-2">
              <ArrowLeft className="w-3.5 h-3.5" />
              <span>Back to Trip Command Center</span>
            </Link>
            <h1 className="text-3xl font-black text-blue-950 font-heading">
              Budget Tracker & Split-Expense Calculator
            </h1>
          </div>

          <div className="flex items-center space-x-2 bg-slate-100 p-1.5 rounded-2xl border border-slate-200">
            <span className="text-xs text-slate-700 font-bold px-2">Display Currency:</span>
            {['USD', 'EUR', 'GBP', 'INR', 'JPY'].map((c) => (
              <button
                key={c}
                onClick={() => setTargetCurrency(c)}
                className={`px-3 py-1 rounded-xl text-xs font-extrabold transition ${
                  targetCurrency === c ? 'bg-emerald-600 text-white shadow-sm' : 'text-slate-700 hover:text-slate-950'
                }`}
              >
                {c}
              </button>
            ))}
          </div>
        </div>

        {/* Total Budget vs Spent Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          
          <div className="glass-panel p-6 rounded-3xl border border-slate-200 space-y-2 shadow-sm">
            <span className="text-xs font-extrabold text-slate-600 uppercase">Target Budget</span>
            <div className="text-3xl font-black text-slate-900 font-heading">
              {summary.currency} ${typeof allocatedBudget === 'number' ? allocatedBudget.toFixed(2) : allocatedBudget}
            </div>
            <p className="text-[11px] text-slate-600 font-medium">Allocated trip limit</p>
          </div>

          <div className="glass-panel p-6 rounded-3xl border border-slate-200 space-y-2 shadow-sm">
            <span className="text-xs font-extrabold text-slate-600 uppercase">Total Spent</span>
            <div className="text-3xl font-black text-emerald-800 font-heading">
              {summary.currency} ${summary.totalSpent?.toFixed(2)}
            </div>
            <div className="w-full bg-slate-100 h-2 rounded-full overflow-hidden mt-2 border border-slate-200">
              <div
                className="bg-gradient-to-r from-emerald-500 to-cyan-600 h-full rounded-full transition-all duration-500"
                style={{ width: `${percentageSpent}%` }}
              />
            </div>
          </div>

          <div className="glass-panel p-6 rounded-3xl border border-slate-200 space-y-2 shadow-sm">
            <span className="text-xs font-extrabold text-slate-600 uppercase">Remaining Balance</span>
            <div className={`text-3xl font-black font-heading ${summary.remainingBudget >= 0 ? 'text-cyan-800' : 'text-rose-800'}`}>
              {summary.currency} ${summary.remainingBudget?.toFixed(2)}
            </div>
            <p className="text-[11px] text-slate-600 font-medium">{percentageSpent}% of total budget used</p>
          </div>

        </div>

        {/* Section: Who Owes Whom (Simplified Debt Engine) */}
        {summary.simplifiedDebts && summary.simplifiedDebts.length > 0 && (
          <div className="p-6 rounded-3xl border border-indigo-200 bg-indigo-50/70 space-y-4 shadow-sm">
            <div className="flex items-center space-x-2 text-indigo-950 font-bold font-heading text-lg">
              <Users className="w-5 h-5 text-indigo-700" />
              <h3>Who Owes Whom — Simplified Debt Settlements</h3>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {summary.simplifiedDebts.map((debt, index) => (
                <div key={index} className="p-4 bg-white rounded-2xl border border-slate-200 flex items-center justify-between shadow-sm">
                  <div className="flex items-center space-x-3 text-xs">
                    <span className="font-bold text-rose-700">{debt.fromUser}</span>
                    <ArrowRight className="w-4 h-4 text-slate-400" />
                    <span className="font-bold text-emerald-700">{debt.toUser}</span>
                  </div>
                  <div className="text-right">
                    <span className="text-base font-black text-cyan-800">{debt.currency} ${debt.amount?.toFixed(2)}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Chart & Expense Logger Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          
          {/* Pie Chart */}
          <div className="glass-panel p-6 rounded-3xl border border-slate-200 space-y-4 shadow-sm">
            <h3 className="text-lg font-bold text-blue-950 font-heading">Expense Breakdown by Category</h3>
            <div className="h-64">
              {pieChartData.length > 0 ? (
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={pieChartData}
                      cx="50%"
                      cy="50%"
                      innerRadius={60}
                      outerRadius={80}
                      paddingAngle={5}
                      dataKey="value"
                    >
                      {pieChartData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={CATEGORY_COLORS[entry.name] || '#94a3b8'} />
                      ))}
                    </Pie>
                    <Tooltip
                      contentStyle={{ backgroundColor: '#ffffff', borderColor: '#e2e8f0', borderRadius: '12px', color: '#0f172a' }}
                      itemStyle={{ color: '#0f172a', fontWeight: 'bold' }}
                    />
                    <Legend />
                  </PieChart>
                </ResponsiveContainer>
              ) : (
                <div className="h-full flex items-center justify-center text-xs text-slate-600 font-medium">
                  No expenses logged yet.
                </div>
              )}
            </div>
          </div>

          {/* Add Expense Form */}
          <div className="glass-panel p-6 rounded-3xl border border-slate-200 space-y-4 shadow-sm">
            <h3 className="text-lg font-bold text-blue-950 font-heading">Log New Expense</h3>

            {successMsg && (
              <div className="p-3 bg-emerald-50 border border-emerald-200 rounded-xl text-emerald-800 text-xs font-bold flex items-center space-x-2 animate-in fade-in duration-200">
                <CheckCircle2 className="w-4 h-4 text-emerald-600" />
                <span>{successMsg}</span>
              </div>
            )}

            {errorMsg && (
              <div className="p-3 bg-rose-50 border border-rose-200 rounded-xl text-rose-800 text-xs font-bold animate-in fade-in duration-200">
                <span>{errorMsg}</span>
              </div>
            )}

            <form onSubmit={handleAddExpense} className="space-y-4">
              <div>
                <label className="block text-xs font-extrabold text-slate-800 mb-1">Expense Title</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Group Dinner at Trattoria"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  className="w-full glass-input text-xs text-slate-900"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-1">Category</label>
                  <select
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    className="w-full glass-input text-xs cursor-pointer text-slate-900"
                  >
                    <option value="FOOD_DRINK">Food & Dining</option>
                    <option value="FLIGHTS">Flight</option>
                    <option value="STAYS">Hotel Stay</option>
                    <option value="ACTIVITIES">Activities</option>
                    <option value="TRANSPORT">Transport</option>
                    <option value="SHOPPING">Shopping</option>
                    <option value="MISC">Other / Misc</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-1">Amount</label>
                  <input
                    type="number"
                    step="0.01"
                    required
                    placeholder="120.00"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    className="w-full glass-input text-xs text-slate-900"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-1">Currency</label>
                  <select
                    value={expenseCurrency}
                    onChange={(e) => setExpenseCurrency(e.target.value)}
                    className="w-full glass-input text-xs cursor-pointer text-slate-900"
                  >
                    <option value="USD">USD</option>
                    <option value="EUR">EUR</option>
                    <option value="GBP">GBP</option>
                    <option value="INR">INR</option>
                    <option value="JPY">JPY</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-extrabold text-slate-800 mb-1">Expense Date</label>
                  <input
                    type="date"
                    value={expenseDate}
                    onChange={(e) => setExpenseDate(e.target.value)}
                    className="w-full glass-input text-xs text-slate-900"
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={submitting}
                className="w-full py-3 bg-gradient-to-r from-emerald-500 to-teal-600 text-white font-black rounded-xl text-xs shadow-lg shadow-emerald-500/20 transition hover:opacity-95 disabled:opacity-50"
              >
                {submitting ? 'Recording Expense...' : 'Log Expense'}
              </button>
            </form>
          </div>

        </div>

        {/* Interactive Group Split Simulator Card */}
        {summary && (
          <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-4">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2 border-b border-slate-100 pb-3">
              <div className="flex items-center space-x-2">
                <div className="p-2 bg-teal-50 text-teal-800 rounded-xl">
                  <Users className="w-4 h-4" />
                </div>
                <div>
                  <h4 className="text-sm font-black text-blue-950 font-heading">Interactive Group Split Simulator</h4>
                  <p className="text-[11px] text-slate-600 font-medium">Real-time equal share calculation across travelers</p>
                </div>
              </div>

              <div className="text-right">
                <span className="text-[10px] uppercase font-extrabold text-slate-600 block">Each Person Owes</span>
                <span className="text-xl font-black text-teal-700 font-heading">
                  ${((summary.totalSpent || 0) / splitTravelers).toFixed(2)}
                </span>
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 items-center pt-1">
              <div className="md:col-span-2 space-y-1.5 bg-slate-50 p-3.5 rounded-2xl border border-slate-200">
                <div className="flex justify-between text-xs font-bold">
                  <span className="text-slate-700">Split Among Travelers:</span>
                  <span className="text-teal-800 font-extrabold">{splitTravelers} People</span>
                </div>
                <input
                  type="range"
                  min="2"
                  max="8"
                  value={splitTravelers}
                  onChange={(e) => setSplitTravelers(Number(e.target.value))}
                  className="w-full accent-teal-600 cursor-pointer h-2"
                />
                <div className="flex justify-between text-[10px] text-slate-600 font-semibold pt-0.5">
                  <span>2 Duo</span>
                  <span>4 Small Group</span>
                  <span>6 Family</span>
                  <span>8 Large Crew</span>
                </div>
              </div>

              <div className="p-3.5 bg-teal-50/80 rounded-2xl border border-teal-200 text-center">
                <span className="text-[10px] uppercase font-bold text-teal-900 block">Total Group Pot</span>
                <span className="text-lg font-black text-teal-950 font-heading">
                  ${(summary.totalSpent || 0).toFixed(2)}
                </span>
                <p className="text-[10px] text-teal-800 mt-0.5 font-medium">
                  {summary.totalBudget ? `Remaining: $${Math.max(0, (summary.totalBudget - summary.totalSpent) / splitTravelers).toFixed(2)} / person` : 'Active trip'}
                </p>
              </div>
            </div>
          </div>
        )}

        {/* Expenses List with Interactive Category Filter */}
        <div className="glass-panel p-6 rounded-3xl border border-slate-200 space-y-4 shadow-sm">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
            <h3 className="text-lg font-bold text-blue-950 font-heading">
              Logged Expenses ({expenses.length})
            </h3>

            {/* Interactive Category Filter Pills */}
            <div className="flex flex-wrap gap-1.5">
              {[
                { id: 'ALL', label: 'All' },
                { id: 'FOOD_DRINK', label: 'Food & Dining' },
                { id: 'FLIGHTS', label: 'Flights' },
                { id: 'STAYS', label: 'Stays' },
                { id: 'ACTIVITIES', label: 'Activities' },
                { id: 'TRANSPORT', label: 'Transit' },
              ].map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setSelectedCategory(tab.id)}
                  className={`px-3 py-1 rounded-xl text-xs font-bold transition ${
                    selectedCategory === tab.id
                      ? 'bg-slate-900 text-white shadow-sm'
                      : 'bg-slate-100 text-slate-700 hover:text-slate-950 hover:bg-slate-200'
                  }`}
                >
                  {tab.label}
                </button>
              ))}
            </div>
          </div>

          {/* Filtered Expenses Output */}
          {(() => {
            const filtered = selectedCategory === 'ALL'
              ? expenses
              : expenses.filter(exp => {
                  const cat = (exp.category || '').toUpperCase();
                  if (selectedCategory === 'FOOD_DRINK') return cat.includes('FOOD') || cat.includes('DRINK') || cat.includes('RESTAURANT');
                  if (selectedCategory === 'FLIGHTS') return cat.includes('FLIGHT');
                  if (selectedCategory === 'STAYS') return cat.includes('STAY') || cat.includes('HOTEL') || cat.includes('ACCOMMODATION');
                  if (selectedCategory === 'ACTIVITIES') return cat.includes('ACTIVITY') || cat.includes('SIGHT');
                  if (selectedCategory === 'TRANSPORT') return cat.includes('TRANS') || cat.includes('TAXI');
                  return cat === selectedCategory;
                });

            return filtered.length > 0 ? (
              <div className="space-y-3">
                {filtered.map((exp) => (
                  <div key={exp.id} className="p-4 bg-white rounded-2xl border border-slate-200 flex items-center justify-between shadow-sm hover:border-teal-500/40 transition">
                    <div className="flex items-center space-x-3">
                      <div className="p-2 rounded-xl bg-cyan-50 border border-cyan-200 text-cyan-800 text-xs font-bold uppercase">
                        {exp.category}
                      </div>
                      <div>
                        <h5 className="font-bold text-xs text-slate-900">{exp.title}</h5>
                        <span className="text-[10px] text-slate-600 font-medium">{exp.date} • Paid by {exp.paidBy?.fullName || exp.paidByUser?.fullName || 'Member'}</span>
                      </div>
                    </div>

                    <div className="flex items-center space-x-4">
                      <span className="text-sm font-black text-emerald-800">
                        {exp.currency || 'USD'} ${typeof exp.amount === 'number' ? exp.amount.toFixed(2) : exp.amount}
                      </span>
                      <button
                        onClick={() => handleDeleteExpense(exp.id)}
                        className="text-slate-400 hover:text-rose-600 transition p-1.5"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-xs text-slate-600 font-medium py-4 text-center">
                No expenses found in category "{selectedCategory}".
              </p>
            );
          })()}
        </div>

      </div>
    </div>
  );
}
