import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Attach JWT token to requests if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => Promise.reject(error));

// Auth APIs
export const authApi = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  getCurrentUser: () => api.get('/auth/me'),
  updateProfile: (data) => api.put('/auth/profile', data),
};

// Destination APIs
export const destinationApi = {
  getAll: (params) => api.get('/destinations', { params }),
  getFeatured: () => api.get('/destinations/featured'),
  getById: (id) => api.get(`/destinations/${id}`),
};

// Trip APIs
export const tripApi = {
  create: (data) => api.post('/trips', data),
  getMyTrips: () => api.get('/trips'),
  getById: (id) => api.get(`/trips/${id}`),
  getByShareCode: (code) => api.get(`/trips/shared/${code}`),
  update: (id, data) => api.put(`/trips/${id}`, data),
  delete: (id) => api.delete(`/trips/${id}`),
  checkConflicts: (id) => api.get(`/itinerary/trips/${id}`),
};

// Itinerary APIs
export const itineraryApi = {
  getDays: (tripId) => api.get(`/itinerary/trips/${tripId}`),
  addItem: (arg1, arg2) => {
    const payload = arg2 !== undefined ? arg2 : arg1;
    return api.post('/itinerary/items', payload);
  },
  updateItem: (arg1, arg2, arg3) => {
    const itemId = arg3 !== undefined ? arg2 : (arg2 !== undefined ? arg1 : arg1);
    const payload = arg3 !== undefined ? arg3 : (arg2 !== undefined ? arg2 : arg1);
    return api.put(`/itinerary/items/${itemId}`, payload);
  },
  deleteItem: (arg1, arg2) => {
    const itemId = arg2 !== undefined ? arg2 : arg1;
    return api.delete(`/itinerary/items/${itemId}`);
  },
  reorderItem: (data) => api.post('/itinerary/items/reorder', data),
};

// Booking APIs
export const bookingApi = {
  searchFlights: (data) => api.post('/bookings/flights/search', data),
  bookFlight: (data) => api.post('/bookings/flights/book', data),
  searchHotels: (data) => api.post('/bookings/hotels/search', data),
  bookHotel: (data) => api.post('/bookings/hotels/book', data),
};

// Catalog APIs (Activities & Restaurants)
export const catalogApi = {
  getActivities: (destinationId, category) => 
    api.get(`/catalog/activities?destinationId=${destinationId}${category ? `&category=${category}` : ''}`),
  getRestaurants: (destinationId, cuisine) => 
    api.get(`/catalog/restaurants?destinationId=${destinationId}${cuisine ? `&cuisine=${cuisine}` : ''}`),
};

// Budget & Expenses APIs
export const budgetApi = {
  getSummary: (tripId) => api.get(`/budget/trips/${tripId}/summary`),
  getExpenses: (tripId) => api.get(`/budget/trips/${tripId}/expenses`),
  addExpense: (data) => api.post('/budget/expenses', data),
  deleteExpense: (expenseId) => api.delete(`/budget/expenses/${expenseId}`),
  settleDebt: (tripId, fromUserId, toUserId) => 
    api.post(`/budget/trips/${tripId}/settle?fromUserId=${fromUserId}&toUserId=${toUserId}`),
  getRates: (base = 'USD') => api.get(`/budget/rates?base=${base}`),
};

// Collaboration APIs
export const collaborationApi = {
  getCollaborators: (tripId) => api.get(`/collaboration/trips/${tripId}/collaborators`),
  invite: (tripId, data) => api.post(`/collaboration/trips/${tripId}/invite`, data),
  updateRole: (tripId, collaboratorId, role) => 
    api.put(`/collaboration/trips/${tripId}/collaborators/${collaboratorId}/role`, { role }),
  removeCollaborator: (tripId, collaboratorId) => 
    api.delete(`/collaboration/trips/${tripId}/collaborators/${collaboratorId}`),
};

// Export APIs
export const exportApi = {
  getExportData: (tripId) => api.get(`/export/trips/${tripId}/data`),
  downloadICal: (tripId) => api.get(`/export/trips/${tripId}/calendar.ics`, { responseType: 'blob' }),
};

export default api;
