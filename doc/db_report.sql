# Host: localhost  (Version 5.7.16)
# Date: 2018-07-31 17:17:36
# Generator: MySQL-Front 5.4  (Build 4.153) - http://www.mysqlfront.de/

/*!40101 SET NAMES utf8 */;

#
# Structure for table "tb_bulletin"
#

CREATE TABLE `tb_bulletin` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `city_id` int(10) NOT NULL DEFAULT '0' COMMENT '大市范围可见',
  `county` int(10) NOT NULL DEFAULT '0' COMMENT '区县范围可见',
  `title` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `create_id` int(10) DEFAULT NULL COMMENT '添加人',
  `publish_time` datetime DEFAULT NULL COMMENT '发布日期',
  `publish_id` int(10) DEFAULT NULL COMMENT '发布人',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态(0:未发布,1:已发布)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='通知公告';

#
# Data for table "tb_bulletin"
#


#
# Structure for table "tb_collection"
#

CREATE TABLE `tb_collection` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(255) NOT NULL COMMENT '标识码',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `quota_id` int(11) NOT NULL DEFAULT '0' COMMENT '指标ID',
  `memo` varchar(255) DEFAULT NULL COMMENT '描述',
  `value_type` enum('file','text','number','enum') NOT NULL DEFAULT 'text' COMMENT '采集数据类型',
  `value_opt` varchar(255) DEFAULT NULL COMMENT '值选项',
  `validation` varchar(255) DEFAULT NULL COMMENT '校验规则',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `create_id` int(10) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `update_id` int(10) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据采集项';

#
# Data for table "tb_collection"
#

INSERT INTO `tb_collection` VALUES (1,'601','区域内常住人口（万人）',6,NULL,'text',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),(2,'602','学校类型：民办    公办',6,NULL,'text',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),(3,'603','学校所处区域是否是老城区(是  否)',6,NULL,'text',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),(4,'701','学校教学、运动、生活等区域划分合理，相对独立（是    否）',7,NULL,'text',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),(5,'702','图书馆、实验室、信息中心等的布局便于教学使用（是    否）',7,NULL,'text',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),(6,'703','请上传校园规划图或请上传校园实景图（JPG图，规定图片大小，图上标注生活区、教学区、运动区，只允许上传1张图片）',7,NULL,'text',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),(7,'801','学校地块总面积（单位：m2）',8,NULL,'text',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL);

#
# Structure for table "tb_dict"
#

CREATE TABLE `tb_dict` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(255) DEFAULT NULL COMMENT '标识码',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `type` varchar(255) NOT NULL COMMENT '字典类型',
  `level` tinyint(1) NOT NULL DEFAULT '1' COMMENT '层级',
  `parent_id` int(10) NOT NULL DEFAULT '0' COMMENT '上级',
  `rank` int(10) NOT NULL DEFAULT '0' COMMENT '排序',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';

#
# Data for table "tb_dict"
#

INSERT INTO `tb_dict` VALUES (1,'PRIMARY','小学','SCHOOL_TYPE',1,0,0,NULL);

#
# Structure for table "tb_eduinst"
#

