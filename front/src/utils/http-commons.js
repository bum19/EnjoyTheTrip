import axios from "axios";
// axios.defaults.baseURL = "http://localhost";
// axios.defaults.withCredentials = true;

const localAxios = axios.create({
  baseURL: "http://localhost",
  headers: {
    "Content-Type": "application/json;charset=utf-8",
  },
  withCredentials: true,
});

// const fileAxios = axios.create({})

export { localAxios };
