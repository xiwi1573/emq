//功能：util.js主要功能为构造查询条件栏中各类查询条件
//作者：liuyt
//生成日期：2009-9-1
/*
 * @history 2009-10-30 guqiong add getCorpCodeFormHTMLControl
 */
/**
	 * 构造查询条件html
	 * 固定下拉框location:当前位置 如果存在corpCode:县区市查询条件则当前位置下拉框不存在
	 * @param conditionArray 过滤条件对象数组
	 * 数组对象中的合法值：
	 * licenceNo:许可证号 storeName:商店名称 corporationName:法人姓名 streetName:街道名称 
  * areaName:地区名称 fareTypeName:经营业态 fareAddress:经营地址 line:线路rank:等级
  * paperTime:办证日期 brd:品牌 cig:规格 corpCode:县区市 sellType:零售业态 markType:市场类型
  * fareSize:经营规模 logoutTime:注销日期 noOrderMonth:不定货月数 specialRank:专卖等级
  * pc:地图校验上传文件批次 dataType:需采点数据(1),已上传校正数据(2),完成校正数据(3)
	 */
var orgId_ = '';
function getCondition(conditionArray){
	getCondition_cmp(conditionArray,'');
}
//触摸屏查询条件构造
function getCondition_cmp(conditionArray,orgId){
	orgId_ = orgId;
	condition = conditionArray;
	var conditionHtml = "";
	var lineCount = 5;
	var flag = true;
	if(conditionArray.length>lineCount){
		conditionHtml = "<table><tr><td><table width='100%' align='left'><tr>";
		for(var i=0;i<lineCount;i++){
			 var parameter = conditionArray[i];
			 if(parameter=="corpCode"){
			 	flag = false;
			 }
			 conditionHtml += addCondition(parameter);
		}
		conditionHtml += "<td width='194'></td></tr></table></td></tr><tr><td><table width='100%'><tr>";
		for(var i=lineCount;i<conditionArray.length;i++){
			 var parameter = conditionArray[i];
			 if(parameter=="corpCode"){
			 	flag = false;
			 }
			 conditionHtml += addCondition(parameter);
		}
		conditionHtml += "<td><input id='queryId' type='button' onclick='query()' value='查询'/></td>";
		if(orgId!=""){
			conditionHtml += "<td><input type='button' onclick='return_()' value='返回'/></td>";
		}
	 if(flag){
	 	conditionHtml += "<td align='right' width='68'><font size='2'>当前位置:</font></td><td id='locationSelect' align='right' width='1'></td>";
	 }
	 conditionHtml += "</tr></table></td></tr></table>";
	}else{
		conditionHtml = "<table width='100%'><tr>";
		for(var i=0;i<conditionArray.length;i++){
		 var parameter = conditionArray[i];
		 if(parameter=="corpCode"){
			 flag = false;
			}
		 conditionHtml += addCondition(parameter);
	 }
	 conditionHtml += "<td align='left'><input id='queryId' type='button' onclick='query()' value='查询'/></td>";
	 if(flag){
	 	conditionHtml += "<td align='right' width='68'><font size='2'>当前位置:</font></td><td id='locationSelect' align='right' width='1'></td>";
	 }
	 conditionHtml += "</tr></table>";
	}
	document.all.conditionTable.innerHTML = conditionHtml;
	if(flag){
		initLocationSelect();
	}
	for(var i=0;i<conditionArray.length;i++){
		var parameter = conditionArray[i];
		if(parameter=="streetName"){
			initStreetSelect('-99','-99');
		}else if(parameter=="fareTypeName"){
			initFareTypeSelect();
		}else if(parameter=="rank"){
			initRankSelect();
		}else if(parameter=="paperTime"){
			document.all.paperTime_start.value=getStartTime();
			document.all.paperTime_end.value=getEndTime();
		}else if(parameter=="brd"){
			initBrdSelect();
		}else if(parameter=="cig"){
			initCigSelect('');
		}else if(parameter=="corpCode"){
			initCorp();
		}else if(parameter=="logoutTime"){
			document.all.logoutTime_start.value=getStartTime();
			document.all.logoutTime_end.value=getEndTime();
		}else if(parameter=="specialRank"){
			initSpecialRankSelect();
		}
	}
}
/**
 * 获取默认开始时间
 * @return {}
 */
function getStartTime(){
	var currentDate = new Date();
	var year = currentDate.getYear(); 
	var month = currentDate.getMonth()+1;
	if(month<10){
		month = "0"+month;
	}
	var day = currentDate.getDate();
	if(day<10){
		day = "0"+day;
	}
	var startTime = year+"-"+month+"-"+day;
	return startTime;
}

/**
 * 获取默认结束时间
 * @return {}
 */
