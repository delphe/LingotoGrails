<html>
    <head>
        <title>lingoto</title>
        <meta name="layout" content="main" />
    </head>
    <body>
	    <g:if test="${session.creditsForTranslation}">
	    	<g:uploadForm action="update" name="updateForm">
	    		<h2><g:message code="translate.label" default="Translate" /> &#8594; 
	    			${session.usersLanguage}<g:if test="${session.usersDialect}">
					(${session.usersDialect})</g:if>
				</h2>
				<g:if test="${nextView}">
					<g:render template="messagePagination"/>
				</g:if>
				<div class="form-horizontal">
					<g:each in="${messages}" status="i" var="message">
						<div class="form-group">
							<label for="message-${i}" class="col-md-6 control-label">
									${message.text}
							</label>
							<g:each in="${translatedMessages}" status="index" var="translatedMessage">
			    				<g:if test="${message.id == translatedMessage.message.id}">
			    					<g:set var="translatedMsg" value="${translatedMessage.text}" />
			    				</g:if>
			    			</g:each>
			    			<div class="col-md-3">
			    				<g:textField class="form-control" id="message-${i}" name="${message.id}" value="${translatedMsg}" maxlength="255"/>
			    			</div>
			    			<g:set var="translatedMsg" value="" />
						</div>
					</g:each>
				</div>
		    	<g:hiddenField name="nextView" value="${nextView ?: 'translateMessages'}" />
		    	
				<div class="form-group">
				  <label class="col-md-6 control-label" for="save"></label>
				  <div class="col-md-3">
				    <button id="save" name="save" class="btn btn-primary" form="updateForm" style="width: 78px !important;">
				    	<span class="glyphicon glyphicon-save" aria-hidden="true"></span><span class="sr-only">Save</span>&nbsp;
				    </button>
				  </div>
				</div>
				<g:render template="messagePagination"/>
				<br />
				<br />&nbsp;
			</g:uploadForm>
		</g:if>
		<g:else>
			<div class="jumbotron" id='login'>
				<div class="alert alert-success">
					<h2>
			    		<g:message code="thanksForTranslating.message" default="Thank you for your translations!" />
			    	</h2>
		    	</div>
	    	</div>
		</g:else>
    </body>
</html>
