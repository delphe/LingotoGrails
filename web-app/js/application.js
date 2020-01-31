$(document).ready(function(){

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

//= require bootstrap
