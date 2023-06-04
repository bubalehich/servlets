create table if not exists invoices_db_servlet.card
(
    id          bigserial
    primary key,
    is_active   boolean     not null,
    barcode     varchar(45) not null
    constraint uk_mlbf4rr2ayv1hffdgbfk1uxf9
    unique,
    holder_name varchar(255)
    );

alter table invoices_db_servlet.card
    owner to postgres;

create table if not exists invoices_db_servlet.cash_receipt
(
    id           bigserial
    primary key,
    is_active    boolean        not null,
    amount       numeric(38, 2),
    barcode      varchar(45)    not null
    constraint uk_1ascoc8eol1u1sw15bbl7uwm7
    unique,
    cashier      varchar(255)   not null,
    date         timestamp(6)   not null,
    discount     numeric(38, 2),
    total_amount numeric(38, 2) not null,
    card_id      bigint
    constraint fklulhueyx3pw0djs2qnqhkj7l3
    references invoices_db_servlet.card
    );

alter table invoices_db_servlet.cash_receipt
    owner to postgres;

create table if not exists invoices_db_servlet.item
(
    id             bigserial
    primary key,
    is_active      boolean        not null,
    barcode        varchar(45)    not null
    constraint uk_bfo0nhih8f3jl9m9ublnxr4uy
    unique,
    description    varchar(255)   not null,
    is_on_discount boolean,
    price          numeric(38, 2) not null
    );

alter table invoices_db_servlet.item
    owner to postgres;

create table if not exists invoices_db_servlet.position
(
    id              bigserial
    primary key,
    is_active       boolean not null,
    count           integer not null,
    cash_receipt_id bigint  not null
    constraint fkergcymjdmlfgl460iy0gvs5os
    references invoices_db_servlet.cash_receipt,
    item_id         bigint  not null
    constraint fk4povmr863xpok7k7blig5ndqx
    references invoices_db_servlet.item
);

alter table invoices_db_servlet.position
    owner to postgres;

