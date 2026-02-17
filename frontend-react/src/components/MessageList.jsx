import { useEffect, useRef } from "react";
import MessageBubble from "./MessageBubble";
import "./chat.css";

export default function MessageList({ messages, currentUserId }) {
  const bottomRef = useRef(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="message-list">
      {messages.map(m => (
        <MessageBubble
          key={m.messageId}
          message={m}
          isMine={m.sender.userId === currentUserId}
        />
      ))}
      <div ref={bottomRef} />
    </div>
  );
}
