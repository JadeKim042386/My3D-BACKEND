insert into user_account(id, email, password, nickname, phone, address, user_role) values
(nextval('seq_user_account'), 'admin@gmail.com', '$2a$10$QLK9CfBaRvQmAJwqu/EUoOltJxLYad8hQp/BZ4J9.FZZYw/lzBv.O', 'admin', '01011112222', '{"zipcode":"12345", "street":"street", "detail":"detail"}', 'USER');

insert into article (id, user_account_id, title, content, article_type, article_category, is_free) values
(nextval('seq_article'), 1, 'title', 'content', 'MODEL', 'MUSIC', TRUE);
