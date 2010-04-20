<%@ page language="java" pageEncoding="GBK"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%
	String contextPath = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ contextPath + "/";
	String orgId = request.getParameter("orgId");
	if(orgId==null){
	 orgId = "";
	}
%>
<html>
<tiles:useAttribute name="topControl" />
<tiles:useAttribute name="rightControl" />
<head>
<title>昆明GIS</title>
<link rel="stylesheet" type="text/css"
	href="<%=contextPath %>/css/ext/resources/css/ext-all.css" />
<!-- LIBS -->
<script type="text/javascript"
	src="<%=contextPath %>/js/ext/adapter/ext/ext-base.js"></script>
<!-- ENDLIBS -->
<script type="text/javascript" src="<%=contextPath %>/js/ext/ext-all.js"></script>
<style type="text/css">
	html, body {
        font:normal 12px verdana;
        margin:0;
        padding:0;
        border:0 none;
        overflow:hidden;
        height:100%;
    }
	p {
	    margin:5px;
	}
    .settings {
        background-image:url(<%=contextPath %>/css/shared/icons/fam/folder_wrench.png);
    }
    .nav {
        background-image:url(<%=contextPath %>/css/shared/icons/fam/folder_go.png);
    }
    </style>
<script type="text/javascript">

    var rightControl="<bean:write name="rightControl" />";	
    var topControl="<bean:write name="topControl" />";	
    var conditionControl='<tiles:getAsString name="conditionControl"/>';	
    var conditionHeight = 55;
    if(conditionControl=="1"){
      conditionHeight = 92;
    }else if(conditionControl=="2"){
      conditionHeight = 55;
    }else{
      conditionHeight = parseInt(conditionControl);
    }
    
    var top_frame = '<%=contextPath%><tiles:getAsString name="top_frame"/>';
    if(top_frame.indexOf('?') > 0){
    	top_frame = top_frame + '&orgId=<%=orgId%>';
    }else{
    	top_frame = top_frame + '?orgId=<%=orgId%>';
    } 
    
    Ext.onReady(function(){
        if(rightControl=="1"){
          if(topControl=="1"){
            var viewport = new Ext.Viewport({
            layout:'border',
            id:'view',
            items:[
                {
                    region:'east',
                    title:'详细信息',
                    split:true,
                    width: 250,
                    minSize: 0,
                    maxSize: 1024,
                    collapsible: true,
                    margins:'0 0 0 0',
                    layout:'accordion',
                    layoutConfig:{
                        animate:true
                    },
                    html:'<iframe scrolling="no" name="rightFrame" src="<%=contextPath%><tiles:getAsString name="right"/>" width="100%" height="98%">'      
                 },
                 {
                    region:'north',
                    id:'tt',
                    title:'查询条件',
                    split:true,
                    height: conditionHeight,
                    minSize: 0,
                    maxSize: 200,
                    collapsible: true,
                    margins:'0 0 0 0',
                    layout:'accordion',
                    layoutConfig:{
                        animate:true
                    },
                    html:'<iframe scrolling="no" name="topFrame" src="' + top_frame + '" width="100%" height="100%">'  
                },
                {
                    region:'center',
                    width: 800,
                    minSize: 175,
                    maxSize: 400,
                    margins:'0 0 0 0',
                    html:'<iframe name="centerFrame" src="<%=contextPath%><tiles:getAsString name="main_frame"/>&orgId=<%=orgId%>" width="100%" height="103%">'  
                }
             ]
        });
          }else{
            var viewport = new Ext.Viewport({
            layout:'border',
            items:[
                {
                region:'east',
                    title:'详细信息',
                    split:true,
                    width: 250,
                    minSize: 0,
                    maxSize: 1024,
                    collapsible: true,
                    margins:'0 0 0 0',
                    layout:'accordion',
                    layoutConfig:{
                        animate:true
                    },
                    listeners:{
																      'expand':function(){
																        widthChange('open');
																      },
																      'collapse':function(){
																        widthChange('close');
																      }
																    },
                    html:'<iframe scrolling="no" name="rightFrame" src="<%=contextPath%><tiles:getAsString name="right"/>" width="100%" height="98%">'      
                 },
                {
                    region:'center',
                    width: 800,
                    minSize: 175,
                    maxSize: 400,
                    margins:'0 0 0 0',
                    html:'<iframe name="centerFrame" src="<%=contextPath%><tiles:getAsString name="main_frame"/>&orgId=<%=orgId%>" width="100%" height="103%">'  
                }
             ]
        });
          }
        
        
    }else{
    if(topControl=="1"){
      var viewport = new Ext.Viewport({
            layout:'border',
            items:[
                 {
                    region:'north',
                    title:'查询条件',
                    split:true,
                    height: conditionHeight,
                    minSize: 0,
                    maxSize: 200,
                    collapsible: true,
                    margins:'0 0 0 0',
                    layout:'accordion',
                    layoutConfig:{
                        animate:true
                    },
                    listeners:{
																      'expand':function(){
																        heightChange('open');
																      },
																      'collapse':function(){
																        heightChange('close');
																      }
																    },
                    html:'<iframe scrolling="no" name="topFrame" src="<%=contextPath%><tiles:getAsString name="top_frame"/>?orgId=<%=orgId%>" width="100%" height="100%">'  
                },
                {
                    region:'center',
                    width: 800,
                    minSize: 175,
                    maxSize: 400,
                    margins:'0 0 0 0',
                    html:'<iframe name="centerFrame" src="<%=contextPath%><tiles:getAsString name="main_frame"/>&orgId=<%=orgId%>" width="100%" height="103%">'  
                }
             ]
        });
    }else{
      var viewport = new Ext.Viewport({
            layout:'border',
            items:[
                {
                    region:'center',
                    width: 800,
                    minSize: 175,
                    maxSize: 400,
                    margins:'0 0 0 0',
                    html:'<iframe name="centerFrame" src="<%=contextPath%><tiles:getAsString name="main_frame"/>&orgId=<%=orgId%>" width="100%" height="103%">'  
                }
             ]
        });
    }
      }
   });
   
<%--function heightChange(type){
  var imageURL1;
  if(type=="close"){
    imageURL1 = '<%=basePath%>'+"servlet/CigStoreMapServlet?imageheight=552&refresh=" + Math.random();
  }else{
    imageURL1 = '<%=basePath%>'+"servlet/CigStoreMapServlet?imageheight=502&refresh=" + Math.random();
  }
  var objMapImage = parent.centerFrame.document.getElementById("imgmap");
  objMapImage.src = imageURL1;
}

function widthChange(type){
  var imageURL1;
  if(type=="close"){
    imageURL1 = '<%=basePath%>'+"servlet/CigStoreMapServlet?imagewidth=1011&refresh=" + Math.random();
  }else{
    imageURL1 = '<%=basePath%>'+"servlet/CigStoreMapServlet?imagewidth=860&refresh=" + Math.random();
  }
  var objMapImage = parent.centerFrame.document.getElementById("imgmap");
  objMapImage.src = imageURL1;
}
	--%></script>
</head>
</html>
