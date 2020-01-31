package com.lingoto

//JAVE - http://www.sauronsoftware.it/projects/jave/manual.php
import it.sauronsoftware.jave.AudioAttributes
import it.sauronsoftware.jave.Encoder
import it.sauronsoftware.jave.EncodingAttributes
import javax.imageio.ImageIO
import javax.servlet.ServletContext
import java.awt.image.BufferedImage
import org.springframework.web.servlet.support.RequestContextUtils
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.hibernate.criterion.CriteriaSpecification
import grails.util.Environment

class LessonService {
	
	def springSecurityService
	def burningImageService
	def userService
	
	//TODO: Phase 2- 5- candidate for moving to a DB table an Admin can make changes to.
	Integer imageCredits = 1
	Integer videoCredits = 2
	Integer audioCredits = 3
	
	ServletContext sc = SCH.getServletContext()
	
	/**
	 * Get list of active lessons for a User filtered by category.
	 * 
	 * @param params - params.max & params.offset for pagination
	 * @param category - String (Beginner, Intermediate or Advanced)
	 * @param user - User of who's lessons to list
	 * @return list of lessons 
	 */
	def lessonList(params, category, user) {
		def c = Lesson.createCriteria()
		params.max = Math.min(params.max ? params.int('max') :20, 100)
		def beginnerlessons = c.list(params){
			eq('user',user)
			eq('category', category)
			eq('active',true)
			order("sortOrder", "asc")
		}
    }
	
	/**
	 * Find all authors of lessons for a specific lang/dialect
	 * filtered down based on params passed in & age restriction.
	 * 
	 * @param params - [lingo, dialect, beginner, intermediate, advanced, formal, informal]
	 * @return list of User IDs (max of 100)
	 */
	def filteredAuthorsWithLessons(params){
		def user = springSecurityService.currentUser
		//TODO: 38- Find highest rated ones first. If more then 100, the lowest rated ones may not show. 
				//Use saveRating in lesson controller to save average rating to account for faster query.
		
		def c = Lesson.createCriteria()
		def authorsWithLessonsByLang = c.list() {
			
			resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
			eq('active',true)
			or {isNull('ageRestriction')
				le('ageRestriction',userService.getAgeOfUser(user.account))}
			if (!params.beginner){
				ne('category','Beginner')
			}
			if (!params.intermediate){
				ne('category','Intermediate')
			}
			if (!params.advanced){
				ne('category','Advanced')
			}
			if (!params.formal){
				ne('informal',false)
			}
			if (!params.informal){
				ne('informal',true)
			}
			masterLang{
				eq('lingo', params.lingo)
				if(params.dialect == "null"){
					isNull('dialect')
				}else{
					eq('dialect', params.dialect)
				}
			}
			createAlias('user', 'u')
			projections {
				distinct("u.id","id")
			}
			maxResults(100)
		}
		return authorsWithLessonsByLang
	}
	
	/**
	 * Builds an array of authors with ratings
	 * 
	 * @param authorsWithLessonsByLang - obtained by using filteredAuthorsWithLessons
	 * @return array of arrays containing [firstname, lastname, userId, rating]
	 */
	def buildAuthorAndRatingsArray(authorsWithLessonsByLang){
		def authors = []
		def lessonMapByAuthor = [:]
		//TODO: 38- sort by rating first then most lessons, then username.
		authorsWithLessonsByLang.each{
			def userAccountInfo = [:]
			userAccountInfo = User.executeQuery(
				'select new map(u.account.id as id, u.account.firstName as firstName, u.account.lastName as lastName) from User u where u.id = ?',
				[it.id])
			if (userAccountInfo.size() > 0){
				def rating = rating(userAccountInfo[0].id)
				authors << ["${userAccountInfo[0].firstName} ${userAccountInfo[0].lastName}", it.id, rating?.round()]
			}
		}
		return authors
	}
	
