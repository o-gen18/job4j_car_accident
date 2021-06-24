CREATE TABLE authorities (
  id SERIAL PRIMARY KEY,
  authority VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  enabled BOOLEAN DEFAULT TRUE,
  authority_id INT NOT NULL REFERENCES authorities(id)
);

INSERT INTO authorities (authority) VALUES ('ROLE_USER');
INSERT INTO authorities (authority) VALUES ('ROLE_ADMIN');

INSERT INTO USERS (username, enabled, password, authority_id)
VALUES ('root', true, '$2a$10$NiiClr4hKMBvvWREnFwli.oY8Ikeg87v8Qv4MwSi162zvza3.bGqW',
(SELECT id FROM authorities WHERE authority = 'ROLE_ADMIN'));