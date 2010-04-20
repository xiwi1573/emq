//功能：处理各类鼠标事件
//作者：liuyt
//外部参数：state: small=点缩略图，pan=拖动，big=矩形选择，circle=圆形选择，region=多边形选择
//        mapId：配置地图ID，points:多边形点坐标数组，eventstate：时间状态（事件开始=eventbegin，时间结束=eventend）
//        oldx：起点x坐标，oldy：起点y坐标，newx：终点x坐标，newy：终点y坐标 startx：开始x坐标，starty：开始y坐标，tempState：临时状态
//        mapserviceurl：主地图渲染路径，mapboundserviceurl 缩略图渲染路径
//create：2009-10-20


//鼠标按下事件
function mapmousedown(){
	if(window.event.srcElement.id=="imgmap"){
		if(state=="small"){
			state="pan";
		}
		if(state=="pan"&&window.event.button==1){
	  document.all.imgmap.style.cursor="move";
	  oldx=window.event.clientX-document.all.mapframe.scrollLeft;
	 	oldy=window.event.clientY-document.all.mapframe.scrollTop;
		}
		if (state=="big"&&window.event.button==1){
			document.all.seltable.style.display="";
			document.all.seltable.style.width=0;
		 document.all.seltable.style.height=0;
			document.all.seltable.style.left=window.event.clientX;
			document.all.seltable.style.top=window.event.clientY;
			oldx=window.event.clientX;
	 	oldy=window.event.clientY;
		}
		if(state=="circle"&&window.event.button==1){
			document.all.mydiv.style.display="";
			oldx=window.event.clientX;
	 	oldy=window.event.clientY;
	 	draw_circle();
		}
		if (state=="region"&&window.event.button==1){
			document.all.mydiv.style.display="";
		}
		eventstate="eventbegin";
	}else if(window.event.srcElement.id=="boundmap"||window.event.srcElement.id=="seltable_boundmap"){
		state="small";
	}else if(window.event.srcElement.id=="mydiv"||window.event.srcElement.id=="ovalId"){
		if (state=="region"){
			document.all.mydiv.style.display="";
		}
		eventstate="eventbegin";
	}
	if(window.event.srcElement.id=="mark"){
		markY = window.event.clientY;
	}
	window.event.returnValue=false;
}
//鼠标移动事件
function mapmousemove(){
	if(window.event.srcElement.id=="imgmap"){
		if(state=="big"){
			if(window.event.button==1){
				box();
			}
		}
		if(state=="pan"&&window.event.button==1){
			pan();
		}
	}
	if(window.event.srcElement.id=="imgmap"||window.event.srcElement.id=="ovalId"){
		if(state=="circle"&&window.event.button==1){
			var x_ = Math.abs(window.event.clientX-oldx);
   var y_ = Math.abs(window.event.clientY-oldy);
   var radius = Math.sqrt(Math.pow(x_,2)+Math.pow(y_,2));
   document.all.ovalId.style.width=radius*2;
			document.all.ovalId.style.height=radius*2;
   document.all.ovalId.style.left=oldx-radius;
	  document.all.ovalId.style.top=oldy-radius;
		}
	}
	if(window.event.srcElement.id=="mark"){
		document.all.overview_toggle.style.pixelTop=window.event.clientY;   
	}
	window.event.returnValue=false;
}
//鼠标弹起事件
function mapmouseup(){
	if(eventstate=="eventbegin"){
		//矩形选择
		if(state=="big"&&window.event.button==1){
			document.all.seltable.style.display="none";
			newx=window.event.clientX-document.all.mapframe.offsetLeft;
			newy=window.event.clientY-document.all.mapframe.offsetTop;
			mapbigger();
		}
		//点击缩略图
		if(state=="small"){
			document.all.selimg.style.display="none";
			smallPoint();
		}
		//拖动
		if(state=="pan"&&window.event.button==1){
			newx=window.event.clientX-document.all.imgmap.scrollLeft;
	  newy=window.event.clientY-document.all.imgmap.scrollTop;
	  document.all.imgmap.style.cursor="default";
			drag_();
		}
		if(window.event.srcElement.id=="imgmap"||window.event.srcElement.id=="ovalId"){
			//多边形选择
			if(state=="region"&&window.event.button==1){
				var tempx = parseInt(window.event.clientX);
				var tempy = parseInt(window.event.clientY);
				var adjustValue = 6;
				if(tempx<=(parseInt(oldx)+adjustValue)&&tempx>=(parseInt(oldx)-adjustValue)&&tempy<=(parseInt(oldy)+adjustValue)&&tempy>=(parseInt(oldy)-adjustValue)){
					regioner();
				}else{
					points+=window.event.clientX+",";
		 	 points+=window.event.clientY+";";
		 	 draw();
				}
			}
		}
		if(window.event.srcElement.id=="ovalId"){
			//圆形选择
			if(state=="circle"){
		 	if(window.event.srcElement.id=="imgmap"||window.event.srcElement.id=="ovalId"){
		 		newx=window.event.clientX;
		 	 newy=window.event.clientY;
		 		circle();
		 	}
			}
		}
		eventstate="eventend";
	}
	if(window.event.button==2&&window.event.srcElement.id=="imgmap"){
			selectByType('circle','');
	}
	if(window.event.srcElement.id=="mark"){
		var zoom = window.event.clientY-markY;
		zoomOutIn(zoom);
		markY = window.event.clientY;
	}
	window.event.returnValue=false
}
//表格拖动事件
function tablemove(){
	var width
	var height
	if(window.event.button==1){
		width=window.event.clientX-oldx
		height=window.event.clientY-oldy
		if(width<0&&height<0){
			document.all.seltable.style.left=window.event.clientX
			document.all.seltable.style.top=window.event.clientY
		}
		else if(width<0){
			document.all.seltable.style.left=window.event.clientX
	    }
		else if(height<0){
			document.all.seltable.style.top=window.event.clientY
		}
		document.all.seltable.style.height=Math.abs(height)+1
		document.all.seltable.style.width=Math.abs(width)+1
	}
	window.event.returnValue=false
}

//主地图状态控制
function downloadstate(){
	var dlstate=document.all.imgmap.readyState;
	if(dlstate=="uninitialized"){
		document.all.downloadimg.style.display="";
		document.all.imgmap.style.visibility="hidden";
	}
	if(dlstate=="loading"){
		document.all.downloadimg.style.display="";
		document.all.imgmap.style.visibility="hidden";
		if(document.all.mapboundframe.style.display==""){
			mapbounder();
		}
	}
	if(dlstate=="complete"){
		document.all.downloadimg.style.display="none"
		document.all.imgmap.style.visibility="visible"
	}
}

//缩略图状态控制
function bounddownloadstate(){
	var dlstate=document.all.boundmap.readyState
	if(dlstate=="uninitialized"){
		document.all.downloadimg.style.display="";
		document.all.boundmap.style.visibility="hidden";
	}
	if(dlstate=="loading"){
		document.all.downloadimg.style.display="";
		document.all.boundmap.style.visibility="hidden";
	}
	if(dlstate=="complete"){
		document.all.downloadimg.style.display="none";
		document.all.boundmap.style.visibility="visible";
	}
}