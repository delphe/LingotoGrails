package com.lingoto

import java.text.DecimalFormat
import grails.converters.JSON
import org.springframework.web.servlet.support.RequestContextUtils
import org.springframework.security.core.context.SecurityContextHolder

class LessonController {

	def springSecurityService
	def rememberMeServices
	def lessonService
	def languageService
	def userService

	def beforeInterceptor = [action: this.&auth]

	/**
	 * The following actions may not have a currentUser and will use a guest user account:
	 * index, showLessons, showFilteredLessons, lessonPlan, learn
	 * @return
	 */
	private auth() {
		//This method only gets called after spring security authenticates the user or the action 'IS_AUTHENTICATED_ANONYMOUSLY'
		def currentUser = springSecurityService.currentUser
		if(!currentUser){
			def guestUserRole = Role.findByAuthority('ROLE_GUESTUSER')
			def userid = User.last().id + 1
			def randomNum = Math.abs(new Random().nextInt()) % 100000 + 1
			def locale = RequestContextUtils.getLocale(request)
			def language = MasterLang.findByLingoIlike("English") //TODO: 01- pull language from languageService.fetchLingoFromSystemLocale
//			def language = languageService.fetchLingoFromSystemLocale()
			def guestUser = new User(username: "${userid}@${randomNum}.com", enabled: true,
				password: "secret").save(flush: true, failOnError:true)
			def guestAccount = new Account(firstName: "Guest", user:guestUser,
				lastName: "User", primaryLanguage: language, age:17, adultContent:false, credits:5).save()
			UserRole.create guestUser, guestUserRole, true
	
			springSecurityService.reauthenticate guestUser.username //automatically login new user
			SetLoginSession.onLogin(guestUser)
			def authentication = SecurityContextHolder.context.authentication
			rememberMeServices.loginSuccess(request, response, authentication)
		}
	}
	
	
	def index = {
		//TODO: 05- fix gsp page to keep rating stars on the right in mobile view
		//TODO: Phase 2- Add fix filter buttons & when nothing is found use the following in gsp: <g:message code="nothingFound.message" default="Nothing found." />
		def lessonMap = [:]
		//def user = springSecurityService.currentUser
		def c = MasterLang.createCriteria()
		def languagesWithLessons = c {
			//only show if there are 5 or more lessons
			sizeGt("lessons", 5)
			maxResults(10000) //prevents too large of a result set
		}
		//TODO: Phase 2- add search feature
		//TODO: Phase 2- only show languages with languageRef (known languages) but allow search to pull up all languages
		languagesWithLessons.each{
			def langKey = "${it.lingo} ${it.dialect}"
			
			lessonMap.put(langKey, ["${it.lingo}":"${it.dialect}"])
			
			/*
			if(lessonMap.containsKey(langKey)){
				//if more then one user has the same language/dialect, store those users in an array
				lessonMap.get(langKey) << it
			}else{
				lessonMap.put(langKey, [it])
			}
			
			lessonMap.get(langKey).sort{a,b -> b.lessons.size() <=> a.lessons.size() ?:
				a.username <=> b.username}
			*/
		}
		[lessonMap:lessonMap.sort{it.key.toUpperCase(Locale.getDefault())}]
	}
	
	def showLessons = {
		def lessons = []
		def authors = []
		params.beginner = true
		params.informal = true
		params.formal = true
		//TODO: 37- sort by rating first then most lessons, then username
		try{
			def authorsWithLessonsByLang = lessonService.filteredAuthorsWithLessons(params)
			if(!authorsWithLessonsByLang){
				params.intermediate = true
				params.advanced = true
				authorsWithLessonsByLang = lessonService.filteredAuthorsWithLessons(params)
			}
			authors = lessonService.buildAuthorAndRatingsArray(authorsWithLessonsByLang)
		}catch(Exception e){
			log.error(e)
			def error = g.message(code: "error.unknown", default: "An unknown error has occurred. Please try again.")
			return [error:error, params:params]
		}
		
		[authors:authors,params:params]
	}
	
