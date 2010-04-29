  //Modified by yyz 2006-10-10
  
	/**
   * ȫ�ֱ�resultStr
   */
var inputDataArray = new Array(); //�����ά����
var valueArray = new Array();     //��ά����ID�б�
var showArray = new Array();      //��ά��������б�
var selectorTitle = "";       		//��-�����
var dataObj = null;
var nameObj = null;       			  //���ض���
var buttonObj = null;							//��ť����
var isMutiSelect = true;          //�Ƿ��ѡ   true ��ѡ��false ��ѡ
	/**
   * �ؼ���ʹ��
   */
initSelector();
function initSelector() {
	var htmlDiv = "";
	htmlDiv = "<div id=\"selectorControlDiv\" width=\"100\" style=\"overflow-y:yes;position:fixed;left:-100px;top:-165px;visibility:hidden;z-index:100;filter:progid:DXImageTransform.Microsoft.DropShadow(Color=#BFBFBF,OffX=4,OffY=4);\">";
	htmlDiv = htmlDiv + "</div>";
	document.write(htmlDiv);
}
/**
 *
 * ��ȡ���
 *	obj  ���鿴��input���� 
 *	titel ѡ������
 *	dataArray ����б�
 *	rowCount ÿ�з��õĸ���
 *	condition ��ʹ���
 *
 */
