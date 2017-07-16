/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50615
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50615
File Encoding         : 65001

Date: 2017-04-17 14:34:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tech_infos`
-- ----------------------------
DROP TABLE IF EXISTS `tech_infos`;
CREATE TABLE `tech_infos` (
  `identifier` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `entry` varchar(255) DEFAULT NULL,
  `format` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `md5` varchar(255) DEFAULT NULL,
  `printable` bit(1) DEFAULT NULL,
  `requirements` longtext,
  `res_type` varchar(255) DEFAULT NULL,
  `resource` varchar(255) DEFAULT NULL,
  `secure_key` varchar(255) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`identifier`),
  KEY `resource` (`resource`),
  KEY `idx_techInfo_printable` (`printable`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tech_infos
-- ----------------------------
