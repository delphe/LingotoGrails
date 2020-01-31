package com.lingoto
import org.springframework.web.servlet.support.RequestContextUtils

class UserController {
	
	static transactional = true
	transient springSecurityService
	def languageService
	def translatedMessageService
	
	def index = {
		redirect(action: "edit")
	}
	
	def delete = {
		//TODO: Phase 2- allow admin to delete a user.
		/*
		def userInstance = User.get(params.id)
		if (userInstance) {
			try {
				Collection<UserRole> userRoles = UserRole.findAllByUser(userInstance)
				userRoles*.delete()
				userInstance.delete()
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
				redirect(action: "list")
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
				redirect(action: "show", id: params.id)
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
			redirect(action: "list")
		}
		*/
	}
	
	def edit = {
		def userInstance = springSecurityService.currentUser //prevents user from viewing other accounts
		def termsOfService = translatedMessageService.termsOfService()
		//TODO: Phase 2- allow admin user to edit other accounts
		return [userInstance: userInstance, termsOfService:termsOfService]
	}
	
	def update = { UserRegistrationCommand urc ->
		
		def userInstance = springSecurityService.currentUser //prevents user from updating other accounts
		
		//TODO: Phase 2- allow admin to update other accounts
		boolean langUpdatedMsg = false
		def sysLocale = RequestContextUtils.getLocale(request)
		if (!userInstance) {
			//User is no longer logged in.
			//Spring security should automatically return user to login page and then edit page
			redirect(action: "edit")
			return
		}
		
		//TODO: 51- move to a service
		//removing duplicate error messages
		def savedErrors = urc.errors?.allErrors.findAll{ true }
		savedErrors = savedErrors?.unique(new ErrorComparator())
		urc.clearErrors()
		savedErrors.each {
				urc.errors.addError(it)
		}
		
		if(!params.updatePassword){
			//User did not check "Update Password". Removing only the password error.
			def passwordError = savedErrors.findAll{ it.field == "password"}
			savedErrors.removeAll(passwordError)
			urc.clearErrors()
			savedErrors.each {
					urc.errors.addError(it)
			}
			//User is not updating password. So, use the old one.
			params.password = userInstance.password
		}
		if (urc.hasErrors()) {
			render(view: "edit", model: [urc: urc, userInstance:userInstance,
					termsOfService:translatedMessageService.termsOfService()])
			return
		}
		if (userInstance.password != springSecurityService.encodePassword(params.currentPassword)){
			//current password entered in does not match the users password
			urc.errors.rejectValue( 'currentPassword', 'springSecurity.errors.login.fail')
			render(view: "edit", model: [urc: urc, userInstance: userInstance,
					termsOfService:translatedMessageService.termsOfService()])
			return
		}
		
		def account = userInstance.account
		def lang = userInstance.account.primaryLanguage
		if (params.userVersion || params.accountVersion || params.languageVersion) {
			def userVersion = params.userVersion?.toLong()
			def accountVersion = params.accountVersion?.toLong()
			def languageVersion = params.languageVersion?.toLong()
			if (userInstance.version > userVersion || account.version > accountVersion
				|| lang.version > languageVersion) {
				//user, account or language has just been updated by someone else or from another browser.
				urc.errors.rejectValue('enabled','error.unknown')
				render(view: "edit", model: [urc:urc, userInstance: userInstance,
						termsOfService:translatedMessageService.termsOfService()])
				return
			}
		}
		
		//refreshing session info captured at login if first name or language is changed.
		boolean refreshSessionLogin = false
		if (userInstance.account.firstName != params.firstName){
			refreshSessionLogin = true
		}
		
		params.credits = account.credits //prevents credits from being changed
		userInstance.properties = params
		account.properties = params
		
		if(lang.lingo != params.userLang || (lang.dialect ?: "") != (params.dialect ?: "") ){
			//if new language parameters have been entered, update the language.
			refreshSessionLogin = true
			def updateLang = languageService.updateLang(params.userLang, params.dialect, lang)
			if (!updateLang.saved){
				//error occurred saving language.
				render(view: "edit", model: [urc:urc, userInstance: userInstance,
						termsOfService:translatedMessageService.termsOfService()])
				return
			}
			userInstance.account.primaryLanguage = updateLang.langObj
			
			def lessonCount = Lesson.withCriteria {
				projections {
					eq("user", userInstance)
					rowCount()
				}
			}
			if(lessonCount[0] > 0){
				//display language updated message only if user has already created lessons.
				langUpdatedMsg = true
			}
			
		}
		
		if (!userInstance.hasErrors() && userInstance.save(flush: true) &&
			!account.hasErrors() && account.save(flush: true) ) {
				if(refreshSessionLogin){
					//refreshing session info captured at login if first name or language is changed.
					SetLoginSession.onLogin(userInstance)
				}
				languageService.deleteUnusedLang(lang)
				render(view: "edit", model: [saved: true, langUpdatedMsg:langUpdatedMsg, userInstance: userInstance])
				return
		}else{
			//render errors not caught in the UserRegistrationCommand constraints in current view.
			//such as unique constraint on email/username.
			render(view: "edit", model: [urc:urc, userInstance:userInstance,
					termsOfService:translatedMessageService.termsOfService()])
			return
		}


	}
	
