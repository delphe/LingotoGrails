package com.lingoto

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.servlet.support.RequestContextUtils
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils

class TranslatedMessageService {
	
	GrailsWebRequest webUtils = WebUtils.retrieveGrailsWebRequest()
	def request = webUtils.getCurrentRequest()
	
	/**
	 * Get words and phrases used by this site for user to translate.
	 *
	 *  If the locale without country/variant is not found in the database,
	 *  it will see if one with a country and variant exists.
	 *  If messages still are not found, use default language: English
	 *
	 * @param messageType - Pass in %label, %message, %error% or other message types found in the message code.
	 * Pass in Null messageType to get all.
	 *
	 * @return All columns of Message table where locale and message type are found.
	 */
	def getMessages(messageType){
		def locale
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		if(session.locale){
			locale = session.locale
		}else{
			//user system locale if session locale is not found
			locale = RequestContextUtils.getLocale(request)
		}
		
		def messages
		if (messageType){
			messages = Message.findAllByLocaleAndCodeLike(locale?.language, messageType)
			if (!messages){
				messages = Message.findAllByLocaleAndCodeLike(locale, messageType)
			}
			if (!messages){
				messages = Message.findAllByLocaleAndCodeLike('en', messageType)
			}
		}else{
			messages = Message.findAllByLocale(locale?.language)
			if (!messages){
				messages = Message.findAllByLocale(locale)
			}
			if (!messages){
				messages = Message.findAllByLocale('en')
			}
		}
		
		return messages
	}
	
	/**
	 * Building the Terms of Service string based on the user's language. 
	 * As long as the termsOfService sentence and paragraph numbers are ordered correctly in the Message table, 
	 * this should display the text correctly.
	 * 
	 * @return terms of service string encodeAsHTML with line breaks between each paragraph.
	 */
	def termsOfService(){
		def termsOfServiceMap = [:]
		def termsOfServiceMessages = getMessages("%termsOfService%")
		
		//turning all terms of service sentences found into a map of paragraphs with map of sentences
		termsOfServiceMessages.each{ message ->
			def sentenceMarker = message.code.findAll( /\d+/ )[0].toInteger()
			def sentenceMap = ["${message.text}":sentenceMarker]
			def paraMarker = message.code.findAll( /\d+/ )[1].toInteger()
			if(termsOfServiceMap.containsKey(paraMarker)){
				termsOfServiceMap.get(paraMarker) << sentenceMap
			}else{
				termsOfServiceMap.put(paraMarker, sentenceMap)
			}
		}
		
		//first sorting the paragraphs
		termsOfServiceMap = termsOfServiceMap.sort {it.key}

		//sorting the sentences in each paragraph
		def termsOfServiceSorted = [:]
		termsOfServiceMap.each{
			def sorted = it.value.sort{it.value}
			termsOfServiceSorted.put(it.key, sorted)
		}
		
		//turning sorted map of paragraphs and map of sentences into one large string.
		def termsOfService = new StringBuilder()
		termsOfServiceSorted.each{ para ->
			para.value.each{ sentenceMap ->
				termsOfService.append(sentenceMap.key + " ")
			}
			//adding character returns between each paragraph
			termsOfService.append('\n\n')
		}
		termsOfService.toString()
		
		return termsOfService.encodeAsHTML().replace('\n', '<br/>\n')
	}

}
