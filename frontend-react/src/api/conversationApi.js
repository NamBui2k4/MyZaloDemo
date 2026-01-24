import axiosClient from "./axiosClient";

export const openPrivateConversation = (targetUserId) =>
  axiosClient.post("/conversations/private", {
    targetUserId,
  });
