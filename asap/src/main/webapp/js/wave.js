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


// rewrite the logon link to the logon dialog in the hidden div   
	$('#userMenu li a').each(function() {
		if($(this).attr('href') == "/user_mgt/login") {
			var $userdialog = $('#forUserDialog'); 
			var $link = $(this).one('click', function() {
				$userdialog.dialog({title: $link.html(),width: 300,height: 160});
             	$link.click(function() {$userdialog.dialog('open');return false;});
		     	return false;
			}); // var $link = $(this).one('click', function()
		} else if($(this).attr('href') == "/user_mgt/sign_up") {
			var $userdialog = $('#forRegisterDialog'); 
			var $link = $(this).one('click', function() {
				$userdialog.dialog({title: $link.html(),width: 300,height: 160});
             	$link.click(function() {$userdialog.dialog('open');return false;});
		     	return false;
			}); // var $link = $(this).one('click', function()
		} else if($(this).attr('href') == "/user_mgt/lost_password") {
			var $userdialog = $('#forSendPassDialog'); 
			var $link = $(this).one('click', function() {
				$userdialog.dialog({title: $link.html(),width: 300,height: 160});
             	$link.click(function() {$userdialog.dialog('open');return false;});
		     	return false;
			}); // var $link = $(this).one('click', function()
		} else if($(this).attr('href') == "/user_mgt/logout") {
			var $userdialog = $('#forSendPassDialog'); 
			var $link = $(this).one('click', function() {
				$userdialog.dialog({title: $link.html(),width: 300,height: 160});
             	$link.click(function() {$userdialog.dialog('open');return false;});
		     	return false;
			}); // var $link = $(this).one('click', function()
		} // if else if
	}); // $('#sitemapMenu li a').each(function()

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
