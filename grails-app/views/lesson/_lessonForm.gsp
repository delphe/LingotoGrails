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
		
		<div class="col-md-4 fieldcontain ${hasErrors(bean: lesson, field: 'imgPayload', 'error')}">
	        <img id="cameraIconImg" src="${resource(dir:'images',file:'cameraIcon.png')}" alt="" border="0" align="center"/>
			<input type="file" id="imgPayload" name="imgPayload" class="form-control" accept="image/*"/>
			<div id="uploadingImg" style="display:none;">
			    <img src="${createLinkTo(dir:'images',file:'uploading.gif')}" />
			</div>
		</div>
		<div class="col-md-4 fieldcontain ${hasErrors(bean: lesson, field: 'originalAudioName', 'error')}">
			<img id="micIconImg" src="${resource(dir:'images',file:'micIcon.png')}" alt="" border="0" align="center"/>
			<input type="file" id="audio" name="audio" class="form-control" accept="audio/*"/>
			<div id="uploadingAudio" style="display:none;">
			    <img src="${createLinkTo(dir:'images',file:'uploading.gif')}" />
			</div>
		</div>
		<%--
		<div class="col-md-4 fieldcontain ${hasErrors(bean: lesson, field: 'originalVideoName', 'error')}">
			<img src="${resource(dir:'images',file:'camcorderIcon.png')}" alt="" border="0" align="center"/>
			<input type="file" name="video" class="form-control" accept="video/*"/>
		</div>
		--%>
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
	
	<g:hiddenField name="id" value="${lesson?.id}" />
    <g:hiddenField name="active" value="true" />
    
    <div class="form-group">
	  <label class="col-md-2 control-label" for="teach"></label>
	  <div class="col-md-4">
	    <button id="teach" name="teach" class="btn btn-primary" form="teachForm" style="width: 78px !important;">
	    	<span class="glyphicon glyphicon-save" aria-hidden="true"></span><span class="sr-only">Save</span>&nbsp;
	    </button>
	  </div>
	</div>
	
	<div class="form-group">
	  <div class="col-md-4">
	    <g:link controller="lesson" action="list" class="btn btn-success">
			<span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> <g:message code="lessonList.label" default="Lesson List" /></g:link>
	  </div>
	</div>
	
</div>
