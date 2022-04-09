DROP TABLE IF EXISTS tbl_person;
DROP TABLE IF EXISTS tbl_relative;
DROP TABLE IF EXISTS tbl_health_record;
DROP TABLE IF EXISTS tbl_illness;
DROP TABLE IF EXISTS tbl_genogram;

CREATE TABLE tbl_person (
                          id VARCHAR(250) AUTO_INCREMENT  PRIMARY KEY,
                          first_name VARCHAR(250) NOT NULL,
                          last_name VARCHAR(250) NOT NULL,
                          birthday DATE,
                          idCard VARCHAR(250) UNIQUE NOT NULL,
                          email VARCHAR(250),
                          phoneNumber VARCHAR(15) UNIQUE NOT NULL,
                          gender VARCHAR(2) NOT NULL
);

CREATE TABLE tbl_relative (
                        rid VARCHAR(250) AUTO_INCREMENT PRIMARY KEY,
                        pid VARCHAR(250) NOT NULL,
                        relation VARCHAR(250) NOT NULL,
                        name VARCHAR(250) NOT NULL
);

CREATE TABLE tbl_illness (
                        id VARCHAR(250) AUTO_INCREMENT  PRIMARY KEY,
                        code VARCHAR(250) NOT NULL,
                        name VARCHAR(250) NOT NULL,
                        hid VARCHAR(250) NOT NULL
);

CREATE TABLE tbl_health_record (
                        id VARCHAR(250) AUTO_INCREMENT  PRIMARY KEY,
                        isTwin BOOLEAN,
                        isAdopted BOOLEAN,
                        height INT(250),
                        weight INT(250),
                        firstPeriodAge INT(250),
                        birthControl INT(250),
                        pregnantTime INT(250),
                        firstBornAge INT(250),
                        isSmoke BOOLEAN,
                        smokeTime INT(250),
                        giveUpSmokeAge VARCHAR(250),
                        wineVolume INT(250),
                        workOutVolume INT(250),
                        workOutType VARCHAR(250),
                        pid VARCHAR(250) NOT NULL
);

CREATE TABLE tbl_genogram(
                        id VARCHAR(250) PRIMARY KEY,
                        name VARCHAR(250) NOT NULL,
                        gender VARCHAR(5) NOT NULL,
                        motherKey VARCHAR(250) NOT NULL,
                        fatherKey VARCHAR(250) NOT NULL,
                        wife VARCHAR(250) UNIQUE,
                        husband VARCHAR(250) UNIQUE,
                        attributes VARCHAR(250) NOT NULL
);

ALTER TABLE tbl_relative ADD FOREIGN KEY (pid) REFERENCES tbl_person(id);
ALTER TABLE tbl_illness ADD FOREIGN KEY (hid) REFERENCES tbl_health_record(id);
ALTER TABLE tbl_health_record ADD FOREIGN KEY (pid) REFERENCES tbl_person(id);
