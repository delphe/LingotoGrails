$(document).ready(function(){
	
	var strength = {
			0: "Worst ☹",
			1: "Bad ☹",
			2: "Weak ☹",
			3: "Good ☺",
			4: "Strong ☻"
	}

	var password = document.getElementById('password');
	    console.log("TEST");
	    console.log(password);
	var meter = document.getElementById('password-strength-meter');
	var text = document.getElementById('password-strength-text');

	password.addEventListener('input', function()
	{
	    var val = password.value;
	    var result = zxcvbn(val);
		
	    // Update the password strength meter
	    meter.value = result.score;
	   
	    // Update the text indicator
	    if(val !== "") {
	        text.innerHTML = "Strength: " + "<strong>" + strength[result.score] + "</strong>" + "<span class='feedback'>" + result.feedback.warning + " " + result.feedback.suggestions + "</span"; 
	    }
	    else {
	        text.innerHTML = "";
	    }
	});
	
	
	if (document.getElementById("passwordUpdate")){
		passwordUpdateShowHide('#updatePassword')
		emailUpdateShowHide('#updateEmail')
	}
	
	$('#updatePassword').change(function () {
		passwordUpdateShowHide('#updatePassword')
	 });
	$('#updateEmail').change(function () {
		emailUpdateShowHide('#updateEmail')
	 });

	$("#username").focus();

	if ($("#successChk").length){  
		if($("#langUpdatedMsg").length){
			$( "#langUpdatedMsg" ).fadeIn('slow');
		}else{
			displayCheckAndRedirect()
		}
	}
	
});

function displayCheckAndRedirect(){
	$( "#successChk" ).fadeIn('slow');
	setTimeout(function(){
		$( "#successChk" ).fadeOut('slow');
	}, 1000);
	setTimeout(function(){
		window.location.href = redirectUrl;
	}, 2000);
}

function adultContentDtShowHide(elemId){
	if($('select.age').val() == "18"){
		elemId.style.display = "block";
	}else{
		elemId.style.display = "none";
	}
}

function passwordUpdateShowHide(elemId){
	if($(elemId).is(":checked")) {
		$( "#passwordUpdate" ).show();
		$( "#passwordUpdate" ).height(130);
    }else{
    	$( "#passwordUpdate" ).hide();
    }
}

function emailUpdateShowHide(elemId){
	if($(elemId).is(":checked")) {
		$( "#emailUpdate" ).show();
		$( "#emailUpdate" ).height(130);
    }else{
    	$( "#emailUpdate" ).hide();
    }
}


