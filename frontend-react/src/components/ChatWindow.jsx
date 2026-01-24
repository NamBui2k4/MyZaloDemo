import { useEffect, useState } from "react";
import { getMessages, sendMessage } from "../api/messageApi";

export default function ChatWindow({ conversationId }) {
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");

  useEffect(() => {
    getMessages(conversationId).then(res =>
      setMessages(res.data)
    );
  }, [conversationId]);

  const handleSend = async () => {
    await sendMessage(conversationId, text);
    setText("");
    const res = await getMessages(conversationId);
    setMessages(res.data);
  };

  return (
    <div>
      <h3>Conversation {conversationId}</h3>

      {messages.map(m => (
        <div key={m.messageId}>
          <b>{m.sender.userId}:</b> {m.content}
        </div>
      ))}

      <input
        value={text}
        onChange={e => setText(e.target.value)}
      />
      <button onClick={handleSend}>Send</button>
    </div>
  );
}
