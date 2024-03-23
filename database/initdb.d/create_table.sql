create sequence seq_company start 1;
create table company(
    id int8 default nextval('seq_company') NOT NULL ,
    company_name varchar(255) NOT NULL ,
    homepage varchar(255),
    primary key (id)
);

create sequence seq_user_refresh_token start 1;
create table user_refresh_token(
    id int8 default nextval('seq_user_refresh_token') NOT NULL ,
    refresh_token varchar(255)	NOT NULL,
    reissue_count bigint default 0 NOT NULL,
    primary key (id)
);
create index reissue_count_idx on user_refresh_token (reissue_count);

create sequence seq_verify_email start 1;
create table verify_email(
    id int8 default nextval('seq_verify_email') NOT NULL ,
    email varchar(255) NOT NULL ,
    secret_code varchar(255) NOT NULL ,
    created_at timestamp NOT NULL ,
    expired_at timestamp NOT NULL ,
    primary key (id)
);
create unique index verify_email_idx on verify_email (email);

create sequence seq_user_account start 1;
create table user_account(
    id int8 default nextval('seq_user_account') NOT NULL ,
    company_id int8 ,
    user_refresh_token_id int8 NOT NULL ,
    email varchar(255) NOT NULL ,
    password varchar(255) NOT NULL ,
    nickname varchar(255) NOT NULL ,
    phone varchar(11) ,
    address jsonb ,
    user_role varchar(11) NOT NULL ,
    created_at timestamp NOT NULL ,
    modified_at timestamp ,
    primary key (id),
    foreign key (company_id) references company (id) on delete cascade,
    foreign key (user_refresh_token_id) references user_refresh_token (id) on delete cascade
);
create unique index email_idx on user_account (email);

create sequence seq_subscribe start 1;
create table subscribe(
    id int8 default nextval('seq_subscribe') NOT NULL ,
    user_account_id int8 NOT NULL ,
    package_type varchar(255) NOT NULL ,
    subscribe_status varchar(255) NOT NULL ,
    started_at timestamp NOT NULL,
    primary key (id),
    foreign key (user_account_id) references user_account (id)
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
    article_category varchar(255) ,
    is_free boolean NOT NULL ,
    like_count int4 NOT NULL default 0 ,
    created_at timestamp NOT NULL ,
    modified_at timestamp ,
    primary key (id),
    foreign key (user_account_id) references user_account (id) on delete cascade ,
    foreign key (article_file_id) references article_file (id)
);

create sequence seq_article_comment start 1;
create table article_comment(
    id bigint default nextval('seq_article') NOT NULL ,
    user_account_id bigint NOT NULL ,
    article_id bigint NOT NULL ,
    parent_comment_id int8 ,
    content varchar(255) NOT NULL ,
    created_at timestamp NOT NULL ,
    modified_at timestamp ,
    primary key (id),
    foreign key (user_account_id) references user_account (id) on delete cascade ,
    foreign key (article_id) references article (id) on delete cascade
);

create sequence seq_article_like start 1;
create table article_like(
    id bigint default nextval('seq_article') NOT NULL ,
    user_account_id bigint NOT NULL ,
    article_id bigint NOT NULL ,
    created_at timestamp NOT NULL ,
    modified_at timestamp ,
    primary key (id),
    foreign key (user_account_id) references user_account (id) on delete cascade ,
    foreign key (article_id) references article (id) on delete cascade
);

create sequence seq_alarm start 1;
create table alarm(
    id bigint default nextval('seq_alarm') NOT NULL ,
    alarm_type varchar(255) NOT NULL ,
    target_id bigint NOT NULL ,
    article_id bigint NOT NULL ,
    sender_id bigint NOT NULL ,
    receiver_id bigint NOT NULL ,
    read_at timestamp ,
    created_at timestamp NOT NULL ,
    modified_at timestamp ,
    primary key (id),
    foreign key (receiver_id) references user_account (id) on delete cascade ,
    foreign key (sender_id) references user_account (id) on delete cascade ,
    foreign key (article_id) references article (id) on delete cascade
);

create index parent_comment_id_idx on article_comment (parent_comment_id);
create index receiver_idx on alarm (receiver_id);
create unique index article_id_and_user_id_idx on article_like (user_account_id, article_id);
