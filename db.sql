CREATE TABLE hanee_db.tb_user ( 
  id VARCHAR(32) NOT NULL , 
  password VARCHAR(128) NOT NULL , 
  phone_no VARCHAR(128) NULL , 
  name VARCHAR(128) NULL , 
  user_type VARCHAR(2) NOT NULL , 
  is_del VARCHAR(1) NOT NULL DEFAULT 'N' , 
  first_id VARCHAR(32) NOT NULL , 
  first_date DATETIME NOT NULL DEFAULT NOW() ,
  last_id VARCHAR(32) NOT NULL , 
  last_date DATETIME NOT NULL DEFAULT NOW() , 
  PRIMARY KEY (id), 
  INDEX tb_user_i02 (name), 
  INDEX tb_user_i03 (is_del, id), 
  INDEX tb_user_i04 (user_type), 
  UNIQUE tb_user_i01 (phone_no)) 
  ENGINE = InnoDB 
  COMMENT = 'login user info';