	def showFilteredLessons = {
		def authors = []
		try{
			def authorsWithLessonsByLang = lessonService.filteredAuthorsWithLessons(params)
			authors = lessonService.buildAuthorAndRatingsArray(authorsWithLessonsByLang)
		}catch(Exception e){
			log.error(e)
			def error = g.message(code: "error.unknown", default: "An unknown error has occurred. Please try again.")
			render(template: "lessons",
				model: [error:error,
						beginner:params.beginner,
						intermediate:params.intermediate,
						advanced:params.advanced,
						formal:params.formal,
						informal:params.informal])
			return
		}

		render(template: "lessons", 
			model: [authors:authors,
					beginner:params.beginner,
					intermediate:params.intermediate,
					advanced:params.advanced,
					formal:params.formal,
					informal:params.informal])
	}
	
	def filterLessons() {
		def currentUser = springSecurityService.currentUser
		params.lesson = Lesson.get(params.lessonId)
		lessonService.filteredLessonsByUser(params, params.lesson.user, currentUser)
		def filteredLessonByAuthorList = currentUser?.account?.filteredLessonsByAuthor?.split(",")
		
		render(template: "lessonPagination", 
			model: [lesson:params.lesson,filteredLessonByAuthorList:filteredLessonByAuthorList,offset:params.offset])
	}
	
	def list = {
		//TODO: Phase 2- Add share lesson list links to Social Media using their icons (like YouTube does)
		def user = springSecurityService.currentUser
		//TODO: 46- find out why 4 queries occur every time springSecurityService.currentUser is called (maybe MySecurityEventListener) and reduce the query.
		
		//TODO: 09- fix gsp for mobile view - sorting lessons does not work. Idea - make arrows bigger on mobile device and sort on click.
		
		def beginnerLessons = "Beginner"
		def intermediateLessons
		def advancedLessons
		def lessons = lessonService.lessonList(params, beginnerLessons, user)
		if (lessons.size() == 0){
			beginnerLessons = null
			intermediateLessons = "Intermediate"
			lessons = lessonService.lessonList(params, intermediateLessons, user)
		}
		if (lessons.size() == 0){
			advancedLessons = "Advanced"
			lessons = lessonService.lessonList(params, advancedLessons, user)
		}
		
		[lessons:lessons,
			beginnerLessons:beginnerLessons,
			intermediateLessons:intermediateLessons,
			advancedLessons:advancedLessons,
			user:user]
	}
	
	def selectedList = {
		def category = 'Beginner'
		def user = springSecurityService.currentUser
		if (params.category && params.category != 'null'){
			category = params.category
		}
		
		def lessons = lessonService.lessonList(params, category, user)
		
		render(template: "sortableList", model: [lessons:lessons, user:user])
	}
	
	def saveSortedList = {
		def user = springSecurityService.currentUser
		def sortedList = JSON.parse(params.sortedList)
		def initialList = JSON.parse(params.initialList)
		def lessonMap = [:]
		sortedList.eachWithIndex{ obj, i ->
			if (obj != initialList[i]){
				//Only change the sort order on list items that have changed				
				lessonMap.put(initialList[i], Lesson.findBySortOrderAndUser(obj, user)) 
			}
		}
		
		lessonMap.each{
			//If value is null, the lesson was deleted.
			//The sort order of lessons that were not deleted should still be save.
			if (it.value) {
				it.value.sortOrder=it.key.toInteger()
				if (!it.value.hasErrors() && it.value.save(flush: true)) {
					//println "Sort order updated!"
				}else{
					log.error("An error occurred saving sort order!")
				}
			}
		}
		render sortedList as JSON
	}

	def teach = {
		//TODO: 26- add label in gsp for informal/formal radio buttons and test
		def user = springSecurityService.currentUser
		if (user.authorities.any { it.authority == "ROLE_GUESTUSER" }) {
			redirect(controller:'user', action: 'edit')
		}
		//TODO: 44- just pass in lingo & dialect instead of entire user object.
		//TODO: 08- fix bug on mobile devices - show edit icon at all times for user to click on instead of on-hover.
		return [user:user]
	}
	
	def saveLesson = {
		def user = springSecurityService.currentUser
		def lesson = new Lesson(params)
		def updatedLesson = lessonService.saveLesson(lesson, user, request, null, null)
		if(updatedLesson.hasErrors()){
			render(view: "teach", model: [lesson:updatedLesson, user:user])
			return
		}
		userService.addCredits(user.account, lessonService.totalCreditsForLesson(lesson, user))
		//TODO: 32- save default entries to Account table and pass in to next new lesson
		//TODO: 43- pass in lingo & dialect instead of entire user object.
		redirect(action: 'learn', id: lesson.id)
		return
	}
	
