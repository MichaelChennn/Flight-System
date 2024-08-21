-- #1 execute the first two sql statements
drop database if exists `flight-system`;
create database if not exists `flight-system`;

-- #2 then choose the database `flight-system` and execute the following script
create table city
(
    id          bigint auto_increment,
    name        varchar(20) null,
    country     varchar(20) null,
    continent   varchar(20) null,
    description longtext    null,
    constraint city_id_uindex
        unique (id)
);

alter table city
    add primary key (id);

create table airport
(
    id      bigint auto_increment,
    name    varchar(20) not null,
    city_id bigint      null,
    iata    varchar(10) null,
    icao    varchar(10) not null,
    constraint airport_id_uindex
        unique (id),
    constraint airport_city_id_fk
        foreign key (city_id) references city (id)
            on update cascade on delete set null
);

alter table airport
    add primary key (id);

create table entertainment
(
    id          bigint auto_increment,
    name        varchar(50)             null,
    type        enum ('MUSIC', 'MOVIE') not null,
    url         text                    not null,
    description text                    null,
    constraint entertainment_id_uindex
        unique (id)
);

alter table entertainment
    add primary key (id);

create table flight
(
    id             bigint auto_increment,
    flight_date    date        not null,
    dep_city_id    bigint      null,
    dep_airport_id bigint      null,
    dep_terminal   int         null comment 'departure terminal -- can be null',
    dep_gate       varchar(10) null comment 'departure gate',
    dep_time_sch   timestamp   null comment 'departure time scheduled',
    dep_time_est   timestamp   null comment 'departure time estimated',
    arr_city_id    bigint      null,
    arr_airport_id bigint      null,
    arr_terminal   int         null comment 'arrival terminal -- can be null',
    arr_gate       varchar(10) null comment 'arrival gate',
    arr_time_sch   timestamp   null comment 'arrival time scheduled',
    arr_time_est   timestamp   null comment 'arrival time estimated',
    icao           varchar(10) null,
    airline        varchar(20) null,
    airplane       varchar(10) null,
    constraint flight_id_uindex
        unique (id),
    constraint flight_airport_id_fk
        foreign key (dep_airport_id) references airport (id)
            on update cascade on delete set null,
    constraint flight_airport_id_fk_2
        foreign key (arr_airport_id) references airport (id)
            on update cascade on delete set null,
    constraint flight_city_id_fk
        foreign key (dep_city_id) references city (id)
            on update cascade on delete set null,
    constraint flight_city_id_fk_2
        foreign key (arr_city_id) references city (id)
            on update cascade on delete set null
);

alter table flight
    add primary key (id);

create table inflight_service
(
    id          bigint auto_increment,
    type        enum ('FOOD', 'DRINK', 'OTHERS') not null,
    description text                             null,
    constraint inflight_service_id_uindex
        unique (id)
);

alter table inflight_service
    add primary key (id);

create table instruction
(
    id          bigint auto_increment,
    name        varchar(50)                                      null,
    type        enum ('SEATBELT', 'EMERGENCYEXIT', 'OXYGENMASK') not null,
    url         text                                             null,
    des         int                                              null,
    description text                                             null,
    constraint instruction_id_uindex
        unique (id)
);

alter table instruction
    add primary key (id);

create table poi
(
    id      bigint auto_increment,
    name    varchar(100)                                         null,
    type    enum ('HOTEL', 'RESTAURANT', 'SIGHTSEEING', 'OTHER') null,
    city_id bigint                                               null,
    lat     double                                               null,
    lon     double                                               null,
    address text                                                 null,
    rate    float(2, 1)                                          null,
    url     text                                                 null,
    constraint poi_id_uindex
        unique (id),
    constraint poi_city_id_fk
        foreign key (city_id) references city (id)
            on update cascade on delete set null
);

alter table poi
    add primary key (id);

create table reward
(
    id   bigint auto_increment,
    name varchar(50) null,
    constraint Reward_id_uindex
        unique (id)
);

alter table reward
    add primary key (id);

