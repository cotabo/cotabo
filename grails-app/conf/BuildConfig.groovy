grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

		//Define our Jenking instance as custom repository
		//For the forked & improved export plugin
		def customPluginRepository = new org.apache.ivy.plugins.resolver.URLResolver()		
		customPluginRepository.addArtifactPattern("https://cotabo.ci.cloudbees.com/job/export-plugin-build/lastSuccessfulBuild/artifact/grails-[module]-[revision].zip")				
		customPluginRepository.name = "export-plugin-repository"		
		resolver customPluginRepository
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    plugins{
    	build ':atmosphere:latest.integration'
		build ':quartz:latest.integration'
		build ':export:1.1.1'
	}
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        //runtime 'mysql:mysql-connector-java:5.1.5'
    }
}
