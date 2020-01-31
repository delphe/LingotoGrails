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
	Integer ageRestriction = 12
	boolean lessonApproved
	boolean mediaApproved
	//if a lesson is edited and media overwritten, the mediaApproved flag will need to be cleared out.
	boolean needsReview
	String originalImageName
	String imagePath
	String originalAudioName
	String audioPath
	String originalVideoName
	String videoPath
	Integer sortOrder
	boolean active = true
	Date dateCreated

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
		originalAudioName(nullable:true)
		audioPath(nullable:true)
		originalVideoName(nullable:true)
		videoPath(nullable:true)
		sortOrder(nullable:true)
		active(nullable:true)
    }
	
	static mapping = {
		sort "sortOrder"
		id generator:'sequence', params:[sequence:'seq_lesson_id']
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
