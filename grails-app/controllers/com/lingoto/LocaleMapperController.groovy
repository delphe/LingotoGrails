package com.lingoto

class LocaleMapperController {

	def scaffold = LocaleMapper
    def index() {redirect(action: "list")}
	//https://en.wikipedia.org/wiki/List_of_territorial_entities_where_English_is_an_official_language
	//https://en.wikipedia.org/wiki/List_of_countries_where_Spanish_is_an_official_language
	
	//TODO: Phase 2 - enhancement _menu/_language.gsp - when flag is clicked, display that flag instead of first flag in the list
}
