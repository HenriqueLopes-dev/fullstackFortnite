import axios from "axios";

export const api = axios.create({
  baseURL: "https://fullstackfortnite.onrender.com",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;
