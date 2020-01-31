<div class="actions">
	<a href="${createLink(uri: '/')}" class="btn btn-large btn-primary">
		<span class="glyphicon glyphicon-home" aria-hidden="true"></span><span class="sr-only">Home</span>&nbsp;
	</a>
	<a href="#" data-toggle="modal" data-target="#contactUsModal" class="btn btn-large btn-success">
   		<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> <g:message code="contactUs.label" default="Contact Us"/></a>
   <g:render template="/contactUs"/>					
</div>