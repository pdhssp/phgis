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
-- Table structure for table `webuser`
--

DROP TABLE IF EXISTS `webuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `webuser` (
  `ID` bigint(20) NOT NULL,
  `ACTIVATECOMMENTS` varchar(255) DEFAULT NULL,
  `ACTIVATED` tinyint(1) DEFAULT '0',
  `ACTIVATEDAT` datetime DEFAULT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `CREATEDAT` datetime DEFAULT NULL,
  `DEFLOCALE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `PRIMETHEME` varchar(255) DEFAULT NULL,
  `RETIRECOMMENTS` varchar(255) DEFAULT NULL,
  `RETIRED` tinyint(1) DEFAULT '0',
  `RETIREDAT` datetime DEFAULT NULL,
  `TELNO` varchar(255) DEFAULT NULL,
  `WEBUSERPASSWORD` varchar(255) DEFAULT NULL,
  `ACTIVATOR_ID` bigint(20) DEFAULT NULL,
  `AREA_ID` bigint(20) DEFAULT NULL,
  `CREATER_ID` bigint(20) DEFAULT NULL,
  `DEPARTMENT_ID` bigint(20) DEFAULT NULL,
  `INSTITUTION_ID` bigint(20) DEFAULT NULL,
  `RETIRER_ID` bigint(20) DEFAULT NULL,
  `ROLE_ID` bigint(20) DEFAULT NULL,
  `STAFF_ID` bigint(20) DEFAULT NULL,
  `USERWEBTHEME_ID` bigint(20) DEFAULT NULL,
  `WEBUSERPERSON_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK_WEBUSER_ROLE_ID` (`ROLE_ID`),
  KEY `FK_WEBUSER_WEBUSERPERSON_ID` (`WEBUSERPERSON_ID`),
  KEY `FK_WEBUSER_DEPARTMENT_ID` (`DEPARTMENT_ID`),
  KEY `FK_WEBUSER_INSTITUTION_ID` (`INSTITUTION_ID`),
  KEY `FK_WEBUSER_STAFF_ID` (`STAFF_ID`),
  KEY `FK_WEBUSER_AREA_ID` (`AREA_ID`),
  KEY `FK_WEBUSER_ACTIVATOR_ID` (`ACTIVATOR_ID`),
  KEY `FK_WEBUSER_RETIRER_ID` (`RETIRER_ID`),
  KEY `FK_WEBUSER_USERWEBTHEME_ID` (`USERWEBTHEME_ID`),
  KEY `FK_WEBUSER_CREATER_ID` (`CREATER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `webuser`
--

LOCK TABLES `webuser` WRITE;
/*!40000 ALTER TABLE `webuser` DISABLE KEYS */;
INSERT INTO `webuser` VALUES (5,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,'Admin',NULL,NULL,1,'2013-10-24 20:49:16',NULL,'94gL1sgseW3ihlnCrg7JITf2FAl4Qv85',NULL,NULL,NULL,NULL,NULL,5,4,NULL,NULL,1),(7,NULL,1,'2013-10-24 15:10:54','1','2013-10-24 15:10:54',NULL,NULL,NULL,'c',NULL,NULL,1,'2013-10-24 15:28:01',NULL,'pZjir778cAKdmZu51T9RVVWiDXsDc3m3',5,NULL,NULL,NULL,NULL,5,NULL,6,NULL,1),(102,NULL,1,'2013-10-24 20:48:52','1','2013-10-24 20:48:52',NULL,NULL,NULL,'1',NULL,NULL,1,'2013-10-24 20:49:19',NULL,'xk3VsgpEvJOTxJsH+1IgevDMzPxrOKJK',5,NULL,NULL,NULL,NULL,5,NULL,101,NULL,2),(104,NULL,1,'2013-10-24 20:49:45','b','2013-10-24 20:49:45','si',NULL,NULL,'Twk/pSHqWK1P/AldvmrRfg==','ui-lightness',NULL,0,NULL,NULL,'Twk/pSHqWK3zmwN8whlNaA+45xcyrPOD',5,NULL,NULL,NULL,NULL,NULL,NULL,103,NULL,3),(153,NULL,1,'2013-10-25 05:30:25','1','2013-10-25 05:30:26',NULL,NULL,NULL,'FWprKeGHNK9nQS1A+eL1ig==',NULL,NULL,0,NULL,NULL,'+t0AGPsGgZ3J5kchDg4JMub8+Tn6+hIP',104,NULL,NULL,NULL,NULL,NULL,NULL,152,NULL,4),(155,NULL,1,'2013-10-25 05:30:57','2','2013-10-25 05:30:57',NULL,NULL,NULL,'nlabLohDV26XCOB91zdWQA==',NULL,NULL,0,NULL,NULL,'lC+4WCQSCwgg9j+88deDo0j8o9DoSHps',104,NULL,NULL,NULL,NULL,NULL,NULL,154,NULL,5),(158,NULL,1,'2013-10-25 08:20:38','11','2013-10-25 08:20:40',NULL,NULL,NULL,'d',NULL,NULL,1,'2013-11-14 19:42:30',NULL,'mmrBA5/wiqznQo6ZbhWhvobL7VZLZ7yf',104,NULL,NULL,NULL,NULL,1202,NULL,157,NULL,6),(1202,NULL,1,'2013-11-05 17:52:39','1','2013-11-05 17:52:40',NULL,NULL,NULL,'fwLaTZHgdgdxcrZinORSTA==',NULL,NULL,0,NULL,NULL,'T0b0cZ1fdhpWwUUdQX8b2AkAyWfG9GD8',104,NULL,NULL,NULL,NULL,NULL,NULL,1201,NULL,7),(2156,NULL,1,'2013-11-14 07:24:59','45','2013-11-14 07:24:59',NULL,NULL,NULL,'aHM2R03sEMJtQzSsKfnYGA==',NULL,NULL,0,NULL,NULL,'T3ji9g70BkG822SdGnqDYdTKwGPVWwgG',1202,NULL,NULL,NULL,NULL,NULL,NULL,2155,NULL,8),(2371,NULL,1,'2013-11-19 22:17:47','dss','2013-11-19 22:17:47',NULL,NULL,NULL,'M0jhOqRBAsh99H3pmQlFIA==','aristo',NULL,0,NULL,NULL,'M0jhOqRBAsii1UWNUnybSrKGRbN+Fldo',104,NULL,NULL,NULL,NULL,NULL,NULL,2370,NULL,9),(2854,NULL,1,'2013-11-26 15:15:01','phi1','2013-11-26 15:15:02',NULL,NULL,NULL,'gwcO00IPMJZDO31VhhmEYw==',NULL,NULL,0,NULL,NULL,'hBc+uRLkzDp41Z58fDlBSjtcDCHPBlX0',2371,NULL,NULL,NULL,NULL,NULL,NULL,2853,NULL,10),(2902,NULL,1,'2013-11-26 15:37:04','moh1','2013-11-26 15:37:04',NULL,NULL,NULL,'+sImFtvDx2vYZonZdCjC+A==',NULL,NULL,0,NULL,NULL,'uu4y51UgCEy3TYhTbDkqvEsFlIXZPPAm',2371,NULL,NULL,NULL,NULL,NULL,NULL,2901,NULL,11);
/*!40000 ALTER TABLE `webuser` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-05-09 18:24:24