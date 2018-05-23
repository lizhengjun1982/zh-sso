-- 特别注意：mysql的数据库字符集一定要设置为utf8mb4，否则微信登录后拉取的昵称如果包含表情符，插入到数据库中时就会出错。
--
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Database: smart-sso
-- ------------------------------------------------------
-- Server version	5.7.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `app`
--

DROP TABLE IF EXISTS `app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL COMMENT '名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_enable` int(1) NOT NULL COMMENT '是否启用',
  `code` varchar(16) NOT NULL COMMENT '编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COMMENT='应用表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app`
--

LOCK TABLES `app` WRITE;
/*!40000 ALTER TABLE `app` DISABLE KEYS */;
INSERT INTO `app` VALUES (1,'单点登录权限管理系统',20,'2015-06-02 11:31:44',1,'smart-sso-server'),(81,'Demo管理系统',15,'2015-11-08 17:16:39',1,'smart-sso-demo');
/*!40000 ALTER TABLE `app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '父ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `url` varchar(255) NOT NULL COMMENT '权限URL',
  `sort` int(11) NOT NULL COMMENT '排序',
  `is_menu` int(1) NOT NULL COMMENT '是否菜单',
  `is_enable` int(1) NOT NULL COMMENT '是否启用',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`id`),
  KEY `FK_SYS_PERM_REFERENCE_SYS_APP` (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8 COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (2,1,NULL,'应用','/admin/app',39,1,1,'fa-plus-circle blue'),(3,1,NULL,'用户','/admin/user',79,1,1,'fa-user'),(4,1,NULL,'角色','/admin/role',59,1,1,'fa-users'),(5,1,NULL,'权限','/admin/permission',29,1,1,'fa-key'),(6,1,2,'应用新增','/admin/app/edit',4,0,1,'fa-plus-circle blue'),(7,1,2,'应用禁用','/admin/app/enable',3,0,1,'fa-lock orange'),(8,1,2,'应用启用','/admin/app/enable',2,0,1,'fa-unlock green'),(9,1,2,'应用删除','/admin/app/delete',1,0,1,'fa-trash-o red'),(10,1,3,'用户新增','/admin/user/edit',6,0,1,'fa-plus-circle blue'),(11,1,3,'用户禁用','/admin/user/enable',5,0,1,'fa-lock orange'),(12,1,3,'用户启用','/admin/user/enable',4,0,1,'fa-unlock green'),(13,1,3,'用户删除','/admin/user/delete',3,0,1,'fa-trash-o red'),(14,1,3,'重置密码','/admin/user/resetPassword',2,0,1,'fa-key grey'),(16,1,4,'角色新增','/admin/role/edit',5,0,1,'fa-plus-circle blue'),(17,1,4,'角色禁用','/admin/role/enable',4,0,1,'fa-lock orange'),(18,1,4,'角色启用','/admin/role/enable',3,0,1,'fa-unlock green'),(19,1,4,'角色删除','/admin/role/delete',2,0,1,'fa-trash-o red'),(20,1,4,'角色授权','/admin/role/allocate',1,0,1,'fa-cog grey'),(22,1,2,'应用列表','/admin/app/list',5,0,1,''),(23,1,3,'用户列表','/admin/user/list',7,0,1,''),(24,1,4,'角色列表','/admin/role/list',6,0,1,''),(25,1,5,'权限树列表','/admin/permission/nodes',1,0,1,''),(26,1,2,'应用保存','/admin/app/save',1,0,1,''),(27,1,3,'用户保存','/admin/user/save',1,0,1,''),(28,1,4,'角色保存','/admin/role/save',1,0,1,''),(29,1,5,'权限保存','/admin/permission/save',1,0,1,''),(30,1,5,'权限删除','/admin/permission/delete',1,0,1,''),(33,81,NULL,'菜单1','/admin/men1',100,1,1,'fa-user'),(35,81,33,'菜单1新增','/admin/menu1/edit',1,0,1,''),(36,81,33,'菜单1删除','/admin/menu1/delete',1,0,1,''),(39,1,NULL,'导航栏','/admin/admin/menu',99,0,1,''),(41,1,NULL,'个人中心','/admin/profile',89,1,1,'fa fa-desktop'),(42,1,41,'修改密码','/admin/profile/savePassword',1,0,1,''),(59,81,NULL,'菜单2','/admin/menu2',90,1,1,'');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `is_enable` int(1) NOT NULL COMMENT '是否启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'系统管理员',999,'系统管理员',1);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `permission_id` int(11) NOT NULL COMMENT '权限ID',
  `app_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_SYS_RE_R_REFERENCE_SYS_PERM` (`permission_id`),
  KEY `FK_SYS_RE_R_REFERENCE_SYS_ROLE` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=349 DEFAULT CHARSET=utf8 COMMENT='角色权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (291,1,39,1),(292,1,41,1),(293,1,42,1),(294,1,3,1),(295,1,23,1),(296,1,10,1),(297,1,11,1),(298,1,12,1),(299,1,13,1),(300,1,14,1),(302,1,27,1),(303,1,2,1),(304,1,22,1),(305,1,6,1),(306,1,7,1),(307,1,8,1),(308,1,9,1),(309,1,26,1),(310,1,4,1),(311,1,24,1),(312,1,16,1),(313,1,17,1),(314,1,18,1),(315,1,19,1),(316,1,20,1),(317,1,28,1),(318,1,5,1),(319,1,25,1),(320,1,29,1),(321,1,30,1),(345,1,33,81),(346,1,35,81),(347,1,36,81),(348,1,59,81);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(45) DEFAULT NULL COMMENT '账号',
  `password` varchar(45) DEFAULT NULL COMMENT '密码',
  `mobile` varchar(45) DEFAULT NULL COMMENT '手机号',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `real_name` varchar(45) NOT NULL DEFAULT '',
  `nickname` varchar(45) NOT NULL DEFAULT '',
  `sex` char(1) NOT NULL DEFAULT '0',
  `province` varchar(45) NOT NULL DEFAULT '',
  `city` varchar(45) NOT NULL DEFAULT '',
  `area` varchar(45) NOT NULL DEFAULT '' COMMENT '地区',
  `country` varchar(45) NOT NULL DEFAULT '',
  `head_img_url` varchar(255) NOT NULL DEFAULT '',
  `last_login_ip` varchar(45) DEFAULT '',
  `last_login_time` datetime DEFAULT NULL,
  `login_count` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `wx_open_id` varchar(45) DEFAULT NULL,
  `wx_web_open_id` varchar(45) DEFAULT NULL,
  `wx_union_id` varchar(45) DEFAULT NULL,
  `is_enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '启用',
  `province_code` varchar(45) DEFAULT NULL,
  `city_code` varchar(45) DEFAULT NULL COMMENT '市代码',
  `area_code` varchar(45) DEFAULT NULL COMMENT '区代码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_UNIQUE` (`account`),
  UNIQUE KEY `mobile_UNIQUE` (`mobile`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `wx_open_id_UNIQUE` (`wx_open_id`),
  UNIQUE KEY `wx_web_open_id_UNIQUE` (`wx_web_open_id`),
  UNIQUE KEY `wx_union_id_UNIQUE` (`wx_union_id`),
  UNIQUE KEY `province_code_UNIQUE` (`province_code`),
  UNIQUE KEY `city_code_UNIQUE` (`city_code`),
  UNIQUE KEY `area_code_UNIQUE` (`area_code`),
  KEY `IDX_UNION_ID` (`wx_union_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7112 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (7111,NULL,NULL,NULL,NULL,'','李正军','1','北京','房山','','中国','http://thirdwx.qlogo.cn/mmopen/vi_32/4EspenNOwzyUPIBpMerQYUyjUqBMuibUofgSFofj2TlAEJSo7htIL0HcgFuq0J2WHCChRou29RlwoibfYdMyHkAw/132','111.194.55.194, 103.46.128.49','2018-05-16 16:59:14',1,'2018-05-15 14:32:17','okmPjsqzkutEsf7jsdT5XGNqebUo',NULL,'','','',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID ',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  KEY `FK_SYS_RE_U_REFERENCE_SYS_USER` (`user_id`),
  KEY `FK_SYS_RE_U_REFERENCE_SYS_ROLE` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (16,2,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-23 10:05:07
