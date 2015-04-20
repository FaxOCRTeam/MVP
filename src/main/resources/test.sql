DROP TABLE IF EXISTS `test_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_form` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Field1` varchar(200) NOT NULL,
  `Field2` varchar(200) NOT NULL,
  `Field3` varchar(200) NOT NULL,
  `Field4` varchar(1000) DEFAULT NULL,
  `Field5` varchar(100) NOT NULL,
  `Field6` varchar(1000) DEFAULT NULL,
  `Field7` varchar(200) DEFAULT NULL,
  `Field8` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;