create table if not exists public.users
(
    id         serial
        primary key,
    name       varchar(255),
    email      varchar(255),
    country    varchar(255),
    gender     varchar(255),
    is_deleted boolean default false
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists public.documents (
                           id SERIAL PRIMARY KEY,
                           number VARCHAR(255) UNIQUE NOT NULL,
                           uuid UUID DEFAULT uuid_generate_v4(),
                           expireDate TIMESTAMP,
                           isHandled BOOLEAN DEFAULT false
);

create table if not exists public.addresses
(
    id                 serial
        primary key,
    address_has_active boolean default false,
    city               varchar(255),
    country            varchar(255),
    street             varchar(255),
    employee_id        integer
        constraint fk_address_employee references public.users
);



