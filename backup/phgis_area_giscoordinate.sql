CREATE DATABASE  IF NOT EXISTS `phgis` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `phgis`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: phgis
-- ------------------------------------------------------
-- Server version	5.6.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `area_giscoordinate`
--

DROP TABLE IF EXISTS `area_giscoordinate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area_giscoordinate` (
  `Area_ID` bigint(20) NOT NULL,
  `cordinates_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Area_ID`,`cordinates_ID`),
  KEY `FK_AREA_GISCOORDINATE_cordinates_ID` (`cordinates_ID`),
  CONSTRAINT `FK_AREA_GISCOORDINATE_Area_ID` FOREIGN KEY (`Area_ID`) REFERENCES `area` (`ID`),
  CONSTRAINT `FK_AREA_GISCOORDINATE_cordinates_ID` FOREIGN KEY (`cordinates_ID`) REFERENCES `giscoordinate` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area_giscoordinate`
--

LOCK TABLES `area_giscoordinate` WRITE;
/*!40000 ALTER TABLE `area_giscoordinate` DISABLE KEYS */;
INSERT INTO `area_giscoordinate` VALUES (11,12),(272,755),(272,766),(272,771),(2,810),(2,812),(1,2135),(1,2136),(1,2137),(1,2378),(2138,2427),(2138,2428),(2433,2434),(2433,2435),(2433,2439),(1,3150),(2138,3153),(1,3204),(2,3604);
/*!40000 ALTER TABLE `area_giscoordinate` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-05-09 18:24:23
