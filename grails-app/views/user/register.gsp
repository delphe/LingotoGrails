<html>
    <head>
        <title>lingoto - <g:message code="createAccount.label" default="Create Account" /></title>
        <meta name="layout" content="noNav"/>
        <r:require modules="languageJS, user"/>
    </head>
    <body>
        <h1><g:message code="createAccount.label" default="Create Account" /></h1><br/>
		
		<g:hasErrors bean="${userInstance}">
            <div class="errors">
               <g:renderErrors bean="${userInstance}" as="list" />
            </div>
        </g:hasErrors>
        
        <g:form action='saveAccount' name='register' class="form-horizontal">
        
        <div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label for="username" class="col-md-4 control-label">
						<g:message code="email.label" default="Email" />:
					</label>
					<div class="col-md-6 fieldcontain ${hasErrors(bean: userInstance, field: 'username', 'error')} required">
						<g:field type="email" name="username" required="" value="${userInstance?.username}" maxlength="100"/>
					</div>
				</div>
				
				<div class="form-group">
					<label for="password" class="col-md-4 control-label">
						<g:message code="password.label" default="Password" />:
					</label>
					<div class="col-md-6 fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
						<g:passwordField name="password" required="" value="${userInstance?.password}" maxlength="20"/>
					</div>
				</div>
	            
	            <div class="form-group">
					<label for="passwordRepeat" class="col-md-4 control-label">
						<g:message code="retypePassword.label" default="Retype Password" />:
					</label>
					<div class="col-md-6 fieldcontain ${hasErrors(bean: userInstance, field: 'passwordRepeat', 'error')} required">
						<g:passwordField name="passwordRepeat" required="" value="${userInstance?.passwordRepeat}" maxlength="20"/>
					</div>
				</div>
	
				<div class="form-group">
					<label for="firstName" class="col-md-4 control-label">
						<g:message code="firstName.label" default="First Name" />:
					</label>
					<div class="col-md-6 fieldcontain ${hasErrors(bean: userInstance, field: 'firstName', 'error')} required">
						<g:textField name="firstName" required="" value="${userInstance?.firstName}" maxlength="100"/>
					</div>
				</div>
				
				<div class="form-group">
					<label for="lastName" class="col-md-4 control-label">
						<g:message code="lastName.label" default="Last Name" />:
					</label>
					<div class="col-md-6 fieldcontain ${hasErrors(bean: userInstance, field: 'lastName', 'error')} required">
						<g:textField name="lastName" required="" value="${userInstance?.lastName}" maxlength="100"/>
					</div>
				</div>
				
				<div class="form-group">
					<label for="userLang" class="col-md-4 control-label">
						<g:message code="language.label" default="Language" />: 
					</label>
					<div class="col-md-6 fieldcontain ${hasErrors(bean: userInstance, field: 'userLang', 'error')} required">
						<input type="text" name="userLang" id="language_textField" maxlength="100" required="" 
							value="${userInstance?.userLang}" />
					</div>
				</div>
				<div class="form-group">
					<label for="dialect" class="col-md-4 control-label">
				    	<g:message code="dialect.label" default="Dialect" />: 
				    </label>
				    <div class="col-md-6 fieldcontain ${hasErrors(bean: userInstance, field: 'dialect', 'error')}">
					    <input type="text" name="dialect" id="dialect_textField" maxlength="100"
							value="${userInstance?.dialect}" />
					</div>
				</div>
				
				<div class="form-group">
					<label for="age" class="col-md-4 control-label">
						<g:message code="age.label" default="Age" />:	
					</label>
					<div class="col-md-6 fieldcontain ${hasErrors(bean: userInstance, field: 'age', 'error')} required">
						<g:select name="age" class="age" required=""
						            	from="${['< 13', '13', '14', '15', '16', '17', '18+']}"
						            	keys="${['12', '13', '14', '15', '16', '17', '18']}"
					         			value="${userInstance?.age}"
					         			noSelection="${['':'']}" />
					    <sec:ifAnyGranted roles="ROLE_ADMIN">
						    <i>
								<g:message code="allowAdultContent.label" default="Allow Adult Content" />: 
								<g:checkBox name="adultContent" value="${userInstance?.adultContent}" />
							</i>
						</sec:ifAnyGranted>
					</div>
				</div>
			</div>
			<div class="col-md-6" style="overflow:scroll; height:320px; border:1px solid;">
				${termsOfService}
			</div>
		</div>
	        
	       		
             <g:hiddenField name="enabled" value="true" />
             
	        <div class="form-group">
			  <label class="col-md-2 control-label" for="register"></label>
			  <div class="col-md-4">
			    <button id="register" name="register" class="btn btn-primary" form='register' style="width: 78px !important;">
			    	<span class="glyphicon glyphicon-save" aria-hidden="true"></span><span class="sr-only">Save</span>&nbsp;
			    </button>
			  </div>
			</div>
        
        </g:form>
		
		<div id="successDiv">
			<script> var redirectUrl = "${createLink(controller:'home',action:'index')}" </script>
			<g:render template="savedSuccessfully"/>
		</div>
    </body>
</html>
