<g:javascript library="sorting" />

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>lingoto - <g:message code="lessonList.label" default="Lesson List" /></title>
        
    </head>
    <body>
        
        <h2><g:message code="lessonList.label" default="Lesson List" /></h2>
           
        <g:hasErrors bean="${lesson}">
            <div class="errors">
               <g:renderErrors bean="${lesson}" as="list" />
            </div>
        </g:hasErrors>
	        
        <div class="list-body">
            
            <div class="container" style="padding:0px">
			    <div id="filterButtons" class="btn-group col-md-4" data-toggle="buttons">
			        <label id="beginnerFilterBtn" class="btn btn-default active">
			        	<g:radio name="category" value="${beginnerLessonList}" checked="${beginnerLessonList}"
							onchange="${remoteFunction (
									action: 'selectedList',
									params: [category:'Beginner'],
									update: [success: 'sortableList'],
									onComplete: 'loadSortableListFunctions();')}"/> 
			            	<g:message code="category.beginner.label" default="Beginner" />
			        </label>
			        <label id="intermediateFilterBtn" class="btn btn-default">
			            <g:radio name="category" value="${intermediateLessonList}" checked="${intermediateLessonList}"
							onchange="${remoteFunction (
									action: 'selectedList',
									params: [category:'Intermediate'],
									update: [success: 'sortableList'],
									onComplete: 'loadSortableListFunctions();')}"/>
							<g:message code="category.intermediate.label" default="Intermediate" />
			        </label>
			        <label id="advancedFilterBtn" class="btn btn-default">
			            <g:radio name="category" value="${advancedLessonList}" checked="${advancedLessonList}"
							onchange="${remoteFunction (
									action: 'selectedList',
									params: [category:'Advanced'],
									update: [success: 'sortableList'],
									onComplete: 'loadSortableListFunctions();')}"/>
			            	<g:message code="category.advanced.label" default="Advanced" />
			        </label>
			    </div>
			</div>

            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            
			<div id="sortableList" class=" list">
	           	<g:render template="sortableList"/>
			</div>

        </div>
    </body>
</html>
