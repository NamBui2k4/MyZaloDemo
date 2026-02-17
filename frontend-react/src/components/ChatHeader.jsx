import "./chat.css";

export default function ChatHeader({ conversation }) {
  return (
    <div className="chat-header">
      Conversation #{conversation.conversationId}
    </div>
  );
}
