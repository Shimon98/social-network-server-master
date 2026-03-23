create table users
(
    id                bigint auto_increment
        primary key,
    username          varchar(50)  not null,
    email             varchar(100) not null,
    password_hash     varchar(255) not null,
    profile_image_url varchar(500) null,
    constraint email
        unique (email),
    constraint username
        unique (username)
);

create table follows
(
    follower_id bigint not null,
    followed_id bigint not null,
    primary key (follower_id, followed_id),
    constraint followed_id
        foreign key (followed_id) references users (id)
            on delete cascade,
    constraint follower_id
        foreign key (follower_id) references users (id)
            on delete cascade
);

create table posts
(
    id         bigint auto_increment
        primary key,
    user_id    bigint                              not null,
    content    text                                not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    constraint poster_user_id
        foreign key (user_id) references users (id)
            on delete cascade
);

create index posts_user_id_created_at_index
    on posts (user_id, created_at);

create table refresh_tokens
(
    id         bigint auto_increment
        primary key,
    user_id    bigint       not null,
    token      varchar(500) not null,
    expires_at bigint       not null,
    created_at bigint       not null,
    constraint token
        unique (token),
    constraint user_id
        foreign key (user_id) references users (id)
            on delete cascade
);

