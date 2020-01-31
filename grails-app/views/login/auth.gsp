<html>
<head>
	<meta name='layout' content='noNav'/>
	<title>lingoto</title>
	
</head>

<body>

<div class="jumbotron" id='login'>
	<div class="form-horizontal">
		<div class='fheader'>
		</div>

		<g:if test='${flash.message}'>
			<div class='login_message'>${flash.message}</div>
		</g:if>
		<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
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
			  <label class="col-md-2 control-label" for="submit"></label>
			  <div class="col-md-4">
			 		<input type='submit' id="submit" class="btn btn-primary" value='${message(code: "login.label", default: "login")}'/>
			 
			  </div>
			</div>
			
			<div class="form-group">
			  <label class="col-md-2 control-label"></label>
			  <div class="col-md-4">
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
