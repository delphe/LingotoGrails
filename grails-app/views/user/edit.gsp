<%@ page import="com.lingoto.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>lingoto - <g:message code="editAccount.label" default="Edit Account" /></title>
		<r:require modules="languageJS, user"/>
	</head>
	<body>
		<h2><g:message code="editAccount.label" default="Edit Account" /></h2>

		<g:hasErrors bean="${userInstance}">
            <div class="errors">
               <g:renderErrors bean="${userInstance}" as="list" />
            </div>
        </g:hasErrors>
				
		<g:hasErrors bean="${urc}">
            <div class="errors">
               <g:renderErrors bean="${urc}" as="list" />
            </div>
        </g:hasErrors>

		<g:form action='update' name="update" method="post" class="form-horizontal">
			<g:hiddenField name="userVersion" value="${userInstance?.version}" />
			<g:hiddenField name="accountVersion" value="${userInstance?.account?.version}" />
			<g:hiddenField name="languageVersion" value="${userInstance?.account?.primaryLanguage?.version}" />
			
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
					<label for="username" class="col-md-4 control-label">
						<g:message code="email.label" default="Email" />:
					</label>
					<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'username', 'error')} required">
						<g:field type="email" name="username" required="" value="${userInstance?.username}" maxlength="100"/>
					</div>
				</div>
				
				<div class="form-group">
						<label for="currentPassword" class="col-md-4 control-label">
							<g:message code="currentPassword.label" default="Current Password" />:
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'currentPassword', 'error')} required">
							<g:passwordField name="currentPassword" value="${currentPassword}" maxlength="20"/>
						</div>
					</div>
				
					<div class="form-group">
						<label for="firstName" class="col-md-4 control-label">
							<g:message code="firstName.label" default="First Name" />:
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'firstName', 'error')} required">
							<g:textField name="firstName" required="" value="${userInstance?.account?.firstName}" maxlength="100"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="lastName" class="col-md-4 control-label">
							<g:message code="lastName.label" default="Last Name" />:
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'lastName', 'error')} required">
							<g:textField name="lastName" required="" value="${userInstance?.account?.lastName}" maxlength="100"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="userLang" class="col-md-4 control-label">
							<g:message code="language.label" default="Language" />: 
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'userLang', 'error')} required">
							<input type="text" name="userLang" id="language_textField" required="" 
								value="${userInstance?.account?.primaryLanguage?.lingo}" maxlength="100"/>
						</div>
					</div>
					<div class="form-group">
					    <label for="dialect" class="col-md-4 control-label">
					    	<g:message code="dialect.label" default="Dialect" />: 
					    </label>
					    <div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'dialect', 'error')}">
						    <input type="text" name="dialect" id="dialect_textField" autocomplete='nope'
								value="${userInstance?.account?.primaryLanguage?.dialect}" maxlength="100"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="age" class="col-md-4 control-label">
							<g:message code="age.label" default="Age" />:	
						</label>
						<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'age', 'error')} required">
							<g:select name="age" class="age" required=""
							            	from="${['< 13', '13', '14', '15', '16', '17', '18+']}"
							            	keys="${['12', '13', '14', '15', '16', '17', '18']}"
						         			value="${userInstance?.account?.age}"/>
						    <sec:ifAnyGranted roles="ROLE_ADMIN">
							    <i>
							    	<g:message code="allowAdultContent.label" default="Allow Adult Content" />: 
							    	<g:checkBox name="adultContent" value="${userInstance?.account?.adultContent}" />
							    </i>
						    </sec:ifAnyGranted>
						</div>
					</div>
					
					
					
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
								<g:passwordField name="password" value="${password}" maxlength="20"/>
							</div>
						</div>
			            
			            <div class="form-group fieldcontain ${hasErrors(bean: urc, field: 'passwordRepeat', 'error')}">
							<label for="passwordRepeat" class="col-md-4 control-label">
								<g:message code="retypePassword.label" default="Retype Password" />:
							</label>
							<div class="col-md-6 fieldcontain ${hasErrors(bean: urc, field: 'password', 'error')}">
								<g:passwordField name="passwordRepeat" value="${passwordRepeat}" maxlength="20"/>
							</div>
						</div>
					</div>
	
				</div>
				<div class="col-md-6" style="overflow:scroll; height:320px; border:1px solid;">
					${termsOfService}
				</div>
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
