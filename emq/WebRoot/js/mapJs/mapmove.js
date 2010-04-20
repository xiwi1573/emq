//功能：处理鼠标移动过程中响应事件
//作者：liuyt
//外部参数：state: small=点缩略图，pan=拖动，big=矩形选择，circle=圆形选择，region=多边形选择
//        mapId：配置地图ID，points:多边形点坐标数组，eventstate：时间状态（事件开始=eventbegin，时间结束=eventend）
//        oldx：起点x坐标，oldy：起点y坐标，newx：终点x坐标，newy：终点y坐标 startx：开始x坐标，starty：开始y坐标，tempState：临时状态
//        mapserviceurl：主地图渲染路径，mapboundserviceurl 缩略图渲染路径
//create：2009-10-20

//表格拖动放大缩小
function box(){
	var newx;
	var newy;
	var framehigh;
	var framewidth;
	var frametop;
	var frameleft;
	framehigh=parseInt(document.all.mapframe.style.height);
	framewidth=parseInt(document.all.mapframe.style.width);
	frametop=parseInt(document.all.mapframe.style.top);
	frameleft=parseInt(document.all.mapframe.style.left);
	if(window.event.clientX>(frameleft+framewidth-2)){
		newx=frameleft+framewidth-2;
	}
	else if(window.event.clientX<(frameleft+1)){
		newx=frameleft+1;
	}
	else{
		newx=window.event.clientX;
	}
	if(window.event.clientY>(frametop+framehigh-2)){
		newy=frametop+framehigh-2;
	}
	else if(window.event.clientY<(frametop+1)){
		newy=frametop+1;
	}
	else{
		newy=window.event.clientY;
	}
	width=newx-oldx;
	height=newy-oldy;
	if(width<0&&height<0){
		document.all.seltable.style.left=newx
		document.all.seltable.style.top=newy
	}
	else if(width<0){
		document.all.seltable.style.left=newx
	}
	else if(height<0){
		document.all.seltable.style.top=newy
	}
	document.all.seltable.style.height=Math.abs(height)+1;
	document.all.seltable.style.width=Math.abs(width)+1;
}
//地图拖动
function pan(){
	if(document.all.imgmap.style.cursor=="move"){
		var newx;
		var newy;
		var picwidth;
		var pichigh;
		picwidth=parseInt(document.all.imgmap.style.width);
		pichigh=parseInt(document.all.imgmap.style.height);
		newx=startx+(window.event.clientX-oldx-document.all.imgmap.scrollLeft);
		newy=starty+(window.event.clientY-oldy-document.all.imgmap.scrollTop);
		if (newx>picwidth) newx=picwidth;
		if (newx<-picwidth) newx=-picwidth;
		if (newy>pichigh) newy=pichigh;
		if (newy<-pichigh) newy=-pichigh;
		document.all.imgmap.style.left=newx;
		document.all.imgmap.style.top=newy;
	}
}
