<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
// dwr上下文路径
	String dwrContextPath = contextPath+"/dwr";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>condition page</title>
  </head>
<script type='text/javascript' src='<%=dwrContextPath%>/engine.js'></script>
<script type='text/javascript' src='<%=dwrContextPath%>/util.js'></script>  
<script type='text/javascript' src='<%=dwrContextPath%>/interface/BaseService.js'></script>
<script type='text/javascript' src='<%=contextPath%>/js/common/util.js'></script>
<script type='text/javascript' src='<%=contextPath%>/js/Globals.js'></script>
<script type='text/javascript' src='<%=contextPath%>/js/common/calendar.js'></script>
<link rel="STYLESHEET" type="text/css" href="<%=contextPath%>/css/style.css">
<script type="text/javascript">
function window.onload(){
  var conditionArray = new Array();
  conditionArray[0] = "licenceNo";
  conditionArray[1] = "storeName";
  conditionArray[2] = "corporationName";
  conditionArray[3] = "areaName";
  conditionArray[4] = "streetName";
  conditionArray[5] = "fareTypeName";
  //conditionArray[6] = "logoutTime";
  getCondition(conditionArray);//调用getCondition(array)来初始化查询条件页面
}

function query(){
  //为查询条件对象赋值
  var co = {licenceNo:document.all.licenceNo.value,storeName:document.all.storeName.value,corporationName:document.all.corporationName.value,streetName:document.all.streetName.value,areaName:document.all.areaName.value,fareTypeName:document.all.fareTypeName.value};
  var flag = valueCheck(co);
  if(flag){
   parent.rightFrame.initInfoTable(co);//调用表格页面刷新表格
   parent.rightFrame.document.all.detailInfoTable.innerHTML="";//清空表格页面详细信息
   parent.centerFrame.findByCondition(co);
  }
}

function tt(){
 BaseService.runConverter();
}
</script>
<body scroll="no" topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">
 <div id="conditionTable"/>
</body>
</html>
