CREATE TABLE people
(
people_id SERIAL PRIMARY KEY,
age integer,
driver_license boolean DEFAULT FALSE
)

CREATE TABLE cars
(
car_id SERIAL PRIMARY KEY,
car_fabrique varchar,
car_model varchar,
car_price integer,
people_id integer,
FOREIGN KEY (people_id) REFERENCES people (people_id)
)