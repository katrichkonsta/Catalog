CREATE TABLE IF NOT EXISTS categories
(
    id              serial primary key,
    name            character varying(255) not null unique,
    subcategory_ids integer                null,
    FOREIGN KEY (subcategory_ids) references categories(id)
);

CREATE TABLE IF NOT EXISTS products
(
    id          serial primary key,
    name        character varying(255) not null,
    amount      integer                not null,
    price       double precision       not null,
    weight      integer                not null,
    country     character varying(255) null,
    year        integer                null,
    category_id integer                not null,
    foreign key (category_id) references categories (id)
);