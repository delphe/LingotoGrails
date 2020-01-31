package com.lingoto
import pl.burningice.plugins.image.ast.FileImageContainer

@FileImageContainer(field = 'imgPayload')
class Lesson {
	User user
	MasterLang masterLang

	String wordPhrase
	String category
	String additionalInfo
	boolean informal
	Integer ageRestriction
	boolean lessonApproved
	//if the lesson is approved it will be copied to the master user's lesson plan. Admins can search for lesson's that have not yet been approved to see what can be used for the master lesson plans.
	boolean mediaApproved
	//if a lesson is edited and media overwritten, the mediaApproved flag will be cleared out.
	boolean needsReview
	//needs review will be used when the lesson is translated by a student. An admin will review the media, to see if it is okay to use and delete it if it's no good. 
	//Deleting the media will remove the lesson from the translator's lesson plan. If media is in a shared path and mediaApproved flag is set to true, the lesson will show up in the translator's lesson plan.
	String originalImageName
	String imagePath
	String imageExt
	String imageName
	String originalAudioName
	String audioPath
	String audioName
	String videoName
	Integer sortOrder
	boolean active
	//if a student translates a lesson, the media will be reviewed and copied to a shared media path and flagged as "needsReview".

    static constraints = {
		user(nullable:false, maxSize:50)
		masterLang(nullable:false)
		wordPhrase(nullable:false, blank:false, maxSize:100, 
			unique:['user','masterLang','category','additionalInfo','informal','originalImageName','originalAudioName','active'])
		category(nullable:false, blank:false, maxSize:50)
		additionalInfo(nullable:true, maxSize:190)
		informal(nullable:false)
		ageRestriction(nullable:true)
		lessonApproved(nullable:true)
		mediaApproved(nullable:true)
		needsReview(nullable:true)
		originalImageName(nullable:true)
		imagePath(nullable:true)
		imageExt(nullable:true, maxSize:5)
		imageName(nullable:true)
		audioPath(nullable:true)
		originalAudioName(nullable:true)
		audioName(nullable:true)
		videoName(nullable:true)
		sortOrder(nullable:true)
		active(nullable:true)
    }
	
	static mapping = {
		sort "sortOrder"
	}
	
	void setWordPhrase(String wordPhrase) {
		this.wordPhrase = wordPhrase?.trim()
	}
	
	void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo?.trim() ?: ""
	}
	
	//turn nulls into empty strings for unique constraint to work.
	void setOriginalAudioName(String originalAudioName) {
		this.originalAudioName = originalAudioName ?: ""
	}
	
	void setOriginalImageName(String originalImageName) {
		this.originalImageName = originalImageName ?: ""
	}

}
