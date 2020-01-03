/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : event_data

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2019-12-30 17:04:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for event_keywords
-- ----------------------------
DROP TABLE IF EXISTS `event_keywords`;
CREATE TABLE `event_keywords` (
  `kid` int(11) NOT NULL AUTO_INCREMENT COMMENT '关键词Id',
  `eid` int(11) NOT NULL COMMENT '事件Id',
  `keywords` varchar(100) NOT NULL COMMENT '关键词',
  `deleted` smallint(2) NOT NULL DEFAULT '0' COMMENT '0-正常\r\n1-删除，\r\n主要用户专题关键词的删除与恢复',
  `not_keywords` text COMMENT '表示“非”关系的关键词',
  PRIMARY KEY (`kid`),
  KEY `idx_key_eid_del` (`keywords`,`eid`,`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=7854 DEFAULT CHARSET=utf8 COMMENT='事件关键词表';