	def edit = {
		//TODO: 48- use _lessonForm.gsp or other template
		def currentUser = springSecurityService.currentUser
		def lesson = Lesson.get(params.id)
		if (!lesson || currentUser.username != lesson.user.username) {
			//Lesson might have been deleted before the user had a chance to edit.
			//or currentUser may be trying to modify another user's lesson
			flash.error = g.message(code: "error.lesson.missing", default: "Sorry, that lesson can no longer be found.")
			redirect(action: "teach")
		}else {
			String imgFileExt = lesson.originalImageName?.substring(lesson.originalImageName?.lastIndexOf( "." ) + 1 ).toLowerCase()
			return [lesson: lesson, lessonImage: "${lesson.id}.${imgFileExt}"]
		}
	}
	
	def update = {
		boolean removeOldLesson = false
		def currentUser = springSecurityService.currentUser
		def currentLesson = Lesson.get(params.id)
		if (!currentLesson || currentUser.username != currentLesson.user.username) {
			//Lesson might have been deleted before the user had a chance to update.
			//or currentUser may be trying to modify another user's lesson
			flash.error = g.message(code: "error.lesson.missing", default: "Sorry, that lesson can no longer be found.")
			redirect(action: "teach")
			return
		}
		String imgFileExt = currentLesson.originalImageName?.substring(currentLesson.originalImageName?.lastIndexOf( "." ) + 1 ).toLowerCase()
		if (params.version) {
			def version = params.version.toLong()
			if (currentLesson.version > version) {
				currentLesson.errors.rejectValue('version','error.lesson.already.edited')
				render(view: "edit", model: [lesson:currentLesson, lessonImage: "${currentLesson.id}.${imgFileExt}"])
				return
			}
		}
		
		def previousLesson = new Lesson()
		previousLesson.properties = currentLesson.properties
		previousLesson.id = currentLesson.id

		currentLesson.properties = params 
		def lesson = currentLesson
		def lessonLang = currentLesson.masterLang
		def updateLang

		if(lessonLang.lingo != params.lingo || 
			(lessonLang.dialect ?: "") != (params.dialect ?: "") ){
			//new language parameters entered
			updateLang = languageService.updateLang(params.lingo, params.dialect, lessonLang)
			if (!updateLang.saved){
				//error occurred saving language.
				lesson.masterLang = null //passing null will allow validation to occur at lesson level
			}else{
				lesson.masterLang = updateLang.langObj
			}
		}
		
		def uploadedImage = request.getFile('imgPayload')
		def uploadedAudio = request.getFile('audio')
		
		def accountsOfViewedLesson = lessonService.accountsOfViewedLesson(lesson)
		
		if(accountsOfViewedLesson && 
			((uploadedImage?.originalFilename && currentLesson.originalImageName) || 
			(uploadedAudio?.originalFilename && currentLesson.originalAudioName))  ){
			//Lesson has been viewed and media has been changed. Create new lesson with new audio and/or image and delete old lesson.
			//This will allow translated lessons to still be used.
			lesson = new Lesson(params)
			removeOldLesson = true
		}
		
		lesson = lessonService.saveLesson(lesson, currentUser, request, lesson.masterLang, previousLesson)
		if (lesson.hasErrors()) {
			if (updateLang?.langObj){
				languageService.deleteUnusedLang(updateLang?.langObj)
			}
			render(view: "edit", model: [lesson:lesson, lessonImage: "${currentLesson.id}.${imgFileExt}"])
			return
		}else {
			if(uploadedImage.originalFilename && uploadedImage.originalFilename.substring(uploadedImage.originalFilename.lastIndexOf( "." ) + 1 ) !=
				previousLesson?.originalImageName.substring(previousLesson?.originalImageName.lastIndexOf( "." ) + 1 )){
				//File extensions of previously saved image and current image don't match.
				//Deleting original because it won't be overwritten.
				lessonService.deleteImgFromUsersLesson(previousLesson)
			}
			languageService.deleteUnusedLang(lessonLang)
			if (removeOldLesson){
				lessonService.moveMediaToNewLesson(currentLesson, lesson, uploadedImage, uploadedAudio)
				lessonService.deactivateOrDeleteLesson(currentLesson)
			}
			redirect(action: "learn", id: lesson.id)
			return
		}

	}