function getEndTime(){
	var currentDate = new Date();
	var year = currentDate.getYear(); 
	var month = currentDate.getMonth()+1;
	if(month<10){
		month = "0"+month;
	}
	var lastDay = new Date(year,month,0).getDate();
	var endTime = year+"-"+month+"-"+lastDay;
	return endTime;
}
/**
	 * 根据参数构造不同的查询对象控件
	 * @param par 查询对象参数
	 * @return 某个查询对象控件html
	 */
function addCondition(par) {
	var temp = "";
	if (par == "licenceNo") {
		temp += "<td width='60' nowrap><font size='2'>许可证号:</font></td><td  align='left' width='1'><input size='10' id='licenceNo' name='licenceNo' type='text'/></td>";
	} else if (par == "storeName") {
		temp += "<td width='60' nowrap><font size='2'>商店名称:</font></td><td  width='1'><input id='storeName' size='10' name='storeName' type='text'/></td>";
	} else if (par == "corporationName") {
		temp += "<td width='60'><font size='2'>法人姓名:</font></td><td  width='1'><input id='corporationName' size='10' name='corporationName' type='text'/></td>";
	} else if (par == "fareTypeName") {
		temp += "<td width='60' nowrap><font size='2'>经营业态:</font></td><td id='fareTypeSelect' width='1'></td>";
	} else if (par == "streetName") {
		temp += "<td width='60' nowrap><font size='2'>街道名称:</font></td><td id='streetSelect' width='1'></td>";
	} else if (par == "areaName") {
		temp += "<td width='60' nowrap><font size='2'>地区名称:</font></td><td id='areaSelect' width='1'></td>";
	} else if (par == "fareAddress") {
		temp += "<td width='60' nowrap><font size='2'>经营地址:</font></td><td  width='1'><input id='fareAddress' size='10' name='fareAddress' type='text'/></td>";
	} else if (par == "line") {
		temp += "<td width='60' nowrap><font size='2'>线&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路:</font></td><td id='lineSelect' width='1'></td>";
	} else if (par == "rank") {
		temp += "<td width='60' nowrap><font size='2'>等&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;级:</font></td><td id='rankSelect' width='1'></td>";
	} else if (par == "paperTime") {
		temp += "<td width='60' nowrap><font size='2'>办证日期:</font></td><td width='200'><input name='paperTime_start' id='paperTime_start' type='text' readonly size='6' readonly>"+
          "<img src='/KMGIS/imgs/time12.gif' style='cursor:hand;' alt='弹出日历下拉菜单' " +
          "onClick='document.all.paperTime_start.value=showCalendar(document.all.paperTime_start.value,650,200)'>"+
          "<font size='2'>&nbsp;到</font><input name='paperTime_end' id='paperTime_end' type='text' readonly size='6' readonly>"+
          "<img src='/KMGIS/imgs/time12.gif' style='cursor:hand;' alt='弹出日历下拉菜单'"+
          "onClick='document.all.paperTime_end.value=showCalendar(document.all.paperTime_end.value,650,200)'></td>";
	} else if (par == "brd") {
		temp += "<td width='60' nowrap><font size='2'>品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌:</font></td><td id='brdSelect' width='1'></td>";
	} else if (par == "cig") {
		temp += "<td width='60' nowrap><font size='2'>规&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;格:</font></td><td id='cigSelect' width='1'></td>";
	} else if (par == "corpCode") {
		temp += "<td width='60' nowrap><font size='2'>县&nbsp;&nbsp;区&nbsp;&nbsp;市:</font></td><td id='corpSelect' width='1'></td>";
	} else if (par == "sellType") {
		temp += "<td width='60' nowrap><font size='2'>零售业态:</font></td><td width='1'><select name='sellType' id='sellType' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option><option value='食杂店'>食杂店</option><option value='便利店'>便利店</option><option value='超市'>超市</option><option value='商场'>商场</option><option value='烟酒商店'>烟酒商店</option><option value='娱乐服务类'>娱乐服务类</option><option value='其他'>其他</option></select></td>";
	} else if (par == "markType") {
		temp += "<td width='60' nowrap><font size='2'>市场类型:</font></td><td width='1'><select name='markType' id='markType' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option><option value='城镇'>城镇</option><option value='乡村'>乡村</option></select></td>";
	} else if (par == "fareSize") {
		temp += "<td width='60' nowrap><font size='2'>经营规模:</font></td><td width='1'><select name='fareSize' id='fareSize' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option><option value='大'>大</option><option value='中'>中</option><option value='小'>小</option></select></td>";
	} else if (par == "logoutTime") {
		temp += "<td width='60' nowrap><font size='2'>注销日期:</font></td><td width='200'><input name='logoutTime_start' id='logoutTime_start' type='text' readonly size='6' readonly>"+
          "<img src='/KMGIS/imgs/time12.gif' style='cursor:hand;' alt='弹出日历下拉菜单' " +
          "onClick='document.all.logoutTime_start.value=showCalendar(document.all.logoutTime_start.value,650,200)'>"+
          "<font size='2'>&nbsp;到</font><input name='logoutTime_end' id='logoutTime_end' type='text' readonly size='6' readonly>"+
          "<img src='/KMGIS/imgs/time12.gif' style='cursor:hand;' alt='弹出日历下拉菜单'"+
          "onClick='document.all.logoutTime_end.value=showCalendar(document.all.logoutTime_end.value,650,200)'></td>";
	} else if (par == "noOrderMonth") {
		temp += "<td width='68' nowrap><font size='2'>不定货月数:</font></td><td  align='left' width='1'><input size='1' id='licenceNo' name='noOrderMonth' type='text' /></td>";
	} else if (par == "specialRank") {
		temp += "<td width='60' nowrap><font size='2'>专卖等级:</font></td><td  align='left' width='1'><td id='specialRankSelect' width='1'></td>";
	} else if (par == "pc") {
		temp += "<td width='60' nowrap><font size='2'>批&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;次:</font></td><td id='pcSelect' width='1'></td>";
	} else if (par == "dataType") {
		temp += "<td width='60' nowrap><font size='2'>数据类型:</font></td><td width='1'><select name='dataType' id='dataType' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option><option value='1'>需采点数据</option><option value='2'>已上传校正数据</option><option value='3'>完成校正数据</option></select></td>";
	} 
	return temp;
}

