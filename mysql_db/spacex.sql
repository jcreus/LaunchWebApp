-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: spacex
-- ------------------------------------------------------
-- Server version	5.5.41-0ubuntu0.14.04.1

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
-- Table structure for table `LaunchSite`
--

DROP TABLE IF EXISTS `LaunchSite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LaunchSite` (
  `launch_site_id` int(11) DEFAULT NULL,
  `launch_site_name` varchar(50) DEFAULT NULL,
  `launch_site_code` varchar(4) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LaunchSite`
--

LOCK TABLES `LaunchSite` WRITE;
/*!40000 ALTER TABLE `LaunchSite` DISABLE KEYS */;
INSERT INTO `LaunchSite` VALUES (1,'Omelek Island, Kwajalein Atoll','OMLK',9.05,-167.74);
INSERT INTO `LaunchSite` VALUES (2,'CCAFS SLC-40','LC40',28.56,-80.57);
INSERT INTO `LaunchSite` VALUES (3,'Vandenburg SLC-4E','LC4E',34.63,-120.61);
INSERT INTO `LaunchSite` VALUES (4,'KSC 39-A','K39A',28.61,-80.6);
/*!40000 ALTER TABLE `LaunchSite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `corrections`
--

DROP TABLE IF EXISTS `corrections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `corrections` (
  `launch_id` int(11) DEFAULT NULL,
  `correction` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `corrections`
--

LOCK TABLES `corrections` WRITE;
/*!40000 ALTER TABLE `corrections` DISABLE KEYS */;
INSERT INTO `corrections` VALUES (1,NULL);
INSERT INTO `corrections` VALUES (2,NULL);
INSERT INTO `corrections` VALUES (3,NULL);
INSERT INTO `corrections` VALUES (4,NULL);
INSERT INTO `corrections` VALUES (5,NULL);
INSERT INTO `corrections` VALUES (6,NULL);
INSERT INTO `corrections` VALUES (7,NULL);
INSERT INTO `corrections` VALUES (8,NULL);
INSERT INTO `corrections` VALUES (9,NULL);
INSERT INTO `corrections` VALUES (10,NULL);
INSERT INTO `corrections` VALUES (11,NULL);
INSERT INTO `corrections` VALUES (12,'400:2:pitch:0.0;450:2:pitch:-0.1');
INSERT INTO `corrections` VALUES (13,'300:2:pitch:0.0;350:2:pitch:-0.1;400:2:pitch:-0.2');
INSERT INTO `corrections` VALUES (14,NULL);
INSERT INTO `corrections` VALUES (15,NULL);
INSERT INTO `corrections` VALUES (16,NULL);
INSERT INTO `corrections` VALUES (17,NULL);
INSERT INTO `corrections` VALUES (18,'310:2:pitch:-0.5;365:2:throttle:80;510:2:pitch:-0.3');
INSERT INTO `corrections` VALUES (19,'275:2:pitch:0.1');
INSERT INTO `corrections` VALUES (20,NULL);
INSERT INTO `corrections` VALUES (21,'200:2:pitch:0.0;300:2:pitch:-0.1');
/*!40000 ALTER TABLE `corrections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `launches`
--

DROP TABLE IF EXISTS `launches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `launches` (
  `launch_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `provider` varchar(20) DEFAULT NULL,
  `provider_id` int(11) DEFAULT NULL,
  `vehicle` varchar(20) DEFAULT NULL,
  `vehicle_id` int(11) DEFAULT NULL,
  `cargo` varchar(20) DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `launches`
--

LOCK TABLES `launches` WRITE;
/*!40000 ALTER TABLE `launches` DISABLE KEYS */;
INSERT INTO `launches` VALUES (1,'2006-03-24','SpaceX',0,'Falcon1',0,'FalconSAT-2','FSAT');
INSERT INTO `launches` VALUES (2,'2007-03-21','SpaceX',0,'Falcon1',0,'DemoSat','DEMO');
INSERT INTO `launches` VALUES (3,'2008-08-03','SpaceX',0,'Falcon1',0,'Trailblazer','TBZR');
INSERT INTO `launches` VALUES (4,'2008-09-28','SpaceX',0,'Falcon1',0,'RatSat','RATS');
INSERT INTO `launches` VALUES (5,'2009-07-14','SpaceX',0,'Falcon1',0,'RazakSAT','RAZS');
INSERT INTO `launches` VALUES (6,'2010-06-04','SpaceX',0,'Falcon9',1,'Dragon','F9F1');
INSERT INTO `launches` VALUES (7,'2010-12-08','SpaceX',0,'Falcon9',1,'COTS-1','COT1');
INSERT INTO `launches` VALUES (8,'2012-05-22','SpaceX',0,'Falcon9',1,'COTS-2+','COT2');
INSERT INTO `launches` VALUES (9,'2012-10-08','SpaceX',0,'Falcon9',1,'CRS-1','CRS1');
INSERT INTO `launches` VALUES (10,'2013-03-01','SpaceX',0,'Falcon9',1,'CRS-2','CRS2');
INSERT INTO `launches` VALUES (11,'2013-09-29','SpaceX',0,'Falcon9',2,'CASSIOPE','CASS');
INSERT INTO `launches` VALUES (12,'2013-12-03','SpaceX',0,'Falcon9',2,'SES-8','SES8');
INSERT INTO `launches` VALUES (13,'2014-01-06','SpaceX',0,'Falcon9',2,'Thaicom-6','THM6');
INSERT INTO `launches` VALUES (14,'2014-04-18','SpaceX',0,'Falcon9',2,'CRS-3','CRS3');
INSERT INTO `launches` VALUES (15,'2014-07-14','SpaceX',0,'Falcon9',2,'OG2-1','OG21');
INSERT INTO `launches` VALUES (16,'2014-08-05','SpaceX',0,'Falcon9',2,'AsiaSat-8','AST8');
INSERT INTO `launches` VALUES (17,'2014-09-07','SpaceX',0,'Falcon9',2,'AsiaSat-6','AST6');
INSERT INTO `launches` VALUES (18,'2014-09-21','SpaceX',0,'Falcon9',2,'CRS-4','CRS4');
INSERT INTO `launches` VALUES (19,'2015-01-10','SpaceX',0,'Falcon9',2,'CRS-5','CRS5');
INSERT INTO `launches` VALUES (20,'2015-02-09','SpaceX',0,'Falcon9',2,'DSCOVR','DSCR');
INSERT INTO `launches` VALUES (21,'2015-03-02','SpaceX',0,'Falcon9',2,'EutelSat/ABS','EUAB');
/*!40000 ALTER TABLE `launches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profiles`
--

DROP TABLE IF EXISTS `profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profiles` (
  `launch_id` int(11) DEFAULT NULL,
  `Mass` double DEFAULT NULL,
  `Legs` tinyint(1) DEFAULT NULL,
  `MEI_1` double DEFAULT NULL,
  `MECO_1` double DEFAULT NULL,
  `MEI_2` double DEFAULT NULL,
  `MECO_2` double DEFAULT NULL,
  `MEI_3` double DEFAULT NULL,
  `MECO_3` double DEFAULT NULL,
  `PitchKick` double DEFAULT NULL,
  `Pitch` double DEFAULT NULL,
  `Yaw` double DEFAULT NULL,
  `StageSep` double DEFAULT NULL,
  `SEI_1` double DEFAULT NULL,
  `SECO_1` double DEFAULT NULL,
  `MEI_4` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES (1,-1,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (2,-1,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (3,-1,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (4,165,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (5,180,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (6,-1,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (7,4200,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (8,4200,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (9,4200,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (10,4775,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (11,500,0,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (12,3170,0,-2,167,0,0,0,0,7,0.055,-0.06,169,171,0,0);
INSERT INTO `profiles` VALUES (13,3325,0,-2,177,0,0,0,0,7,0.055,-0.06,180,186,0,0);
INSERT INTO `profiles` VALUES (14,6289,1,-2,167,0,0,0,0,7,0.065,0.78,169,171,0,0);
INSERT INTO `profiles` VALUES (15,1032,1,-2,161,515,552,628,0,7,0.025,0.78,163,165,0,0);
INSERT INTO `profiles` VALUES (16,4535,0,-2,180,0,0,0,0,7,0.045,-0.07,182,189,0,0);
INSERT INTO `profiles` VALUES (17,4428,0,-2,180,0,0,0,0,7,0.027,-0.06,182,189,0,0);
INSERT INTO `profiles` VALUES (18,6416,1,-2,167,276,291,439,461,7,0.063,0.78,169,177,0,0);
INSERT INTO `profiles` VALUES (19,6517,1,-2,163,276,291,439,461,7,0.045,0.82,165,173,0,530);
INSERT INTO `profiles` VALUES (20,570,1,-2,166,NULL,NULL,420,445,7,0.07,0.5,170,177,0,460);
INSERT INTO `profiles` VALUES (21,4200,0,-2,180,0,0,0,0,7,0.045,0,182,189,0,0);
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-04 18:56:11
