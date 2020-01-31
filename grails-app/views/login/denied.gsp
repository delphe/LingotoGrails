<head>
<meta name='layout' content='main' />
<title><g:if test="${grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")}"><g:message code="default.title.label" default="lingoto"/>-beta</g:if>
        	<g:else>
        		<g:message code="default.title.label" default="lingoto"/>
        	</g:else> - 403 - <g:message code="forbidden.label" default="Forbidden"/>!</title>
</head>

<body>
<div class='body'>
	<div class='errors'><g:message code="springSecurity.denied.message" /></div>
</div>
</body>
