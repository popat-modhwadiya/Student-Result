-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 01, 2024 at 01:14 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ajava`
--

-- --------------------------------------------------------

--
-- Table structure for table `student_result`
--

CREATE TABLE `student_result` (
  `Eno` bigint(12) NOT NULL,
  `Sname` varchar(11) NOT NULL,
  `AJAVA` int(11) NOT NULL,
  `NMA` int(11) NOT NULL,
  `MCOM` int(11) NOT NULL,
  `Status` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student_result`
--

INSERT INTO `student_result` (`Eno`, `Sname`, `AJAVA`, `NMA`, `MCOM`, `Status`) VALUES
(206270307020, 'Uday', 20, 20, 21, 'pass'),
(206270307021, 'Kuldip', 21, 24, 23, 'pass'),
(206270307022, 'Popat', 30, 27, 30, 'pass'),
(206270307023, 'Bharat', 15, 17, 15, 'pass'),
(206270307024, 'Vivek', 24, 28, 27, 'pass'),
(206270307026, 'Hardik', 28, 20, 28, 'pass'),
(206270307028, 'Nirmal', 14, 14, 17, 'pass'),
(206270307029, 'Isha', 12, 14, 19, 'pass'),
(206270307030, 'Tanvi', 23, 21, 25, 'pass'),
(206270307031, 'Rishi', 21, 23, 20, 'pass'),
(206270307032, 'Utsahi', 18, 28, 23, 'pass'),
(206270307034, 'Yash', 12, 16, 13, 'pass'),
(206270307035, 'Harsh', 22, 26, 26, 'pass'),
(206270307037, 'Jay', 14, 17, 15, 'pass'),
(206270307038, 'Maitri', 29, 21, 28, 'pass');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
