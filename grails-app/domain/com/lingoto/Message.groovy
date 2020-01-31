package com.lingoto

class Message {
	
	String code
	Locale locale
	String text
	
	static constraints = {
		code(nullable:false, unique: ["locale"], maxSize:190)
		locale(nullable:false, maxSize:190)
		text(nullable:false, maxSize:255)
	}

	static mapping = {
		version(false)
		locale sqlType: 'varchar(190)'
		cache true
	}
}