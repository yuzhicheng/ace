/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50615
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50615
File Encoding         : 65001

Date: 2017-04-17 13:53:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `paints`
-- ----------------------------
DROP TABLE IF EXISTS `paints`;
CREATE TABLE `paints` (
  `identifier` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `appreciation` varchar(255) DEFAULT NULL,
  `author_id` varchar(255) DEFAULT NULL,
  `background` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `copyright` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `current_location` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `estatus` varchar(255) DEFAULT NULL,
  `height` varchar(255) DEFAULT NULL,
  `medium` varchar(255) DEFAULT NULL,
  `object_type` varchar(255) DEFAULT NULL,
  `popular_culture` varchar(255) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `provider_source` varchar(255) DEFAULT NULL,
  `skill` varchar(255) DEFAULT NULL,
  `specific_source` varchar(255) DEFAULT NULL,
  `title_cn` varchar(255) DEFAULT NULL,
  `width` varchar(255) DEFAULT NULL,
  `write_date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`identifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;