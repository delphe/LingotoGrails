<!DOCTYPE html>
<html>
    <head>
        <title>lingoto</title>
        <meta name="layout" content="noNav" />
    </head>
    <body>
    
    <div class="container-fluid">
		<div class="row">
			<div class="col-xs-6">
				<g:link controller="lesson" action="teach">
					<div class="imgLink" style="text-align:center; width:80%">
						<img class="img-responsive" src="images/teacher.png"><br />
						<h1><g:message code="teach.label" default="Teach" /></h1>
					</div>
				</g:link>
			</div>
			<div class="col-xs-6">
				<g:link controller="lesson" action="index">
					<div class="imgLink" style="text-align:center; width:80%">
						<img class="img-responsive" src="images/student.png"><br />
						<h1><g:message code="learn.label" default="Learn" /></h1>
					</div>
				</g:link>
			</div>
		</div>
	</div>
    
    </body>
</html>
