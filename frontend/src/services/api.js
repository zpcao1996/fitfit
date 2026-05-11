import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authApi = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
};

export const projectApi = {
  list: (params) => api.get('/projects', { params }),
  get: (id) => api.get(`/projects/${id}`),
  create: (data) => api.post('/projects', data),
  update: (id, data) => api.put(`/projects/${id}`, data),
  delete: (id) => api.delete(`/projects/${id}`),
};

export const researcherApi = {
  list: (params) => api.get('/researchers', { params }),
  get: (id) => api.get(`/researchers/${id}`),
  create: (data) => api.post('/researchers', data),
  update: (id, data) => api.put(`/researchers/${id}`, data),
  delete: (id) => api.delete(`/researchers/${id}`),
};

export const fundingApi = {
  list: (params) => api.get('/funding', { params }),
  create: (data) => api.post('/funding', data),
  delete: (id) => api.delete(`/funding/${id}`),
};

export const publicationApi = {
  list: (params) => api.get('/publications', { params }),
  get: (id) => api.get(`/publications/${id}`),
  create: (data) => api.post('/publications', data),
  update: (id, data) => api.put(`/publications/${id}`, data),
  delete: (id) => api.delete(`/publications/${id}`),
  stats: () => api.get('/publications/stats'),
};

export const milestoneApi = {
  list: (params) => api.get('/milestones', { params }),
  get: (id) => api.get(`/milestones/${id}`),
  create: (data) => api.post('/milestones', data),
  update: (id, data) => api.put(`/milestones/${id}`, data),
  delete: (id) => api.delete(`/milestones/${id}`),
};

export const announcementApi = {
  list: (params) => api.get('/announcements', { params }),
  get: (id) => api.get(`/announcements/${id}`),
  create: (data) => api.post('/announcements', data),
  update: (id, data) => api.put(`/announcements/${id}`, data),
  delete: (id) => api.delete(`/announcements/${id}`),
};

export const expenditureApi = {
  list: (params) => api.get('/expenditures', { params }),
  get: (id) => api.get(`/expenditures/${id}`),
  create: (data) => api.post('/expenditures', data),
  update: (id, data) => api.put(`/expenditures/${id}`, data),
  delete: (id) => api.delete(`/expenditures/${id}`),
  stats: () => api.get('/expenditures/stats'),
};

export const dashboardApi = {
  getStats: () => api.get('/dashboard'),
};

export default api;
