package com.lingoto

class TranslatedMessage {
	
	User user
	MasterLang masterLang
	Message message
	String text
	
	static constraints = {
		text nullable:true, maxSize:255
		message nullable:true
		masterLang nullable:true
	}
	
	void setText(String text) {
		this.text = text?.trim()
	}

}