function getMutiSelect(obj, rObj,nObj,title, dataArray, rowCount, isMuti) {
  var condition = rObj.value;
	//����DIV
	hiddenLayer("selectorControlDiv");
	
	if(isMuti != null){
		isMutiSelect = isMuti;
	}else {
			alert("isMuti option is null");
	}
		
	//�����
	if (dataArray == null) {
		alert("input Array is null");
		return false;
	} else {
		inputDataArray = dataArray;
		valueArray = inputDataArray[0];//id
		showArray = inputDataArray[1];//title
		if (valueArray == null || showArray == null) {
			alert("input Array is empty");
			return false;
		}

	//����
		if (title != null) {
			selectorTitle = title;
		} else {
		}
		
	//������
	buttonObj = obj;

	//���ؽ�����		
		if (rObj != null) {
			dataObj = rObj;

		} else {
			alert("the return Obj is empty!");
			return false;
		}

   if (nObj != null) {
			nameObj = nObj;

		} else {
			alert("the return name Obj is empty!");
			return false;
		}
	//��ʹ�������
		if (condition == null) {
			condition = "all";
		}
	}
  //��̬���DIV����
	updataDiv(rowCount);
	
  //��ԭѡ������
	initDivSelectCondition(condition);
	
  //��ʾDIV
	displayLayer(obj);
}
function updataDiv(rowCount) {
	var htmlDiv = "";
	htmlDiv = htmlDiv + "<table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
	htmlDiv = htmlDiv + 	"<tr>";
	htmlDiv = htmlDiv + 		"<td height=\"24\" nowarp>";
	htmlDiv = htmlDiv + 			"<table width=\"100%\" height=\"5\" border=\"0\" bgcolor=\"#0886E7\">";
	htmlDiv = htmlDiv + 				"<tr>";
	htmlDiv = htmlDiv + 					"<td align=\"left\" valign=\"middle\" height=\"5\">&nbsp;<span style=\"color:#FFFFFF\"><font size=\"2\">\u8bf7\u9009\u62e9" + selectorTitle + "</font></span></td>";
	htmlDiv = htmlDiv + 					"<td align=\"right\" valign=\"middle\" height=\"5\">";
	htmlDiv = htmlDiv + 						"<table border=\"0\">";
	htmlDiv = htmlDiv + 							"<tr>";
	
	//��ѡ
	if(isMutiSelect=="true"){
		htmlDiv = htmlDiv + 								"<td align=\"left\" valign=\"middle\" height=\"5\"><span><a onclick=\"selectAllUnit();\" style=\"color:#FFFFFF;cursor:hand;\"><font size=\"2\">[\u5168\u9009]</font></a></span></td>";
		htmlDiv = htmlDiv + 								"<td align=\"left\" valign=\"middle\" height=\"5\"><span><a onclick=\"emptyAllUnit();\" style=\"color:#FFFFFF;cursor:hand;\"><font size=\"2\">[\u6e05\u7a7a]</font></a></span></td>";
	}
	
	htmlDiv = htmlDiv + 								"<td align=\"left\" valign=\"middle\" height=\"5\"><span><a onclick=\"returnResult();\" style=\"color:#FFFFFF;cursor:hand;\"><font size=\"2\">[\u786e\u5b9a]</font></a></span></td>";
	htmlDiv = htmlDiv + 								"<td align=\"left\" valign=\"middle\" height=\"5\"><span><a onclick=\"closeWindow();\" style=\"color:#FFFFFF;cursor:hand;\"><font size=\"2\">[CLOSE]</font></a></span></td>";
	htmlDiv = htmlDiv + 							"</tr>";
	htmlDiv = htmlDiv + 						"</table>";
	htmlDiv = htmlDiv + 					"</td>";
	htmlDiv = htmlDiv + 				"</tr>";
	htmlDiv = htmlDiv + 			"</table>";
	htmlDiv = htmlDiv + 		"</td>";
	htmlDiv = htmlDiv + 	"</tr>";
	htmlDiv = htmlDiv + 	"<tr >";
	htmlDiv = htmlDiv + 		"<td colspan=\"2\" valign=\"top\">";
	htmlDiv = htmlDiv + 		" <table bgcolor=\"#EFEFEF\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFFFFF\">";

	
	var brd_i = 0;
	var colCount = 0;
	for(brd_i = 0;brd_i<showArray.length;brd_i++){
	  if(valueArray[brd_i]=="brd"){
	    colCount = 0;
	    if(brd_i!=0){
	      tmlDiv = htmlDiv + 	"</tr>";
	    }
	  	 htmlDiv = htmlDiv + 	"<tr bgcolor=\"#C8E1FB\">";
	  	 htmlDiv = htmlDiv + 	"<td height=\"24\" width=\"5\" align=\"center\"  nowrap></td>";
	  	 htmlDiv = htmlDiv + 	"<td height=\"24\" width=\"92\" align=\"left\" colspan=\"10\" nowrap><a style=\"cursor:hand\"><font size=\"3\" bold color=\"#0000CC\"><b>" + showArray[brd_i] + "</b></font></a></td>";
	    htmlDiv = htmlDiv + 	"</tr>";
	    htmlDiv = htmlDiv + 	"<tr>";
	  }else{
	    if(colCount%5==0){
	      htmlDiv = htmlDiv + "</tr>";
			    htmlDiv = htmlDiv + "<tr>";
	    }
	    htmlDiv = htmlDiv + 	"<td height=\"24\" align=\"center\" nowrap><input type=\"checkbox\" name=\"" + valueArray[brd_i] + "\"></td>";
	    htmlDiv = htmlDiv + 	"<td height=\"24\" width=\"140\" align=\"left\" nowrap><a style=\"cursor:hand\"><font size=\"2\">" + showArray[brd_i] + "</font></a></td>";
	    colCount = colCount+1;
	  }
	}
	tmlDiv = htmlDiv + 	"</tr>";
	htmlDiv = htmlDiv + "	</table></td></tr>";

		  htmlDiv = htmlDiv + '<tr><td colspan="2" height="4" whith="100%" bgcolor="#0886E7"></td></tr>';
	htmlDiv = htmlDiv + "</table>";
	document.getElementById("selectorControlDiv").innerHTML = htmlDiv;
}

  //�ָ��ύ����
