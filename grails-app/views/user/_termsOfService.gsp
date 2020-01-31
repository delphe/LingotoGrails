<div class="col-md-6" style="overflow:scroll; height:320px; border:1px solid;">
	<g:if test="${grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")}">
		<p class="red">
			This "beta" version of Lingoto is intended for trial use only. 
			Content submitted/uploaded on this beta version may or may not carry over to lingoto.com.
			The following terms of service are subject to change. 
		</p>
	</g:if>
	${termsOfService}
</div>