$(document).ready(function(){
	
	loadSortableListFunctions()
	
	$( "#filterButtons" ).click(function() {
		$('#beginnerFilterBtn').removeClass('active');
		$('#intermediateFilterBtn').removeClass('active');
		$('#advancedFilterBtn').removeClass('active');
		$(':input:checked').parent('.btn').addClass('active');
	});

});

function loadSortableListFunctions() {
	document.getElementById("save-order-buttons").style.visibility = "hidden";
	$('#lesson-list .sortable-list').sortable();
	var cache = $("#lesson-list .sortable-list").html();
	$('#lesson-list .sortable-list').sortable({
		change: function() {
			document.getElementById("save-order-buttons").style.visibility = "visible";
		}
		});
	var initialList = $('#lesson-list .sortable-list').sortable("toArray");
	$( "#save-order" ).click(function() {
		var sortedList = $('#lesson-list .sortable-list').sortable( "toArray" );
		
		$.ajax({
			type:'POST',
		    url: "/lingoto/lesson/saveSortedList/",
		    dataType: 'json',
		    data: {sortedList:JSON.stringify(sortedList),
		    	initialList:JSON.stringify(initialList)},
		    success: function(data) {
		        //alert(data);
		    },
		    complete: function() {
		    	location.reload();
		    }
		});
	});
	
	$( "#cancel-save-order" ).click(function() {
		$('#lesson-list .sortable-list').html(cache).sortable("refresh");
		document.getElementById("save-order-buttons").style.visibility = "hidden";
	});
	
}
