package com.lingoto

class MasterLang {
	
	static hasMany = [lessons: Lesson]
	
	String iso2
	String iso3
	String languageRef
	String lingo //changed all instances of params.language and params.lang to lingo to prevent issues with system language being changed.
	String dialect //= "" //using null values in MySQL does not allow unique constraint to work properly
	//postgresql will not save the dialect as a blank value, it saves as null if not defined.
	String localeExt
	
    static constraints = {
		iso2(nullable:true, maxSize:2)
		iso3(nullable:true, maxSize:3)
		languageRef(nullable:true, maxSize:100)
		lingo(blank:false, nullable:false, unique:'dialect', maxSize:100)
		dialect(nullable:true, maxSize:100)
		localeExt(nullable:true, maxSize:20)
    }
}
