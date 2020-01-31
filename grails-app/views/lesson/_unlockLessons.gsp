<b class="red"><g:message code="unlockLessons.message" default="Unlock more lessons!" /></b>
<ul>
	<sec:ifAnyGranted roles="ROLE_GUESTUSER">
		<li>
			<g:link controller="user" action="edit"><g:message code="createAccount.label" default="Create Account" /></g:link>
		</li>
	</sec:ifAnyGranted>
	<sec:ifNotGranted roles="ROLE_GUESTUSER">
<%--	<li><g:message code="purchaseCredits.message" default="Purchase credits" /></li>--%>
		<li><g:link controller="lesson" action="lessonPlan"
							params="[translate:true,filteredLessonByAuthorList:filteredLessonByAuthorList,
									beginner:params.beginner,
									intermediate:params.intermediate,
									advanced:params.advanced,
									formal:params.formal,
									informal:params.informal]">
			<g:message code="translate.label" default="Translate" />
				&#8594; ${session.usersLanguage} 
				<g:if test="${session.usersDialect}">
					(${session.usersDialect})
				</g:if>
			</g:link>
		</li>
		<li><g:link controller="lesson" action="teach">
				<g:message code="teach.label" default="Teach" /> 
			</g:link>
		</li>
		<g:if test="${session.creditsForTranslation}">
			<li>
				<g:link controller="translatedMessage" action="index">
					<g:message code="translateThisSite.message"
						default="Translate This Site" />
				</g:link>
			</li>
		</g:if>
	</sec:ifNotGranted>
</ul>