	/**
	 * Get a list of Lessons by User (filtered base on params passed in & age restriction) and 
	 * saved to Account.filteredLessonsByAuthor
	 * 
	 * @param params - [beginner, intermediate, advanced, formal, informal]
	 * @param user - Author of lessons
	 * @param currentUser - User viewing the lesson plan
	 * @return list of lesson IDs (at most 10000)
	 */
	def filteredLessonsByUser(params, user, currentUser){
		def c = Lesson.createCriteria()
		def lessonsByUser = c {
			eq('active',true)
			or {isNull('ageRestriction')
				le('ageRestriction',userService.getAgeOfUser(user.account))}
			eq('user',user)
			if (!params.beginner){
				ne('category','Beginner')
			}
			if (!params.intermediate){
				ne('category','Intermediate')
			}
			if (!params.advanced){
				ne('category','Advanced')
			}
			if (!params.formal){
				ne('informal',false)
			}
			if (!params.informal){
				ne('informal',true)
			}
			order("sortOrder", "asc")
			maxResults(10000)
			projections {
				property("id")
			}
			//TODO: Phase 2- 5- Evaluate if this is too many or not enough (may want to add to AdminSettings table for quick adjustment)
		}
		
		if(lessonsByUser && lessonsByUser.size()>0){
			currentUser.account.filteredLessonsByAuthor = lessonsByUser?.join(",")
		}else{
			currentUser.account.filteredLessonsByAuthor = null
		}
		currentUser.account.save()

		return lessonsByUser
	}
	
	/**
	 * Get a list of lessons that need to be approved.
	 * 
	 * @param currentUser - User viewing the lesson plan
	 * @param mediaReview - Pass in True if needing to view lessons with media not approved.
	 * @return list of lesson IDs (at most 1000)
	 */
	def allLessonsNotReviewed(currentUser, mediaReview){
		def c = Lesson.createCriteria()
		def lessons = c {
			eq('active',true)
			if(mediaReview){
				eq('mediaApproved',false)
			}else{
				eq('lessonApproved',false)
			}
			ne('user',currentUser)
			order("sortOrder", "asc")
			maxResults(1000)
			projections {
				property("id")
			}
		}
		
		if(lessons && lessons.size()>0){
			currentUser.account.filteredLessonsByAuthor = lessons?.join(",")
		}else{
			currentUser.account.filteredLessonsByAuthor = null
		}
		currentUser.account.save()

		return lessons
	}
	
	/**
	 * This will return the Id of the first lesson found that needs to be translated based on a list of lessons passed in. 
	 * This is used when the user is at 0 credits and wants to unlock more lessons.
	 * 
	 * @param lessonList - string array of lessons by ID
	 * @param currentUser
	 * @return lesson id
	 */
	def lesssonNeedingTranslation(lessonList, currentUser){
		//converting array of strings to array of long
		long[] lessonListLong= new long[lessonList.length];
		 for(int n = 0; n < lessonList.length; n++) {
			lessonListLong[n] = Long.parseLong(lessonList[n]);
		  }

		def c = ViewedLesson.createCriteria()
		def lessonsNeedingTranslation = c {
			eq('user',currentUser)
			isNull("translation")
			lesson{
				'in'("id",lessonListLong)
				order("sortOrder", "asc")
			}
			maxResults(20000)
			projections {
				property("lesson.id")
			}
			
		}
		
		if(lessonsNeedingTranslation.size()>0){
			return lessonsNeedingTranslation[0]
		}else{
			return null
		}
	}
	
	/**
	 * Finds the average rating of User who created a lesson plan.
	 *  
	 * @param ratedAccountID - ID of Account
	 * @return rating is returned as Double
	 */
	def rating(ratedAccountID){
		def rateCriteria = Rate.createCriteria()
		def rating = rateCriteria.get{
			projections {
				avg("rating")
			}
			eq("ratedAccountID", ratedAccountID)
		}
		return rating
	}
	
	//TODO: 47- build filter params for model in service instead of duplicating efforts in controller
//	def filterParams(params){
//		println "-filterParams-"
//		println params
//	}
	
	/**
	 * Find the filename of the small image (used for Quiz).
	 * 
	 * @param lesson - Lesson
	 * @return image filename as String. If filename with '-small' can't be found, uses original filename.
	 */
	def getSmallImgName(lesson){
		//TODO: 50- mediaRoot is used in multiple areas. Keep code DRY.
		def mediaRoot = "web-app"
		if (Environment.current == Environment.PRODUCTION) {
			mediaRoot = "/webdata"
		}
		String imgFileExt = lesson.originalImageName?.substring(lesson.originalImageName?.lastIndexOf( "." ) + 1 )
		
		boolean smallImageExists = new File("${mediaRoot}/${lesson.imagePath}/${lesson.id}-small.${imgFileExt}").exists()
		if (smallImageExists){
			return "${lesson.id}-small.${imgFileExt}"
		}else{
			//if small image was not created, the original should be small enough.
			return "${lesson.id}.${imgFileExt}"
		}
	}
	
