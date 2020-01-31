<html>
    <head>
        <title>lingoto - <g:message code="teach.label" default="Teach" /></title>
        <meta name="layout" content="main"/>
    </head>
    <body>

        <h2><g:message code="createLanguageLesson.label" default="Create Language Lesson" /></h2>
        <h3>${user.account.primaryLanguage.lingo}
        	<g:if test="${user.account.primaryLanguage.dialect && user.account.primaryLanguage.dialect != ''}">
        		(${user.account.primaryLanguage.dialect})
        	</g:if> 
        </h3>
        	
        
        <g:hasErrors bean="${lesson}">
            <div class="errors">
               <g:renderErrors bean="${lesson}" as="list" />
            </div>
        </g:hasErrors>
        <g:if test="${flash.error}">
		  <div class="errors" >${flash.error}</div>
		</g:if>
		
        <g:uploadForm action="saveLesson" name="teachForm">
        	<g:render template="lessonForm"/>
        </g:uploadForm>
        
    </body>
</html>
