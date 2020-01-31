<html>
<head>
	<title><g:if test="${grailsApplication.metadata.getApplicationVersion().contains("SNAPSHOT")}"><g:message code="default.title.label" default="lingoto"/>-beta</g:if>
        	<g:else>
        		<g:message code="default.title.label" default="lingoto"/>
        	</g:else> - <g:message code="quiz.label" default="Quiz" /></title>
	<meta name="layout" content="main" />
	<g:javascript library="lesson" />
	<g:javascript>var langId="${langId}";</g:javascript>
	<link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'inlineplayer.css')}"/>
	<script type="text/javascript" src="/js/soundmanager2.js"></script>
	<script type="text/javascript" src="/js/inlineplayer.js"></script>
</head>
<body>

<g:if test="${flash.message}">
	<g:render template="percentageModal" model="['percentCorrect':flash.message]"/>
</g:if>
	
	<h1>
		<g:if test="${lesson?.audioPath}">
			<ul class="graphic">
			  <li><a href="${application.contextPath}/${lesson?.audioPath}/${lesson?.id}.mp3">
				${lesson?.wordPhrase} </a></li>
			 </ul>
			
		</g:if>
		<g:else>
			${lesson?.wordPhrase} 
		</g:else>
	</h1>
	
		<div class="row">
			<div class="col-sm-6">
				<div class="imgLink" id="img1" style="height:270px; width:310px;"
					onclick="quizChecker('${imageNames[0]}','${lesson.id}',(this.id));">
					<img class="absoluteImg"
						src="${application.contextPath}/${lesson?.imagePath}/${imageNames[0]}" />
					<img class="quizValidation" id="img1correct"
					src="${createLinkTo(dir:'images',file:'check.png')} " /> 
					<img class="quizValidation" id="img1incorrect"
					src="${createLinkTo(dir:'images',file:'redx.png')} " />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="imgLink" id="img2" style="height:270px; width:310px;"
					onclick="quizChecker('${imageNames[1]}','${lesson.id}',(this.id));">
					<img class="absoluteImg"
						src="${application.contextPath}/${lesson?.imagePath}/${imageNames[1]}" />
					<img class="quizValidation" id="img2correct"
					src="${createLinkTo(dir:'images',file:'check.png')} " /> 
					<img class="quizValidation" id="img2incorrect"
					src="${createLinkTo(dir:'images',file:'redx.png')} " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<div class="imgLink" id="img3" style="height:270px; width:310px;"
					onclick="quizChecker('${imageNames[2]}','${lesson.id}',(this.id));">
					<img class="absoluteImg"
						src="${application.contextPath}/${lesson?.imagePath}/${imageNames[2]}" />
					<img class="quizValidation" id="img3correct"
					src="${createLinkTo(dir:'images',file:'check.png')} " /> 
					<img class="quizValidation" id="img3incorrect"
					src="${createLinkTo(dir:'images',file:'redx.png')} " />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="imgLink" id="img4" style="height:270px; width:310px;"
					onclick="quizChecker('${imageNames[3]}','${lesson.id}',(this.id));">
					<img class="absoluteImg"
						src="${application.contextPath}/${lesson?.imagePath}/${imageNames[3]}" />
					<img class="quizValidation" id="img4correct"
					src="${createLinkTo(dir:'images',file:'check.png')} " /> 
					<img class="quizValidation" id="img4incorrect"
					src="${createLinkTo(dir:'images',file:'redx.png')} " />
				</div>
			</div>
		</div>

</body>
</html>
