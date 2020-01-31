
class SecurityFilters {
	
	def filters = {
		allExceptTwo(controller: 'login|logout', invert: true) {
			before = { 
				//Set the language of the application to the session.locale (user's locale)
				
				//TODO: 49- review this to see if there is a better way. Passing in the params.lang should set the following:
					//session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE
					//may be able to use SessionLocaleResolver.LOCALE instead of session.locale
				if( params.lang == null && session.locale) {
					params.lang = session.locale.language
					forward( controller: params.controller, action: params.action, params:params )
				}else{
					//The user may select the form the lang drop down multiple times on the same page, causing an array of lang
					boolean langIsArray = params.lang != null && params.lang.getClass().isArray()
					if(langIsArray){
						//use the language the user last selected, which shows first in the array
						params.lang = params.lang[0]
					}
					//setting session locale for the selected language
					session.locale = new Locale(params.lang ?: 'en')
				}
			}
		}
	}

}
