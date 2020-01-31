package com.lingoto

import java.text.DecimalFormat
import grails.converters.JSON
import org.springframework.web.servlet.support.RequestContextUtils

class LessonController {

	def springSecurityService
	def lessonService
	def languageService
	
	
	def index = {
		//TODO: 21- Fix filter buttons & when nothing is found use the following in gsp: <g:message code="nothingFound.message" default="Nothing found." />
		def lessonMap = [:]
		//def user = springSecurityService.currentUser
		def c = MasterLang.createCriteria()
		def languagesWithLessons = c {
			//only show if there are 5 or more lessons
			sizeGt("lessons", 5)
			maxResults(10000) //prevents too large of a result set
		}
		//TODO: Phase 2- add search feature
		//TODO: Phase 2- only show languages with languageRef but allow search to pull up all languages
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
		
		def beginnerLessonList = "Beginner"
		def intermediateLessonList
		def advancedLessonList
		def lessons = lessonService.lessonList(params, beginnerLessonList, user)
		if (lessons.size() == 0){
			beginnerLessonList = null
			intermediateLessonList = "Intermediate"
			lessons = lessonService.lessonList(params, intermediateLessonList, user)
		}
		if (lessons.size() == 0){
			advancedLessonList = "Advanced"
			lessons = lessonService.lessonList(params, advancedLessonList, user)
		}
		
		[lessons:lessons,
			beginnerLessonList:beginnerLessonList,
			intermediateLessonList:intermediateLessonList,
			advancedLessonList:advancedLessonList,
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
		//TODO: 44- just pass in lingo & dialect instead of entire user object.
		//TODO: 16- change images of camera/audio icons to use green/blue color scheme
		return [user:user]
	}
	
	def saveLesson = {
		def user = springSecurityService.currentUser
		def lesson = new Lesson(params)
		lesson = lessonService.saveLesson(lesson, user, request, null, null)
		
		if(lesson.hasErrors()){
			render(view: "teach", model: [lesson:lesson, user:user])
			return
		}
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
			return [lesson: lesson]
		}
	}
	
	def update = {
		boolean deleteOldLesson = false
		def currentUser = springSecurityService.currentUser
		def currentLesson = Lesson.get(params.id)
		if (!currentLesson || currentUser.username != currentLesson.user.username) {
			//Lesson might have been deleted before the user had a chance to update.
			//or currentUser may be trying to modify another user's lesson
			flash.error = g.message(code: "error.lesson.missing", default: "Sorry, that lesson can no longer be found.")
			redirect(action: "teach")
			return
		}
		if (params.version) {
			def version = params.version.toLong()
			if (currentLesson.version > version) {
				currentLesson.errors.rejectValue('version','error.lesson.already.edited')
				render(view: "edit", model: [lesson:currentLesson])
				return
			}
		}

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
		
		if(uploadedImage?.originalFilename || uploadedAudio?.originalFilename){
			//create new lesson with new audio or image and delete old lesson
			//TODO: 1- editing an existing lesson is broken. Fix it.
			lesson = new Lesson(params)
			deleteOldLesson = true
		}
		
		lesson = lessonService.saveLesson(lesson, currentUser, request, lesson.masterLang, currentLesson)
		if (!lesson.hasErrors()) {
			languageService.deleteUnusedLang(lessonLang)
			if (deleteOldLesson){
				def lessonDeletedOrInactive = lessonService.deactivateOrDeleteLesson(currentLesson, currentUser)
			}
			redirect(action: "learn", id: lesson.id)
		}else {
			if (updateLang?.langObj){
				languageService.deleteUnusedLang(updateLang?.langObj)
			}
			
			//TODO: 51- move to service
			//removing duplicate error messages
			def savedErrors = lesson.errors.allErrors.findAll{ true }
			savedErrors = savedErrors?.unique(new ErrorComparator())
			lesson.clearErrors()
			savedErrors.each {
					lesson.errors.addError(it)
			}
			render(view: "edit", model: [lesson:lesson])
			return
		}

	}

