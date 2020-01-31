<html>
<head>
	<meta name='layout' content='noNav'/>
	<title><g:if test="${grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")}"><g:message code="default.title.label" default="lingoto"/>-beta</g:if>
        	<g:else>
        		<g:message code="default.title.label" default="lingoto"/>
        	</g:else></title>
	<g:javascript library="application" />
</head>

<body>

<div class="jumbotron" id='login'>
	<div class="form-horizontal">
		<div class='fheader'>
		</div>

		<g:if test='${flash.message}'>
			<div class='red'>${flash.message}</div>
		</g:if>

		<g:render template="resetPasswordModal"/>
		
		<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
			<%--<g:if test="${flash.message}">
				<div class="form-group">
				  <label class="col-xs-2 control-label"></label>
				  <div class="col-xs-10">
			  		<a id="resetPasswordLink" href="#">
			  			<g:message code="resetPassword.label" default="Reset Password" />
			  		</a>
				  </div>
				</div>
			</g:if>
			--%>
			<div class="form-group">
				<label for='username' class="col-md-2 control-label"><g:message code="email.label" default="Email"/>:</label>
				<div class="col-md-3">
					<input type='text' class="form-control" name='j_username' id='username'/>
				</div>
			</div>

			<div class="form-group">
				<label for='password' class="col-md-2 control-label"><g:message code="password.label" default="password"/>:</label>
				<div class="col-md-3">
					<input type='password' class="form-control" name='j_password' id='password'/>
				</div>
			</div>
			
			<div class="form-group">
			  <label class="col-xs-2 control-label" for="submit"></label>
			  <div class="col-xs-10">
			 		<input type='submit' id="submit" class="btn btn-primary" value='${message(code: "login.label", default: "login")}'/>
			  </div>
			</div>
			
			<div class="form-group">
			  <label class="col-xs-2 control-label"></label>
			  <div class="col-xs-10">
			 		<g:link controller="user" action="register">
						<g:message code="createAccount.label" default="Create Account" />
					</g:link>
			  </div>
			</div>
			
		</form>
	</div>
	
</div>
</body>
</html>
