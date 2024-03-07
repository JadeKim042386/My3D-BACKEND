create sequence seq_user_account start 1;
create table user_account(
    id int8 default nextval('seq_user_account') NOT NULL ,
    email varchar(255) NOT NULL ,
    password varchar(255) NOT NULL ,
    nickname varchar(255) NOT NULL ,
    phone varchar(11) ,
    address jsonb ,
    user_role varchar(11) NOT NULL ,
    created_at timestamp NOT NULL ,
    modified_at timestamp ,
    primary key (id)
);

create sequence seq_article_file start 1;
create table article_file(
    id int8 default nextval('seq_article_file') NOT NULL ,
    byte_size int8 NOT NULL ,
    original_file_name varchar(255) NOT NULL ,
    file_name varchar(255) NOT NULL ,
    file_extension varchar(255) NOT NULL ,
    dimension_option jsonb,
    primary key (id),
    constraint byte_size_range check (byte_size >= 0)
);

create sequence seq_article start 1;
create table article(
    id int8 default nextval('seq_article') NOT NULL ,
    user_account_id int8 NOT NULL ,
    article_file_id int8 ,
    title varchar(255) NOT NULL ,
    content varchar(255) NOT NULL ,
    article_type varchar(255) NOT NULL ,
    article_category varchar(255) NOT NULL ,
    is_free boolean NOT NULL ,
    created_at timestamp NOT NULL ,
    modified_at timestamp ,
    primary key (id),
    foreign key (user_account_id) references user_account (id),
    foreign key (article_file_id) references article_file (id)
);

create sequence seq_article_comment start 1;
create table article_comment(
    id int8 default nextval('seq_article') NOT NULL ,
    user_account_id int8 NOT NULL ,
    article_id int8 NOT NULL ,
    content varchar(255) NOT NULL ,
    created_at timestamp NOT NULL ,
    modified_at timestamp ,
    primary key (id),
    foreign key (user_account_id) references user_account (id),
    foreign key (article_id) references article (id)
);
