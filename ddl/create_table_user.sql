-- MySQL
CREATE TABLE users (
  username VARCHAR(50) NOT NULL
  , password VARCHAR(100) NOT NULL
  , enabled TINYINT NOT NULL
  , active TINYINT NOT NULL
  , CONSTRAINT pk_users PRIMARY KEY (username)
);