	/**
	 * Saves lesson to the database.
	 * 
	 * @param lesson - The new lesson or existing lesson with containing parameters to be saved.
	 * @param user - The user the lesson belongs to
	 * @param request - used to getFile for image & audio
	 * @param language - The language used for the lesson. If null, the users language will be used.
	 * @param previousLesson - Only used if an existing lesson is being edited.
	 * 
	 * @return saved lesson or new lesson object with errors
	 *
	 */
	def saveLesson(lesson, user, request, language, previousLesson){
		def uploadedImage = request.getFile('imgPayload')
		def uploadedAudio = request.getFile('audio')
				
		boolean imageNotSelectedForNewLesson = false
		if (!uploadedImage?.originalFilename && !lesson.originalImageName && !previousLesson){
			imageNotSelectedForNewLesson = true
		}

		lesson.user = user
		if(language){
			lesson.masterLang = language
		}else{
			lesson.masterLang = user.account.primaryLanguage
		}
		
		//originalImageName & originalAudioName need to be updated prior to lesson.validate for the unique constraint
		lesson.originalImageName = uploadedImage?.originalFilename ?: previousLesson?.originalImageName
		lesson.originalAudioName = uploadedAudio?.originalFilename ?: previousLesson?.originalAudioName

		lesson.validate()
		
		if(lesson.hasErrors() || imageNotSelectedForNewLesson){
			if(imageNotSelectedForNewLesson){
				lesson.errors.rejectValue('imgPayload','error.media.missing')
			}
			return lesson
		}
		
		lesson.save(flush:true)
		lesson.sortOrder = lesson.id
		
		//IMAGE HANDLER
		String newImagePath
		if(uploadedImage.originalFilename){
			newImagePath = "media/images/${user.id}"
			saveImage(uploadedImage, newImagePath, lesson, user)

		}
		
		//AUDIO HANDLER
		String newAudioPath
		if(uploadedAudio?.originalFilename){
			newAudioPath = "media/audio/${user.id}"
			saveAudio(uploadedAudio, newAudioPath, lesson, user)
		}
		
		//TODO: Phase 2- add 1GB size restriction for video 
		//TODO: Phase 2- see if length restriction can be set for audio & video
		//TODO: Phase 2- look into HTML5's capabilities to use the user's camera and microphone http://www.html5rocks.com/en/tutorials/getusermedia/intro/
				//example of audio upload and record site http://vocaroo.com/
		
		lesson.imagePath = newImagePath ?: previousLesson?.imagePath
		lesson.audioPath = newAudioPath ?: previousLesson?.audioPath
		
		if(lesson.hasErrors()){
			//Delete new lesson (if exists) and return lesson values with errors.
			lesson.originalImageName = previousLesson?.originalImageName ?: uploadedImage?.originalFilename
			lesson.originalAudioName = previousLesson?.originalAudioName ?: uploadedAudio?.originalFilename
			def lessonWithErrors = new Lesson()
			lessonWithErrors.properties = lesson.properties
			lessonWithErrors.id = previousLesson?.id
			
			if(previousLesson?.id != lesson?.id){
				//Delete lesson & media if it was a newly created one.
				deactivateOrDeleteLesson(lesson)
			}
			def savedErrors = lesson.errors.allErrors.findAll{ true }
			savedErrors.each {
				lessonWithErrors.errors.addError(it)
			}
			return lessonWithErrors
		}
		
		return lesson
	}
	