/**
	 * 初始化街道下拉框
	 * @param area 区域名称
	 */
function initStreetSelect(corpCode,area){
	getLoadMessage(waitMessage);
	BaseService.getStreetSelect(corpCode,area,function callback(html){
		 document.all.streetSelect.innerHTML = html;
	});
}

/**
	 * 初始化地区下拉框
	 */
function initAreaSelect(){
	getLoadMessage(waitMessage);
	var corpCode = "";
	if(document.all.corpCode){
		corpCode = document.all.corpCode.value;
	}else{
		corpCode = document.all.location.value;
		corpCode = corpCode.substring(0,corpCode.indexOf("|"));
	}
	BaseService.getAreaSelect(corpCode,function callback(html){
		document.all.areaSelect.innerHTML = html;
	});
}
/**
	 * 初始化专卖等级下拉框
	 */
function initSpecialRankSelect(){
	getLoadMessage(waitMessage);
	BaseService.getSpecialRankSelect(function callback(html){
		 document.all.specialRankSelect.innerHTML = html;
	});
}

/**
	 * 初始化经营业态下拉框
	 */
function initFareTypeSelect(){
	getLoadMessage(waitMessage);
	BaseService.getFareTypeSelect(function callback(html){
		 document.all.fareTypeSelect.innerHTML = html;
	});
}

/**
	 * 初始化批次下拉框
	 */
function initPcSelect(){
	getLoadMessage(waitMessage);
	var corpCode = "";
	if(document.all.corpCode){
		corpCode = document.all.corpCode.value;
	}else{
		corpCode = document.all.location.value;
		corpCode = corpCode.substring(0,corpCode.indexOf("|"));
	}
	BaseService.getPcSelect(corpCode,function callback(html){
		 document.all.pcSelect.innerHTML = html;
	});
}

/**
	 * 初始化当前位置下拉框
	 */
function initLocationSelect(){
	getLoadMessage(waitMessage);
	BaseService.getLocationSelect(orgId_,function callback(html){
		 document.all.locationSelect.innerHTML = html;
		 if(document.all.areaSelect){
		 	initAreaSelect();
		 }
		 if(document.all.lineSelect){
		 	initLineSelect();
		 }
		 if(document.all.pcSelect){
		 	initPcSelect();
		 }
	});
}
/*
 function initLocationSelect(){
	getLoadMessage(waitMessage);
	setTimeout("getLocation()",100); 
}

function getLocation(){
	BaseService.getLocationSelect(orgId_,function callback(html){
		 document.all.locationSelect.innerHTML = html;
		 if(document.all.areaSelect){
		 	initAreaSelect();
		 }
		 if(document.all.lineSelect){
		 	initLineSelect();
		 }
	}); 
}
 */
/**
	 * 初始化线路下拉框
	 */
function initLineSelect(){
	getLoadMessage(waitMessage);
	var corpCode = "";
	if(document.all.corpCode){
		corpCode = document.all.corpCode.value;
	}else{
		corpCode = document.all.location.value;
		corpCode = corpCode.substring(0,corpCode.indexOf("|"));
	}
	BaseService.getLineSelect(corpCode,function callback(html){
		 document.all.lineSelect.innerHTML = html;
	});
}
/**
	 * 初始化等级下拉框
	 */
