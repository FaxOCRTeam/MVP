DROP TABLE IF EXISTS `laboratory_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `laboratory_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Patient_name` varchar(200) NOT NULL,
  `Phone_number` varchar(200) NOT NULL,
  `Patient_ID` varchar(200) NOT NULL,
  `Physician` varchar(200) DEFAULT NULL,
  `Sex` varchar(100) NOT NULL,
  `Age` varchar(200) NOT NULL,
  `Date_of_birth` varchar(200) DEFAULT NULL,
  `Sodium` varchar(200) DEFAULT NULL,
  `Potassium` varchar(200) DEFAULT NULL,
  `Chloride` varchar(200) DEFAULT NULL,
  `Carbon_dioxide` varchar(200) DEFAULT NULL,
  `Calcium` varchar(200) DEFAULT NULL,
  `Alkaline_phosphatase` varchar(200) DEFAULT NULL,
  `AST` varchar(200) DEFAULT NULL,
  `ALT` varchar(200) DEFAULT NULL,
  `Bilirubin_total` varchar(200) DEFAULT NULL,
  `Glucose` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;