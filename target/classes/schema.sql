DROP TABLE IF EXISTS tbl_user;

CREATE TABLE person (
                          id VARCHAR(250) AUTO_INCREMENT  PRIMARY KEY,
                          first_name VARCHAR(250) NOT NULL,
                          last_name VARCHAR(250) NOT NULL,
                          birthday DATE
);