	/**
	 * Saves uploaded image to the file system, scaled down to at most 400x850.
	 * If image is larger then 290x250 an additional image with '-small' 
	 * attached to the file name will be saved.
	 * 
	 * @param uploadedImage - request.getFile('imgPayload')
	 * @param imagePath - path of where to store the image file
	 * @param lesson - Lesson
	 * @param user - User
	 * @return Lesson is returned if errors. lesson.errors.rejectValue('imgPayload'...
	 */
	def saveImage(uploadedImage, imagePath, lesson, user){
		//Tests confirm that metadata is removed, preventing geotag information from being viewed	
		
		if(uploadedImage?.size > 20000000){
			lesson.errors.rejectValue('imgPayload','error.media.large', 'File size is too large. Please reduce the size and try again.')
			//TODO: Phase 2- 1- In red, display something like 20.5 mb > 20 mb
			//TODO: Phase 2- 5- Create a settings table so an Admin can change the max size limit as well as other features.
			return lesson
		}
		
		if(uploadedImage?.size > 0 && !["image/png", "image/jpeg", "image/bmp"].contains(uploadedImage?.contentType) ){
			lesson.errors.rejectValue('imgPayload','error.media.format', 'The format you are uploading is not accepted.')
			//TODO: Phase 2- add more accepted file formats.
			//TODO: Phase 2- 1- display accepted formats on gsp
			return lesson
		}
		
		String imgFileExt = uploadedImage.originalFilename.substring(uploadedImage.originalFilename.lastIndexOf( "." ) + 1 ).toLowerCase()
		
		try{
			def mediaRoot = "web-app"
			String realImagePath = sc.getRealPath("/${imagePath}")
			if (Environment.current == Environment.PRODUCTION) {
				mediaRoot = "/webdata"
				realImagePath = "/webdata/${imagePath}"
			}
			def mediaDir = new File("${mediaRoot}/${imagePath}")
			if (!mediaDir.exists()){
				//create directory if it does not exist
				mediaDir.mkdir()
			}
	
			burningImageService.doWith(uploadedImage, "${mediaRoot}/${imagePath}").execute("${lesson.id}", {
					//saving file name as lesson id
				   it.scaleApproximate(400, 850) //resizing to max width and height if too large
				})
			
			BufferedImage img = ImageIO.read(new File("${realImagePath}/${lesson.id}.${imgFileExt}"))
			if(img.getWidth() > 290 || img.getHeight() > 250){
				//Only create a smaller image if the width or height is too large
				burningImageService.doWith(uploadedImage, "${mediaRoot}/${imagePath}")
				.execute("${lesson.id}-small", {
					//saving file name as lesson id followed by -small
					it.scaleApproximate(290, 250) //resizing to max width and height for quiz if too large
				})
			}
		}catch (Exception e){
			log.error(e)
			lesson.errors.rejectValue('imgPayload','error.unknown.media.save', 
				'An unknown error has occurred while saving the selected media. Please try choosing a different one.')
			return lesson
		}
	}
	
	/**
	 * Saves uploaded audio to the file system.
	 * 
	 * @param uploadedAudio - request.getFile('audio')
	 * @param audioPath - path of where to store the audio file
	 * @param lesson - Lesson
	 * @param user - User
	 * @return Lesson is returned. If errors, lesson.errors.rejectValue('originalAudioName'...
	 */
	def saveAudio(uploadedAudio, audioPath, lesson, user){
//			println "Class: ${uploadedAudio.class}"
//		  println "Name: ${uploadedAudio.name}"
//		  println "OriginalFileName: ${uploadedAudio.originalFilename}"
//		  println "Size: ${uploadedAudio.size}"
//		  println "ContentType: ${uploadedAudio.contentType}"
			
		  if(uploadedAudio?.size > 100000000){
			  lesson.errors.rejectValue('originalAudioName','error.media.large')
			  //TODO: Phase 2- 1- In red, display something like 256.5 mb > 100 mb
			  //TODO: Phase 2- 5- Create a settings table so an Admin can change the max size limit as well as other features.
			  return lesson
		  }
		  
/*		  if(uploadedAudio?.size > 0 && uploadedAudio?.contentType != "audio/mpeg"){
			  lesson.errors.rejectValue('originalAudioName','error.media.format', 'The format you are uploading is not accepted.')
			  //TODO: Phase 2- find all supported audio formats and test each
			 	//supported audio formats found so far: 3gp, aiff, amr,	au,	flac, m4a, mmf, mp2, mp3, mp4, ogg, rm, wav
			 	//already tested the following: m4a
			  return lesson
		  }*/
		File source = null
		try{
			def mediaRoot = "web-app"
			//String realImagePath = sc.getRealPath("/${imagePath}")
			String realAudioPath = sc.getRealPath("/media/audio")
			if (Environment.current == Environment.PRODUCTION) {
				mediaRoot = "/webdata"
				realAudioPath = "/webdata/media/audio"
			}
			
			def audioDir = new File("${mediaRoot}/${audioPath}")
			if (!audioDir.exists()){
				//create directory if it does not exist
				audioDir.mkdir()
			}

			source = new File( realAudioPath, uploadedAudio.originalFilename)
			uploadedAudio.transferTo( source)
			 File target = new File( realAudioPath+"/${user.id}", "${lesson.id}.mp3")
			 AudioAttributes audio = new AudioAttributes();
			 audio.setCodec("libmp3lame");
			 audio.setBitRate(new Integer(128000));
			 audio.setChannels(new Integer(2));
			 audio.setSamplingRate(new Integer(44100));
			 EncodingAttributes attrs = new EncodingAttributes();
			 attrs.setFormat("mp3");
			 attrs.setAudioAttributes(audio);
			 Encoder encoder = new Encoder();
//					 println encoder.getSupportedEncodingFormats()


			 encoder.encode(source, target, attrs);
			
			 if (source.exists()){
				 //deleting the original audio. Keeping the encoded one.
				 source.delete()
			 }
		}catch (it.sauronsoftware.jave.InputFormatException ife){
//				println ife
			//TODO: Phase 2- 1- display all available audio formats when error occurs
			lesson.errors.rejectValue('originalAudioName','error.media.format', 'The format you are uploading is not accepted.')
			if (source?.exists()){
				source.delete() //deleting the original uploaded file (before encoding/rename was attempted).
			}
			return lesson
			
		}catch (Exception e){
//				println e
			lesson.errors.rejectValue('originalAudioName','error.unknown.media.save', 
					'An unknown error has occurred while saving the selected media. Please try choosing a different one.')
			log.error(e)
			if (source?.exists()){
				source.delete() //deleting the original uploaded file (before encoding/rename was attempted).
			}
			return lesson
		}
	}
	
