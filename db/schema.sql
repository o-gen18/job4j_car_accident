CREATE TABLE accident_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR (2000) UNIQUE
);

CREATE TABLE rule (
    id SERIAL PRIMARY KEY,
    name VARCHAR (2000) UNIQUE
);

CREATE TABLE accident (
    id serial primary key,
    name VARCHAR (2000),
    text TEXT,
    address VARCHAR (2000),
    accident_type_id INT REFERENCES accident_type(id)
);

CREATE TABLE accident_rule (
    accident_id INT REFERENCES accident(id),
    rule_id INT REFERENCES rule(id)
);