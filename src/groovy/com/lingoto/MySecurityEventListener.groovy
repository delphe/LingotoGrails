package com.lingoto

import org.springframework.context.ApplicationListener  
import org.springframework.security.authentication.event. AuthenticationSuccessEvent
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.servlet.support.RequestContextUtils
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils

class SuccessfulAuthenticationEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
	
	void onApplicationEvent(AuthenticationSuccessEvent event) { 
		def user = event.getAuthentication().getPrincipal()
		SetLoginSession.onLogin(user)
      }
}

public class SetLoginSession{
	public static onLogin(user){
		def grailsApplication = new Account().domainClass.grailsApplication
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		def currentUser = User.findByUsername(user.username)
		Locale usersISO2locale
		Locale usersISO3locale
		Locale usersISO2localeAndExt
		Locale usersISO3localeAndExt
		boolean siteTranslationNeeded = true
			
		//Setting credits & firstName to the session so it can be displayed in the header
		session.credits = currentUser?.account?.credits
		session.firstName = currentUser?.account?.firstName
		def primaryLang = currentUser?.account?.primaryLanguage

		//ISO2 is more commonly used by browsers then ISO3. Store ISO3 into the session unless ISO2 exists.
		//the LocaleExt is usually the country code.
		if(primaryLang.iso3){
			usersISO3locale = new Locale("${primaryLang.iso3}")
			session.locale = usersISO3locale
		}
		if(primaryLang.iso3 && primaryLang.localeExt){
			usersISO3localeAndExt = new Locale("${primaryLang.iso3}", "${primaryLang.localeExt}")
			session.locale = usersISO3localeAndExt
		}
		if(primaryLang.iso2){
			usersISO2locale = new Locale("${primaryLang.iso2}")
			session.locale = usersISO2locale
		}
		if(primaryLang.iso2 && primaryLang.localeExt){
			usersISO2localeAndExt = new Locale("${primaryLang.iso2}", "${primaryLang.localeExt}")
			session.locale = usersISO2localeAndExt
		}

		if(session.locale){
			//TODO: Phase 2- Instead of findByLocale, use count of messages by locale and see if it equals the count of English messages. If not, more translations are needed.
					//in translatedMessage controller, pre-fill form with words already translated and put ones not yet translated at the top of the page.
			
			//if the site has already been translated into the users language,
			//remove the "translate this site" link
			if(Message.findByLocale(usersISO2locale)){
				siteTranslationNeeded = false
			}
			if(siteTranslationNeeded && Message.findByLocale(usersISO2localeAndExt)){
				siteTranslationNeeded = false
			}
			if(siteTranslationNeeded && Message.findByLocale(usersISO3locale)){
				siteTranslationNeeded = false
			}
			if(siteTranslationNeeded && Message.findByLocale(usersISO3localeAndExt)){
				siteTranslationNeeded = false
			}
		}else{
			//if the locale has not been identified for the users selected language,
			//use the system locale.
			GrailsWebRequest webUtils = WebUtils.retrieveGrailsWebRequest()
			def request = webUtils.getCurrentRequest()
			session.locale = RequestContextUtils.getLocale(request)
		}

		//if the language is not English and the version is a SNAPSHOT (beta version), allow site translation
		if(session.locale?.language != "en" && grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")){
			siteTranslationNeeded = true
		}
		if(siteTranslationNeeded){
			//Find out how many credits the user can earn to display. Messages available to translate minus the messages already translated.
			session.creditsForTranslation = ( Message.countByLocale(new Locale("en")) - TranslatedMessage.countByUser(currentUser) ) * 5
		}
	}
}