import { useState, useEffect } from "react";
import Sidebar from "../components/Sidebar";
import ChatLayout from "../components/ChatLayout";
import { openPrivateConversation, getAllConversation } from "../api/conversationApi";
import { getMessages, sendMessage } from "../api/messageApi";

export default function ChatPage() {
  const currentUserId = 1; // tạm thời
  const [conversation, setConversation] = useState(null);
  const [messages, setMessages] = useState([]);
  const [conversationList, setConversationList] = useState([]);

  

  useEffect(()=>{
    const fetchAllData = async () =>{
      try {
        const res = await getAllConversation();
        setConversationList(res.data)
        // setConversationList(res.data)
      } catch (error) {
        console.log("Lỗi lấy danh sách hội thoại", error);
      }
    };
    fetchAllData();
  }, [currentUserId])

  const selectContact = async (contact) => {
    try {
      const res = await openPrivateConversation(contact.id);
      setConversation(res.data);
      setMessages(res.data.listMessage);  
    } catch (error) {
      console.log("Lỗi mở hội thoại: ", error);
    }
  };

  const handleSend = async (text) => {
    try {
      await sendMessage(conversation.conversationId, text);
      const res = await getMessages(conversation.conversationId);
      setMessages(res.data);
    } catch (error) {
        console.log("Lỗi gửi tin nhắn: ", error);
    }
  };


  return (
    <div className="chat-container">
      <Sidebar contacts={conversationList} onSelect={selectContact} />
      <ChatLayout
        conversation={conversation}
        conversationList={conversationList}
        messages={messages}
        currentUserId={currentUserId}
        onSend={handleSend}
      />
    </div>
  );
}
