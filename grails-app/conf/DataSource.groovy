dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
		/*MySQL script used to create database:
		
		CREATE DATABASE `lingotodb_dev` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
		GRANT ALL ON `lingotodb_dev`.* TO `admin`@localhost IDENTIFIED BY 'secret';
		FLUSH PRIVILEGES;*/
		
		// Alter tables with query if necessary:
//			ALTER TABLE table_name CONVERT TO CHARACTER SET utf8mb4
//			COLLATE utf8mb4_general_ci;
//			- See more at: http://www.oodlestechnologies.com/blogs/Configuring-Grails-App-for-UTF8-Character-Encoding#sthash.hOKAYTBC.dpuf
        dataSource {
			//Postgresql DB Settings:
//			driverClassName = "org.postgresql.Driver"
//			dialect = "org.hibernate.dialect.PostgreSQLDialect"
//			username = "postgres"
//			password = "secret"
//			url = "jdbc:postgresql://localhost:5534/lingototest"

			//Inline Memory Database
            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
			
			//MYSQL settings:
//			driverClassName = "com.mysql.jdbc.Driver"
//			username = "admin"
//			dialect= "com.lingoto.mysql.dialect.MySQLUTF8InnoDBDialect"
//			url = "jdbc:mysql://localhost:3306/lingotodb_dev?useUnicode=yes&characterEncoding=UTF-8"
			
			logSql=true
			dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    production {
        dataSource {
            dbCreate = "create-drop"
            //url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
	url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            //pooled = true
            //properties {
               //maxActive = -1
               //minEvictableIdleTimeMillis=1800000
               //timeBetweenEvictionRunsMillis=1800000
               //numTestsPerEvictionRun=3
               //testOnBorrow=true
               //testWhileIdle=true
               //testOnReturn=true
               //validationQuery="SELECT 1"
            //}
        }
    }
}