	def delete = {
		//TODO: 07 - When deleting a lesson, remove it from saved lesson plans & advance to the next lesson created by the user or back to teach if none exists
		def lessonInstance = Lesson.get(params.id)
		def currentUser = springSecurityService.currentUser
		if(!lessonInstance.active || currentUser.username != lessonInstance.user.username){
			//if lesson is already set to non-active, no need to delete.
			//or currentUser may be trying to delete another user's lesson
			redirect(action: "teach")
			return
		}
		
		def lessonDeletedOrInactive = lessonService.deactivateOrDeleteLesson(lessonInstance)

		if (lessonDeletedOrInactive){
			//TODO: Phase 2- Add animation to show lesson going into the trash can.
			userService.removeCredits(currentUser.account, lessonService.totalCreditsForLesson(lessonInstance, currentUser))
			redirect(action: "teach")
		}else{
			//An error occurred deleting
			flash.error = g.message(code: "error.unknown", default: "An unknown error has occurred. Please try again.")
			redirect(action: "learn", id: params.id)
		}
	}
	
	def lessonPlan = {
		def currentUser = springSecurityService.currentUser
		def lessonsByIndex = [:]
		def currentIDindex
		
		if(params.auth){
			//TODO: Phase 2- This is hacky. Allow the user to send a filtered list of params through the shared link instead of hard coding the params
			if(!params.beginner && !params.intermediate && !params.advanced && !params.formal && !params.informal){
				params.beginner = true
				params.intermediate = true
				params.advanced = true
				params.formal = true
				params.informal = true
			}
			//TODO: 04- fix bug - If author has lessons with two or more different languages, return only one language (the one passed in)
			params.filteredLessonByAuthorList = lessonService.filteredLessonsByUser(params, User.get(params.auth?.toInteger() ), currentUser)

			if(params.filteredLessonByAuthorList.size()>0){
				params.id = params.filteredLessonByAuthorList[0]
			}
			
		}
		
		if(!params.filteredLessonByAuthorList){
			params.filteredLessonByAuthorList = currentUser?.account?.filteredLessonsByAuthor?.split(",")
			if(params.filteredLessonByAuthorList?.size()>0){
				params.id = params.filteredLessonByAuthorList[0]
			}
		}
		
		if(!params.filteredLessonByAuthorList){
			redirect(action: "index", params: [
					beginner:params.beginner,
					intermediate:params.intermediate,
					advanced:params.advanced,
					formal:params.formal,
					informal:params.informal]) 
			return
		}

		if(params.next){
			def nextID
			if (params.filteredLessonByAuthorList?.last()==params.id){
				//if last item is being viewed, use the first item as the next lesson
				nextID = params.filteredLessonByAuthorList?.first()
			}else{
				//use the next id in the list
				params.filteredLessonByAuthorList.eachWithIndex{ obj, i ->
					lessonsByIndex.put(i, obj)
					if (obj == params.id){
						currentIDindex = lessonsByIndex.size()
					}
				}
				// if currentIDindex is 1 then the next item will be selected from this list
				// because the first item is a 0 value [0:1, 1:2, 2:3, 3:4]
				nextID = lessonsByIndex[currentIDindex?:0] //if currentIDindex is null, use the first item in the array
			}
			params.id = nextID
		}else if(params.previous){
			params.filteredLessonByAuthorList.eachWithIndex{ obj, i ->
				lessonsByIndex.put(i, obj)
				if (obj == params.id){
					//use the previous id in the list
					currentIDindex = lessonsByIndex?.size()-2
					// if lessonsByIndex size is 2 then the first item will be selected from this list
					// because the first item is a 0 value [0:1, 1:2, 2:3, 3:4]
				}
			}
			params.id = lessonsByIndex[currentIDindex?:0] //if currentIDindex is null, use the first item in the array
		}else if (params.filteredLessonByAuthorList){
		
			if(params.translate){
				//translate param is passed in when the user is at 0 credits and wants to unlock more lessons,
				//so they are presented with the first lesson in the list that has not been translated yet
				def lesssonNeedingTranslation = lessonService.lesssonNeedingTranslation(params.filteredLessonByAuthorList, currentUser)
				if(lesssonNeedingTranslation){
					params.id = lesssonNeedingTranslation
				}else{
					//This gets hit when the user is at 0 credits and views a new lesson plan.
					//In this scenario, the first viewed lesson that has not yet been translated will be presented.
					def viewedLessonNeedingTranslation = ViewedLesson.findWhere(user: currentUser, translation: null)?.lesson
					params.id = viewedLessonNeedingTranslation?.id
					lessonService.filteredLessonsByUser(params, viewedLessonNeedingTranslation?.user, currentUser) 
				}
			}
			
			//if user already has previous quizzes save in their account, clear them out.
			currentUser?.account?.shuffledLessons = null
			currentUser?.account?.save()
			
		}
		redirect(action: "learn", params: [id: params.id?.toLong(),
					beginner:params.beginner,
					intermediate:params.intermediate,
					advanced:params.advanced,
					formal:params.formal,
					informal:params.informal])
	}
	