function initDivSelectCondition(condition) {
	  //alert("condition.lenth = " + condition.length + " condtion=("+condition+")");
	  
	if(isMutiSelect == "true"){
	var selectedID = "";
	var start = 1;
	var end = 0;
	if (condition == "all") {
		for (var k = 0; k < valueArray.length; k++) {
			document.getElementById(valueArray[k]).checked = true;
		}
	}
	if (condition == "empty" || condition =="") {
		return false;
	}
	
	if (condition != null && condition != "" && condition.length != 0) {
	  var mark = condition.indexOf(",");
	  if(mark==-1){
	    var _checkboxName = condition.substr(1,condition.length-2);
	    for (var k = 0; k < valueArray.length; k++) {
								if (valueArray[k] == _checkboxName) {
									document.getElementById(_checkboxName).checked = true;
								}
					}
	  }else{
	    for (i = 0; i < condition.length; i++) {
	      var _checkboxName = condition.substr(i+1,mark-2);
	      for (var k = 0; k < valueArray.length; k++) {
								if (valueArray[k] == _checkboxName) {
									document.getElementById(_checkboxName).checked = true;
								}
					  }
					 i = i+mark;
	    }
	  }
	}
}else {
		if(condition != null && condition != "" && condition != "all" && condition != "empty" && condition.length != 0){
			document.getElementById("singleSelectRadio").value = condition;
			//alert(document.getElementById("singleSelectRadio").value);
		//singleSelectRadio[i].checked = true;
	}
	}
}

  //ȫѡ��Ԫ��
function selectAllUnit() {
	for (var k = 0; k < valueArray.length; k++) {
	 if(valueArray[k]!='brd'){
	   document.getElementById(valueArray[k]).checked = true;
	 }
	}
}
  //��յ�Ԫ��
function emptyAllUnit() {
	for (var k = 0; k < valueArray.length; k++) {
	 if(valueArray[k]!='brd'){
		  document.getElementById(valueArray[k]).checked = false;
		}
	}
	
}

function closeWindow() {
	hiddenLayer("selectorControlDiv");
	document.getElementById("selectorControlDiv").innerHTML = "";
}
  //���ؽ��
function returnResult() {
	var resultStr = "";
	var resultnameStr = "";
	var isEmpty = true;
	var isFull = true;
	var tempStr = "\u9009\u4e2d\u7684\u7c92\u5ea6\u4e3a:\r\n\r\n";

	//��ѡ
	if(isMutiSelect=="true"){
	
	for (var k = 0; k < valueArray.length; k++) {
	if(valueArray[k]!="brd"){
		if (document.getElementById(valueArray[k]).checked == true) {
			resultStr = resultStr + "'"+valueArray[k] + "',";
			resultnameStr = resultnameStr + showArray[k] + ",";
			tempStr = tempStr + valueArray[k] + "  " + showArray[k] + "\r\n";
			isEmpty = false;
		}else {
			isFull = false;
		}
		}
	}
	
	if (isEmpty) {
		resultStr = "";
		resultnameStr = "";
	}
	if (isFull){
		resultStr = "all";
		resultnameStr = "all";
	}
	//ȥ�����ʱ�� ,
	if(resultStr.substr((resultStr.length -1),(resultStr.length )) == ","){
		resultStr = resultStr.substr(0,(resultStr.length - 1 ));
		resultnameStr = resultnameStr.substr(0,(resultnameStr.length - 1 ));
		//alert(resultStr);
	}
	
	//��ѡ
	}else {
	alert(document.all.singleSelectRadio.value);
		resultStr = document.getElementById("singleSelectRadio").value ;

	}
	
	hiddenLayer("selectorControlDiv");
	dataObj.value = resultStr;
	nameObj.value = resultnameStr;

	form_submit();
}

  //��ʾͼ��
function displayLayer(obj) {
	hiddenLayer("selectorControlDiv");
	var dv = document.getElementById("selectorControlDiv");
	
	GetCenterXY_ForLayer(obj, dv);
	dv.style.visibility = "visible";
	//DivSetVisible(dv);
  //dv.style.visibility = "visible";
}

  //����ͼ��
