create schema if not exists game;

create table if not exists game.field (
    id serial primary key,
    xAxis integer,
    yAxis integer,
    borderColor text,
    color text
);

create table if not exists game.last_move (
    color text primary key
);