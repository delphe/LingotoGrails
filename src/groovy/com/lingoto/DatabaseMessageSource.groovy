package com.lingoto;

import net.sf.ehcache.Ehcache
import org.springframework.context.support.AbstractMessageSource
import net.sf.ehcache.Element;
import java.text.MessageFormat

class DatabaseMessageSource extends AbstractMessageSource {
    Ehcache messageCache
    def messageBundleMessageSource

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
		//First check if the language without country/variant is in the database
		def keyLangOnly = "${code}_${locale.language}_${locale.variant}"
//		println "-----------"
//		println keyLangOnly

		def format = messageCache.get(keyLangOnly)?.value
		if (!format) {
			Message message = Message.findByCodeAndLocale(code, locale.language)
			if (message) {
				format = new MessageFormat(message.text, message.locale)
			} else {
				//if the locale without country/variant is not found in the database, 
				//see if one with a country and variant exists
				def keyLangAndCountry = "${code}_${locale.language}_${locale.country}_${locale.variant}"
		        format = messageCache.get(keyLangAndCountry)?.value
		        if (!format) {
		            message = Message.findByCodeAndLocale(code, locale)
					
		            if (message) {
		                format = new MessageFormat(message.text, message.locale)
		            } else {
		                format = messageBundleMessageSource.resolveCode(code, locale)
		            }
		            messageCache.put(new Element(keyLangAndCountry, format))
		        } else {
		            format = (MessageFormat) format
		        }
			}
			messageCache.put(new Element(keyLangOnly, format))
		} else {
			format = (MessageFormat) format
		}

        return format;
    }
}
