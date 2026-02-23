# MyZaloDemo

<p align="center">
  <img src="https://github.com/user-attachments/assets/1ffefc9f-622f-4ca3-83f4-967c511560f3" width="450" height="453" alt="image"/>
</p>


A modern real-time messaging application built with Spring Boot and WebSocket (STOMP protocol). Supports instant messaging, message delivery/read receipts (SENT → DELIVERED → SEEN), and real-time notifications.

## Technologies Used

- **Backend**: Java 11, Maven, Spring Boot 3.2.3, Spring WebSocket + STOMP
- **Database**: MySQL
- **ORM**: JPA + Hibernate
- **Authentication**: JWT
- **Frontend**: React + vite
- **Other tools**: Lombok (For instance builder), Jackson (for JSON convert)

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

### Implement with Docker
1. Docker - frontend
```
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

FROM nginx:stable-alpine

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```
2. Docker - backend
```
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```
3. Docker compose
```
services:
  mysql:
    image: mysql:8.0
    container_name: mysql-container
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE:-dev_chat_app}
      MYSQL_USER: ${MYSQL_DB_USER:-chatuser}
      MYSQL_PASSWORD: ${MYSQL_DB_PASSWORD:-password123}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: [ "CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-p${MYSQL_ROOT_PASSWORD}" ]
      interval: 20s
      timeout: 10s
      retries: 15
      start_period: 60s

  rabbitmq:
    image: rabbitmq:4-management
    container_name: my-rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
      - "61613:61613"
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
    command: sh -c "rabbitmq-plugins enable rabbitmq_stomp && rabbitmq-server"
    healthcheck:
      test: [ "CMD", "rabbitmqctl", "status" ]
      interval: 20s
      timeout: 10s
      retries: 15
      start_period: 30s

  chat-app:
    build:
      context: ./Backend-springboot
      dockerfile: Dockerfile
    container_name: chat-app
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: dev
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/${MYSQL_DATABASE:-dev_chat_app}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      SPRING_RABBITMQ_HOST: rabbitmq
      SPRING_RABBITMQ_PORT: 5672
    depends_on:
      mysql:
        condition: service_healthy
      rabbitmq:
        condition: service_healthy
    restart: on-failure

  frontend:
    build:
      context: ./frontend-react
      dockerfile: Dockerfile
    container_name: react-app
    ports:
      - "3000:80" 
    depends_on:
      - chat-app
    restart: always

volumes:
  mysql_data:
  rabbitmq_data:
```

2. Run:
```
docker-compose up -d --build
```

Test WebSocket connection (For example)
Access http://localhost:8080/test_socket.html
```
Endpoint: ws://localhost:8080/ws?userId=2
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
