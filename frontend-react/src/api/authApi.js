import axiosClient from "./axiosClient";

export const login = (data) =>
  axiosClient.post("/auth/login", data);
