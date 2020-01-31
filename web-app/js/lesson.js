$(document).ready(function(){
	
	$("#beginner,#intermediate,#advanced,#formal,#informal" ).change(function() {
		submitForm()
	});
	
	$("#editableImgDiv").hover(function(){
		editImg.style.visibility = "visible";
	}, function() {
		editImg.style.visibility = "hidden";
	});
	
	$("#editableAudioDiv").hover(function(){
		editAudio.style.visibility = "visible";
	}, function() {
		editAudio.style.visibility = "hidden";
	});
	
});

function submitForm(){
    var querystring = $("#rightNavFilterForm").serialize();
    $.ajax({
        url : "/lingoto/lesson/filterLessons",
        data : querystring,
        success : function(data) {
            $('#pagination').html(data);
        }
    })
}

function showImgInput() {
	editableImgDiv.style.visibility = "hidden"
	editableImgDiv.style.height = "0px";
	$("#imgInputDiv" ).show();
	imgInputDiv.style.visibility = "visible";
	imgInputDiv.style.height = "90px";
}

function showAudioInput() {
	editableAudioDiv.style.visibility = "hidden"
	editableAudioDiv.style.height = "0px";
	$("#audioInputDiv" ).show();
	audioInputDiv.style.visibility = "visible";
	audioInputDiv.style.height = "90px";
}

function ratingClick(star, accountId) {
	if(star == 1 &&
	  document.getElementById("ratingStar5").style.color == "gold" &&
	  document.getElementById("ratingStar4").style.color == "lightgray"){
		star = 0
	}
		
	$.ajax({
		type:'POST',
	    url: "/lingoto/lesson/saveRating/",
	    dataType: 'json',
	    data: {rating:JSON.stringify(star), accountId:JSON.stringify(accountId)},
	    complete: function() {
	    	if (star == 5){
				document.getElementById("ratingStar1").style.color = "gold";
				document.getElementById("ratingStar2").style.color = "gold";
				document.getElementById("ratingStar3").style.color = "gold";
				document.getElementById("ratingStar4").style.color = "gold";
				document.getElementById("ratingStar5").style.color = "gold";
			}
			if (star == 4){
				document.getElementById("ratingStar1").style.color = "lightgray";
				document.getElementById("ratingStar2").style.color = "gold";
				document.getElementById("ratingStar3").style.color = "gold";
				document.getElementById("ratingStar4").style.color = "gold";
				document.getElementById("ratingStar5").style.color = "gold";
			}
			if (star == 3){
				document.getElementById("ratingStar1").style.color = "lightgray";
				document.getElementById("ratingStar2").style.color = "lightgray";
				document.getElementById("ratingStar3").style.color = "gold";
				document.getElementById("ratingStar4").style.color = "gold";
				document.getElementById("ratingStar5").style.color = "gold";
			}
			if (star == 2){
				document.getElementById("ratingStar1").style.color = "lightgray";
				document.getElementById("ratingStar2").style.color = "lightgray";
				document.getElementById("ratingStar3").style.color = "lightgray";
				document.getElementById("ratingStar4").style.color = "gold";
				document.getElementById("ratingStar5").style.color = "gold";
			}
			if (star == 1){
				document.getElementById("ratingStar1").style.color = "lightgray";
				document.getElementById("ratingStar2").style.color = "lightgray";
				document.getElementById("ratingStar3").style.color = "lightgray";
				document.getElementById("ratingStar4").style.color = "lightgray";
				document.getElementById("ratingStar5").style.color = "gold";
			}
			if(star == 0){
				document.getElementById("ratingStar1").style.color = "lightgray";
				document.getElementById("ratingStar2").style.color = "lightgray";
				document.getElementById("ratingStar3").style.color = "lightgray";
				document.getElementById("ratingStar4").style.color = "lightgray";
				document.getElementById("ratingStar5").style.color = "lightgray";
			}
	    }
	});
	
}

function saveTranslation(viewedLessonId, translation){
	$.ajax({
		type:'POST',
	    url: "/lingoto/lesson/saveTranslation/",
	    dataType: 'json',
	    data: {translation:translation, viewedLessonId:viewedLessonId},
	    complete: function() {
	    	//remove lock from next arrow
	    }
	});
}


function unlockNextArrow(){
	if(document.getElementById('locked')){
		$(".unlocked").show();
		locked.style.visibility = "hidden";
	}
}

function quizChecker(imageName, lessonId, elemId){
	var elem = document.getElementById(elemId);
	var correctImgElem = document.getElementById(elemId + "correct");
	var incorrectImgElem = document.getElementById(elemId + "incorrect");
	var img = imageName.split("-") // 1-small.jpg becomes [1,small.jpg] 1.jpg becomes [1.jpg,]
	var imgage = img[0].split(".") // 1.jpg becomes [1,jpg]
	var markedIncorrect = (document.getElementById("img1incorrect").style.zIndex == 3);
	
	if (imgage[0] == lessonId){
		$("#" + elemId + "correct").fadeIn(500);
		$("#" + elemId + "correct").delay(500).fadeOut(1000);
		
		$.ajax({
			type:'POST',
		    url: "/lingoto/lesson/grader/",
		    dataType: 'json',
		    data: {lessonId:lessonId, markedIncorrect:markedIncorrect, langId:langId},
		    complete: function() {location.reload();}
		});

	}else{
		//marking as incorrect by adding a style to the element that can be checked.
		document.getElementById("img1incorrect").style.zIndex = 3
		
		$("#" + elemId + "incorrect").fadeIn(500);
		$("#" + elemId + "incorrect").delay(500).fadeOut(1000);
	}
	
}
