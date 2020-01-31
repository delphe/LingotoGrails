package com.lingoto


class LanguageService {
	
	/**
	 * Returns boolean 'saved' to identify if languages was successfully saved.
	 * Returns 'langObj' with updated language object if saved was true.
	 * Don't forget to run a languageService.deleteUnusedLang(originalLang) after saving the lesson or account.
	 */
	def updateLang(newLang, newDialect, originalLang){

		def existingLang
		//using null values in MySQL does not allow unique constraint to work properly
		
//					if (newDialect?.trim() == "" || !newDialect){
//						newDialect = "" //postgresql does not save this as a blank value, it saves as null
//					}
		if (newDialect?.trim() == ""){
			newDialect = null //check postgresql for null dialect when dialect is left blank
		}
		existingLang = MasterLang.findByLingoAndDialect(newLang, newDialect)
		if(existingLang){
			originalLang = existingLang
		}else{
			def newLanguage = new MasterLang(
					lingo:newLang, dialect:newDialect)
			//Language entered not found.
			if(newLanguage.validate()){
				//Create new language.
				originalLang = newLanguage.save()
			}else{
				//validation errors occurred
				return [saved:false, langObj:originalLang]
			}
			
		}
		return [saved:true, langObj:originalLang]
		
	}
	
	/**
	 * Returns boolean true if language is deleted. 
	 * Delete if language does not contain a languageRef, meaning it was most likely created by a user and not the admin,
	 * and no lessons or user accounts are associated to it.
	 */
	def deleteUnusedLang(language) {
		def accountsUsingLangCount = Account.countByPrimaryLanguage(language)
		def lessonsUsingLangCount = Lesson.executeQuery('select count(*) from Lesson where masterLang=?',[language])

		if (!language.languageRef && 
			accountsUsingLangCount <= 0 && lessonsUsingLangCount[0] <= 0){
			language.delete()
			return true
		}else{
			return false
		}
	}
}
