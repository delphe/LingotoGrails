package com.lingoto

class Rate {

	Integer rating
	Long ratedAccountID
	Long ratedByAccountID
	
	static constraints = {
		rating(nullable:false)
		ratedAccountID(nullable:false)
		ratedByAccountID(nullable:false)
	}
	
}
