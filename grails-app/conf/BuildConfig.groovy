grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
//        mavenRepo "http://repository.codehaus.org"
//        mavenRepo "http://download.java.net/maven/2/"
//        mavenRepo "http://repository.jboss.com/maven2/"
		mavenRepo "http://repo.grails.org/grails/plugins/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

         //runtime 'mysql:mysql-connector-java:5.1.5'
		runtime "postgresql:postgresql:9.1-901.jdbc4"
		//compile "org.grails:grails-webflow:$grailsVersion"
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
//        runtime ":jquery:1.8.3"
        runtime ":resources:1.2"
		compile ":jquery-ui:1.10.3"
		
		if (Environment.current == Environment.DEVELOPMENT) {
			compile "org.grails.plugins:console:1.5.7"
		}

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.5"
		//TODO: 45- use resource to minimize js & look at other resource capabilities

        build ":tomcat:$grailsVersion"

        runtime ":database-migration:1.3.2"

        compile ':cache:1.0.1'
		compile ":spring-security-core:1.2.7.3"
		compile ":burning-image:0.5.1"
		compile ":modalbox:0.4"
		compile ":remote-pagination:0.4.8"
		compile ":sound-manager:0.4" //http://grails.org/plugin/sound-manager
		compile ':kickstart-with-bootstrap:0.9.9'
		compile ':lesscss-resources:1.3.0.3'
		//runtime ':twitter-bootstrap:3.1.1.3'
//		compile ':webflow:2.0.0', {
//			exclude 'grails-webflow'
//		  }
    }
}
