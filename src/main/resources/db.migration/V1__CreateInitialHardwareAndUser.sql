CREATE TABLE user
(
    id int AUTO_INCREMENT,
    username varchar(64) NOT NULL,
    email varchar(64) NOT NULL,
    password_hash varchar(64) NOT NULL,
    password_salt varchar(64) NOT NULL,
    admin bit NOT NULL,
    avatar_url varchar(64) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (email, username)
);

CREATE TABLE gpu
(
    id int AUTO_INCREMENT,
    model varchar(128) NOT NULL,
    manufacturer varchar(64),
    ram int NOT NULL,
    clock_speed int NOT NULL,
    turbo_speed int NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (model)
);

create table cpu
(
    id int AUTO_INCREMENT,
    model varchar(128) NOT NULL,
    manufacturer varchar(64),
    cores int NOT NULL,
    clock_speed double NOT NULL,
    turbo_speed double NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (model)
);

create table ram
(
    id int AUTO_INCREMENT,
    model varchar(128) NOT NULL,
    manufacturer varchar(64),
    ram int NOT NULL,
    memory_speed int NOT NULL,
    type varchar(32) NOT NULL,
    timings VARCHAR(32),
    channels int,
    PRIMARY KEY (id),
    UNIQUE (model)
);