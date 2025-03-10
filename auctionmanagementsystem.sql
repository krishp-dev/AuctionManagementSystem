-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 24, 2024 at 10:37 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `auctionmanagementsystem`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertBid` (IN `p_userId` INT, IN `p_itemId` INT, IN `p_bidAmount` DOUBLE)   BEGIN
    INSERT INTO bids (userId, itemId, bidAmount) 
    VALUES (p_userId, p_itemId, p_bidAmount);
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `adminId` int(11) NOT NULL,
  `adminName` varchar(100) NOT NULL,
  `adminEmail` varchar(100) NOT NULL,
  `adminPassword` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`adminId`, `adminName`, `adminEmail`, `adminPassword`) VALUES
(18, 'deepPanchal', 'deepanchal2290@gmail.com', 'deep9587');

-- --------------------------------------------------------

--
-- Table structure for table `auctiondetails`
--

CREATE TABLE `auctiondetails` (
  `auctionId` int(11) NOT NULL,
  `auctionName` varchar(100) NOT NULL,
  `auctionDuration` int(11) NOT NULL,
  `isActive` tinyint(1) NOT NULL,
  `numberOfUsers` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `auctiondetails`
--

INSERT INTO `auctiondetails` (`auctionId`, `auctionName`, `auctionDuration`, `isActive`, `numberOfUsers`) VALUES
(1, 'Diamond House ', 1, 0, 3),
(2, 'mm', 1, 0, 2),
(18, 'Russians', 1, 1, 2),
(44, '44', 1, 0, 3),
(55, '55', 1, 0, 2);

-- --------------------------------------------------------

--
-- Table structure for table `auctionitemmapping`
--

CREATE TABLE `auctionitemmapping` (
  `auctionId` int(11) NOT NULL,
  `sellerId` int(11) NOT NULL,
  `itemId` int(11) NOT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `auctionitemmapping`
--

INSERT INTO `auctionitemmapping` (`auctionId`, `sellerId`, `itemId`, `id`) VALUES
(1, 12, 99, 1),
(2, 1, 2, 2),
(44, 1, 23, 3),
(55, 1, 4, 4),
(18, 1, 12, 5);

-- --------------------------------------------------------

--
-- Table structure for table `bids`
--

CREATE TABLE `bids` (
  `bidId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `itemId` int(11) NOT NULL,
  `bidAmount` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bids`
--

INSERT INTO `bids` (`bidId`, `userId`, `itemId`, `bidAmount`) VALUES
(1, 5, 99, 500),
(2, 4, 99, 3000),
(3, 2, 99, 4000),
(4, 2, 99, 5000),
(5, 5, 99, 4000),
(6, 4, 99, 6000),
(7, 2, 99, 7000),
(8, 5, 99, 8000),
(9, 11, 23, 33),
(10, 22, 23, 44),
(11, 22, 23, 34),
(12, 22, 23, 44),
(13, 33, 23, 34),
(14, 11, 23, 43),
(15, 11, 23, 545),
(16, 33, 23, 455),
(17, 33, 23, 656),
(18, 22, 23, 6556),
(19, 33, 23, 5656),
(20, 11, 23, 5665),
(21, 22, 23, 6565),
(22, 11, 23, 6556),
(23, 33, 23, 5665),
(24, 33, 23, 6565),
(25, 22, 23, 6565),
(26, 33, 23, 6565),
(27, 11, 23, 6556),
(28, 11, 23, 7676),
(29, 33, 23, 6767),
(30, 33, 23, 6886),
(31, 33, 23, 9777),
(32, 33, 23, 43344),
(33, 33, 23, 34443),
(34, 22, 23, 53434),
(35, 22, 23, 63464),
(36, 22, 23, 46346),
(37, 11, 23, 36346),
(38, 22, 23, 3644334),
(39, 22, 23, 3463434),
(40, 11, 23, 364634),
(41, 11, 23, 63446),
(42, 22, 23, 64334),
(43, 33, 23, 34346),
(44, 11, 23, 34346),
(45, 33, 23, 3634),
(46, 22, 23, 3434),
(47, 11, 23, 343),
(48, 7, 4, 555),
(49, 6, 4, 666),
(50, 7, 4, 555),
(51, 6, 4, 666),
(52, 7, 4, 555),
(53, 6, 4, 6666),
(54, 7, 4, 55656),
(55, 6, 4, 65665),
(56, 7, 4, 656656),
(57, 6, 4, 656665),
(58, 7, 4, 656556),
(59, 6, 4, 565665),
(60, 7, 4, 5665656),
(61, 6, 4, 65656665),
(62, 7, 4, 65656),
(63, 6, 4, 56656),
(64, 7, 4, 66656),
(65, 6, 4, 6556656),
(66, 7, 4, 565665),
(67, 6, 4, 565666665),
(68, 7, 4, 656565),
(69, 6, 4, 565665),
(70, 7, 4, 56565),
(71, 6, 4, 565656),
(72, 7, 4, 56556),
(73, 6, 4, 65565),
(74, 7, 4, 566556),
(75, 6, 4, 565656),
(76, 7, 4, 566556),
(77, 6, 4, 56655),
(78, 7, 4, 565665),
(79, 6, 4, 565656),
(80, 12, 2, 2000),
(81, 13, 2, 3000),
(82, 12, 2, 100),
(83, 13, 2, 3000),
(84, 12, 2, 20200),
(85, 13, 2, 3000),
(86, 12, 2, 49999),
(87, 13, 2, 3000000),
(88, 12, 2, 100000000000),
(89, 13, 2, 2000000),
(90, 12, 2, 3000000),
(91, 13, 2, 3000000),
(92, 12, 2, 20000000),
(93, 13, 2, 399999999),
(94, 12, 2, 288888888),
(95, 13, 2, 399999999),
(96, 12, 2, 40000000),
(97, 13, 2, 50000000),
(98, 12, 2, 60000000);

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `itemId` int(11) NOT NULL,
  `itemName` varchar(100) NOT NULL,
  `auctionid` int(11) NOT NULL,
  `startBid` double NOT NULL,
  `sellerId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`itemId`, `itemName`, `auctionid`, `startBid`, `sellerId`) VALUES
