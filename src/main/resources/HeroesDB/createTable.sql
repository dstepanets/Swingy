create table HEROES
(
    id      INTEGER           not null AUTO_INCREMENT,
    name    VARCHAR(255)      not null
        constraint HEROES_NAME_INDEX
            unique,
    class   VARCHAR(255)      not null,
    level   INTEGER default 0 not null,
    exp     INTEGER default 0 not null,
    hp      INTEGER           not null,
    attack  INTEGER,
    defence INTEGER,
    weapon  INTEGER,
    armor   INTEGER,
    helm    INTEGER,
    constraint HEROES_PK
        primary key (id)
);

INSERT INTO PUBLIC.HEROES (id, name, class, level, exp, hp, attack, defence, weapon, armor, helm)
    VALUES (0, 'Murzik', 'SadCat', 0, 0, 100, 10, 5, 0, 0, 0);
