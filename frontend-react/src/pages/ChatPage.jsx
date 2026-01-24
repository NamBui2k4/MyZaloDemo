import { useState } from "react";
import ContactList from "../components/ContactList";
import ChatWindow from "../components/ChatWindow";

export default function ChatPage() {
  const [conversationId, setConversationId] = useState(null);

  return (
    <div style={{ display: "flex" }}>
      <ContactList onSelectConversation={setConversationId} />
      {conversationId && (
        <ChatWindow conversationId={conversationId} />
      )}
    </div>
  );
}
