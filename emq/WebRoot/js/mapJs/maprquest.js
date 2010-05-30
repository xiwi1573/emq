//功能：处理鼠标操作后响应事件
//作者：liuyt
//外部参数：state: small=点缩略图，pan=拖动，big=矩形选择，circle=圆形选择，region=多边形选择
//        mapId：配置地图ID，points:多边形点坐标数组，eventstate：时间状态（事件开始=eventbegin，时间结束=eventend）
//        oldx：起点x坐标，oldy：起点y坐标，newx：终点x坐标，newy：终点y坐标 startx：开始x坐标，starty：开始y坐标，tempState：临时状态
//        mapserviceurl：主地图渲染路径，mapboundserviceurl 缩略图渲染路径
//create：2009-10-20


//矩形选择地图刷新 
function mapbigger(){
 startx = oldx-document.all.mapframe.offsetLeft;
 starty = oldy-document.all.mapframe.offsetTop;
	document.all.imgmap.src=mapserviceurl+"?action=select&selecttype=rectangle&left="+startx+"&top="+starty+"&right="+newx+"&down="+newy+"&refresh=" + Math.random();
	selectByType('rectangle','');
	startx = 0;
	starty = 0;
	newx = 0;
	newy = 0;
}
//多边形选择地图刷新
function regioner(){
 var pointArray=points.split(";");
 var pcount = pointArray.length;
 document.getElementById("mydiv").innerHTML="";
 var temp_p = points.substring(0,points.length-1);
 var p=temp_p.split(";");
 var t_point="";
 for(var i=0;i<p.length;i++){
 	var x = p[i].substring(0,p[i].indexOf(","));
 	var y = p[i].substring(p[i].indexOf(",")+1,p[i].length);
 	x = x-document.all.mapframe.offsetLeft;
 	y = y-document.all.mapframe.offsetTop;
 	t_point += x+","+y+";";
 }
 points = t_point.substring(0,t_point.length-1);
 if(p.length>2){
 	document.all.imgmap.src=mapserviceurl+"?action=select&selecttype=region&pcount="+pcount+"&points="+points+"&refresh=" + Math.random();
	 selectByType('region','');
 }else{
 	points="";
 }
}

//圆形选择地图刷新
function circle(){
 var x_ = Math.abs(window.event.clientX-oldx);
 var y_ = Math.abs(window.event.clientY-oldy);
 var radius = Math.sqrt(Math.pow(x_,2)+Math.pow(y_,2));
 document.getElementById("mydiv").innerHTML="";
 oldx = oldx-document.all.mapframe.offsetLeft;
 oldy = oldy-document.all.mapframe.offsetTop;
	document.all.imgmap.src=mapserviceurl+"?action=select&selecttype=circle&x="+oldx+"&y="+oldy+"&radius="+radius+"&refresh=" + Math.random();
	parent.rightFrame.queryByRadius(mapId,oldx,oldy,radius);
}

//缩略图同步
function mapbounder(){
	document.getElementById('boundmap').src=mapboundserviceurl + "&refresh=" + Math.random();
}
