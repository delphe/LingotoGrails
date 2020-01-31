package com.lingoto

class ViewedLesson {
	User user
	Lesson lesson
	
	String translation
	String audioPath
	String audioName
	boolean passedQuiz
	Date dateCreated

    static constraints = {
		user(nullable:false)
		lesson(nullable:false)
		translation(nullable:true, maxSize:190)
		audioPath(nullable:true, maxSize:190)
		audioName(nullable:true, maxSize:190)
		passedQuiz(nullable:true)
    }
	
	void setTranslation(String translation) {
		this.translation = translation?.trim()
	}

}
