package com.lingoto

class HomeController {
	//IE message "this page uses fonts that need to be temporarily installed." may occur on all pages.
	//May want to troubleshoot if it becomes an issue. Here's a workaround for the user:
	//http://answers.microsoft.com/en-us/ie/forum/ie8-windows_other/temporary-fonts-when-visiting-specific-sites-im/80cc2fa7-69ad-4329-a1e3-44c3821a21ae
	
	//TODO: Phase 2- Use FlagsTagLib with Contact Us page
	//TODO: Phase 2-5- Pull admin@lingoto email in contact us template from the an administrative settings DB table instead of hard coding.
	
	//TODO: Phase 3- Include ads for Netflix (and other video sites) that show movies available in the language they are studying. Example (if studying ES): http://instantwatcher.com/search?language_audio=58 - http://www.programmableweb.com/api/netflix
	
	//TODO: 14- fix meta tag for FB image: <meta property="og:image" content="http://lingoto-beta.com/static/images/DTlogo.png"/>
	//TODO: 13- add www.lingoto-beta.com if no additional cost. Otherwise, research how to setup www.lingoto.com
    def index = {}

}