<html>
    <head>
        <title><g:if test="${grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")}"><g:message code="default.title.label" default="lingoto"/>-beta</g:if>
        	<g:else>
        		<g:message code="default.title.label" default="lingoto"/>
        	</g:else> - <g:message code="teach.label" default="Teach" /></title>
        <meta name="layout" content="main"/>
        <r:require modules="languageJS"/>
        <r:require modules="lesson"/>
        <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'inlineplayer.css')}"/>
		<script type="text/javascript" src="/js/soundmanager2.js"></script>
		<script type="text/javascript" src="/js/inlineplayer.js"></script>
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
        
        	<errors:errorList bean="${lesson}"/>
	        
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
						<msg:categoryDropDown name="category" class="form-control"
						value="${lesson?.category}" noSelection="${['':'']}"/>
					</div>
				</div>
				
				<div class="form-group">
					<g:if test="${!lesson?.informal}">
						<g:set var="formal" value="true" />
					</g:if>
					<label class="col-md-3 control-label">
				    	<g:message code="formal.label" default="Formal" />:
					<g:radio name="informal" value="false"
						checked="${formal}" />
					</label>
					<label class="col-md-3 control-label">
						<g:message code="informal.label" default="Informal/Slang" />:
					<g:radio name="informal" value="true"
						checked="${lesson?.informal}" />
					</label>
				</div>	
				</br>
				<div class="form-group">
				<g:if test="${lesson.imagePath && lessonImage}">
			        <div class="col-md-4" id="editableImgDiv" style="height:90px; width:150px;">
			        	<img class="absoluteImg" 
			        		src="${application.contextPath}/${lesson.imagePath}/${lessonImage}" height="90"/>
						<a href="#" class="editImg" id="editImg" onclick="showImgInput()">
							<span class="glyphicon glyphicon-pencil" style="color:#1F3E73" aria-hidden="true"></span><span class="sr-only">Edit</span>
						</a>
					</div>
					<div class="col-md-4 hideit" id="imgInputDiv">
						<div class="fieldcontain ${hasErrors(bean: lesson, field: 'imgPayload', 'error')}">
					        <img id="cameraIconImg" src="${resource(dir:'images',file:'cameraIcon.png')}" alt="" border="0" align="center"/>
							<input type="file" id="imgPayload" name="imgPayload" class="form-control" accept="image/*"/>
							<div id="uploadingImg" style="display:none;">
							    <img src="${createLinkTo(dir:'images',file:'uploading.gif')}" />
							</div>
						</div>
					</div>
				</g:if>	
				<g:if test="${lesson?.audioPath && lesson?.originalAudioName}">
					<div class="col-md-4" id="editableAudioDiv" style="height:40px; width:150px;">
						<a href="#" class="editImg" id="editAudio" onclick="showAudioInput()">
							<span class="glyphicon glyphicon-pencil" style="color:#1F3E73" aria-hidden="true"></span><span class="sr-only">Edit</span>
						</a>
						<ul class="graphic">
						  <li><a href="${application.contextPath}/${lesson?.audioPath}/${lesson?.id}.mp3" 
							style="position:relative; left:40px" >
							${lesson?.originalAudioName} </a></li>
						 </ul>
						
					</div>
					<div class="col-md-4 hideit" id="audioInputDiv">
						<div class="fieldcontain ${hasErrors(bean: lesson, field: 'originalAudioName', 'error')}">
							<img id="micIconImg" src="${resource(dir:'images',file:'micIcon.png')}" alt="" border="0" align="center"/>
							<input type="file" id="audio" name="audio" class="form-control" accept="audio/*"/>
							<div id="uploadingAudio" style="display:none;">
							    <img src="${createLinkTo(dir:'images',file:'uploading.gif')}" />
							</div>
						</div>
					</div>
			       </g:if>
			       <g:else>
			       	<div class="col-md-4" id="audioInputDiv">
						<div class="fieldcontain ${hasErrors(bean: lesson, field: 'originalAudioName', 'error')}">
							<img src="${resource(dir:'images',file:'micIcon.png')}" alt="" border="0" align="center"/>
							<input type="file" name="audio" class="form-control" accept="audio/*"/>
							<div id="uploadingAudio" style="display:none;">
							    <img src="${createLinkTo(dir:'images',file:'uploading.gif')}" />
							</div>
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
