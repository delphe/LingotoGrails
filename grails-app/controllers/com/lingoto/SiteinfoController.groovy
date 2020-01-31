package com.lingoto

class SiteinfoController {
	
	def index = {redirect(action: "systeminfo")}
    def systeminfo = {}
	
	def stats = {
		def oneDayAgo = new Date() -1
		def oneWeekAgo = new Date() -7
		def newUsersOneDay = Account.countByDateCreatedGreaterThan(oneDayAgo)
		def newLessonsOneDay = Lesson.executeQuery('select count(l) from Lesson l WHERE l.dateCreated > ?',[oneDayAgo])
		def newUsersOneWeek = Account.countByDateCreatedGreaterThan(oneWeekAgo)
		def newLessonsOneWeek = Lesson.executeQuery('select count(l) from Lesson l WHERE l.dateCreated > ?',[oneWeekAgo])
		def lessonsViewedOneDay = ViewedLesson.countByDateCreatedGreaterThan(oneWeekAgo)
		def lessonsViewedOneWeek = ViewedLesson.countByDateCreatedGreaterThan(oneWeekAgo)
		
		[userCount:User.count(), lessonCount:Lesson.count(), lessonsViewedCount:ViewedLesson.count(),
			newUsersOneDay:newUsersOneDay,
			newLessonsOneDay:newLessonsOneDay[0],
			newUsersOneWeek:newUsersOneWeek,
			newLessonsOneWeek:newLessonsOneWeek[0],
			lessonsViewedOneDay:lessonsViewedOneDay,
			lessonsViewedOneWeek:lessonsViewedOneWeek]
	}
}