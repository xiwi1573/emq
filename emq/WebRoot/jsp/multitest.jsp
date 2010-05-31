<%@ page language="java" contentType="text/html; charset=GBK"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@ include file="../comm/comm.jsp"%>
		<script type="text/javascript" src="<%=DWR%>/PlantService.js"></script>
		<script type="text/javascript" src="likeGrid.js"></script>

		<title>right page</title>
		<style type="text/css">
		#gridLike {background:#fff;border:1px #ccc solid;}
        #gridLike td {border:1px #ccc solid; border-top:none; border-left:none;text-align:center;font-size:10pt; }
        #gridLike th {background:#f9f9f9; width:100px;height:22px;border:1px #ccc solid; border-top:none; border-left:none;text-align:center;font-size:10pt; }
		</style>
	</head>

	<body scroll="no" topmargin="0" leftmargin="0" rightmargin="0"
		bottommargin="0">
    <div id='mainTable' style="overflow:scroll;"></div>
	</body>
</html>
