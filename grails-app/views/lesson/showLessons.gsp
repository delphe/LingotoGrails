<div class="row">
	<div class="col-md-12">
		<g:if test="${error}">
			<div class="error">
				<g:message code="${error}" />
			</div>
		</g:if>
		<g:elseif test="${authors.size() == 0}">
		<br/>
			<g:message code="nothingFound.message" default="Nothing found." />
		</g:elseif>
		<g:else>
			<g:each in="${authors}" var="author">
			<%-- authors.value contains	[firstname, userId, rating]--%>
				
				<div class="col-md-2 lessonPlan">
					<g:link action="lessonPlan" params="[auth: author[1], 
						beginner:params.beginner,
						intermediate:params.intermediate,
						advanced:params.advanced,
						formal:params.formal,
						informal:params.informal]">
						${author[0]}
					</g:link>
				</div>
				<div class="col-md-10">
					<g:if test="${author[2]}">
						<g:each in="${ (0..< author[2]) }">
							<span class="ratingGold">&#9733;</span>
						</g:each>
					</g:if>
				</div>
				
			</g:each>
		</g:else>
	</div>
</div>