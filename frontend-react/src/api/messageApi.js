import axiosClient from "./axiosClient";

export const getMessages = (conversationId) =>
  axiosClient.get(`/messages/conversation/${conversationId}`);

export const sendMessage = (conversationId, content) =>
  axiosClient.post(`/messages/${conversationId}`, {
    content,
  });