	def reviewLessons = {
		if(params.id && (params.mediaApproved || params.lessonApproved)){
			def lesson = Lesson.get(params.id.toLong())
			lesson.mediaApproved = params.mediaApproved ? true : false
			lesson.lessonApproved = params.lessonApproved ? true : false
			lesson.save(flush:true)
		}
		def currentUser = springSecurityService.currentUser
		def allLessonsNotReviewed = lessonService.allLessonsNotReviewed(currentUser, params.mediaReview)
		
		if(allLessonsNotReviewed.size()==0){
			flash.message = "All lessons have been reviewed. Nothing New."
			redirect(action: "index")
			return
		}else{
			params.id = allLessonsNotReviewed[0].toLong()
		}
		redirect(action: "learn", params: [id: params.id, mediaReview:params.mediaReview])
	}
	
	def learn = {
		//TODO: 06- allow students who logged in and want to translate a lesson to upload audio.
				//See lingoto\Documents\FlowCharts\audio-upload.jpg (need to convert idea to a flow chart)
		def currentUser = springSecurityService.currentUser
		//TODO: if no currentUser and session.credits <= 0, redirect to Create Account or display "Create Account" Link
		def filteredLessonByAuthorList = currentUser?.account?.filteredLessonsByAuthor?.split(",")
		if (params.offset && filteredLessonByAuthorList && filteredLessonByAuthorList?.size()>0){
			//if a pagination number is selected and the array of lessons is provided,
			//identify which lesson to display.
			params.id = filteredLessonByAuthorList[params.offset.toInteger()]
		}
        if (!params.id) {
			flash.error = g.message(code: "error.lesson.missing", default: "Sorry, that lesson can no longer be found.")
			return
        }
        def lesson = Lesson.get(params.id)
		if (!lesson) {
			flash.error = g.message(code: "error.lesson.missing", default: "Sorry, that lesson can no longer be found.")
			return
		}

		session.usersLanguage = currentUser.account.primaryLanguage.lingo
		session.usersDialect = currentUser.account.primaryLanguage.dialect
		
		def lessonsByCategory
		def viewedLesson
		def vLcount = 0
		def ratingClassArray = ['ratingGold','ratingGold','ratingGold','ratingGold','ratingGold']
        
		boolean lessonNotInList = true
		if (filteredLessonByAuthorList && filteredLessonByAuthorList?.size()>0 && params.id){
			//if a user selects the previous or next arrow
			//set the offset value so pagination number will be highlighted
			filteredLessonByAuthorList.eachWithIndex { obj, i ->
				if(obj == params.id){
					params.offset = i
					lessonNotInList = false
				}
			}
		}
		if(!params.beginner && !params.intermediate && !params.advanced && !params.formal && !params.informal){
			//If no filter params passed in, use all of them.
			params.beginner = true
			params.intermediate = true
			params.advanced = true
			params.formal = true
			params.informal = true
		}
		if (lessonNotInList){
			//If lesson number was passed in and does not match what's in the list, reset the filteredLessonByAuthorList
			filteredLessonByAuthorList = lessonService.filteredLessonsByUser(params, lesson.user, currentUser)
			filteredLessonByAuthorList.eachWithIndex { obj, i ->
				if(obj == params.id?.toInteger()){
					params.offset = i //adding offset value so correct pagination number will be highlighted
				}
			}
		}
		
		def rating
		//if user did not create the lesson...
		if(currentUser?.username != lesson.user.username){
			if (userService.getAgeOfUser(currentUser?.account) < (lesson.ageRestriction ?: 12)){
				//Only allow user to see lessons suitable for their age.
				redirect(action: "index")
				return
			}
			def previouslyRated = Rate.findByRatedByAccountID(currentUser?.account?.id)
			//TODO: test if a user rated multiple lessons. This rating system may need some work.
			
			//if user already rated, show what they rated it
			//otherwise, get the average rating for the user account they are looking at
			if(previouslyRated){
				rating = previouslyRated.rating
			}else{
				rating = lessonService.rating(lesson.user.account.id)?.round() ?: 0
			}
			
			//Creating array of css class labels to use for rating stars.
			def replaceThisMany = 5 - rating
			while (replaceThisMany-->0){
				//if rating is 4 there will be 1 gray and 4 gold in the array.
				ratingClassArray = ['ratingGray', *ratingClassArray]
				ratingClassArray = ratingClassArray.take(ratingClassArray.size() - 1)
			}
			
			viewedLesson = ViewedLesson.findByUserAndLesson(currentUser, lesson)
			if (!viewedLesson && currentUser?.account?.credits > 0){
				//TODO: Phase 2- Create "Purchase Credits" page. When the user has 0 credits provide link to "Purchase credits."
				
				//if lesson has not been viewed and the user still has credits left, 
				//save as a viewed lesson and reduce amount of credits left
				currentUser?.account?.credits = currentUser?.account?.credits -1
				currentUser?.account?.save(flush:true)
				viewedLesson = new ViewedLesson(user:currentUser, lesson:lesson).save(flush:true)
			}
			//getting count of viewedLessons that can be used for the quiz.
			//only displaying take quiz link if 4 or more are found.
			//TODO: 42- fix query to pull just the number instead of an array
			def vLcountArray = ViewedLesson.executeQuery('select count(v) from ViewedLesson v WHERE v.passedQuiz = false AND v.lesson.imagePath IS NOT NULL AND v.lesson.masterLang.id = ? AND v.user = ?',
				[lesson.masterLang.id, currentUser])
			vLcount = vLcountArray ? vLcountArray[0] : 0 // if array contains a value, use it, otherwise use 0
		}
		lessonsByCategory = Lesson.findAllByCategoryAndUser(lesson.category, lesson.user)
		
		session.credits = currentUser?.account?.credits ?: 0
		
		String imgFileExt = lesson.originalImageName?.substring(lesson.originalImageName?.lastIndexOf( "." ) + 1 ).toLowerCase()
		return [lesson:lesson, ratingClassArray:ratingClassArray, 
				lessonImage: "${lesson.id}.${imgFileExt}",
				vLcount:vLcount,
				viewedLesson:viewedLesson, 
				filteredLessonByAuthorList:filteredLessonByAuthorList,
				beginner:params.beginner ? "true" : "false",
				intermediate:params.intermediate ? "true" : "false",
				advanced:params.advanced ? "true" : "false",
				formal:params.formal ? "true" : "false",
				informal:params.informal ? "true" : "false"]
    }
	
