/**
 * Removing trouble making slf4j jar file during build.
 * See http://jira.grails.org/browse/GRAILS-6800.
 * We can remove this while upgrading too Grails 1.4M1+
 */
eventCreateWarStart = { warName, stagingDir ->
    Ant.delete(file:"${stagingDir}/WEB-INF/lib/slf4j-log4j12-1.5.8.jar", verbose:true)    
}