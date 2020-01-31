<html>
    <head>
        <title>lingoto - <g:message code="teach.label" default="Teach" /></title>
        <meta name="layout" content="main"/>
        <r:require modules="languageJS"/>
        <r:require modules="lesson"/>
        <sm:inlinePlayer/>
    </head>
    <body>
		<g:uploadForm action="update" name="editForm">
        
        	
       		<div class="fieldcontain ${hasErrors(bean: lesson, field: 'masterLang', 'error')} required">
        		<h2>
		        	<g:message code="language.label" default="Language" />:
		        	<g:textField name="lingo" id="language_textField" required="" 
		        		value="${lesson?.masterLang?.lingo}"/>
	        	</h2>
        	</div>
        
	        <h3>
	        	<g:message code="dialect.label" default="Dialect" />:
	        	<g:textField name="dialect" id="dialect_textField" value="${lesson?.masterLang?.dialect}"/>
        	</h3>
        
	        <g:hasErrors bean="${lesson}">
	            <div class="errors">
	               <g:renderErrors bean="${lesson}" as="list" />
	            </div>
	        </g:hasErrors>
	        
	        <g:if test="${flash.message}">
		        <div class="error">
		        	<g:message code="${flash.message}"/>
		        </div>
	        </g:if>
	        
			
			<g:hiddenField name="id" value="${lesson?.id}" />
	        <g:hiddenField name="version" value="${lesson?.version}" />
        	<g:hiddenField name="active" value="true" />
        
			 <div class="form-horizontal">
				<div class="form-group">
			   		<label for="wordPhrase" class="col-md-2 control-label">
			   			<g:message code="wordPhrase.label" default="Word/Phrase" />: 
			   		</label>
			   		<div class="col-md-3 fieldcontain ${hasErrors(bean: lesson, field: 'wordPhrase', 'error')} required">
				   		<g:textField name="wordPhrase" class="form-control" value="${lesson?.wordPhrase}" maxlength="100"/> &nbsp;&nbsp;&nbsp; 
				   	</div>
			
				   	<label for="category" class="col-md-2 control-label">
						<g:message code="level.label" default="Level" />: 
					</label>
					<div class="col-md-3 fieldcontain ${hasErrors(bean: lesson, field: 'category', 'error')} required">
						<g:categoryDropDown name="category" class="form-control"
						value="${lesson?.category}" noSelection="${['':'']}"/>
					</div>
				</div>
				
				<div class="form-group">
					<g:if test="${!lesson?.informal}">
						<g:set var="formal" value="true" />
					</g:if>
					<label class="col-md-2 control-label">
				    	<g:message code="formal.label" default="Formal" />:
					<g:radio name="informal" value="false"
						checked="${formal}" />
					</label>
					<label class="col-md-2 control-label">
						<g:message code="informal.label" default="Informal/Slang" />:
					<g:radio name="informal" value="true"
						checked="${lesson?.informal}" />
					</label>
				</div>	
				
				<div class="form-group">
				<g:if test="${lesson.imagePath && lesson.imageName}">
			        <div id="editableImgDiv" style="height:90px; width:150px;">
			        	<img class="absoluteImg" 
			        		src="${application.contextPath}/${lesson.imagePath}/${lesson.imageName}" height="90"/>
						<a href="#" class="editImg" id="editImg" onclick="showImgInput()">
							<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span><span class="sr-only">Edit</span>
						</a>
					</div>
					<div class="hideit" id="imgInputDiv">
						<div class="col-md-3 fieldcontain ${hasErrors(bean: lesson, field: 'imgPayload', 'error')}">
					        <img src="${resource(dir:'images',file:'cameraIcon.gif')}" alt="" border="0" align="center"/>
							<input type="file" id="imgPayload" name="imgPayload" class="form-control" accept="image/*"/>
						</div>
					</div>
				</g:if>	
				<g:if test="${lesson?.audioPath && lesson?.originalAudioName}">
					<div id="editableAudioDiv" style="height:40px; width:150px;">
						<a href="#" class="editImg" id="editAudio" onclick="showAudioInput()">
							<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span><span class="sr-only">Edit</span>
						</a>
						<a href="${application.contextPath}/${lesson?.audioPath}/${lesson?.id}.mp3" 
							style="position:relative; left:40px" >
							${lesson?.originalAudioName} </a>
					</div>
					<div class="hideit" id="audioInputDiv">
						<div class="col-md-3 fieldcontain ${hasErrors(bean: lesson, field: 'originalAudioName', 'error')}">
							<img src="${resource(dir:'images',file:'micIcon.gif')}" alt="" border="0" align="center"/>
							<input type="file" name="audio" value="test.jpg" class="form-control" accept="audio/*"/>
						</div>
					</div>
			       </g:if>
			       <g:else>
			       	<div id="audioInputDiv">
						<div class="col-md-3 fieldcontain ${hasErrors(bean: lesson, field: 'originalAudioName', 'error')}">
							<img src="${resource(dir:'images',file:'micIcon.gif')}" alt="" border="0" align="center"/>
							<input type="file" name="audio" value="test.jpg" class="form-control" accept="audio/*"/>
						</div>
					</div>
			       </g:else>
			     </div>
				
				<div class="form-group">
					<label for="additionalInfo" class="col-md-2 control-label">
						<g:message code="additionalInfo.label" default="Additional Information" />  
						(<g:message code="optional.label" default="optional" />): 
					</label>
					<div class="col-md-6 fieldcontain ${hasErrors(bean: lesson, field: 'additionalInfo', 'error')}">
			        	<g:textArea name="additionalInfo" value="${lesson?.additionalInfo}" class="form-control" maxlength="190"/>
			        </div>
				</div>
				
				<sec:ifAnyGranted roles="ROLE_ADMIN">
					<div class="form-group">
						<label for="ageRestriction" class="col-md-2 control-label">
							<g:message code="ageRestriction.label" default="Age Restriction" />:
						</label>
						<div class="col-md-2 fieldcontain ${hasErrors(bean: lesson, field: 'ageRestriction', 'error')}">
						    <g:select name="ageRestriction" class="form-control"
						    	from="${['13+', '14+', '15+', '16+', '17+', '18+']}"
						    	keys="${[13, 14, 15, 16, 17, 18]}"
									value="${lesson?.ageRestriction}"
									noSelection="${[12:'0']}" />
						</div>
					</div>
				</sec:ifAnyGranted>
			    
			    <div class="form-group">
				  <label class="col-md-2 control-label" for="teach"></label>
				  <div class="col-md-4">
				    <button id="update" name="update" class="btn btn-primary" form="editForm" style="width: 78px !important;">
				    	<span class="glyphicon glyphicon-save" aria-hidden="true"></span><span class="sr-only">Save</span>&nbsp;
				    </button>
				  </div>
				</div>
				
			</div>
        </g:uploadForm>
        
    </body>
</html>
