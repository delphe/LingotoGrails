<html>
<head>
	<title>lingoto - <g:message code="langLessonsByAuthor.label" default="Language Lessons by Author" /></title>
	<meta name="layout" content="main" />
	<g:javascript library="lessonList" />
<%--	<g:javascript library="prototype" />--%>
</head>
<body>
	<g:if test="${flash.message}">
		<div class="error">
			<g:message code="${flash.message}" />
		</div>
	</g:if>

		<h2><g:message code="langLessonsByAuthor.label" default="Language Lessons by Author" /></h2>
		
		<div class="container" style="padding:0px">
		   <g:render template="filterButtons" />
		</div>
		
		<%--
	
		<g:formRemote name="filterForm" update="lessonAuthors"
	              url="[action:'showFilteredLessons']">
	
	    	<table>
	    		<tr>
					<td>
						<g:message code="category.beginner.label" default="Beginner" />: 
						<g:checkBox name="beginner" value="${params.beginner}" method="GET" 
							onclick="new Ajax.Updater('lessonAuthors','/lingoto/lesson/showFilteredLessons',{parameters:filterForm.serialize(this)});"/>
					</td>
					<td>
						<g:message code="category.intermediate.label" default="Intermediate" />: 
						<g:checkBox name="intermediate" value="${params.intermediate}" method="GET" 
							onclick="new Ajax.Updater('lessonAuthors','/lingoto/lesson/showFilteredLessons',{parameters:filterForm.serialize(this)});"/>
					</td>
					<td>
						<g:message code="category.advanced.label" default="Advanced" />: 
						<g:checkBox name="advanced" value="${params.advanced}" method="GET" 
							onclick="new Ajax.Updater('lessonAuthors','/lingoto/lesson/showFilteredLessons',{parameters:filterForm.serialize(this)});"/>
					</td>
				</tr>
				<tr>
					<td>
						<g:message code="formal.label" default="Formal" />: 
						<g:checkBox name="formal" value="true" method="GET" 
							onclick="new Ajax.Updater('lessonAuthors','/lingoto/lesson/showFilteredLessons',{parameters:filterForm.serialize(this)});"/>
					</td>
					<td>
						<g:message code="informal.label" default="Informal/Slang" />: 
						<g:checkBox name="informal" value="true" method="GET" 
							onclick="new Ajax.Updater('lessonAuthors','/lingoto/lesson/showFilteredLessons',{parameters:filterForm.serialize(this)});"/>
					</td>
				</tr>
			</table>
	
			<g:hiddenField name="lingo" value="${params.lingo}" />
			<g:hiddenField name="dialect" value="${params.dialect}" />
		</g:formRemote>
	
		--%>
		
	<g:each in="${lessonMap}" status="i" var="lm">
		<g:each in="${lm.value}" var="lang">
			<div class="panel-group" id="panel-769487">
				<div class="panel panel-default">
					<div class="panel-heading">
						 <a class="panel-title collapsed" data-toggle="collapse" data-parent="#panel-769487" 
						 	href="#panel-element-${i}" onclick="renderLessons('${i}')">
						 	<form id="form-${i}" method="post">
								<g:hiddenField name="lingo" value="${lang.key}"/>
								<g:hiddenField name="dialect" value="${lang.value}"/>
							</form>
							 <dl>
								 <dt>${lang.key}</dt>
								<g:if test="${lang.value != 'null'}">
									<dd>${lang.value}</dd>
								</g:if>
							</dl>
						 </a>
					</div>
					<div id="panel-element-${i}" class="panel-collapse collapse">
						<div class="panel-body" id="panel-body-${i}">
							<div id="loading-${i}">
								<img src="${createLinkTo(dir:'images',file:'spinner.gif')}" />
							</div>
							<div id="inlineResults-${i}"></div>
						</div>
					</div>
				</div>
			</div>
		
		<%--
			<modalbox:createLink controller="lesson" action="showLessons" width="400" 
				params="[lingo:lang.key, dialect:lang.value]" 
				title="${langLessonsByAuthor}">
					
					<dt>${lang.key}</dt>
					<g:if test="${lang.value != 'null'}">
						<dl>${lang.value}</dl>
					</g:if>
				
			</modalbox:createLink>--%>
		</g:each>
	</g:each>
</body>
</html>
