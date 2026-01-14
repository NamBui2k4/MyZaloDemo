# 📱 Chat Application based on WebSocket (Zalo-like)

## 1. Giới thiệu đề tài

Dự án này nhằm xây dựng một **ứng dụng nhắn tin tức thời (Instant Messaging)** mô phỏng theo mô hình hoạt động của **Zalo**, sử dụng **WebSocket** để hỗ trợ giao tiếp hai chiều theo thời gian thực với độ trễ thấp.

Mục tiêu của đề tài không chỉ dừng lại ở việc xây dựng một ứng dụng chat cơ bản, mà còn tập trung vào **thiết kế kiến trúc dữ liệu và API đúng bản chất của các hệ thống chat hiện đại**, đảm bảo khả năng mở rộng, tính nhất quán dữ liệu và trải nghiệm người dùng.

---

## 2. Mục tiêu chính

- Xây dựng backend cho ứng dụng chat realtime sử dụng **Spring Boot** và **WebSocket**
- Thiết kế mô hình dữ liệu phản ánh đúng hành vi thực tế của hệ thống chat (như Zalo, Messenger)
- Phân tách rõ ràng giữa:
  - Ngữ cảnh hội thoại (Conversation)
  - Nội dung tin nhắn (Message)
  - Trạng thái tin nhắn theo từng người nhận (MessageReceipt / MessageStatus)
- Đảm bảo mỗi API tuân thủ **Single Responsibility Principle**
- Chuẩn bị nền tảng cho các tính năng nâng cao như:
  - Dấu tích gửi / nhận / xem (✓, ✓✓)
  - Chat nhóm
  - Mở rộng WebSocket realtime

---

## 3. Công nghệ sử dụng

### Backend
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security + JWT**
- **WebSocket (STOMP – dự kiến tích hợp)**
- **MySQL / PostgreSQL**

### Công cụ & môi trường
- IntelliJ IDEA
- Maven
- Postman (test API)
- Git

---

## 4. Kiến trúc tổng thể

Hệ thống được thiết kế theo mô hình **layered architecture**:

