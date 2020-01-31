<html>
	<head>
		<title>lingoto - 403 - <g:message code="forbidden.label" default="Forbidden"/>!</title>
		<meta name="layout" content="noNav"/>
	</head>

<body>
	<content tag="header">
		<!-- Empty Header -->
	</content>
	
  	<section id="Error" class="">
		<div class="big-message">
			<div class="container">
				<h1>
					<g:message code="error.403.callout"/>
				</h1>
				<h2>
					403 - <g:message code="forbidden.label" default="Forbidden"/>!
				</h2>
				<p>
					<g:message code="springSecurity.denied.message" default="Sorry, you are not authorized to view that page."/>
				</p>
				<g:render template="/_errors/actions"/>
			</div>
		</div>
	</section>
  
  
  </body>
</html>