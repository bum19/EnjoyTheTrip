use enjoytrip;

CREATE TABLE IF NOT EXISTS `enjoytrip`.`members` (
  `email` VARCHAR(50) NOT NULL CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$'),
  `user_name` VARCHAR(20) NOT NULL,
  `user_password` VARCHAR(255),
  `question` VARCHAR(50),
  `answer` VARCHAR(255),
  `join_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
    
commit;


CREATE TABLE IF NOT EXISTS `review` (
  `review_no` int NOT NULL AUTO_INCREMENT,
  `email` varchar(16) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `content` varchar(2000) DEFAULT NULL,
  `hit` int DEFAULT '0',
  `score` int DEFAULT 5,
  `content_id` int not null,
  `attraction_title` varchar(100) not null,
  `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`review_no`),
  KEY `review_to_members_email_fk` (`email`),
  CONSTRAINT `review_to_members_email_fk` FOREIGN KEY (`email`) REFERENCES `members` (`email`),
  CONSTRAINT fk_review_content FOREIGN KEY (content_id) REFERENCES attraction_info(content_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE review
MODIFY COLUMN email VARCHAR(50);

CREATE TABLE IF NOT EXISTS login_check (
    email VARCHAR(50) NOT NULL CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$'),
    try_count INT NOT NULL DEFAULT 1,
    last_try_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

create database enjoytrip_sec;
use enjoytrip_sec;

CREATE TABLE IF NOT EXISTS `info` (
  `email` VARCHAR(50) NOT NULL CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$') primary key,
    `salt`    varchar(100) NOT NULL,
    `seckey`    varchar(100) NOT NULL,
    `initvector`    varchar(100) NOT NULL
);