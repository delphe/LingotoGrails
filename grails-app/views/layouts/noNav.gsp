<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="${session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'}">
    <head>
        <title><g:layoutTitle default="lingoto" /></title> 

        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        
		<link rel="apple-touch-icon"	href="assets/ico/apple-touch-icon.png">
	    <link rel="apple-touch-icon"	href="assets/ico/apple-touch-icon-72x72.png"	sizes="72x72">
	    <link rel="apple-touch-icon"	href="assets/ico/apple-touch-icon-114x114.png"	sizes="114x114">
<%--TODO: replace icons with lingoto logo	--%>

		<link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'custom.css')}"/>
		<g:javascript>var contextPath="${request.contextPath}";</g:javascript>
		
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />

		<r:require modules="bootstrap"/>
		<r:require modules="bootstrap_utils"/>
		
        <g:layoutHead />
        <r:layoutResources/>
    </head>
    <body>
    
	    <div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<g:render template="/header"/>
					<div id="Content" class="container">
						<g:layoutBody />
					</div>
				</div>
			</div>
		</div>
		<!-- Enable to overwrite Footer by individual page -->
        <g:if test="${ pageProperty(name:'page.footer') }">
		    <g:pageProperty name="page.footer" />
		</g:if>
		<g:else>
			<g:render template="/footer"/>														
		</g:else>
        <r:layoutResources/>
    </body>
</html>