	def saveTranslation = {
		def translatedLesson = ViewedLesson.get(params.viewedLessonId)
		def currentUser = springSecurityService.currentUser
		if (currentUser.username != translatedLesson.user.username){
			//currentUser may be trying to modify another user's lesson
			render null as JSON
			return
		}
		//TODO: Phase 2- May need to add some more rules to prevent invalid translations (no special characters or numbers by themselves, 
			//do not allow the same translation to be used more then 3 times in a row).
		//TODO: Phase 2- Give admin the ability to remove translate lesson feature
		if (!translatedLesson.translation && params.translation && params.translation?.trim() != ""){
			//only add credits to account if the lesson hasn't already been translated.
			currentUser.account.credits = currentUser.account.credits +1
			currentUser.account.save(flush:true)
			session.credits = currentUser.account.credits
		}
		
		if (translatedLesson.translation != params.translation && params.translation && params.translation?.trim() != ""){
			translatedLesson.translation = params.translation
			translatedLesson.save(flush:true)
		}
		
		def viewedLesson = JSON.parse(params.viewedLessonId)
		render viewedLesson as JSON
	}
	
	def saveRating = {
		def currentUser = springSecurityService.currentUser
		def previouslyRated = Rate.findByRatedByAccountID(currentUser.account.id)
		
		if (previouslyRated){
			previouslyRated.rating = params.rating.toInteger()
			previouslyRated.save()
		}else{
			def newRating = new Rate(rating:params.rating.toInteger(),
				ratedAccountID:params.accountId,
				ratedByAccountID:currentUser.account.id).save()
		}
		
		def saveRating = JSON.parse(params.rating)
		render saveRating as JSON
	}
	
