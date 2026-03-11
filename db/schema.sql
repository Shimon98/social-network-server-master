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

