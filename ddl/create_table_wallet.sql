CREATE TABLE wallet (
  id INT AUTO_INCREMENT NOT NULL
  , name VARCHAR(50) NOT NULL
  , username VARCHAR(50) NOT NULL
  , blockchain_address VARCHAR(100) NOT NULL
  , private_key VARCHAR(200) NOT NULL
  , public_key VARCHAR(200) NOT NULL
  , mine TINYINT NOT NULL
  , CONSTRAINT wallet_pk PRIMARY KEY (id)
  , FOREIGN KEY fk_username(username) REFERENCES user(username)
);