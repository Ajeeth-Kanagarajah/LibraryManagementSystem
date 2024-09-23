-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 23, 2024 at 08:53 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library2`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `itemId` int(11) NOT NULL,
  `author` varchar(255) NOT NULL CHECK (`author` <> ''),
  `genre` varchar(255) DEFAULT NULL,
  `isbn` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`itemId`, `author`, `genre`, `isbn`) VALUES
(1, 'Man/Of/Action', 'sci-fi', '2323'),
(2, 'Anakin/Skywalker', 'Sci-Fi/Action/Thriller', '33632202'),
(4, 'Robert/Kiyosaki', 'personal/development', '9781612681139'),
(7, 'Kalki', 'History', '29101950'),
(8, 'Fyodor/Dostoevsky', '9781784871970', '9781784871970');

-- --------------------------------------------------------

--
-- Table structure for table `borrowinghistory`
--

CREATE TABLE `borrowinghistory` (
  `borrowId` int(11) NOT NULL,
  `itemId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `borrowDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `returnDate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrowinghistory`
--

INSERT INTO `borrowinghistory` (`borrowId`, `itemId`, `userId`, `borrowDate`, `returnDate`) VALUES
(2, 2, 12, '2024-09-23 15:22:45', '2024-09-23 15:22:45'),
(3, 2, 13, '2024-09-23 15:25:44', '2024-09-23 15:25:44'),
(4, 3, 12, '2024-09-23 15:45:49', '2024-09-23 15:45:49'),
(5, 2, 12, '2024-09-23 15:46:42', '2024-09-23 15:46:42'),
(6, 3, 14, '2024-09-23 15:47:00', '2024-09-23 15:47:00'),
(7, 4, 14, '2024-09-23 15:54:55', '2024-09-23 15:54:55'),
(9, 4, 15, '2024-09-23 15:58:47', '2024-09-23 15:58:47'),
(10, 4, 16, '2024-09-23 15:59:28', '2024-09-23 15:59:28'),
(12, 6, 14, '2024-09-23 17:25:20', '2024-09-23 17:25:20'),
(13, 7, 13, '2024-09-23 17:33:24', '2024-09-23 17:33:24'),
(14, 4, 13, '2024-09-23 18:47:16', '2024-09-23 18:47:16');

--
-- Triggers `borrowinghistory`
--
DELIMITER $$
CREATE TRIGGER `update_return_date` AFTER UPDATE ON `borrowinghistory` FOR EACH ROW BEGIN
    IF NEW.returnDate IS NOT NULL THEN
        UPDATE Item
        SET available = TRUE
        WHERE itemId = NEW.itemId;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `dvd`
--

CREATE TABLE `dvd` (
  `itemId` int(11) NOT NULL,
  `director` varchar(255) NOT NULL CHECK (`director` <> ''),
  `duration` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dvd`
--

INSERT INTO `dvd` (`itemId`, `director`, `duration`) VALUES
(3, 'Kyle/Ren', 98),
(5, 'Bear/Grylls', 60),
(6, '300', 300);

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `itemId` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `available` tinyint(1) DEFAULT 1,
  `type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`itemId`, `title`, `available`, `type`) VALUES
(1, 'Ben/10', 1, 'Book'),
(2, 'Rough/One', 1, 'Book'),
(3, 'Star/Wars/Clone/War', 1, 'DVD'),
(4, 'Rich/Dad/Poor/Dad', 0, 'Book'),
(5, 'man vs wild', 0, 'DVD'),
(6, 'National/Geographic/Kids', 1, 'DVD'),
(7, 'Ponniyin/Selvsn', 1, 'Book'),
(8, 'Crime and Punishment', 1, 'Book');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userId` int(11) NOT NULL,
  `userName` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userId`, `userName`, `password`) VALUES
(12, 'Unknown User', 'defaultpassword'),
(13, 'ajeeth', '1234'),
(14, 'ak', '23'),
(15, 'AK', '12344'),
(16, 'VIJAY', 'VIJAY');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`itemId`);

--
-- Indexes for table `borrowinghistory`
--
ALTER TABLE `borrowinghistory`
  ADD PRIMARY KEY (`borrowId`),
  ADD KEY `itemId` (`itemId`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `dvd`
--
ALTER TABLE `dvd`
  ADD PRIMARY KEY (`itemId`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`itemId`),
  ADD UNIQUE KEY `title` (`title`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `borrowinghistory`
--
ALTER TABLE `borrowinghistory`
  MODIFY `borrowId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `item`
--
ALTER TABLE `item`
  MODIFY `itemId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `book_ibfk_1` FOREIGN KEY (`itemId`) REFERENCES `item` (`itemId`);

--
-- Constraints for table `borrowinghistory`
--
ALTER TABLE `borrowinghistory`
  ADD CONSTRAINT `borrowinghistory_ibfk_1` FOREIGN KEY (`itemId`) REFERENCES `item` (`itemId`),
  ADD CONSTRAINT `borrowinghistory_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`);

--
-- Constraints for table `dvd`
--
ALTER TABLE `dvd`
  ADD CONSTRAINT `dvd_ibfk_1` FOREIGN KEY (`itemId`) REFERENCES `item` (`itemId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
