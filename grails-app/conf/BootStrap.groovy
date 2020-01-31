import com.lingoto.*
import grails.util.Environment

class BootStrap {

	def init = { servletContext ->
		
		switch (Environment.current) {
			case Environment.DEVELOPMENT:
				loadDataIfRequired()
				break
			case Environment.PRODUCTION:
				loadDataIfRequired()
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
			
			new Role(authority: 'ROLE_GUESTUSER').save()
			
			def superUserRole = new Role(authority: 'ROLE_SUPERUSER').save()
			def superUser = new User(username: "superuser@lingoto.com", enabled: true,
				password: "secret").save(flush: true, failOnError:true)
			def superAaccount = new Account(firstName: "Super", user:superUser,
				lastName: "User", primaryLanguage: language, age:18, adultContent:true, credits:20000).save()
			UserRole.create superUser, superUserRole, true
			
			//***Creating Messages***
//			println "Creating Messages"
			//Labels
			new Message(code:'teach.label', locale: 'en', text: 'Teach').save()
			new Message(code:'learn.label', locale: 'en', text: 'Learn').save()
			new Message(code:'quiz.label', locale: 'en', text: 'Quiz').save()
			new Message(code:'password.label', locale: 'en', text: 'Password').save()
			new Message(code:'login.label', locale: 'en', text: 'Login').save()
			new Message(code:'createAccount.label', locale: 'en', text: 'Create Account').save()
			new Message(code:'forgotPassword.label', locale: 'en', text: 'Forgot Password').save()
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
			new Message(code:'yourLanguage.label', locale: 'en', text: 'Your Language').save()
			new Message(code:'language.label', locale: 'en', text: 'Language').save()
			new Message(code:'dialect.label', locale: 'en', text: 'Dialect').save()
			new Message(code:'editAccount.label', locale: 'en', text: 'Edit Account').save()
			new Message(code:'firstName.label', locale: 'en', text: 'First Name').save()
			new Message(code:'lastName.label', locale: 'en', text: 'Last Name').save()
			new Message(code:'email.label', locale: 'en', text: 'Email').save()
			new Message(code:'age.label', locale: 'en', text: 'Age').save()
			new Message(code:'currentPassword.label', locale: 'en', text: 'Current Password').save()
			new Message(code:'updatePassword.label', locale: 'en', text: 'Update Password').save()
			new Message(code:'updateEmail.label', locale: 'en', text: 'Update Email').save()
			new Message(code:'newPassword.label', locale: 'en', text: 'New Password').save()
			new Message(code:'newEmail.label', locale: 'en', text: 'New Email').save()
			new Message(code:'retypePassword.label', locale: 'en', text: 'Retype Password').save()
			new Message(code:'retypeEmail.label', locale: 'en', text: 'Retype Email').save()
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
			new Message(code:'sendingResetLink.message', locale: 'en', text: 'An email will be sent with a link to reset your password.').save()
			new Message(code:'resetEmail.message', locale: 'en', text: 'A request to reset your Lingoto password was recently made.').save()
			new Message(code:'resetEmailLink.message', locale: 'en', text: 'Please click here to change your password.').save()
			new Message(code:'disregardResetEmail.message', locale: 'en', text: 'If you did not request a password reset, please disregard this email.').save()
			
			//Error Messages
			new Message(code:'error.lesson.missing', locale: 'en', text: 'Sorry, that lesson can no longer be found.').save()
			new Message(code:'nothingFound.message', locale: 'en', text: 'Nothing found.').save()
			new Message(code:'default.blank.message', locale: 'en', text: 'Please fill out all required fields.').save()
			new Message(code:'default.invalid.min.size.message', locale: 'en', text: 'Password must be at least 6 characters.').save()
			new Message(code:'default.invalid.email.message', locale: 'en', text: 'Please enter a valid email address.').save()
			new Message(code:'default.not.unique.message', locale: 'en', text: 'Sorry, that email address belongs to an existing account.').save()
			new Message(code:'userRegistrationCommand.password.validator.error', locale: 'en', text: 'Your password cannot be the same as your email.').save()
			new Message(code:'userRegistrationCommand.passwordRepeat.validator.error', locale: 'en', text: 'Retyped password does not match.').save()
			new Message(code:'userRegistrationCommand.usernameRepeat.validator.error', locale: 'en', text: 'Retyped email does not match.').save()
			new Message(code:'springSecurity.errors.login.fail', locale: 'en', text: 'Sorry, that email and password does not match.').save()
			new Message(code:'springSecurity.denied.message', locale: 'en', text: 'Sorry, you are not authorized to view that page.').save()
			new Message(code:'error.unknown', locale: 'en', text: 'An unknown error has occurred. Please try again.').save()
			new Message(code:'error.media.missing', locale: 'en', text: 'Please choose media to upload before saving.').save()
			new Message(code:'error.unknown.media.save', locale: 'en', text: 'An unknown error has occurred while saving the selected media. Please try choosing a different one.').save()
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
			new Message(code:'termsOfService.25.para11', locale: 'en', text: 'You agree not to use or launch any automated system that accesses the Service more then humanly possibly.').save()
			new Message(code:'termsOfService.26.para11', locale: 'en', text: 'You agree not to collect or harvest any personally identifiable information, including account names, from the Service.').save()
			new Message(code:'termsOfService.27.para11', locale: 'en', text: 'You agree not to solicit, for commercial purposes, any users of the Service.').save()
			new Message(code:'termsOfService.28.para12', locale: 'en', text: 'You shall not copy, reproduce, sell, or otherwise exploit any Content without the prior written consent of Lingoto or the respective licensors of the Content.').save()
			new Message(code:'termsOfService.29.para12', locale: 'en', text: 'Educational institutions are permitted to use the Service as long as their students are not charged extra for the Service.').save()
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
			
						//***Creating Spanish Messages***
			//Labels
			new Message(code:'teach.label', locale: 'es', text: 'Enseñar').save()
			new Message(code:'learn.label', locale: 'es', text: 'Aprender').save()
			new Message(code:'quiz.label', locale: 'es', text: 'Examen').save()
			new Message(code:'password.label', locale: 'es', text: 'Contraseña').save()
			new Message(code:'login.label', locale: 'es', text: 'Iniciar sesión').save()
			new Message(code:'createAccount.label', locale: 'es', text: 'Crear una cuenta').save()
			new Message(code:'forgotPassword.label', locale: 'en', text: 'Forgot Password').save()
			new Message(code:'logout.label', locale: 'es', text: 'Cerrar sesión').save()
			new Message(code:'hello.label', locale: 'es', text: 'Hola').save()
			new Message(code:'credits.label', locale: 'es', text: 'créditos').save()
			new Message(code:'createLanguageLesson.label', locale: 'es', text: 'Crear idioma de los cursos').save()
			new Message(code:'wordPhrase.label', locale: 'es', text: 'Palabra/Frase').save()
			new Message(code:'level.label', locale: 'es', text: 'Nivel').save()
			new Message(code:'category.beginner.label', locale: 'es', text: 'Principiante').save()
			new Message(code:'category.intermediate.label', locale: 'es', text: 'Intermedio').save()
			new Message(code:'category.advanced.label', locale: 'es', text: 'Avanzado').save()
			new Message(code:'formal.label', locale: 'es', text: 'Formal').save()
			new Message(code:'informal.label', locale: 'es', text: 'Informal/argot').save()
			new Message(code:'additionalInfo.label', locale: 'es', text: 'Información Adicional').save()
			new Message(code:'optional.label', locale: 'es', text: 'opcional').save()
			new Message(code:'langLessonsByAuthor.label', locale: 'es', text: 'Cursos de lengua por autor').save()
			new Message(code:'translate.label', locale: 'es', text: 'Traducir').save()
			new Message(code:'lessonCreatedBy.label', locale: 'es', text: 'Lección creado por').save()
			new Message(code:'lessonList.label', locale: 'es', text: 'Lista lección').save()
			new Message(code:'yourLanguage.label', locale: 'en', text: 'Su Idioma').save()
			new Message(code:'language.label', locale: 'es', text: 'Idioma').save()
			new Message(code:'dialect.label', locale: 'es', text: 'Dialecto').save()
			new Message(code:'editAccount.label', locale: 'es', text: 'Editar cuenta').save()
			new Message(code:'firstName.label', locale: 'es', text: 'Nombre de pila').save()
			new Message(code:'lastName.label', locale: 'es', text: 'Apellido').save()
			new Message(code:'email.label', locale: 'es', text: 'Correo electrónico').save()
			new Message(code:'age.label', locale: 'es', text: 'Años').save()
			new Message(code:'currentPassword.label', locale: 'es', text: 'Contraseña actual').save()
			new Message(code:'updatePassword.label', locale: 'es', text: 'Actualiza contraseña').save()
			new Message(code:'updateEmail.label', locale: 'en', text: 'Actualiza Correo Electrónico').save()
			new Message(code:'newPassword.label', locale: 'es', text: 'Nueva contraseña').save()
			new Message(code:'newEmail.label', locale: 'en', text: 'Nueva Correo Electrónico').save()
			new Message(code:'retypePassword.label', locale: 'es', text: 'Vuelva a escribir la contraseña').save()
			new Message(code:'retypeEmail.label', locale: 'en', text: 'Vuelva a escribir el correo electrónico').save()
			new Message(code:'contactUs.label', locale: 'es', text: 'Contáctenos').save()
			new Message(code:'forbidden.label', locale: 'es', text: 'Prohibido').save()
			
			//Messages
			new Message(code:'earnCredits.message', locale: 'es', text: '¡Ganar créditos!').save()
			new Message(code:'translateThisSite.message', locale: 'es', text: 'Traducir este sitio').save()
			new Message(code:'thanksForTranslating.message', locale: 'es', text: '¡Gracias por sus traducciones!').save()
			new Message(code:'unlockLessons.message', locale: 'es', text: '¡Desbloquear más lecciones!').save()
			new Message(code:'purchaseCredits.message', locale: 'es', text: 'Comprar créditos').save()
			new Message(code:'adminsKnowTheseLangs.message', locale: 'es', text: 'Lingoto administradores conocen los siguientes idiomas:').save()
			new Message(code:'weWillDoOurBest.message', locale: 'es', text: 'Haremos nuestro mejor esfuerzo para entender y responder si está utilizando un idioma diferente.').save()
			new Message(code:'language.change.warning.message', locale: 'es', text: 'Advertencia: Usted ha realizado cambios en su idioma.').save()
			new Message(code:'language.change.all.message', locale: 'es', text: 'Cambiar el idioma de todas las lecciones anteriores y futuras').save()
			new Message(code:'sendingResetLink.message', locale: 'en', text: 'Un correo electrónico será mandado con un enlace de internet para reiniciar su contraseña.').save()
			new Message(code:'resetEmail.message', locale: 'en', text: 'Recién se ha hecho una solicitud para reiniciar su contraseña de Lingoto.').save()
			new Message(code:'resetEmailLink.message', locale: 'en', text: 'Por favor haga clic en este enlace para cambiar su contraseña.').save()
			new Message(code:'disregardResetEmail.message', locale: 'en', text: 'Si usted no ha pedido reiniciar su contraseña, por favor ignore este correo electrónico.').save()
			
			//Error Messages
			new Message(code:'error.lesson.missing', locale: 'es', text: 'Lo sentimos, esa lección ya no se puede encontrar.').save()
			new Message(code:'nothingFound.message', locale: 'es', text: 'Nada encontrado.').save()
			new Message(code:'default.blank.message', locale: 'es', text: 'Por favor, rellene todos los campos obligatorios.').save()
			new Message(code:'default.invalid.min.size.message', locale: 'es', text: 'La contraseña debe tener al menos 6 caracteres.').save()
			new Message(code:'default.invalid.email.message', locale: 'es', text: 'Por favor, introduce una dirección de correo electrónico válida.').save()
			new Message(code:'default.not.unique.message', locale: 'es', text: 'Lo sentimos, dirección de correo electrónico pertenece a una cuenta existente.').save()
			new Message(code:'userRegistrationCommand.password.validator.error', locale: 'es', text: 'La contraseña no puede ser el mismo que su correo electrónico').save()
			new Message(code:'userRegistrationCommand.passwordRepeat.validator.error', locale: 'es', text: 'Volver a teclear. La contraseña no coincide.').save()
			new Message(code:'userRegistrationCommand.usernameRepeat.validator.error', locale: 'en', text: 'Volver a teclear. El correo electrónico no coincide.').save()
			new Message(code:'springSecurity.errors.login.fail', locale: 'es', text: 'Lo sentimos, el correo electrónico y la contraseña no coinciden.').save()
			new Message(code:'springSecurity.denied.message', locale: 'es', text: 'Lo sentimos, usted no está autorizado para ver esa página.').save()
			new Message(code:'error.unknown', locale: 'es', text: 'Un error desconocido a ocurrido.').save()
			new Message(code:'error.media.missing', locale: 'es', text: 'Por favor seleccione los medios para subir antes de guardar.').save()
			new Message(code:'error.unknown.media.save', locale: 'en', text: 'Un error desconocido ha ocurrido mientras se guarda los medios. Por favor, intente elegir uno diferente.').save()
			new Message(code:'error.lesson.already.edited', locale: 'es', text: 'Esta lección ya ha sido editado.').save()
			new Message(code:'error.media.large', locale: 'es', text: 'Tamaño del archivo es demasiado grande.').save()
			new Message(code:'error.media.format', locale: 'es', text: 'El formato que está cargando no es aceptada.').save()
			new Message(code:'com.lingoto.Lesson.wordPhrase.unique.error', locale: 'es', text: 'Lo sentimos, parece que esa lección ya se ha creado.').save()
			 
			//Terms of Service Messages
			new Message(code:'termsOfService.1.para1', locale: 'es', text: 'Términos de Servicio').save()
			new Message(code:'termsOfService.2.para2', locale: 'es', text: 'Este acuerdo fue escrito en Inglés (Estados Unidos).').save()
			new Message(code:'termsOfService.3.para2', locale: 'es', text: 'En la medida en cualquier versión traducida de este acuerdo entre en conflicto con la versión en Inglés, prevalecerá la versión en Inglés.').save()
			new Message(code:'termsOfService.4.para3', locale: 'es', text: 'Lingoto es una aplicación basada en crédito.').save()
			new Message(code:'termsOfService.5.para3', locale: 'es', text: 'Los créditos pueden ser adquiridos, obtenidos mediante la creación de lecciones o han sido obtenidos por las traducciones que realizan cuando sea necesario.').save()
			new Message(code:'termsOfService.6.para3', locale: 'es', text: 'Un crédito se deduce de su cuenta para cada clase de lengua nueva vista, con excepción a las lecciones creadas por ti.').save()
			new Message(code:'termsOfService.7.para3', locale: 'es', text: 'Anteriormente lecciones vistos pueden ser revisados tantas veces como desee sin créditos se deducirán de su cuenta.').save()
			new Message(code:'termsOfService.8.para3', locale: 'es', text: 'Si se extrae una lección que ya se ha visto, un crédito se volverá a añadir a su cuenta.').save()
			new Message(code:'termsOfService.9.para4', locale: 'es', text: 'Al crear una cuenta en el sitio web Lingoto o cualquier software Lingoto (colectivamente el "Servicio"), usted expresa su aceptación de estos términos y condiciones (los "Términos de Servicio".').save()
			new Message(code:'termsOfService.10.para4', locale: 'es', text: 'Si no está de acuerdo con cualquiera de estos términos o directrices, por favor no utilice el Servicio.').save()
			new Message(code:'termsOfService.11.para4', locale: 'es', text: 'Estas Términos del Servicio se aplican a todos los usuarios del Servicio, incluidos los usuarios que también son contribuyentes de contenido en el Servicio.').save()
			new Message(code:'termsOfService.12.para4', locale: 'es', text: '"Contenido" incluye el texto, gráficos, fotos, sonidos, vídeos, combinaciones audiovisuales y otros materiales que se pueden ver en, el acceso a través de, o contribuir al Servicio.').save()
			new Message(code:'termsOfService.13.para5', locale: 'es', text: 'Los precios de los productos ofrecidos a través del Servicio pueden cambiar en cualquier momento, y Lingoto no proporciona protección de precios o reembolsos en el caso de reducción de precios u ofertas promocionales.').save()
			new Message(code:'termsOfService.14.para5', locale: 'es', text: 'Lingoto no tiene ninguna obligación de compensar el tiempo de inactividad, si el tiempo de inactividad ser causada por Lingoto u otro.').save()
			new Message(code:'termsOfService.15.para6', locale: 'es', text: 'Medios mostrando la desnudez, contenido sexual, actos dañinos o peligrosos, la violencia, el odio o el contenido gráfico pueden tener una restricción de edad o eliminado dependiendo de su gravedad.').save()
			new Message(code:'termsOfService.16.para7', locale: 'es', text: 'Respete los derechos de autor.').save()
			new Message(code:'termsOfService.17.para7', locale: 'es', text: 'Sólo carga contenido que usted ha hecho o que está autorizado para su uso.').save()
			new Message(code:'termsOfService.18.para8', locale: 'es', text: 'No cree engañosa lecciones o contenido con el fin de aumentar las reproducciones.').save()
			new Message(code:'termsOfService.19.para8', locale: 'es', text: 'No es aceptable publicar grandes cantidades de contenido no directo, no deseado o repetitivo, incluyendo mensajes privados.').save()
			new Message(code:'termsOfService.20.para9', locale: 'es', text: 'Al crear su cuenta, debe proporcionar información precisa y completa.').save()
			new Message(code:'termsOfService.21.para9', locale: 'es', text: 'Usted es el único responsable de la actividad que ocurre en su cuenta, y debe mantener su contraseña de cuenta segura.').save()
			new Message(code:'termsOfService.22.para9', locale: 'es', text: 'Lingoto no será responsable de las pérdidas causadas por el uso no autorizado de su cuenta.').save()
			new Message(code:'termsOfService.25.para11', locale: 'es', text: 'El usuario acepta no utilizar o lanzar ningún sistema automatizado que tiene acceso al servicio más de lo humanamente posible.').save()
			new Message(code:'termsOfService.26.para11', locale: 'es', text: 'Usted se compromete a no recoger o almacenar cualquier información de identificación personal, incluyendo nombres de cuenta, del Servicio.').save()
			new Message(code:'termsOfService.27.para11', locale: 'es', text: 'Usted se compromete a no solicitar, con fines comerciales, a ningún usuario del Servicio.').save()
			new Message(code:'termsOfService.28.para12', locale: 'es', text: 'Usted no podrá copiar, reproducir, vender, o de otra forma explotar cualquier contenido sin el consentimiento previo por escrito de los respectivos Lingoto o licenciatarios del Contenido.').save()
			new Message(code:'termsOfService.29.para12', locale: 'es', text: 'Se permiten las instituciones educativas para utilizar el Servicio, siempre que sus estudiantes no se les cobra extra por el Servicio.').save()
			new Message(code:'termsOfService.29.para13', locale: 'es', text: 'Usted está de acuerdo con eludir, desactivar o interferir con las funciones relacionadas con la seguridad del Servicio.').save()
			new Message(code:'termsOfService.30.para14', locale: 'es', text: 'Usted entiende que Lingoto no es responsable de la exactitud, utilidad, seguridad o derechos de propiedad intelectual de o en relación con el contenido enviado por los usuarios.').save()
			new Message(code:'termsOfService.31.para14', locale: 'es', text: 'Usted entiende y reconoce que puede estar expuesto a Contenido que es inexacto, ofensivo o censurable.').save()
			new Message(code:'termsOfService.32.para14', locale: 'es', text: 'Usted acepta mantener indemne Lingoto, sus propietarios y operadores en la mayor medida permitida por la ley con respecto a todos los asuntos relacionados con su uso del Servicio.').save()
			new Message(code:'termsOfService.33.para15', locale: 'es', text: 'Como titular de la cuenta Lingoto puede enviar contenido al Servicio.').save()
			new Message(code:'termsOfService.34.para15', locale: 'es', text: 'Usted entiende que Lingoto no garantiza la confidencialidad con respecto a cualquier Contenido que publique.').save()
			new Message(code:'termsOfService.35.para15', locale: 'es', text: 'Usted será el único responsable de su propio contenido y las consecuencias de la presentación y publicación de su Contenido en el Servicio.').save()
			new Message(code:'termsOfService.36.para15', locale: 'es', text: 'Usted afirma que posee o tiene las licencias, derechos y permisos para publicar contenido que usted propone.').save()
			new Message(code:'termsOfService.37.para16', locale: 'es', text: 'Usted conserva todos los derechos de propiedad en su contenido.').save()
			new Message(code:'termsOfService.38.para16', locale: 'es', text: 'Al enviar contenido a Lingoto, usted otorga Lingoto una licencia mundial, libre de regalías, transferible para usar, reproducir y distribuir el contenido en relación con el Servicio.').save()
			new Message(code:'termsOfService.39.para16', locale: 'es', text: 'Lingoto puede utilizar el contenido de la promoción y redistribución de parte o la totalidad del Servicio en cualquier formato ya través de cualquier canal de comunicación.').save()
			new Message(code:'termsOfService.40.para16', locale: 'es', text: 'También otorga a cada usuario del servicio de una licencia no exclusiva para acceder a su Contenido a través del Servicio.').save()
			new Message(code:'termsOfService.41.para16', locale: 'es', text: 'Usted entiende y acepta que Lingoto puede conservar, exhibir, distribuir, o realizar, copias de su contenido que han sido retirados o eliminados.').save()
			new Message(code:'termsOfService.42.para17', locale: 'es', text: 'Lingoto podrá, en cualquier momento y sin previo aviso ya su sola discreción, eliminar Contenido y / o cancelar la cuenta de un usuario en caso de violación de estas Términos de Servicio.').save()
			new Message(code:'termsOfService.43.para18', locale: 'es', text: 'Usted afirma que tiene más de 18 años de edad, o menor emancipado, o posee consentimiento de su padre o tutor.').save()
			new Message(code:'termsOfService.44.para18', locale: 'es', text: 'Usted afirma que es completamente capaz y competente para entrar en estas Términos de Servicio y de acatar y cumplir con estas Términos de servicio.').save()
			
			//***End Creating Spanish Messages*** 
			
			
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
//				imageExt:"jpg",
				originalImageName:"one.jpg",
				audioPath:"media/audio/${adminUser.id}",
				originalAudioName:"one.mp3",
				dateCreated: new Date(),
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
				originalImageName:"test.jpg",
//				imageExt:"jpg",
//				imageName:"2.jpg",
				dateCreated: new Date(),
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
				originalImageName:"test.jpg",
//				imageExt:"jpg",
//				imageName:"3.jpg",
				dateCreated: new Date(),
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
				originalImageName:"test.jpg",
//				imageExt:"jpg",
//				imageName:"4.jpg",
				dateCreated: new Date(),
				sortOrder:4).save(flush:true)
			/*
			for (i in 1..100){
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
						 dateCreated: new Date(),
						 sortOrder:idx).save(flush: true)
				 }
			 }
			
			
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
					originalImageName:"test.jpg",
					dateCreated: new Date(),
//					imageExt:"jpg",
//					imageName:"${i}.jpg",
					sortOrder:i).save(flush:true)
			}
			
			//TODO: 13- create better example lessons to add in bootstrap.
			*/
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
					originalImageName:"test.jpg",
//					imageExt:"jpg",
//					imageName:"${i}.jpg",
					dateCreated: new Date(),
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
					originalImageName:"test.jpg",
					dateCreated: new Date(),
//					imageExt:"jpg",
//					imageName:"${i}.jpg",
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
					originalImageName:"test.jpg",
//					imageExt:"jpg",
//					imageName:"${i}.jpg",
					dateCreated: new Date(),
					sortOrder:i).save()
			}
			
			def rating = new Rate(rating:5, ratedAccountID:account.id, ratedByAccountID:account2.id).save()
			def rating2 = new Rate(rating:4, ratedAccountID:user2.account.id, ratedByAccountID:account2.id).save()
			def rating3 = new Rate(rating:1, ratedAccountID:user3.account.id, ratedByAccountID:account2.id).save()
			def rating4 = new Rate(rating:2, ratedAccountID:user4.account.id, ratedByAccountID:account2.id).save()
		
		}
	}
}
