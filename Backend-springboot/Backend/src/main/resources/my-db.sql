-- Bảng Users
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    avatar_url TEXT,
    last_seen BOOLEAN,
    phone TEXT,
    password_hash TEXT NOT NULL,
    hide_online BOOLEAN,
    hide_last_seen BOOLEAN
);

-- Bảng Conversation
CREATE TABLE conversation (
    conversation_id INT PRIMARY KEY AUTO_INCREMENT,
    type TEXT,
    create_at TIMESTAMP NOT NULL
);

-- Bảng Group (liên kết với Conversation)
CREATE TABLE `group` (
    group_id INT PRIMARY KEY,
    create_at TIMESTAMP NOT NULL,
    num_member INT,
    group_name TEXT,
    CONSTRAINT fk_group_conversation FOREIGN KEY (group_id) REFERENCES conversation(conversation_id)
);

-- Bảng Message
CREATE TABLE message (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    conversation_id INT NOT NULL,
    content TEXT,
    CONSTRAINT fk_message_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id)
);

-- Bảng Contact
CREATE TABLE contact (
    contact_id INT PRIMARY KEY AUTO_INCREMENT,
    contact_name TEXT,
    phone TEXT,
    user_id INT,
    CONSTRAINT fk_contact_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Bảng Member
CREATE TABLE member (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    group_id INT,
    CONSTRAINT fk_member_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_member_group FOREIGN KEY (group_id) REFERENCES `group`(group_id)
);

-- Bảng Participant
CREATE TABLE participant (
    participant_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    conversation_id INT,
    CONSTRAINT fk_participant_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_participant_conversation FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id)
);

-- Bảng Message_Status
CREATE TABLE message_status (
    message_id INT,
    user_id INT,
    recieve_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    seen_time TIMESTAMP,
    sent_time TIMESTAMP,
    status TEXT,
    PRIMARY KEY (message_id, user_id),
    CONSTRAINT fk_status_message FOREIGN KEY (message_id) REFERENCES message(message_id),
    CONSTRAINT fk_status_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Bảng Media
CREATE TABLE media (
    message_id INT,
    type TEXT,
    media_name TEXT,
    file_name VARCHAR(255),   -- đổi từ TEXT sang VARCHAR
    create_at TIMESTAMP,
    size INT,
    mime_type TEXT,
    PRIMARY KEY (message_id, file_name),
    CONSTRAINT fk_media_message FOREIGN KEY (message_id) REFERENCES message(message_id)
);

