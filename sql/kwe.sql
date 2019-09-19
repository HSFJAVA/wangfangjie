/*
Navicat MySQL Data Transfer

Source Server         : 192.168.10.12
Source Server Version : 50616
Source Host           : 192.168.10.12:3306
Source Database       : kwe

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2019-09-19 15:59:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for base_jcsj_inspection
-- ----------------------------
DROP TABLE IF EXISTS `base_jcsj_inspection`;
CREATE TABLE `base_jcsj_inspection` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `Inspection_type` int(10) DEFAULT NULL COMMENT '商检类型',
  `Inspection_name` varchar(128) DEFAULT NULL COMMENT '商检名称',
  `Inspection_code` varchar(128) DEFAULT NULL COMMENT '商检代码',
  `customs_name` varchar(128) DEFAULT NULL COMMENT '海关名称',
  `customs_code` varchar(128) DEFAULT NULL COMMENT '海关代码',
  `remake` varchar(150) DEFAULT NULL,
  `ctime` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `cname` varchar(32) DEFAULT NULL COMMENT '创建人',
  `utime` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `uname` varchar(32) DEFAULT NULL COMMENT '更新人',
  `name` varchar(255) DEFAULT NULL,
  `ncode` varchar(255) DEFAULT NULL,
  `en` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_Inspection_type_code` (`Inspection_type`,`Inspection_code`) USING BTREE,
  KEY `indexc_type` (`Inspection_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=43823 DEFAULT CHARSET=utf8mb4 COMMENT='商检参数';

-- ----------------------------
-- Table structure for jintie_base_input_field
-- ----------------------------
DROP TABLE IF EXISTS `jintie_base_input_field`;
CREATE TABLE `jintie_base_input_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jintie_field` varchar(255) DEFAULT NULL COMMENT '近铁字段',
  `field` varchar(255) DEFAULT NULL,
  `remarks1` varchar(255) DEFAULT NULL,
  `remarks2` varchar(255) DEFAULT NULL,
  `remarks3` varchar(255) DEFAULT NULL,
  `remarks4` varchar(255) DEFAULT NULL,
  `remarks5` varchar(255) DEFAULT NULL,
  `remarks6` varchar(255) DEFAULT NULL,
  `remarks7` varchar(255) DEFAULT NULL,
  `remarks8` varchar(255) DEFAULT NULL,
  `remarks9` varchar(255) DEFAULT NULL,
  `remarks10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_base_output_field
-- ----------------------------
DROP TABLE IF EXISTS `jintie_base_output_field`;
CREATE TABLE `jintie_base_output_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jintie_field` varchar(255) DEFAULT NULL COMMENT '近铁字段',
  `field` varchar(255) DEFAULT NULL,
  `remarks1` varchar(255) DEFAULT NULL,
  `remarks2` varchar(255) DEFAULT NULL,
  `remarks3` varchar(255) DEFAULT NULL,
  `remarks4` varchar(255) DEFAULT NULL,
  `remarks5` varchar(255) DEFAULT NULL,
  `remarks6` varchar(255) DEFAULT NULL,
  `remarks7` varchar(255) DEFAULT NULL,
  `remarks8` varchar(255) DEFAULT NULL,
  `remarks9` varchar(255) DEFAULT NULL,
  `remarks10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_custom
-- ----------------------------
DROP TABLE IF EXISTS `jintie_custom`;
CREATE TABLE `jintie_custom` (
  `customId` bigint(11) NOT NULL AUTO_INCREMENT,
  `customName` varchar(200) DEFAULT NULL,
  `key` varchar(100) DEFAULT NULL,
  `remark1` varchar(200) DEFAULT NULL,
  `remark2` varchar(200) DEFAULT NULL,
  `remark3` varchar(200) DEFAULT NULL,
  `remark4` varchar(200) DEFAULT NULL,
  `remark5` varchar(200) DEFAULT NULL,
  `remark6` varchar(200) DEFAULT NULL,
  `remark7` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`customId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_custom_field
-- ----------------------------
DROP TABLE IF EXISTS `jintie_custom_field`;
CREATE TABLE `jintie_custom_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `customId` int(11) DEFAULT NULL COMMENT 'customId',
  `jintie_field` varchar(255) DEFAULT NULL COMMENT 'jintie字段',
  `field` varchar(100) DEFAULT NULL COMMENT 'field',
  `remarks1` varchar(100) DEFAULT NULL COMMENT '客户ID',
  `remarks2` varchar(100) DEFAULT NULL COMMENT '字段类别 0进 1 出 2不启用',
  `remarks3` varchar(100) DEFAULT NULL COMMENT '预留3',
  `remarks4` varchar(100) DEFAULT NULL COMMENT '预留4',
  `remarks5` varchar(100) DEFAULT NULL COMMENT '预留5',
  `remarks6` varchar(100) DEFAULT NULL COMMENT '预留6',
  `remarks7` varchar(100) DEFAULT NULL COMMENT '预留7',
  `remarks8` varchar(100) DEFAULT NULL COMMENT '预留8',
  `remarks9` varchar(100) DEFAULT NULL COMMENT '预留9',
  `remarks10` varchar(100) DEFAULT NULL COMMENT '预留10',
  `remarks11` varchar(100) DEFAULT NULL COMMENT '预留11',
  `remarks12` varchar(100) DEFAULT NULL COMMENT '预留12',
  `remarks13` varchar(100) DEFAULT NULL COMMENT '预留13',
  `remarks14` varchar(100) DEFAULT NULL COMMENT '预留14',
  `remarks15` varchar(100) DEFAULT NULL COMMENT '预留15',
  `remarks16` varchar(100) DEFAULT NULL COMMENT '预留16',
  `remarks17` varchar(100) DEFAULT NULL COMMENT '预留17',
  `remarks18` varchar(100) DEFAULT NULL COMMENT '预留18',
  `remarks19` varchar(100) DEFAULT NULL COMMENT '预留19',
  `remarks20` varchar(100) DEFAULT NULL COMMENT '预留20',
  `remarks21` varchar(100) DEFAULT NULL COMMENT '预留21',
  `remarks22` varchar(100) DEFAULT NULL COMMENT '预留22',
  `remarks23` varchar(100) DEFAULT NULL COMMENT '预留23',
  `remarks24` varchar(100) DEFAULT NULL COMMENT '预留24',
  `remarks25` varchar(100) DEFAULT NULL COMMENT '预留25',
  `remarks26` varchar(100) DEFAULT NULL COMMENT '预留26',
  `remarks27` varchar(100) DEFAULT NULL COMMENT '预留27',
  `remarks28` varchar(100) DEFAULT NULL COMMENT '预留28',
  `remarks29` varchar(100) DEFAULT NULL COMMENT '预留29',
  `remarks30` varchar(100) DEFAULT NULL COMMENT '预留30',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2757 DEFAULT CHARSET=utf8 COMMENT='客户字段表';

-- ----------------------------
-- Table structure for jintie_dict
-- ----------------------------
DROP TABLE IF EXISTS `jintie_dict`;
CREATE TABLE `jintie_dict` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `Inspection_type` int(10) DEFAULT NULL COMMENT '商检类型',
  `Inspection_name` varchar(128) DEFAULT NULL COMMENT '商检名称',
  `Inspection_code` varchar(128) DEFAULT NULL COMMENT '商检代码',
  `customs_name` varchar(128) DEFAULT NULL COMMENT '海关名称',
  `customs_code` varchar(128) DEFAULT NULL COMMENT '海关代码',
  `remake` varchar(150) DEFAULT NULL,
  `ctime` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `cname` varchar(32) DEFAULT NULL COMMENT '创建人',
  `utime` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `uname` varchar(32) DEFAULT NULL COMMENT '更新人',
  `name` varchar(255) DEFAULT NULL,
  `ncode` varchar(255) DEFAULT NULL,
  `en` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_Inspection_type_code` (`Inspection_type`,`Inspection_code`) USING BTREE,
  KEY `indexc_type` (`Inspection_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=50127 DEFAULT CHARSET=utf8mb4 COMMENT='商检参数';

-- ----------------------------
-- Table structure for jintie_input_detail
-- ----------------------------
DROP TABLE IF EXISTS `jintie_input_detail`;
CREATE TABLE `jintie_input_detail` (
  `pid` bigint(11) NOT NULL AUTO_INCREMENT,
  `MESSAGEHEAD` varchar(100) DEFAULT NULL,
  `MESSAGETYPE` varchar(100) DEFAULT NULL,
  `MESSAGEDATE` varchar(100) DEFAULT NULL,
  `MESSAGETIME` varchar(100) DEFAULT NULL,
  `UPDATEFLAG` varchar(100) DEFAULT NULL,
  `DOCLINENO` varchar(100) DEFAULT NULL,
  `CUSTOMERLINENO` varchar(100) DEFAULT NULL,
  `STOREID` varchar(100) DEFAULT NULL,
  `SKU` varchar(100) DEFAULT NULL,
  `BRAND` varchar(256) DEFAULT NULL,
  `MODEL` varchar(256) DEFAULT NULL,
  `HSCODE` varchar(100) DEFAULT NULL,
  `CHINESENAME` varchar(256) DEFAULT NULL,
  `SPECIFICATIONS` varchar(256) DEFAULT NULL,
  `DECLARATIONELEMENT` varchar(512) DEFAULT NULL,
  `PONO` varchar(100) DEFAULT NULL,
  `EXPECTEDQTY` varchar(100) DEFAULT NULL,
  `LOTTABLE01` varchar(100) DEFAULT NULL,
  `LOTTABLE02` varchar(100) DEFAULT NULL,
  `LOTTABLE03` varchar(100) DEFAULT NULL,
  `LOTTABLE04` varchar(100) DEFAULT NULL,
  `LOTTABLE05` varchar(100) DEFAULT NULL,
  `LOTTABLE06` varchar(100) DEFAULT NULL,
  `LOTTABLE07` varchar(100) DEFAULT NULL,
  `LOTTABLE08` varchar(100) DEFAULT NULL,
  `LOTTABLE09` varchar(100) DEFAULT NULL,
  `LOTTABLE10` varchar(100) DEFAULT NULL,
  `LOTTABLE11` varchar(100) DEFAULT NULL,
  `LOTTABLE12` varchar(100) DEFAULT NULL,
  `DAMAGEDIND` varchar(100) DEFAULT NULL,
  `DAMAGEDCD` varchar(100) DEFAULT NULL,
  `RCVDWITHEXCPTN` decimal(18,5) DEFAULT NULL,
  `REASON` varchar(512) DEFAULT NULL,
  `D_USERDEFINE1` decimal(18,5) DEFAULT NULL,
  `D_USERDEFINE2` decimal(18,5) DEFAULT NULL,
  `RECEIVEDTIME` varchar(100) DEFAULT NULL,
  `ID` varchar(100) DEFAULT NULL,
  `TOID` varchar(100) DEFAULT NULL,
  `CARTONQTY` varchar(100) DEFAULT NULL,
  `DECLARATIONUNIT` varchar(100) DEFAULT NULL,
  `COUNTRYOFORIGIN` varchar(100) DEFAULT NULL,
  `CURRENCY` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT1` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT2` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT2QTY` int(10) DEFAULT NULL,
  `CUBIC` decimal(18,3) DEFAULT NULL,
  `TOTALCUBIC` decimal(18,3) DEFAULT NULL,
  `GROSSWEIGHT` decimal(18,5) DEFAULT NULL,
  `TOTALGROSSWEIGHT` decimal(18,5) DEFAULT NULL,
  `NETWEIGHT` decimal(18,5) DEFAULT NULL,
  `TOTALNETWEIGHT` decimal(18,5) DEFAULT NULL,
  `PRICE` decimal(18,6) DEFAULT NULL,
  `TOTALPRICE` decimal(18,3) DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  `taketime` datetime DEFAULT NULL COMMENT '取走日期',
  `wl_ywno` varchar(100) DEFAULT NULL,
  `DNCODE` varchar(100) DEFAULT NULL,
  `dimflag` varchar(255) DEFAULT NULL COMMENT '模糊标记',
  `COPYLINENO` varchar(100) DEFAULT NULL COMMENT '行数',
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=105050 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_input_detail_copy
-- ----------------------------
DROP TABLE IF EXISTS `jintie_input_detail_copy`;
CREATE TABLE `jintie_input_detail_copy` (
  `pid` bigint(11) NOT NULL AUTO_INCREMENT,
  `MESSAGEHEAD` varchar(100) DEFAULT NULL,
  `MESSAGETYPE` varchar(100) DEFAULT NULL,
  `MESSAGEDATE` varchar(100) DEFAULT NULL,
  `MESSAGETIME` varchar(100) DEFAULT NULL,
  `UPDATEFLAG` varchar(100) DEFAULT NULL,
  `DOCLINENO` varchar(100) DEFAULT NULL,
  `CUSTOMERLINENO` varchar(100) DEFAULT NULL,
  `STOREID` varchar(100) DEFAULT NULL,
  `SKU` varchar(100) DEFAULT NULL,
  `BRAND` varchar(256) DEFAULT NULL,
  `MODEL` varchar(256) DEFAULT NULL,
  `HSCODE` varchar(100) DEFAULT NULL,
  `CHINESENAME` varchar(256) DEFAULT NULL,
  `SPECIFICATIONS` varchar(256) DEFAULT NULL,
  `DECLARATIONELEMENT` varchar(512) DEFAULT NULL,
  `PONO` varchar(100) DEFAULT NULL,
  `EXPECTEDQTY` varchar(100) DEFAULT NULL,
  `LOTTABLE01` varchar(100) DEFAULT NULL,
  `LOTTABLE02` varchar(100) DEFAULT NULL,
  `LOTTABLE03` varchar(100) DEFAULT NULL,
  `LOTTABLE04` varchar(100) DEFAULT NULL,
  `LOTTABLE05` varchar(100) DEFAULT NULL,
  `LOTTABLE06` varchar(100) DEFAULT NULL,
  `LOTTABLE07` varchar(100) DEFAULT NULL,
  `LOTTABLE08` varchar(100) DEFAULT NULL,
  `LOTTABLE09` varchar(100) DEFAULT NULL,
  `LOTTABLE10` varchar(100) DEFAULT NULL,
  `LOTTABLE11` varchar(100) DEFAULT NULL,
  `LOTTABLE12` varchar(100) DEFAULT NULL,
  `DAMAGEDIND` varchar(100) DEFAULT NULL,
  `DAMAGEDCD` varchar(100) DEFAULT NULL,
  `RCVDWITHEXCPTN` decimal(18,5) DEFAULT NULL,
  `REASON` varchar(512) DEFAULT NULL,
  `D_USERDEFINE1` decimal(18,5) DEFAULT NULL,
  `D_USERDEFINE2` decimal(18,5) DEFAULT NULL,
  `RECEIVEDTIME` varchar(100) DEFAULT NULL,
  `ID` varchar(100) DEFAULT NULL,
  `TOID` varchar(100) DEFAULT NULL,
  `CARTONQTY` varchar(100) DEFAULT NULL,
  `DECLARATIONUNIT` varchar(100) DEFAULT NULL,
  `COUNTRYOFORIGIN` varchar(100) DEFAULT NULL,
  `CURRENCY` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT1` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT2` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT2QTY` int(10) DEFAULT NULL,
  `CUBIC` decimal(18,3) DEFAULT NULL,
  `TOTALCUBIC` decimal(18,3) DEFAULT NULL,
  `GROSSWEIGHT` decimal(18,5) DEFAULT NULL,
  `TOTALGROSSWEIGHT` decimal(18,5) DEFAULT NULL,
  `NETWEIGHT` decimal(18,5) DEFAULT NULL,
  `TOTALNETWEIGHT` decimal(18,5) DEFAULT NULL,
  `PRICE` decimal(18,5) DEFAULT NULL,
  `TOTALPRICE` decimal(18,3) DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  `taketime` datetime DEFAULT NULL COMMENT '取走日期',
  `wl_ywno` varchar(100) DEFAULT NULL,
  `DNCODE` varchar(100) DEFAULT NULL,
  `COPYLINENO` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100246 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_input_head
-- ----------------------------
DROP TABLE IF EXISTS `jintie_input_head`;
CREATE TABLE `jintie_input_head` (
  `pid` bigint(11) NOT NULL AUTO_INCREMENT,
  `MESSAGEHEAD` varchar(100) DEFAULT NULL,
  `MESSAGETYPE` varchar(100) DEFAULT NULL,
  `MESSAGEDATE` varchar(100) DEFAULT NULL,
  `MESSAGETIME` varchar(100) DEFAULT NULL,
  `UPDATEFLAG` varchar(100) DEFAULT NULL,
  `WAREHOUSEID` varchar(100) DEFAULT NULL,
  `ASNCREATIONTIME` varchar(100) DEFAULT NULL,
  `ASNGROUP` varchar(100) DEFAULT NULL,
  `STOREID` varchar(100) DEFAULT NULL,
  `DOCTYPE` varchar(100) DEFAULT NULL,
  `ASNREFERENCE1` varchar(100) DEFAULT NULL,
  `ASNREFERENCE2` varchar(100) DEFAULT NULL,
  `ASNREFERENCE3` varchar(100) DEFAULT NULL,
  `ASNREFERENCE4` varchar(100) DEFAULT NULL,
  `ASNREFERENCE5` varchar(100) DEFAULT NULL,
  `BILLOFLADING` varchar(100) DEFAULT NULL,
  `EXPECTARRIVETIME` datetime DEFAULT NULL,
  `ACTUALARRIVETIME` varchar(100) DEFAULT NULL,
  `EFFECTIVEDATE` varchar(100) DEFAULT NULL,
  `PONO` varchar(100) DEFAULT NULL,
  `CARRIERID` varchar(100) DEFAULT NULL,
  `CARRIERNAME` varchar(256) DEFAULT NULL,
  `CARRIERADDRESS1` varchar(512) DEFAULT NULL,
  `CARRIERADDRESS2` varchar(512) DEFAULT NULL,
  `CARRIERCITY` varchar(100) DEFAULT NULL,
  `CARRIERPROVINCE` varchar(100) DEFAULT NULL,
  `CARRIERZIP` varchar(100) DEFAULT NULL,
  `SHIPPERID` varchar(256) DEFAULT NULL,
  `SHIPPERNAME` varchar(256) DEFAULT NULL,
  `SHIPPERADDRESS1` varchar(512) DEFAULT NULL,
  `SHIPPERADDRESS2` varchar(512) DEFAULT NULL,
  `SHIPPERCITY` varchar(100) DEFAULT NULL,
  `SHIPPERPROVINCE` varchar(100) DEFAULT NULL,
  `SHIPPERZIP` varchar(100) DEFAULT NULL,
  `VEHICLENO` varchar(100) DEFAULT NULL,
  `VEHICLEDATE` varchar(100) DEFAULT NULL,
  `VEHICLEDRIVER` varchar(100) DEFAULT NULL,
  `COUNTRYOFORIGIN` varchar(100) DEFAULT NULL,
  `COUNTRYOFDESTINATION` varchar(100) DEFAULT NULL,
  `PLACEOFLOADING` varchar(100) DEFAULT NULL,
  `PLACEOFDISCHARGE` varchar(100) DEFAULT NULL,
  `PLACEOFDELIVERY` varchar(100) DEFAULT NULL,
  `DELIVERYTERMS` varchar(100) DEFAULT NULL,
  `DELIVERYTERMSDESCR` varchar(100) DEFAULT NULL,
  `USERDEFINE1` varchar(100) DEFAULT NULL,
  `USERDEFINE2` varchar(100) DEFAULT NULL,
  `TRANSITMODE` varchar(100) DEFAULT NULL,
  `VESSEL` varchar(100) DEFAULT NULL,
  `SPOTSERVICE01` varchar(100) DEFAULT NULL,
  `SPOTSERVICE02` varchar(100) DEFAULT NULL,
  `SPOTSERVICE03` varchar(100) DEFAULT NULL,
  `SPOTSERVICE04` varchar(100) DEFAULT NULL,
  `SPOTSERVICE05` varchar(100) DEFAULT NULL,
  `CONTAINERTYPE` varchar(100) DEFAULT NULL,
  `CONTAINERQTY` varchar(100) DEFAULT NULL,
  `SEALNO1` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `taketime` datetime DEFAULT NULL COMMENT '取走日期',
  `wl_ywno` varchar(100) DEFAULT NULL,
  `TOTALNW` varchar(255) DEFAULT NULL COMMENT '总净重',
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2151 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_logs
-- ----------------------------
DROP TABLE IF EXISTS `jintie_logs`;
CREATE TABLE `jintie_logs` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) DEFAULT NULL COMMENT '操作人',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `start_time` datetime DEFAULT NULL COMMENT '操作时间',
  `spend_time` datetime DEFAULT NULL COMMENT '耗时',
  `url` varchar(255) DEFAULT NULL COMMENT '路径',
  `method` varchar(255) DEFAULT NULL COMMENT 'post/get',
  `parameter` mediumtext COMMENT '参数',
  `ip` varchar(255) DEFAULT NULL,
  `result` varchar(255) DEFAULT NULL COMMENT '返回信息',
  `remarks1` varchar(255) DEFAULT NULL,
  `remarks2` varchar(255) DEFAULT NULL,
  `remarks3` varchar(255) DEFAULT NULL,
  `remarks4` varchar(255) DEFAULT NULL,
  `remarks5` varchar(255) DEFAULT NULL,
  `remarks6` varchar(255) DEFAULT NULL,
  `remarks7` varchar(255) DEFAULT NULL,
  `remarks8` varchar(255) DEFAULT NULL,
  `remarks9` varchar(255) DEFAULT NULL,
  `remarks10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4205 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_model
-- ----------------------------
DROP TABLE IF EXISTS `jintie_model`;
CREATE TABLE `jintie_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` text,
  `remark1` varchar(255) DEFAULT NULL,
  `remark2` varchar(255) DEFAULT NULL,
  `remark3` varchar(255) DEFAULT NULL,
  `remark4` varchar(255) DEFAULT NULL,
  `remark5` varchar(255) DEFAULT NULL,
  `remark6` varchar(255) DEFAULT NULL,
  `remark7` varchar(255) DEFAULT NULL,
  `remark8` varchar(255) DEFAULT NULL,
  `remark9` varchar(255) DEFAULT NULL,
  `remark10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_model_json
-- ----------------------------
DROP TABLE IF EXISTS `jintie_model_json`;
CREATE TABLE `jintie_model_json` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `input_json` text,
  `output_json` text,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_model_list
-- ----------------------------
DROP TABLE IF EXISTS `jintie_model_list`;
CREATE TABLE `jintie_model_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `custom_key` varchar(255) DEFAULT NULL,
  `model_id` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` varchar(225) DEFAULT NULL,
  `remarks1` varchar(255) DEFAULT NULL,
  `remarks2` varchar(255) DEFAULT NULL,
  `remarks3` varchar(255) DEFAULT NULL,
  `remarks4` varchar(255) DEFAULT NULL,
  `remarks5` varchar(255) DEFAULT NULL,
  `remarks6` varchar(255) DEFAULT NULL,
  `remarks7` varchar(255) DEFAULT NULL,
  `remarks8` varchar(255) DEFAULT NULL,
  `remarks9` varchar(255) DEFAULT NULL,
  `remarks10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_ocr_template
-- ----------------------------
DROP TABLE IF EXISTS `jintie_ocr_template`;
CREATE TABLE `jintie_ocr_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `templateId` varchar(255) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `customkey` varchar(255) DEFAULT NULL,
  `remarks1` varchar(255) DEFAULT NULL COMMENT '类型',
  `remarks2` varchar(255) DEFAULT NULL,
  `remarks3` varchar(255) DEFAULT NULL,
  `remarks4` varchar(255) DEFAULT NULL,
  `remarks5` varchar(255) DEFAULT NULL,
  `remarks6` varchar(255) DEFAULT NULL,
  `remarks7` varchar(255) DEFAULT NULL,
  `remarks8` varchar(255) DEFAULT NULL,
  `remarks9` varchar(255) DEFAULT NULL,
  `remarks10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=338 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jintie_output_detail
-- ----------------------------
DROP TABLE IF EXISTS `jintie_output_detail`;
CREATE TABLE `jintie_output_detail` (
  `pid` bigint(11) NOT NULL AUTO_INCREMENT,
  `MESSAGEHEAD` varchar(100) DEFAULT NULL,
  `MESSAGETYPE` varchar(100) DEFAULT NULL,
  `MESSAGEDATE` varchar(100) DEFAULT NULL,
  `MESSAGETIME` varchar(100) DEFAULT NULL,
  `UPDATEFLAG` varchar(100) DEFAULT NULL,
  `DOCLINENO` varchar(100) DEFAULT NULL,
  `CUSTOMERLINENO` varchar(100) DEFAULT NULL,
  `STOREID` varchar(100) DEFAULT NULL,
  `SKU` varchar(100) DEFAULT NULL,
  `MANUFACTURERSKU` varchar(100) DEFAULT NULL,
  `BRAND` varchar(256) DEFAULT NULL,
  `MODEL` varchar(256) DEFAULT NULL,
  `HSCODE` varchar(100) DEFAULT NULL,
  `CHINESENAME` varchar(256) DEFAULT NULL,
  `SPECIFICATIONS` varchar(256) DEFAULT NULL,
  `DECLARATIONELEMENT` varchar(512) DEFAULT NULL,
  `QTYORDERED` int(10) DEFAULT NULL,
  `LOTTABLE01` varchar(100) DEFAULT NULL,
  `LOTTABLE02` varchar(100) DEFAULT NULL,
  `LOTTABLE03` varchar(100) DEFAULT NULL,
  `LOTTABLE04` varchar(100) DEFAULT NULL,
  `LOTTABLE05` varchar(100) DEFAULT NULL,
  `LOTTABLE06` varchar(100) DEFAULT NULL,
  `LOTTABLE07` varchar(100) DEFAULT NULL,
  `LOTTABLE08` varchar(100) DEFAULT NULL,
  `LOTTABLE09` varchar(100) DEFAULT NULL,
  `LOTTABLE10` varchar(100) DEFAULT NULL,
  `LOTTABLE11` varchar(100) DEFAULT NULL,
  `LOTTABLE12` varchar(100) DEFAULT NULL,
  `VOLUMN` decimal(18,5) DEFAULT NULL,
  `VOLUMNUOM` varchar(100) DEFAULT NULL,
  `CONTAINERNUM` varchar(100) DEFAULT NULL,
  `SEALNUM` varchar(100) DEFAULT NULL,
  `SPECINSTRTXT` varchar(120) DEFAULT NULL,
  `HAZARDMATERIALTXT` varchar(100) DEFAULT NULL,
  `DAMAGEIND` varchar(100) DEFAULT NULL,
  `DAMAGECD` varchar(100) DEFAULT NULL,
  `OPENQTY` int(11) DEFAULT NULL,
  `ID` varchar(100) DEFAULT NULL,
  `DECLARATIONUNIT` varchar(100) DEFAULT NULL,
  `COUNTRYOFORIGIN` varchar(100) DEFAULT NULL,
  `CURRENCY` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT1` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT2` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT2QTY` int(10) DEFAULT NULL,
  `CUBIC` decimal(18,5) DEFAULT NULL,
  `TOTALCUBIC` decimal(18,5) DEFAULT NULL,
  `GROSSWEIGHT` decimal(18,5) DEFAULT NULL,
  `TOTALGROSSWEIGHT` decimal(18,5) DEFAULT NULL,
  `NETWEIGHT` decimal(18,5) DEFAULT NULL,
  `TOTALNETWEIGHT` decimal(18,5) DEFAULT NULL,
  `PRICE` decimal(18,5) DEFAULT NULL,
  `TOTALPRICE` decimal(18,3) DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  `taketime` datetime DEFAULT NULL COMMENT '取走日期',
  `SOREFERENCE2` varchar(100) DEFAULT NULL,
  `wl_ywno` varchar(100) DEFAULT NULL,
  `DNCODE` varchar(100) DEFAULT NULL,
  `dimflag` varchar(255) DEFAULT NULL COMMENT '模糊标记',
  `COPYLINENO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=39507 DEFAULT CHARSET=utf8 COMMENT='表一';

-- ----------------------------
-- Table structure for jintie_output_detail_copy
-- ----------------------------
DROP TABLE IF EXISTS `jintie_output_detail_copy`;
CREATE TABLE `jintie_output_detail_copy` (
  `pid` bigint(11) NOT NULL AUTO_INCREMENT,
  `MESSAGEHEAD` varchar(100) DEFAULT NULL,
  `MESSAGETYPE` varchar(100) DEFAULT NULL,
  `MESSAGEDATE` varchar(100) DEFAULT NULL,
  `MESSAGETIME` varchar(100) DEFAULT NULL,
  `UPDATEFLAG` varchar(100) DEFAULT NULL,
  `DOCLINENO` varchar(100) DEFAULT NULL,
  `CUSTOMERLINENO` varchar(100) DEFAULT NULL,
  `STOREID` varchar(100) DEFAULT NULL,
  `SKU` varchar(100) DEFAULT NULL,
  `MANUFACTURERSKU` varchar(100) DEFAULT NULL,
  `BRAND` varchar(256) DEFAULT NULL,
  `MODEL` varchar(256) DEFAULT NULL,
  `HSCODE` varchar(100) DEFAULT NULL,
  `CHINESENAME` varchar(256) DEFAULT NULL,
  `SPECIFICATIONS` varchar(256) DEFAULT NULL,
  `DECLARATIONELEMENT` varchar(512) DEFAULT NULL,
  `QTYORDERED` int(10) DEFAULT NULL,
  `LOTTABLE01` varchar(100) DEFAULT NULL,
  `LOTTABLE02` varchar(100) DEFAULT NULL,
  `LOTTABLE03` varchar(100) DEFAULT NULL,
  `LOTTABLE04` varchar(100) DEFAULT NULL,
  `LOTTABLE05` varchar(100) DEFAULT NULL,
  `LOTTABLE06` varchar(100) DEFAULT NULL,
  `LOTTABLE07` varchar(100) DEFAULT NULL,
  `LOTTABLE08` varchar(100) DEFAULT NULL,
  `LOTTABLE09` varchar(100) DEFAULT NULL,
  `LOTTABLE10` varchar(100) DEFAULT NULL,
  `LOTTABLE11` varchar(100) DEFAULT NULL,
  `LOTTABLE12` varchar(100) DEFAULT NULL,
  `VOLUMN` decimal(18,5) DEFAULT NULL,
  `VOLUMNUOM` varchar(100) DEFAULT NULL,
  `CONTAINERNUM` varchar(100) DEFAULT NULL,
  `SEALNUM` varchar(100) DEFAULT NULL,
  `SPECINSTRTXT` varchar(120) DEFAULT NULL,
  `HAZARDMATERIALTXT` varchar(100) DEFAULT NULL,
  `DAMAGEIND` varchar(100) DEFAULT NULL,
  `DAMAGECD` varchar(100) DEFAULT NULL,
  `OPENQTY` int(11) DEFAULT NULL,
  `ID` varchar(100) DEFAULT NULL,
  `DECLARATIONUNIT` varchar(100) DEFAULT NULL,
  `COUNTRYOFORIGIN` varchar(100) DEFAULT NULL,
  `CURRENCY` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT1` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT2` varchar(100) DEFAULT NULL,
  `STATUTORYUNIT2QTY` int(10) DEFAULT NULL,
  `CUBIC` decimal(18,5) DEFAULT NULL,
  `TOTALCUBIC` decimal(18,5) DEFAULT NULL,
  `GROSSWEIGHT` decimal(18,5) DEFAULT NULL,
  `TOTALGROSSWEIGHT` decimal(18,5) DEFAULT NULL,
  `NETWEIGHT` decimal(18,5) DEFAULT NULL,
  `TOTALNETWEIGHT` decimal(18,5) DEFAULT NULL,
  `PRICE` decimal(18,5) DEFAULT NULL,
  `TOTALPRICE` decimal(18,3) DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  `taketime` datetime DEFAULT NULL COMMENT '取走日期',
  `SOREFERENCE2` varchar(100) DEFAULT NULL,
  `wl_ywno` varchar(100) DEFAULT NULL,
  `DNCODE` varchar(100) DEFAULT NULL,
  `COPYLINENO` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38404 DEFAULT CHARSET=utf8 COMMENT='表一';

-- ----------------------------
-- Table structure for jintie_output_head
-- ----------------------------
DROP TABLE IF EXISTS `jintie_output_head`;
CREATE TABLE `jintie_output_head` (
  `pid` bigint(11) NOT NULL AUTO_INCREMENT,
  `MESSAGEHEAD` varchar(100) DEFAULT NULL,
  `MESSAGETYPE` varchar(100) DEFAULT NULL,
  `MESSAGEDATE` varchar(100) DEFAULT NULL,
  `MESSAGETIME` varchar(100) DEFAULT NULL,
  `UPDATEFLAG` varchar(100) DEFAULT NULL,
  `WAREHOUSEID` varchar(100) DEFAULT NULL,
  `ORDERTIME` datetime DEFAULT NULL,
  `STOREID` varchar(100) DEFAULT NULL,
  `DOCTYPE` varchar(100) DEFAULT NULL,
  `SOREFERENCE1` varchar(100) DEFAULT NULL,
  `SOREFERENCE2` varchar(100) DEFAULT NULL,
  `SOREFERENCE3` varchar(100) DEFAULT NULL,
  `SOREFERENCE4` varchar(100) DEFAULT NULL,
  `SOREFERENCE5` varchar(100) DEFAULT NULL,
  `EXPECTEDSHIPMENTTIME` datetime DEFAULT NULL,
  `CONSIGNEEID` varchar(200) DEFAULT NULL,
  `CONSIGNEEADDRESS1` varchar(512) DEFAULT NULL,
  `CONSIGNEEADDRESS2` varchar(512) DEFAULT NULL,
  `CONSIGNEEADDRESS3` varchar(512) DEFAULT NULL,
  `CONSIGNEEADDRESS4` varchar(512) DEFAULT NULL,
  `CONSIGNEECITY` varchar(100) DEFAULT NULL,
  `CONSIGNEEPROVINCE` varchar(100) DEFAULT NULL,
  `CONSIGNEEZIP` varchar(100) DEFAULT NULL,
  `CONSIGNEECONTACT1` varchar(256) DEFAULT NULL,
  `CONSIGNEECONTACT2` varchar(256) DEFAULT NULL,
  `CONSIGNEETEL1` varchar(100) DEFAULT NULL,
  `CONSIGNEETEL2` varchar(100) DEFAULT NULL,
  `CONSIGNEEFAX1` varchar(100) DEFAULT NULL,
  `CONSIGNEEFAX2` varchar(100) DEFAULT NULL,
  `BUYERPO` varchar(100) DEFAULT NULL,
  `COUNTRYOFORIGIN` varchar(100) DEFAULT NULL,
  `COUNTRYOFDESTINATION` varchar(100) DEFAULT NULL,
  `TRANSPORTATION` varchar(100) DEFAULT NULL,
  `VEHICLENO` varchar(100) DEFAULT NULL,
  `PLACEOFDISCHARGE` varchar(100) DEFAULT NULL,
  `PLACEOFDELIVERY` varchar(100) DEFAULT NULL,
  `SPOTSERVICE01` varchar(100) DEFAULT NULL,
  `SPOTSERVICE02` varchar(100) DEFAULT NULL,
  `SPOTSERVICE03` varchar(100) DEFAULT NULL,
  `SPOTSERVICE04` varchar(100) DEFAULT NULL,
  `SPOTSERVICE05` varchar(100) DEFAULT NULL,
  `CONTAINERTYPE` varchar(100) DEFAULT NULL,
  `CONTAINERQTY` varchar(100) DEFAULT NULL,
  `NOTES` varchar(1000) DEFAULT NULL,
  `BILLTOID` varchar(100) DEFAULT NULL,
  `CONSIGNEECOUNTRYCODE` varchar(100) DEFAULT NULL,
  `OPENQTY` varchar(100) DEFAULT NULL,
  `STATUS` varchar(100) DEFAULT NULL,
  `taketime` datetime DEFAULT NULL COMMENT '取走日期',
  `wl_ywno` varchar(100) DEFAULT NULL,
  `BILLOFLADING` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1094 DEFAULT CHARSET=utf8 COMMENT='表一';

-- ----------------------------
-- Table structure for jintie_output_serial
-- ----------------------------
DROP TABLE IF EXISTS `jintie_output_serial`;
CREATE TABLE `jintie_output_serial` (
  `pid` bigint(11) NOT NULL AUTO_INCREMENT,
  `MESSAGEHEAD` varchar(100) DEFAULT NULL,
  `MESSAGETYPE` varchar(100) DEFAULT NULL,
  `MESSAGEDATE` varchar(100) DEFAULT NULL,
  `MESSAGETIME` varchar(100) DEFAULT NULL,
  `UPDATEFLAG` varchar(100) DEFAULT NULL,
  `DOCNO` varchar(100) DEFAULT NULL,
  `STOREID` varchar(100) DEFAULT NULL,
  `SKU` varchar(100) DEFAULT NULL,
  `SERIALNO` varchar(100) DEFAULT NULL,
  `SCANTIME` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '数据状态(-1待审核,0已审核,1已取)',
  `taketime` datetime DEFAULT NULL COMMENT '取走日期',
  `wl_ywno` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='表一';

-- ----------------------------
-- Table structure for jintie_ywinfo
-- ----------------------------
DROP TABLE IF EXISTS `jintie_ywinfo`;
CREATE TABLE `jintie_ywinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `yw_no` varchar(50) DEFAULT NULL COMMENT '业务编号',
  `custom_no` varchar(50) DEFAULT NULL COMMENT '客户业务编号',
  `clearing_customer_name` varchar(200) DEFAULT NULL COMMENT '结算客户名称',
  `status` varchar(50) DEFAULT NULL,
  `type` int(50) DEFAULT NULL COMMENT '单证类型(0进仓1出仓)',
  `remark` varchar(500) DEFAULT NULL COMMENT '接单备注',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(50) DEFAULT NULL COMMENT '创建日期',
  `update_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_date` varchar(50) DEFAULT NULL COMMENT '修改日期',
  `bring_user` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `bring_date` varchar(50) DEFAULT NULL,
  `remark1` varchar(50) DEFAULT NULL,
  `remark2` varchar(50) DEFAULT NULL,
  `remark3` varchar(50) DEFAULT NULL,
  `remark4` varchar(50) DEFAULT NULL,
  `remark5` varchar(50) DEFAULT NULL,
  `remark6` varchar(50) DEFAULT NULL,
  `remark7` varchar(50) DEFAULT NULL,
  `remark8` varchar(50) DEFAULT NULL,
  `remark9` varchar(50) DEFAULT NULL,
  `remark10` varchar(50) DEFAULT NULL,
  `create_date_time` datetime DEFAULT NULL,
  `taketime` datetime DEFAULT NULL,
  `audit_user` varchar(50) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  `create_user_emp` varchar(50) DEFAULT NULL,
  `update_user_emp` varchar(50) DEFAULT NULL,
  `audit_user_emp` varchar(50) DEFAULT NULL,
  `audit_time` datetime DEFAULT NULL,
  `audit_first_user` varchar(255) DEFAULT NULL,
  `audit_first_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2697 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for kwe_dict
-- ----------------------------
DROP TABLE IF EXISTS `kwe_dict`;
CREATE TABLE `kwe_dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `custom_key` varchar(100) DEFAULT NULL COMMENT '客户Key',
  `sku` varchar(100) DEFAULT NULL COMMENT '料号',
  `hscode` varchar(100) DEFAULT NULL COMMENT '税号',
  `chinesename` varchar(100) DEFAULT NULL COMMENT '品名',
  `unit_en` varchar(100) DEFAULT NULL COMMENT '英文单位',
  `unit_cn` varchar(100) DEFAULT NULL COMMENT '中文单位',
  `currency` varchar(100) DEFAULT NULL COMMENT '币制',
  `coo` varchar(100) DEFAULT NULL COMMENT '原产国',
  `netweight` varchar(100) DEFAULT NULL COMMENT '净重',
  `price` varchar(100) DEFAULT NULL COMMENT '单价',
  `create_user` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(100) DEFAULT NULL COMMENT '创建时间',
  `status` varchar(100) DEFAULT NULL COMMENT '状态0：删除，1：已完成，2：已确认，3：录入',
  `flag_id` int(11) DEFAULT NULL COMMENT '更新标志位',
  `remark1` varchar(100) DEFAULT NULL COMMENT 'remark1',
  `remark2` varchar(100) DEFAULT NULL COMMENT 'remark2',
  `remark3` varchar(100) DEFAULT NULL COMMENT 'remark3',
  `remark4` varchar(100) DEFAULT NULL COMMENT 'remark4',
  `remark5` varchar(100) DEFAULT NULL COMMENT 'remark5',
  `remark6` varchar(100) DEFAULT NULL COMMENT 'remark6',
  `remark7` varchar(100) DEFAULT NULL COMMENT 'remark7',
  `remark8` varchar(100) DEFAULT NULL COMMENT 'remark8',
  `remark9` varchar(100) DEFAULT NULL COMMENT 'remark9',
  `remark10` varchar(100) DEFAULT NULL COMMENT 'remark10',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=425 DEFAULT CHARSET=utf8 COMMENT='Kwe_商品库';

-- ----------------------------
-- Table structure for kwe_dict_flag
-- ----------------------------
DROP TABLE IF EXISTS `kwe_dict_flag`;
CREATE TABLE `kwe_dict_flag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `old_value` varchar(1000) DEFAULT NULL COMMENT '旧值',
  `new_value` varchar(1000) DEFAULT NULL COMMENT '新值',
  `update_user` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
  `remark1` varchar(255) DEFAULT NULL COMMENT 'remark1',
  `remark2` varchar(255) DEFAULT NULL COMMENT 'remark2',
  `remark3` varchar(255) DEFAULT NULL COMMENT 'remark3',
  `remark4` varchar(255) DEFAULT NULL COMMENT 'remark4',
  `remark5` varchar(255) DEFAULT NULL COMMENT 'remark5',
  `remark6` varchar(255) DEFAULT NULL COMMENT 'remark6',
  `remark7` varchar(255) DEFAULT NULL COMMENT 'remark7',
  `remark8` varchar(255) DEFAULT NULL COMMENT 'remark8',
  `remark9` varchar(255) DEFAULT NULL COMMENT 'remark9',
  `remark10` varchar(255) DEFAULT NULL COMMENT 'remark10',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COMMENT='商品库更新标志';

-- ----------------------------
-- Table structure for serialno
-- ----------------------------
DROP TABLE IF EXISTS `serialno`;
CREATE TABLE `serialno` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serialNO_name` varchar(50) DEFAULT NULL COMMENT '编号名称',
  `currNum` varchar(50) DEFAULT NULL,
  `currMonth` varchar(50) DEFAULT NULL,
  `currYear` varchar(50) DEFAULT NULL,
  `currDay` varchar(50) DEFAULT NULL,
  `serialNO_length` int(11) DEFAULT NULL,
  `serialNO_type` varchar(50) DEFAULT NULL,
  `serialNO_rule` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for upms_log
-- ----------------------------
DROP TABLE IF EXISTS `upms_log`;
CREATE TABLE `upms_log` (
  `log_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `description` varchar(100) DEFAULT NULL COMMENT '操作描述',
  `username` varchar(20) DEFAULT NULL COMMENT '操作用户',
  `start_time` bigint(20) DEFAULT NULL COMMENT '操作时间',
  `spend_time` int(11) DEFAULT NULL COMMENT '消耗时间',
  `base_path` varchar(500) DEFAULT NULL COMMENT '根路径',
  `uri` text COMMENT 'URI',
  `url` text COMMENT 'URL',
  `method` varchar(10) DEFAULT NULL COMMENT '请求类型',
  `parameter` mediumtext,
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户标识',
  `ip` varchar(30) DEFAULT NULL COMMENT 'IP地址',
  `result` mediumtext,
  `permissions` varchar(100) DEFAULT NULL COMMENT '权限值',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `log_id` (`log_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1634796 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` bigint(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(50) DEFAULT NULL,
  `chinesename` varchar(255) DEFAULT NULL,
  `pwd` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `remarks1` varchar(255) DEFAULT NULL,
  `remarks2` varchar(255) DEFAULT NULL,
  `remarks3` varchar(255) DEFAULT NULL,
  `remarks4` varchar(255) DEFAULT NULL,
  `remarks5` varchar(255) DEFAULT NULL,
  `remarks6` varchar(255) DEFAULT NULL,
  `remarks7` varchar(255) DEFAULT NULL,
  `remarks8` varchar(255) DEFAULT NULL,
  `remarks9` varchar(255) DEFAULT NULL,
  `remarks10` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;
