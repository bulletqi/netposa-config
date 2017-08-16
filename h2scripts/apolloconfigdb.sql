DROP TABLE IF EXISTS `App`;

CREATE TABLE `App` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `AppId` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''AppID'',
  `Name` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''应用名'',
  `OrgId` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''部门Id'',
  `OrgName` varchar(64) NOT NULL DEFAULT ''default'' COMMENT ''部门名字'',
  `OwnerName` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''ownerName'',
  `OwnerEmail` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''ownerEmail'',
   `IsDeleted` int(1) NOT NULL DEFAULT  0  COMMENT ''1: deleted, 0: normal'',
   `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
   `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
   `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
   `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''最后修改时间'',
   PRIMARY KEY (`Id`),
    KEY `App_AppId` (`AppId`),
  KEY `App_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `App_IX_Name` (`Name`)
)COMMENT=''应用表'';


DROP TABLE IF EXISTS `AppNamespace`;

CREATE TABLE `AppNamespace` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增主键'',
  `Name` varchar(32) NOT NULL DEFAULT '''' COMMENT ''namespace名字，注意，需要全局唯一'',
  `AppId` varchar(32) NOT NULL DEFAULT '''' COMMENT ''app id'',
  `Format` varchar(32) NOT NULL DEFAULT ''properties'' COMMENT ''namespace的format类型'',
  `IsPublic` int(1) NOT NULL DEFAULT 0 COMMENT ''namespace是否为公共'',
  `Comment` varchar(64) NOT NULL DEFAULT '''' COMMENT ''注释'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT '''' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP  COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
    KEY `AppNamespace_IX_AppId` (`AppId`),
  KEY `AppNamespace_Name_AppId` (`Name`,`AppId`),
  KEY `AppNamespace_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT=''应用namespace定义'';



DROP TABLE IF EXISTS `Audit`;

CREATE TABLE `Audit` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `EntityName` varchar(50) NOT NULL DEFAULT ''default'' COMMENT ''表名'',
  `EntityId` int(10) unsigned DEFAULT NULL COMMENT ''记录ID'',
  `OpName` varchar(50) NOT NULL DEFAULT ''default'' COMMENT ''操作类型'',
  `Comment` varchar(500) DEFAULT NULL COMMENT ''备注'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
    KEY `Audit_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT=''日志审计表'';


DROP TABLE IF EXISTS `Cluster`;

CREATE TABLE `Cluster` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增主键'',
  `Name` varchar(32) NOT NULL DEFAULT '''' COMMENT ''集群名字'',
  `AppId` varchar(32) NOT NULL DEFAULT '''' COMMENT ''App id'',
  `ParentClusterId` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''父cluster'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT '''' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
   KEY `Cluster_IX_AppId_Name` (`AppId`,`Name`),
  KEY `Cluster_IX_ParentClusterId` (`ParentClusterId`),
  KEY `Cluster_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT=''集群'';



DROP TABLE IF EXISTS `Commit`;

CREATE TABLE `Commit` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `ChangeSets` longtext NOT NULL COMMENT ''修改变更集'',
  `AppId` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''AppID'',
  `ClusterName` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''ClusterName'',
  `NamespaceName` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''namespaceName'',
  `Comment` varchar(500) DEFAULT NULL COMMENT ''备注'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP  COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
  KEY `Commit_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `Commit_AppId` (`AppId`),
  KEY `Commit_ClusterName` (`ClusterName`),
  KEY `Commit_NamespaceName` (`NamespaceName`)
)  COMMENT=''commit 历史表'';




DROP TABLE IF EXISTS `GrayReleaseRule`;

CREATE TABLE `GrayReleaseRule` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `AppId` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''AppID'',
  `ClusterName` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''Cluster Name'',
  `NamespaceName` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''Namespace Name'',
  `BranchName` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''branch name'',
  `Rules` varchar(16000) DEFAULT ''[]'' COMMENT ''灰度规则'',
  `ReleaseId` int(11) unsigned NOT NULL DEFAULT ''0'' COMMENT ''灰度对应的release'',
  `BranchStatus` tinyint(2) DEFAULT ''1'' COMMENT ''灰度分支状态: 0:删除分支,1:正在使用的规则 2：全量发布'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
  KEY `GrayReleaseRule_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `GrayReleaseRule_IX_Namespace` (`AppId`,`ClusterName`,`NamespaceName`)
) COMMENT=''灰度规则表'';



