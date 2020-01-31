package com.lingoto

import org.springframework.web.context.request.RequestContextHolder

class UserService {
	
	/**
	 * Add credits to Account.
	 *
	 * @param account - Account to add credits to.
	 * @param amount - amount to add as Integer
	 * @return
	 */
	def addCredits(account, amount){
		account.credits = account.credits + amount
		saveCreditInfo(account)
	}
	
	/**
	 * Remove credits from Account.
	 * 
	 * @param account - Account to remove credits from.
	 * @param amount - amount to remove as Integer
	 * @return
	 */
	def removeCredits(account, amount){
		account.credits = account.credits - amount
		saveCreditInfo(account)
	}
	
	/**
	 * Saves new credit amount to Account.
	 * 
	 * @param account - Account with account.credits added or removed
	 * @return
	 */
	def saveCreditInfo(account){
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		account.save()
		session.credits = account.credits
	}
	
	/**
	 * Find the age of a User. Some users are 18 or older but may not want to see adult content.
	 * Set age to 17 if adultContent == false.
	 * 
	 * @param account - Account of User - If null, default to age 17
	 * @return age as an Integer
	 */
	def getAgeOfUser(account){
		if(!account){
			return 17
		}
		int age = account.age
		if(account.age == 18 && account.adultContent == false){
			age = 17
		}
		return age
	}
	
}
