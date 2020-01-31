package com.lingoto

//JAVE - http://www.sauronsoftware.it/projects/jave/manual.php?PHPSESSID=s3hcnm5p19ebbsejpolgutit91
import it.sauronsoftware.jave.AudioAttributes
import it.sauronsoftware.jave.Encoder
import it.sauronsoftware.jave.EncodingAttributes
import javax.imageio.ImageIO
import javax.servlet.ServletContext
import java.awt.image.BufferedImage
import org.springframework.web.servlet.support.RequestContextUtils
import org.springframework.web.context.request.RequestContextHolder
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.hibernate.criterion.CriteriaSpecification

class LessonService {
	
	def springSecurityService
	def burningImageService
	
	Integer imageCredits = 1
	Integer videoCredits = 2
	Integer audioCredits = 3
	
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
	
	def filteredAuthorsWithLessons(params){
		def user = springSecurityService.currentUser
		int age = user.account.age
		if(user.account.age == 18 && user.account.adultContent == false){
			//Some users are 18 or older but do not want to see adult content.
			//setting age content of lessons to 17
			age = 17
		}
		
		def c = Lesson.createCriteria()
		def authorsWithLessonsByLang = c.list() {
			
			resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
			eq('active',true)
			le('ageRestriction',age)
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
	 * @param authorsWithLessonsByLang - obtained by using filteredAuthorsWithLessons
	 * @return array of arrays containing [firstname lastname, userId, rating]
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
	
	def filteredLessonsByUser(params, user, currentUser){
		int age = currentUser.account.age
		if(currentUser.account.age == 18 && currentUser.account.adultContent == false){
			//Some users are 18 or older but may not want to see adult content.
			//setting age content of lessons to 17
			age = 17
		}
		
		def c = Lesson.createCriteria()
		def lessonsByUser = c {
			eq('active',true)
			le('ageRestriction',age)
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
			maxResults(20000)
		}
		
		if(lessonsByUser && lessonsByUser.size()>0){
			currentUser.account.filteredLessonsByAuthor = lessonsByUser?.id?.join(",")
		}else{
			currentUser.account.filteredLessonsByAuthor = null
		}
		currentUser.account.save()

		//TODO: 46- optimize this query. Only need the ID, nothing else.

		return lessonsByUser?.id
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
//	def storeFilterInSession(params){
//		println "-storeFilterInSession-"
//		println params
//		def session = RequestContextHolder.currentRequestAttributes().getSession()
//		session.beginner = params.beginner
//		session.intermediate = params.intermediate
//		session.advanced = params.advanced
//		session.formal = params.formal
//		session.informal = params.informal
//	}
	
	def getImgName(lesson){
		boolean smallImageExists = new File("web-app/${lesson.imagePath}/${lesson.id}-small.${lesson.imageExt}").exists()
		if (smallImageExists){
			return "${lesson.id}-small.${lesson.imageExt}"
		}else{
			//if small image was not created, the original should be small enough.
			return "${lesson.id}.${lesson.imageExt}"
		}
	}
	
	/**
	 * Saves lesson to the database.
	 * 
	 * @param lesson - The new lesson or existing lesson with containing parameters to be saved.
	 * @param user - The user the lesson belongs to
	 * @param request - used to getFile for image & audio
	 * @param language - The language used for the lesson. If null, the users language will be used.
	 * @param currentLesson - Only used if an existing lesson is being edited.
	 * 
	 * @return saved lesson
	 *
	 */
	def saveLesson(lesson, user, request, language, currentLesson){
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		def uploadedImage = request.getFile('imgPayload')
		def uploadedAudio = request.getFile('audio')
				
		boolean imageNotSelected = false
		if (!uploadedImage?.originalFilename && !lesson.originalImageName && !currentLesson){
			imageNotSelected = true
		}
		
//			println "Class: ${uploadedAudio.class}"
//		  println "Name: ${uploadedAudio.name}"
//		  println "OriginalFileName: ${uploadedAudio.originalFilename}"
//		  println "Size: ${uploadedAudio.size}"
//		  println "ContentType: ${uploadedAudio.contentType}"
			
		  if(uploadedAudio?.size > 100000000){
			  lesson.errors.rejectValue('originalAudioName','error.media.large')
			  //TODO: In red, display something like 256.5 mb > 100 mb
			  //TODO: Create a settings table so an Admin can change the max size limit as well as other features.
			  return lesson
		  }
		  
/*		  if(uploadedAudio?.size > 0 && uploadedAudio?.contentType != "audio/mpeg"){
			  lesson.errors.rejectValue('originalAudioName','error.media.format',
				  ['originalAudioName', 'class Lesson', uploadedAudio?.contentType, 'mp3'] as Object[],
				  'The format you are uploading is not accepted.')
			  //TODO: Phase 2- add more accepted file formats.
			  return lesson
		  }*/
		  
		  //TODO: 22- test error message for invalid image size/type
		  
			lesson.user = user
			if(language){
				lesson.masterLang = language
			}else{
				lesson.masterLang = user.account.primaryLanguage
			}
			
			lesson.originalImageName = uploadedImage?.originalFilename
			lesson.originalAudioName = uploadedAudio?.originalFilename

			lesson.validate()
			
			if(lesson.hasErrors() || imageNotSelected){
				//TODO: 51- move to new method
				//removing duplicate error messages
				def savedErrors = lesson.errors.allErrors.findAll{ true }
				savedErrors = savedErrors?.unique(new ErrorComparator())
				lesson.clearErrors()
				savedErrors.each {
					lesson.errors.addError(it)
				}
				
				if(imageNotSelected){
					lesson.errors.rejectValue('imgPayload','error.media.missing')
				}
				return lesson
			}
			
			lesson.save(flush:true)
			lesson.sortOrder = lesson.id
			
			ServletContext sc = SCH.getServletContext()
			
			/* This code doesn't make sense. I don't know what I was thinking.
			 * If the lesson is being edit, the ID of the lesson should not change and the old image can still be used.
			 * //TODO: 1- editing an existing lesson is broken. Fix it.
			 if(!uploadedImage?.originalFilename && currentLesson){
				//Lesson is being edited and a new image has not been uploaded. Rename image to match new lesson ID.
				lesson.originalImageName = currentLesson.originalImageName
				lesson.imagePath = currentLesson.imagePath
				lesson.imageExt = currentLesson.imageExt
				
				lesson.imageName = "${lesson.id}.${currentLesson.imageExt}".toLowerCase()
				
				String imagePath = sc.getRealPath("/${lesson.imagePath}")
				File file = new File( imagePath, "${currentLesson.imageName}")
				boolean fileMoved = file.renameTo(new File(imagePath, lesson.imageName))
				if (!fileMoved){
					//file did not rename successfully, keep original name.
					lesson.imageName = currentLesson.imageName
				}else{
					File smallFile = new File(imagePath, "${currentLesson.id}-small.${currentLesson.imageExt}")
					smallFile.renameTo(new File(imagePath, "${lesson.id}-small.${currentLesson.imageExt}"))
				}
			}*/
			
			if(uploadedImage.originalFilename){
				//Tests confirm that metadata is removed, preventing geotag information from being viewed
				def imageFile = uploadedImage.originalFilename.split("\\.")
				
//			println imageFile[0] //file name without extension
//			println imageFile[1] //File extension
				lesson.imagePath = "media/images/${user.id}"
				lesson.imageName = "${lesson.id}.${imageFile[1]}".toLowerCase()
				//TODO: only save image name to DB, not image ext.
				//String imgFileExt = uploadedImage.originalFilename.substring(uploadedImage.originalFilename.lastIndexOf( "." ) + 1 ) //test a file with no ext
				lesson.imageExt = "${imageFile[1]}".toLowerCase()
				
				try{
					
					def mediaDir = new File("web-app/${lesson.imagePath}")
					if (!mediaDir.exists()){
						//create directory if it does not exist
						mediaDir.mkdir()
					}

					burningImageService.doWith(uploadedImage, "web-app/${lesson.imagePath}").execute("${lesson.id}", {
							//saving file name as lesson id
						   it.scaleApproximate(400, 850) //resizing to max width and height if too large
						})
					
					String imagePath = sc.getRealPath("/${lesson.imagePath}")
					BufferedImage img = ImageIO.read(new File("${imagePath}/${lesson.id}.${lesson.imageExt}"))
					if(img.getWidth() > 290 || img.getHeight() > 250){
						//Only create a smaller image if the width or height is too large
						burningImageService.doWith(uploadedImage, "web-app/${lesson.imagePath}")
						.execute("${lesson.id}-small", {
							//saving file name as lesson id followed by -small
							it.scaleApproximate(290, 250) //resizing to max width and height for quiz if too large
						})
					}
					user.account.credits = user.account.credits +imageCredits
					user.account.save()
					session.credits = user.account.credits
				}catch (Exception e){
//						println e
					lesson.errors.rejectValue('imgPayload','error.unknown.image.save')
					log.error(e)
					deleteImgFromUsersLesson(lesson, user)
					lesson?.delete()
					return lesson
				}
			}
			
			File source = null
			try{
				if(!uploadedAudio?.originalFilename && currentLesson){
					//Lesson is being edited and a new audio has not been uploaded. Rename audio to match new lesson ID.
					lesson.audioPath = currentLesson.audioPath
					lesson.originalAudioName = currentLesson.originalAudioName
					
					String audioPath = sc.getRealPath("/${lesson.audioPath}")
					File file = new File( audioPath, "${currentLesson.id}.mp3")
					file.renameTo(new File(audioPath, "${lesson.id}.mp3"))
				}
				
				if(uploadedAudio?.originalFilename){
					user.account.credits = user.account.credits +audioCredits					
					lesson.audioPath = "media/audio/${user.id}"
					def audioDir = new File("web-app/${lesson.audioPath}")
					if (!audioDir.exists()){
						//create directory if it does not exist
						audioDir.mkdir()
					}
					String audioPath = sc.getRealPath("/media/audio")

					source = new File( audioPath, uploadedAudio.originalFilename)
					uploadedAudio.transferTo( source)
					 File target = new File( audioPath+"/${user.id}", "${lesson.id}.mp3")
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
					 //TODO: find all supported audio formats and test each
					 /* supported audio formats found so far:
					3gp
					aiff
					amr
					au
					flac
					m4a
					mmf
					mp2
					mp3
					mp4
					ogg
					rm
					wav
					*/
					 encoder.encode(source, target, attrs);
					
					 if (source.exists()){
						 //deleting the original audio. Keeping the encoded one.
						 source.delete()
					 }
				}
			}catch (it.sauronsoftware.jave.InputFormatException ife){
//				println ife
				//TODO: display all available audio formats when error occurs
				lesson.errors.rejectValue('originalAudioName','error.media.format')
				deleteAudioFromUsersLesson(lesson, user, source)
				lesson?.delete()
				return lesson
				
			}catch (Exception e){
//				println e
				lesson.errors.rejectValue('originalAudioName','error.unknown')
				log.error(e)
				deleteAudioFromUsersLesson(lesson, user, source)
				lesson?.delete()
				return lesson
			}
			
			//TODO: Phase 2- add 1GB size restriction for video 
			//TODO: Phase 2- see if length restriction can be set for audio & video
			
			//example of audio upload and record site http://vocaroo.com/
			
//				def ant = new AntBuilder()   // create an antbuilder
//				ant.exec(outputproperty:"cmdOut",
//							 errorproperty: "cmdErr",
//							 resultproperty:"cmdExit",
//							 failonerror: "true",
//							 executable: '/opt/myExecutable') {
//											 arg(line:"""*"first with space"* second""")
//             }
//				println "return code:  ${ant.project.properties.cmdExit}"
//				println "stderr:         ${ant.project.properties.cmdErr}"
//				println "stdout:        ${ ant.project.properties.cmdOut}"
			
			//TODO: Phase 2- look into HTML5's capabilities to use the user's camera and microphone http://www.html5rocks.com/en/tutorials/getusermedia/intro/
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

			return lesson
	}
	
	/**
	 * Deactivates lesson if is has been translated, otherwise deletes lesson.
	 * 
	 * @param lessonInstance - lesson to be deleted
	 * @param currentUser - user attempting the deletion
	 * 
	 * @return true if deleted or set inactive, false if error deleting
	 */
	def deactivateOrDeleteLesson(lessonInstance, currentUser){
		//Issues deleting (open in Java(TM) Platform SE binary) were resolved using the following:
		// http://stackoverflow.com/questions/16237147/grails-deleting-uploaded-images
		
		def translatedLessons = ViewedLesson.executeQuery('select l.id from ViewedLesson l where l.lesson =? and l.translation is not null',[lessonInstance])

		if(translatedLessons.size() > 0){
			//If lesson has been translated, mark lesson as inactive instead of deleting.
			lessonInstance.active = false
			
			//TODO: 31- test behavior of deleting right after an admin deleted.
			lessonInstance.save()
			return true
		}
		
		if (lessonInstance) {
			try {
				lessonInstance.delete(flush: true)
				deleteImgFromUsersLesson(lessonInstance, currentUser)
				deleteAudioFromUsersLesson(lessonInstance, currentUser, null)
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
	
	def deleteImgFromUsersLesson(lessonInstance, currentUser){
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		
		def image = new File("web-app/${lessonInstance.imagePath}/${lessonInstance.imageName}")
		if (image.exists()){
			boolean imageDeleted = image.delete()
			if (!imageDeleted){
				log.error("Error occurred deleting image: web-app/${lessonInstance.imagePath}/${lessonInstance.imageName}")
			}
			currentUser.account.credits = currentUser.account.credits -imageCredits
			currentUser.account.save()
			session.credits = currentUser.account.credits
		}
		def smallImage = new File("web-app/${lessonInstance.imagePath}/${lessonInstance.id}-small.${lessonInstance.imageExt}")
		if (smallImage.exists()){
			smallImage.delete()
		}
		
	}
	
	def deleteAudioFromUsersLesson(lessonInstance, currentUser, source){
		if (source?.exists()){
			source.delete() //deleting the original uploaded file (before encoding/rename was attempted).
		}
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		def audio = new File("web-app/${lessonInstance.audioPath}/${lessonInstance.id}.mp3")
		if (audio.exists()){
			audio.delete()
			currentUser.account.credits = currentUser.account.credits -audioCredits
			currentUser.account.save()
			session.credits = currentUser.account.credits
		}
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
	
}
