<%@ page import="com.lingoto.Message" %>
<g:set var="lang" value="${session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE' ?: org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).toString().substring(0,2)}"/>
<g:set var="currentURL" value="${request.forwardURI}"/>

<%--<ul class="nav secondary-nav language-dropdown pull-right">--%>
	<li class="dropdown dropdown-btn js-language-dropdown">
		<a class="dropdown-toggle" role="button" data-toggle="dropdown" data-target="#" href="javascript:;">
			<img class="" src="${resource(dir: 'images/flags',file: lang.toString()+'.png')}" />
		</a>
		<ul class="dropdown-menu dropdown-menu-dark" role="menu">

			<!-- get list of all locales available in the Message database (where entire site has been translated) -->
			<g:set var="allLocales" value="${Message.withCriteria {projections {distinct("locale")}}}"/>
			<g:set var="additionalParams" value="${request.queryString ? '&'+request.queryString : ''}"/>
			
			<g:each status="i" var="locale" in="${allLocales}">
				<li><a class="js-language-link" title="${message(code: 'language.'+locale.language, default: locale.language)}" data-lang-code="${locale.language}" 
						href="${currentURL+'?lang='+locale.language+additionalParams}">
					<img class="" src="${resource(dir: 'images/flags',file: locale.language+'.png')}"/>
				</a></li>
			</g:each>

		</ul>
	</li>
<%--</ul>--%>