	def grader = {
		def lessonId = params.lessonId?.toLong()
		if(params.markedIncorrect != "true"){
			def currentUser = springSecurityService.currentUser
			def c = ViewedLesson.createCriteria()
			//TODO: 42- change query to pull one instead of a list.
			def viewedLessonArray = c {
				lesson{
					eq('id', lessonId)
				}
				user{
					eq('id',currentUser.id)
				}
			}
			def viewedLesson = viewedLessonArray[0] //should only have one item in the array
			viewedLesson?.passedQuiz = true
			viewedLesson.save(flush:true)
		}
		
		def percentage
		def shuffledLessonList = []
		def currentUser = springSecurityService.currentUser
		def shuffledLessons = currentUser?.account?.shuffledLessons

		//remove first item from shuffled list because it was already viewed.
		//if only one lesson left that has already been viewed, shuffledLessonList will be null.
		def shuffledLessonsArray = shuffledLessons.split(",")
		shuffledLessonList = shuffledLessonsArray - params.lessonId
		
		if(shuffledLessonsArray.size() == 1){
			
			def query = 'select v.lesson.id from ViewedLesson v WHERE v.lesson.imagePath IS NOT NULL AND v.lesson.masterLang.id = ? AND v.user = ?'
			def quizableLessons = ViewedLesson.executeQuery(query + ' AND v.passedQuiz = false',[params.langId.toLong(), currentUser])
			
			//last quiz has been viewed. Display percentage correct.
			//find allViewedLessons that the user both passed and failed, used for percentage.
			def allViewedLessons = ViewedLesson.executeQuery(query,[params.langId.toLong(), currentUser])
			def percentform = new DecimalFormat("###.##%")
			percentage = percentform.format(
				(allViewedLessons.size() - quizableLessons.size()) / allViewedLessons.size() )
			
			//rebuild shuffledLessonList with quizableLessons the user hasn't passed
			//and move last quiz item to the end. Quiz will continue until user gets 100%
			shuffledLessonList = []
			quizableLessons.each{
				if (shuffledLessonsArray[0] != it){
					shuffledLessonList << it
				}
			}
			Collections.shuffle(shuffledLessonList)
			shuffledLessonList << shuffledLessonsArray[0]
		}
		
		currentUser?.account?.shuffledLessons = shuffledLessonList?.join(",")
		currentUser.account.save(flush:true)
		
		redirect(action: "quiz", params: [
				langId:params.langId, lessonId:lessonId, shuffledLessonList:shuffledLessonList,
				shuffledLessons:shuffledLessons, currentUser:currentUser, percentage:percentage])
		return
	}
	
