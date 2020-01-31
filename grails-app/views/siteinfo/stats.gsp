<html>

<head>
    <title><g:if test="${grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")}"><g:message code="default.title.label" default="lingoto"/>-beta</g:if>
        	<g:else>
        		<g:message code="default.title.label" default="lingoto"/>
        	</g:else></title>
    <meta name="layout" content="main" />
</head>

<body>

	<div class="container">
		<h1 class="title">Lingoto Stats</h1>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-3">
					<h3>24 hrs</h3>
						<ul class="list-unstyled">
							<li>New Users: <b>${newUsersOneDay}</b></li>
							<li>New Lessons: <b>${newLessonsOneDay}</b></li>
							<li>Lessons Viewed: <b>${lessonsViewedOneDay}</b></li>
						</ul>
				</div>
				<div class="col-md-3">
					<h3>Seven Days</h3>
						<ul class="list-unstyled">
							<li>New Users: <b>${newUsersOneWeek}</b></li>
							<li>New Lessons: <b>${newLessonsOneWeek}</b></li>
							<li>Lessons Viewed: <b>${lessonsViewedOneWeek}</b></li>
						</ul>
				</div>
				<div class="col-md-3">
					<h3>Total</h3>
						<ul class="list-unstyled">
							<li>Users: <b>${userCount}</b></li>
							<li>Lessons: <b>${lessonCount}</b></li>
							<li>Lessons Viewed: <b>${lessonsViewedCount}</b></li>
						</ul>
				</div>
			</div>
		</div>
	</div>

</body>

</html>
