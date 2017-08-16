
DROP TABLE IF EXISTS `App`;
CREATE TABLE `App` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `AppId` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'AppID',
  `Name` varchar(500) NOT NULL DEFAULT 'default' COMMENT '应用名',
  `OrgId` varchar(32) NOT NULL DEFAULT 'default' COMMENT '部门Id',
  `OrgName` varchar(64) NOT NULL DEFAULT 'default' COMMENT '部门名字',
  `OwnerName` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerName',
  `OwnerEmail` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerEmail',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
   KEY `App_AppId` (`AppId`),
  KEY `App_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `App_IX_Name` (`Name`)
) COMMENT='应用表';


DROP TABLE IF EXISTS `AppNamespace`;

CREATE TABLE `AppNamespace` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `Name` varchar(32) NOT NULL DEFAULT '' COMMENT 'namespace名字，注意，需要全局唯一',
  `AppId` varchar(32) NOT NULL DEFAULT '' COMMENT 'app id',
  `Format` varchar(32) NOT NULL DEFAULT 'properties' COMMENT 'namespace的format类型',
  `IsPublic` int(1) NOT NULL DEFAULT 0 COMMENT 'namespace是否为公共',
  `Comment` varchar(64) NOT NULL DEFAULT '' COMMENT '注释',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
   KEY `AppNamespace_IX_AppId` (`AppId`),
  KEY `AppNamespace_Name_AppId` (`Name`,`AppId`),
  KEY `AppNamespace_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT='应用namespace定义';


DROP TABLE IF EXISTS `Consumer`;

CREATE TABLE `Consumer` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `AppId` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'AppID',
  `Name` varchar(500) NOT NULL DEFAULT 'default' COMMENT '应用名',
  `OrgId` varchar(32) NOT NULL DEFAULT 'default' COMMENT '部门Id',
  `OrgName` varchar(64) NOT NULL DEFAULT 'default' COMMENT '部门名字',
  `OwnerName` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerName',
  `OwnerEmail` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'ownerEmail',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
    KEY `Consumer_AppId` (`AppId`),
  KEY `Consumer_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT='开放API消费者';



DROP TABLE IF EXISTS `ConsumerAudit`;

CREATE TABLE `ConsumerAudit` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `ConsumerId` int(11) unsigned DEFAULT NULL COMMENT 'Consumer Id',
  `Uri` varchar(1024) NOT NULL DEFAULT '' COMMENT '访问的Uri',
  `Method` varchar(16) NOT NULL DEFAULT '' COMMENT '访问的Method',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
   KEY `ConsumerAudit_IX_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `ConsumerAudit_IX_ConsumerId` (`ConsumerId`)
) COMMENT='consumer审计表';



DROP TABLE IF EXISTS `ConsumerRole`;

CREATE TABLE `ConsumerRole` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `ConsumerId` int(11) unsigned DEFAULT NULL COMMENT 'Consumer Id',
  `RoleId` int(10) unsigned DEFAULT NULL COMMENT 'Role Id',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
   KEY `ConsumerRole_IX_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `ConsumerRole_IX_RoleId` (`RoleId`),
  KEY `ConsumerRole_IX_ConsumerId_RoleId` (`ConsumerId`,`RoleId`)
)  COMMENT='consumer和role的绑定表';


DROP TABLE IF EXISTS `ConsumerToken`;

CREATE TABLE `ConsumerToken` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `ConsumerId` int(11) unsigned DEFAULT NULL COMMENT 'ConsumerId',
  `Token` varchar(128) NOT NULL DEFAULT '' COMMENT 'token',
  `Expires` datetime NOT NULL DEFAULT '2099-01-01 00:00:00' COMMENT 'token失效时间',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_Token` (`Token`),
  KEY `ConsumerToken_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT='consumer token表';



DROP TABLE IF EXISTS `Favorite`;

CREATE TABLE `Favorite` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UserId` varchar(32) NOT NULL DEFAULT 'default' COMMENT '收藏的用户',
  `AppId` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'AppID',
  `Position` int(32) NOT NULL DEFAULT '10000' COMMENT '收藏顺序',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `Favorite_AppId` (`AppId`),
  KEY `Favorite_IX_UserId` (`UserId`),
  KEY `Favorite_DataChange_LastTime` (`DataChange_LastTime`)
)COMMENT='应用收藏表';


DROP TABLE IF EXISTS `Permission`;

CREATE TABLE `Permission` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `PermissionType` varchar(32) NOT NULL DEFAULT '' COMMENT '权限类型',
  `TargetId` varchar(256) NOT NULL DEFAULT '' COMMENT '权限对象类型',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
   KEY `Permission_IX_TargetId_PermissionType` (`TargetId`,`PermissionType`),
  KEY `Permission_IX_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT='permission表';


DROP TABLE IF EXISTS `Role`;

CREATE TABLE `Role` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `RoleName` varchar(256) NOT NULL DEFAULT '' COMMENT 'Role name',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `Role_IX_RoleName` (`RoleName`),
  KEY `Role_IX_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT='角色表';



DROP TABLE IF EXISTS `RolePermission`;

CREATE TABLE `RolePermission` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `RoleId` int(10) unsigned DEFAULT NULL COMMENT 'Role Id',
  `PermissionId` int(10) unsigned DEFAULT NULL COMMENT 'Permission Id',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
   KEY `RolePermission_IX_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `RolePermission_IX_RoleId` (`RoleId`),
  KEY `RolePermission_IX_PermissionId` (`PermissionId`)
)  COMMENT='角色和权限的绑定表';



DROP TABLE IF EXISTS `ServerConfig`;

CREATE TABLE `ServerConfig` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `Key` varchar(64) NOT NULL DEFAULT 'default' COMMENT '配置项Key',
  `Value` varchar(2048) NOT NULL DEFAULT 'default' COMMENT '配置项值',
  `Comment` varchar(1024) DEFAULT '' COMMENT '注释',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT 'default' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
   KEY `ServerConfig_IX_Key` (`Key`),
  KEY `ServerConfig_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT='配置服务自身配置';



DROP TABLE IF EXISTS `UserRole`;

CREATE TABLE `UserRole` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `UserId` varchar(128) DEFAULT '' COMMENT '用户身份标识',
  `RoleId` int(10) unsigned DEFAULT NULL COMMENT 'Role Id',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT '1: deleted, 0: normal',
  `DataChange_CreatedBy` varchar(32) DEFAULT '' COMMENT '创建人邮箱前缀',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '' COMMENT '最后修改人邮箱前缀',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '最后修改时间',
  PRIMARY KEY (`Id`),
  KEY `UserRole_IX_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `UserRole_IX_RoleId` (`RoleId`),
  KEY `UserRole_IX_UserId_RoleId` (`UserId`,`RoleId`)
)  COMMENT='用户和role的绑定表';



INSERT INTO `ServerConfig` (`Key`, `Value`, `Comment`)
VALUES
    ('apollo.portal.envs', 'dev', '可支持的环境列表'),
    ('organizations', '[{\"orgId\":\"netposa\",\"orgName\":\"解析系统应用研发部\"}]', '部门列表'),
    ('superAdmin', 'admin', 'Portal超级管理员'),
    ('api.readTimeout', '10000', 'http接口read timeout'),
    ('consumer.token.salt', 'someSalt', 'consumer token salt');


