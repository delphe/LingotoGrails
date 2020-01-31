package com.lingoto
import java.util.Locale;

class Account {
	
	static belongsTo = [user: User]
	MasterLang primaryLanguage
	Rate rate
	
	String firstName
	String lastName
	Integer credits
	Integer age
	boolean adultContent
	Locale systemLocale
	String filteredLessonsByAuthor
	String shuffledLessons
	Date dateCreated
	
	
	static constraints = {
		//Make sure constraints also match what's in UserRegistrationCommand in the UserController
		firstName(blank:false, maxSize:100)
		lastName(blank:false, maxSize:100)
		age(nullable:false)
		credits(nullable:true)
		primaryLanguage(nullable:true)
		adultContent(nullable:false)
		rate(nullable:true)
		systemLocale(nullable:true, maxSize:20)
		filteredLessonsByAuthor(nullable:true)
		shuffledLessons(nullable:true)
	}
	
	String toString () {
		"$lastName, $firstName"
	}
	
	static mapping = {
		user lazy: false
		primaryLanguage lazy: false
		//use TEXT instead of VARCHAR to allow more characters.
		filteredLessonsByAuthor type: 'text'
		shuffledLessons type: 'text'
	}
	
	void setFirstName(String firstName) {
		this.firstName = firstName?.trim()
	}
	
	void setLastName(String lastName) {
		this.lastName = lastName?.trim()
	}
	
}
