-- phpMyAdmin SQL Dump
-- version 4.3.8
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 16, 2018 at 09:58 AM
-- Server version: 5.5.51-38.2
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `panzooka_prj566`
--

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`itemID`, `userID`, `name`, `price`, `description`, `UPC`) VALUES
(1, 1, 'Ohan Bananas', 100, 'Bananas from Ohan''s banana farm', 612345678905),
(2, 1, 'PRJ566 Final Document', 200, 'Hey it''s something', 609876543215),
(3, 2, 'e-Ceipt App', 200, 'This app', 610293847565);

--
-- Dumping data for table `receiptItems`
--

INSERT INTO `receiptItems` (`receiptID`, `itemID`) VALUES
(1, 1),
(1, 2),
(2, 3);

--
-- Dumping data for table `receipts`
--

INSERT INTO `receipts` (`receiptID`, `userID`, `creationDate`, `totalCost`, `tax`, `description`, `categoryID`) VALUES
(1, 1, '2018-04-09', 0, 0, 'Trip to the college', NULL),
(2, 2, '2018-04-09', 0, 0, 'Jane''s Receipt', NULL);

--
-- Dumping data for table `test`
--

INSERT INTO `test` (`id`, `userName`, `name`, `created`, `comment`) VALUES
(1, 'JohnDoe', 'John Doe', '2018-03-28 05:00:00', 'Hello World'),
(2, 'JaneDoe', 'Jane Doe', '2018-03-29 00:40:27', 'Goodbye World');

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `name`, `creationDate`, `photo`, `email`) VALUES
(1, 'John Doe', '2018-04-09', NULL, NULL),
(2, 'Jane Doe', '2018-04-09', '', ''),
(5, 'Justin', '2018-04-09', NULL, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
