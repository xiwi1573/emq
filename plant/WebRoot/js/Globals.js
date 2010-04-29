var waitMessage = "正在执行查询请稍候";
var checkMessage = "请选择查询条件！";
var corpCodeMessage = "请选择县区市！";
/**
	 * 加载Load信息
	 * @param type 1:waitMessage
	 */
function getLoadMessage(message){
		DWRUtil.useLoadingMessage(message) ;
		DWREngine.setPreHook(function() {
		    var disabledZone = $('disabledZone');
		    if (!disabledZone) {
		      disabledZone = document.createElement('div');
		      disabledZone.setAttribute('id', 'disabledZone');
		      disabledZone.style.position = "absolute";
		      disabledZone.style.zIndex = "1000";
		      disabledZone.style.left = "0px";
		      disabledZone.style.top = "0px";
		      disabledZone.style.width = "100%";
		      disabledZone.style.height = "100%";
		      document.body.appendChild(disabledZone);
		      var messageZone = document.createElement('div');
		      messageZone.setAttribute('id', 'messageZone');
		      messageZone.style.position = "absolute";
		      messageZone.style.top = "0px";
		      messageZone.style.right = "0px";
		      messageZone.style.width = "150";
		      messageZone.style.height = "20";  
		      messageZone.style.background = "#0000CC";
		      messageZone.style.color = "white";
		      messageZone.style.fontFamily = "����";
		      messageZone.style.fontSize="9pt";
		      messageZone.style.padding = "4px";
		      disabledZone.appendChild(messageZone);
		      var text = document.createTextNode(message);
		      messageZone.appendChild(text);
		    }
		    else {
		      $('messageZone').innerHTML = message;
		      disabledZone.style.visibility = 'visible';
		    }
		  });

		  DWREngine.setPostHook(function() {
		    $('disabledZone').style.visibility = 'hidden';
		  });
}

//校验坐标是否在有效范围内
function checkPoint(x,y){
		if(!((x>102.1&&x<103.7&&y>24.3&&y<26.6)||(y>102.1&&y<103.7&&x>24.3&&x<26.6))){
				alert("昆明市有效经纬度：X(102.1-103.7),Y(24.3-26.6),该点(X="+x+",Y="+y+")不在有效范围内！");
				return false;
		}
		return true;
}
