<h2><g:message code="wordPhrase.label" default="Word/Phrase" /></h2>

<g:if test="${lessons?.size() == 0 || !lessons}">
	<p class="red"><g:message code="nothingFound.message" default="Nothing found." /></p>
</g:if>
<g:else>
<div class="container-fluid">
	<div class="row">
		<div id="lesson-list" class="col-sm-6">
			<ul class="sortable-list">
				<g:each in="${lessons}" status="i" var="lessonInstance">
					<li class="sortable-item" id="${lessonInstance.sortOrder}" style="list-style-type:none">
					<table>
						<tr>
							<td>
								<g:link action="learn" id="${lessonInstance.id}">
									${fieldValue(bean: lessonInstance, field: "wordPhrase")}
								</g:link>
							</td>
							<td class="floatright">
								<img class="up-arrow" src="${createLinkTo(dir:"images/skin", file:"sorted_asc.gif")}" />
								<img class="down-arrow" src="${createLinkTo(dir:"images/skin", file:"sorted_desc.gif")}" />
							</td>
						</tr>
					</table>
					</li>
				</g:each>
			</ul>
		</div>
		<div class="col-sm-4">
			<p id="save-order-buttons">
				<button id="save-order" name="save-order" class="btn btn-primary" style="width: 78px !important;">
			    	<span class="glyphicon glyphicon-save" aria-hidden="true"></span><span class="sr-only">Save</span>&nbsp;
			    </button>
			    <button id="cancel-save-order" name="cancel-save-order" class="btn btn-danger" style="width: 78px !important;">
			    	<span class="glyphicon glyphicon-ban-circle" aria-hidden="true"></span><span class="sr-only">Cancel</span>&nbsp;
			    </button>
		   </p>
			<g:set var="usersLessonPlanURL" value="${createLink(action:'lessonPlan', absolute:true)}/?auth=${user.id}" />
			<a HREF="mailto:?subject=Lingoto - ${message(code:'lessonList.label', default:'Lesson List')}
				&body=${message(code:'lessonCreatedBy.label', default:'Lesson created by')}: ${user?.account?.firstName} ${user?.account?.lastName}
				 - ${message(code:'lessonList.label', default:'Lesson List')}: ${usersLessonPlanURL}" 
				class="btn btn-info">
			<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span></a>			
         	<g:textField name="lessonPlanURL" class="form-control" value="${usersLessonPlanURL}"/>
		</div>
	</div>
	
</div>

<ul class="pagination pagination-md">
	<li>
	    <util:remotePaginate controller="lesson" action="selectedList" params="${[category:params?.category]}" 
	    	total="${lessons?.totalCount}" update="sortableList" max="20" pageSizes="[20:' <= 20', 50:' <= 50', 100:' <= 100', 1000:' <= 1000']"/>
	</li>
</ul>
</g:else>
<br />
<br />