import { openPrivateConversation } from "../api/conversationApi";

export default function ContactList({ onSelectConversation }) {
  const contacts = [
    { id: 2, name: "User B" },
    { id: 3, name: "User C" },
  ];

  const handleClick = async (userId) => {
    const res = await openPrivateConversation(userId);
    onSelectConversation(res.data.conversationId);
  };

  return (
    <div>
      <h3>Contacts</h3>
      {contacts.map(c => (
        <div key={c.id} onClick={() => handleClick(c.id)}>
          {c.name}
        </div>
      ))}
    </div>
  );
}
