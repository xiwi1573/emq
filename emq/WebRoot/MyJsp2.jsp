<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
// dwr上下文路径
	String dwrContextPath = contextPath+"/dwr";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>right page</title>
  </head>

<script type='text/javascript' src='<%=dwrContextPath%>/engine.js'></script>
<script type='text/javascript' src='<%=dwrContextPath%>/util.js'></script>  
<script type='text/javascript' src='<%=dwrContextPath%>/interface/HomeService.js'></script>
<script type='text/javascript' src="<%=contextPath%>/js/common/util.js"></script>
<script type='text/javascript' src="<%=contextPath%>/js/Globals.js"></script>
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/css/style.css">
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/css/dhtmlXGrid.css">
<link rel="STYLESHEET" type="text/css" href="<%=request.getContextPath()%>/css/dhtmlXGrid_skins.css">
<script src="<%=request.getContextPath()%>/js/dhtmlXCommon.js"></script>
<script src="<%=request.getContextPath()%>/js/dhtmlXGrid.js"></script>
<script src="<%=request.getContextPath()%>/js/dhtmlXGridCell.js"></script>
<script src="<%=request.getContextPath()%>/js/dhtmlXGrid_splt.js"></script>
<script type="text/javascript">
/*var serverType = '1';
//初始化表格
function window.onload(){
  var co = {licenceNo:-99,storeName:null,corporationName:null,streetName:null,areaName:null,fareTypeName:null};
  initInfoTable(co);
}
//表格刷新
var infoTable = new dhtmlXGridObject('infoTableGrid');
function initInfoTable(co)
{
	getLoadMessage(waitMessage);
	if(infoTable){
	  infoTable.destructor();
	}
	infoTable = new dhtmlXGridObject('infoTableGrid');
	infoTable.init();
	infoTable.setImagePath("<%=request.getContextPath()%>/imgs/") ;
	infoTable.setSkin("light");
	HomeService.getTableXml(co,'0',function callback(xml)
	{
	 document.all.noteInfo.innerHTML = xml[1];
		infoTable.loadXMLString(xml[0]); 
		
	});
	infoTable.setOnRowSelectHandler(doOnRowSelected);
}
//处理行选择方法
function doOnRowSelected(id){
  getLoadMessage(waitMessage);
		HomeService.getDetailInfo(id,'',function callback(html){
		  document.all.detailInfoTable.innerHTML = html;
		});
		parent.centerFrame.selectByType('pk',id);
	}
	
	//点地图选择某点查询
	function queryByPoint(mapId,x,y){
	  HomeService.queryByPoint(mapId,x,y,serverType,function callback(xml){
	    getLoadMessage(waitMessage);
					if(infoTable){
					  infoTable.destructor();
					}
					infoTable = new dhtmlXGridObject('infoTableGrid');
					infoTable.init();
					infoTable.setImagePath("<%=request.getContextPath()%>/imgs/") ;
					infoTable.setSkin("light");
					document.all.noteInfo.innerHTML = xml[1];
					infoTable.loadXMLString(xml[0]); 
					infoTable.setOnRowSelectHandler(doOnRowSelected);
					document.all.detailInfoTable.innerHTML="";
	  });
	}
	
	//以地图某点圆查询
	function queryByRadius(mapId,x,y,radius){
	  HomeService.queryByRadius(mapId,x,y,radius,serverType,function callback(xml){
	    getLoadMessage(waitMessage);
					if(infoTable){
					  infoTable.destructor();
					}
					infoTable = new dhtmlXGridObject('infoTableGrid');
					infoTable.init();
					infoTable.setImagePath("<%=request.getContextPath()%>/imgs/") ;
					infoTable.setSkin("light");
					document.all.noteInfo.innerHTML = xml[1];
					infoTable.loadXMLString(xml[0]); 
					infoTable.setOnRowSelectHandler(doOnRowSelected);
					document.all.detailInfoTable.innerHTML="";
	  });
	}
	
		//矩形选择查询
	function queryByRectangle(mapId,x1,y1,x2,y2){
	  HomeService.findByRect(mapId,x1,y1,x2,y2,serverType,function callback(xml){
	    getLoadMessage(waitMessage);
					if(infoTable){
					  infoTable.destructor();
					}
					infoTable = new dhtmlXGridObject('infoTableGrid');
					infoTable.init();
					infoTable.setImagePath("<%=request.getContextPath()%>/imgs/") ;
					infoTable.setSkin("light");
					document.all.noteInfo.innerHTML = xml[1];
					infoTable.loadXMLString(xml[0]); 
					infoTable.setOnRowSelectHandler(doOnRowSelected);
					document.all.detailInfoTable.innerHTML="";
	  });
	}
	
			//多边行选择查询
	function queryByRegion(mapId,pcount,points){
	  HomeService.findByRegion(mapId,pcount,points,serverType,function callback(xml){
	    getLoadMessage(waitMessage);
					if(infoTable){
					  infoTable.destructor();
					}
					infoTable = new dhtmlXGridObject('infoTableGrid');
					infoTable.init();
					infoTable.setImagePath("<%=request.getContextPath()%>/imgs/") ;
					infoTable.setSkin("light");
					document.all.noteInfo.innerHTML = xml[1];
					infoTable.loadXMLString(xml[0]); 
					infoTable.setOnRowSelectHandler(doOnRowSelected);
					document.all.detailInfoTable.innerHTML="";
	  });
	}*/
</script>
<body scroll="no" topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">
 <div id="noteInfo"></div>
 <div id="infoTableGrid" width="100%" height="55%" style="background-color:white;"></div>
 <div id="detailInfoTable"/>
 
</body>
</html>
