function showCalendar(fieldValue, showx, showy) {
    var ret;
	showx = event.screenX - event.offsetX - 190 ; // + deltaX;
	showy = event.screenY - event.offsetY + 5; // + deltaY;
	//ret = window.showModalDialog("/stmadc/stma/dc/include/js/date.htm", "", "dialogWidth:197px; dialogHeight:210px; dialogLeft:"+showx+"px; dialogTop:"+showy+"px; status:no; directories:yes;scrollbars:no;Resizable=no; "  );
	ret = window.showModalDialog("/KMGIS/js/common/date.htm", "", "dialogWidth:100px; dialogHeight:180px; dialogLeft:"+showx+"px; dialogTop:"+showy+"px; status:no; directories:yes;scrollbars:no;Resizable=no; "  );

    if (ret == null) {
        ret = fieldValue;
    }
    return ret;
}


function showCalendar(fieldValue) {
    var ret;
	showx = event.screenX - event.offsetX - 190 ; // + deltaX;
	showy = event.screenY - event.offsetY + 5; // + deltaY;
	//ret = window.showModalDialog("/stmadc/stma/dc/include/js/date.htm", "", "dialogWidth:197px; dialogHeight:210px; dialogLeft:"+showx+"px; dialogTop:"+showy+"px; status:no; directories:yes;scrollbars:no;Resizable=no; "  );
	ret = window.showModalDialog("/KMGIS/js/common/date.htm", "", "dialogWidth:100px; dialogHeight:180px; dialogLeft:"+showx+"px; dialogTop:"+showy+"px; status:no; directories:yes;scrollbars:no;Resizable=no; "  );

    if (ret == null) {
        ret = fieldValue;
    }
    return ret;
}

Date.prototype.format = function(format) {
	/*
	 * eg:format="YYYY-MM-dd hh:mm:ss";
	 */
	var o = {
		"M+" : this.getMonth() + 1, //month
		"d+" : this.getDate(), //day
		"h+" : this.getHours(), //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
		"S" : this.getMilliseconds()
		//millisecond
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
						- RegExp.$1.length));
	}

	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1
							? o[k]
							: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}
