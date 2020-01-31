<li class="dropdown">
	<a class="dropdown-toggle" data-toggle="dropdown" href="#">
   		<i class="icon-wrench"></i>
		<g:message code="default.admin.label"/><b class="caret"></b>
	</a>
	<ul class="dropdown-menu">
		<li class="">
			<a tabindex="-1" href="#"><b>Technical Admin</b></a>
		</li>
		<g:if env="development">
		<li class="">
			<a href="${createLink(uri: '/dbconsole')}">
				<i class="icon-dashboard"></i>
				<g:message code="default.dbconsole.label"/>
			</a>
		</li>
		</g:if>
		<li class="">
			<a href="${createLink(uri: '/siteinfo/systeminfo')}">
				<i class="icon-info-sign"></i>
				<g:message code="default.systeminfo.label"/>
			</a>
		</li>
<%--		<li><g:link controller="MasterLang">Master Language Controller</g:link></li>--%>
	</ul>
</li>

