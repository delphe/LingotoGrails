$(document).ready(function(){
	
	$('#resetPasswordLink').click(function(){ sendEmailToController(); return false; });

	$('#lessonPlanURL').click(function() {
        $(this).select();
        $(this).css("direction", "RTL");
    });
	$('#lessonPlanURL').blur(function() {
        $(this).css("direction", "LTR");
    });
});

if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}

function sendEmailToController(){
	var email = $('#username').val();
	$.ajax({
        type: "GET",
        url: "/login/auth/",
        dataType: "json",
	    data: {email:email, showResetPasswordModal:true},
//        success : function(response) {
//        	console.log("SUCCESS!!");
//        	$('#resetPasswordModal').modal('show');
////        	$("#dialect_textField").autocomplete({
////                source: response,
////            });
//        },
	    complete:function(data){
//	    	console.log(response);
	    	console.dir(data);
	    	$('#resetPasswordModal').modal('show');
	    }
    });
	
	
}

//= require bootstrap
