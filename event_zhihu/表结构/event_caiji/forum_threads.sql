/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : event_caiji

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2019-12-30 17:03:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for forum_threads
-- ----------------------------
DROP TABLE IF EXISTS `forum_threads`;
CREATE TABLE `forum_threads` (
  `ftid` bigint(32) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `title` varchar(1500) NOT NULL COMMENT '主帖标题',
  `content` longtext NOT NULL COMMENT '主帖内容',
  `fuid` int(11) DEFAULT NULL COMMENT '主帖作者id,对应forum_users表主键',
  `author` varchar(500) DEFAULT NULL COMMENT '主帖作者',
  `pubtime` datetime DEFAULT NULL COMMENT '主帖发布时间',
  `pic` text COMMENT '主帖正文图片链接，多个图片之间"#"分割',
  `picpath` text COMMENT '图片本地保存路径，多个图片之间"#"间隔',
  `reviewcount` int(10) DEFAULT '0' COMMENT '主帖点击量',
  `replycount` int(10) DEFAULT '0' COMMENT '主帖回复量',
  `lastreplyauthor` varchar(500) DEFAULT NULL COMMENT '主帖最好回复作者',
  `lastreplytime` datetime DEFAULT NULL COMMENT '主帖最好回复时间',
  `fbid` int(10) NOT NULL COMMENT '版块id，对应版块表主键',
  `genus` int(10) NOT NULL COMMENT '帖子类型',
  `site` varchar(300) DEFAULT NULL COMMENT '帖子所属站点',
  `url` varchar(2000) NOT NULL COMMENT '来源url',
  `intime` datetime NOT NULL COMMENT '插入时间',
  `urlmd5` varchar(200) DEFAULT NULL COMMENT '帖子url的MD5，用于排重\r\n            ',
  `keywords` varchar(1024) DEFAULT NULL COMMENT '关键词',
  `sengineid` int(10) DEFAULT '0' COMMENT '站点id，对应forum_templates_engine表主键',
  `keywords_id` int(10) DEFAULT NULL COMMENT '关键词id',
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '本条记录的最后修改时间',
  `status` int(1) DEFAULT NULL COMMENT '主帖状态:1-存在;0-已删除',
  `snapshot` longtext COMMENT '主帖快照',
  `ip` varchar(50) DEFAULT NULL COMMENT '发帖人ip',
  `forum_total` int(5) DEFAULT NULL COMMENT '帖子作者在本主帖中发言数',
  `picture_total` int(5) DEFAULT NULL COMMENT '帖子作者发图数',
  `fhid` int(10) DEFAULT NULL COMMENT '站点id字段',
  `nation_category` int(11) DEFAULT NULL COMMENT '1-境外，2-境内',
  `source_type` int(2) DEFAULT NULL COMMENT '1-云推送，2-本地',
  PRIMARY KEY (`ftid`),
  UNIQUE KEY `urlmd5` (`urlmd5`),
  KEY `index_fbid` (`fbid`),
  KEY `index_ft_keyword` (`keywords`(255)),
  KEY `index_ft_sengineid` (`sengineid`),
  KEY `index_threads_fuid` (`fuid`),
  KEY `idx_pubtime` (`pubtime`),
  KEY `idx_intime` (`intime`) USING BTREE,
  KEY `idx_keywords_id` (`keywords_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13222 DEFAULT CHARSET=utf8 COMMENT='论坛主帖表';
