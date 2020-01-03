/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : event_data

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2019-12-30 17:03:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for event_info
-- ----------------------------
DROP TABLE IF EXISTS `event_info`;
CREATE TABLE `event_info` (
  `eid` int(11) NOT NULL AUTO_INCREMENT COMMENT '事件Id',
  `name` varchar(100) NOT NULL COMMENT '事件名称',
  `start_time` datetime NOT NULL COMMENT '关注开始时间',
  `end_time` datetime NOT NULL COMMENT '关注结束时间',
  `language` int(11) NOT NULL DEFAULT '0' COMMENT '语种：0-中英；1-泰；2-越；3-老挝',
  `category` int(11) NOT NULL COMMENT '专题分类',
  `location` int(11) NOT NULL DEFAULT '0' COMMENT '事件爆发地',
  `description` varchar(1024) DEFAULT NULL COMMENT '摘要',
  `pic` varchar(1024) DEFAULT NULL COMMENT '图片URL',
  `pub_level` int(11) NOT NULL DEFAULT '0' COMMENT '公开级别：0-仅自己，1-部门，2-单位',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类型：0-已发生，1-预埋',
  `alarm_type` int(11) NOT NULL DEFAULT '0' COMMENT '预警分类：0-站内弹窗，1-短信',
  `site_type` int(11) NOT NULL DEFAULT '0' COMMENT '站点选择：0-全网数据，1-特定站点',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：1-正常，0-删除',
  `dep_id` int(11) NOT NULL COMMENT '部门id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近修改时间',
  `keywords` text COMMENT '关键词 JSON格式，包含type，kws属性值，其中type关键词配置类型：1-普通模式，2-高级模式，3-智能模式	如：	{"type":"1","kws":"(内蒙古);(呼和浩特)"}',
  `support_level` int(11) DEFAULT NULL COMMENT '极性：1-正；2-负；3-中',
  `hot_sensitive` int(3) DEFAULT NULL COMMENT '热敏类型：0-全部，1-热点，2-敏感',
  `is_cyc` int(3) DEFAULT NULL COMMENT '是否周期事件：0-非周期，1-周期',
  `dt_eid` varchar(200) DEFAULT NULL COMMENT '舆情处项目dt的专题id',
  `dt_userid` int(11) DEFAULT NULL COMMENT '舆情除项目dt传过来的userid',
  `city_level` int(11) DEFAULT NULL COMMENT '事发地的城市级别（0-超一线，1-一线，2-二线，3-三线，4-四线，5-五线，6-其他）',
  `city_area` int(11) DEFAULT NULL COMMENT '事发地的城市区域（0-市区，1-郊区，2-全范围）',
  `damage_level` int(11) DEFAULT NULL COMMENT '事件造成的伤害级别（0-无伤害，1-轻伤，2-重伤，3-个体死亡，4-群体死亡，5-财物损失个人，6-财物损失群体）',
  PRIMARY KEY (`eid`)
) ENGINE=InnoDB AUTO_INCREMENT=2456 DEFAULT CHARSET=utf8 COMMENT='事件信息表';
