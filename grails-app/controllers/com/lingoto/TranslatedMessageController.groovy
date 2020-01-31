package com.lingoto

class TranslatedMessageController {
	
	def springSecurityService
	def translatedMessageService
	
    def index = {
		def user = springSecurityService.currentUser

		session.usersLanguage = user.account.primaryLanguage.lingo
		session.usersDialect = user.account.primaryLanguage.dialect
		def messages = translatedMessageService.getMessages("%label")
		def translatedMessages = TranslatedMessage.findAllByUser(user)
		return [messages:messages, translatedMessages:translatedMessages]
	}
	
	def update = {
		def user = springSecurityService.currentUser
		def lang = user.account.primaryLanguage
		//passing the messages object from the view would require serializing and deserializing,
		//which may not be secure because the user could modify the object before submission (not likely though).
		//need to revisit this if things get slow.
		params.each{
			//if parameter key is an integer, it is the ID of the message to translate.
			//Value is what the message was translated into.
			if (it.value && it.value?.trim() !="" && it.key.isInteger()){
				def msg = Message.get(it.key.toInteger())
				def text = it.value?.trim()
				//if user already translated the message and the text is different, update the existing message.
				def existingTranslatedMsg = TranslatedMessage.findByUserAndMasterLangAndMessage(user,lang,msg)
				if (existingTranslatedMsg){
					if(existingTranslatedMsg.text != text){
						existingTranslatedMsg.text = text
						existingTranslatedMsg.save()
					}
				}else{
					def translatedMessage = new TranslatedMessage(user:user, masterLang:lang,
						message:msg, text:text).save()
					//adding 5 credits for each message translated to users account
					user.account.credits = user.account.credits +5
					user.account.save()
					session.credits = user.account.credits
				}
			}
		}
		def nextView
		def messages
		switch (params.nextView){
			case "index":
				messages = translatedMessageService.getMessages("%label")
				nextView = "translateMessages"
				break
			case "translateMessages":
				def theMessages = translatedMessageService.getMessages("%message")
				def errorMessages = translatedMessageService.getMessages("%error%")
				messages = theMessages + errorMessages
				nextView = "termsOfService"
				break
			case "termsOfService":
				messages = translatedMessageService.getMessages("%termsOfService%")
				nextView = "index"
				break
			default:
				nextView = "index"
		}
		def translatedMessages = TranslatedMessage.findAllByUser(user)
		//recalculate the number of credits the user can earn.
		session.creditsForTranslation = ( Message.countByLocale(new Locale("en")) - translatedMessages?.size() ) * 5
		render(view: "index", model: [nextView:nextView, messages:messages, translatedMessages:translatedMessages])
	}
	
	
}