function hiddenLayer() {
	var dv = document.getElementById("selectorControlDiv");
	dv.style.visibility = "hidden";
	
}

  //������λ�ö�λ�ڿؼ��·�
function GetCenterXY_ForLayer(obj, div) {
	
	//�����ƶ�λ��
	x = document.body.scrollLeft;
	y = document.body.scrollTop;

  //��ȡ��Ļ���
	availWidth = parseInt(window.screen.availWidth);
	availHeight = parseInt(window.screen.availHeight);
	//div���
	tblWidth = parseInt(div.width);
	
	var divStyle = div.style;
	var ttop = obj.offsetTop;     //obj�ؼ��Ķ�λ���
	var thei = obj.clientHeight;  //obj�ؼ�����ĸ�
	var tleft = obj.offsetLeft;    //obj�ؼ��Ķ�λ���
	var ttyp = obj.type;          //obj�ؼ�������
	while (obj = obj.offsetParent) {
		ttop += obj.offsetTop;
		tleft += obj.offsetLeft;
	}
	divStyle.top = (ttyp == "image") ? ttop + thei : ttop + thei + 6;
	//divͼ������Ļ���
	if((availWidth - tleft) < tblWidth){
	
		//div ��ȴ�����Ļ���
		if(tblWidth > availWidth){
			divStyle.left = 6;
		}else {
			divStyle.left = availWidth-tblWidth-6;
		}
	}else {
		divStyle.left = tleft;
	}
}

  //���һ����ֵ
function sltIssuedate(show, value) {
          //document.all("issuedate").value = value;
	document.quicksearch.issuedate.value = value;
	document.all("btnSltIssuedate").value = show;
	document.all("btnSltIssuedate1").value = show;
          //hiddenLayer("selectorControlDiv");
}
function DivSetVisible(objDiv) {
          //var IfrRef = document.getElementById('DivShim');

          //IfrRef.style.width = objDiv.offsetWidth;
          //IfrRef.style.height = objDiv.offsetHeight;
          //IfrRef.style.top = objDiv.style.top;
          //IfrRef.style.left = objDiv.style.left;
          //IfrRef.style.zIndex = objDiv.style.zIndex - 1;

          //IfrRef.style.display = "block";
	hideElementAll();
	objDiv.style.visibility = "visible";
}
function HideElement(strElementTagName) {
	try {
		for (i = 0; i < window.document.all.tags(strElementTagName).length; i++) {
			var objTemp = window.document.all.tags(strElementTagName)[i];
			objTemp.style.visibility = "hidden";
		}
	}
	catch (e) {
		alert(e.message);
	}
}
function ShowElement(strElementTagName) {
	try {
		for (i = 0; i < window.document.all.tags(strElementTagName).length; i++) {
			var objTemp = window.document.all.tags(strElementTagName)[i];
			objTemp.style.visibility = "visible";
		}
	}
	catch (e) {
		alert(e.message);
	}
}
function hideElementAll() {
	HideElement("SELECT");
	HideElement("OBJECT");
	HideElement("IFRAME");
}
function showElementAll() {
	ShowElement("SELECT");
	ShowElement("OBJECT");
	ShowElement("IFRAME");
}

function document.onclick(){
  /*with(window.event)
  { if (srcElement.getAttribute("Author")==null && document.activeElement != document.getElementById("selectorControlDiv") && srcElement != dataObj && srcElement != buttonObj)
    hiddenLayer("selectorControlDiv");
  }*/
}

function document.onkeyup(){
 if (window.event.keyCode==27){
		if(dataObj)dataObj.blur();
		if(nameObj)nameObj.blur();
		hiddenLayer("selectorControlDiv");
	}else if(document.activeElement)
		if(document.activeElement.getAttribute("Author")==null && document.activeElement != document.getElementById("selectorControlDiv") && document.activeElement != dataObj && document.activeElement != nameObj && document.activeElement != buttonObj)
		{
			hiddenLayer("selectorControlDiv");
		}
 }
		