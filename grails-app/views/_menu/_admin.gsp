<li class="dropdown">
	<a class="dropdown-toggle" data-toggle="dropdown" href="#">
   		<i class="icon-wrench"></i>
		<g:message code="default.admin.label"/><b class="caret"></b>
	</a>
	<ul class="dropdown-menu">
		<li><a href="${createLink(uri: '/siteinfo/stats')}">
				<span class="glyphicon glyphicon-stats"></span>
				Report
			</a></li>
		<li><g:link controller="lesson" action="reviewLessons" params="[mediaReview: 'true']">
			<span class="glyphicon glyphicon-film"></span>
			Review New Media</g:link></li>
		<li><g:link controller="lesson" action="reviewLessons">
			<span class="glyphicon glyphicon-eye-open"></span>
			Review New Lessons</g:link></li>
		<li class="">
			<a href="${createLink(uri: '/dbconsole')}">
				<span class="glyphicon glyphicon-tower"></span>
				<g:message code="default.dbconsole.label"/>
			</a>
		</li>
		<li class="">
			<a href="${createLink(uri: '/siteinfo/systeminfo')}">
				<i class="icon-info-sign"></i>
				<g:message code="default.systeminfo.label"/>
			</a>
		</li>
		<li><g:link controller="MasterLang" action="list">Master Language Controller</g:link></li>
		<li><g:link controller="LocaleMapper" action="list">Locale Mapper Controller</g:link></li>
	</ul>
</li>

