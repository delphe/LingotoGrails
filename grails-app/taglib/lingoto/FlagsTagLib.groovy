package lingoto

import org.hibernate.criterion.CriteriaSpecification
import com.lingoto.LocaleMapper

class FlagsTagLib {
	
	static namespace = "flags"
	
	
	def flagDropDown = {attrs ->		
		StringBuilder sb = new StringBuilder()
		
		def allCountries = fetchAllCountries()
		def country = fetchCountry(attrs.lang, allCountries)
		
		sb << """<a class="dropdown-toggle" role="button" data-toggle="dropdown" data-target="#" href="javascript:;">
					<img class="" src="${resource(dir: 'images/flags',file: country?.toLowerCase()+'.png')}" />
				</a>
				<ul class="dropdown-menu dropdown-menu-dark" role="menu">

				"""
						
		def langCountryMap = buildLangCountryMap(allCountries)
		def additionalParams = attrs.additionalParams.replaceAll('&lang=.?.?','') //removing previous lang params
		langCountryMap.each{ lingo ->
			def language = lingo.key.split("-")[0]
			def langCode = lingo.key.split("-")[1]
			sb << """<li><a class="js-language-link" title="${langCode}" data-lang-code="${langCode}" 
						href="${attrs.currentURL+'?lang='+langCode+additionalParams}">
					${language} """
			lingo.value.eachWithIndex{countryCode , i ->
				
				if(i<5){
					//Limit the results of flags to 5
					sb << """<img class="" title="${countryCode}" src="${resource(dir: 'images/flags',file: countryCode?.toLowerCase()+'.png')}"/> &nbsp;"""
				}
			}
			sb << "</a></li>"
		}
		
		sb << "</ul>"
		
		out << sb.toString()
	}
	
	/**
	 * Get all countries of languages the site has been translated into.
	 * @return list containing lingo, langCode & countryCode sorted by lingo then population.
	 */
	def fetchAllCountries(){
		def c = LocaleMapper.createCriteria()
		def allCountries = c.list() {
			resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
			projections {
				property("population","population")
				property("lingo","lingo")
				property("langCode","langCode")
				property("countryCode","countryCode")
				}
			order("lingo", "asc")
			order("population", "desc")
		}
		return allCountries
	}
	
	/**
	 * If user's language & country code is listed in the LocaleMapper, the flag of that country will display.
	 * If the country can't be found but the language is found, the country flag with the highest population will show.
	 * If nothing is found, it will default to US.
	 * 
	 * @param locale - This could be a locale or a string containing the language code
	 * @param allCountries - list of countries & languages from fetchAllCountries method
	 * @return country code as a String
	 */
	def fetchCountry(locale, allCountries){
		boolean countryFound = false
		def country
		def lang
		def usersLang
		
		if(locale.getClass() == String){
			lang = locale
		}else{
			lang = locale?.language.toString()
			country = locale?.country.toString()
		}
		
		Integer numberOfLangFound = 0
		def altCountry
		allCountries.each{
			if(lang == it.langCode && country == it.countryCode){
				countryFound = true //user's country and language is found and should be used as the top country flag
			}
			
			if(lang == it.langCode){
				numberOfLangFound ++
				usersLang = it.lingo
				
				if(numberOfLangFound == 1 && !countryFound){
					//users language was found but not the country. 
					//Use the first country found for that language, which should have the highest population.
					altCountry = it.countryCode
				}
			}
		}
		if(!countryFound){
			country = altCountry ?: "US"
		}
		
		return country
	}
	
	/**
	 * Creates a map of languages with an array of countries that language is most used in.
	 * @param allCountries - list of countries & languages from fetchAllCountries method
	 * @return map of arrays - [lingo-langCode:[countryCode, countryCode...]]
	 */
	def buildLangCountryMap(allCountries){
		def lingo
		def langCountryMap = [:]
		def countryArray = []
		
		allCountries.each{
			if (lingo != it.lingo){
				//clear array for each new language
				countryArray = []
			}
			lingo = it.lingo
			countryArray << it.countryCode
			langCountryMap.put(it.lingo+"-"+it.langCode, countryArray)
		}
		
		return langCountryMap
	}
	
}
