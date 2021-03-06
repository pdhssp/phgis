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
-- Table structure for table `webuserprivilege`
--

DROP TABLE IF EXISTS `webuserprivilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `webuserprivilege` (
  `ID` bigint(20) NOT NULL,
  `CREATEDAT` datetime DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `RETIRECOMMENTS` varchar(255) DEFAULT NULL,
  `RETIRED` tinyint(1) DEFAULT '0',
  `RETIREDAT` datetime DEFAULT NULL,
  `SNAME` varchar(255) DEFAULT NULL,
  `TNAME` varchar(255) DEFAULT NULL,
  `CREATER_ID` bigint(20) DEFAULT NULL,
  `RETIRER_ID` bigint(20) DEFAULT NULL,
  `WEBUSER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_WEBUSERPRIVILEGE_WEBUSER_ID` (`WEBUSER_ID`),
  KEY `FK_WEBUSERPRIVILEGE_RETIRER_ID` (`RETIRER_ID`),
  KEY `FK_WEBUSERPRIVILEGE_CREATER_ID` (`CREATER_ID`),
  CONSTRAINT `FK_WEBUSERPRIVILEGE_CREATER_ID` FOREIGN KEY (`CREATER_ID`) REFERENCES `webuser` (`ID`),
  CONSTRAINT `FK_WEBUSERPRIVILEGE_RETIRER_ID` FOREIGN KEY (`RETIRER_ID`) REFERENCES `webuser` (`ID`),
  CONSTRAINT `FK_WEBUSERPRIVILEGE_WEBUSER_ID` FOREIGN KEY (`WEBUSER_ID`) REFERENCES `webuser` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `webuserprivilege`
--

LOCK TABLES `webuserprivilege` WRITE;
/*!40000 ALTER TABLE `webuserprivilege` DISABLE KEYS */;
/*!40000 ALTER TABLE `webuserprivilege` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-05-09 18:24:26
