import "./chat.css";

export default function Sidebar({ contacts, onSelect }) {
  return (
    <div className="sidebar">
      <h3 style={{ padding: "10px" }}>Contacts</h3>
      {contacts.map(c => (
        <div
          key={c.id}
          style={{ padding: "10px", cursor: "pointer" }}
          onClick={() => onSelect(c)}
        >
          {c.name}
        </div>
      ))}
    </div>
  );
}
