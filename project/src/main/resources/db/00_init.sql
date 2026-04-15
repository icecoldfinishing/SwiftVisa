DROP DATABASE IF EXISTS swiftvisa_db;
CREATE DATABASE swiftvisa_db;
\c swiftvisa_db;

CREATE TABLE passport (
    id SERIAL PRIMARY KEY,
    numero VARCHAR(50) UNIQUE NOT NULL,
    date_delivrance DATE,
    date_expiration DATE
);

INSERT INTO passport (numero, date_delivrance, date_expiration) VALUES
('P100001', '2020-01-15', '2030-01-15'),
('P100002', '2019-06-20', '2029-06-20'),
('P100003', '2021-03-10', '2031-03-10'),
('P100004', '2018-11-05', '2028-11-05'),
('P100005', '2022-07-25', '2032-07-25');
