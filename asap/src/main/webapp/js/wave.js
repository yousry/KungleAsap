/**
 * small jslib for KungleImp.de 
 * (c)2010 Yousry Abdallah
*/

$(function() {
	$("#newstopology").dialog({height: 448, width: 297, position:[5 ,145], resizable: false});
});

$(document).ready(function() {

// animate icon
	var thing = $("#loginico");
	thing.animate({rotate: '45deg', skewX: '25deg', scale: '+=.4 +=.4', origin: [34, 34]}, {
		duration: 800,
		easing: 'easeOutCirc',
		complete: function() {
			thing.animate({rotate: '-45deg', skewX: '-25deg', scale: '+=.8 +=.8', origin: [34, 34]}, {
				duration: 800,
				easing: 'easeOutBack',
				complete: function() {
					thing.animate({rotate: '+=405', skewX: '0', scale: '1 1', origin: [34, 34]}, {
						duration: 800,
						easing: 'easeOutElastic'
					});
				}
			});
		}
	});
	

$("#animenu").show("bounce",{direction: "right", distance: 40, times: 5},"normal");

});

// Define droppable space for newsicons
$(function() {
	$("#droppable").droppable({
		drop: function(event, ui) {
        	var picId = ui.draggable.attr("id");
    		addNews("newsCallback", picId);  
	    }
	});
});

// Notify the dropbox
function newsCallback(what) {
	$("#droppable").addClass('ui-state-highlight').find('p').html(  what  );
}
