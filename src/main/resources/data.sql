insert into company(id, company_name, homepage) values
(nextval('seq_company'), 'my3d', null);

insert into user_account(id, company_id, email, password, nickname, phone, address, user_role, created_at, modified_at) values
(nextval('seq_user_account'), null, 'admin@gmail.com', '$2a$10$QLK9CfBaRvQmAJwqu/EUoOltJxLYad8hQp/BZ4J9.FZZYw/lzBv.O', 'admin', '01011112222', '{"zipcode":"12345", "street":"street", "detail":"detail"}', 'USER', now(), null),
(nextval('seq_user_account'), 1, 'adminCompany@gmail.com', '$2a$10$QLK9CfBaRvQmAJwqu/EUoOltJxLYad8hQp/BZ4J9.FZZYw/lzBv.O', 'adminCompany', '01011112222', '{"zipcode":"12345", "street":"street", "detail":"detail"}', 'COMPANY', now(), null);

insert into article_file (id, byte_size, original_file_name, file_name, file_extension, dimension_option) values
(nextval('seq_article_file'), 1000, 'test.stl', 'test.stl', 'stl', '{"name": "option", "dimensions": [{"name": "dimName", "value": 10.0, "unit": "MM"}]}');

insert into article (id, user_account_id, article_file_id, title, content, article_type, article_category, is_free, like_count, created_at, modified_at) values
(nextval('seq_article'), 1, 1, 'title', 'content', 'MODEL', 'MUSIC', TRUE, 1, now(), null);

insert into article_comment (id, user_account_id, article_id, parent_comment_id, content, created_at, modified_at) values
(nextval('seq_article_comment'), 1, 1, null, 'content', now(), null),
(nextval('seq_article_comment'), 1, 1, 1, 'content', now(), null);

insert into article_like (id, user_account_id, article_id, created_at, modified_at) values
(nextval('seq_article_like'), 1, 1, now(), null);

insert into alarm (id, alarm_type, target_id, article_id, sender_id, receiver_id, read_at, created_at, modified_at) values
(nextval('seq_alarm'), 1, 1, 1, 1, 2, null, now(), null);