CREATE TABLE `tb_eduinst` (
  `id` int(10) NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '名称',
  `code` varchar(255) NOT NULL COMMENT '标识',
  `level` int(1) NOT NULL DEFAULT '1' COMMENT '教育局类型(1:省,2:市,3:县)',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `create_id` int(10) DEFAULT NULL COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `update_id` int(10) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "tb_eduinst"
#


#
# Structure for table "tb_eduinst_collection"
#

CREATE TABLE `tb_eduinst_collection` (
  `inst_id` int(10) NOT NULL COMMENT '教育局ID',
  `collection_id` int(10) NOT NULL COMMENT '采集项ID',
  `report_id` int(10) NOT NULL COMMENT '填报年份ID',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `create_id` int(10) DEFAULT NULL COMMENT '添加人',
  PRIMARY KEY (`inst_id`,`collection_id`,`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='教育局采集项关系表';

#
# Data for table "tb_eduinst_collection"
#


#
# Structure for table "tb_eduinst_process"
#

CREATE TABLE `tb_eduinst_process` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `inst_id` int(10) NOT NULL COMMENT '教育局ID',
  `report_id` int(10) NOT NULL COMMENT '填报年份ID',
  `total_num` int(10) NOT NULL DEFAULT '0' COMMENT '总数量',
  `finish_num` int(10) NOT NULL DEFAULT '0' COMMENT '已完成数量',
  `submit_time` datetime DEFAULT NULL COMMENT '提交日期',
  `audit_time` datetime DEFAULT NULL COMMENT '审核日期',
  `audit_id` int(1) NOT NULL COMMENT '审核人ID',
  `audit_note` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '审核意见',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态(0:未完成,1:已提交审核,2:审核通过,3:审核驳回)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Data for table "tb_eduinst_process"
#


#
# Structure for table "tb_eduinst_report"
#

CREATE TABLE `tb_eduinst_report` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `inst_id` int(10) NOT NULL COMMENT '教育局ID',
  `collection_id` int(10) NOT NULL COMMENT '采集项ID',
  `report_id` int(10) NOT NULL COMMENT '填报年份ID',
  `content` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '填报内容',
  `create_time` datetime DEFAULT NULL COMMENT '保存日期',
  `create_id` int(10) DEFAULT NULL COMMENT '添加人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='教育局采集内容表';

#
# Data for table "tb_eduinst_report"
#


#
# Structure for table "tb_quota"
#

CREATE TABLE `tb_quota` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `level` tinyint(1) NOT NULL DEFAULT '1' COMMENT '层级',
  `parent_id` int(10) NOT NULL DEFAULT '0' COMMENT '父级指标',
  `content` text COMMENT '监测内容',
  `summery` varchar(255) DEFAULT NULL COMMENT '监测要点',
  `note` text COMMENT '备注',
  `formula` text COMMENT '计算公式',
  `rule` text COMMENT '合格标准',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='填报指标';

#
# Data for table "tb_quota"
#

INSERT INTO `tb_quota` VALUES (1,'学校设置',1,0,NULL,NULL,NULL,NULL,NULL,0),(2,'校园建设',1,0,NULL,NULL,NULL,NULL,NULL,0),(6,'布局规模',2,1,NULL,NULL,NULL,NULL,NULL,0),(7,'校园规划',2,2,NULL,NULL,NULL,NULL,NULL,0),(8,'生均占地',2,2,NULL,NULL,NULL,NULL,NULL,0);

#
# Structure for table "tb_report"
#

CREATE TABLE `tb_report` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '显示标题',
  `start_time` datetime NOT NULL COMMENT '起始日期',
  `expire_time` datetime NOT NULL COMMENT '结束日期',
  `memo` varchar(512) CHARACTER SET utf8 DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `create_id` int(10) DEFAULT NULL COMMENT '添加人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='填报年份';

#
# Data for table "tb_report"
#

INSERT INTO `tb_report` VALUES (1,'2018','2018-01-01 00:00:00','2018-12-01 00:00:00',NULL,NULL,NULL);

#
# Structure for table "tb_school"
#

CREATE TABLE `tb_school` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `CODE` varchar(255) NOT NULL COMMENT '标识码',
  `NAME` varchar(255) NOT NULL COMMENT '学校名称',
  `city` varchar(255) NOT NULL COMMENT '所属市',
  `county` varchar(255) NOT NULL COMMENT '所属区县',
  `region_type` varchar(255) NOT NULL COMMENT '城乡分类',
  `run_by` varchar(255) NOT NULL COMMENT '办学者类型',
  `TYPE` varchar(255) NOT NULL COMMENT '办学类型',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `create_id` int(10) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `update_id` int(10) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学校表';

#
# Data for table "tb_school"
#

INSERT INTO `tb_school` VALUES (1,'00001','测试学校','0','0','0','0','PRIMARY',NULL,NULL,NULL,NULL);

#
# Structure for table "tb_school_collection"
#

CREATE TABLE `tb_school_collection` (
  `school_type` varchar(255) NOT NULL COMMENT '学校类型',
  `collection_id` int(10) NOT NULL COMMENT '采集项',
  `report_id` int(10) NOT NULL COMMENT '上报记录',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `create_id` int(10) DEFAULT NULL COMMENT '添加人',
  PRIMARY KEY (`school_type`,`collection_id`,`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学校类型采集项关系表';

#
# Data for table "tb_school_collection"
#

INSERT INTO `tb_school_collection` VALUES ('PRIMARY',1,1,NULL,NULL),('PRIMARY',2,1,NULL,NULL),('PRIMARY',3,1,NULL,NULL),('PRIMARY',4,1,NULL,NULL),('PRIMARY',5,1,NULL,NULL),('PRIMARY',6,1,NULL,NULL),('PRIMARY',7,1,NULL,NULL);

#
# Structure for table "tb_school_process"
#

CREATE TABLE `tb_school_process` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `school_id` int(10) NOT NULL COMMENT '填报学校id',
  `report_id` int(10) NOT NULL COMMENT '填报年份id',
  `total` int(10) NOT NULL DEFAULT '0' COMMENT '所有数量',
  `finish` int(10) NOT NULL DEFAULT '0' COMMENT '已完成数量',
  `submit_time` datetime DEFAULT NULL COMMENT '提交审核日期',
  `audit_time` datetime DEFAULT NULL COMMENT '审核日期',
  `audit_id` int(10) NOT NULL COMMENT '审核人ID',
  `audit_note` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '审核意见',
  `status` int(10) NOT NULL DEFAULT '0' COMMENT '状态(0:填报中,1:未审核,2:审核通过,3:审核驳回)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='学校填报审核进度';

#
# Data for table "tb_school_process"
#


#
# Structure for table "tb_school_report"
#

CREATE TABLE `tb_school_report` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `school_id` int(10) NOT NULL COMMENT '学校ID',
  `collection_id` int(10) NOT NULL COMMENT '采集项',
  `report_id` int(10) NOT NULL COMMENT '填报记录ID',
  `content` varchar(255) DEFAULT NULL COMMENT '填报内容',
  `create_time` datetime DEFAULT NULL COMMENT '填报日期',
  `create_id` int(10) DEFAULT NULL COMMENT '填报人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学校填报原始数据';

#
# Data for table "tb_school_report"
#


#
# Structure for table "tb_school_type"
#

CREATE TABLE `tb_school_type` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '类型名称',
  `primary_rate` decimal(11,4) NOT NULL DEFAULT '0.0000' COMMENT '小学占比',
  `middle_rate` decimal(11,4) NOT NULL DEFAULT '0.0000' COMMENT '初中占比',
  `high_rate` decimal(11,4) NOT NULL DEFAULT '0.0000' COMMENT '高中占比',
  `note` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学校类型';

#
# Data for table "tb_school_type"
#


#
# Structure for table "tb_subaccount_quota"
#

CREATE TABLE `tb_subaccount_quota` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '子账号ID',
  `quota_id` int(10) NOT NULL COMMENT '二级指标',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `create_id` int(10) DEFAULT NULL COMMENT '添加人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='子账号指标关系表';

#
# Data for table "tb_subaccount_quota"
#


#
# Structure for table "tb_sys_city"
#

CREATE TABLE `tb_sys_city` (
  `id` int(10) NOT NULL COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '名称',
  `level` int(1) NOT NULL DEFAULT '1' COMMENT '层级(1:市,2:区县)',
  `parent_id` int(10) NOT NULL DEFAULT '0' COMMENT '上级地区',
  `rank` int(10) NOT NULL DEFAULT '0' COMMENT '排序',
  `note` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='地区表';

#
# Data for table "tb_sys_city"
#


#
# Structure for table "tb_sys_config"
#

CREATE TABLE `tb_sys_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '配置类型',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '配置名称',
  `value` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '配置项值',
  `note` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态(0:启用配置项,1:禁用配置项)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='系统配置表';

#
# Data for table "tb_sys_config"
#


#
# Structure for table "tb_sys_menu"
#

CREATE TABLE `tb_sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `parent_id` int(11) DEFAULT '0' COMMENT '父菜单ID',
  `url` varchar(255) DEFAULT NULL COMMENT '菜单URL',
  `rank` int(11) DEFAULT '0' COMMENT '菜单排序',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `status` tinyint(1) DEFAULT '0' COMMENT '是否可用0:可用,1禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COMMENT='系统菜单表';

#
# Data for table "tb_sys_menu"
#


#
# Structure for table "tb_sys_permission"
#

CREATE TABLE `tb_sys_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) DEFAULT NULL COMMENT '权限名称',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `pattern` varchar(255) NOT NULL COMMENT '权限映射',
  `description` varchar(255) DEFAULT NULL COMMENT '权限描述',
  `type` tinyint(1) DEFAULT '0' COMMENT '权限类型0:功能权限,1:菜单权限',
  `status` tinyint(1) DEFAULT '1' COMMENT '是否可用1:可用,0:不可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=194 DEFAULT CHARSET=utf8 COMMENT='系统权限表';

#
# Data for table "tb_sys_permission"
#


#
# Structure for table "tb_sys_role"
#

CREATE TABLE `tb_sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `is_system` tinyint(1) DEFAULT '0' COMMENT '是否系统角色0:否,1:是',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `create_uid` int(11) DEFAULT NULL COMMENT '创建人',
  `update_time` bigint(20) DEFAULT '0' COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '是否可用0:不可用,1:可用',
  `CODE` varchar(255) DEFAULT NULL COMMENT '角色code',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='系统角色表';

#
# Data for table "tb_sys_role"
#


#
# Structure for table "tb_sys_role_permission"
#

CREATE TABLE `tb_sys_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `permission_id` int(11) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4850 DEFAULT CHARSET=utf8 COMMENT='角色权限关系表';

#
# Data for table "tb_sys_role_permission"
#


#
# Structure for table "tb_user"
#

CREATE TABLE `tb_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) NOT NULL COMMENT '账号',
  `passwd` varchar(255) NOT NULL COMMENT '密码',
  `real_name` varchar(255) DEFAULT NULL COMMENT '用户姓名',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `role_type` varchar(255) NOT NULL COMMENT '角色',
  `inst_type` varchar(255) NOT NULL COMMENT '机构类型',
  `inst_id` int(10) NOT NULL COMMENT '机构ID',
  `access_token` varchar(255) DEFAULT NULL COMMENT '登陆token',
  `token_time` datetime DEFAULT NULL COMMENT '上次登陆日期',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `create_id` int(10) NOT NULL COMMENT '添加人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识',
  `STATUS` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态(0:正常,1:禁用)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

#
# Data for table "tb_user"
#

