<html>
<head>
	<title>lingoto - <g:message code="learn.label" default="Learn" /></title>
	<meta name="layout" content="main" />
	
    <g:if test="${sec?.loggedInUserInfo(field: 'username') == lesson?.user?.username}">
		<modalbox:modalIncludes />
	</g:if>
	<g:else>
<%--			lesson.js doesn't seem to work with the modalbox plugin--%>
		<g:javascript library="lesson" />
	</g:else>
	
	<sm:inlinePlayer/>
</head>
<body>

		
<g:if test="${flash.message}">
	<g:render template="percentageModal" model="['percentCorrect':flash.message]"/>
</g:if>
<g:if test="${flash.error}">
  <div class="errors" >${flash.error}</div>
</g:if>
<g:if test="${lesson?.id}">
	<div class="row">
		<div class="col-md-2">
			${lesson?.masterLang?.lingo}<br/>
			<div style="color:gray;">${lesson?.masterLang?.dialect}</div>
		</div>
		<g:render template="filterButtons" />
		<div class="col-md" style="text-align:right; padding-right:10px;">
			<g:message code="lessonCreatedBy.label" default="Lesson created by" />:<br/>
			<div style="color:gray;">${lesson?.user?.account?.firstName} ${lesson?.user?.account?.lastName}</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-6"></div>
		<div class="col-md-6" style="text-align:right;">
			<g:if test="${ratingClassArray && sec?.loggedInUserInfo(field: 'username') != lesson?.user?.username}">
				<div class="rating">
					<span class="${ratingClassArray[0]}" id=ratingStar1 onclick="ratingClick(5, ${lesson?.user?.account?.id})">&#9734;</span>
					<span class="${ratingClassArray[1]}" id=ratingStar2 onclick="ratingClick(4, ${lesson?.user?.account?.id})">&#9734;</span>
					<span class="${ratingClassArray[2]}" id=ratingStar3 onclick="ratingClick(3, ${lesson?.user?.account?.id})">&#9734;</span>
					<span class="${ratingClassArray[3]}" id=ratingStar4 onclick="ratingClick(2, ${lesson?.user?.account?.id})">&#9734;</span>
					<span class="${ratingClassArray[4]}" id=ratingStar5 onclick="ratingClick(1, ${lesson?.user?.account?.id})">&#9734;</span>
				</div>
			</g:if>
		</div>
	</div>
	
	<div class="jumbotron" style="font-size:16px; line-height:1.42857;">
	<g:if test="${sec?.loggedInUserInfo(field: 'username') != lesson?.user?.username &&
		session.credits <= 0 && !viewedLesson}">
		<g:render template="unlockLessons"/>
	</g:if>
	<g:else>
		<div class="row">
			<div class="col-sm-6" style="color:gray;">
				<g:message code="level.label" default="Level" />: 
				<g:message code="category.${lesson?.category?.toLowerCase()}.label" default="${lesson?.category}" />
			</div>
			<div class="col-sm-6" style="text-align:right; color:gray;">
				<g:if test="${!lesson?.informal}">
					<g:set var="formal" value="true" />
				</g:if>
				<g:else>
					<g:set var="formal" value="${null}" />
				</g:else>
				<div style="white-space:nowrap;">
					<g:message code="formal.label" default="Formal" />:
					<g:radio name="formalRadio" value="Formal"
						checked="${formal}" disabled="true" />
				</div>
				<div style="white-space:nowrap;">
					<g:message code="informal.label" default="Informal/Slang" />:
					<g:radio name="informalRadio" value="Informal/Slang"
						checked="${lesson?.informal}" disabled="true" />
				</div>
			</div>
		</div>

		<h1 style="text-align:center;">
			<g:if test="${lesson?.audioPath}">
				<a href="${application.contextPath}/${lesson?.audioPath}/${lesson?.id}.mp3" style="text-decoration: none;">
					${lesson?.wordPhrase} </a>
			</g:if>
			<g:else>
				${lesson?.wordPhrase} 
			</g:else>
		</h1>
		
		<g:if test="${lesson.imagePath}">
			<img class="img-responsive center-block" src="${application.contextPath}/${lesson.imagePath}/${lesson.imageName}" />
		</g:if>	
		<dl>
			<dd>
				${lesson?.additionalInfo}
			</dd>
		</dl>
		<g:if test="${sec?.loggedInUserInfo(field: 'username') != lesson?.user?.username}">
			<p class="center">
				<g:if test="${session.credits <= 0 && !viewedLesson?.translation}">
					<b class="red"><g:message code="translate.label" default="Translate" />
					&#8594; ${session.usersLanguage} 
					<g:if test="${session.usersDialect}">
						(${session.usersDialect})
					</g:if>
					</b>
				</g:if>
				<g:else>
					<g:message code="translate.label" default="Translate" />
					 &#8594; ${session.usersLanguage}<g:if test="${session.usersDialect}">
						 (${session.usersDialect})</g:if>:
				</g:else>
				<g:textField name="translation" value="${viewedLesson?.translation}" maxlength="190"
					onblur="saveTranslation(${viewedLesson.id}, this.value)" onkeyup="unlockNextArrow()"/>
			</p>
		</g:if>
		</g:else>

	</div>
	<g:if test="${sec?.loggedInUserInfo(field: 'username') == lesson?.user?.username}">
		<div class="row">
			<div class="col-sm-3">
				<g:link controller="lesson" action="teach" class="btn btn-default">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span class="sr-only">Add More</span></g:link>
				<g:link controller="lesson" action="edit" params="[id:lesson?.id]" class="btn btn-default">
					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span><span class="sr-only">Edit</span></g:link> 
				<a href="#" data-toggle="modal" data-target="#deleteModal" class="btn btn-default">
				   <span class="glyphicon glyphicon-trash" aria-hidden="true"></span><span class="sr-only">Delete</span></a>
				<g:render template="delete"/>
			</div>
			<g:set var="usersLessonPlanURL" value="${createLink(action:'lessonPlan', absolute:true)}/?auth=${lesson?.user?.id}" />
			<div class="col-sm-3" style="text-align:right;">
				<a HREF="mailto:?subject=Lingoto - ${message(code:'lessonList.label', default:'Lesson List')}
					&body=${message(code:'lessonCreatedBy.label', default:'Lesson created by')}: ${lesson?.user?.account?.firstName} ${lesson?.user?.account?.lastName}
					 - ${message(code:'lessonList.label', default:'Lesson List')}: ${usersLessonPlanURL}" 
					class="btn btn-info">
				<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span></a>
			</div>
			<div class="col-sm-3" style="text-align:right;">
	         	<g:textField name="lessonPlanURL" class="form-control" value="${usersLessonPlanURL}"/>
			</div>
			<div class="col-sm-3" style="text-align:right;">
				<g:link controller="lesson" action="list" class="btn btn-success">
					<span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> <g:message code="lessonList.label" default="Lesson List" /></g:link>
			</div>
		</div>
	</g:if>

	<g:if test="${filteredLessonByAuthorList && filteredLessonByAuthorList?.size()>0 }">
	<div id="pagination">
		<g:render template="lessonPagination"/>
	</div>
	</g:if>

</g:if>
</body>
</html>
