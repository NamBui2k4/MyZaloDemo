import axiosClient from "./axiosClient";

export const openPrivateConversation = (currentUserId, body) =>
    axiosClient.post(`/conversations/private?currentUserId=${currentUserId}`,body)

export const getAllConversation = (currentUserId) =>
    axiosClient.get(`/conversations?currentUserId=${currentUserId}`)