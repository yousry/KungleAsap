/**
 * jslib for KungleImp.de
 * perfectNumber Demo 
 * (c)2010 Yousry Abdallah
*/

function findFactors(crunchyS) {

var crunchy = eval('(' + crunchyS + ')');

    var ticket = crunchy.ticket;
	var start = crunchy.start;
	var end = Math.min(crunchy.end, crunchy.master);
    var master = crunchy.master;  

    var factors = [];

	for (i = start;i <= end;i++){
		if(master % i == 0) {
		  factors.push(i);
		  factors.push(master / i);
		}
	}

    var jsonTransmitter = {
    	"ticket": ticket,
    	"factors": factors 
    };
    
	replyResult("replyResult", JSON.stringify(jsonTransmitter));
}