function initRankSelect(){
	getLoadMessage(waitMessage);
	BaseService.getRankSelect(function callback(html){
		 document.all.rankSelect.innerHTML = html;
	});
}
/**
	 * 区域下拉框改变函数，联动街道下拉框
	 */
function areaChange(){
	getLoadMessage(waitMessage);
	if(document.all.streetName){
		 var areaName = document.all.areaName.value;
		 var corpCode = "";
		 if(document.all.corpCode){
		 	corpCode = document.all.corpCode.value;
		 }else{
		 	corpCode = document.all.location.value;
		 	corpCode = corpCode.substring(0,corpCode.indexOf("|"));
		 }
	  BaseService.getStreetSelect(corpCode,areaName,function callback(html){
		 document.all.streetSelect.innerHTML = html;
	});
	}
}

/**
	 * 当前位置下拉框改变函数，刷新不同的地图
	 */
function locationChange(mapId){
	getLoadMessage(waitMessage);
	if(document.all.areaSelect){
		initAreaSelect();
	}
	if(document.all.lineSelect){
		initLineSelect();
	}
	if(document.all.streetSelect){
		initStreetSelect('-99','-99');
	}
	if(document.all.pcSelect){
	  initPcSelect();
 }
 parent.centerFrame.document.all.mapId.value=mapId.substring(mapId.indexOf("|")+1, mapId.length);
 parent.centerFrame.switchArea();
}

/**
	 * 初始化县区市下拉框
	 */
function initCorp(){
	getLoadMessage(waitMessage);
	BaseService.getCorpSelect(function callback(html){
		 document.all.corpSelect.innerHTML = html;
		 if(document.all.areaSelect){
		 	initAreaSelect();
		 }
		 if(document.all.lineSelect){
		 	initLineSelect();
		 }
	});
}

/**
	 * 县区市下拉框改变函数，联动区域下拉框
	 */
function corpCodeChange(corpCode){
	getLoadMessage(waitMessage);
	if(document.all.lineSelect){
		initLineSelect();
	}
	if(document.all.areaSelect){
	  BaseService.getAreaSelect(corpCode,function callback(html){
		 document.all.areaSelect.innerHTML = html;
	});
	}
	if(document.all.streetSelect){
		initStreetSelect('-99','-99');
	}
	if(document.all.pcSelect){
	  initPcSelect();
 }
}
/**
	 * 初始化品牌下拉框
	 */
function initBrdSelect(){
	getLoadMessage(waitMessage);
	BaseService.getBrdSelect(function callback(html){
		 document.all.brdSelect.innerHTML = html;
	});
}

/**
	 * 品牌下拉框改变函数，联动规格下拉框
	 */
function brdChange(){
	getLoadMessage(waitMessage);
	if(document.all.cig){
		 var brd = document.all.brd.value;
	  BaseService.getCigSelect(brd,function callback(html){
		 document.all.cigSelect.innerHTML = html;
	});
	}
}

/**
	 * 初始化规格下拉框
	 */
function initCigSelect(brd){
	getLoadMessage(waitMessage);
	BaseService.getCigSelect(brd,function callback(html){
		 document.all.cigSelect.innerHTML = html;
	});
}
/**
	 * 参数校验
	 */
function valueCheck(co){
	var corpCode = document.getElementById('corpCode');
	var noOrderMonth = document.getElementById('noOrderMonth');
	if(corpCode&&(corpCode.value==null||corpCode.value=="")){
		alert(corpCodeMessage);
	}else if(noOrderMonth&&(noOrderMonth.value==null||noOrderMonth.value=="")){
		alert("请输入不定货月数");
	}else{
	 var flag = false;
		if(co.streetName!=null&&co.streetName!=''){
			flag = true;
		}else if(co.areaName!=null&&co.areaName!=''){
			flag = true;
		}else if(co.fareTypeName!=null&&co.fareTypeName!=''){
			flag = true;
		}else if(co.line!=null&&co.line!=''){
			flag = true;
		}else if(co.rank!=null&&co.rank!=''){
			flag = true;
		}else if(co.brd!=null&&co.brd!=''){
			flag = true;
		}else if(co.cig!=null&&co.cig!=''){
			flag = true;
		}else if(co.sellType!=null&&co.sellType!=''){
			flag = true;
		}else if(co.markType!=null&&co.markType!=''){
			flag = true;
		}else if(co.fareSize!=null&&co.fareSize!=''){
			flag = true;
		}else if(co.specialRank!=null&&co.specialRank!=''){
			flag = true;
		}
		if(!flag){
			alert(checkMessage);
		}
		return flag;
	}
	
}
/*
* 从html条件控件获取公司代码
*/
function getCorpCodeFormHTMLControl(){
	var corpCode = null;
	var locationSelect = document.getElementById('location');
 	if(locationSelect){
		var corpCode = locationSelect.value.split('|')[0];
 	}
 	return corpCode;
}
