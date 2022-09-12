-- MySQL Script generated by MySQL Workbench
-- Sun Jun 26 16:45:21 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

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
-- Table `aniquiz`.`author`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`author` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`author` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(30) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `created_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`type` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`difficulty`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`difficulty` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`difficulty` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`series`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`series` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`series` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `author` VARCHAR(200) NOT NULL,
  `release_date` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`question`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`question` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creation_date` DATETIME NOT NULL,
  `author_id` INT NOT NULL,
  `type_id` INT NOT NULL,
  `difficulty_id` INT NOT NULL,
  `series_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_question_author_idx` (`author_id` ASC) VISIBLE,
  INDEX `fk_question_type1_idx` (`type_id` ASC) VISIBLE,
  INDEX `fk_question_difficulty1_idx` (`difficulty_id` ASC) VISIBLE,
  INDEX `fk_question_series1_idx` (`series_id` ASC) VISIBLE,
  CONSTRAINT `fk_question_author`
    FOREIGN KEY (`author_id`)
    REFERENCES `aniquiz`.`author` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_question_type1`
    FOREIGN KEY (`type_id`)
    REFERENCES `aniquiz`.`type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_question_difficulty1`
    FOREIGN KEY (`difficulty_id`)
    REFERENCES `aniquiz`.`difficulty` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_question_series1`
    FOREIGN KEY (`series_id`)
    REFERENCES `aniquiz`.`series` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`locale`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`locale` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`locale` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`title`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`title` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`title` (
  `question_id` INT NOT NULL,
  `locale_id` INT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`question_id`, `locale_id`),
  INDEX `fk_title_question1_idx` (`question_id` ASC) VISIBLE,
  INDEX `fk_title_locale1_idx` (`locale_id` ASC) VISIBLE,
  CONSTRAINT `fk_title_question1`
    FOREIGN KEY (`question_id`)
    REFERENCES `aniquiz`.`question` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_title_locale1`
    FOREIGN KEY (`locale_id`)
    REFERENCES `aniquiz`.`locale` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aniquiz`.`proposition`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aniquiz`.`proposition` ;

CREATE TABLE IF NOT EXISTS `aniquiz`.`proposition` (
  `title_question_id` INT NOT NULL,
  `title_locale_id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`title_question_id`, `title_locale_id`),
  CONSTRAINT `fk_proposition_title1`
    FOREIGN KEY (`title_question_id` , `title_locale_id`)
    REFERENCES `aniquiz`.`title` (`question_id` , `locale_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
