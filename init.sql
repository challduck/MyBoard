USE springboot_bbs;

CREATE TABLE member (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE KEY email_UNIQUE (email)
);
CREATE TABLE article (
    article_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    hit_count BIGINT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);
CREATE TABLE article_like (
    article_like_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    article_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES member (member_id),
    FOREIGN KEY (article_id) REFERENCES article (article_id)
);
CREATE TABLE comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    article_id BIGINT,
    body VARCHAR(255) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (member_id) REFERENCES member (member_id),
    FOREIGN KEY (article_id) REFERENCES article (article_id)
);
CREATE TABLE refresh_token (
    refresh_token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    refresh_token VARCHAR(1000) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);
CREATE TABLE member_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    roles VARCHAR(255),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);
CREATE TABLE member_login_log (
      member_login_id BIGINT AUTO_INCREMENT PRIMARY KEY,
      member_id BIGINT,
      last_login_ip VARCHAR(255),
      last_login DATETIME,
      FOREIGN KEY (member_id) REFERENCES member(member_id)
);

INSERT INTO member(email, nickname, password, created_at, updated_at) VALUES ('admin@admin.com', 'admin', '$2a$10$jBPH3jwS7Lw8JlxL8NcIXeweGlkUF89wG6gDFudDb8u82RGh/HbEm', NOW(), NOW());
INSERT INTO member(email, nickname, password, created_at, updated_at) VALUES ('abc@a.com', 'user1', '$2a$10$jBPH3jwS7Lw8JlxL8NcIXeweGlkUF89wG6gDFudDb8u82RGh/HbEm', NOW(), NOW());
INSERT INTO member(email, nickname, password, created_at, updated_at) VALUES ('abc@b.com', 'user2', '$2a$10$jBPH3jwS7Lw8JlxL8NcIXeweGlkUF89wG6gDFudDb8u82RGh/HbEm', NOW(), NOW());
INSERT INTO member(email, nickname, password, created_at, updated_at) VALUES ('abc@c.com', 'user3', '$2a$10$jBPH3jwS7Lw8JlxL8NcIXeweGlkUF89wG6gDFudDb8u82RGh/HbEm', NOW(), NOW());


INSERT INTO member_roles(member_id, roles) VALUES ((SELECT member_id FROM member WHERE email = 'admin@admin.com'), 'ADMIN');
INSERT INTO member_roles(member_id, roles) VALUES ((SELECT member_id FROM member WHERE email = 'abc@a.com'), 'MEMBER');
INSERT INTO member_roles(member_id, roles) VALUES ((SELECT member_id FROM member WHERE email = 'abc@b.com'), 'MEMBER');
INSERT INTO member_roles(member_id, roles) VALUES ((SELECT member_id FROM member WHERE email = 'abc@c.com'), 'MEMBER');


INSERT INTO article(member_id, title, content, created_at, updated_at) VALUES (1, 'new article 1', 'new content 1', NOW(), NOW());
INSERT INTO article(member_id, title, content, created_at, updated_at) VALUES (2, 'new article 2', 'new content 2', NOW(), NOW());
INSERT INTO article(member_id, title, content, created_at, updated_at) VALUES (3, 'new article 3', 'new content 3', NOW(), NOW());

INSERT INTO comment(member_id, article_id, body, created_at, updated_at) VALUES (1, 1, '1번 게시글의 1번유저', NOW(), NOW());
INSERT INTO comment(member_id, article_id, body, created_at, updated_at) VALUES (1, 2, '2번 게시글의 1번유저', NOW(), NOW());
INSERT INTO comment(member_id, article_id, body, created_at, updated_at) VALUES (1, 3, '3번 게시글의 1번유저', NOW(), NOW());
INSERT INTO comment(member_id, article_id, body, created_at, updated_at) VALUES (2, 1, '1번 게시글의 2번유저', NOW(), NOW());
INSERT INTO comment(member_id, article_id, body, created_at, updated_at) VALUES (2, 2, '2번 게시글의 2번유저', NOW(), NOW());
INSERT INTO comment(member_id, article_id, body, created_at, updated_at) VALUES (2, 3, '3번 게시글의 2번유저', NOW(), NOW());
INSERT INTO comment(member_id, article_id, body, created_at, updated_at) VALUES (3, 1, '1번 게시글의 3번유저', NOW(), NOW());
INSERT INTO comment(member_id, article_id, body, created_at, updated_at) VALUES (3, 2, '2번 게시글의 3번유저', NOW(), NOW());
INSERT INTO comment(member_id, article_id, body, created_at, updated_at) VALUES (3, 3, '3번 게시글의 3번유저', NOW(), NOW());
