package com.lingoto

import grails.converters.JSON

class MasterLangController {

	//TODO: 28- generate admin pages after fixing scaffolding under src/templates and remove "def scaffold = MasterLang"
	def scaffold = MasterLang
	def index() {redirect(action: "list")}
	
	def getAllLanguages = {
		//TODO: 10- Bug fix - Magyar shows up twice. The list does not seem to be distinct. 
		def languages = MasterLang.executeQuery('select distinct lingo from MasterLang where lingo is not null')
		render languages as JSON
	}
	
	def getAllDialects = {
		def dialects = MasterLang.executeQuery('select distinct a.dialect from MasterLang a where a.dialect is not null and a.lingo =?',[params.lingo])
		render dialects as JSON
	}
}
