SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

GRANT ALL PRIVILEGES ON aniquiz.* TO 'aniquiz'@'%';

-- -----------------------------------------------------
-- Schema aniquiz
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `aniquiz` DEFAULT CHARACTER SET utf8 ;
USE `aniquiz` ;

-- -----------------------------------------------------
-- Table `aniquiz`.`series`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`series` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`series` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `author` VARCHAR(200) NOT NULL,
  `release_date` DATE NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`contributor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`contributor` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`contributor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(30) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(70) NOT NULL,
  `roles` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`question`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`question` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(5) NOT NULL,
  `difficulty` VARCHAR(3) NOT NULL,
  `created_at` DATETIME NULL,
  `contributor_id` INT NOT NULL,
  `series_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_question_series1_idx` (`series_id` ASC) VISIBLE,
  INDEX `fk_question_contributor1_idx` (`contributor_id` ASC) VISIBLE,
  CONSTRAINT `fk_question_series1`
    FOREIGN KEY (`series_id`)
    REFERENCES `aniquiz`.`series` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_question_contributor1`
    FOREIGN KEY (`contributor_id`)
    REFERENCES `aniquiz`.`contributor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`language`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`language` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`language` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(6) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`title`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`title` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`title` (
  `question_id` INT NOT NULL,
  `language_id` INT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`question_id`, `language_id`),
  INDEX `fk_title_question1_idx` (`question_id` ASC) VISIBLE,
  INDEX `fk_title_language1_idx` (`language_id` ASC) VISIBLE,
  CONSTRAINT `fk_title_question1`
    FOREIGN KEY (`question_id`)
    REFERENCES `aniquiz`.`question` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_title_language1`
    FOREIGN KEY (`language_id`)
    REFERENCES `aniquiz`.`language` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`proposition`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`proposition` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`proposition` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `is_correct` TINYINT(1) NOT NULL,
  `title_question_id` INT NOT NULL,
  `title_language_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_proposition_title1_idx` (`title_question_id` ASC, `title_language_id` ASC) VISIBLE,
  CONSTRAINT `fk_proposition_title1`
    FOREIGN KEY (`title_question_id` , `title_language_id`)
    REFERENCES `aniquiz`.`title` (`question_id` , `language_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
