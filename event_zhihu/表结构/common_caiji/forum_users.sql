/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : common_caiji

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2019-12-30 17:02:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for forum_users
-- ----------------------------
DROP TABLE IF EXISTS `forum_users`;
CREATE TABLE `forum_users` (
  `fuid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `fhid` int(10) NOT NULL COMMENT '站点id，对应forum_hosts主键',
  `username` varchar(500) NOT NULL COMMENT '作者名',
  `gender` varchar(20) DEFAULT NULL COMMENT '作者性别',
  `registdate` datetime DEFAULT NULL COMMENT '注册时间',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ号码',
  `msn` varchar(200) DEFAULT NULL COMMENT 'msn号码',
  `email` varchar(200) DEFAULT NULL COMMENT 'Email地址',
  `indexurl` varchar(1000) DEFAULT NULL COMMENT '作者论坛地址',
  `blogurl` varchar(1000) DEFAULT NULL COMMENT '博客地址',
  `signature` varchar(2000) DEFAULT NULL COMMENT '签名',
  `note` text COMMENT '作者简介',
  `inserttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `urlmd5` varchar(64) NOT NULL COMMENT '作者url的MD5值',
  `last_ip` varchar(50) DEFAULT NULL COMMENT '作者最后登录ip',
  `last_logintime` datetime DEFAULT NULL COMMENT '最后登录时间',
  `post_total` int(8) DEFAULT NULL COMMENT '作者发帖总数',
  `reply_total` int(11) DEFAULT NULL COMMENT '作者发布回帖总数',
  `login_times` int(10) DEFAULT NULL COMMENT '作者登录次数',
  `author_url` varchar(1000) DEFAULT NULL COMMENT '作者url',
  `imgheadurl` varchar(1024) DEFAULT NULL COMMENT '作者头像url',
  PRIMARY KEY (`fuid`),
  KEY `forum_users_index_urlmd5` (`urlmd5`)
) ENGINE=InnoDB AUTO_INCREMENT=44026 DEFAULT CHARSET=utf8 COMMENT='论坛帖子作者信息，包含主帖和回帖作者';
