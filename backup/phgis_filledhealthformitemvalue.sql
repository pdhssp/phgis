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
-- Table structure for table `filledhealthformitemvalue`
--

DROP TABLE IF EXISTS `filledhealthformitemvalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filledhealthformitemvalue` (
  `ID` bigint(20) NOT NULL,
  `BAIMAGE` longblob,
  `DOUBLEVALUE` double DEFAULT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `FILETYPE` varchar(255) DEFAULT NULL,
  `LOBVALUE` longtext,
  `STRVALUE` varchar(255) DEFAULT NULL,
  `FILLEDHEALTHFORMREPORT_ID` bigint(20) DEFAULT NULL,
  `HEALTHFORMITEM_ID` bigint(20) DEFAULT NULL,
  `PATIENT_ID` bigint(20) DEFAULT NULL,
  `PATIENTENCOUNTER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_FILLEDHEALTHFORMITEMVALUE_PATIENTENCOUNTER_ID` (`PATIENTENCOUNTER_ID`),
  KEY `FILLEDHEALTHFORMITEMVALUEFILLEDHEALTHFORMREPORT_ID` (`FILLEDHEALTHFORMREPORT_ID`),
  KEY `FK_FILLEDHEALTHFORMITEMVALUE_PATIENT_ID` (`PATIENT_ID`),
  KEY `FK_FILLEDHEALTHFORMITEMVALUE_HEALTHFORMITEM_ID` (`HEALTHFORMITEM_ID`),
  CONSTRAINT `FILLEDHEALTHFORMITEMVALUEFILLEDHEALTHFORMREPORT_ID` FOREIGN KEY (`FILLEDHEALTHFORMREPORT_ID`) REFERENCES `filledhealthform` (`ID`),
  CONSTRAINT `FK_FILLEDHEALTHFORMITEMVALUE_HEALTHFORMITEM_ID` FOREIGN KEY (`HEALTHFORMITEM_ID`) REFERENCES `reportitem` (`ID`),
  CONSTRAINT `FK_FILLEDHEALTHFORMITEMVALUE_PATIENTENCOUNTER_ID` FOREIGN KEY (`PATIENTENCOUNTER_ID`) REFERENCES `patientencounter` (`ID`),
  CONSTRAINT `FK_FILLEDHEALTHFORMITEMVALUE_PATIENT_ID` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filledhealthformitemvalue`
--

LOCK TABLES `filledhealthformitemvalue` WRITE;
/*!40000 ALTER TABLE `filledhealthformitemvalue` DISABLE KEYS */;
INSERT INTO `filledhealthformitemvalue` VALUES (1852,NULL,10,NULL,NULL,NULL,NULL,1851,1302,NULL,NULL),(1853,NULL,NULL,NULL,NULL,NULL,'hggj',1851,1801,NULL,NULL),(1855,NULL,NULL,NULL,NULL,NULL,NULL,1854,1302,NULL,NULL),(1856,NULL,NULL,NULL,NULL,NULL,'',1854,1801,NULL,NULL),(1857,NULL,12,NULL,NULL,NULL,NULL,1854,1302,NULL,NULL),(1858,NULL,NULL,NULL,NULL,NULL,'kjlklkl',1854,1801,NULL,NULL),(1902,NULL,NULL,NULL,NULL,NULL,NULL,1901,1302,NULL,NULL),(1903,NULL,NULL,NULL,NULL,NULL,NULL,1901,1801,NULL,NULL),(1905,NULL,5,NULL,NULL,NULL,NULL,1904,1302,NULL,NULL),(1906,NULL,NULL,NULL,NULL,NULL,'sefsdfsd',1904,1801,NULL,NULL),(1908,NULL,5,NULL,NULL,NULL,NULL,1907,1302,NULL,NULL),(1909,NULL,NULL,NULL,NULL,NULL,'sefsdfsd',1907,1801,NULL,NULL),(1911,NULL,NULL,NULL,NULL,NULL,NULL,1910,1302,NULL,NULL),(1912,NULL,NULL,NULL,NULL,NULL,NULL,1910,1801,NULL,NULL),(1914,NULL,3,NULL,NULL,NULL,NULL,1913,1302,NULL,NULL),(1915,NULL,NULL,NULL,NULL,NULL,'dgdfgdf',1913,1801,NULL,NULL),(1917,NULL,NULL,NULL,NULL,NULL,NULL,1916,1302,NULL,NULL),(1918,NULL,NULL,NULL,NULL,NULL,NULL,1916,1801,NULL,NULL),(1920,NULL,15,NULL,NULL,NULL,NULL,1919,1302,NULL,NULL),(1921,NULL,NULL,NULL,NULL,NULL,'gfhgh',1919,1801,NULL,NULL),(1953,NULL,NULL,NULL,NULL,NULL,NULL,1952,254,NULL,NULL),(1954,NULL,NULL,NULL,NULL,NULL,NULL,1952,260,NULL,NULL),(1955,NULL,NULL,NULL,NULL,NULL,NULL,1952,264,NULL,NULL),(1956,NULL,NULL,NULL,NULL,NULL,NULL,1952,267,NULL,NULL),(1958,NULL,NULL,NULL,NULL,NULL,'12',1957,254,NULL,NULL),(1959,NULL,NULL,NULL,NULL,NULL,'1',1957,260,NULL,NULL),(1960,NULL,NULL,NULL,NULL,NULL,'11',1957,264,NULL,NULL),(1961,NULL,NULL,NULL,NULL,NULL,'1',1957,267,NULL,NULL),(1963,NULL,NULL,NULL,NULL,NULL,'12',1962,254,NULL,NULL),(1964,NULL,NULL,NULL,NULL,NULL,'1',1962,260,NULL,NULL),(1965,NULL,NULL,NULL,NULL,NULL,'11',1962,264,NULL,NULL),(1966,NULL,NULL,NULL,NULL,NULL,'1',1962,267,NULL,NULL),(2354,NULL,NULL,NULL,NULL,NULL,NULL,2353,254,NULL,NULL),(2355,NULL,NULL,NULL,NULL,NULL,NULL,2353,260,NULL,NULL),(2356,NULL,NULL,NULL,NULL,NULL,NULL,2353,264,NULL,NULL),(2357,NULL,NULL,NULL,NULL,NULL,NULL,2353,267,NULL,NULL),(2359,NULL,NULL,NULL,NULL,NULL,NULL,2358,254,NULL,NULL),(2360,NULL,NULL,NULL,NULL,NULL,NULL,2358,260,NULL,NULL),(2361,NULL,NULL,NULL,NULL,NULL,NULL,2358,264,NULL,NULL),(2362,NULL,NULL,NULL,NULL,NULL,NULL,2358,267,NULL,NULL),(2363,NULL,NULL,NULL,NULL,NULL,'121',2358,254,NULL,NULL),(2364,NULL,NULL,NULL,NULL,NULL,'12',2358,260,NULL,NULL),(2365,NULL,NULL,NULL,NULL,NULL,'121',2358,264,NULL,NULL),(2366,NULL,NULL,NULL,NULL,NULL,'12',2358,267,NULL,NULL),(2403,NULL,5,NULL,NULL,NULL,NULL,2402,1302,NULL,NULL),(2404,NULL,NULL,NULL,NULL,NULL,'sff',2402,1801,NULL,NULL),(2406,NULL,NULL,NULL,NULL,NULL,NULL,2405,1302,NULL,NULL),(2407,NULL,NULL,NULL,NULL,NULL,NULL,2405,1801,NULL,NULL),(2408,NULL,12,NULL,NULL,NULL,NULL,2405,1302,NULL,NULL),(2409,NULL,NULL,NULL,NULL,NULL,'sdfsdfsdfsd',2405,1801,NULL,NULL),(2411,NULL,NULL,NULL,NULL,NULL,'1',2410,254,NULL,NULL),(2412,NULL,NULL,NULL,NULL,NULL,'1',2410,260,NULL,NULL),(2413,NULL,NULL,NULL,NULL,NULL,'1',2410,264,NULL,NULL),(2414,NULL,NULL,NULL,NULL,NULL,'1',2410,267,NULL,NULL),(2416,NULL,NULL,NULL,NULL,NULL,NULL,2415,254,NULL,NULL),(2417,NULL,NULL,NULL,NULL,NULL,NULL,2415,260,NULL,NULL),(2418,NULL,NULL,NULL,NULL,NULL,NULL,2415,264,NULL,NULL),(2419,NULL,NULL,NULL,NULL,NULL,NULL,2415,267,NULL,NULL),(2420,NULL,NULL,NULL,NULL,NULL,'12',2415,254,NULL,NULL),(2421,NULL,NULL,NULL,NULL,NULL,'13',2415,260,NULL,NULL),(2422,NULL,NULL,NULL,NULL,NULL,'14',2415,264,NULL,NULL),(2423,NULL,NULL,NULL,NULL,NULL,'15',2415,267,NULL,NULL),(2441,NULL,NULL,NULL,NULL,NULL,NULL,2440,254,NULL,NULL),(2442,NULL,NULL,NULL,NULL,NULL,NULL,2440,260,NULL,NULL),(2443,NULL,NULL,NULL,NULL,NULL,NULL,2440,264,NULL,NULL),(2444,NULL,NULL,NULL,NULL,NULL,NULL,2440,267,NULL,NULL),(2446,NULL,NULL,NULL,NULL,NULL,'2',2445,254,NULL,NULL),(2447,NULL,NULL,NULL,NULL,NULL,'52',2445,260,NULL,NULL),(2448,NULL,NULL,NULL,NULL,NULL,'56',2445,264,NULL,NULL),(2449,NULL,NULL,NULL,NULL,NULL,'2',2445,267,NULL,NULL),(2451,NULL,NULL,NULL,NULL,NULL,'2',2450,254,NULL,NULL),(2452,NULL,NULL,NULL,NULL,NULL,'52',2450,260,NULL,NULL),(2453,NULL,NULL,NULL,NULL,NULL,'56',2450,264,NULL,NULL),(2454,NULL,NULL,NULL,NULL,NULL,'2',2450,267,NULL,NULL),(2456,NULL,NULL,NULL,NULL,NULL,NULL,2455,254,NULL,NULL),(2457,NULL,NULL,NULL,NULL,NULL,NULL,2455,260,NULL,NULL),(2458,NULL,NULL,NULL,NULL,NULL,NULL,2455,264,NULL,NULL),(2459,NULL,NULL,NULL,NULL,NULL,NULL,2455,267,NULL,NULL),(2461,NULL,NULL,NULL,NULL,NULL,'1',2460,254,NULL,NULL),(2462,NULL,NULL,NULL,NULL,NULL,'13',2460,260,NULL,NULL),(2463,NULL,NULL,NULL,NULL,NULL,'1',2460,264,NULL,NULL),(2464,NULL,NULL,NULL,NULL,NULL,'1',2460,267,NULL,NULL),(2466,NULL,NULL,NULL,NULL,NULL,NULL,2465,254,NULL,NULL),(2467,NULL,NULL,NULL,NULL,NULL,NULL,2465,260,NULL,NULL),(2468,NULL,NULL,NULL,NULL,NULL,NULL,2465,264,NULL,NULL),(2469,NULL,NULL,NULL,NULL,NULL,NULL,2465,267,NULL,NULL),(2906,NULL,NULL,NULL,NULL,NULL,NULL,2905,254,NULL,NULL),(2907,NULL,NULL,NULL,NULL,NULL,NULL,2905,260,NULL,NULL),(2908,NULL,NULL,NULL,NULL,NULL,NULL,2905,264,NULL,NULL),(2909,NULL,NULL,NULL,NULL,NULL,NULL,2905,267,NULL,NULL),(2911,NULL,NULL,NULL,NULL,NULL,'12',2910,254,NULL,NULL),(2912,NULL,NULL,NULL,NULL,NULL,'67',2910,260,NULL,NULL),(2913,NULL,NULL,NULL,NULL,NULL,'34',2910,264,NULL,NULL),(2914,NULL,NULL,NULL,NULL,NULL,'56',2910,267,NULL,NULL),(2916,NULL,NULL,NULL,NULL,NULL,'12',2915,254,NULL,NULL),(2917,NULL,NULL,NULL,NULL,NULL,'67',2915,260,NULL,NULL),(2918,NULL,NULL,NULL,NULL,NULL,'34',2915,264,NULL,NULL),(2919,NULL,NULL,NULL,NULL,NULL,'56',2915,267,NULL,NULL),(3006,NULL,NULL,NULL,NULL,NULL,NULL,3005,254,NULL,NULL),(3007,NULL,NULL,NULL,NULL,NULL,NULL,3005,260,NULL,NULL),(3008,NULL,NULL,NULL,NULL,NULL,NULL,3005,264,NULL,NULL),(3009,NULL,NULL,NULL,NULL,NULL,NULL,3005,267,NULL,NULL),(3011,NULL,NULL,NULL,NULL,NULL,'1',3010,254,NULL,NULL),(3012,NULL,NULL,NULL,NULL,NULL,'2',3010,260,NULL,NULL),(3013,NULL,NULL,NULL,NULL,NULL,'3',3010,264,NULL,NULL),(3014,NULL,NULL,NULL,NULL,NULL,'4',3010,267,NULL,NULL),(3016,NULL,NULL,NULL,NULL,NULL,'1',3015,254,NULL,NULL),(3017,NULL,NULL,NULL,NULL,NULL,'2',3015,260,NULL,NULL),(3018,NULL,NULL,NULL,NULL,NULL,'3',3015,264,NULL,NULL),(3019,NULL,NULL,NULL,NULL,NULL,'4',3015,267,NULL,NULL),(3058,NULL,NULL,NULL,NULL,NULL,NULL,3057,1302,NULL,NULL),(3059,NULL,NULL,NULL,NULL,NULL,NULL,3057,1801,NULL,NULL),(3061,NULL,100,NULL,NULL,NULL,NULL,3060,1302,NULL,NULL),(3062,NULL,NULL,NULL,NULL,NULL,'hggj',3060,1801,NULL,NULL),(3064,NULL,100,NULL,NULL,NULL,NULL,3063,1302,NULL,NULL),(3065,NULL,NULL,NULL,NULL,NULL,'hggj',3063,1801,NULL,NULL),(3108,NULL,NULL,NULL,NULL,NULL,NULL,3107,254,NULL,NULL),(3109,NULL,NULL,NULL,NULL,NULL,NULL,3107,260,NULL,NULL),(3110,NULL,NULL,NULL,NULL,NULL,NULL,3107,264,NULL,NULL),(3111,NULL,NULL,NULL,NULL,NULL,NULL,3107,267,NULL,NULL),(3113,NULL,NULL,NULL,NULL,NULL,'1',3112,254,NULL,NULL),(3114,NULL,NULL,NULL,NULL,NULL,'1',3112,260,NULL,NULL),(3115,NULL,NULL,NULL,NULL,NULL,'1',3112,264,NULL,NULL),(3116,NULL,NULL,NULL,NULL,NULL,'1',3112,267,NULL,NULL),(3118,NULL,NULL,NULL,NULL,NULL,NULL,3117,254,NULL,NULL),(3119,NULL,NULL,NULL,NULL,NULL,NULL,3117,260,NULL,NULL),(3120,NULL,NULL,NULL,NULL,NULL,NULL,3117,264,NULL,NULL),(3121,NULL,NULL,NULL,NULL,NULL,NULL,3117,267,NULL,NULL),(3123,NULL,NULL,NULL,NULL,NULL,'2',3122,254,NULL,NULL),(3124,NULL,NULL,NULL,NULL,NULL,'2',3122,260,NULL,NULL),(3125,NULL,NULL,NULL,NULL,NULL,'2',3122,264,NULL,NULL),(3126,NULL,NULL,NULL,NULL,NULL,'2',3122,267,NULL,NULL),(3128,NULL,NULL,NULL,NULL,NULL,NULL,3127,254,NULL,NULL),(3129,NULL,NULL,NULL,NULL,NULL,NULL,3127,260,NULL,NULL),(3130,NULL,NULL,NULL,NULL,NULL,NULL,3127,264,NULL,NULL),(3131,NULL,NULL,NULL,NULL,NULL,NULL,3127,267,NULL,NULL),(3133,NULL,NULL,NULL,NULL,NULL,'5',3132,254,NULL,NULL),(3134,NULL,NULL,NULL,NULL,NULL,'5',3132,260,NULL,NULL),(3135,NULL,NULL,NULL,NULL,NULL,'5',3132,264,NULL,NULL),(3136,NULL,NULL,NULL,NULL,NULL,'5',3132,267,NULL,NULL),(3138,NULL,NULL,NULL,NULL,NULL,'5',3137,254,NULL,NULL),(3139,NULL,NULL,NULL,NULL,NULL,'5',3137,260,NULL,NULL),(3140,NULL,NULL,NULL,NULL,NULL,'5',3137,264,NULL,NULL),(3141,NULL,NULL,NULL,NULL,NULL,'5',3137,267,NULL,NULL),(3155,NULL,NULL,NULL,NULL,NULL,NULL,3154,1302,NULL,NULL),(3156,NULL,NULL,NULL,NULL,NULL,NULL,3154,1801,NULL,NULL),(3158,NULL,120,NULL,NULL,NULL,NULL,3157,1302,NULL,NULL),(3159,NULL,NULL,NULL,NULL,NULL,'hghg',3157,1801,NULL,NULL),(3161,NULL,120,NULL,NULL,NULL,NULL,3160,1302,NULL,NULL),(3162,NULL,NULL,NULL,NULL,NULL,'hghg',3160,1801,NULL,NULL),(3208,NULL,NULL,NULL,NULL,NULL,NULL,3207,1353,NULL,NULL),(3209,NULL,NULL,NULL,NULL,NULL,NULL,3207,1354,NULL,NULL),(3210,NULL,NULL,NULL,NULL,NULL,NULL,3207,3066,NULL,NULL),(3212,NULL,NULL,NULL,NULL,NULL,NULL,3211,254,NULL,NULL),(3213,NULL,NULL,NULL,NULL,NULL,NULL,3211,260,NULL,NULL),(3214,NULL,NULL,NULL,NULL,NULL,NULL,3211,264,NULL,NULL),(3215,NULL,NULL,NULL,NULL,NULL,NULL,3211,267,NULL,NULL),(3217,NULL,NULL,NULL,NULL,NULL,'2',3216,254,NULL,NULL),(3218,NULL,NULL,NULL,NULL,NULL,'10',3216,260,NULL,NULL),(3219,NULL,NULL,NULL,NULL,NULL,'15',3216,264,NULL,NULL),(3220,NULL,NULL,NULL,NULL,NULL,'1',3216,267,NULL,NULL),(3305,NULL,NULL,NULL,NULL,NULL,NULL,3304,254,NULL,NULL),(3306,NULL,NULL,NULL,NULL,NULL,NULL,3304,260,NULL,NULL),(3307,NULL,NULL,NULL,NULL,NULL,NULL,3304,264,NULL,NULL),(3308,NULL,NULL,NULL,NULL,NULL,NULL,3304,267,NULL,NULL),(3310,NULL,NULL,NULL,NULL,NULL,NULL,3309,3103,NULL,NULL),(3311,NULL,NULL,NULL,NULL,NULL,NULL,3309,3105,NULL,NULL),(3312,NULL,NULL,NULL,NULL,NULL,NULL,3309,3303,NULL,NULL),(3314,NULL,NULL,NULL,NULL,NULL,NULL,3313,3103,NULL,NULL),(3315,NULL,NULL,NULL,NULL,NULL,NULL,3313,3105,NULL,NULL),(3316,NULL,NULL,NULL,NULL,NULL,NULL,3313,3303,NULL,NULL),(3318,NULL,NULL,NULL,NULL,NULL,NULL,3317,3103,NULL,NULL),(3319,NULL,NULL,NULL,NULL,NULL,NULL,3317,3105,NULL,NULL),(3320,NULL,NULL,NULL,NULL,NULL,NULL,3317,3303,NULL,NULL),(3322,NULL,NULL,NULL,NULL,NULL,NULL,3321,3103,NULL,NULL),(3323,NULL,NULL,NULL,NULL,NULL,NULL,3321,3105,NULL,NULL),(3324,NULL,NULL,NULL,NULL,NULL,NULL,3321,3303,NULL,NULL),(3326,NULL,NULL,NULL,NULL,NULL,NULL,3325,3103,NULL,NULL),(3327,NULL,NULL,NULL,NULL,NULL,NULL,3325,3105,NULL,NULL),(3328,NULL,NULL,NULL,NULL,NULL,NULL,3325,3303,NULL,NULL),(3330,NULL,NULL,NULL,NULL,NULL,NULL,3329,3103,NULL,NULL),(3331,NULL,NULL,NULL,NULL,NULL,NULL,3329,3105,NULL,NULL),(3332,NULL,NULL,NULL,NULL,NULL,NULL,3329,3303,NULL,NULL),(3334,NULL,NULL,NULL,NULL,NULL,NULL,3333,3103,NULL,NULL),(3335,NULL,NULL,NULL,NULL,NULL,NULL,3333,3105,NULL,NULL),(3336,NULL,NULL,NULL,NULL,NULL,'10',3333,3303,NULL,NULL),(3338,NULL,NULL,NULL,NULL,NULL,NULL,3337,3103,NULL,NULL),(3339,NULL,NULL,NULL,NULL,NULL,NULL,3337,3105,NULL,NULL),(3340,NULL,NULL,NULL,NULL,NULL,'10',3337,3303,NULL,NULL),(3342,NULL,NULL,NULL,NULL,NULL,NULL,3341,3103,NULL,NULL),(3343,NULL,NULL,NULL,NULL,NULL,NULL,3341,3105,NULL,NULL),(3344,NULL,NULL,NULL,NULL,NULL,NULL,3341,3303,NULL,NULL),(3346,NULL,NULL,NULL,NULL,NULL,NULL,3345,3103,NULL,NULL),(3347,NULL,NULL,NULL,NULL,NULL,NULL,3345,3105,NULL,NULL),(3348,NULL,NULL,NULL,NULL,NULL,'20',3345,3303,NULL,NULL),(3350,NULL,NULL,NULL,NULL,NULL,NULL,3349,3103,NULL,NULL),(3351,NULL,NULL,NULL,NULL,NULL,NULL,3349,3105,NULL,NULL),(3352,NULL,NULL,NULL,NULL,NULL,'20',3349,3303,NULL,NULL),(3354,NULL,NULL,NULL,NULL,NULL,NULL,3353,3103,NULL,NULL),(3355,NULL,NULL,NULL,NULL,NULL,NULL,3353,3105,NULL,NULL),(3356,NULL,NULL,NULL,NULL,NULL,NULL,3353,3303,NULL,NULL),(3358,NULL,NULL,NULL,NULL,NULL,NULL,3357,3103,NULL,NULL),(3359,NULL,NULL,NULL,NULL,NULL,NULL,3357,3105,NULL,NULL),(3360,NULL,NULL,NULL,NULL,NULL,'10',3357,3303,NULL,NULL),(3362,NULL,NULL,NULL,NULL,NULL,NULL,3361,3103,NULL,NULL),(3363,NULL,NULL,NULL,NULL,NULL,NULL,3361,3105,NULL,NULL),(3364,NULL,NULL,NULL,NULL,NULL,'10',3361,3303,NULL,NULL),(3453,NULL,NULL,NULL,NULL,NULL,NULL,3452,1302,NULL,NULL),(3454,NULL,NULL,NULL,NULL,NULL,NULL,3452,1801,NULL,NULL),(3456,NULL,10,NULL,NULL,NULL,NULL,3455,1302,NULL,NULL),(3457,NULL,NULL,NULL,NULL,NULL,'hjghj',3455,1801,NULL,NULL),(3459,NULL,10,NULL,NULL,NULL,NULL,3458,1302,NULL,NULL),(3460,NULL,NULL,NULL,NULL,NULL,'hjghj',3458,1801,NULL,NULL),(3462,NULL,NULL,NULL,NULL,NULL,NULL,3461,1302,NULL,NULL),(3463,NULL,NULL,NULL,NULL,NULL,NULL,3461,1801,NULL,NULL),(3465,NULL,12,NULL,NULL,NULL,NULL,3464,1302,NULL,NULL),(3466,NULL,NULL,NULL,NULL,NULL,'',3464,1801,NULL,NULL),(3468,NULL,12,NULL,NULL,NULL,NULL,3467,1302,NULL,NULL),(3469,NULL,NULL,NULL,NULL,NULL,'',3467,1801,NULL,NULL),(3471,NULL,NULL,NULL,NULL,NULL,NULL,3470,1302,NULL,NULL),(3472,NULL,NULL,NULL,NULL,NULL,NULL,3470,1801,NULL,NULL),(3474,NULL,10,NULL,NULL,NULL,NULL,3473,1302,NULL,NULL),(3475,NULL,NULL,NULL,NULL,NULL,'565',3473,1801,NULL,NULL),(3477,NULL,10,NULL,NULL,NULL,NULL,3476,1302,NULL,NULL),(3478,NULL,NULL,NULL,NULL,NULL,'565',3476,1801,NULL,NULL),(3480,NULL,NULL,NULL,NULL,NULL,NULL,3479,1302,NULL,NULL),(3481,NULL,NULL,NULL,NULL,NULL,NULL,3479,1801,NULL,NULL),(3483,NULL,500,NULL,NULL,NULL,NULL,3482,1302,NULL,NULL),(3484,NULL,NULL,NULL,NULL,NULL,'',3482,1801,NULL,NULL),(3486,NULL,500,NULL,NULL,NULL,NULL,3485,1302,NULL,NULL),(3487,NULL,NULL,NULL,NULL,NULL,'',3485,1801,NULL,NULL);
/*!40000 ALTER TABLE `filledhealthformitemvalue` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-05-09 18:24:22