	def saveVideo(uploadedVideo){
		//setup ffmpeg and/or JAVE to do video format conversion.
		/*A smartphone suitable video:
		
		File source = new File("source.avi");
		File target = new File("target.3gp");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libfaac");
		audio.setBitRate(new Integer(128000));
		audio.setSamplingRate(new Integer(44100));
		audio.setChannels(new Integer(2));
		VideoAttributes video = new VideoAttributes();
		video.setCodec("mpeg4");
		video.setBitRate(new Integer(160000));
		video.setFrameRate(new Integer(15));
		video.setSize(new VideoSize(176, 144));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("3gp");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		encoder.encode(source, target, attrs);*/
		
		
		//TODO: Phase 2- add 2 credits for video
		//Flash video conversions for grails - http://cantina.co/library/video-plugin/
	}
	
	/**
	 * If lesson is edited and was already viewed it gets a new lesson ID. Move media to match the new lesson ID.
	 * 
	 * @param oldLesson - Lesson object of old lesson the media is moving from.
	 * @param newLesson - Lesson object of new lesson the media is moving to.
	 * @param uploadedImage - request.getFile('imgPayload')
	 * @param uploadedAudio - request.getFile('audio')
	 * @return
	 */
	def moveMediaToNewLesson(oldLesson, newLesson, uploadedImage, uploadedAudio){
		//ServletContext sc = SCH.getServletContext()
		if(!uploadedImage?.originalFilename && oldLesson?.originalImageName){
		   //If old image exists and has not been edited, make sure it matches new lesson ID.
			String imgFileExt = oldLesson.originalImageName.substring(oldLesson.originalImageName.lastIndexOf( "." ) + 1 )
		   String imagePath = sc.getRealPath("/${newLesson.imagePath}")
		   File file = new File( imagePath, "${oldLesson.id}.${imgFileExt}")
		   boolean fileMoved = file.renameTo(new File(imagePath, "${newLesson.id}.${imgFileExt}"))
		   if (fileMoved){
			   File smallFile = new File(imagePath, "${oldLesson.id}-small.${imgFileExt}")
			   smallFile.renameTo(new File(imagePath, "${newLesson.id}-small.${imgFileExt}"))
		   }
	   }
		if(!uploadedAudio?.originalFilename && oldLesson?.originalAudioName){
			//If old audio exists and has not been edited,, make sure it matches new lesson ID.
			String audioPath = sc.getRealPath("/${newLesson.audioPath}")
			File file = new File( audioPath, "${oldLesson.id}.mp3")
			file.renameTo(new File(audioPath, "${newLesson.id}.mp3"))
		}

	}
	
