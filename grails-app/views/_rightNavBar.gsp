<div class="row row-centered">
	<div class="col-md-12 col-centered">
	<g:link controller="lesson" action="teach">
		<div class="imgLink" style="text-align:center;">
			<img class="img-responsive" src="${createLinkTo(dir:'images',file:'teacher.png')}" width="120"/>
			<g:message code="teach.label" default="Teach" />
		</div>
	</g:link>
	</div>
	<g:if test="${vLcount && vLcount > 3}">
		<div class="col-md-12 col-centered">
			<g:link controller="lesson" action="quiz" params="[langId:lesson?.masterLang?.id]">
				<div class="imgLink" style="text-align:center;">
					<img class="img-responsive" src="${createLinkTo(dir:'images',file:'questionMark.png')}" width="100">
					<g:message code="quiz.label" default="Quiz" />
				</div>
			</g:link>
		</div>
	</g:if>
	<div class="col-md-12 col-centered">
		<g:link controller="lesson" action="index">
			<div class="imgLink" style="text-align:center;">
				<img class="img-responsive" src="${createLinkTo(dir:'images',file:'student.png')}" width="110">
				<g:message code="learn.label" default="Learn" />
			</div>
		</g:link>
	</div>
</div>

<g:if test="${session.creditsForTranslation}">
	<div class="row">
		<div class="col-xs-9">
			<br />
			<div class="alert alert-warning col-centered">
				<g:message code="earnCredits.message" args="${[session.creditsForTranslation]}"
					default="Earn ${session.creditsForTranslation} Credits!" /> <br />
					&#43;${session.creditsForTranslation}<br />
				<g:link controller="translatedMessage" action="index">
					<g:message code="translateThisSite.message"
						default="Translate This Site" />
				</g:link>
			</div>
		</div>
	</div>
</g:if>