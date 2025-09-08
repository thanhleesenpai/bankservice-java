import axios from 'axios';

const API_BASE = '/bankservice/api/auth';

export const register = async (data) => {
  return axios.post(`${API_BASE}/register`, data);
};

export const login = async (data) => {
  return axios.post(`${API_BASE}/login`, data);
};
