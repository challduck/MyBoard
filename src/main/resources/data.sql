INSERT INTO member(email, nickname, password, created_at, updated_at, last_login, last_login_ip) VALUES ('admin@admin.com', 'admin', '$2a$10$jBPH3jwS7Lw8JlxL8NcIXeweGlkUF89wG6gDFudDb8u82RGh/HbEm', NOW(), NOW(), NOW(), '192.168.0.1');

INSERT INTO member(email, nickname, password, created_at, updated_at, last_login, last_login_ip) VALUES ('abc@a.com', 'user1', '$2a$10$jBPH3jwS7Lw8JlxL8NcIXeweGlkUF89wG6gDFudDb8u82RGh/HbEm', NOW(), NOW(), NOW(), '192.168.0.2');
INSERT INTO member(email, nickname, password, created_at, updated_at, last_login, last_login_ip) VALUES ('abc@b.com', 'user2', '$2a$10$jBPH3jwS7Lw8JlxL8NcIXeweGlkUF89wG6gDFudDb8u82RGh/HbEm', NOW(), NOW(), NOW(), '192.168.0.3');
INSERT INTO member(email, nickname, password, created_at, updated_at, last_login, last_login_ip) VALUES ('abc@c.com', 'user3', '$2a$10$jBPH3jwS7Lw8JlxL8NcIXeweGlkUF89wG6gDFudDb8u82RGh/HbEm', NOW(), NOW(), NOW(), '192.168.0.4');


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
