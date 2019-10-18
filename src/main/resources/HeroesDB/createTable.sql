create table HEROES
(
    "id"      INTEGER           not null,
    "name"    VARCHAR(255)      not null
        constraint HEROES_NAME_INDEX
            unique,
    "class"   VARCHAR(255)      not null,
    "level"   INTEGER default 0 not null,
    "exp"     INTEGER default 0 not null,
    "hp"      INTEGER           not null,
    "attack"  INTEGER,
    "defence" INTEGER,
    "weapon"  INTEGER,
    "armor"   INTEGER,
    "helm"    INTEGER,
    constraint HEROES_PK
        primary key ("id")
);
