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
-- Table structure for table `reportitem`
--

DROP TABLE IF EXISTS `reportitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reportitem` (
  `ID` bigint(20) NOT NULL,
  `DTYPE` varchar(31) DEFAULT NULL,
  `CREATEDAT` datetime DEFAULT NULL,
  `CSSBACKCOLOR` varchar(255) DEFAULT NULL,
  `CSSBORDER` varchar(255) DEFAULT NULL,
  `CSSBORDERRADIUS` varchar(255) DEFAULT NULL,
  `CSSCLIP` varchar(255) DEFAULT NULL,
  `CSSCOLOR` varchar(255) DEFAULT NULL,
  `CSSFONTFAMILY` varchar(255) DEFAULT NULL,
  `CSSFONTSIZE` varchar(255) DEFAULT NULL,
  `CSSFONTSTYLE` varchar(255) DEFAULT NULL,
  `CSSFONTVARIANT` varchar(255) DEFAULT NULL,
  `CSSFONTWEIGHT` varchar(255) DEFAULT NULL,
  `CSSHEIGHT` varchar(255) DEFAULT NULL,
  `CSSLEFT` varchar(255) DEFAULT NULL,
  `CSSLINEHEIGHT` varchar(255) DEFAULT NULL,
  `CSSMARGIN` varchar(255) DEFAULT NULL,
  `CSSOVERFLOW` varchar(255) DEFAULT NULL,
  `CSSPADDING` varchar(255) DEFAULT NULL,
  `CSSPOSITION` varchar(255) DEFAULT NULL,
  `CSSTEXTALIGN` varchar(255) DEFAULT NULL,
  `CSSTOP` varchar(255) DEFAULT NULL,
  `CSSVERTICALALIGN` varchar(255) DEFAULT NULL,
  `CSSWIDTH` varchar(255) DEFAULT NULL,
  `CSSZORDER` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `FORMATPREFIX` varchar(255) DEFAULT NULL,
  `FORMATSTRING` varchar(255) DEFAULT NULL,
  `FORMATSUFFIX` varchar(255) DEFAULT NULL,
  `IXITEMTYPE` varchar(255) DEFAULT NULL,
  `IXITEMVALUETYPE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `ORDERNO` int(11) DEFAULT NULL,
  `REPORTITEMTYPE` varchar(255) DEFAULT NULL,
  `RETIRECOMMENTS` varchar(255) DEFAULT NULL,
  `RETIRED` tinyint(1) DEFAULT '0',
  `RETIREDAT` datetime DEFAULT NULL,
  `SNAME` varchar(255) DEFAULT NULL,
  `TNAME` varchar(255) DEFAULT NULL,
  `CATEGORY_ID` bigint(20) DEFAULT NULL,
  `CREATER_ID` bigint(20) DEFAULT NULL,
  `ITEM_ID` bigint(20) DEFAULT NULL,
  `RETIRER_ID` bigint(20) DEFAULT NULL,
  `PAGENO` int(11) DEFAULT NULL,
  `HEALTHFORMITEMTYPE` varchar(255) DEFAULT NULL,
  `HEALTHFORMITEMVALUETYPE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_REPORTITEM_CREATER_ID` (`CREATER_ID`),
  KEY `FK_REPORTITEM_CATEGORY_ID` (`CATEGORY_ID`),
  KEY `FK_REPORTITEM_RETIRER_ID` (`RETIRER_ID`),
  KEY `FK_REPORTITEM_ITEM_ID` (`ITEM_ID`),
  CONSTRAINT `FK_REPORTITEM_CATEGORY_ID` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `category` (`ID`),
  CONSTRAINT `FK_REPORTITEM_CREATER_ID` FOREIGN KEY (`CREATER_ID`) REFERENCES `webuser` (`ID`),
  CONSTRAINT `FK_REPORTITEM_ITEM_ID` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`),
  CONSTRAINT `FK_REPORTITEM_RETIRER_ID` FOREIGN KEY (`RETIRER_ID`) REFERENCES `webuser` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reportitem`
--

LOCK TABLES `reportitem` WRITE;
/*!40000 ALTER TABLE `reportitem` DISABLE KEYS */;
INSERT INTO `reportitem` VALUES (252,'HealthFormItem',NULL,'','','','','','','250','Normal','','20','20','35','','','auto','','Static','Left','10','Middle','','',NULL,'','','',NULL,NULL,'H509',0,NULL,NULL,0,NULL,'සෞක්ය සෙවා දෙපර්තුමෙන්තුව - දකුනු පලාත','සෞක්ය සෙවා දෙපර්තුමෙන්තුව - දකුනු පලාත',NULL,NULL,452,NULL,0,'Label','Varchar'),(253,'HealthFormItem',NULL,'','','','','','','','Normal','','','','10','','','auto','','Static','Left','20','Baseline','','',NULL,'','','',NULL,NULL,'Maternal Death Count',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,452,NULL,0,'Label','Varchar'),(254,'HealthFormItem',NULL,'','','','','','','','Normal','','','','55','','','auto','','Static','Left','20','Baseline','','',NULL,'','','',NULL,NULL,'Maternal Death Count Value',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,452,NULL,0,'Value','Varchar'),(255,'HealthFormItem','2013-10-26 05:30:07','','','','','','','','Normal','','','','','','','auto','','Static','Left','','Baseline','','',NULL,'','','',NULL,NULL,'Live births',0,NULL,NULL,0,NULL,NULL,NULL,NULL,104,NULL,NULL,0,'Label','Varchar'),(256,'HealthFormItem',NULL,'','','','','','','','Normal','','','','10','','','auto','','Static','Left','25','Baseline','','',NULL,'','','',NULL,NULL,'Live births',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,452,NULL,0,'Label','Varchar'),(257,'HealthFormItem','2013-10-26 05:30:28','','','','','','','','Normal','','','','','','','auto','','Static','Left','','Baseline','','',NULL,'','','',NULL,NULL,'Live births',0,NULL,NULL,0,NULL,NULL,NULL,NULL,104,NULL,NULL,0,'Label','Varchar'),(258,'HealthFormItem',NULL,'','','','','','','','Normal','','','','','','','auto','','Static','Left','','Baseline','','',NULL,'','','',NULL,NULL,'New Label',0,NULL,NULL,1,'2013-10-26 05:31:41',NULL,NULL,NULL,NULL,452,104,0,'Label','Varchar'),(259,'HealthFormItem','2013-10-26 05:31:31','','','','','','','','Normal','','','','30','','','auto','','Static','Left','20','Baseline','','',NULL,'','','',NULL,NULL,'Live births',0,NULL,NULL,0,NULL,NULL,NULL,NULL,104,NULL,NULL,0,'Label','Varchar'),(260,'HealthFormItem',NULL,'','','','','','','','Normal','','','','55','','','auto','','Static','Left','25','Baseline','','',NULL,'','','',NULL,NULL,'Live births',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,452,NULL,0,'Value','Varchar'),(261,'HealthFormItem','2013-10-26 05:33:22','','','','','','','','Normal','','','','50','','','auto','','Static','Left','25','Baseline','','',NULL,'','','',NULL,NULL,'Live births',0,NULL,NULL,0,NULL,NULL,NULL,NULL,104,NULL,NULL,0,'Label','Varchar'),(262,'HealthFormItem',NULL,'','','','','','','','Normal','','','','10','','','auto','','Static','Left','30','Baseline','','',NULL,'','','',NULL,NULL,'Prgnant mothers Normal',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,452,NULL,0,'Label','Varchar'),(263,'HealthFormItem',NULL,'','','','','','','','Normal','','','','','','','auto','','Static','Left','','Baseline','','',NULL,'','','',NULL,NULL,'New Label',0,NULL,NULL,1,'2013-10-26 05:35:23',NULL,NULL,NULL,NULL,452,104,0,'Label','Varchar'),(264,'HealthFormItem',NULL,'','','','','','','','Normal','','','','55','','','auto','','Static','Left','30','Baseline','','',NULL,'','','',NULL,NULL,'Prgnant mothers Normal',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,452,NULL,0,'Value','Varchar'),(265,'HealthFormItem','2013-10-26 05:37:45','','','','','','','','Normal','','','','','','','auto','','Static','Left','','Baseline','','',NULL,'','','',NULL,NULL,'',0,NULL,NULL,0,NULL,NULL,NULL,NULL,104,NULL,NULL,0,'Label','Varchar'),(266,'HealthFormItem',NULL,'','','','','','','','Normal','','','','10','','','auto','','Static','Left','35','Baseline','','',NULL,'','','',NULL,NULL,'Prgnant mothers at risk',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,452,NULL,0,'Label','Varchar'),(267,'HealthFormItem',NULL,'','','','','','','','Normal','','','','55','','','auto','','Static','Left','35','Baseline','','',NULL,'','','',NULL,NULL,'Prgnant mothers at risk',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,452,NULL,0,'Value','Varchar'),(317,'HealthFormItem',NULL,'','','','','','','','Normal','','','','','','','auto','','Static','Left','','Baseline','','',NULL,'','','',NULL,NULL,'New Label',0,NULL,NULL,1,'2013-10-27 10:55:31',NULL,NULL,NULL,NULL,452,104,0,'Label','Varchar'),(318,'HealthFormItem',NULL,'','','','','','','','Normal','','','','10','','','auto','','Static','Left','40','Baseline','','',NULL,'','','',NULL,NULL,'Sample Data',0,NULL,NULL,1,'2013-11-28 08:33:49',NULL,NULL,NULL,NULL,452,104,0,'Label','Varchar'),(1302,'HealthFormItem',NULL,'','','','','','','','Normal','','','','20','','','auto','','Static','Left','30','Baseline','','',NULL,'','','',NULL,NULL,'Total Regiaterd Eligible Couples',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,207,NULL,0,'Value','Double'),(1353,'HealthFormItem',NULL,'','','','','','','','Normal','','','','10','','','auto','','Static','Left','20','Baseline','','',NULL,'','','',NULL,NULL,'no1',0,NULL,NULL,1,'2013-11-27 22:37:03',NULL,NULL,NULL,NULL,1352,104,0,'Value','Varchar'),(1354,'HealthFormItem',NULL,'','','','','','','','Normal','','','','10','','','auto','','Static','Left','30','Baseline','','',NULL,'','','',NULL,NULL,'no2',0,NULL,NULL,1,'2013-11-27 22:37:07',NULL,NULL,NULL,NULL,1352,104,0,'Value','Varchar'),(1603,'HealthFormItem',NULL,'','','','','','','','Normal','','','','10','','','auto','','Static','Left','20','Baseline','','',NULL,'','','',NULL,NULL,'Attribute',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1602,NULL,0,'Value','Varchar'),(1801,'HealthFormItem',NULL,'','','','','','','','Normal','','','','20','','','auto','','Static','Left','32','Baseline','','',NULL,'','','',NULL,NULL,'Comments',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,207,NULL,0,'Value','Varchar'),(3066,'HealthFormItem',NULL,'','','','','','','','Normal','','','','','','','auto','','Static','Left','','Baseline','','',NULL,'','','',NULL,NULL,'sample',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,1352,NULL,0,'Value','Double'),(3103,'HealthFormItem',NULL,'','','','','','','','Normal','','','','','','','auto','','Static','Center','','Baseline','','',NULL,'','','',NULL,NULL,'New Value',0,NULL,NULL,1,'2013-12-11 15:47:01',NULL,NULL,NULL,NULL,3102,104,0,'Value','Integer'),(3104,'HealthFormItem','2013-11-28 08:30:18','','','','','','','','Normal','','','','','','','auto','','Static','Left','','Baseline','','',NULL,'','','',NULL,NULL,'',0,NULL,NULL,0,NULL,NULL,NULL,NULL,104,NULL,NULL,0,'Label','Double'),(3105,'HealthFormItem',NULL,'','','','','','','','Normal','','','','10','','','auto','','Static','Left','10','Baseline','','',NULL,'','','',NULL,NULL,'New Value',0,NULL,NULL,1,'2013-12-11 15:47:06',NULL,NULL,NULL,NULL,3102,104,0,'Value','Double'),(3106,'HealthFormItem','2013-11-28 08:32:36','','','','','','','','Normal','','','','20','','','auto','','Static','Left','20','Baseline','','',NULL,'','','',NULL,NULL,'',0,NULL,NULL,0,NULL,NULL,NULL,NULL,104,NULL,NULL,0,'Value','Integer'),(3301,'HealthFormItem',NULL,'','','','','','','','Normal','','','','20','','','auto','','Static','Left','20','Baseline','','',NULL,'','','',NULL,NULL,'Number of  live births',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,3102,NULL,0,'Label','Varchar'),(3302,'HealthFormItem','2013-12-11 15:47:31','','','','','','','','Normal','','','','10','','','auto','','Static','Left','10','Baseline','','',NULL,'','','',NULL,NULL,'number',0,NULL,NULL,0,NULL,NULL,NULL,NULL,104,NULL,NULL,0,'Label','Varchar'),(3303,'HealthFormItem',NULL,'','','','','','','','Normal','','','','40','','','auto','','Static','Left','20','Baseline','','',NULL,'','','',NULL,NULL,'Live births',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,3102,NULL,0,'Value','Varchar'),(3852,'HealthFormItem',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'New Label',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,3851,NULL,0,'Label',NULL),(3854,'HealthFormItem',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'New Label',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,3853,NULL,0,'Label',NULL),(3856,'HealthFormItem',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'New Label',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,3855,NULL,0,'Label',NULL),(3858,'HealthFormItem',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'New Label',0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,3857,NULL,0,'Label',NULL);
/*!40000 ALTER TABLE `reportitem` ENABLE KEYS */;
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
