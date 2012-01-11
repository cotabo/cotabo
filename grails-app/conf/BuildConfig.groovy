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
		//base stuff
		runtime ":hibernate:$grailsVersion"
		runtime ":jquery:1.7.1"
		runtime ":resources:1.1.5"
		
		//Cotabo specific
		runtime ":jcaptcha:1.2.1"
		runtime ":atmosphere:0.4.1.2"
		runtime ":pretty-time:0.3"
		runtime ":quartz:0.4.2"
		runtime ":spring-security-core:1.2.7"
		
		
		//build
		build ":tomcat:$grailsVersion"
		build ':export:1.1.1'
	}
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        //runtime 'mysql:mysql-connector-java:5.1.5'
    }
}
