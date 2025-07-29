CREATE DATABASE  IF NOT EXISTS `db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: db
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `password_UNIQUE` (`password`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin','admin123','ADMIN'),(2,'quang','123','ADMIN'),(3,'admin1','admin1','ADMIN');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `price` int NOT NULL,
  `image` varchar(200) NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `type` varchar(45) NOT NULL,
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (6,'Đùi gà ngon thơm bơ ngọt béo','Ngon lắm luôn',60000,'Đùi-gà-ngon-thơm-bơ-ngọt-béo.jpeg',0,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(7,'Mực xào chua ngọt, món khoái khẩu của các bé','Ngon lắm luôn',60000,'Mực-xào-chua-ngọt,-món-khoái-khẩu-của-các-bé.jpeg',0,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(8,'Sườn xào chua ngọt, món khoái khẩu của các bé','Ngon lắm luôn',100000,'Sườn-xào-chua-ngọt,-món-khoái-khẩu-của-các-bé.jpeg',1,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(9,'Trà lài thơm ngon Size L','Ngon lắm luôn',40000,'Trà-lài-thơm-ngon-Size-L.jpeg',1,'DRINK','2025-07-25 10:10:25','2025-07-25 10:10:25'),(10,'Trà tắc đậm vị size M','Ngon lắm luôn',20000,'Trà-tắc-đậm-vị-size-M.jpeg',1,'DRINK','2025-07-25 10:10:25','2025-07-25 10:10:25'),(11,'Cơm chiên hải sản','Ngon lắm luôn',50000,'Cơm-chiên-hải-sản.jpeg',1,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(12,'Nước ép dưa hấu đỏ','Ngon lắm nè',25000,'Nước-ép-dưa-hấu-đỏ.jpeg',1,'DRINK','2025-07-25 10:10:25','2025-07-25 10:10:25'),(13,'Trà ổi thơm ngon','Cực ngon',25000,'Trà-ổi-thơm-ngon.jpeg',1,'DRINK','2025-07-25 10:10:25','2025-07-25 10:10:25'),(14,'Thịt kho mắm ruốc','Cực ngon',50000,'Thịt-kho-mắm-ruốc.jpeg',1,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(15,'Thịt kho trứng','Cực ngon',60000,'Thịt-kho-trứng.jpeg',1,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(16,'Khổ qua nhồi thịt béo ngon lắm','Cực ngon',45000,'Khổ-qua-nhồi-thịt-béo-ngon-lắm.jpeg',1,'FOOD','2025-07-25 10:15:18','2025-07-25 10:10:25'),(17,'Trứng chiên hành ( 3 miếng)','Cực ngon',50000,'Trứng-chiên-hành-(-3-miếng).jpeg',1,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(18,'Thịt ba chỉ chiên giòn (Có kèm rau sống và tương ớt)','Cực ngon',60000,'Thịt-ba-chỉ-chiên-giòn-(Có-kèm-rau-sống-và-tương-ớt).jpeg',1,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(19,'Thịt bò xào tỏi','Cực ngon',50000,'Thịt-bò-xào-tỏi.jpeg',1,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(20,'Hủ tiếu xương truyền thống (Kèm hộp)','Cực ngon',50000,'Hủ-tiếu-xương-truyền-thống-(Kèm-hộp).jpeg',1,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(21,'Hủ tiếu mực','Cực ngon',60000,'Hủ-tiếu-mực.jpeg',1,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(24,'Trà đào size L','Cực ngon',30000,'Trà-đào-size-L.png',1,'DRINK','2025-07-25 10:10:25','2025-07-25 10:10:25'),(25,'Trà đào size S','Cực ngon',20000,'Trà-đào-size-S.png',1,'DRINK','2025-07-25 10:10:25','2025-07-25 10:10:25'),(26,'Cơm gà tam kì thơm ngon cho 2 người ăn','Cực ngon',80000,'Cơm-gà-tam-kì-thơm-ngon-cho-2-người-ăn.jpeg',0,'FOOD','2025-07-25 10:10:25','2025-07-25 10:10:25'),(27,'Nước ép thơm ngon size L, M (Điền ghi chú khi chọn size)','Cực ngon bổ dưỡng',20000,'Nước-ép-thơm-ngon-size-L,-M-(Điền-ghi-chú-khi-chọn-size).jpeg',1,'DRINK','2025-07-25 10:18:44','2025-07-25 10:10:25');
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `item_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `a_idx` (`item_id`),
  KEY `b_idx` (`order_id`),
  CONSTRAINT `a` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`),
  CONSTRAINT `b` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (2,3,16,1),(3,3,11,2),(4,4,15,1),(5,4,18,1),(6,5,15,1),(7,5,8,1);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(200) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `message` varchar(500) DEFAULT NULL,
  `total_price` int NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('Completed','Shipping','New','Canceled') NOT NULL,
  `fee_ship` int NOT NULL,
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (3,'10 Phổ Quang, Ward Hàng Bồ, Hà Nội City, VietNam','1234567','Chung Duc Quang','',160000,'2025-07-24 08:35:18','New',15000,'2025-07-24 08:35:18'),(4,'10 Phổ Quang, Ward Trần Hưng Đạo, Hà Nội City, VietNam','1234567','Chung Duc Quang','',175000,'2025-07-25 03:21:24','New',55000,'2025-07-24 08:35:18'),(5,'10 Phổ Quang, Ward Đơn Dương, Lâm Đồng City, VietNam','1234567','Chung Duc Quang','CHo thêm dụng cụ ăn uống',205000,'2025-07-25 03:23:37','New',45000,'2025-07-25 03:23:37');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-25 17:20:59
