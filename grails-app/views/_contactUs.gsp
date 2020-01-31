<div class="modal fade" id="contactUsModal" tabindex="-1" role="dialog"
	aria-labelledby="contactUsModal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button></div>
			<div class="modal-body">
				<p>
					<g:message code="adminsKnowTheseLangs.message" default="Lingoto administrators know the following languages:"/>
				</p>
				<p>English, Español, Magyar, Deutsch </p>
				<p>
					<g:message code="weWillDoOurBest.message" default="We will do our best to understand and respond if you are using a different language."/>
				</p>
				<a HREF="mailto:admin@lingoto.com?subject=${g.message(code:'hello.label', default:'Hello')} Lingoto
					&body=${g.message(code:'email.label', default:'Email')}: ${sec?.loggedInUserInfo(field: 'username') ?: '?'} - 
					${g.message(code:'language.label', default:'Language')}: ${session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE' ?: org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).toString().substring(0,2)}" 
					class="btn btn-info">
					<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> <g:message code="contactUs.label" default="Contact Us"/></a>

			</div>
			<div class="modal-footer"></div>
		</div>
	</div>
</div>