	/**
	 * Deactivates lesson if is has been viewed, otherwise deletes lesson.
	 * 
	 * @param lessonInstance - lesson to be deleted
	 * @return true if deleted or set inactive, false if error deleting
	 */
	def deactivateOrDeleteLesson(lessonInstance){
		//Issues deleting (open in Java(TM) Platform SE binary) were resolved using the following:
		// http://stackoverflow.com/questions/16237147/grails-deleting-uploaded-images
		//def translatedLessons = ViewedLesson.executeQuery('select l.id from ViewedLesson l where l.lesson =? and l.translation is not null',[lessonInstance])
		def accountToCredit = accountsOfViewedLesson(lessonInstance)
		
		//TODO: 39- performance test if 1000+ users viewed the lesson. May need a better update method.
		if(accountToCredit){
			//If lesson has been viewed, mark lesson as inactive instead of deleting.
			lessonInstance.active = false
			lessonInstance.save()
			accountToCredit.each{
				userService.addCredits(it, 1)
			}
			return true
		}
		
		if (lessonInstance) {
			try {
				//TODO: 31- test behavior of deleting right after an admin deleted.
				lessonInstance.delete(flush: true)
				deleteImgFromUsersLesson(lessonInstance)
				deleteAudioFromUsersLesson(lessonInstance)
				return true
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				log.error(e)
				return false
			}
		}else {
			//Lesson must have been deleted before the user had a chance to delete.
			return true
		}
	}
	
	/**
	 * Delete image and small image (if any) associated to a user's lesson.
	 * 
	 * @param lessonInstance - Lesson
	 * @return
	 */
	def deleteImgFromUsersLesson(lessonInstance){
		def mediaRoot = "web-app"
		if (Environment.current == Environment.PRODUCTION) {
			mediaRoot = "/webdata"
		}
		String imgFileExt = lessonInstance.originalImageName?.substring(lessonInstance.originalImageName?.lastIndexOf( "." ) + 1 )
		def image = new File("${mediaRoot}/${lessonInstance.imagePath}/${lessonInstance.id}.${imgFileExt}")
		if (image.exists()){
			boolean imageDeleted = image.delete()
			if (!imageDeleted){
				log.error("Error occurred deleting image: ${mediaRoot}/${lessonInstance.imagePath}/${lessonInstance.id}.${imgFileExt}")
			}
		}
		
		def smallImage = new File("${mediaRoot}/${lessonInstance.imagePath}/${lessonInstance.id}-small.${imgFileExt}")
		if (smallImage.exists()){
			smallImage.delete()
		}
		
	}
	
	/**
	 * Delete audio from file system associated to a Lesson.
	 * 
	 * @param lessonInstance - Lesson
	 * @return
	 */
	def deleteAudioFromUsersLesson(lessonInstance){
		def mediaRoot = "web-app"
		if (Environment.current == Environment.PRODUCTION) {
			mediaRoot = "/webdata"
		}
		def audio = new File("${mediaRoot}/${lessonInstance.audioPath}/${lessonInstance.id}.mp3")
		if (audio.exists()){
			audio.delete()
		}
	}
	
	/**
	 * Total credits the user should (or should have) earned for creating the lesson.
	 * 
	 * @param lesson
	 * @param user
	 * @return Total number of credits as an Integer.
	 */
	def totalCreditsForLesson(lesson, user){
		Integer totalCredits = 0
		if(lesson.imagePath){
			totalCredits += imageCredits
		}
		if(lesson.audioPath){
			totalCredits += audioCredits
		}
		if(lesson.videoPath){
			totalCredits += videoCredits
		}
		return totalCredits
	}
	
	/**
	 * This will find a list of lessons created by the user that are in the same category.
	 * they will be used to show 3 other random images from other lessons.
	 * 
	 * @param lessonId
	 * @param currentUser
	 * @param lesson
	 * @return array of random lessons created by the user that are in the same category as the quiz
	 */
	def randomLessonSample(lessonId, currentUser, lesson){
		//TODO: 42- pass in one lesson instead of lesson array.
		def randomLessonList = []
		
		def shuffledLessonList = Lesson.executeQuery('select id from Lesson where user_id =? and category =?',[currentUser.id, lesson[0].category])
		Collections.shuffle(shuffledLessonList)
		shuffledLessonList.each{
			if(lessonId != it.toString()){
				//make sure the lesson image being viewed does not show up twice.
				randomLessonList << it
			}
		}
		return randomLessonList
	}
	
	/**
	 * Pulls all accounts of everyone who viewed the lesson.
	 * 
	 * @param lesson - Lesson object
	 * @return Array of Accounts
	 */
	def accountsOfViewedLesson(lesson){
		def accountsOfViewedLesson = ViewedLesson.executeQuery('select l.user.account from ViewedLesson l where l.lesson =?',[lesson])
		if(accountsOfViewedLesson.size()==0){
			return false
		}
		return accountsOfViewedLesson
	}
	
}
