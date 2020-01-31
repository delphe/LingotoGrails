import com.lingoto.DatabaseMessageSource
import org.springframework.cache.ehcache.EhCacheFactoryBean
import com.lingoto.SuccessfulAuthenticationEventListener

// Place your Spring DSL code here
beans = {
    messageCache(EhCacheFactoryBean) {
        eternal = false;
        timeToIdle = 5000;
    }
    messageSource(DatabaseMessageSource) {
        messageCache = messageCache
        messageBundleMessageSource = ref("messageBundleMessageSource")
    }

    messageBundleMessageSource(org.codehaus.groovy.grails.context.support.PluginAwareResourceBundleMessageSource) {
        basenames = "grails-app/i18n/messages"
    }
	userDetailsService(com.lingoto.MyUserDetailsService)
	
	successfulAuthenticationEventListener(SuccessfulAuthenticationEventListener )
}