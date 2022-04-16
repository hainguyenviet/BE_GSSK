
CREATE TABLE IF not EXISTS tbl_person (
                          id VARCHAR(250) PRIMARY KEY,
                          first_name VARCHAR(250) NOT NULL,
                          last_name VARCHAR(250) NOT NULL,
                          birthday DATE,
                          idCard VARCHAR(250) UNIQUE NOT NULL,
                          email VARCHAR(250) UNIQUE,
                          phoneNumber VARCHAR(15) UNIQUE NOT NULL,
                          gender VARCHAR(2) NOT NULL
);

CREATE TABLE IF not EXISTS tbl_relative (
                        rid VARCHAR(250) ,
                        pid VARCHAR(250) NOT NULL,
                        relation VARCHAR(250) NOT NULL,
                        name VARCHAR(250) NOT NULL
);

CREATE TABLE IF not EXISTS tbl_illness (
                        id VARCHAR(250)   PRIMARY KEY,
                        code VARCHAR(250) NOT NULL,
                        name VARCHAR(250) NOT NULL,
                        hid VARCHAR(250) NOT NULL
);

CREATE TABLE IF not EXISTS tbl_health_record (
                        id VARCHAR(250) PRIMARY KEY,
                        isTwin BOOLEAN,
                        isAdopted BOOLEAN,
                        height smallINT,
                        weight smallINT,
                        firstPeriodAge smallINT,
                        birthControl smallINT,
                        pregnantTime smallINT,
                        firstBornAge smallINT,
                        isSmoke BOOLEAN,
                        smokeTime smallINT,
                        giveUpSmokeAge VARCHAR(250),
                        wineVolume smallINT,
                        workOutVolume smallINT,
                        workOutType VARCHAR(250),
                        pid VARCHAR(250) NOT NULL
);

CREATE TABLE IF not EXISTS tbl_genogram(
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
