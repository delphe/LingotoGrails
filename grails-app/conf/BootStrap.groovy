import com.lingoto.*
import grails.util.Environment

class BootStrap {

	def init = { servletContext ->
		
		switch (Environment.current) {
			case Environment.DEVELOPMENT:
				loadDataIfRequired()
				break
			case Environment.PRODUCTION:
//				println "No BootStrap configuration required."
				break
		}
	}
	
	def destroy = {
	}

	void loadDataIfRequired() {
		if (User.findByUsername("delphedwin@gmail.com")) {
//			println "Existing admin user, skipping creation"
		} else {
			//TODO: Phase 2- create super user. This user will be allowed to view all lessons and set age restrictions on them.
//			println "Creating ADMIN user."
			def adminRole = new Role(authority: 'ROLE_ADMIN').save()
			def language = MasterLang.findByLingoIlike("English")
			if (!language){
				language = new MasterLang(
					iso2:'en', iso3:'eng', languageRef:'English', localeExt:'US',
					lingo: 'English', dialect: 'United States').save()
			}
			new MasterLang(iso2:'hu', iso3:'hun', languageRef:'Hungarian',	lingo: 'Magyar', dialect: '').save()
			
			//def enUSlocale = new Locale("en")
			
			def adminUser = new User(username: "delphedwin@gmail.com", enabled: true,
				password: "secret").save(flush: true, failOnError:true)
			def account = new Account(firstName: "Edwin", user:adminUser,
				lastName: "Delph", primaryLanguage: language, age:18, adultContent:true, credits:20000).save()
			UserRole.create adminUser, adminRole, true
			
			assert Role.count() == 1
			assert UserRole.count() == 1
			
			//***Creating Messages***
//			println "Creating Messages"
			//Labels
			new Message(code:'teach.label', locale: 'en', text: 'Teach').save()
			new Message(code:'learn.label', locale: 'en', text: 'Learn').save()
			new Message(code:'quiz.label', locale: 'en', text: 'Quiz').save()
			new Message(code:'password.label', locale: 'en', text: 'Password').save()
			new Message(code:'login.label', locale: 'en', text: 'Login').save()
			new Message(code:'createAccount.label', locale: 'en', text: 'Create Account').save()
			new Message(code:'logout.label', locale: 'en', text: 'Logout').save()
			new Message(code:'hello.label', locale: 'en', text: 'Hello').save()
			new Message(code:'credits.label', locale: 'en', text: 'credits').save()
			new Message(code:'createLanguageLesson.label', locale: 'en', text: 'Create Language Lesson').save()
			new Message(code:'wordPhrase.label', locale: 'en', text: 'Word/Phrase').save()
			new Message(code:'level.label', locale: 'en', text: 'Level').save()
			new Message(code:'category.beginner.label', locale: 'en', text: 'Beginner').save()
			new Message(code:'category.intermediate.label', locale: 'en', text: 'Intermediate').save()
			new Message(code:'category.advanced.label', locale: 'en', text: 'Advanced').save()
			new Message(code:'formal.label', locale: 'en', text: 'Formal').save()
			new Message(code:'informal.label', locale: 'en', text: 'Informal/Slang').save()
			new Message(code:'additionalInfo.label', locale: 'en', text: 'Additional Information').save()
			new Message(code:'optional.label', locale: 'en', text: 'optional').save()
			new Message(code:'langLessonsByAuthor.label', locale: 'en', text: 'Language Lessons by Author').save()
			new Message(code:'translate.label', locale: 'en', text: 'Translate').save()
			new Message(code:'lessonCreatedBy.label', locale: 'en', text: 'Lesson created by').save()
			new Message(code:'lessonList.label', locale: 'en', text: 'Lesson List').save()
			new Message(code:'language.label', locale: 'en', text: 'Language').save()
			new Message(code:'dialect.label', locale: 'en', text: 'Dialect').save()
			new Message(code:'editAccount.label', locale: 'en', text: 'Edit Account').save()
			new Message(code:'firstName.label', locale: 'en', text: 'First Name').save()
			new Message(code:'lastName.label', locale: 'en', text: 'Last Name').save()
			new Message(code:'email.label', locale: 'en', text: 'Email').save()
			new Message(code:'age.label', locale: 'en', text: 'Age').save()
			new Message(code:'currentPassword.label', locale: 'en', text: 'Current Password').save()
			new Message(code:'updatePassword.label', locale: 'en', text: 'Update Password').save()
			new Message(code:'newPassword.label', locale: 'en', text: 'New Password').save()
			new Message(code:'retypePassword.label', locale: 'en', text: 'Retype Password').save()
			new Message(code:'contactUs.label', locale: 'en', text: 'Contact Us').save()
			new Message(code:'forbidden.label', locale: 'en', text: 'Forbidden').save()
			
			//Messages
			new Message(code:'earnCredits.message', locale: 'en', text: 'Earn Credits!').save()
			new Message(code:'translateThisSite.message', locale: 'en', text: 'Translate This Site').save()
			new Message(code:'thanksForTranslating.message', locale: 'en', text: 'Thank you for your translations!').save()
			new Message(code:'unlockLessons.message', locale: 'en', text: 'Unlock more lessons!').save()
			new Message(code:'purchaseCredits.message', locale: 'en', text: 'Purchase credits').save()
			new Message(code:'adminsKnowTheseLangs.message', locale: 'en', text: 'Lingoto administrators know the following languages:').save()
			new Message(code:'weWillDoOurBest.message', locale: 'en', text: 'We will do our best to understand and respond if you are using a different language.').save()
			new Message(code:'language.change.warning.message', locale: 'en', text: 'Warning: You have made changes to your language.').save()
			new Message(code:'language.change.all.message', locale: 'en', text: 'Change the language on all previous and future lessons').save()
			new Message(code:'language.change.future.message', locale: 'en', text: 'Change the language on future lessons only').save()
			
			//Error Messages
			new Message(code:'error.lesson.missing', locale: 'en', text: 'Sorry, that lesson can no longer be found.').save()
			new Message(code:'nothingFound.message', locale: 'en', text: 'Nothing found.').save()
			new Message(code:'default.blank.message', locale: 'en', text: 'Please fill out all required fields.').save()
			new Message(code:'default.invalid.min.size.message', locale: 'en', text: 'Password must be at least 6 characters.').save()
			new Message(code:'default.invalid.email.message', locale: 'en', text: 'Please enter a valid email address.').save()
			new Message(code:'default.not.unique.message', locale: 'en', text: 'Sorry, that email address belongs to an existing account.').save()
			new Message(code:'userRegistrationCommand.password.validator.error', locale: 'en', text: 'Your password cannot be the same as your email.').save()
			new Message(code:'userRegistrationCommand.passwordRepeat.validator.error', locale: 'en', text: 'Retyped password does not match.').save()
			new Message(code:'springSecurity.errors.login.fail', locale: 'en', text: 'Sorry, that email and password does not match.').save()
			new Message(code:'springSecurity.denied.message', locale: 'en', text: 'Sorry, you are not authorized to view that page.').save()
			new Message(code:'error.unknown', locale: 'en', text: 'An unknown error has occurred. Please try again.').save()
			new Message(code:'error.media.missing', locale: 'en', text: 'Please choose media to upload before saving.').save()
			new Message(code:'error.unknown.image.save', locale: 'en', text: 'An unknown error has occurred while saving the image. Please try renaming your image or choosing a different one.').save()
			new Message(code:'error.lesson.already.edited', locale: 'en', text: 'This lesson has already been edited. Please see changes below and try editing again.').save()
			new Message(code:'error.media.large', locale: 'en', text: 'File size is too large. Please reduce the size and try again.').save()
			new Message(code:'error.media.format', locale: 'en', text: 'The format you are uploading is not accepted.').save()
			new Message(code:'com.lingoto.Lesson.wordPhrase.unique.error', locale: 'en', text: 'Sorry, it looks like that lesson was already created.').save()
			
			
			//Terms of Service Messages
			new Message(code:'termsOfService.1.para1', locale: 'en', text: 'Terms of Service').save()
			new Message(code:'termsOfService.2.para2', locale: 'en', text: 'This agreement was written in English (US).').save()
			new Message(code:'termsOfService.3.para2', locale: 'en', text: 'To the extent any translated version of this agreement conflicts with the English version, the English version controls.').save()
			new Message(code:'termsOfService.4.para3', locale: 'en', text: 'Lingoto is a credit based application.').save()
			new Message(code:'termsOfService.5.para3', locale: 'en', text: 'Credits can be purchased, earned by creating lessons or earned by performing translations where needed.').save()
			new Message(code:'termsOfService.6.para3', locale: 'en', text: 'One credit is deducted from your account for each new language lesson viewed, with exception to lessons created by you.').save()
			new Message(code:'termsOfService.7.para3', locale: 'en', text: 'Previously viewed lessons can be reviewed as many times as you like without credits being deducted from your account.').save()
			new Message(code:'termsOfService.8.para3', locale: 'en', text: 'If a lesson is removed that you have already viewed, one credit will be added back to your account.').save()
			new Message(code:'termsOfService.9.para4', locale: 'en', text: 'By creating an account on the Lingoto website or any Lingoto software (collectively the "Service") you signify your agreement to these terms and conditions (the "Terms of Service").').save()
			new Message(code:'termsOfService.10.para4', locale: 'en', text: 'If you do not agree to any of these terms or guidelines, please do not use the Service.').save()
			new Message(code:'termsOfService.11.para4', locale: 'en', text: 'These Terms of Service apply to all users of the Service, including users who are also contributors of Content on the Service.').save()
			new Message(code:'termsOfService.12.para4', locale: 'en', text: '"Content" includes the text, graphics, photos, sounds, videos, audiovisual combinations, and other materials you may view on, access through, or contribute to the Service. ').save()
			new Message(code:'termsOfService.13.para5', locale: 'en', text: 'Prices for products offered via the Service may change at any time, and Lingoto does not provide price protection or refunds in the event of a price reduction or promotional offering.').save()
			new Message(code:'termsOfService.14.para5', locale: 'en', text: 'Lingoto is under no obligation to compensate for downtime, whether the downtime be caused by Lingoto or other.').save()
			new Message(code:'termsOfService.15.para6', locale: 'en', text: 'Media showing nudity, sexual content, harmful or dangerous acts, violence, hateful or graphic content may get age-restricted or removed depending on their severity.').save()
			new Message(code:'termsOfService.16.para7', locale: 'en', text: 'Respect copyright.').save()
			new Message(code:'termsOfService.17.para7', locale: 'en', text: 'Only upload Content that you made or that you are authorized to use.').save()
			new Message(code:'termsOfService.18.para8', locale: 'en', text: 'Do not create misleading lessons or Content in order to increase views.').save()
			new Message(code:'termsOfService.19.para8', locale: 'en', text: 'It is not okay to post large amounts of untargeted, unwanted or repetitive content, including private messages.').save()
			new Message(code:'termsOfService.20.para9', locale: 'en', text: 'When creating your account, you must provide accurate and complete information.').save()
			new Message(code:'termsOfService.21.para9', locale: 'en', text: 'You are solely responsible for the activity that occurs on your account, and you must keep your account password secure.').save()
			new Message(code:'termsOfService.22.para9', locale: 'en', text: 'Lingoto will not be liable for losses caused by any unauthorized use of your account.').save()
			new Message(code:'termsOfService.23.para10', locale: 'en', text: 'You agree not to use the Service for any commercial uses unless you obtain prior written approval from Lingoto.').save()
			new Message(code:'termsOfService.24.para10', locale: 'en', text: 'Educational institutions are permitted to use the Service as long as their students are not charged extra for the Service.').save()
			new Message(code:'termsOfService.25.para11', locale: 'en', text: 'You agree not to use or launch any automated system that accesses the Service more then humanly possibly.').save()
			new Message(code:'termsOfService.26.para11', locale: 'en', text: 'You agree not to collect or harvest any personally identifiable information, including account names, from the Service.').save()
			new Message(code:'termsOfService.27.para11', locale: 'en', text: 'You agree not to solicit, for commercial purposes, any users of the Service.').save()
			new Message(code:'termsOfService.28.para12', locale: 'en', text: 'You shall not copy, reproduce, sell, or otherwise exploit any Content without the prior written consent of Lingoto or the respective licensors of the Content.').save()
			new Message(code:'termsOfService.29.para13', locale: 'en', text: 'You agree not to circumvent, disable or otherwise interfere with security-related features of the Service.').save()
			new Message(code:'termsOfService.30.para14', locale: 'en', text: 'You understand that Lingoto is not responsible for the accuracy, usefulness, safety, or intellectual property rights of or relating to the Content submitted by users.').save()
			new Message(code:'termsOfService.31.para14', locale: 'en', text: 'You further understand and acknowledge that you may be exposed to Content that is inaccurate, offensive, or objectionable.').save()
			new Message(code:'termsOfService.32.para14', locale: 'en', text: 'You agree to hold harmless Lingoto, its owners and operators to the fullest extent allowed by law regarding all matters related to your use of the Service.').save()
			new Message(code:'termsOfService.33.para15', locale: 'en', text: 'As a Lingoto account holder you may submit Content to the Service.').save()
			new Message(code:'termsOfService.34.para15', locale: 'en', text: 'You understand that Lingoto does not guarantee any confidentiality with respect to any Content you submit.').save()
			new Message(code:'termsOfService.35.para15', locale: 'en', text: 'You shall be solely responsible for your own Content and the consequences of submitting and publishing your Content on the Service.').save()
			new Message(code:'termsOfService.36.para15', locale: 'en', text: 'You affirm that you own or have the necessary licenses, rights, and permissions to publish Content you submit.').save()
			new Message(code:'termsOfService.37.para16', locale: 'en', text: 'You retain all of your ownership rights in your Content.').save()
			new Message(code:'termsOfService.38.para16', locale: 'en', text: 'By submitting Content to Lingoto, you hereby grant Lingoto a worldwide, royalty-free, transferable license to use, reproduce, and distribute, the Content in connection with the Service.').save()
			new Message(code:'termsOfService.39.para16', locale: 'en', text: 'Lingoto may use the Content for promoting and redistributing part or all of the Service in any media formats and through any media channels.').save()
			new Message(code:'termsOfService.40.para16', locale: 'en', text: 'You also hereby grant each user of the Service a non-exclusive license to access your Content through the Service.').save()
			new Message(code:'termsOfService.41.para16', locale: 'en', text: 'You understand and agree that Lingoto may retain, display, distribute, or perform, copies of your Content that have been removed or deleted. ').save()
			new Message(code:'termsOfService.42.para17', locale: 'en', text: 'Lingoto may at any time, without prior notice and in its sole discretion, remove Content and/or terminate the account of a user if in violation of these Terms of Service.').save()
			new Message(code:'termsOfService.43.para18', locale: 'en', text: 'You affirm that you are either more than 18 years of age, or an emancipated minor, or possess legal parental or guardian consent. ').save()
			new Message(code:'termsOfService.44.para18', locale: 'en', text: 'You affirm that you are fully able and competent to enter into these Terms of Service, and to abide by and comply with these Terms of Service.').save()
			//***End Creating Messages***
			
//			println "Creating lessons."
			def lesson1 = new Lesson(user:adminUser,
				masterLang:language,
				wordPhrase:"one",
				category:"Beginner",
				informal:false,
				ageRestriction:12,
				lessonApproved:false,
				mediaApproved:false,
				needsReview:false,
				active:true,
				imagePath:"media/images/${adminUser.id}",
				imageExt:"jpg",
				imageName:"1.jpg",
				originalImageName:"one.jpg",
				audioPath:"media/audio/${adminUser.id}",
				originalAudioName:"one.mp3",
				sortOrder:1).save(flush:true)
			def lesson2 = new Lesson(user:adminUser,
				masterLang:language,
				wordPhrase:"two",
				category:"Beginner",
				informal:false,
				ageRestriction:12,
				lessonApproved:false,
				mediaApproved:false,
				needsReview:false,
				active:true,
				imagePath:"media/images/${adminUser.id}",
				imageExt:"jpg",
				imageName:"2.jpg",
				sortOrder:2).save(flush:true)
			def lesson3 = new Lesson(user:adminUser,
				masterLang:language,
				wordPhrase:"three",
				category:"Beginner",
				informal:false,
				ageRestriction:12,
				lessonApproved:false,
				mediaApproved:false,
				needsReview:true,
				active:true,
				imagePath:"media/images/${adminUser.id}",
				imageExt:"jpg",
				imageName:"3.jpg",
				sortOrder:3).save(flush:true)
			def lesson4 = new Lesson(user:adminUser,
				masterLang:language,
				wordPhrase:"four",
				category:"Beginner",
				informal:false,
				ageRestriction:12,
				lessonApproved:false,
				mediaApproved:false,
				needsReview:true,
				active:true,
				imagePath:"media/images/${adminUser.id}",
				imageExt:"jpg",
				imageName:"4.jpg",
				sortOrder:4).save(flush:true)
			
			/*for (i in 1..100){
				 //creating 100 users, languages and 100 lessons each
				 def userRole = new Role(authority: 'ROLE_USER').save(flush: true)
				 def newLang = new MasterLang(lingo: "lang${i}").save(flush: true)
				 def newUser = new User(username: "user${i}@test.com", enabled: true,
					 password: "secret").save(flush: true, failOnError:true)
				 def newAccount = new Account(firstName: "fname${i}", user:newUser,
					 lastName: "lname${i}", primaryLanguage: newLang, age:15, adultContent:false, credits:20).save(flush: true)
				 
				 UserRole.create newUser, userRole, true
				 
				 for (idx in 1..100){
					 new Lesson(user:newUser,
						 masterLang:language,
						 wordPhrase:"Test Lesson ${idx}",
						 category:"Beginner",
						 informal:false,
						 ageRestriction:15,
						 lessonApproved:true,
						 mediaApproved:true,
						 needsReview:false,
						 active:true,
						 imagePath:"media/images/user${idx}@test.com",
						 imageExt:"jpg",
						 imageName:"${idx}.jpg",
						 sortOrder:idx).save(flush: true)
				 }
			 }
			*/
			
			for (i in 5..10){
				def lessons = new Lesson(user:adminUser,
					masterLang:language,
					wordPhrase:"Intermediate Test ${i}",
					category:"Intermediate",
					informal:false,
					ageRestriction:18,
					lessonApproved:true,
					mediaApproved:true,
					needsReview:false,
					active:true,
					imagePath:"media/images/${adminUser.id}",
					imageExt:"jpg",
					imageName:"${i}.jpg",
					sortOrder:i).save(flush:true)
			}
			
			//TODO: 14- create better example lessons to add in bootstrap.
			
//			println "Creating Spanish user."
			def spLanguage = MasterLang.findByLingoIlike("Español")
			if (!spLanguage){
				spLanguage = new MasterLang(
					iso2:'es', iso3:'spa', languageRef:'Spanish',
					lingo: 'Español').save()
			}
			def user2 = new User(username: "pedro@lingoto.com", enabled: true,
				password: "secret").save(flush: true, failOnError:true)
			def account2 = new Account(firstName: "Pedro", user:user2,
				lastName: "Rodriguez", primaryLanguage: spLanguage, age:15, adultContent:false, credits:20).save()
			def userRole = new Role(authority: 'ROLE_USER').save()
			UserRole.create user2, userRole, true
			
			def user3 = new User(username: "pedro3@lingoto.com", enabled: true,
				password: "secret")
			user3.account = new Account(firstName: "Pedro3", 
				lastName: "Rodriguez", primaryLanguage: spLanguage, age:18, adultContent:true, credits:20)
			user3.save(flush: true, failOnError:true)
			def userRole3 = new Role(authority: 'ROLE_USER').save()
			UserRole.create user3, userRole3, true
			
			def user4 = new User(username: "pedro2@lingoto.com", enabled: true,
				password: "secret")
			user4.account = new Account(firstName: "Pedro2", 
				lastName: "Rodriguez", primaryLanguage: spLanguage, age:18, adultContent:true, credits:20)
			user4.save()
			def userRole4 = new Role(authority: 'ROLE_USER').save()
			UserRole.create user4, userRole4, true
			
			for (i in 101..106){
				def lessons = new Lesson(user:user3,
					masterLang:spLanguage,
					wordPhrase:"Intermediate Test ${i}",
					category:"Intermediate",
					informal:false,
					ageRestriction:0,
					lessonApproved:true,
					mediaApproved:true,
					needsReview:false,
					active:true,
					imagePath:"media/images/${user3.id}",
					imageExt:"jpg",
					imageName:"${i}.jpg",
					sortOrder:i).save(flush:true)
			}
			
			for (i in 107..12){
				def lessons = new Lesson(user:user4,
					masterLang:spLanguage,
					wordPhrase:"Beginner Test ${i}",
					category:"Beginner",
					informal:true,
					ageRestriction:15,
					lessonApproved:true,
					mediaApproved:true,
					needsReview:false,
					active:true,
					imagePath:"media/images/${user4.id}",
					imageExt:"jpg",
					imageName:"${i}.jpg",
					sortOrder:i).save(flush:true)
			}
			
			for (i in 31..100){
				def lessons = new Lesson(user:user2,
					masterLang:spLanguage,
					wordPhrase:"Beginner Test ${i}",
					category:"Beginner",
					informal:false,
					ageRestriction:15,
					lessonApproved:true,
					mediaApproved:true,
					needsReview:false,
					active:true,
					imagePath:"media/images/${user2.id}",
					imageExt:"jpg",
					imageName:"${i}.jpg",
					sortOrder:i).save()
			}
			
			def rating = new Rate(rating:5, ratedAccountID:account.id, ratedByAccountID:account2.id).save()
			def rating2 = new Rate(rating:4, ratedAccountID:user2.account.id, ratedByAccountID:account2.id).save()
			def rating3 = new Rate(rating:1, ratedAccountID:user3.account.id, ratedByAccountID:account2.id).save()
			def rating4 = new Rate(rating:2, ratedAccountID:user4.account.id, ratedByAccountID:account2.id).save()
		
		}
	}
}
