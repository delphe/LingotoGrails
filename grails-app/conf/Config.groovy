// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}.
//Note: changing this to html caused grails generated radio buttons to not render properly
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = "/"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
	
	def logDirectory = "/webdata/lingoto"
	def logPattern = '%d{yyyy-MMM-dd HH:mm:ss,SSS} [%t] [%X{sid}] %c{2} %p %m%n'
	
	def logFile = new File(logDirectory)
	if (!logFile.exists()) {
		logDirectory = "./logs"
		new File(logDirectory).mkdirs()
	}
	
	appenders {
		//Rollover file for every 6MB
	   rollingFile name:'fileErrorAppender', maxFileSize:'6MB', file: "${logDirectory}/APP_ERROR_LOG.log", 
	   threshold: org.apache.log4j.Level.ERROR, maxBackupIndex:20,
	   layout:pattern(conversionPattern: logPattern)
	   //To Rollover at midnight each day, use   datePattern: "'.'yyyy-MM-dd"
	   appender new org.apache.log4j.DailyRollingFileAppender(name:"fileInfoAppender", 
		   datePattern: "'.'yyyy-MM-dd",  file:"${logDirectory}/APP_INFO_LOG.log",  
		   threshold: org.apache.log4j.Level.INFO, 
		   layout:pattern(conversionPattern: logPattern))
   }
	root {
		// Configure the root logger to only output to 'fileInfoAppender' appender.
		// Gives a flag so we can also turn it on to StDout as well
		if (System.getProperty("turnInfoOn")) {
			info 'fileInfoAppender', 'stdout'
			error 'fileErrorAppender', 'stdout'
		}
		else {
			info 'fileInfoAppender'
		}

	   additivity: false
	   error 'fileErrorAppender'


   }

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate',
		   'com.lingoto'
   info "com.lingoto", "grails.app.controllers", "grails.app.services", "org.apache.cxf", "grails.app.jobs"
   warn "com.lingoto", "org.springframework", "org.hibernate", "grails.plugins.springsecurity"
   debug "com.lingoto"
} 

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.lingoto.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.lingoto.UserRole'
grails.plugins.springsecurity.authority.className = 'com.lingoto.Role'
grails.plugins.springsecurity.useSecurityEventListener = true

security {

    userLookup.userDomainClassName = "User"
    authority.className = "Role"
    userLookup.usernamePropertyName = "userName"
    userLookup.passwordPropertyName = "password"

    auth.loginFormUrl = "/" //may need to change
    successHandler.defaultTargetUrl = "/" //may need to change

    requestMap.configAttributeField = false
	
	grails.plugins.springsecurity.securityConfigType = "InterceptUrlMap"
	
	//TODO: 30- make sure security is high enough
	grails.plugins.springsecurity.interceptUrlMap = [
		'/js/**':        ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/css/**':       ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/images/**':    ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/plugins/**':   ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/*':            ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/captcha/**':     ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/user/register/**':    ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/user/saveAccount/**':    ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/masterLang/getAllLanguages/**':    ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/masterLang/getAllDialects/**':    ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/masterLang/**':    ['ROLE_ADMIN'],
		'/login/**':    ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/logout/**':    ['IS_AUTHENTICATED_ANONYMOUSLY'],
		'/dbconsole/**':    ['ROLE_ADMIN'],
		'/siteinfo/**':    ['ROLE_ADMIN'],
		'/_DemoPage/**':    ['ROLE_ADMIN'],
		'/**':    ['IS_AUTHENTICATED_REMEMBERED']
	 ]

	//Authenticated_remembered page "/**" may need to change
}
grails.plugins.springsecurity.rememberMe.alwaysRemember=true
grails.plugins.springsecurity.rememberMe.tokenValiditySeconds=86400 //user remains logged in for 24hrs.
grails.plugins.springsecurity.rememberMe.persistent = true
grails.plugins.springsecurity.rememberMe.persistentToken.domainClassName = 'com.lingoto.PersistentLogin'
