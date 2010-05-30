<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>condition page</title>
  </head>
<%@ include file="comm/comm.jsp" %>
<script type="text/javascript" src="<%=WEBAPP%>/js/ext/icss/excel/fileImport.js"></script>
<script type="text/javascript" src="<%=DWR%>/PlantService.js"></script>
<script type="text/javascript" src="<%=WEBAPP%>/js/common/util.js"></script>

<script type="text/javascript">
function window.onload(){
  var conditionArray = new Array();
  conditionArray[0] = "licenceNo";
  getCondition(conditionArray);
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
function test(){
 PlantService.getPersonById("liuyt");
}
</script>
<body scroll="no" topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">
 <div id="conditionTable"></div>
 <!-- <input type="button" onclick="test()"></input> -->
</body>
</html>
