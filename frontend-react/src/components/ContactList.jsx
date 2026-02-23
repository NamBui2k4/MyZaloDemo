import { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext"; // Giả sử bạn có AuthContext lưu token
import { getContacts } from "../api/userApi"; // API lấy danh sách contacts/user
import { openPrivateConversation } from "../api/conversationApi";
import { Avatar, Badge, List, Spin, Empty, Typography } from "antd"; // Dùng Ant Design cho đẹp (hoặc component tự viết)
import { UserOutlined } from "@ant-design/icons";

const { Text } = Typography;

export default function ContactList({ onSelectConversation }) {
  const { token } = useAuth(); // Lấy token từ context
  const [contacts, setContacts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch danh sách contacts từ backend khi component mount
  useEffect(() => {
    const fetchContacts = async () => {
      try {
        setLoading(true);
        setError(null);

        const res = await getContacts(token); 
        
        setContacts(res.data || []);
      } catch (err) {
        console.error("Fetch contacts failed:", err);
        setError("Không thể tải danh sách liên hệ");
      } finally {
        setLoading(false);
      }
    };

    if (token) {
      fetchContacts();
    }
  }, [token]);

  const handleSelect = async (userId) => {
    try {
      // Gọi API mở private conversation
      const res = await openPrivateConversation(userId, token);
      const conversationId = res.data.conversationId;

      // Callback để parent (ChatWindow) mở conversation
      onSelectConversation(conversationId, userId);
    } catch (err) {
      console.error("Open conversation failed:", err);
      alert("Không thể mở cuộc trò chuyện");
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-full">
        <Spin tip="Đang tải danh sách liên hệ..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center text-red-500 p-4">
        {error}
      </div>
    );
  }

  if (contacts.length === 0) {
    return (
      <Empty
        description="Chưa có liên hệ nào"
        className="mt-10"
      />
    );
  }

  return (
    <div className="h-full overflow-y-auto">
      <div className="p-4 border-b">
        <h3 className="text-lg font-semibold">Danh sách liên hệ</h3>
      </div>

      <List
        itemLayout="horizontal"
        dataSource={contacts}
        renderItem={(contact) => (
          <List.Item
            className="cursor-pointer hover:bg-gray-100 transition-colors"
            onClick={() => handleSelect(contact.id)}
          >
            <List.Item.Meta
              avatar={
                <Badge dot={contact.online}> {/* Nếu backend có field online */}
                  <Avatar
                    src={contact.avatarUrl}
                    icon={!contact.avatarUrl && <UserOutlined />}
                  />
                </Badge>
              }
              title={<Text strong>{contact.name || contact.phone}</Text>}
              description={contact.lastMessagePreview || "Bắt đầu trò chuyện"} // Nếu có last message
            />
          </List.Item>
        )}
      />
    </div>
  );
}