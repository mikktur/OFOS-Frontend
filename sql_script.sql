--
-- Table structure for table `Categories`
--

CREATE TABLE `Categories` (
  `CategoryID` int(11) NOT NULL,
  `CategoryName` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `Categories`
--

INSERT INTO `Categories` (`CategoryID`, `CategoryName`) VALUES
(1, 'Burger'),
(2, 'Pizza'),
(4, 'Steak');

-- --------------------------------------------------------

--
-- Table structure for table `ContactInfo`
--

CREATE TABLE `ContactInfo` (
  `PhoneNumber` varchar(15) NOT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `City` varchar(255) DEFAULT NULL,
  `FirstName` varchar(255) NOT NULL,
  `LastName` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `PostalCode` varchar(10) NOT NULL,
  `User_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `ContactInfo`
--

INSERT INTO `ContactInfo` (`PhoneNumber`, `Address`, `City`, `FirstName`, `LastName`, `Email`, `PostalCode`, `User_ID`) VALUES
('0401234567', 'Täällä asuu mikkee', 'Kouvola', 'Mikaeli', 'Testerinpoika', 'mikke@granlund.com', '02360', 8),
('123456789', 'Leppälinnunrinne', 'Espoo', 'Jimi', 'Pettilä', 'asdasd@gmail.com', '02620', 30),
('2', 'Evergreen Terrace', 'Springfield', 'Wanha', 'Osuja', 'jabadabadont@gmail.com', '00410', 36),
('4363637', 'Testikatu 3 A 3', 'Espoo', 'Jimi', 'Testeri', 'asdasd@gmail.com', '02370', 37),
('1245135352', 'Katuosoite 3 A 2', 'Espoo', 'Mikko', 'Testeri', 'asdsa@gmail.com', '02350', 38),
('12314145', 'Testikatu 8 A 9', 'Kerava', 'Just', 'Testing', 'sfafs@gmail.com', '19600', 39),
('3253522', 'osoite 1', 'Espoo', 'johannes', 'liikanen', 'afsasd@gmail.com', '02350', 41),
('04012345678', 'Testiteponkuja 3 A 1', 'Teppola', 'Testi', 'Teppo', 'teppo@gmail.com', '02200', 48),
('4', 'sadda', 'adsads', 'ds', 'ss', 'asf@gmail.com', '33333', 57),
('0451234567', 'Toimiikohan tää katu 8 a 3', 'Espoo', 'Uusin', 'Testeri', 'adsas@gmail.com', '02022', 58),
('03504030', 'katukatu 1 a 3', 'espoo', 'uus', 'akko', 'asf@gmail.com', '020202', 65),
('532252353', 'katulantie 2 a 1', 'espoo', 'seppo', 'seponpoika', 'adsas@gmail.com', '02020', 80);

-- --------------------------------------------------------

--
-- Table structure for table `DeliveryAddresses`
--

CREATE TABLE `DeliveryAddresses` (
  `StreetAddress` varchar(255) NOT NULL,
  `City` varchar(255) NOT NULL,
  `PostalCode` varchar(10) NOT NULL,
  `DeliveryAddressID` int(11) NOT NULL,
  `Info` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `DeliveryAddresses`
--

INSERT INTO `DeliveryAddresses` (`StreetAddress`, `City`, `PostalCode`, `DeliveryAddressID`, `Info`) VALUES
('Kekkilänkuja 3 A 8', 'Gegilä', '66600', 1, ''),
('Evergreen Terrace', 'Springfield', '00410', 2, ''),
('Main Street', 'Main', '11111', 6, ''),
('Eka toimitusoosite', 'Espoo', '04202', 20, 'ovikoodi xx'),
('Toka toimitusosoite', 'Helsinki', '02350', 21, 'ovikoodi zz'),
('Ensimmäinen osoite', 'Kerava', '16800', 23, 'ovikoodi yy'),
('testikatu', 'espoo', '02305', 37, 'asd'),
('toinen testikatu', 'kajaani', '03555', 38, 'asdsdd'),
('ei pitäis olla default', 'espoo', '33525', 40, 'asfafs'),
('ads', 'asdas', '1231', 62, 'asdadasd'),
('ads', 'asdas', '1231', 63, 'asdadasd'),
('Testiosoite', 'Testilä', '02020', 65, 'jätä ovelle'),
('saads', 'adsads', '053520', 67, 'adsdas'),
('toimitusosoite', 'kaupunki', '03030', 69, 'työnnä luukusta sisään'),
('tänne se kalapihvi', 'kalala', '02020', 102, 'Salty'),
('Katulankatu 3 B 81', 'Espoo', '02230', 122, 'Jee jee\n'),
('safkat tänne', 'espoo', '02032', 123, 'asdff'),
('katuosoite 3', 'kaupunki', '02020', 141, 'asd'),
('toinen osoite', 'kaupunki tääki', '03003', 142, 'asddd');

-- --------------------------------------------------------

--
-- Table structure for table `OrderProducts`
--

CREATE TABLE `OrderProducts` (
  `OrderID` int(11) NOT NULL,
  `ProductID` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `OrderProducts`
--

INSERT INTO `OrderProducts` (`OrderID`, `ProductID`, `quantity`) VALUES
(1, 13, NULL),
(4, 27, 0),
(5, 27, 0),
(6, 27, 5),
(7, 27, 5),
(8, 1, 1),
(9, 1, 1),
(10, 31, 3),
(10, 34, 7),
(12, 10, 1),
(13, 1, 1),
(14, 1, 1),
(16, 1, 1),
(16, 23, 1),
(17, 9, 1),
(18, 1, 1),
(19, 9, 1),
(20, 31, 1),
(20, 34, 1),
(22, 23, 1),
(23, 21, 1),
(24, 9, 1),
(24, 10, 5),
(25, 34, 1),
(26, 21, 1),
(27, 34, 1),
(28, 31, 1),
(28, 32, 1),
(28, 34, 1),
(28, 35, 1),
(29, 31, 2),
(29, 32, 1),
(29, 34, 2),
(29, 35, 3),
(30, 31, 1),
(30, 32, 1),
(30, 34, 1),
(31, 32, 1),
(31, 34, 1),
(32, 31, 1),
(32, 32, 1),
(32, 34, 1),
(32, 35, 1),
(32, 41, 1),
(33, 29, 1),
(33, 34, 1),
(34, 34, 3),
(35, 34, 1),
(36, 31, 2),
(36, 32, 2),
(37, 34, 1),
(38, 31, 1),
(39, 34, 1),
(40, 29, 1),
(41, 34, 1),
(42, 9, 1),
(46, 10, 1),
(47, 32, 1),
(53, 28, 1),
(54, 10, 1),
(60, 31, 1),
(60, 32, 1),
(61, 10, 1),
(64, 48, 1),
(75, 31, 3),
(75, 32, 2),
(75, 34, 2),
(75, 35, 2),
(75, 42, 2),
(76, 31, 1),
(76, 32, 1),
(76, 34, 3),
(77, 34, 1),
(78, 34, 6),
(78, 35, 8),
(78, 47, 4),
(79, 31, 1),
(79, 32, 1),
(79, 34, 1),
(80, 31, 1),
(80, 32, 1),
(80, 34, 1),
(81, 31, 1),
(82, 32, 1),
(83, 31, 1),
(83, 32, 1),
(83, 34, 1),
(84, 29, 1),
(85, 31, 1),
(85, 32, 1),
(85, 34, 1),
(85, 35, 1),
(85, 42, 1),
(85, 44, 1),
(86, 31, 1),
(86, 32, 1),
(86, 34, 1),
(86, 35, 1),
(86, 42, 1),
(86, 44, 1),
(87, 31, 1),
(88, 32, 1),
(89, 31, 1),
(89, 32, 2),
(90, 74, 1);

-- --------------------------------------------------------

--
-- Table structure for table `Orders`
--

CREATE TABLE `Orders` (
  `OrderID` int(11) NOT NULL,
  `State` varchar(255) NOT NULL,
  `OrderAddress` varchar(255) NOT NULL,
  `OrderDate` date NOT NULL DEFAULT current_timestamp(),
  `User_ID` int(11) NOT NULL,
  `RestaurantID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `Orders`
--

INSERT INTO `Orders` (`OrderID`, `State`, `OrderAddress`, `OrderDate`, `User_ID`, `RestaurantID`) VALUES
(1, 'Done', 'Kekkilänkuja 3 A 8', '2024-09-17', 30, 2),
(4, 'Done', 'Evergreen Terrace', '2024-09-24', 36, 2),
(5, 'Done', 'Evergreen Terrace', '2024-09-24', 36, 2),
(6, 'Ordered', 'Evergreen Terrace', '2024-09-24', 36, 2),
(7, 'Ordered', 'Evergreen Terrace', '2024-09-24', 36, 2),
(8, 'Ordered', 'jee jee', '2024-09-27', 8, 1),
(9, 'Ordered', 'uusi osoite', '2024-09-27', 8, 1),
(10, 'Preparing', 'Evergreen Terrace', '2024-10-05', 36, 9),
(12, 'Ordered', 'uusi osoite', '2024-10-05', 8, 1),
(13, 'Ordered', 'Testiteponkuja 3 A 1', '2024-10-05', 48, 1),
(14, 'Ordered', 'Testiteponkuja 3 A 1', '2024-10-05', 48, 2),
(16, 'Ordered', 'Uusin setti', '2024-10-06', 8, 2),
(17, 'Ordered', 'Uusin setti', '2024-10-06', 8, 3),
(18, 'Ordered', 'Uusin setti', '2024-10-06', 8, 2),
(19, 'Ordered', 'Uusin setti', '2024-10-06', 8, 3),
(20, 'Ordered', 'Uusin setti', '2024-10-06', 8, 9),
(22, 'Ordered', 'Uusin setti', '2024-10-06', 8, 2),
(23, 'Ordered', 'Uusin setti', '2024-10-06', 8, 1),
(24, 'Ordered', 'Uusin setti (voi myös muokata)', '2024-10-06', 8, 3),
(25, 'Ordered', 'Uusin setti (voi myös muokata)', '2024-10-06', 8, 9),
(26, 'Ordered', 'Uusin setti (voi myös muokata) <- Jep!', '2024-10-07', 8, 1),
(27, 'Ordered', 'Vakotie 69', '2024-10-07', 8, 1),
(28, 'Ordered', 'ads', '2024-10-08', 50, 1),
(29, 'Ordered', 'Uusin setti (voi myös muokata) <- Jep!', '2024-10-08', 8, 1),
(30, 'Ordered', 'Uusin setti (voi myös muokata) <- Jep!', '2024-10-08', 8, 1),
(31, 'Ordered', 'Uusin setti (voi myös muokata) <- Jep!', '2024-10-08', 8, 2),
(32, 'Ordered', 'Uusin setti (voi myös muokata) <- Jep!', '2024-10-08', 8, 1),
(33, 'Ordered', 'Testiosoite', '2024-10-08', 51, 3),
(34, 'Ordered', 'toimitusosoite', '2024-10-08', 53, 2),
(35, 'Ordered', 'Vakotie 69', '2024-10-25', 8, 2),
(36, 'Ordered', 'Vakotie 69', '2024-10-27', 8, 1),
(37, 'Ordered', 'uus osote', '2024-10-31', 8, 1),
(38, 'Ordered', 'uus osote', '2024-10-31', 8, 1),
(39, 'Ordered', 'uus osote', '2024-10-31', 8, 1),
(40, 'Ordered', 'tänne se kalapihvi', '2024-11-05', 56, 5),
(41, 'Ordered', 'dsadadasdsdasasdasdasdassdaasasdasdasdasdasdasdasdasdasdasdadasdasdasd', '2024-11-07', 8, 2),
(42, 'Ordered', 'Kissalankatu 3 A 9', '2024-11-07', 8, 3),
(46, 'Ordered', 'sdasd', '2024-11-13', 8, 3),
(47, 'Ordered', 'sdasd', '2024-11-13', 8, 1),
(53, 'Ordered', 'Katulankatu 3 B 81', '2024-11-13', 8, 10),
(54, 'Ordered', 'safkat tänne', '2024-11-13', 65, 3),
(60, 'Ordered', 'Katulankatu 3 B 81', '2024-11-14', 8, 1),
(61, 'Ordered', 'Katulankatu 3 B 81', '2024-11-14', 8, 3),
(64, 'Ordered', 'Adminin koti 7 C 7', '2024-11-16', 58, 3),
(75, 'Dunno', 'osoite 2', '2024-11-16', 8, 1),
(76, 'dunno', 'osoite 2', '2024-11-16', 8, 1),
(77, 'Ordered', 'Adminin koti 7 C 7', '2024-11-17', 58, 1),
(78, 'dunno', 'osoite 2', '2024-11-17', 8, 1),
(79, 'dunno', 'Katulankatu 3 B 81', '2024-11-19', 8, 1),
(80, 'dunno', 'Katulankatu 3 B 81', '2024-11-19', 8, 1),
(81, 'Ordered', 'dasads', '2024-11-19', 80, 1),
(82, 'Ordered', 'katuosoite 3', '2024-11-19', 80, 1),
(83, 'dunno', 'Katulankatu 3 B 81', '2024-11-25', 8, 1),
(84, 'Ordered', 'Katulankatu 3 B 81', '2024-11-27', 8, 10),
(85, 'dunno', 'Katulankatu 3 B 81', '2024-11-28', 8, 1),
(86, 'dunno', 'Katulankatu 3 B 81', '2024-11-28', 8, 1),
(87, 'dunno', 'Katulankatu 3 B 81', '2024-11-28', 8, 1),
(88, 'Ordered', 'Katulankatu 3 B 81', '2024-11-28', 8, 1),
(89, 'dunno', 'Katulankatu 3 B 81', '2024-11-28', 8, 1),
(90, 'dunno', 'Katulankatu 3 B 81', '2024-11-30', 8, 3);

-- --------------------------------------------------------

--
-- Table structure for table `Products`
--

CREATE TABLE `Products` (
  `ProductID` int(11) NOT NULL,
  `ProductName` varchar(255) NOT NULL,
  `ProductDesc` varchar(255) NOT NULL,
  `ProductPrice` decimal(10,2) NOT NULL,
  `Picture` varchar(255) NOT NULL,
  `Category` varchar(255) NOT NULL,
  `Active` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `Products`
--

INSERT INTO `Products` (`ProductID`, `ProductName`, `ProductDesc`, `ProductPrice`, `Picture`, `Category`, `Active`) VALUES
(1, 'Testi Hamppari', 'This is a test burger', 42.00, '', 'Burger', 0),
(7, 'Deleteable', 'Deleteable', 55.00, '', 'Deleteable', 0),
(9, 'Pizza bolognese', 'Pelkkä jauheliha.', 22.00, 'bolognese.jpg', 'Pizza', 1),
(10, 'Hawai', 'Kinkku-ananas namnam.', 22.50, 'hawaii.jpg', 'Pizza', 0),
(11, 'postmens burger', 'postmens eat burger', 78.00, 'https://www.allrecipes.com/thmb/5JVfA7MxfTUPfRerQMdF-nGKsLY=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/25473-the-perfect-basic-burger-DDMFS-4x3-56eaba3833fd4a26a82755bcd0be0c54.jpg', 'Burger', 0),
(12, 'postmens2 burger', 'postmens eat burger', 78.00, 'https://www.allrecipes.com/thmb/5JVfA7MxfTUPfRerQMdF-nGKsLY=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/25473-the-perfect-basic-burger-DDMFS-4x3-56eaba3833fd4a26a82755bcd0be0c54.jpg', 'Burger', 0),
(13, 'true burger', 'olispa active true', 78.00, 'https://www.allrecipes.com/thmb/5JVfA7MxfTUPfRerQMdF-nGKsLY=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/25473-the-perfect-basic-burger-DDMFS-4x3-56eaba3833fd4a26a82755bcd0be0c54.jpg', 'Burger', 0),
(14, 'true burger2', 'olispa active true', 8.00, 'https://www.allrecipes.com/thmb/5JVfA7MxfTUPfRerQMdF-nGKsLY=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/25473-the-perfect-basic-burger-DDMFS-4x3-56eaba3833fd4a26a82755bcd0be0c54.jpg', 'Burger', 1),
(19, 'false burger57', 'olispa active true', 19.99, 'https://www.allrecipes.com/thmb/5JVfA7MxfTUPfRerQMdF-nGKsLY=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/25473-the-perfect-basic-burger-DDMFS-4x3-56eaba3833fd4a26a82755bcd0be0c54.jpg', 'Burger', 1),
(20, 'false burger57', 'olispa active true', 8.00, 'https://www.allrecipes.com/thmb/5JVfA7MxfTUPfRerQMdF-nGKsLY=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/25473-the-perfect-basic-burger-DDMFS-4x3-56eaba3833fd4a26a82755bcd0be0c54.jpg', 'Burger', 1),
(21, 'hemobursa', 'namnam', 5.00, 'link.com', 'Burger', 1),
(22, 'testbrgr2', 'Kuvaus ja hinta päivitetty', 5556.00, 'link55fivesix.com', 'Burger', 1),
(23, 'Shrt Brgr', 'Lyhennetty burgeri', 88.00, 'link.com', 'Burger', 1),
(24, 'Long Brgr', 'Pidennetty burgeri', 88888.00, 'link.com', 'Burger', 1),
(26, 'Long Brgr7', 'Pidennetty burgeri', 88888.00, 'link.com', 'Burger', 1),
(27, 'Long Brgr72', 'Pidennetty burgeri', 88888.00, 'link.com', 'Burger', 1),
(28, 'Hyvä pihvi', 'Hyvä pihvi waguy jne', 50.00, 'lehma.jpg', 'Steak', 1),
(29, 'Kalapihvi', 'Tonnikala namnam', 49.99, 'tuna.jpg', 'Steak', 1),
(30, 'Porkkanapihvi', 'Porkkanasta jauhettu pihvi', 9.00, 'porkkanadippi.jpg', 'Steak', 1),
(31, 'Kanaburgeri', 'kana', 1.00, 'kanahamppari.jpg', 'Burger', 1),
(32, 'Työmiehen tosibursa', 'Vain tosimiehille ja muille', 6.00, 'peksa.jpg', 'Burger', 1),
(33, 'kanahamppari', 'kana', 5.00, 'kana.com', 'Burger', 0),
(34, 'Cheeseburger', 'A burger with cheese', 10.99, 'juustohamppari.jpg', 'Burger', 1),
(35, 'Veggieburger', 'Plant-based burger', 9.44, 'veggie.jpg', 'Burger', 1),
(36, 'Inactive burger', 'this is not active', 8.00, 'pic.com', 'Burger', 0),
(37, 'Jäätävä setti', 'Tällä lähtee nälkä', 21.00, 'kuva.com', 'Burger', 1),
(38, 'Lil\' burger', 'Pienempään nälkään', 3.00, 'joo.com', 'Burger', 0),
(39, 'Aurajuusto burger', 'Namnam', 6.00, 'aura.jpg', 'Burger', 1),
(40, 'Hopeless burger', 'Not working', 5.00, 'sadburger.jpg', 'Burger', 1),
(41, 'Crazyburger', 'Totally bananas', 13.00, 'burger.com', 'Burger', 1),
(42, 'Hopeful burger', 'Its working!', 8.00, 'happyburger.jpg', 'Burger', 1),
(43, 'Double cheeseburger', 'Burger with double the cheese', 8.00, 'double.jpg', 'Burger', 1),
(44, 'Pitkä burgeri', 'Pidennetty burgeri', 85.00, 'link.com', 'Burger', 1),
(45, 'asda', 'asd', 10.00, 'asd', 'asd', 0),
(46, 'asdadada', 'asddada', 10000.00, 'asd', 'asd', 1),
(47, 'Fish burger', 'Burger with fish', 8.00, 'fishburger.jpg', 'burger', 1),
(48, 'Kristus burgeri', 'Nam burgeri', 88855.00, 'link.com', 'Testable', 1),
(49, 'testi', 'testi', 5.00, 'onko.jpg', 'ei', 1),
(50, 'das', 'das', 2.00, 'e', 'a', 1),
(51, 'a', 'a', 2.00, 'a', 'a', 1),
(52, 'dsadasburgeri', 'Nam burgeri', 88855.00, 'link.com', 'Testable', 1),
(64, 'asd', 'asd', 10.00, 'asd', 'asd', 1),
(65, 'asdadad', 'asdasd', 100.00, 'asdad', 'asd', 1),
(67, 'asdad', 'asdas', 111.00, 'asd', 'asd', 1),
(73, 'eng name', 'eng desc', 1.00, 'asdas', 'Burger', 1),
(74, 'Americana pizza', 'Americana', 15.00, 'sadsdas', 'Pizza', 1),
(75, 'New burger', 'New burger description is here', 2.00, 'asda', 'Burger', 1),
(76, 'Newest burger', 'new new brgr', 20.00, 'url_is_here', 'Burger', 1),
(77, 'Language burger', 'Yapyap', 3.00, 'bur.com', 'burger', 1);

-- --------------------------------------------------------

--
-- Table structure for table `product_translations`
--

CREATE TABLE `product_translations` (
  `ProductID` int(11) NOT NULL,
  `Lang` varchar(2) NOT NULL,
  `Description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `product_translations`
--

INSERT INTO `product_translations` (`ProductID`, `Lang`, `Description`, `Name`) VALUES
(1, 'en', 'The first burger', 'Test burger'),
(1, 'fi', 'Ensimmäinen burgeri', 'Testi Hamppari'),
(1, 'ja', 'テストハンバーガー', 'テストハンバーガー'),
(1, 'ru', 'первый бургер', 'Тестовый бургер'),
(9, 'en', 'Minced meat pizza', 'Pizza bolognese'),
(9, 'fi', 'Pelkkä jauheliha pizza', 'Pizza bolognese'),
(9, 'ja', 'ボロネーゼピザ', 'ボロネーゼピザ'),
(9, 'ru', 'просто фарш', 'пицца болоньезе'),
(10, 'en', 'Ham and pineapple', 'Hawaii pizza'),
(10, 'fi', 'Kinkku-ananas pizza', 'Hawaii'),
(10, 'ja', 'ハムとパイナップルのピザ', 'ハワイ'),
(10, 'ru', 'ветчина и ананас', 'Гавайская пицца'),
(11, 'en', 'For the famished', 'Postmans burger'),
(11, 'fi', 'Tällä lähtee nälkä', 'Postimiehen burgeri'),
(11, 'ja', 'これはお腹が空く', '郵便屋さんのハンバーガー\r\n'),
(11, 'ru', 'это делает меня голодным', 'бургер почтальона'),
(12, 'en', 'For the extra famished', 'Postmans burger 2'),
(12, 'fi', 'Tällä lähtee nälkä paremmin', 'Postimiehen burgeri 2'),
(12, 'ja', 'これはお腹が空く', '郵便屋さんのハンバーガー 2\r\n'),
(12, 'ru', 'это делает меня голодным!', 'бургер почтальона 2'),
(21, 'en', 'So big', 'Hugeburger'),
(21, 'fi', 'Kaikista isoin', 'Hemobursa'),
(21, 'ja', 'すべての中で最大の', '本当に大きなハンバーガー\r\n'),
(21, 'ru', 'действительно большой', 'большой бургер'),
(22, 'en', 'For testing porpoises. DELETE LATER', 'Tested burger.'),
(22, 'fi', 'Testaukseen tarkoitettu burgeri2', 'Testiburgeri2'),
(23, 'en', 'Shorter than usual', 'Shortburger'),
(23, 'fi', 'Lyhennetty burgeri', 'Lyhytbursa'),
(23, 'ja', '短縮バーガー', 'ショートバーガー'),
(23, 'ru', 'в горшке', 'короткий бургер'),
(24, 'en', 'Longer than usual', 'Longburger'),
(24, 'fi', 'Tosi pitkä', 'Pitkäbursa'),
(24, 'ja', '拡張バーガー', 'ロングバーガー\r\n'),
(24, 'ru', 'дольше', 'длинный бургер'),
(27, 'en', 'FOR TEST', 'DELETE LATER'),
(28, 'en', 'Juicy steak', 'Steak'),
(28, 'fi', 'Laadukas pihvi', 'Maukas pihvi'),
(28, 'ja', '良いステーキ', '良いステーキ'),
(28, 'ru', 'вкусный стейк', 'стейк'),
(29, 'en', 'Juicy fish filet', 'Fish filet'),
(29, 'fi', 'Mehevä kalapihvi', 'Kalapihvi'),
(29, 'ja', 'ジューシーな魚のステーキ', '魚のステーキ\r\n'),
(29, 'ru', 'хороший рыбный стейк', 'рыбный стейк'),
(30, 'en', 'Carrots and a fresh dip', 'Carrots and dip'),
(30, 'fi', 'Porkkanaa ja dippiä', 'Porkkanaa'),
(30, 'ja', 'ニンジンとディップ', 'ニンジン'),
(30, 'ru', 'кусочки моркови и соус', 'морковь и соус'),
(31, 'en', 'Juicy chicken', 'Chicken'),
(31, 'fi', 'Kanaa', 'Kana'),
(31, 'ja', 'チキン', 'チキン\r\n'),
(31, 'ru', 'вкусная курица', 'курица'),
(32, 'en', 'For the working class', 'Workers burger'),
(32, 'fi', 'Ryömiehen tosibursa aijai', 'Työmiehen tosibursa'),
(32, 'ja', 'ワーカーズバーガーアイザット', '労働者のハンバーガー'),
(32, 'ru', 'для рабочего', 'бургер рабочего человека'),
(34, 'en', 'A burger with cheese', 'Cheeseburger'),
(34, 'fi', 'Hamppari juustolla', 'Juustohampurilainen'),
(34, 'ja', 'チーズ入りバーガー', 'チーズバーガー'),
(34, 'ru', 'бургер с сыром', 'чизбургер'),
(35, 'en', 'Plant-based burger', 'Veggieburger'),
(35, 'fi', 'Kasvipohjainen burgeri', 'Vegehampurilainen'),
(35, 'ja', '植物ベースのハンバーガー', 'ベジバーガー'),
(35, 'ru', 'растительный бургер', 'вегетарианский бургер'),
(36, 'en', 'This is not active', 'Inactive burger'),
(36, 'fi', 'Ei aktiivinen', 'Epäaktiivinen burgeri'),
(36, 'ja', 'これはアクティブではありません', 'アクティブなハンバーガーではありません\r\n'),
(36, 'ru', 'не активен', 'не активен Бургер'),
(38, 'en', 'Very small', 'Lil burger'),
(38, 'fi', 'snadi bursa', 'Pikku burgeri'),
(38, 'ja', '小さい', '小さなハンバーガー'),
(38, 'ru', 'очень маленький', 'маленький бургер'),
(40, 'en', 'Not working', 'Hopeless burger'),
(40, 'fi', 'Miksei tämä toimi', 'Toivoton burgeri'),
(40, 'ja', 'なぜこれが機能しないのですか?', '絶望的なハンバーガー\r\n'),
(40, 'ru', 'не работает', 'безнадежный бургер'),
(41, 'en', 'Totally bananas', 'Crazyburger'),
(41, 'fi', 'Aivan jäätävä', 'Hillitön burgeri'),
(41, 'ja', '絶対に凍える', '自由なハンバーガー'),
(41, 'ru', 'абсолютно замерзающий', 'безудержный бургер'),
(42, 'en', 'Its working!', 'Hopeful burger'),
(42, 'fi', 'nyt toimii', 'Toiveikas burgeri'),
(42, 'ja', '今は動作します', '希望に満ちたバーガー'),
(42, 'ru', 'теперь работаетт', 'обнадеживающий бургер'),
(43, 'en', 'Double the cheese', 'Doublecheese burger'),
(43, 'fi', 'kaksi juustoa', 'Tuplajuusto'),
(43, 'ja', 'チーズ2個', 'ダブルチーズバーガー\r\n'),
(43, 'ru', 'два сыра', 'двойной чизбургер'),
(44, 'en', 'The longest', 'The longburger'),
(44, 'fi', 'Tää on pitkä', 'Pitkäbursa'),
(44, 'ja', '本当に長い', 'ロングハンバーガー'),
(44, 'ru', 'очень долго', 'длинный гамбургер'),
(47, 'en', 'Burger with fish', 'Fish burger'),
(47, 'fi', 'Burgeri kalalla', 'Kala burgeri'),
(47, 'ja', '魚入りバーガー', 'フィッシュバーガー'),
(47, 'ru', 'бургер с рыбой', 'рыбный бургер'),
(48, 'en', 'Yummy burger', 'Christ Burger'),
(64, 'en', 'asd', 'asd'),
(65, 'fi', 'asdasd', 'asdadad'),
(67, 'en', 'asdas', 'asdad'),
(73, 'en', 'eng desc', 'eng name'),
(73, 'fi', 'fin desc', 'fin name'),
(73, 'ja', 'japp desc', 'japp name'),
(73, 'ru', 'rus desc', 'rus name'),
(74, 'en', 'Americana', 'Americana pizza'),
(74, 'fi', 'Amerikana', 'Amerikana pizza'),
(74, 'ja', 'Americana jap desc', 'Americana Jap'),
(74, 'ru', 'Americana rus desc', 'Americana rus'),
(75, 'en', 'New burger description is here', 'New burger'),
(75, 'fi', 'Uuden burgerin kuvaus', 'Uus burgeri'),
(75, 'ja', 'uuden bgr kuvaus', 'uus japsi burgeri'),
(75, 'ru', 'uus rus brg kuvaus', 'uus rus burgeri'),
(76, 'en', 'new new brgr', 'Newest burger'),
(76, 'fi', 'Uus uus burgeri', 'Uusin burgeri'),
(76, 'ja', 'kuvaus japaniks', 'uusin japani burgeri'),
(76, 'ru', 'kuvaus po ruski', 'uusin rus burger'),
(77, 'en', 'Yapyap', 'Language burger'),
(77, 'fi', '', ''),
(77, 'ja', '', ''),
(77, 'ru', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `Provides`
--

CREATE TABLE `Provides` (
  `RestaurantID` int(11) NOT NULL,
  `ProductID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `Provides`
--

INSERT INTO `Provides` (`RestaurantID`, `ProductID`) VALUES
(1, 31),
(1, 32),
(1, 34),
(1, 35),
(1, 42),
(1, 44),
(1, 47),
(1, 76),
(2, 32),
(2, 34),
(2, 40),
(2, 42),
(2, 43),
(2, 44),
(2, 47),
(2, 77),
(3, 10),
(3, 48),
(3, 52),
(3, 74),
(5, 28),
(5, 29),
(5, 32),
(6, 9),
(6, 10),
(7, 31),
(7, 32),
(7, 34),
(7, 35),
(8, 10),
(8, 29),
(8, 34),
(9, 31),
(9, 34),
(9, 35),
(9, 39),
(9, 65),
(10, 28),
(10, 29),
(10, 30),
(11, 28),
(11, 29),
(11, 30);

-- --------------------------------------------------------

--
-- Table structure for table `Restaurants`
--

CREATE TABLE `Restaurants` (
  `RestaurantID` int(11) NOT NULL,
  `RestaurantName` varchar(255) NOT NULL,
  `RestaurantPhone` varchar(15) NOT NULL,
  `Picture` varchar(255) NOT NULL,
  `Owner` int(11) NOT NULL,
  `RestaurantAddress` varchar(255) NOT NULL,
  `BusinessHours` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `Restaurants`
--

INSERT INTO `Restaurants` (`RestaurantID`, `RestaurantName`, `RestaurantPhone`, `Picture`, `Owner`, `RestaurantAddress`, `BusinessHours`) VALUES
(1, 'McDoanlds', '040 6923023', 'kuva.jpg', 47, 'Vakotie 24', '07-21'),
(2, 'BököKing', '0421325032', 'kungen.jpg', 47, 'Joosepinkuja 66', '09-22'),
(3, 'Pizzeria Nam', '123456789', 'updatetest.jpg', 47, 'Osoitetoimi 55', '1-12'),
(5, 'Ravintola 4', '54346243', 'ei_oo.jpg', 47, 'asdinkuja 2', '22-06'),
(6, 'InterPizza', '050 278 8893', 'eiumlautteja1.jpg', 47, 'Sebskatu 3', '00-23'),
(7, 'InterHamppari', '044 444 4444', 'eiumlautteja2.jpg', 47, 'Myllypurontie 44', '04-16'),
(8, 'Gamer Guys', '040 404 0404', 'notfound.jpg', 47, 'NotNullStreet 0', '07-09'),
(9, 'Datan Hampparit', '010 010 0101', 'jee.jpg', 47, 'Bittikatu 10', '10-01'),
(10, 'Nuori Osuja', '050 123 4567', 'sergej.jpg', 47, 'Jippiijahuukuja 8', '11-21'),
(11, 'Pihlan Pihvit', '040 222 2221', 'staker.jpg', 54, 'Rajneeshpuram 7', '1-11');

-- --------------------------------------------------------

--
-- Table structure for table `Restaurant_Categories`
--

CREATE TABLE `Restaurant_Categories` (
  `RestaurantID` int(11) NOT NULL,
  `CategoryID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `Restaurant_Categories`
--

INSERT INTO `Restaurant_Categories` (`RestaurantID`, `CategoryID`) VALUES
(1, 1),
(2, 1),
(3, 2),
(5, 1),
(5, 4),
(6, 2),
(7, 1),
(8, 1),
(8, 2),
(8, 4),
(9, 1),
(10, 4),
(11, 4);

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `User_ID` int(11) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Role` varchar(255) NOT NULL,
  `Enabled` bit(1) DEFAULT b'1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`User_ID`, `Username`, `Password`, `Role`, `Enabled`) VALUES
(8, 'mikael', '$2a$10$79kmXHDHHNF0u3OxuwCSTeBmEEUhqClNw.rLsOa63MNgPsksUn5gO', 'velho', b'1'),
(9, 'kissa', '$2a$10$W4G0bsWXrK2T0vfd7bMRd.VibBYGIzBwtBn6XNRSdVtQSPU7GPRiS', 'USER', b'1'),
(10, 'kissa1', '$2a$10$/9eKIczGpymlHiUkwbnLGuXLXJyv8Mwg4RiMbiwftcpBrjSDPQ.kG', 'kissa', b'1'),
(11, 'Seppo', '$2a$10$uNsl7wRY3kA2vO1NKpKVyeW8.Oo.Z/x1ehGCStXts07Z5rVIhAyPe', 'megaman', b'0'),
(12, 'john_doe', '$2a$10$bacZh9b7/FWr6NvPMy92geJjP8IKo10utkWX7jC0OCcfld4wNGnIS', 'USER', b'1'),
(23, 'heyo', '$2a$10$.5waQChcyWYkhTztQ0Tzg.1Oq./XHj29I.uULXHRk8Pq2U30OEXCC', 'USER', b'1'),
(25, 'megisking', '$2a$10$yrX/HI2l5AJK0Rrw5sqAUOju1sN5iObeUCL6MmgV.vbZZzA8eSfUy', 'USER', b'1'),
(26, 'megis2', '$2a$10$3WuIcNHs7qr91kHechlLs.Lt2MAorZ5A.Q1T5sa8mRDrgoVdkykG2', 'USER', b'1'),
(28, 'testi1', '$2a$10$rAEalRBwX08g3zTr87g0Ze2hiSDyLRTQteB6Z7P6K7cixoLbt4cma', 'USER', b'1'),
(29, 'Jimbo', '$2a$10$yQsmhgB7.mho/mx4/nDseuWz9WACNFrFC3Dr1JGigLZkkITYX3Jf.', 'USER', b'1'),
(30, 'Jimi1', 'asdasdad', 'USER', b'0'),
(31, 'jimmy1', '$2a$10$SXPY6wj7GSzxO5bRXK9dbuutwJNr4uC8qjzWRgVoaVHw5hxN1Zn8S', 'USER', b'1'),
(32, 'jimmy2', '$2a$10$RHjpYX0NgTCObuCnnL6DZ.sWCVhdL7wJ3ZqHVc8m74c8sAEmfLkky', 'USER', b'1'),
(33, 'jimmy3', '$2a$10$ZdXj2FL8wFN6d8t9GW25o.TY5FXHDMqdS4Gx/Qa5FIoFa2xG0SGbq', 'USER', b'1'),
(34, 'meitsi', '$2a$10$70uDbLj50aqE30te95yA2u1tIEaQ6bMRw/bedJ/X38pPuoKH4vJsG', 'USER', b'1'),
(35, 'postmens', '$2a$10$o7v2p/qIVa3JKd70f4uZgu4r.608g2g73lYKQlqzlSyfKSx5oLtuC', 'USER', b'1'),
(36, 'salasana&', '$2a$10$ECpS3Cv2USdJcw4O8QUzcOXxWq1jHmHC5vFP4PKx/mTAslXcVOjoy', 'USER', b'1'),
(37, 'Jimitest', '$2a$10$alrnsEH4PY8nD4NjwzrzFeWp5UVunfQOFKk/Z3xGrP3PN0nvlbHt2', 'USER', b'1'),
(38, 'MikkoTesteri', '$2a$10$spk2y0mxgeilZt.FXlwYpuWn4KHMvdsIU0d.zjMzXRFRLaazgckfu', 'USER', b'1'),
(39, 'Uus', '$2a$10$Ms1t/J.OGuE77hHz9Bwh8uXdFDYO4BveQC4rEn7/y7QMjebFjwh2e', 'USER', b'1'),
(40, 'seppotaalasmaa', '$2a$10$M5ee0/.Q9X6gw60rNTu/l.ZgK/K/GrKFmaK0trPCj7r87sCZHvEGC', 'USER', b'1'),
(41, 'johannes1', '$2a$10$FxH7LxEc2D.nuI175G80JeAa5qPZfFAUYXIelRhb31.DXJJfuQ3aq', 'USER', b'1'),
(43, 'testimies', '$2a$10$iBEAOVOVL1VwTtIg3CN27ethnl9Fw13wf2KeZivW63AWsuE2lGRni', 'USER', b'1'),
(47, 'johannes', '$2a$10$uxSBA.s.Egqd.7H4F4kwqugT9Fcxk6wsxpfEPqX8cX0rNdUHQjcHC', 'OWNER', b'1'),
(48, 'testiteppo', '$2a$10$8ztF4nPH7kcVr64aXmhX2OJn0ucpjQeMArirVvAMjg5GFmcUpbKHm', 'OWNER', b'1'),
(49, 'JimiTesteri', '$2a$10$nYO4HZm1RPC8w/lryIebMOo9vSqR5ocxB1wJmKA/I7U58DJP3QKK.', 'OWNER', b'1'),
(50, 'jere', '$2a$10$VRNNTak.LSeUqFZkI7xH3uLafjxKaSJlmQgMXT.iX2Ae0OGSj6aeG', 'USER', b'1'),
(51, 'testitili', '$2a$10$JQivZ0V8T29/ImL/.23DMOy1Y7Iu86Jbhn/eu7z3FlajMjUW.msyK', 'USER', b'1'),
(52, 'testiseppo1', '$2a$10$jMVv3ugEVNB4Slwl8T0MB.P/3y.3D87UHSsOlIue2vqGgGJpLS68y', 'USER', b'1'),
(53, 'demotesti', '$2a$10$CNccRuwR74CFoUfE/pwgnOvZoHAgNQg4WS65SdhXkvzldhKLxDoSa', 'USER', b'1'),
(54, 'mikko', '$2a$10$ES7XHrNVYMoOJrRr7JMrDOuP3Jkk5qAnidDKMFjBSdvf.LQvKH9wq', 'OWNER', b'1'),
(55, 'mikko2', '$2a$10$nW6ZNfKASZ4PaeG1xfhXvuX2lERH.FIFsWYR0xHN0kshIXPyYVxN2', 'USER', b'0'),
(56, 'uuskäyttis', '$2a$10$ggcBB7X9IISuaXhD/pi17.dG3Rp/wzMpr2G2BAmecgC3fR/aZVhI6', 'USER', b'1'),
(57, 'uusin', '$2a$10$/v63pgfdwMGvHy63IzJvqe81A2hl098O4LCLvJWnV3Xx/OMUSkh7u', 'ADMIN', b'1'),
(58, 'uusin1', '$2a$10$nxADI0TTxBG.ujGdyBGL7Of0Y5tMnohkk1TiMUn8GheQ6OnT2Lv7O', 'ADMIN', b'1'),
(65, 'uusakko', '$2a$10$oTaqdUeOy5henDCikEER.uY7kvsbzw8lVcUkmjioIfjhxVMY0KNua', 'USER', b'1'),
(72, 'spede', '$2a$10$5Xwxq1yRRjMCV5GM3rYEF.GTkByqVsFHA5M649oHFA8syeJgoxRF6', 'USER', b'1'),
(74, 'spede1', '$2a$10$ZZp6jeAab5t.qN34tUHydeLzn2C7TZAUwjaiaNKOLkTGABGCmODcK', 'USER', b'1'),
(75, 'asd', '$2a$10$HFpx9CRbifVX4rJWcSTAluRP4AdrUiEIRYVL24QkMtMU43kjh6fIm', 'USER', b'0'),
(78, 'Testi5', '$2a$10$cL0PUqTGmj/h5.DtGlvyBOUiRfBpOWUO7SsA/eFB/AW7wq3kLlByS', 'USER', b'0'),
(80, 'seppo2', '$2a$10$AbM2L0nebDu9BjM7AMGU0.2EETJ8o6ap/opM67MvVfPYvMqzCwR3K', 'USER', b'0'),
(81, 'mikkotest', '$2a$10$pF2iL6aSpbNezJL.AS64SeUi9YzoP0MPjPvQxadLrg.ZM6T7C6eyi', 'USER', b'0');

-- --------------------------------------------------------

--
-- Table structure for table `UsersAddress`
--

CREATE TABLE `UsersAddress` (
  `User_ID` int(11) NOT NULL,
  `DeliveryAddressID` int(11) NOT NULL,
  `is_default` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `UsersAddress`
--

INSERT INTO `UsersAddress` (`User_ID`, `DeliveryAddressID`, `is_default`) VALUES
(8, 122, 1),
(30, 1, NULL),
(36, 6, NULL),
(36, 2, 1),
(38, 20, NULL),
(38, 21, NULL),
(39, 23, NULL),
(43, 40, 1),
(50, 63, NULL),
(50, 62, 1),
(51, 65, 1),
(52, 67, 1),
(53, 69, 1),
(56, 102, 1),
(65, 123, 1),
(80, 142, NULL),
(80, 141, 1);

-- --------------------------------------------------------

--
-- Table structure for table `users_seq`
--

CREATE TABLE `users_seq` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) UNSIGNED NOT NULL,
  `cycle_option` tinyint(1) UNSIGNED NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB;

--
-- Dumping data for table `users_seq`
--

INSERT INTO `users_seq` (`next_not_cached_value`, `minimum_value`, `maximum_value`, `start_value`, `increment`, `cache_size`, `cycle_option`, `cycle_count`) VALUES
(401, 1, 9223372036854775806, 1, 50, 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Categories`
--
ALTER TABLE `Categories`
  ADD PRIMARY KEY (`CategoryID`);

--
-- Indexes for table `ContactInfo`
--
ALTER TABLE `ContactInfo`
  ADD PRIMARY KEY (`User_ID`);

--
-- Indexes for table `DeliveryAddresses`
--
ALTER TABLE `DeliveryAddresses`
  ADD PRIMARY KEY (`DeliveryAddressID`);

--
-- Indexes for table `OrderProducts`
--
ALTER TABLE `OrderProducts`
  ADD PRIMARY KEY (`OrderID`,`ProductID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Indexes for table `Orders`
--
ALTER TABLE `Orders`
  ADD PRIMARY KEY (`OrderID`),
  ADD KEY `RestaurantID` (`RestaurantID`),
  ADD KEY `orders_fk` (`User_ID`);

--
-- Indexes for table `Products`
--
ALTER TABLE `Products`
  ADD PRIMARY KEY (`ProductID`);

--
-- Indexes for table `product_translations`
--
ALTER TABLE `product_translations`
  ADD PRIMARY KEY (`ProductID`,`Lang`);

--
-- Indexes for table `Provides`
--
ALTER TABLE `Provides`
  ADD PRIMARY KEY (`RestaurantID`,`ProductID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Indexes for table `Restaurants`
--
ALTER TABLE `Restaurants`
  ADD PRIMARY KEY (`RestaurantID`),
  ADD KEY `Owner` (`Owner`);

--
-- Indexes for table `Restaurant_Categories`
--
ALTER TABLE `Restaurant_Categories`
  ADD PRIMARY KEY (`RestaurantID`,`CategoryID`),
  ADD KEY `CategoryID` (`CategoryID`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`User_ID`),
  ADD UNIQUE KEY `Username` (`Username`);

--
-- Indexes for table `UsersAddress`
--
ALTER TABLE `UsersAddress`
  ADD PRIMARY KEY (`User_ID`,`DeliveryAddressID`),
  ADD UNIQUE KEY `unique_default_per_user` (`User_ID`,`is_default`),
  ADD KEY `uid_address_fk` (`DeliveryAddressID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Categories`
--
ALTER TABLE `Categories`
  MODIFY `CategoryID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `DeliveryAddresses`
--
ALTER TABLE `DeliveryAddresses`
  MODIFY `DeliveryAddressID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=147;

--
-- AUTO_INCREMENT for table `Orders`
--
ALTER TABLE `Orders`
  MODIFY `OrderID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT for table `Products`
--
ALTER TABLE `Products`
  MODIFY `ProductID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=78;

--
-- AUTO_INCREMENT for table `Restaurants`
--
ALTER TABLE `Restaurants`
  MODIFY `RestaurantID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `Users`
--
ALTER TABLE `Users`
  MODIFY `User_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=83;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ContactInfo`
--
ALTER TABLE `ContactInfo`
  ADD CONSTRAINT `contactinfo_fk` FOREIGN KEY (`User_ID`) REFERENCES `Users` (`User_ID`) ON DELETE CASCADE;

--
-- Constraints for table `OrderProducts`
--
ALTER TABLE `OrderProducts`
  ADD CONSTRAINT `OrderProducts_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `Products` (`ProductID`),
  ADD CONSTRAINT `order_id_fk` FOREIGN KEY (`OrderID`) REFERENCES `Orders` (`OrderID`) ON DELETE CASCADE;

--
-- Constraints for table `Orders`
--
ALTER TABLE `Orders`
  ADD CONSTRAINT `Orders_ibfk_2` FOREIGN KEY (`RestaurantID`) REFERENCES `Restaurants` (`RestaurantID`),
  ADD CONSTRAINT `orders_fk` FOREIGN KEY (`User_ID`) REFERENCES `Users` (`User_ID`) ON DELETE CASCADE;

--
-- Constraints for table `product_translations`
--
ALTER TABLE `product_translations`
  ADD CONSTRAINT `product_translations_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `Products` (`ProductID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Provides`
--
ALTER TABLE `Provides`
  ADD CONSTRAINT `Provides_ibfk_1` FOREIGN KEY (`RestaurantID`) REFERENCES `Restaurants` (`RestaurantID`),
  ADD CONSTRAINT `Provides_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `Products` (`ProductID`);

--
-- Constraints for table `Restaurants`
--
ALTER TABLE `Restaurants`
  ADD CONSTRAINT `Restaurants_ibfk_1` FOREIGN KEY (`Owner`) REFERENCES `Users` (`User_ID`);

--
-- Constraints for table `Restaurant_Categories`
--
ALTER TABLE `Restaurant_Categories`
  ADD CONSTRAINT `Restaurant_Categories_ibfk_1` FOREIGN KEY (`RestaurantID`) REFERENCES `Restaurants` (`RestaurantID`),
  ADD CONSTRAINT `Restaurant_Categories_ibfk_2` FOREIGN KEY (`CategoryID`) REFERENCES `Categories` (`CategoryID`);

--
-- Constraints for table `UsersAddress`
--
ALTER TABLE `UsersAddress`
  ADD CONSTRAINT `uid_address_fk` FOREIGN KEY (`DeliveryAddressID`) REFERENCES `DeliveryAddresses` (`DeliveryAddressID`) ON DELETE CASCADE,
  ADD CONSTRAINT `uid_fk` FOREIGN KEY (`User_ID`) REFERENCES `Users` (`User_ID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
