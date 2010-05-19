/**
 * small jslib for KungleImp.de 
 * (c)2010 Yousry Abdallah
*/

// Dialog for Note4All
$(function() {
	$("#dialog").dialog({height: 448, width: 297, position:[5 ,180], resizable: false});
});

$(document).ready(function() {

// Switch news language via js
	$('keng').show('slow'); 
    $('kfrn').hide('slow'); 
    $('kger').hide('slow'); 

    // Init the drawing canvas
	$('#drawbox').drawbox({caption:'Leave your message',lineWidth:1,lineCap:'round',lineJoin:'round',colorSelector:false});
  	addStroke("addStroke", "init 0 0");

});

// Define drppable space for newsicons
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



var prevx = 0;
var prevy = 0;

function remoteStroke(what) {

	var canvas = document.getElementById("drawbox");
  	var context = canvas.getContext("2d");
  
  	var whats = what.split(" ");
  	var wcmd = whats.splice(0,1);
  
  	if(wcmd == 'start') {
		alert("error start");
  	}

  	if(wcmd == 'move') {
	 	alert("error stop");
  	}

  	if(wcmd == 'stop') {
		context.beginPath();
	  	for(x in whats) {
			var oneP = whats[x].split(",")
		  	if(x == 0) {
		    	context.moveTo(oneP[0],oneP[1]);
		  	} else  {
           		context.lineTo(oneP[0],oneP[1]);
		  	}
	  	} 
	   	context.stroke();
	}

  	if(wcmd == 'clear') {
   		context.save();
   		context.beginPath();
   		context.closePath();
   		context.restore();
   		context.clearRect(0, 0, $(canvas).width(), $(canvas).height());
  }
}