create table survey
(
    id       bigint auto_increment,
    theme    enum ('FLIGHT', 'CATERING', 'ENTERTAINMENT', 'SERVICE', 'COMFORT') not null,
    question longtext                                                           not null,
    constraint flight_survey_id_uindex
        unique (id)
);

alter table survey
    add primary key (id);

create table user
(
    id         bigint auto_increment,
    user_name  varchar(100) not null,
    birth_date date         not null,
    email      varchar(100) not null,
    password   text         null,
    constraint table_name_id_uindex
        unique (id)
);

alter table user
    add primary key (id);

create table user_entertainment
(
    id               bigint auto_increment,
    user_id          bigint    null,
    entertainment_id bigint    null,
    timestamp        timestamp null,
    constraint user_entertainment_id_uindex
        unique (id),
    constraint user_entertainment_entertainment_id_fk
        foreign key (entertainment_id) references entertainment (id)
            on update cascade on delete cascade,
    constraint user_entertainment_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
);

alter table user_entertainment
    add primary key (id);

create table user_flight
(
    id        bigint auto_increment,
    user_id   bigint null,
    flight_id bigint null,
    constraint user_flight_id_uindex
        unique (id),
    constraint user_flight_flight_id_fk
        foreign key (flight_id) references flight (id)
            on update cascade on delete cascade,
    constraint user_flight_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
);

alter table user_flight
    add primary key (id);

create table user_inflightservice
(
    id                 bigint auto_increment,
    user_id            bigint    null,
    inflightservice_id bigint    null,
    timestamp          timestamp null,
    constraint user_service_id_uindex
        unique (id),
    constraint user_service_inflight_service_id_fk
        foreign key (inflightservice_id) references inflight_service (id)
            on update cascade on delete cascade,
    constraint user_service_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
);

alter table user_inflightservice
    add primary key (id);

create table user_instruction
(
    id             bigint auto_increment,
    user_id        bigint    null,
    instruction_id bigint    null,
    timestamp      timestamp null,
    constraint user_instruction_id_uindex
        unique (id),
    constraint user_instruction_instruction_id_fk
        foreign key (instruction_id) references instruction (id)
            on update cascade on delete cascade,
    constraint user_instruction_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
);

alter table user_instruction
    add primary key (id);

create table user_poi
(
    id      bigint auto_increment,
    user_id bigint not null,
    poi_id  bigint not null,
    constraint user_poi_id_uindex
        unique (id),
    constraint user_poi_poi_id_fk
        foreign key (poi_id) references poi (id)
            on update cascade on delete cascade,
    constraint user_poi_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
);

alter table user_poi
    add primary key (id);

create table user_reward
(
    id        bigint auto_increment,
    user_id   bigint    null,
    reward_id bigint    null,
    timestamp timestamp null,
    constraint user_reward_id_uindex
        unique (id),
    constraint user_reward_reward_id_fk
        foreign key (reward_id) references reward (id)
            on update cascade on delete set null,
    constraint user_reward_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete set null
);

alter table user_reward
    add primary key (id);

create table user_survey
(
    id        bigint auto_increment,
    user_id   bigint        null,
    survey_id bigint        not null,
    rate      decimal(2, 1) null,
    timestamp timestamp     null,
    comment   longtext      null,
    constraint user_survey_id_uindex
        unique (id),
    constraint user_survey_survey_id_fk
        foreign key (survey_id) references survey (id)
            on update cascade on delete cascade,
    constraint user_survey_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete set null
);

alter table user_survey
    add primary key (id);

create table weather
(
    id                bigint auto_increment,
    date              date          not null,
    city_id           bigint        null,
    weather_condition varchar(20)   null,
    high_temp         tinyint       null comment 'grad celcius',
    low_temp          tinyint       null,
    wind              tinyint       null comment 'km/h',
    humidity          decimal(3, 2) null comment 'percentage',
    constraint weather_id_uindex
        unique (id),
    constraint weather_city_id_fk
        foreign key (city_id) references city (id)
            on update cascade on delete set null
);

alter table weather
    add primary key (id);


