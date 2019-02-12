## master (quartz-2.4.x)

* #161 Upgrade to use JDK8
* #304 Upgrade HikariCP 3.2.0 to jdk8 compatible

## quartz-2.3.x

* #66 Remove unused 'svn' requirement during maven package build
* #147 Fix BINARY to BLOG type for job data for hsqldb
* #156 Fix null string used in thread name with DirectSchedulerFactory
* #159 Fix extra bad char tick on drop table qurtz_fired_triggers for postgres
* #160 Add drop table if exists check in sql script for postgres
* #301 Improve project with readme, and license changes log
* #302 Update mvnw wrapper to use Maven 3.6.0
* #226 Replace maven-forge-plugin with maven-jar-plugin
* #294 Update hikaricp-java6:2.3.13 to hikaricp-java7:2.4.13
* #281 Fix no setter for dataSource property validateOnCheckout
* #264 Fix no setter for dataSource property discardIdleConnectionsSeconds
* #293 Setup Azure CI server for Quartz project
* #212 QuartzInitializerListener: fix a typo
* #245 Sybase: Changed varchar length TRIGGER_NAME from 80 to 200
* #170 Minor fix and improvement on Javadoc
* #203 Minor fix and improvement on Javadoc
* #146 Release BLOCKED triggers in releaseAcquiredTrigger

## quartz-2.3.0

* Released on 19-Apr-2017
* Require minium JDK7
* #6 Fix cannot create tables in MySQL with InnoDB and UTF8mb4
* #9 Fix the Java 8 javadoc issue with 'doclint:none'
* #25 Add resetTriggerFromErrorState functionality
* #93 Fix the jobs recovering (on scheduler startup)
* #76 Add missing foreign key for BLOB triggers table for ms sql server
* #114 Add 'if exists' clause to drop tables command for postgres
* #126 Add support for hikari cp, upgrade c3p0 library, break static dependencies
* #214 Reuse JobBuilder.storeDurably(boolean) in JobBuilder

## Previous Releases

```
quartz-2.2.3 	18-Apr-2016
quartz-2.2.2 	12-Oct-2015
quartz-2.2.1 	25-Sep-2013
quartz-2.2.0 	29-Jun-2013
quartz-2.1.7 	05-Mar-2013
quartz-2.1.6 	22-Feb-2013
quartz-2.1.5 	27-Apr-2012
quartz-1.8.1 	11-Jun-2010
quartz-1.8.0 	23-Apr-2010
quartz-1.7.3 	24-Feb-2010
quartz-1.7.2 	10-Feb-2010 
```