	def delete = {
		def lessonInstance = Lesson.get(params.id)
		def currentUser = springSecurityService.currentUser
		if(!lessonInstance.active || currentUser.username != lessonInstance.user.username){
			//if lesson is already set to non-active, no need to delete.
			//or currentUser may be trying to delete another user's lesson
			redirect(action: "teach")
			return
		}
		
		def lessonDeletedOrInactive = lessonService.deactivateOrDeleteLesson(lessonInstance, currentUser)

		if (lessonDeletedOrInactive){
			//If lesson has been viewed by another user, add credit to user who viewed deleted/inactive lesson to make up for deletion.
			def viewedLessons = ViewedLesson.executeQuery('select l.user from ViewedLesson l where l.lesson =?',[lessonInstance])
			//TODO: 39- performance test if 1000+ users viewed the lesson.
			viewedLessons.each{
				it.account.credits = it.account.credits +1
				it.account.save()
			}
			
			//TODO: Phase 2- Add animation to show lesson going into the trash can.
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
			if(!params.beginner){
				params.beginner = true
				params.intermediate = true
				params.advanced = true
				params.formal = true
				params.informal = true
			}
			
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
			currentUser.account.save()
			
		}
		redirect(action: "learn", params: [id: params.id?.toLong(),
					beginner:params.beginner,
					intermediate:params.intermediate,
					advanced:params.advanced,
					formal:params.formal,
					informal:params.informal])
	}
	
