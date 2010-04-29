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
	htmlDiv = "<div id=\"selectorControlDiv\" width=\"100\" style=\"overflow-y:yes;position:absolute;left:-100px;top:-165px;visibility:hidden;z-index:100;filter:progid:DXImageTransform.Microsoft.DropShadow(Color=#BFBFBF,OffX=4,OffY=4);\">";
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
	var j = -1;// 
	htmlDiv = htmlDiv + 	"<tr>";
	var flag = false;
	for (var k = 0; k < showArray.length; k++) {
	  if(showArray[k]=="over"){
	    flag = true;
	    break;
	  }
	}
	for (var k = 0; k < showArray.length; k++) {
		htmlDiv = htmlDiv + 	"<td height=\"24\" width=\"5\" align=\"center\" nowrap></td>";
		//alert(isMutiSelect);
		if(flag){
		 if(showArray[k]=="over"){
		  htmlDiv = htmlDiv + "</tr>";
			 htmlDiv = htmlDiv + "<tr>";
		}else{
		  if(isMutiSelect == "true"){
		//��ѡ
				htmlDiv = htmlDiv + 	"<td height=\"24\" align=\"center\" nowrap><input type=\"checkbox\" name=\"" + valueArray[k] + "\"></td>";
		}else {
		//��ѡ
						htmlDiv = htmlDiv + 	"<td height=\"24\" align=\"center\" nowrap><input type=\"radio\" name=\"singleSelectRadio\" value=\"" + valueArray[k] + "\"></td>";
		}																																					

		htmlDiv = htmlDiv + 	"<td height=\"24\" width=\"92\" align=\"left\" nowrap><a style=\"cursor:hand\"><font size=\"2\">" + showArray[k] + "</font></a></td>";
		htmlDiv = htmlDiv + 	"<td height=\"24\" width=\"1\" align=\"center\" nowrap></td>";
		if (((k + 1) % rowCount) != 0 && k != (showArray.length - 1)) {
			//htmlDiv = htmlDiv + "<td   width=\"1\" height=\"24\" align=\"center\" valign=\"middle\"><span style=\"color:#B8B8B8\">��</span></td>";
		}
		//if (k % rowCount == rowCount - 1) {
			//htmlDiv = htmlDiv + "</tr>";
			//htmlDiv = htmlDiv + "<tr>";
		//}
		}
	}else{
		 if(isMutiSelect == "true"){
		//��ѡ
				htmlDiv = htmlDiv + 	"<td height=\"24\" align=\"center\" nowrap><input type=\"checkbox\" name=\"" + valueArray[k] + "\"></td>";
		}else {
		//��ѡ
						htmlDiv = htmlDiv + 	"<td height=\"24\" align=\"center\" nowrap><input type=\"radio\" name=\"singleSelectRadio\" value=\"" + valueArray[k] + "\"></td>";
		}																																					
		htmlDiv = htmlDiv + 	"<td height=\"24\" width=\"92\" align=\"left\" nowrap><a style=\"cursor:hand\"><font size=\"2\">" + showArray[k] + "</font></a></td>";
		htmlDiv = htmlDiv + 	"<td height=\"24\" width=\"1\" align=\"center\" nowrap></td>";
		if (((k + 1) % rowCount) != 0 && k != (showArray.length - 1)) {
			//htmlDiv = htmlDiv + "<td   width=\"1\" height=\"24\" align=\"center\" valign=\"middle\"><span style=\"color:#B8B8B8\">��</span></td>";
		}
		if (k % rowCount == rowCount - 1) {
			htmlDiv = htmlDiv + "</tr>";
			htmlDiv = htmlDiv + "<tr>";
		}
	}
		
		
	}
	if (j > 0) {
		for (var l = 1; l <= j; l++) {
			htmlDiv = htmlDiv + 	"<td height=\"24\">&nbsp;</td>";
			htmlDiv = htmlDiv + 	"<td width=\"1\" height=\"24\" align=\"center\" valign=\"middle\"><img src=\"images/line_bg.gif\"></td>";
		}
		htmlDiv = htmlDiv + 	"</tr>";
	}
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
	 if(valueArray[k]!=''){
	   document.getElementById(valueArray[k]).checked = true;
	 }
	}
}
  //��յ�Ԫ��
function emptyAllUnit() {
	for (var k = 0; k < valueArray.length; k++) {
	 if(valueArray[k]!=''){
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
	if(valueArray[k]!=""){
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

function document.onclick() //������ʱ�رոÿؼ�	//ie6����������������л����㴦�����
{
  /*with(window.event)
  { if (srcElement.getAttribute("Author")==null && document.activeElement != document.getElementById("selectorControlDiv") && srcElement != dataObj && srcElement != buttonObj)
    hiddenLayer("selectorControlDiv");
  }*/
}

function document.onkeyup()		//��Esc��رգ��л�����ر�
  {
    if (window.event.keyCode==27){
		if(dataObj)dataObj.blur();
		if(nameObj)nameObj.blur();
		hiddenLayer("selectorControlDiv");
	}
	else if(document.activeElement)
		if(document.activeElement.getAttribute("Author")==null && document.activeElement != document.getElementById("selectorControlDiv") && document.activeElement != dataObj && document.activeElement != nameObj && document.activeElement != buttonObj)
		{
			hiddenLayer("selectorControlDiv");
		}
  }
		