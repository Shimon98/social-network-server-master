create table users
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

create table social_network.refresh_tokens
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
        foreign key (user_id) references social_network.users (id)
);

create index user_id
    on social_network.refresh_tokens (user_id);



