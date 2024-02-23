create sequence seq_article start 1;
create table article(
    id bigint default nextval('seq_article') NOT NULL ,
    title varchar(255) NOT NULL ,
    content varchar(255) NOT NULL ,
    article_type varchar(255) NOT NULL ,
    article_category varchar(255) NOT NULL ,
    is_free boolean NOT NULL ,
    primary key (id)
);