	def quiz = {
		def lessonId
		def randomLessonList = []
		def lesson
		def percentCorrect = null
		def currentUser = springSecurityService.currentUser
		def shuffledLessons = params.shuffledLessons ?: currentUser?.account?.shuffledLessons
		def shuffledLessonList = params.shuffledLessonList ?: currentUser?.account?.shuffledLessons?.split(",")
		
		//an ID is passed through when the creator of the lesson wants to see what it looks like as a quiz.
		if(params.id){
			//get the lesson used in the quiz
			lesson = Lesson.executeQuery('select new map(a.id as id, a.wordPhrase as wordPhrase, a.category as category, a.imagePath as imagePath) from Lesson a where a.id = ?',[params.id.toLong()])
			randomLessonList = lessonService.randomLessonSample(params.id, currentUser, lesson)
		}else if(params.langId){
			//Generating quiz of viewed lessons based on the language of the lesson.
			def query = 'select v.lesson.id from ViewedLesson v WHERE v.lesson.imagePath IS NOT NULL AND v.lesson.masterLang.id = ? AND v.user = ?'
			def quizableLessons = ViewedLesson.executeQuery(query + ' AND v.passedQuiz = false',[params.langId.toLong(), currentUser])
			if (quizableLessons && !params.lessonId && !shuffledLessons){
				//beginning of quiz or refresh occurred.
				shuffledLessonList = []
				quizableLessons.each{
					shuffledLessonList << it
				}
				Collections.shuffle(shuffledLessonList)
				currentUser?.account?.shuffledLessons = shuffledLessonList?.join(",")
				currentUser.account.save(flush:true)
			}
			if(!quizableLessons){
				//No quizable lessons found. User must have passed all quiz items.
				//Return to last viewed lesson, passing in filter parameters and display 100%.
				def viewedLessonQuery = """select new map(v.lesson.id as id, v.lesson.category as category, v.lesson.informal as informal) from ViewedLesson v
						WHERE v.lesson.imagePath IS NOT NULL AND v.lesson.masterLang.id = ? AND v.user = ?"""
				
				def viewedLesson = ViewedLesson.executeQuery(viewedLessonQuery,[params.langId.toLong(), currentUser])
				flash.message = "100%"
				currentUser?.account?.shuffledLessons = null
				currentUser?.account.save()
				redirect(action: "learn", params: [id: viewedLesson.last().id.toLong(),
					beginner:(viewedLesson.last().category == "Beginner") ? "Beginner" : null,
					intermediate:(viewedLesson.last().category == "Intermediate") ? "Intermediate" : null,
					advanced:(viewedLesson.last().category == "Advanced") ? "Advanced" : null,
					formal:(viewedLesson.last().informal == false) ? "Formal" : null,
					informal:(viewedLesson.last().informal == true) ? "Informal" : null])
				return
			}

			//find lesson from the first item in the shuffled list, it will be used as the quiz item.
			lessonId = shuffledLessonList[0]
			lesson = Lesson.executeQuery('select new map(l.id as id, l.wordPhrase as wordPhrase, l.imagePath as imagePath, l.originalImageName as originalImageName, l.audioPath as audioPath) from Lesson l where l.id = ?',
							[lessonId?.toLong()])
			
			//getting random lesson list, used to find 3 random images that are not the lesson being quizzed against.
			def quizableLessonsToRandomize
			if (quizableLessons.size() < 4){
				//if the user passed all but 3 or less quizzes, 
				//find quizableLessons that the user either passed or failed to prevent pulling from a null object.
				quizableLessonsToRandomize = ViewedLesson.executeQuery(query,[params.langId.toLong(), currentUser])
			}else{
				quizableLessonsToRandomize = quizableLessons
			}
			
			quizableLessonsToRandomize.each{
				if(lessonId.toLong() != it){
					randomLessonList << it //add random lessons to array, except the lesson being quizzed
				}
			}
			
		}
		
		if(lesson){
			def randomImageNames = []
			
			//get 3 random images from the filtered lesson list except the lesson image
			
			Collections.shuffle(randomLessonList)
			String lessonImageQuery = 'select new map(l.id as id, l.imagePath as imagePath, l.originalImageName as originalImageName) from Lesson l where l.id = ?'
			def randomLesson1 = Lesson.executeQuery(lessonImageQuery,[randomLessonList[0]?.toLong()])
			def randomLesson2 = Lesson.executeQuery(lessonImageQuery,[randomLessonList[1]?.toLong()])
			def randomLesson3 = Lesson.executeQuery(lessonImageQuery,[randomLessonList[2]?.toLong()])
			
			randomImageNames << lessonService.getSmallImgName(randomLesson1[0])
			randomImageNames << lessonService.getSmallImgName(randomLesson2[0])
			randomImageNames << lessonService.getSmallImgName(randomLesson3[0])
			
			def imageNames = [randomImageNames[0],randomImageNames[1],randomImageNames[2],lessonService.getSmallImgName(lesson[0])]
			Collections.shuffle(imageNames) //randomize array of 4 images to display, including the lesson image
			if(params.percentage){
				flash.message = params.percentage
			}
			return [ lesson:lesson[0], imageNames:imageNames, langId:params.langId ]
		}else{
			redirect(controller: "home", action: "index")
		}
	}
	
}
