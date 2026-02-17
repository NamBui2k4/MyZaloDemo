import axiosClient from "./axiosClient";

export const login = (data) =>
  axiosClient.post("/auth/login", data);

export const signin = (data)=>
  axiosClient.post("/auth/register", data)
