
-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server Version:               10.10.2-MariaDB - mariadb.org binary distribution
-- Server Betriebssystem:        Win64
-- HeidiSQL Version:             12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Exportiere Datenbank Struktur für eiscafegelatelli
CREATE DATABASE IF NOT EXISTS `eiscafegelatelli` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `eiscafegelatelli`;

-- Exportiere Struktur von Tabelle eiscafegelatelli.flavour
CREATE TABLE IF NOT EXISTS flavour (
                                       flavour_name VARCHAR(15) NOT NULL COMMENT 'flavour''s unique name',
                                       contribution_margin DECIMAL(65, 2) COMMENT 'flavor''s contribution margin',
                                       PRIMARY KEY (flavour_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='offered icecream flavours';

-- Exportiere Daten aus Tabelle eiscafegelatelli.flavour: ~4 rows (ungefähr)
DELETE FROM flavour;
INSERT INTO flavour (flavour_name, contribution_margin) VALUES
        ('Chocolate', 0.8),
        ('Vanilla', 0.6),
        ('Oreo', 0.4),
        ('Strawberry', 0.5);

-- Exportiere Struktur von Tabelle eiscafegelatelli.ingredient
CREATE TABLE IF NOT EXISTS `ingredient` (
	`ingredient_name` VARCHAR(15) NOT NULL COMMENT 'ingredient''s unique name',
	`purchase_price` DECIMAL(3,2) NOT NULL COMMENT 'purchase price of the ingredient per unit',
	`unit` TINYTEXT NOT NULL COMMENT 'unit in which the ingredient is commonly measured',
	PRIMARY KEY (`ingredient_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='available ingredients to produce icecream flavours';

-- Exportiere Daten aus Tabelle eiscafegelatelli.ingredient: 4~ rows (ungefähr)
DELETE FROM `ingredient`;
INSERT INTO `ingredient` (`ingredient_name`, `purchase_price`, `unit`) VALUES
    ('Unsweetened Cocoa Powder', 4.5,'kg'),
    ('Whole Milk', 2.5,'l'),
    ('Granulated Sugar', 1.5,'kg'),
    ('Heavy Cream', 3.5,'l'),
    ('Egg Yolk', 2.75,'l'),
    ('Bittersweet Chocolate', 5.5,'kg'),
    ('Fine Sea Salt', 1.5,'kg'),
    ('Vanilla Extract', 4.5,'kg'),
    ('Oreo Cookies', 5.0,'kg'),
    ('Lemon Juice', 3.0,'l'),
    ('Fresh Strawberries', 7,'kg');

-- Exportiere Struktur von Tabelle eiscafegelatelli.batch
CREATE TABLE IF NOT EXISTS `batch` (
  `id` INT(10) NOT NULL COMMENT 'unique id to identify batch of ingredients',
  `bbd` DATE NOT NULL COMMENT 'ingredient batch''s best before date',
  `amount` DECIMAL(65, 2) NOT NULL,
  `ingredient_name` VARCHAR(15) NOT NULL COMMENT 'foreign key type of ingredient stored in the batch',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK__stored_ingredient` FOREIGN KEY (`ingredient_name`) REFERENCES `ingredient` (`ingredient_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='storage for batches of ingredients';

CREATE EVENT IF NOT EXISTS check_bbd_event
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
    DELETE FROM batch WHERE bbd < CURDATE();
END;

-- Exportiere Daten aus Tabelle eiscafegelatelli.batch: 4~ rows (ungefähr)
DELETE FROM `batch`;
INSERT INTO `batch` (`id`, `bbd`, `amount`, `ingredient_name`) VALUES
	(1, '2025.05.23', 1, 'Unsweetened Cocoa Powder'),
	(2, '2023.12.31', 0.22, 'Vanilla Extract'),
	(3, '2024.12.31', 0.4, 'Oreo Cookies'),
	(4, '2023.04.10', 1, 'Fresh Strawberries');

-- Exportiere Struktur von Tabelle eiscafegelatelli.flavour_ingredient
CREATE TABLE IF NOT EXISTS `flavour_ingredient` (
  `flavour_name` VARCHAR(15) NOT NULL,
  `ingredient_name` VARCHAR(15) NOT NULL,
  `amount` DECIMAL(65, 2) NOT NULL,
  PRIMARY KEY (`flavour_name`,`ingredient_name`),
  KEY `FK__ingredient` (`ingredient_name`),
  CONSTRAINT `FK__flavour` FOREIGN KEY (`flavour_name`) REFERENCES `flavour` (`flavour_name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK__ingredient` FOREIGN KEY (`ingredient_name`) REFERENCES `ingredient` (`ingredient_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='ingredients needed for flavours';

-- Exportiere Daten aus Tabelle eiscafegelatelli.flavour_ingredient: 4~ rows (ungefähr)
DELETE FROM `flavour_ingredient`;
INSERT INTO `flavour_ingredient` (`flavour_name`, `ingredient_name`, `amount`) VALUES
	('Chocolate', 'Heavy Cream', 2.5),
	('Chocolate', 'Whole Milk', 1.25),
	('Chocolate', 'Granulated Sugar', 0.75),
	('Chocolate', 'Unsweetened Cocoa Powder', 0.25),
	('Chocolate', 'Fine Sea Salt', 0.5),
	('Chocolate', 'Egg Yolk', 15),
	('Chocolate', 'Bittersweet Chocolate', 0.5),
	('Chocolate', 'Vanilla Extract', 2.5),
	('Vanilla', 'Heavy Cream', 2.5),
	('Vanilla', 'Whole Milk', 1.25),
	('Vanilla', 'Granulated Sugar', 0.75),
	('Vanilla', 'Unsweetened Cocoa Powder', 0.25),
	('Vanilla', 'Fine Sea Salt', 0.5),
	('Vanilla', 'Egg Yolk', 15),
	('Oreo', 'Heavy Cream', 2.5),
	('Oreo', 'Whole Milk', 1.25),
	('Oreo', 'Granulated Sugar', 0.75),
	('Oreo', 'Unsweetened Cocoa Powder', 0.25),
	('Oreo', 'Fine Sea Salt', 0.5),
	('Oreo', 'Egg Yolk', 15),
	('Oreo', 'Oreo Cookies', 1.5),
	('Strawberry', 'Heavy Cream', 2.5),
	('Strawberry', 'Whole Milk', 1.25),
	('Strawberry', 'Granulated Sugar', 0.5),
	('Strawberry', 'Unsweetened Cocoa Powder', 0.25),
	('Strawberry', 'Fine Sea Salt', 0.5),
	('Strawberry', 'Egg Yolk', 15),
	('Strawberry', 'Fresh Strawberries', 2),
	('Strawberry', 'Lemon Juice', 4);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
