<%@page contentType="text/html; charset=UTF-8" language="java" errorPage=""%> 
<%
	String contextPath = request.getContextPath();
// dwr上下文路径
	String dwrContextPath = contextPath+"/dwr";
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+contextPath+"/";
	String mapId = request.getParameter("mapId");
	//orgId触摸屏应用专用参数
	String orgId = request.getParameter("orgId");	
	boolean isCMP = true;
	//if(orgId != null && !"".equals(orgId))
	//	isCMP = true; 
%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<style>
 v\:* { BEHAVIOR: url(#default#VML) }
.1-1 { background:url(<%=contextPath%>/imgs/cmpCtrol/1-1.png); width:38px;height:40px; border:0px; padding-top:0px;}
.1-2 { background:url(<%=contextPath%>/imgs/cmpCtrol/1-2.png); width:42px;height:40px; border:0px; padding-top:0px; cursor:pointer;}
.1-3 { background:url(<%=contextPath%>/imgs/cmpCtrol/1-3.png); width:38px;height:40px; border:0px; padding-top:0px;}
.2-1 { background:url(<%=contextPath%>/imgs/cmpCtrol/2-1.png); width:38px;height:40px; border:0px; padding-top:0px; cursor:pointer;}
.2-2 { background:url(<%=contextPath%>/imgs/cmpCtrol/2-2.png); width:42px;height:40px; border:0px; padding-top:0px; cursor:pointer;}
.2-3 { background:url(<%=contextPath%>/imgs/cmpCtrol/2-3.png); width:37px;height:40px; border:0px; padding-top:0px; cursor:pointer;}
.3-1 { background:url(<%=contextPath%>/imgs/cmpCtrol/3-1.png); width:38px;height:38px; border:0px; padding-top:0px; }
.3-2 { background:url(<%=contextPath%>/imgs/cmpCtrol/3-2.png); width:42px;height:38px; border:0px; padding-top:0px; cursor:pointer;}
.3-3 { background:url(<%=contextPath%>/imgs/cmpCtrol/3-3.png); width:37px;height:38px; border:0px; padding-top:0px; }
.5 { background:url(<%=contextPath%>/imgs/cmpCtrol/5.png); width:60px;height:29px; border:0px; padding-top:0px; cursor:pointer;}
.5-2 { background:url(<%=contextPath%>/imgs/cmpCtrol/5-2.png); width:60px;height:29px; border:0px; padding-top:0px; cursor:pointer;}
.6 { background:url(<%=contextPath%>/imgs/cmpCtrol/6.png); width:60px;height:29px; border:0px; padding-top:0px; cursor:pointer;}
.6-2 { background:url(<%=contextPath%>/imgs/cmpCtrol/6-2.png); width:60px;height:29px; border:0px; padding-top:0px; cursor:pointer;}
.8 { background:url(<%=contextPath%>/imgs/cmpCtrol/8.png); width:60px;height:30px; border:0px; padding-top:0px; cursor:pointer;}
.8-2 { background:url(<%=contextPath%>/imgs/cmpCtrol/8-2.png); width:60px;height:30px; border:0px; padding-top:0px; cursor:pointer;}
.9 { background:url(<%=contextPath%>/imgs/cmpCtrol/9.png); width:60px;height:30px; border:0px; padding-top:0px; cursor:pointer;}
.9-2 { background:url(<%=contextPath%>/imgs/cmpCtrol/9-2.png); width:60px;height:30px; border:0px; padding-top:0px; cursor:pointer;}
.10 { background:url(<%=contextPath%>/imgs/cmpCtrol/10.png); width:60px;height:30px; border:0px; padding-top:0px; cursor:pointer;}
.10-2 { background:url(<%=contextPath%>/imgs/cmpCtrol/10-2.png); width:60px;height:30px; border:0px; padding-top:0px; cursor:pointer;}
.11 { background:url(<%=contextPath%>/imgs/cmpCtrol/11.png); width:60px;height:30px; border:0px; padding-top:0px; cursor:pointer;}
.11-2 { background:url(<%=contextPath%>/imgs/cmpCtrol/11-2.png); width:60px;height:30px; border:0px; padding-top:0px; cursor:pointer;}
 
.mapControle{ position:absolute;left:10px; top:50px; z-index:100 }  
.checkbox {  width:40px;height:40px;  cursor:pointer;}
 
</style>
<script language="JavaScript" src="js/mapJs/mapevent.js">
</script>
<script language="JavaScript" src="js/mapJs/mapmove.js">
</script>
<script language="JavaScript" src="js/mapJs/maprquest.js">
</script>
<script type='text/javascript' src='<%=dwrContextPath%>/engine.js'></script>
<script type='text/javascript' src='<%=dwrContextPath%>/util.js'></script>  
<script type='text/javascript' src='<%=dwrContextPath%>/interface/BaseService.js'></script>
<script type='text/javascript' src='<%=dwrContextPath%>/interface/CigStoreFinderService.js'></script>

<script type='text/javascript'>

/*
* 按钮高亮选中css样式名称后缀
*/
var btnOnClassSuffix = 	'-2';
 /*
 * 设置obj的样式高亮,状态为选中
 * @param obj html元素
 */
function setBtnClassAndState(obj){
	obj.className = obj.className.replace(btnOnClassSuffix,'');	
	obj.className = obj.className + btnOnClassSuffix;
	$('btnDefault').className = $('btnDefault').className.replace(btnOnClassSuffix,'');	 
	if(obj.id=='btnRect' && !$('rectangleSelect').checked){
		$('rectangleSelect').click(); 
	}
	if(obj.id=='btnRegion' && !$('regionSelect').checked){
		$('regionSelect').click(); 
	}
	if(obj.id=='btnCircle' && !$('circleSelect').checked){
		$('circleSelect').click(); 
	}
}

 /*
 * 取消所有关联按钮的高亮样式 
 */
function resetBtnsClassAndState(){	 
	$('btnRect').className = $('btnRect').className.replace(btnOnClassSuffix,'');	
	$('btnRegion').className = $('btnRegion').className.replace(btnOnClassSuffix,'');
	$('btnCircle').className = $('btnCircle').className.replace(btnOnClassSuffix,'');
	$('btnDefault').className = $('btnDefault').className.replace(btnOnClassSuffix,'');	 
	$('btnDefault').className = $('btnDefault').className + btnOnClassSuffix;	 
	if($('rectangleSelect').checked){
		$('rectangleSelect').click(); 
	}
	if($('regionSelect').checked){
		$('regionSelect').click();	 
	}
	if($('circleSelect').checked){
		$('circleSelect').click();	
	}
}
</script>
</head>
<body scroll="no" bgcolor="#DFFFDF" link="#000000" vlink="#000000" alink="#000000" oncontextmenu="return false">
<input type='hidden' name='mapId' id='mapId' value='<%=mapId %>'/> 
<input type='hidden' name='xkzhm' id='xkzhm'/> 
<input type='hidden' name='sfkxg' id='sfkxg'/> 
<img name="downloadimg" src="imgs/download.gif" style="position:absolute;left:180px;top:180px;z-index:2;" > 
<!--主地图-->
<div id="mapframe" style="position:absolute;left:0px;top:0px;overflow:hidden;background-color:#efeeea;layer-background-color:#efeeea;border:1px #339933 solid"> 
	<img id="imgmap" GALLERYIMG="false" style="position:relative;left:0px;top:0px;cursor:default"> 
</div> 
<div id="mydiv" style="position:absolute; width:1250px; height:690px; z-index:1; left: 0px; top: 0px; filter:Alpha(opacity=30)"></div>
<!--缩略图-->
<div id="mapboundframe" style="position:absolute;left:0px;top:600px;height:150px;width:150px;overflow:hidden;background-color:#efeeea;layer-background-color:#efeeea;border:1px #339933 solid;"> 
	<img id="boundmap" GALLERYIMG="false" style="position:relative;left:0px;top:0px;" onclick="smallPoint()"> 
</div>
<table id="seltable" style="position:absolute;border:1px solid Red;width:0px;height:0px;display:none;">
	<tr><td></td></tr>
</table>
<div id="seltable_boundmap" style="position:absolute;border:1px solid Red;width:60px;height:45px;left:44px;"></div>
<img name="selimg" style="position:absolute;border:1px solid Red;width:1px;height:1px;display:none;">

<% if(isCMP){ %>

<div id="mapControle" class="mapControle">
	<table cellspacing="0" cellpadding="0" border="0"> 
		<tr>
			<td>
				<table cellspacing="0" cellpadding="0" border="0"> 
					<tr>
						<td><div class="1-1"></div></td>
						<td><input type="button" class="1-2" onclick="move_('up')"/></td>
						<td><div class="1-3"></div></td>
					</tr>
					<tr>
						<td><input type="button" class="2-1" onclick="move_('left')"/></td>
						<td><input type="button" class="2-2" onclick="reset_()"/></td>
						<td><input type="button" class="2-3" onclick="move_('right')"/></td>
					</tr>
					<tr>
						<td><div class="3-1"></div></td>
						<td><input type="button" class="3-2" onclick="move_('down')"/></td>
						<td><div class="3-3"></div></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table cellspacing="0" cellpadding="0" border="0" align="center"> 
					
					<tr>
						<td><div class="5" onMouseMove="this.className='5-2';" onMouseOut="this.className='5';" onclick="zoomOut_();"/></div></td>
					</tr>
					<tr>
						<td><div class="6" onMouseMove="this.className='6-2';" onMouseOut="this.className='6';" onclick="zoomIn_();"/></div></td>
					</tr>
					<tr>
						<td><div id="btnDefault" class="8-2" onclick="resetBtnsClassAndState();"/></div></td>
					</tr>
					<tr>
						<td><div id="btnRect" class="9" onclick="resetBtnsClassAndState();setBtnClassAndState(this);"/></div></td>
					</tr>
					<tr>
						<td><div id="btnRegion" class="10" onclick="resetBtnsClassAndState();setBtnClassAndState(this);"/></div></td>
					</tr>
					<tr>
						<td><div id="btnCircle" class="11" onclick="resetBtnsClassAndState();setBtnClassAndState(this);"/></div></td>
					</tr>
				</table>
			</td>
		</tr>
	</table> 
</div>
<!-- 保留同非触摸屏的一致,隐藏不显示 -->
<div style="display:none">
	<div id="rectangleButton" style="position:absolute;left:10px; top:125px; width:87px; height:18px;z-index:100"><input id='rectangleSelect' type="checkbox" name="rectangleSelect" onclick="rectangleSelect()"/><font size="2">矩形选择</font></div>
	<div id="regionButton" style="position:absolute;left:10px; top:140px; width:100px; height:18px;z-index:100"><input id='regionSelect' type="checkbox" name="regionSelect" onclick="regionSelect()"/><font size="2">多边形选择</font></div>
	<div id="circleButton" style="position:absolute;left:10px; top:155px; width:100px; height:18px;z-index:100"><input id='circleSelect' type="checkbox" name="circleSelect" onclick="circleSelect()"/><font size="2">圆形选择</font></div>
</div>
<%}else{ %>
<div id="leftButton" style="position:absolute;left:12px; top:50px; width:87px; height:18px;z-index:100"><input type="button" value="左" onclick="move_('left')"/></div>
<div id="rightButton" style="position:absolute;left:52px; top:50px; width:87px; height:18px;z-index:100"><input type="button" value="右" onclick="move_('right')"/></div>
<div id="topButton" style="position:absolute;left:32px; top:25px; width:87px; height:18px;z-index:100"><input type="button" value="上" onclick="move_('up')"/></div>
<div id="downButton" style="position:absolute;left:32px; top:75px; width:87px; height:18px;z-index:100"><input type="button" value="下" onclick="move_('down')"/></div>
<div id="centerButton" style="position:absolute;left:32px; top:50px; width:87px; height:18px;z-index:100"><input type="button" value="中" onclick="reset_()"/></div>
<div id="addButton" style="position:absolute;left:12px; top:100px; width:87px; height:18px;z-index:100"><input type="button" value="+" onclick="zoomOut_()"/></div>
<div id="reduceButton" style="position:absolute;left:55px; top:100px; width:87px; height:18px;z-index:100"><input type="button" value="-" onclick="zoomIn_()"/></div>
<div id="rectangleButton" style="position:absolute;left:10px; top:125px; width:87px; height:18px;z-index:100"><input id='rectangleSelect' type="checkbox" name="rectangleSelect" onclick="rectangleSelect()"/><font size="2">矩形选择</font></div>
<div id="regionButton" style="position:absolute;left:10px; top:140px; width:100px; height:18px;z-index:100"><input id='regionSelect' type="checkbox" name="regionSelect" onclick="regionSelect()"/><font size="2">多边形选择</font></div>
<div id="circleButton" style="position:absolute;left:10px; top:155px; width:100px; height:18px;z-index:100"><input id='circleSelect' type="checkbox" name="circleSelect" onclick="circleSelect()"/><font size="2">圆形选择</font></div>
<%} %>

<!-- <DIV id="overview_toggle" style="position:absolute;left:5px; top:160px; width:100px; height:18px;z-index:100">
 <IMG id="overview_toggle_image" src="imgs/mapcontrols3d5.png"/>
</DIV>
<div id="mark"><input type="button"/></div> -->
<input type="hidden" name="oldx">
<input type="hidden" name="oldy">
<input type="hidden" name="oldzoom">

</body>
<script type="text/javascript" language="JavaScript">
document.all.mapboundframe.style.top = document.body.offsetHeight-162;
document.all.seltable_boundmap.style.top = document.all.mapboundframe.offsetTop+54;
document.all.mapId.value='<%=mapId%>'; 
var mapId = document.all.mapId.value;
var orgId = '<%=orgId%>';
var points="";
var state="pan";
var eventstate="";
var oldx=0;
var oldy=0;
var newx=0;
var newy=0;
var startx=0;
var starty=0;
var tempState=0;
var markY=0;
var wh = "?imagewidth=1210&imageheight=695";
var mapserviceurl='<%=basePath%>'+"gis/main-map/"+mapId;
var mapboundserviceurl='<%=basePath%>'+"gis/preview-map/"+mapId+"?imagewidth=150&imageheight=150";
document.onmousedown=mapmousedown;
document.onmousemove=mapmousemove;
document.onmouseup=mapmouseup;
document.getElementById('imgmap').onreadystatechange=downloadstate;
document.getElementById('boundmap').onreadystatechange=bounddownloadstate;
document.getElementById('seltable').onmousemove=tablemove;
window.name="mapwindow";
document.getElementById('imgmap').src=mapserviceurl+wh+"&action=init&orgId=" + orgId;
document.getElementById('boundmap').src=mapboundserviceurl+"&action=init&orgId=" + orgId;

//地图放大
function zoomOut_(){
  var imageURL1 = mapserviceurl+"?action=zoomout&refresh=" + Math.random();
  var objMapImage = document.getElementById("imgmap");
  objMapImage.src = imageURL1;
}
//地图放大或缩小N倍
function zoomOutIn(){
  var imageURL1 = mapserviceurl+"?action=zoomout&refresh=" + Math.random();
  var objMapImage = document.getElementById("imgmap");
  objMapImage.src = imageURL1;
}
//地图缩小
function zoomIn_(){
  var imageURL1 = mapserviceurl+"?action=zoomin&refresh=" + Math.random();
  var objMapImage = document.getElementById("imgmap");
  objMapImage.src = imageURL1;
}
//地图移动
function move_(type){
  var imageURL1 = mapserviceurl+"?action=move&movetype="+type+"&refresh=" + Math.random();
  var objMapImage = document.getElementById("imgmap");
  objMapImage.src = imageURL1;
}
//拖放地图
function drag_(){
  if((oldx>(newx+1)||oldx<(newx-1))||(oldy>(newy+1)&&oldy<(newy-1))){
    var imageURL1 = mapserviceurl+"?action=move&movetype=drag&startx="+oldx+"&starty="+oldy+"&endx="+newx+"&endy="+newy+"&refresh=" + Math.random();
    var objMapImage = document.getElementById("imgmap");
    objMapImage.src = imageURL1;
    document.all.imgmap.style.left=0;
		  document.all.imgmap.style.top=0;
  }else{
    var xkzhm = document.all("xkzhm").value;
    var sfkxg = document.all("sfkxg").value;
    if(sfkxg==null||sfkxg!=1){
     return;
    }
    CigStoreFinderService.getConverPoint(mapId,newx,newy,function(point){
     BaseService.updateCheckPoint(point[0],point[1],xkzhm,function(d){
      var imageURL1 = mapserviceurl+"?action=move&movetype=drag&startx="+oldx+"&starty="+oldy+"&endx="+newx+"&endy="+newy+"&refresh=" + Math.random();
	     var objMapImage = document.getElementById("imgmap");
	     objMapImage.src = imageURL1;
     })
    });
  }
}
//回到初始状态
function reset_(){
  var mapId_ = document.all.mapId.value;
  //mapserviceurl = '<%=basePath%>'+"gis/main-map/"+mapId_;
  var imageURL1 = mapserviceurl+"?action=defaultcenter&refresh=" + Math.random();
  var objMapImage = document.getElementById("imgmap");
  objMapImage.src = imageURL1;
}

//切换地图
function switchArea(){
  var mapId_ = document.all.mapId.value;
  //mapserviceurl = '<%=basePath%>'+"gis/main-map/"+mapId_;
  var imageURL1 = mapserviceurl+"?action=switcharea&areamapid="+mapId_+"&refresh=" + Math.random();
  var objMapImage = document.getElementById("imgmap");
  objMapImage.src = imageURL1;
}

//进行不同方式的选择：point 点选择 rectangle 矩形选择 region 多边形选择 pk 根据数据主键选择 circle 圆选择
function selectByType(type,id){
  var objMapImage = document.getElementById("imgmap");
  var imageURL1 = mapserviceurl+"?action=select&refresh=" + Math.random();
  if(type=="point"){
    var x=window.event.clientX-document.all.mapframe.scrollLeft;
		  var y=window.event.clientY-document.all.mapframe.scrollTop;
    imageURL1 += "&selecttype=point&x="+x+"&y="+y;
    objMapImage.src = imageURL1;
    parent.rightFrame.queryByPoint(mapId,x,y);
  }else if(type=="rectangle"){
    parent.rightFrame.queryByRectangle(mapId,startx,starty,newx,newy);
  }else if(type=="region"){
    var pointArray=points.split(";");
    var pcount = pointArray.length;
    parent.rightFrame.queryByRegion(mapId,pcount,points);
    points="";
  }else if(type=="pk"){
    imageURL1 += "&selecttype=pk&id="+id;
    objMapImage.src = imageURL1;
  }else if(type=="circle"){
    var x=window.event.clientX-document.all.mapframe.offsetLeft;
		  var y=window.event.clientY-document.all.mapframe.offsetTop;
		  var radius = 8;
    imageURL1 += "&selecttype=circle&x="+x+"&y="+y+"&radius="+radius;
    objMapImage.src = imageURL1;
    parent.rightFrame.queryByRadius(mapId,x,y,radius);
  }
  
  
}

//点击缩略图
function smallPoint(){
  if(window.event.srcElement.id=="boundmap"||window.event.srcElement.id=="seltable_boundmap"||window.event.srcElement.id==""){
     var x=window.event.clientX-document.all.boundmap.scrollLeft+10;
					var y=window.event.clientY-document.all.mapboundframe.offsetTop;
			  var imageURL1_preview = '<%=basePath%>'+"gis/preview-map/"+mapId+"?action=centerto&x="+x+"&y="+y+"&refresh=" + Math.random();
			  var objMapImage_preview = document.getElementById("boundmap");
			  objMapImage_preview.src = imageURL1_preview;
			  var imageURL1_main = mapserviceurl+"?&refresh=" + Math.random();
			  var objMapImage_main = document.getElementById("imgmap");
			  objMapImage_main.src = imageURL1_main;
			  document.all.regionSelect.checked = false;
			  document.all.rectangleSelect.checked = false;
			  document.all.circleSelect.checked = false;
  }
}

//矩形选择
function rectangleSelect(){
  if(document.all.rectangleSelect.checked){
    state = "big";
    if(document.all.regionSelect.checked){
      document.all.regionSelect.checked = false;
    }
    if(document.all.circleSelect.checked){
      document.all.circleSelect.checked = false;
    }
  }else{
    state = "pan";
  }
}

//多边形选择
function regionSelect(){
  if(document.all.regionSelect.checked){
    state = "region";
    document.all.mydiv.style.display="";
    if(document.all.rectangleSelect.checked){
      document.all.rectangleSelect.checked = false;
    }
    if(document.all.circleSelect.checked){
      document.all.circleSelect.checked = false;
    }
  }else{
    state = "pan";
    document.all.mydiv.style.display="none";
    points="";
    document.getElementById("mydiv").innerHTML="";
  }
}

//圆形选择
function circleSelect(){
  if(document.all.circleSelect.checked){
    state = "circle";
    document.all.mydiv.style.display="";
    if(document.all.rectangleSelect.checked){
      document.all.rectangleSelect.checked = false;
    }
    if(document.all.regionSelect.checked){
      document.all.regionSelect.checked = false;
    }
  }else{
    state = "pan";
    document.all.mydiv.style.display="none";
    points="";
    document.getElementById("mydiv").innerHTML="";
  }
}

//根据查询条件刷新地图
function findByCondition(co){
  BaseService.findByCondition(mapId,co,function callback(){
    var objMapImage = document.getElementById("imgmap");
    objMapImage.src = mapserviceurl+"?refresh=" + Math.random();;
  });
}

//画多边形
function draw()
{
 var temp_p = points.substring(0,points.length-1);
 var p=temp_p.split(";");
 if(p.length<2){
  oldx = p[0].substring(0,p[0].indexOf(","));
  oldy = p[0].substring(p[0].indexOf(",")+1,p[0].length);
  return ;
 }
	var polyline;
	var xmax;
	var ymax;
	var ctx;
	document.getElementById("mydiv").innerHTML="";
	if(document.all){
		polyline=document.createElement("v:polyline");
		polyline.strokecolor="red";
		polyline.points=temp_p;
	}else{
		polyline=document.createElement("canvas");
		xmax=0;
		ymax=0;
		for(i=0;i<p.length;i++)
		{
		if(p[i].substring(0,p[i].indexOf(","))-xmax>0)
		 xmax=p[i].substring(0,p[i].indexOf(","));
		if(p[i].substring(p[i].indexOf(",")+1,p[i].length)-ymax>0)
		 ymax=p[i].substring(p[i].indexOf(",")+1,p[i].length);
	}
	polyline.width=xmax;
	polyline.height=ymax;
	ctx=polyline.getContext("2d");
	ctx.strokeStyle="red";
	ctx.beginPath();
	ctx.moveTo(p[0].substring(0,p[0].indexOf(",")),p[0].substring(p[0].indexOf(",")+1,p[0].length));
	for(i=2;i<p.length;i++)
		ctx.lineTo(p[i].substring(0,p[i].indexOf(",")),p[i].substring(p[i].indexOf(",")+1,p[i].length));
	ctx.stroke();
	}
	polyline.id='polylineId';
	document.getElementById("mydiv").appendChild(polyline);
	
	var oval = document.createElement("v:oval");
	oval.id='ovalId';
	oval.style.position = 'relative';
	oval.style.left=oldx-3;
	oval.style.top=oldy-3;
	oval.style.width=5;
	oval.style.height=5;
	oval.fillcolor='red';
	document.getElementById("mydiv").appendChild(oval);
}

//画圆形
function draw_circle(){
 document.getElementById("mydiv").innerHTML="";
 var oval = document.createElement("v:oval");
	oval.id='ovalId';
	oval.style.position = 'relative';
	oval.style.width=0;
	oval.style.height=0;
	oval.fillcolor='red';
	document.getElementById("mydiv").appendChild(oval);
} 
</script>

</html>
