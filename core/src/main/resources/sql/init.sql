CREATE TABLE `t_book` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `fileId` INT(32) DEFAULT NULL,
  `title` VARCHAR(200) DEFAULT NULL,
  `description` VARCHAR(2000) DEFAULT NULL,
  `publisher` VARCHAR(50) DEFAULT NULL,
  `author` VARCHAR(1000) DEFAULT NULL,
  `isbn` VARCHAR(50) DEFAULT NULL,
  `year` INT(32) DEFAULT NULL,
  `pages` INT(32) DEFAULT NULL,
  `language` VARCHAR(50) DEFAULT NULL,
  `size` VARCHAR(50) DEFAULT NULL,
  `format` VARCHAR(50) DEFAULT NULL,
  `download` VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=6873 DEFAULT CHARSET=utf8