-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 08, 2018 at 01:35 PM
-- Server version: 5.7.23-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Table structure for table `businesses`
--

CREATE TABLE `businesses` (
  `businessID` int(10) NOT NULL,
  `userID` varchar(28) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `phoneNum` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `businesses`
--

INSERT INTO `businesses` (`businessID`, `userID`, `description`, `phoneNum`) VALUES
(1, '12', 'Fictional Seneca@York store', '555 555-5555');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `categoryID` int(10) UNSIGNED NOT NULL,
  `userID` varchar(28) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(150) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`categoryID`, `userID`, `name`) VALUES
(1, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Aha ahaha'),
(3, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'testMe');

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `itemID` int(10) UNSIGNED NOT NULL,
  `userID` varchar(28) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `price` float UNSIGNED NOT NULL DEFAULT '0',
  `description` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPC` bigint(20) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`itemID`, `userID`, `name`, `price`, `description`, `UPC`) VALUES
(404, 'hzyyjwWPzHRslVO6TbycuC5gCuz2', 'Book', 12, '', NULL),
(405, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'Item1', 20, '', NULL),
(406, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'item2', 30, '', NULL),
(407, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'gjoo', 2, '', NULL),
(408, 'hzyyjwWPzHRslVO6TbycuC5gCuz2', 'Paper', 5, '', NULL),
(409, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', '005910000154 S D', 1.47, '', NULL),
(410, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', '000000004053K s 9', 2.51, '', NULL),
(411, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'VISA TEND ', 27.74, '', NULL),
(412, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', ' D FARMERS MAR 062891517342', 5.77, '', NULL),
(413, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', ' D GV SALMON 062891523590', 9.97, '', NULL),
(414, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', '3 AT LEMONS', 0.87, '', NULL),
(415, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'Captain Crunch', 5.99, '', NULL),
(416, 'hzyyjwWPzHRslVO6TbycuC5gCuz2', 'surface', 999, '', NULL),
(417, 'hzyyjwWPzHRslVO6TbycuC5gCuz2', 'Macbook', 2499, '', NULL),
(418, 'hzyyjwWPzHRslVO6TbycuC5gCuz2', 'Ipad', 699, '', NULL),
(419, 'yprTEq2IL5bH6LLQmeUyiNTUnoC2', 'honey', 5.99, '', NULL),
(420, 'yprTEq2IL5bH6LLQmeUyiNTUnoC2', 'fish', 17.81, '', NULL),
(421, 'yprTEq2IL5bH6LLQmeUyiNTUnoC2', 'iHUNGRYMANSPECIAL', 12.99, '', NULL),
(422, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'wooden sword', 5, '', NULL),
(423, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'bronze helm', 2, '', NULL),
(428, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 4', 100, 'Test Description', NULL),
(429, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(876, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(877, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(878, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(879, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(880, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(881, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(882, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(883, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(884, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(885, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(886, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(887, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(888, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(889, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(890, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(891, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(892, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(893, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(894, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(895, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(896, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(897, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(898, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(899, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(900, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(901, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(902, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(903, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(904, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(905, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(906, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(907, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(908, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(909, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(910, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(911, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(912, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(913, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(914, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(915, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(916, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(917, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(918, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(919, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(920, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(921, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(922, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(923, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(924, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(925, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(926, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(927, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(928, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(929, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(930, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(931, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(932, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(933, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(934, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(935, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(936, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(937, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(938, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(939, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(940, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(941, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(942, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(943, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(944, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(945, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(946, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(947, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(948, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(949, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(950, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(951, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(952, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(953, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(954, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(955, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(956, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(957, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(958, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(959, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(960, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(961, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(962, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(963, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(964, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 5', 200, 'Test Description 2', NULL),
(965, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 1', 100, 'Test Description', NULL),
(966, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 2', 200, 'Test Description 2', NULL),
(967, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 1', 100, 'Test Description', NULL),
(968, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Test Item 2', 200, 'Test Description 2', NULL),
(969, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Demo Item 1', 100, '', NULL),
(970, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Demo Item 2', 100, '', NULL),
(971, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Demo Item 3', 100, '', NULL),
(972, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Demo Item 4', 100, '', NULL),
(973, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Demo Item 5', 100, '', NULL),
(974, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Demo Item 6', 100, '', NULL),
(976, 'hzyyjwWPzHRslVO6TbycuC5gCuz2', 'Shampoo', 8.99, '', NULL),
(977, 'hzyyjwWPzHRslVO6TbycuC5gCuz2', 'Surface Pro', 1899, '', NULL),
(979, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Surface Pro', 1899, '', NULL),
(980, 'hzyyjwWPzHRslVO6TbycuC5gCuz2', 'Surface', 1999, '', NULL),
(981, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Surface', 1999, '', NULL),
(982, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Surface PRO', 1999, '', NULL),
(983, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Dell XPS 15', 2599, '', NULL),
(984, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Surface', 1799, '', NULL),
(988, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Surface', 1999, '', NULL),
(989, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'notebook', 2.99, '', NULL),
(990, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'shampoo', 8.99, '', NULL),
(991, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Google drive', 2.99, '', NULL),
(992, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Google Glass', 1299, '', NULL),
(993, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Hamburger', 5.99, '', NULL),
(994, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Surface', 1999, '', NULL),
(995, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'gf', 99999, '', NULL),
(996, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'DBA', 89, '', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `receiptItems`
--

CREATE TABLE `receiptItems` (
  `receiptID` int(10) UNSIGNED NOT NULL,
  `itemID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `receiptItems`
--

INSERT INTO `receiptItems` (`receiptID`, `itemID`) VALUES
(255, 428),
(255, 429),
(255, 876),
(255, 877),
(255, 878),
(255, 879),
(255, 880),
(255, 881),
(255, 882),
(255, 883),
(255, 884),
(255, 885),
(255, 886),
(255, 887),
(255, 888),
(255, 889),
(255, 890),
(255, 891),
(255, 892),
(255, 893),
(255, 894),
(255, 895),
(255, 896),
(255, 897),
(255, 898),
(255, 899),
(255, 900),
(255, 901),
(255, 902),
(255, 903),
(255, 904),
(255, 905),
(255, 906),
(255, 907),
(255, 908),
(255, 909),
(255, 910),
(255, 911),
(255, 912),
(255, 913),
(255, 914),
(255, 915),
(255, 916),
(255, 917),
(255, 918),
(255, 919),
(255, 920),
(255, 921),
(255, 922),
(255, 923),
(255, 924),
(255, 925),
(255, 926),
(255, 927),
(255, 928),
(255, 929),
(255, 930),
(255, 931),
(255, 932),
(255, 933),
(255, 934),
(255, 935),
(255, 936),
(255, 937),
(255, 938),
(255, 939),
(255, 940),
(255, 941),
(255, 942),
(255, 943),
(255, 944),
(255, 945),
(255, 946),
(255, 947),
(255, 948),
(255, 949),
(255, 950),
(255, 951),
(255, 952),
(255, 953),
(255, 954),
(255, 955),
(255, 956),
(255, 957),
(255, 958),
(255, 959),
(255, 960),
(255, 961),
(255, 962),
(255, 963),
(255, 964),
(265, 406),
(266, 407),
(271, 419),
(271, 420),
(272, 421),
(273, 422),
(273, 423),
(273, 995),
(274, 965),
(274, 966),
(275, 967),
(275, 968),
(276, 969),
(277, 970),
(278, 971),
(279, 972),
(280, 973),
(281, 974),
(285, 980),
(289, 988),
(290, 989),
(290, 990),
(291, 991),
(291, 992),
(292, 993),
(295, 996);

-- --------------------------------------------------------

--
-- Table structure for table `receipts`
--

CREATE TABLE `receipts` (
  `receiptID` int(10) UNSIGNED NOT NULL,
  `userID` varchar(28) COLLATE utf8_unicode_ci NOT NULL,
  `businessName` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creationDate` date NOT NULL,
  `totalCost` float UNSIGNED NOT NULL DEFAULT '0',
  `tax` float UNSIGNED NOT NULL DEFAULT '0',
  `description` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `categoryName` varchar(28) COLLATE utf8_unicode_ci DEFAULT NULL,
  `categoryID` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `receipts`
--

INSERT INTO `receipts` (`receiptID`, `userID`, `businessName`, `creationDate`, `totalCost`, `tax`, `description`, `categoryName`, `categoryID`) VALUES
(255, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Last Year', '2017-12-31', 0.01, 14, NULL, 'Last year', NULL),
(256, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'January', '2018-01-01', 0.1, 14, NULL, 'January 2018', NULL),
(257, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'June Start', '2018-06-01', 1, 14, NULL, 'June 2018', NULL),
(258, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'June End', '2018-06-30', 10, 14, NULL, 'June 2018', NULL),
(259, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Last Week Start', '2018-07-15', 100, 14, NULL, 'Last week', NULL),
(260, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Last Week End', '2018-07-21', 1000, 14, NULL, 'Last week', NULL),
(261, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'This Week Start', '2018-07-27', 10000, 14, NULL, 'This week', NULL),
(262, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'Today', '2018-07-28', 100000, 14, NULL, 'This week', NULL),
(263, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'This Week End', '2018-07-28', 1000000, 14, NULL, 'This week', NULL),
(265, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'Falador', '2018-07-28', 5.9, 14, NULL, 'Retail', NULL),
(266, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'Varrock', '2018-07-22', 2, 14, NULL, 'Retail', NULL),
(271, 'yprTEq2IL5bH6LLQmeUyiNTUnoC2', 'Metro', '2018-07-30', 44.51, 14, NULL, 'Retail', NULL),
(272, 'yprTEq2IL5bH6LLQmeUyiNTUnoC2', 'Reginos Pizza', '2018-07-30', 14.68, 14, NULL, 'Retail', NULL),
(273, 'UosCJdzXQfhaHzFudu4DtZjMYFg2', 'Lumbridge', '2018-07-30', 30000, 14, NULL, 'Noob items', NULL),
(274, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'test', '2018-07-09', 1080, 10, NULL, 'AddTest', NULL),
(275, '3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'test', '2018-07-09', 1080, 10, NULL, 'AddTest', NULL),
(276, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Justins Store', '2018-07-31', 100, 14, NULL, 'Demo', NULL),
(277, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Justins Store', '2018-07-24', 100, 14, NULL, 'Last week', NULL),
(278, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Justins Store', '2018-07-03', 100, 14, NULL, 'This month', NULL),
(279, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Justins Store', '2018-06-05', 100, 14, NULL, 'Last month', NULL),
(280, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Justins Store', '2018-01-01', 100, 14, NULL, 'Early this year', NULL),
(281, 'sjGXlh7P0LRutyRz9MO5719YCRp1', 'Last Minute Boxing Store', '2017-12-26', 100, 14, NULL, 'Last year', NULL),
(285, 'hzyyjwWPzHRslVO6TbycuC5gCuz2', 'Microsoft', '2018-07-31', 1999, 14, NULL, 'It', NULL),
(289, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Microsoft', '2018-07-31', 1999, 14, NULL, 'It', NULL),
(290, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Walmart', '2018-07-01', 11.98, 14, NULL, 'Retail', NULL),
(291, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Google', '2018-07-25', 1301.99, 14, NULL, 'It', NULL),
(292, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Mc Donald', '2018-07-31', 5.99, 14, NULL, 'Food', NULL),
(295, 'ojr7D60DAcdTTgP44AWZnhhFrng1', 'Seneca', '2018-08-01', 89, 14, NULL, 'Retail', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `test`
--

CREATE TABLE `test` (
  `id` int(8) NOT NULL,
  `userName` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `comment` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='This is a table to test java sql connection';

--
-- Dumping data for table `test`
--

INSERT INTO `test` (`id`, `userName`, `name`, `created`, `comment`) VALUES
(1, 'JohnDoe', 'John Doe', '2018-03-28 05:00:00', 'Connected to VM!'),
(2, 'JaneDoe', 'Jane Doe', '2018-03-29 00:40:27', 'Goodbye World');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userID` varchar(28) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `creationDate` date NOT NULL,
  `photo` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `name`, `creationDate`, `photo`, `email`) VALUES
('09YKhUIyVqXxiYZpQZsVv9evkLl2', 'registration test', '2018-06-19', NULL, 'justin.e.vuu@gmail.com'),
('0MVZHGOBOwg9cEIbA34Vk7xhdct2', 'alistairgodwin', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('1', 'John Doe', '2018-04-09', NULL, NULL),
('12', 'SeneMart', '2018-05-23', NULL, 'senemart@example.com'),
('13', 'Freddy', '2018-05-30', NULL, NULL),
('17', 'alistairgodwin', '2018-05-31', NULL, 'alistairgodwin@hotmail.com'),
('2', 'Jane Doe', '2018-04-09', '', ''),
('3ZRDD2RM16aKrWEWw14EHrrX1Lo1', 'registration test', '2018-06-19', NULL, 'daofujai@gmail.com'),
('4UqPmuA3JCPOxO9psAvkCFkeMm13', 'registration test', '2018-06-19', NULL, 'justin.e.vuu@gmail.com'),
('5', 'Justin', '2018-04-09', NULL, NULL),
('7gdJHfQzPKZr6Pig497pOLF4n933', 'alistairgodwin', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('9gi0GZnK1ISw8wv1Q0ZyBVglwG02', 'registration test', '2018-06-19', NULL, 'godwinalistair@hotmail.com'),
('AgCM2luf3UXFQecWFwnmpTTzXYb2', 'jevuu', '2018-07-31', NULL, 'jevuu@myseneca.ca'),
('AlnritRCjCZsMRlyQlcJrkqq0ib2', 'Friendly Mobile User :)', '2018-07-29', NULL, '+16135322248'),
('bN2NOrzdOfdVPTaz1CYLadLUIp33', 'alistairgodwin', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('Fa5JvFDUu9PkDBCIMxML6e4EtY83', 'registration test', '2018-06-19', NULL, 'alistairgodwin@hotmail.com'),
('fcXcWEP2a6cyRNF28CWgNUnnvAA2', 'Friendly Mobile User :)', '2018-07-24', NULL, '+16475511418'),
('GOFJT2l2rvWG6UdkJrhYxCtuhG13', 'E-ceipt user', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('hzyyjwWPzHRslVO6TbycuC5gCuz2', 'registration test', '2018-06-19', NULL, 'kignorchan@gmail.com'),
('L4MSs94mEuScPdGy8RK10Vt2eqv1', 'Calum Godwin', '2018-07-22', NULL, 'calumgodwin@gmail.com'),
('LITippHiQcbZ42PfW6OozPZa0aV2', 'E-ceipt user', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('m64PtVP3AVYL1jtity8WqCNoB7G3', 'registration test', '2018-06-19', NULL, 'godwinalistair@hotmail.com'),
('mJVQHuoF63dU0HI7EfkAE9dB3hJ3', 'E-ceipt user', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('NHNk3Yqyw9h9pE6ssAlhmhsIMNM2', 'Friendly Mobile User :)', '2018-07-23', NULL, '+16134535468'),
('o2PwltUrXGUtxPvlrMfdAMAdb2i1', 'E-ceipt user', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('ojr7D60DAcdTTgP44AWZnhhFrng1', 'E-ceipt user', '2018-07-31', NULL, 'qchen102@myseneca.ca'),
('pwEJ1gfhebXX0hlQuFkvUjDtKAq1', 'E-ceipt user', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('r1nbAbbV4Mbm8SKAeuidECqdCfq2', 'E-ceipt user', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('regTest', 'E-ceipt user', '2018-07-22', NULL, 'regTest@example.com'),
('S7rUfoy6SKhAbqk82Fw8fWotkut1', 'E-ceipt user', '2018-07-29', NULL, 'alistairgodwin@hotmail.com'),
('sjGXlh7P0LRutyRz9MO5719YCRp1', 'jevuu', '2018-07-31', NULL, 'jevuu@myseneca.ca'),
('tRnnpUgmTnXFBU5vEIaAYb8Opai1', 'Friendly Mobile User :)', '2018-07-22', NULL, '+16135322248'),
('UosCJdzXQfhaHzFudu4DtZjMYFg2', 'registration test', '2018-06-19', NULL, 'agodwin@myseneca.ca'),
('y1GvddPWFAPQ9XpG5Yj63lUu1573', 'registration test', '2018-06-20', NULL, 'emile.ohan@senecacollege.ca'),
('yprTEq2IL5bH6LLQmeUyiNTUnoC2', 'alistairgodwin', '2018-07-29', NULL, 'alistairgodwin@hotmail.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `businesses`
--
ALTER TABLE `businesses`
  ADD PRIMARY KEY (`businessID`),
  ADD KEY `BusinessID` (`userID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`categoryID`),
  ADD KEY `userID` (`userID`),
  ADD KEY `userID_2` (`userID`),
  ADD KEY `userID_3` (`userID`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`itemID`,`userID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `receiptItems`
--
ALTER TABLE `receiptItems`
  ADD UNIQUE KEY `receiptID_2` (`receiptID`,`itemID`),
  ADD UNIQUE KEY `receiptID_3` (`receiptID`,`itemID`),
  ADD KEY `receiptID` (`receiptID`),
  ADD KEY `itemID` (`itemID`);

--
-- Indexes for table `receipts`
--
ALTER TABLE `receipts`
  ADD PRIMARY KEY (`receiptID`),
  ADD KEY `userID` (`userID`),
  ADD KEY `categoryID` (`categoryID`);

--
-- Indexes for table `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `userName` (`userName`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `businesses`
--
ALTER TABLE `businesses`
  MODIFY `businessID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `categoryID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `itemID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=997;
--
-- AUTO_INCREMENT for table `receipts`
--
ALTER TABLE `receipts`
  MODIFY `receiptID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=296;
--
-- AUTO_INCREMENT for table `test`
--
ALTER TABLE `test`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `businesses`
--
ALTER TABLE `businesses`
  ADD CONSTRAINT `businesses_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `items`
--
ALTER TABLE `items`
  ADD CONSTRAINT `items_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `receiptItems`
--
ALTER TABLE `receiptItems`
  ADD CONSTRAINT `receiptItems_ibfk_1` FOREIGN KEY (`receiptID`) REFERENCES `receipts` (`receiptID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `receiptItems_ibfk_2` FOREIGN KEY (`itemID`) REFERENCES `items` (`itemID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `receipts`
--
ALTER TABLE `receipts`
  ADD CONSTRAINT `receipts_ibfk_2` FOREIGN KEY (`categoryID`) REFERENCES `categories` (`categoryID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `receipts_ibfk_4` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
