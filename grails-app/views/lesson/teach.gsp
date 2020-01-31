<html>
    <head>
        <title><g:if test="${grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")}"><g:message code="default.title.label" default="lingoto"/>-beta</g:if>
        	<g:else>
        		<g:message code="default.title.label" default="lingoto"/>
        	</g:else> - <g:message code="teach.label" default="Teach" /></title>
        <meta name="layout" content="main"/>
        <r:require modules="lesson"/>
    </head>
    <body>

        <h2><g:message code="createLanguageLesson.label" default="Create Language Lesson" /></h2>
        <h3>${user.account.primaryLanguage.lingo}
        	<g:if test="${user.account.primaryLanguage.dialect && user.account.primaryLanguage.dialect != ''}">
        		(${user.account.primaryLanguage.dialect})
        	</g:if> 
        </h3>
        	
        <errors:errorList bean="${lesson}"/>
        
        <g:if test="${flash.error}">
		  <div class="errors" >${flash.error}</div>
		</g:if>
		
        <g:uploadForm action="saveLesson" name="teachForm">
        	<g:render template="lessonForm"/>
        </g:uploadForm>
        
    </body>
</html>
