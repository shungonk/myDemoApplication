CREATE TABLE wallet (
  name VARCHAR(50) NOT NULL
  , username VARCHAR(50) NOT NULL
  , blockchain_address VARCHAR(100) NOT NULL
  , public_key VARCHAR(200) NOT NULL
  , private_key VARCHAR(200) NOT NULL
  , CONSTRAINT wallet_pk PRIMARY KEY (name, username)
  , FOREIGN KEY fk_username(username) REFERENCES user(username)
);