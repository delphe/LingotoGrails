$(document).ready(function(){
	if (document.getElementById("passwordUpdate")){
		passwordUpdateShowHide('#updatePassword')
	}
	
	$('#updatePassword').change(function () {
		passwordUpdateShowHide('#updatePassword')
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
