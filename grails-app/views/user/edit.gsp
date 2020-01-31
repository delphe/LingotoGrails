<%@ page import="com.lingoto.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:if test="${grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")}"><g:message code="default.title.label" default="lingoto"/>-beta</g:if>
        	<g:else>
        		<g:message code="default.title.label" default="lingoto"/>
        	</g:else> - <g:message code="editAccount.label" default="Edit Account" /></title>
		<r:require modules="languageJS, user"/>
	</head>
	<body>
		<h2>
			<sec:ifAnyGranted roles="ROLE_GUESTUSER">
				<g:message code="createAccount.label" default="Create Account" />
			</sec:ifAnyGranted>
			<sec:ifNotGranted roles="ROLE_GUESTUSER">
				<g:message code="editAccount.label" default="Edit Account" />
			</sec:ifNotGranted>
		</h2>

		<errors:errorList bean="${userInstance}"/>
		<errors:errorList bean="${urc}"/>

		<g:form action='update' name="update" method="post" class="form-horizontal">
			<g:hiddenField name="userVersion" value="${userInstance?.version}" />
			<g:hiddenField name="accountVersion" value="${userInstance?.account?.version}" />
			<g:hiddenField name="languageVersion" value="${userInstance?.account?.primaryLanguage?.version}" />
			
			<sec:ifNotGranted roles="ROLE_GUESTUSER">
				<g:set var="firstName" value="${userInstance?.account?.firstName}"/>
				<g:set var="lastName" value="${userInstance?.account?.lastName}"/>
				<g:set var="lingo" value="${userInstance?.account?.primaryLanguage?.lingo}"/>
				<g:set var="dialect" value="${userInstance?.account?.primaryLanguage?.dialect}"/>
				<g:set var="age" value="${userInstance?.account?.age}"/>
			</sec:ifNotGranted>
			
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label for="username" class="col-md-4 control-label">
							<g:message code="email.label" default="Email" />:
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'username', 'error')} required">
							<sec:ifAnyGranted roles="ROLE_GUESTUSER">
								<g:field class="form-control" 
									type="email" name="username" value="${urc?.username}"/>
							</sec:ifAnyGranted>
							<sec:ifNotGranted roles="ROLE_GUESTUSER">
								<g:field class="form-control" 
									type="email" name="username" value="${urc?.username ?: userInstance?.username}" disabled="true"/>
							</sec:ifNotGranted>
						</div>
					</div>
					
					<sec:ifAnyGranted roles="ROLE_GUESTUSER">
						<div class="form-group">
							<label for="usernameRepeat" class="col-md-4 control-label">
								<g:message code="retypeEmail.label" default="Retype Email" />:
							</label>
							<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'usernameRepeat', 'error')} required">
								<g:field class="form-control"
									type="email" name="usernameRepeat" value="" maxlength="100"/>
							</div>
						</div>
					</sec:ifAnyGranted>
				
					<sec:ifNotGranted roles="ROLE_GUESTUSER">
						<div class="form-group">
							<label for="currentPassword" class="col-md-4 control-label" style="color:green">
								<g:message code="currentPassword.label" default="Current Password" />:
							</label>
							<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'currentPassword', 'error')} required">
								<g:passwordField class="form-control"
									name="currentPassword" value="" maxlength="20"/>
							</div>
						</div>
					</sec:ifNotGranted>
					<sec:ifAnyGranted roles="ROLE_GUESTUSER">
						<div class="form-group">
							<label for="password" class="col-md-4 control-label">
								<g:message code="password.label" default="Password" />:
							</label>
							<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'password', 'error')} required">
								<g:passwordField class="form-control"
									name="password" value="${urc?.password}" maxlength="20"/>
							</div>
						</div>
			            
			            <div class="form-group">
							<label for="passwordRepeat" class="col-md-4 control-label">
								<g:message code="retypePassword.label" default="Retype Password" />:
							</label>
							<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'passwordRepeat', 'error')} required">
								<g:passwordField class="form-control"
									name="passwordRepeat" value="${urc?.passwordRepeat}" maxlength="20"/>
							</div>
						</div>
					</sec:ifAnyGranted>
					
					<div class="form-group">
						<label for="firstName" class="col-md-4 control-label">
							<g:message code="firstName.label" default="First Name" />:
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'firstName', 'error')} required">
							<g:textField class="form-control"
								name="firstName" required="" value="${urc?.firstName ?: firstName}" maxlength="100"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="lastName" class="col-md-4 control-label">
							<g:message code="lastName.label" default="Last Name" />:
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'lastName', 'error')} required">
							<g:textField class="form-control"
								name="lastName" required="" value="${urc?.lastName ?: lastName}" maxlength="100"/>
						</div>
					</div>
						
					<div class="form-group">
						<label for="userLang" class="col-md-4 control-label">
							<g:message code="yourLanguage.label" default="Your Language" />: 
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'userLang', 'error')} required">
							<input class="form-control"
								type="text" name="userLang" id="language_textField" required="" 
								value="${urc?.userLang ?: userInstance?.account?.primaryLanguage?.lingo}" maxlength="100"/>
						</div>
					</div>
					<div class="form-group">
					    <label for="dialect" class="col-md-4 control-label">
					    	<g:message code="dialect.label" default="Dialect" />: 
					    </label>
					    <div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'dialect', 'error')}">
						    <input class="form-control"
						    	type="text" name="dialect" id="dialect_textField" autocomplete='nope'
								value="${urc?.dialect ?: userInstance?.account?.primaryLanguage?.dialect}" maxlength="100"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="age" class="col-md-4 control-label">
							<g:message code="age.label" default="Age" />:	
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'age', 'error')} required">
							<g:select class="form-control age"
										name="age" required=""
						            	from="${['< 13', '13', '14', '15', '16', '17', '18+']}"
						            	keys="${['12', '13', '14', '15', '16', '17', '18']}"
					         			value="${urc?.age ?: age}"
					         			noSelection="${['':'']}"/>
						    <sec:ifAnyGranted roles="ROLE_ADMIN">
							    <i>
							    	<g:message code="allowAdultContent.label" default="Allow Adult Content" />: 
							    	<g:checkBox name="adultContent" value="${userInstance?.account?.adultContent}" />
							    </i>
						    </sec:ifAnyGranted>
						</div>
					</div>
					
					<sec:ifNotGranted roles="ROLE_GUESTUSER">
						<div class="form-group">
							<label for="updatePassword" class="col-md-4 control-label">
								<g:message code="updatePassword.label" default="Update Password" />
							</label>
							<div class="col-md-3">
						   		<g:checkBox name="updatePassword" value="${urc?.updatePassword}"/>
							</div>
						</div>
					
						<div id="passwordUpdate" class="hideit">
							<div class="form-group">
								<label for="password" class="col-md-4 control-label">
									<g:message code="newPassword.label" default="New Password" />:
								</label>
								<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'password', 'error')}">
									<g:passwordField class="form-control"
										name="password" value="${password}" maxlength="20"/>
								</div>
							</div>
				            
				            <div class="form-group">
								<label for="passwordRepeat" class="col-md-4 control-label">
									<g:message code="retypePassword.label" default="Retype Password" />:
								</label>
								<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'passwordRepeat', 'error')}">
									<g:passwordField class="form-control"
										name="passwordRepeat" value="${passwordRepeat}" maxlength="20"/>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label for="updateEmail" class="col-md-4 control-label">
								<g:message code="updateEmail.label" default="Update Email" />
							</label>
							<div class="col-md-3">
						   		<g:checkBox name="updateEmail" value="${urc?.updateEmail}"/>
							</div>
						</div>
						
						<div id="emailUpdate" class="hideit">
							<div class="form-group">
								<label for="username" class="col-md-4 control-label">
									<g:message code="newEmail.label" default="New Email" />:
								</label>
								<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'username', 'error')} required">
									<g:field class="form-control"
										type="email" name="username" value="${username}" maxlength="100"/>
								</div>
							</div>
				            
				            <div class="form-group">
								<label for="usernameRepeat" class="col-md-4 control-label">
									<g:message code="retypeEmail.label" default="Retype Email" />:
								</label>
								<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'usernameRepeat', 'error')}">
									<g:field class="form-control"
										type="email" name="usernameRepeat" value="${usernameRepeat}" maxlength="100"/>
								</div>
							</div>
						</div>
					</sec:ifNotGranted>
					
				</div>
				<g:render template="termsOfService"/>
			</div>
			<div class="form-group">
				<label class="col-md-2 control-label" for="updateButton"></label>
				<div class="col-md-4">
				    <button id="updateButton" name="updateButton" class="btn btn-primary" form='update' style="width: 78px !important;">
				    	<span class="glyphicon glyphicon-save" aria-hidden="true"></span><span class="sr-only">Save</span>&nbsp;
				    </button>
				</div>
			</div>
		</g:form>
		<g:form action='updateLang' name="updateLang" method="post">
		
			<div id="successDiv">
				<script> var redirectUrl = "${createLink(controller:'home',action:'index')}" </script>
				<g:render template="savedSuccessfully"/>
				<g:if test="${langUpdatedMsg}">
					<g:render template="langUpdatedModal" />
				</g:if>
			</div>

		</g:form>
	</body>
</html>