(2, '2', 2, 33, 1),
(4, 'rr', 55, 334, 1),
(12, 'diamond', 18, 10000, 1),
(23, '23', 44, 23, 1),
(99, 'diamond', 1, 300.5, 12);

-- --------------------------------------------------------

--
-- Table structure for table `sellerdetails`
--

CREATE TABLE `sellerdetails` (
  `sellerId` int(11) NOT NULL,
  `sellerName` varchar(100) NOT NULL,
  `sellerEmail` varchar(100) NOT NULL,
  `sellerPassword` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sellerdetails`
--

INSERT INTO `sellerdetails` (`sellerId`, `sellerName`, `sellerEmail`, `sellerPassword`) VALUES
(11, 'Deep panchal', 'ssss@gmail.com', '2005'),
(12, 'kkk', 'kk@gmail.com', '2005'),
(1, '1', '1@gmail.com', '1'),
(3, '3', '3@gmail.com', '3');

-- --------------------------------------------------------

--
-- Table structure for table `userauctionmapping`
--

CREATE TABLE `userauctionmapping` (
  `mappingId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `auctionId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `userauctionmapping`
--

INSERT INTO `userauctionmapping` (`mappingId`, `userId`, `auctionId`) VALUES
(1, 2, 1),
(2, 4, 1),
(3, 5, 1),
(4, 11, 44),
(5, 22, 44),
(6, 33, 44),
(7, 6, 55),
(8, 7, 55),
(9, 12, 2),
(10, 13, 2);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userId` int(11) NOT NULL,
  `userName` varchar(100) NOT NULL,
  `userPassword` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userId`, `userName`, `userPassword`) VALUES
(1, 'e', '2'),
(2, 'r', '2'),
(3, 'ee', '3'),
(4, 't', '4'),
(5, 'g', '5'),
(6, '6', '6'),
(7, '7', '7'),
(11, '11', '11'),
(12, 'Anand', 'anand098'),
(13, 'goku', 'goku098'),
(22, '22', '22'),
(33, '33', '33');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `auctiondetails`
--
ALTER TABLE `auctiondetails`
  ADD PRIMARY KEY (`auctionId`);

--
-- Indexes for table `auctionitemmapping`
--
ALTER TABLE `auctionitemmapping`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bids`
--
ALTER TABLE `bids`
  ADD PRIMARY KEY (`bidId`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`itemId`);

--
-- Indexes for table `userauctionmapping`
--
ALTER TABLE `userauctionmapping`
  ADD PRIMARY KEY (`mappingId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `auctionitemmapping`
--
ALTER TABLE `auctionitemmapping`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `bids`
--
ALTER TABLE `bids`
  MODIFY `bidId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=99;

--
-- AUTO_INCREMENT for table `userauctionmapping`
--
ALTER TABLE `userauctionmapping`
  MODIFY `mappingId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
