dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            url = "jdbc:hsqldb:mem:devDB"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
			//Use the below for CloudFoundry deployment
//			dialect = org.hibernate.dialect.MySQLInnoDBDialect
//			driverClassName = 'com.mysql.jdbc.Driver'
//			username = 'n/a'
//			password = 'n/a'
//			url = 'jdbc:mysql://localhost/db?useUnicode=true&characterEncoding=utf8'			
//            dbCreate = "update"       
            
			//For usual deployments -  
			jndiName = "java:comp/env/jdbc/cotabo"
        }
    }
}
