
create table artist
(
    id   integer not null
        constraint artist_pk
            primary key autoincrement,
    name TEXT
);

create table venue
(
    id       integer not null
        constraint venue_pk
            primary key autoincrement,
    name     TEXT,
    address  TEXT,
    capacity integer not null
);

create table show
(
    id         integer not null
        constraint show_pk
            primary key autoincrement,
    date       TimeStamp,
    title      TEXT,
    sold_seats integer,
    venue_id   integer not null
        constraint show_venue_id_fk
            references venue
);

create table show_artist
(
    show_id   integer
        constraint show_artist_show_id_fk
            references show,
    artist_id integer
        constraint show_artist_artist_id_fk
            references artist
);

create table ticket
(
    id              integer not null
        constraint ticket_pk
            primary key autoincrement,
    number_of_seats integer,
    buyer_name      TEXT,
    purchase_date   TimeStamp
);

create table user
(
    username TEXT    not null,
    password TEXT    not null,
    id       integer not null
        constraint user_pk
            primary key autoincrement
);