	def updateLang = {
		def userInstance = springSecurityService.currentUser //prevents user from updating other accounts
		if (params.lessonsToChange == "allLangs"){
			//User has selected to change all previous languages to new language.
			Lesson.executeUpdate("update Lesson l set l.masterLang=:newLang " +
				"where l.user=:user", [newLang: userInstance.account.primaryLanguage, user:userInstance])
		}
		
//		if (params.lessonsToChange == "futureLangs"){
//			println "future languages will be changed... nothing to do"
//		}
		render(view: "edit", model: [saved: true, userInstance: userInstance])
		return
	}
	
	def register = { 
		//TODO: 19- add a password strength indicator
		def termsOfService = translatedMessageService.termsOfService()
		return [ userInstance : params.userInstance, termsOfService:termsOfService ]
	}
	
	def saveAccount = { UserRegistrationCommand urc ->
		//TODO: 29- use https to protect password info
		//TODO: 40- prevent passwordRepeat from showing in the logs if an error is logged.
		
//		if (!params.register) return
		if (urc.hasErrors()) {
			//removing duplicate error messages
			def savedErrors = urc.errors.allErrors.findAll{ true }
			savedErrors = savedErrors?.unique(new ErrorComparator())
			urc.clearErrors()
			savedErrors.each {
					urc.errors.addError(it)
			}
			render(view: "register", model: [userInstance: urc, 
					termsOfService:translatedMessageService.termsOfService()])
			return
		}else{
			def user = new User(urc.properties)
			def role = Role.findByAuthority("ROLE_USER") ?:
				new Role(authority:"ROLE_USER").save()
			user.account = new Account(urc.properties)
			def sysLocale = RequestContextUtils.getLocale(request)
			user.account.systemLocale = sysLocale
			User.withTransaction{ status ->
				//if user is creating an account with a new language, save the new language to the list.
				//Ilike ignores case
				//def existingLang = MasterLang.findByLanguageIlikeAndDialectIlike(urc.language, urc.dialect)
				//Searching for language with exact case to prevent forcing the use of a case that does not suit the language
				//Converting to title case may cause problems for some languages.
//				if (params.dialect?.trim() == ""){
//					params.dialect = null //Used for mySQL. Blanks were interpreted as null
//				}
				if (params.dialect?.trim() == ""){
					params.dialect = null //check postgresql for null dialect when param left blank
				}
				def existingLang = MasterLang.findByLingoAndDialect(params.userLang, params.dialect)
				if(existingLang){
					//link user to existing language
					user.account.primaryLanguage = existingLang
				}else{
					user.account.primaryLanguage = new MasterLang(
						lingo:params.userLang, dialect:params.dialect).save()
				}
				if (user.save()) {
					UserRole.create user, role
					springSecurityService.reauthenticate urc.username //automatically login new user
					SetLoginSession.onLogin(user)
					
					render(view: "register", model: [saved: true, userInstance: urc, 
							termsOfService:translatedMessageService.termsOfService()])
					return
				} else {
					//All other constraints are captured by the UserRegistrationCommand
					urc.errors.rejectValue('username','default.not.unique.message')
					
					//roll back any saved languages or account info.
					status.setRollbackOnly()
					render(view: "register", model: [userInstance: urc,
							termsOfService:translatedMessageService.termsOfService()])
					return
				}
			}
		}
	}
	
}

class UserRegistrationCommand {
	//TODO: add usernameRepeat and validation rules
	String updatePassword
	String currentPassword
	String username
	String firstName
	String lastName
	String password
	String passwordRepeat
	String userLang //if parameter was lang or language it would cause the system locale to change
	String dialect
	Integer age
	Integer credits = 20
	boolean adultContent
	boolean enabled
	boolean langEnabled
	
	static constraints = {
		username(maxSize:100, blank:false, email:true) //unique constraint cannot be tested here. It gets validated at the domain level.
		userLang(blank:false, maxSize:100)
		firstName(blank:false, maxSize:100)
		lastName(blank:false, maxSize:100)
		password(minSize:6, maxSize:20, blank: false,
			validator: { passwd, urc ->
				return passwd != urc.username
			})
		passwordRepeat(nullable: false,
			validator: { passwd2, urc ->
				return passwd2 == urc.password
			})
	}
	
}

