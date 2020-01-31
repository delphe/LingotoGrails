<nav id="Navbar" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-ex1-collapse,#navbar-ex2-collapse,#navbar-ex3-collapse">
       		<span class="sr-only">Toggle navigation</span>
       		<span class="icon-bar"></span>
           	<span class="icon-bar"></span>
           	<span class="icon-bar"></span>
		</button>

		<a class="navbar-brand" style="padding: 5px; color:#1F3E73" href="${createLink(uri: '/')}">
			<img src="${resource(dir:'images',file:'DTlogo.png')}" border="0" height="40"/><g:if test="${grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")}">-beta</g:if>
		</a>
	</div>

	<div class="collapse navbar-collapse" role="navigation" id="navbar-ex1-collapse">
		<form action='/lingoto/j_spring_security_check' method='POST' id='loginForm' autocomplete='off' class="navbar-form navbar-right">
			<sec:ifLoggedIn>
				<sec:ifNotGranted roles="ROLE_GUESTUSER">
					<div class="form-group fieldcontain text-left">
						<div class="col-md-12">
							<g:message code="hello.label" default="Hello" /> <g:link controller="user" action="edit">${session.firstName}</g:link>
						</div>
					</div>
				</sec:ifNotGranted>
				<g:if test="${session.credits >= 0}">
					<div class="form-group fieldcontain text-left">
						<div class="col-md-12">
							<g:message code="credits.label" default="credits" /> 
							<g:if test="${session.credits > 5}">
								<b class="green">${session.credits}</b>
							</g:if>
							<g:else>
								<b class="red">${session.credits}</b>
							</g:else>
						</div>
					</div>
				</g:if>
				<sec:ifNotGranted roles="ROLE_GUESTUSER">
					<div class="form-group fieldcontain text-left">
						<div class="col-md-12">
							<g:link controller="logout" action="index"> <g:message code="logout.label" default="Logout" /></g:link> 
						</div>
					</div>
				</sec:ifNotGranted>
				<sec:ifAnyGranted roles="ROLE_GUESTUSER">
					<div class="form-group fieldcontain text-left">
						<div class="col-md-12">
							<g:link controller="login" action="login"> <g:message code="login.label" default="login" /></g:link> 
						</div>
					</div>
					<div class="form-group fieldcontain text-left">
						<div class="col-md-12">
							<g:link controller="user" action="edit"><g:message code="createAccount.label" default="Create Account" /></g:link>
						</div>
					</div>
				</sec:ifAnyGranted>
				
			</sec:ifLoggedIn> 
			<sec:ifNotLoggedIn>
				<g:if test="${controllerName != 'login' && controllerName != 'user'}">
					<div class="form-group fieldcontain text-left">
						<label class="col-md-4 control-label" for='username'><g:message code="email.label" default="Email" />:</label>
						<div class="col-md-8">
							<input class="form-control input-md" type='text' name='j_username' id='username'/>
						</div>
					</div>
					<div class="form-group fieldcontain text-left">
						<label class="col-md-4 control-label" for='password'><g:message code="password.label" default="Password" />:</label>
						<div class="col-md-8">
							<input class="form-control input-md" type='password' name='j_password' id='password'/>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-12">
							<input class="btn btn-default" type='submit' id="submit" value='login'/>
						</div>
					</div>
					<div class="form-group fieldcontain text-left">
						<div class="col-md-12">
							<g:link controller="user" action="register"> <g:message code="createAccount.label" default="Create Account" /></g:link>
						</div>
					</div>
				</g:if>	
			</sec:ifNotLoggedIn>
		</form>
		<sec:ifNotLoggedIn>
			<g:if test="${controllerName == 'home'}">
				<script type='text/javascript'>
					<!--
					(function() {
						document.forms['loginForm'].elements['j_username'].focus();
					})();
					// -->
				</script>
			</g:if>
		</sec:ifNotLoggedIn>
		<ul class="nav navbar-nav navbar-left">
	 		<g:render template="/_menu/language"/>
	 		<sec:ifAnyGranted roles="ROLE_ADMIN">
	 			<g:render template="/_menu/admin"/>
	 		</sec:ifAnyGranted>										
	    </ul>
	</div>
</nav>