DROP TABLE IF EXISTS `Instance`;

CREATE TABLE `Instance` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增Id'',
  `AppId` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''AppID'',
  `ClusterName` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''ClusterName'',
  `DataCenter` varchar(64) NOT NULL DEFAULT ''default'' COMMENT ''Data Center Name'',
  `Ip` varchar(32) NOT NULL DEFAULT '''' COMMENT ''instance ip'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Instance_IX_UNIQUE_KEY` (`AppId`,`ClusterName`,`Ip`,`DataCenter`),
  KEY `Instance_IX_IP` (`Ip`),
  KEY `Instance_IX_DataChange_LastTime` (`DataChange_LastTime`)
)  COMMENT=''使用配置的应用实例'';


DROP TABLE IF EXISTS `InstanceConfig`;

CREATE TABLE `InstanceConfig` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增Id'',
  `InstanceId` int(11) unsigned DEFAULT NULL COMMENT ''Instance Id'',
  `ConfigAppId` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''Config App Id'',
  `ConfigClusterName` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''Config Cluster Name'',
  `ConfigNamespaceName` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''Config Namespace Name'',
  `ReleaseKey` varchar(64) NOT NULL DEFAULT '''' COMMENT ''发布的Key'',
  `ReleaseDeliveryTime` timestamp NULL DEFAULT NULL COMMENT ''配置获取时间'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `InstanceConfig_IX_UNIQUE_KEY` (`InstanceId`,`ConfigAppId`,`ConfigNamespaceName`),
  KEY `InstanceConfig_IX_ReleaseKey` (`ReleaseKey`),
  KEY `InstanceConfig_IX_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `InstanceConfig_IX_Valid_Namespace` (`ConfigAppId`,`ConfigClusterName`,`ConfigNamespaceName`,`DataChange_LastTime`)
) COMMENT=''应用实例的配置信息'';




DROP TABLE IF EXISTS `Item`;

CREATE TABLE `Item` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增Id'',
  `NamespaceId` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''集群NamespaceId'',
  `Key` varchar(128) NOT NULL DEFAULT ''default'' COMMENT ''配置项Key'',
  `Value` longtext NOT NULL COMMENT ''配置项值'',
  `Comment` varchar(1024) DEFAULT '''' COMMENT ''注释'',
  `LineNum` int(10) unsigned DEFAULT ''0'' COMMENT ''行号'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
  KEY `Item_IX_GroupId` (`NamespaceId`),
  KEY `Item_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT=''配置项目'';


DROP TABLE IF EXISTS `Namespace`;

CREATE TABLE `Namespace` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增主键'',
  `AppId` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''AppID'',
  `ClusterName` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''Cluster Name'',
  `NamespaceName` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''Namespace Name'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
   KEY `Namespace_AppId_ClusterName_NamespaceName` (`AppId`,`ClusterName`,`NamespaceName`),
  KEY `Namespace_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `Namespace_IX_NamespaceName` (`NamespaceName`)
)  COMMENT=''命名空间'';



DROP TABLE IF EXISTS `NamespaceLock`;

CREATE TABLE `NamespaceLock` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增id'',
  `NamespaceId` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''集群NamespaceId'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT ''default'' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP  COMMENT ''最后修改时间'',
  `IsDeleted` int(1) DEFAULT 0 COMMENT ''软删除'',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `NamespaceLock_IX_NamespaceId` (`NamespaceId`),
    KEY `NamespaceLock_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT=''namespace的编辑锁'';


DROP TABLE IF EXISTS `Release`;

