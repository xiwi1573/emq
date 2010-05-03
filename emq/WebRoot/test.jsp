<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<script type="javascript">

</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>鏈虹エ瀵煎叆</title>

<%@ include file="comm/comm.jsp" %>
<!-- dwr鎺ュ彛 -->
<script type="text/javascript" src="<%=ExtRoot%>/icss/excel/fileImport.js"></script>
<script type="text/javascript" src="<%=DWR%>/PlantService.js"></script>

<script type="text/javascript" src="test.js"></script>
</head>

<body>
<input type="hidden" name="path" value="<%=request.getContextPath()%>/fileImportServlet"/>  
<%   
	request.getSession().setAttribute("forward","EMQ/test.jsp");
%>
</body>

