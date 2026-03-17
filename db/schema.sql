create table if not exists users
(
    id            int auto_increment
        primary key,
    username      varchar(50)  not null,
    email         varchar(100) not null,
    password_hash varchar(255) not null,
    constraint email
        unique (email),
    constraint username
        unique (username)
);

create table if not exists posts
(
    id         int auto_increment
        primary key,
    user_id    int                                not null,
    content    text                                not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    constraint posts_ibfk_1
        foreign key (user_id) references users (id)
);

create index if not exists user_id
    on posts (user_id);

create table if not exists refresh_tokens
(
    id         int auto_increment
        primary key,
    user_id    int          not null,
    token      varchar(500) not null,
    expires_at bigint       not null,
    created_at bigint       not null,
    constraint token
        unique (token),
    constraint refresh_tokens_ibfk_1
        foreign key (user_id) references users (id)
);

