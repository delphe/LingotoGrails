<ul class="pagination pagination-lg">					
	<li>
		<g:if test="${!nextView || nextView == 'translateMessages'}">
			<span class="currentStep">1</span>
		</g:if>
		<g:else>
			<g:link action="update" params="[nextView:'index']">1</g:link>
		</g:else>
	</li>
	
	<li>
		<g:if test="${nextView == 'termsOfService'}">
			<span class="currentStep">2</span>
		</g:if>
		<g:else>
			<g:link action="update" params="[nextView:'translateMessages']">2</g:link>
		</g:else>
	</li>
	<li>
		<g:if test="${nextView == 'index'}">
			<span class="currentStep">3</span>
		</g:if>
		<g:else>
			<g:link action="update" params="[nextView:'termsOfService']">3</g:link>
		</g:else>
	</li>
	<li><g:link action="update" params="[nextView:nextView ?: 'translateMessages']">
		<span aria-hidden="true">&raquo;</span><span class="sr-only">Next</span>
	</g:link></li>
</ul>
