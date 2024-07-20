-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)

create table address
(
    id     bigserial not null primary key,
    street varchar(50)
);


create table phone
(
    id        bigserial not null primary key,
    client_id bigint,
    number    varchar(50)
);

ALTER TABLE client
    ADD address_id bigint;
