# MyZaloDemo

![Chat App Demo](https://via.placeholder.com/800x400?text=Real-Time+Chat+Application+Screenshot)  
*(Replace with an actual screenshot or GIF of your chat interface)*

A modern real-time messaging application built with Spring Boot and WebSocket (STOMP protocol). Supports instant messaging, message delivery/read receipts (SENT → DELIVERED → SEEN), and real-time notifications.

## Technologies Used

- **Backend**: Spring Boot 3.x, Spring WebSocket + STOMP
- **Database**: MySQL / PostgreSQL (or H2 for development)
- **ORM**: JPA + Hibernate
- **Security**: Spring Security (JWT or session-based authentication)
- **Frontend** (optional): Plain HTML + JavaScript (SockJS + Stomp.js) / React / Vue / Mobile client
- **Other tools**: Lombok, MapStruct (for DTO mapping), Jackson

## Key Features

- User registration and login
- Create / join 1-1 or group conversations
- Real-time message sending
- Message status tracking: SENT → DELIVERED → SEEN
- Seen receipts (notify sender when recipient has viewed the message)
- Real-time updates via WebSocket (STOMP destinations: /topic, /queue, /user)

## Project Structure
```
src/
├── main/
│   ├── java/org/example/
│   │   ├── config/               # WebSocketConfig, SecurityConfig, Auth Interceptor
│   │   ├── controller/           # MessageController, UserController
│   │   ├── dto/                  # Data Transfer Objects (MessageStatusDTO, SendMessageRequest...)
│   │   ├── entity/               # JPA entities (Message, MessageStatus, User...)
│   │   ├── repository/           # JPA Repositories
│   │   ├── service/              # Business logic (MessageService, UserService...)
│   │   └── ChatApplication.java
│   └── resources/
│       ├── application.yml       # Database, WebSocket, logging config
│       └── static/               # (if serving static frontend files)
└── test/
```

### Techs

- Java 17 or higher (recommended: 21)
- Maven (or Gradle)
- MySQL / PostgreSQL database (or use embedded H2 for quick testing)

### Step to implement

1. Configure the database (edit src/main/resources/application.yml or application-dev.yml
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/chat_db?useSSL=false&serverTimezone=UTC
    username: root
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update          # or validate / create-drop for dev
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

2. Run:
```
mvn spring-boot:run
# or
./mvnw spring-boot:run
```

Test WebSocket connection
Open file test_socket.html
```
Endpoint: ws://localhost:8080/ws
Send messages to: /app/message.sendMessage (or /app/chat.sendMessage)
Subscribe to: /topic/conversations/{conversationId}, /user/queue/status
```

### Common Issues & Solutions

LazyInitializationException / Could not initialize proxy - no Session
→ Do not return JPA entities directly in WebSocket responses or REST APIs.
→ Always map entities to DTOs inside a @Transactional service method before sending.
Principal null or UsernameNotFoundException
→ Ensure WebSocket handshake is authenticated (use JWT in CONNECT header or query param for testing).
→ Implement a ChannelInterceptor to set Principal from the Authorization header.

### Future Improvements
- File attachments (images, videos, documents)
- Typing indicators ("user is typing...")
- Offline message queuing & delivery
- Push notifications (Firebase Cloud Messaging)
- Docker support & CI/CD pipeline
- Group chat with admin features

### Author

- Name: Nam Phuong
- Location: Ho Chi Minh City, Vietnam
- Email: buin40528@gmail.com

Thank you for checking out the project!
Feel free to open issues or submit pull requests.
text#
