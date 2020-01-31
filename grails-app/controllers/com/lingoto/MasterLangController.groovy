package com.lingoto

import grails.converters.JSON

class MasterLangController {

	//TODO: 28- generate admin pages after fixing scaffolding under src/templates and remove "def scaffold = MasterLang"
	//TODO: 28- only allow admins to access admin pages
	//def scaffold = MasterLang
	
	def getAllLanguages = {
		def languages = MasterLang.executeQuery('select distinct lingo from MasterLang where lingo is not null')
		render languages as JSON
	}
	
	def getAllDialects = {
		def dialects = MasterLang.executeQuery('select distinct a.dialect from MasterLang a where a.dialect is not null and a.lingo =?',[params.lingo])
		render dialects as JSON
	}
}
