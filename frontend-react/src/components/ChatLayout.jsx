import ChatHeader from "./ChatHeader";
import MessageList from "./MessageList";
import MessageInput from "./MessageInput";
import "./chat.css";

export default function ChatLayout({
  conversation,
  conversationList,
  messages,
  currentUserId,
  onSend
}) {
  if (!conversation) {
    return (
      <div className="chat-main">
        <div style={{ margin: "auto", color: "#888" }}>
          Select a contact to start chatting
        </div>
      </div>
    );
  }

  return (
    <div className="chat-main">
      <ChatHeader conversation={conversation} />
      <MessageList
        messages={messages}
        currentUserId={currentUserId}
      />
      <MessageInput onSend={onSend} />
    </div>
  );
}
