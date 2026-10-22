import axios from "axios";
const api = axios.create({ baseURL: "http://localhost:9998/api" });
export default api;
