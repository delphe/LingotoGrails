$(document).ready(function(){
	
	$.ajax({
		//Code found at http://www.icodeya.com/2013/03/grails-autocomplete-via-jquery-ui-and.html
        type: "GET",
        url: "/masterLang/getAllLanguages/",
        dataType: "json",
        success : function(response) {
        	/*
            //Create a map.
            var data =
                $.map(response, function(item){
                    return{
                        id: item.id,
                        value: item.lingo
                    }
                });
        	 */

            $("#language_textField").autocomplete({
            	source: response
            	/*
                source: data,
                select: function (event, ui){
                    //when a language is selected(ie: type English and press enter),
                    //change the value of hidden field to the language id.
                    $('#language_id').val(ui.item.id);
                }
            	*/
            });

        }
    });
	
	$("#dialect_textField").focus(function(){
    	var lang = $('#language_textField').val();    	
		$.ajax({
	        type: "GET",
	        url: "/masterLang/getAllDialects/",
	        dataType: "json",
		    data: {lingo:lang},
	        success : function(response) {
            	$("#dialect_textField").autocomplete({
                    source: response,
                });
	        }
	    });
	});
	
});
