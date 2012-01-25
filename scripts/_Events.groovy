import org.apache.catalina.connector.Connector

/**
 * Removing trouble making slf4j jar file during build.
 * See http://jira.grails.org/browse/GRAILS-6800.
 * We can remove this while upgrading too Grails 1.4M1+
 */
eventCreateWarStart = { warName, stagingDir ->
    //Ant.delete(file:"${stagingDir}/WEB-INF/lib/slf4j-log4j12-1.5.8.jar", verbose:true)    
}

/**
 * Configure the embedded tomcat for use of Http11NioProtocol as
 * Cotabo is intended to use the same in Production.
 * 
 * See - http://jira.grails.org/browse/GRAILS-7018?page=com.xiplink.jira.git.jira-git-plugin:git-commits-tabpanel#issue-tabs
 */
eventConfigureTomcat = { tomcat ->
	def connector = new Connector("org.apache.coyote.http11.Http11NioProtocol")
	connector.port = 8080
	connector.setProperty("protocol", "AJP/1.3")
	connector.setProperty("enableLookups", "false")
	tomcat.service.removeConnector(tomcat.connector)
	tomcat.service.addConnector(connector)
	tomcat.connector = connector	
}