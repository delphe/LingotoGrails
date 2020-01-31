package com.lingoto

class LocaleMapper {
	
	String country
	String lingo
	String countryCode
	String langCode
	Locale locale
	Integer population //number of people who speak the language in that country
	
	static constraints = {
		country(nullable:false, maxSize:100)
		lingo(nullable:false, maxSize:100)
		countryCode(nullable:false, unique: ["langCode"], maxSize:10)
		langCode(nullable:false, maxSize:10)
		locale(nullable:true, maxSize:190)
		population(nullable:true, maxSize:10)
	}

	static mapping = {
		version(false)
		locale sqlType: 'varchar(190)'
		cache true
	}
}