CREATE TABLE `Release` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增主键'',
  `ReleaseKey` varchar(64) NOT NULL DEFAULT '''' COMMENT ''发布的Key'',
  `Name` varchar(64) NOT NULL DEFAULT ''default'' COMMENT ''发布名字'',
  `Comment` varchar(256) DEFAULT NULL COMMENT ''发布说明'',
  `AppId` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''AppID'',
  `ClusterName` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''ClusterName'',
  `NamespaceName` varchar(500) NOT NULL DEFAULT ''default'' COMMENT ''namespaceName'',
  `Configurations` longtext NOT NULL COMMENT ''发布配置'',
  `IsAbandoned` int(1) NOT NULL DEFAULT 0 COMMENT ''是否废弃'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
  KEY `Release_AppId_ClusterName_GroupName` (`AppId`,`ClusterName`,`NamespaceName`),
  KEY `Release_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `Release_IX_ReleaseKey` (`ReleaseKey`)
) COMMENT=''发布'';


DROP TABLE IF EXISTS `ReleaseHistory`;

CREATE TABLE `ReleaseHistory` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增Id'',
  `AppId` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''AppID'',
  `ClusterName` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''ClusterName'',
  `NamespaceName` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''namespaceName'',
  `BranchName` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''发布分支名'',
  `ReleaseId` int(11) unsigned NOT NULL DEFAULT ''0'' COMMENT ''关联的Release Id'',
  `PreviousReleaseId` int(11) unsigned NOT NULL DEFAULT ''0'' COMMENT ''前一次发布的ReleaseId'',
  `Operation` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''发布类型，0: 普通发布，1: 回滚，2: 灰度发布，3: 灰度规则更新，4: 灰度合并回主分支发布，5: 主分支发布灰度自动发布，6: 主分支回滚灰度自动发布，7: 放弃灰度'',
  `OperationContext` longtext NOT NULL COMMENT ''发布上下文信息'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
  KEY `ReleaseHistory_IX_Namespace` (`AppId`,`ClusterName`,`NamespaceName`,`BranchName`),
  KEY `ReleaseHistory_IX_ReleaseId` (`ReleaseId`),
  KEY `ReleaseHistory_IX_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT=''发布历史'';




DROP TABLE IF EXISTS `ReleaseMessage`;

CREATE TABLE `ReleaseMessage` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增主键'',
  `Message` varchar(1024) NOT NULL DEFAULT '''' COMMENT ''发布的消息内容'',
  `DataChange_LastTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
  KEY `ReleaseMessage_DataChange_LastTime` (`DataChange_LastTime`),
  KEY `ReleaseMessage_IX_Message` (`Message`)
) COMMENT=''发布消息'';



DROP TABLE IF EXISTS `ServerConfig`;

CREATE TABLE `ServerConfig` (
  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''自增Id'',
  `Key` varchar(64) NOT NULL DEFAULT ''default'' COMMENT ''配置项Key'',
  `Cluster` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''配置对应的集群，default为不针对特定的集群'',
  `Value` varchar(2048) NOT NULL DEFAULT ''default'' COMMENT ''配置项值'',
  `Comment` varchar(1024) DEFAULT '''' COMMENT ''注释'',
  `IsDeleted` int(1) NOT NULL DEFAULT 0 COMMENT ''1: deleted, 0: normal'',
  `DataChange_CreatedBy` varchar(32) NOT NULL DEFAULT ''default'' COMMENT ''创建人邮箱前缀'',
  `DataChange_CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `DataChange_LastModifiedBy` varchar(32) DEFAULT '''' COMMENT ''最后修改人邮箱前缀'',
  `DataChange_LastTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP  COMMENT ''最后修改时间'',
  PRIMARY KEY (`Id`),
  KEY `ServerConfig_IX_Key` (`Key`),
  KEY `ServerConfig_DataChange_LastTime` (`DataChange_LastTime`)
) COMMENT=''配置服务自身配置'';


INSERT INTO `ServerConfig` (`Key`, `Cluster`, `Value`, `Comment`)
VALUES
    (''eureka.service.url'', ''default'', ''http://localhost:20002/eureka/'', ''Eureka服务Url，多个service以英文逗号分隔''),
    (''namespace.lock.switch'', ''default'', ''false'', ''一次发布只能有一个人修改开关''),
    (''item.value.length.limit'', ''default'', ''20000'', ''item value最大长度限制''),
    (''appnamespace.private.enable'', ''default'', ''false'', ''是否开启private namespace''),
    (''item.key.length.limit'', ''default'', ''128'', ''item key 最大长度限制'');

