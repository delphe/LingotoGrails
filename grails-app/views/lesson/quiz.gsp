<html>
<head>
	<title>lingoto - <g:message code="quiz.label" default="Quiz" /></title>
	<meta name="layout" content="main" />
	<g:javascript library="lesson" />
	<g:javascript>var langId="${langId}";</g:javascript>
	<sm:inlinePlayer/>
</head>
<body>

<g:if test="${flash.message}">
	<g:render template="percentageModal" model="['percentCorrect':flash.message]"/>
</g:if>
	
	<h1>
		<g:if test="${lesson?.audioPath}">
			<a href="${application.contextPath}/${lesson?.audioPath}/${lesson?.id}.mp3">
				${lesson?.wordPhrase} </a>
		</g:if>
		<g:else>
			${lesson?.wordPhrase} 
		</g:else>
	</h1>
	
		<div class="row">
			<div class="col-sm-6">
				<div class="imgLink" id="img1" style="height:250px; width:297px;"
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
				<div class="imgLink" id="img2" style="height:250px; width:297px;"
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
				<div class="imgLink" id="img3" style="height:250px; width:297px;"
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
				<div class="imgLink" id="img4" style="height:250px; width:297px;"
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
