create sequence seq_user_account start 1;
create table user_account(
    id bigint default nextval('seq_user_account') NOT NULL ,
    email varchar(255) NOT NULL ,
    password varchar(255) NOT NULL ,
    nickname varchar(255) NOT NULL ,
    phone varchar(11) ,
    address jsonb ,
    user_role varchar(11) NOT NULL ,
    primary key (id)
);

create sequence seq_article start 1;
create table article(
    id bigint default nextval('seq_article') NOT NULL ,
    user_account_id bigint NOT NULL ,
    title varchar(255) NOT NULL ,
    content varchar(255) NOT NULL ,
    article_type varchar(255) NOT NULL ,
    article_category varchar(255) NOT NULL ,
    is_free boolean NOT NULL ,
    primary key (id),
    foreign key (user_account_id) references user_account (id)
);
