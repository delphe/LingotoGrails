class UrlMappings {

	static mappings = {
		
		/*
		 * Pages with controller
		 * WARN: No domain/controller should be named "api" or "mobile" or "web"!
		 */
		"/"	{
			controller	= 'home'
			action		= { 'index' }
			view		= { 'index' }
		}
		"/$controller/$action?/$id?"{
			constraints {
				controller(matches:/^((?!(api|mobile|web)).*)$/)
		  	}
		}
		
		/*
		 * System Pages without controller
		 */
		"403"	(view:'/_errors/403')
		"404"	(view:'/_errors/404')
		"500"	(view:'/_errors/500')
		"503"	(view:'/_errors/503')
	}
}
