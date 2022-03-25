DROP TABLE IF EXISTS tbl_user;

CREATE TABLE person (
                          id VARCHAR(250) AUTO_INCREMENT  PRIMARY KEY,
                          first_name VARCHAR(250) NOT NULL,
                          last_name VARCHAR(250) NOT NULL,
                          birthday DATE,
                          idCard VARCHAR(250) UNIQUE NOT NULL,
                          email VARCHAR(250),
                          phoneNumber VARCHAR(15) UNIQUE NOT NULL,
                          gender VARCHAR(250) NOT NULL
);

CREATE TABLE relative (
                        pid VARCHAR(250) NOT NULL,
                        relation VARCHAR(250) NOT NULL,
                        name VARCHAR(250) NOT NULL,
                        CONSTRAINT fk_pid FOREIGN KEY (pid) REFERENCES person(id)
);

CREATE TABLE illness (
                        id VARCHAR(250) AUTO_INCREMENT  PRIMARY KEY,
                        code VARCHAR(250) NOT NULL,
                        name VARCHAR(250) NOT NULL,
                        hid VARCHAR(250) NOT NULL,
                        CONSTRAINT fk_hid FOREIGN KEY (hid) REFERENCES health_record(id)
);

CREATE TABLE health_record (
                        id VARCHAR(250) AUTO_INCREMENT  PRIMARY KEY,
                        isTwin BIT,
                        isAdopted BIT,
                        height INT(250),
                        weight INT(250),
                        firstPeriodAge INT(250),
                        birthControl INT(250),
                        pregnantTime INT(250),
                        firstBornAge INT(250),
                        isSmoke BIT,
                        smokeTime INT(250),
                        giveUpSmokeAge VARCHAR(250),
                        wineVolume INT(250),
                        workOutVolume INT(250),
                        workOutType VARCHAR(250),
                        pid VARCHAR(250) NOT NULL,
                        CONSTRAINT fk_personid FOREIGN KEY (pid) REFERENCES person(id)
);
