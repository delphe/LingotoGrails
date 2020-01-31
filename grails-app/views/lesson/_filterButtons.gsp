<g:form action="filterLessons" name="rightNavFilterForm">    
	<g:hiddenField name="lessonId" value="${lesson?.id}" />
	<g:hiddenField name="offset" value="${params.offset}" />
	<div class="collapse navbar-collapse col-md-5" id="navbar-ex2-collapse">
		<div class="btn-group" data-toggle="buttons">
		        <label class="btn btn-default active">
		            <input type="checkbox" name="beginner" id="beginner" value="${beginner}" checked="${beginner}">
		            	<g:message code="category.beginner.label" default="Beginner" />
		        </label>
		        <label class="btn btn-default">
		            <input type="checkbox" name="intermediate" id="intermediate" value="${intermediate}" checked="${intermediate}">
						<g:message code="category.intermediate.label" default="Intermediate" />
		        </label>
		        <label class="btn btn-default">
		            <input type="checkbox" name="advanced" id="advanced" value="${advanced}" checked="${advanced}">
		            	<g:message code="category.advanced.label" default="Advanced" />
		        </label>
	    </div>
	 </div>
	 <div class="collapse navbar-collapse col-md-3" id="navbar-ex3-collapse">
	    <div class="btn-group" data-toggle="buttons">
	        <label class="btn btn-default active">
	            <input type="checkbox" name="formal" id="formal" value="${formal}" checked="${formal}">
	            	<g:message code="formal.label" default="Formal" />
	        </label>
	        <label class="btn btn-default active">
	            <input type="checkbox" name="informal" id="informal" value="${informal}" checked="${informal}">
	            	<g:message code="informal.label" default="Informal/Slang" />
	        </label>
	    </div>
	</div>

</g:form>