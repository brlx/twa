CREATE SEQUENCE seq_users
INCREMENT 50
START 1000
MINVALUE 1000;

CREATE SEQUENCE seq_alerts
INCREMENT 50
START 1000
MINVALUE 1000;

CREATE SEQUENCE seq_cities
INCREMENT 50
START 1000
MINVALUE 1000;

CREATE TABLE users (
  id       BIGINT NOT NULL DEFAULT NULL,
  username TEXT   NOT NULL DEFAULT NULL,
  password TEXT   NOT NULL DEFAULT NULL,

  CONSTRAINT user_pk PRIMARY KEY (id),
  CONSTRAINT user_username_unique UNIQUE (username)
);

CREATE TABLE cities (
  id       BIGINT NOT NULL DEFAULT NULL,
  cityname TEXT   NOT NULL DEFAULT NULL,
  country  TEXT   NOT NULL DEFAULT NULL,

  CONSTRAINT cities_pk PRIMARY KEY (id)
);

CREATE TABLE alerts (
  id             BIGINT    NOT NULL DEFAULT NULL,
  user_id        BIGINT    NOT NULL DEFAULT NULL,
  city_id        BIGINT    NOT NULL DEFAULT NULL,
  temp_threshold INTEGER   NOT NULL DEFAULT NULL,
  last_triggered TIMESTAMP NOT NULL DEFAULT NULL,
  last_notified  TIMESTAMP NOT NULL DEFAULT NULL,

  CONSTRAINT alerts_pk PRIMARY KEY (id),
  CONSTRAINT alerts_users_fk FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT alerts_cities_fk FOREIGN KEY (city_id) REFERENCES cities (id),
  CONSTRAINT alerts_user_city_unique UNIQUE (user_id, city_id)
);
