insert into user_account(id, email, password, nickname, phone, address, user_role, created_at, modified_at) values
(nextval('seq_user_account'), 'admin@gmail.com', '$2a$10$QLK9CfBaRvQmAJwqu/EUoOltJxLYad8hQp/BZ4J9.FZZYw/lzBv.O', 'admin', '01011112222', '{"zipcode":"12345", "street":"street", "detail":"detail"}', 'USER', now(), null);

insert into article_file (id, byte_size, original_file_name, file_name, file_extension, dimension_option) values
(nextval('seq_article_file'), 1000, 'test.stl', 'test.stl', 'stl', '{"name": "option", "dimensions": [{"name": "dimName", "value": 10.0, "unit": "MM"}]}');

insert into article (id, user_account_id, article_file_id, title, content, article_type, article_category, is_free, created_at, modified_at) values
(nextval('seq_article'), 1, 1, 'title', 'content', 'MODEL', 'MUSIC', TRUE, now(), null);

insert into article_comment (id, user_account_id, article_id, content, created_at, modified_at) values
(nextval('seq_article_comment'), 1, 1, 'content', now(), null);
