modules = {
    application {
		dependsOn 'jquery'
        resource url:'js/application.js'
    }
	user {
		dependsOn 'jquery'
		resource url:'js/user.js'
	}
	lesson {
		dependsOn 'jquery'
		resource url:'js/lesson.js'
	}
	lessonList {
		dependsOn 'jquery'
		resource url:'js/lesson-list.js'
	}
	sorting {
		dependsOn 'jqueryui'
		resource url:'js/sorting.js'
	}
	jqueryui{
		resource url:'js/jquery-ui.js'
	}
	languageJS {
		dependsOn 'jqueryui'
		resource url:'js/language.js'
	}
	zxcvbn{
		resource url:'js/zxcvbn.js'
	}	

}