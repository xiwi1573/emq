<%@ page language="java" contentType="text/html; charset=GBK"%>
<%
	String WEBAPP = request.getContextPath();
	String DWR = WEBAPP + "/dwr/interface";	 
	String ExtRoot = request.getContextPath() + "/js/Ext2.1";
%>
<script type="text/javascript" src="<%=WEBAPP%>/jsp/base/consts.js"></script>
<!-- dwr支持库 -->
<script type='text/javascript' src='<%=DWRToolRoot%>/engine.js'></script>
<script type='text/javascript' src='<%=DWRToolRoot%>/util.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/comm/comm.js'></script>
<!-- ext支持库 -->
<link rel="stylesheet" type="text/css" href="<%=ExtRoot%>/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=ExtRoot%>/resources/css/icon.css" />

<script type="text/javascript" src="<%=ExtRoot%>/adapter/ext/ext-base.js"></script>
<!-- 
<script type="text/javascript" src="<%=ExtRoot%>/ext-all.js"></script>
 -->
<script type="text/javascript" src="<%=ExtRoot%>/ext-all-debug.js"></script>


<script type="text/javascript" src="<%=ExtRoot%>/icss/util/MixedCollectionEx.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/data/StoreEx.js"></script>

<script type="text/javascript" src="<%=ExtRoot%>/dwr-all-debug.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/global/Global.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/msg/Msg.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/util/Util.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/grid/lockgrid.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/grid/gridex.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/plugins/XGridPlugin.js"></script>

<script type="text/javascript" src="<%=ExtRoot%>/icss/date/DateItemEx.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/date/DateMenuEx.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/date/DatePickerEx.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/date/DateFieldEx.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/combox/ComboEx.js"></script>

<script type="text/javascript">Ext.BLANK_IMAGE_URL="<%=ExtRoot%>/resources/images/default/s.gif";</script>
<script type="text/javascript" src="<%=ExtRoot%>/icss/plugins/ProgressColumn/Ext.ux.grid.ProgressColumn.js"></script>
<link rel="stylesheet" type="text/css" href="<%=ExtRoot%>/icss/plugins/ProgressColumn/Ext.ux.grid.ProgressColumn.css" />

<script type="text/javascript" src="<%=ExtRoot%>/icss/plugins/GridSummary/Ext.ux.grid.GridSummary.js"></script>
<link rel="stylesheet" type="text/css" href="<%=ExtRoot%>/icss/plugins/GridSummary/Ext.ux.grid.GridSummary.css" />
<script type="text/javascript" src="<%=ExtRoot%>/icss/date/DatetimePicker.js"></script>
<script type="text/javascript" src="<%=ExtRoot%>/ext-lang-zh_CN.js"></script>

<link rel="stylesheet" type="text/css" href="<%=ExtRoot%>/resources/css/columnLock.css" />
<script type="text/javascript" src="<%=ExtRoot%>/icss/grid/lockgridex.js"></script>
<!-- Excel支持库 -->
<script type='text/javascript' src='<%=ExtRoot%>/icss/excel/excel_const.js'></script>
<script type='text/javascript' src='<%=ExtRoot%>/icss/excel/excel_app.js'></script>
<script type='text/javascript' src='<%=ExtRoot%>/icss/excel/excel.js'></script>