	def learn = {
		//TODO: prevent rating stars from changing size and preventing selection when screen is small		
		def currentUser = springSecurityService.currentUser
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
        
		
		if (filteredLessonByAuthorList && filteredLessonByAuthorList?.size()>0 && params.id){
			//if a user selects the previous or next arrow
			//set the offset value so pagination number will be highlighted
			filteredLessonByAuthorList.eachWithIndex { obj, i ->
				if(obj == params.id){
					params.offset = i
				}
			}
		}
		
		def rating
		//if user did not create the lesson...
		if(currentUser.username != lesson.user.username){
			if (currentUser.account.age < lesson.ageRestriction){
				//Only allow user to see lessons suitable for their age.
				redirect(action: "index")
				return
			}
			def previouslyRated = Rate.findByRatedByAccountID(currentUser.account.id)
			//if user already rated, show what they rated it
			//otherwise, get the average rating for the user account they are looking at
			if(previouslyRated){
				rating = previouslyRated.rating
			}else{
				rating = lessonService.rating(lesson.user.account.id).round()
			}
			
			//Creating array of css class labels to use for rating stars.
			def replaceThisMany = 5 - rating
			while (replaceThisMany-->0){
				//if rating is 4 there will be 1 gray and 4 gold in the array.
				ratingClassArray = ['ratingGray', *ratingClassArray]
				ratingClassArray = ratingClassArray.take(ratingClassArray.size() - 1)
			}
			
			viewedLesson = ViewedLesson.findByUserAndLesson(currentUser, lesson)
			if (!viewedLesson && currentUser.account.credits > 0){
				//TODO: Phase 2- Create "Purchase Credits" page. When the user has 0 credits provide link to "Purchase credits."
				
				//if lesson has not been viewed and the user still has credits left, 
				//save as a viewed lesson and reduce amount of credits left
				currentUser.account.credits = currentUser.account.credits -1
				currentUser.account.save()
				viewedLesson = new ViewedLesson(user:currentUser, lesson:lesson).save()
			}
			//getting count of viewedLessons that can be used for the quiz.
			//only displaying take quiz link if 4 or more are found.
			//TODO: 42- fix query to pull just the number instead of an array
			def vLcountArray = ViewedLesson.executeQuery('select count(v) from ViewedLesson v WHERE v.passedQuiz = false AND v.lesson.imagePath IS NOT NULL AND v.lesson.masterLang.id = ?',[lesson.masterLang.id])
			vLcount = vLcountArray ? vLcountArray[0] : 0 // if array contains a value, use it, otherwise use 0
		}
		lessonsByCategory = Lesson.findAllByCategoryAndUser(lesson.category, lesson.user)
		session.credits = currentUser.account.credits
		
		return [lesson:lesson, ratingClassArray:ratingClassArray, 
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
			currentUser.account.save()
			session.credits = currentUser.account.credits
		}
		
		if (translatedLesson.translation != params.translation && params.translation && params.translation?.trim() != ""){
			translatedLesson.translation = params.translation
			translatedLesson.save()
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
			//TODO: change query to use get to pull one instead of a list.
			def viewedLessonArray = c {
				lesson{
					eq('id', lessonId)
				}
				user{
					eq('id',currentUser.id)
				}
			}
			def viewedLesson = viewedLessonArray[0] //should only have one item in the array
			viewedLesson.passedQuiz = true
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
			
			def query = 'select v.lesson.id from ViewedLesson v WHERE v.lesson.imagePath IS NOT NULL AND v.lesson.masterLang.id = ?'
			def quizableLessons = ViewedLesson.executeQuery(query + ' AND v.passedQuiz = false',[params.langId.toLong()])
			
			//last quiz has been viewed. Display percentage correct.
			//find allViewedLessons that the user both passed and failed, used for percentage.
			def allViewedLessons = ViewedLesson.executeQuery(query,[params.langId.toLong()])
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
		//TODO: if lesson has audio, allow playing of audio in the quiz
		def lessonId
		def randomLessonList = []
		def lesson
		def percentCorrect = null
		def currentUser = params.currentUser ?: springSecurityService.currentUser
		def shuffledLessons = params.shuffledLessons ?: currentUser?.account?.shuffledLessons
		def shuffledLessonList = params.shuffledLessonList ?: currentUser?.account?.shuffledLessons?.split(",")
		
		//an ID is passed through when the creator of the lesson wants to see what it looks like as a quiz.
		if(params.id){
			//get the lesson used in the quiz
			lesson = Lesson.executeQuery('select new map(a.id as id, a.wordPhrase as wordPhrase, a.category as category, a.imagePath as imagePath, a.imageExt as imageExt) from Lesson a where a.id = ?',[params.id.toLong()])
			randomLessonList = lessonService.randomLessonSample(params.id, currentUser, lesson)
		}else if(params.langId){
			//Generating quiz of viewed lessons based on the language of the lesson.
			def query = 'select v.lesson.id from ViewedLesson v WHERE v.lesson.imagePath IS NOT NULL AND v.lesson.masterLang.id = ?'
			def quizableLessons = ViewedLesson.executeQuery(query + ' AND v.passedQuiz = false',[params.langId.toLong()])
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
						WHERE v.lesson.imagePath IS NOT NULL AND v.lesson.masterLang.id = ?"""
				
				def viewedLesson = ViewedLesson.executeQuery(viewedLessonQuery,[params.langId.toLong()])
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
			lesson = Lesson.executeQuery('select new map(a.id as id, a.wordPhrase as wordPhrase, a.imagePath as imagePath, a.imageExt as imageExt) from Lesson a where a.id = ?',[lessonId?.toLong()])
			
			//getting random lesson list, used to find 3 random images that are not the lesson being quizzed against.
			def quizableLessonsToRandomize
			if (quizableLessons.size() < 4){
				//if the user passed all but 3 or less quizzes, 
				//find quizableLessons that the user either passed or failed to prevent pulling from a null object.
				quizableLessonsToRandomize = ViewedLesson.executeQuery(query,[params.langId.toLong()])
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
			
			def randomLesson1 = Lesson.executeQuery('select new map(a.id as id, a.imagePath as imagePath, a.imageExt as imageExt) from Lesson a where a.id = ?',[randomLessonList[0]?.toLong()])
			def randomLesson2 = Lesson.executeQuery('select new map(a.id as id, a.imagePath as imagePath, a.imageExt as imageExt) from Lesson a where a.id = ?',[randomLessonList[1]?.toLong()])
			def randomLesson3 = Lesson.executeQuery('select new map(a.id as id, a.imagePath as imagePath, a.imageExt as imageExt) from Lesson a where a.id = ?',[randomLessonList[2]?.toLong()])
			
			randomImageNames << lessonService.getImgName(randomLesson1[0])
			randomImageNames << lessonService.getImgName(randomLesson2[0])
			randomImageNames << lessonService.getImgName(randomLesson3[0])
			
			def imageNames = [randomImageNames[0],randomImageNames[1],randomImageNames[2],lessonService.getImgName(lesson[0])]
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
