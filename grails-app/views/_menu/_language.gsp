<g:set var="lang" value="${session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE' ?: org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).toString().substring(0,2)}"/>
<g:set var="currentURL" value="${request.forwardURI}"/>
<g:set var="additionalParams" value="${request.queryString ? '&'+request.queryString : ''}"/>

<li class="dropdown dropdown-btn js-language-dropdown">
	<flags:flagDropDown lang="${lang}" additionalParams="${additionalParams}" currentURL="${currentURL}"/>
</li>