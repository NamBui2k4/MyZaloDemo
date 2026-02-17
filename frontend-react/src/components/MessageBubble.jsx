import "./chat.css";

export default function MessageBubble({ message, isMine }) {
  const renderStatus = () => {
    if (!isMine) return null;

    switch (message.status) {
      case "SENT":
        return "✓";
      case "RECEIVED":
        return "✓✓";
      case "SEEN":
        return <span style={{ color: "#4da3ff" }}>✓✓</span>;
      default:
        return null;
    }
  };

  return (
    <div
      className={`message-row ${
        isMine ? "message-right-wrapper" : "message-left-wrapper"
      }`}
    >
      {!isMine && (
        <div className="avatar">
          {message.sender.name?.charAt(0) || "U"}
        </div>
      )}

      <div
        className={`message-bubble ${
          isMine ? "message-right" : "message-left"
        }`}
      >
        {!isMine && (
          <div style={{ fontSize: "12px", color: "#555" }}>
            {message.sender.name}
          </div>
        )}

        <div>{message.content}</div>

        {isMine && (
          <div style={{ fontSize: "12px", textAlign: "right" }}>
            {renderStatus()}
          </div>
        )}
      </div>
    </div>
  );
}
