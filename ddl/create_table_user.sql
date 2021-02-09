CREATE TABLE user (
  username VARCHAR(50) NOT NULL
  , password VARCHAR(100) NOT NULL
  , enabled TINYINT NOT NULL
  , CONSTRAINT pk_user PRIMARY KEY (username)
);