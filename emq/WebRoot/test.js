/**
 * EXT 入口方法
 */
Ext.onReady(function(){
	createMainUI();
});
/*Ext.onReady(function() {
   var grid = {
			xtype: 'grid',
			name:'gridLike',
			buttonAlign:"right",
			title: '双表头',
			store: new Ext.data.SimpleStore({
				fields: ['id', 'nr1', 'text1', 'info1', 'special1', 'nr2', 'text2', 'info2', 'special2', 'special3', 'changed'],
				data: [
					['A1', '男', '1', '4', '5', '6', '7', '8', '9', '10', '11'],
					['A1', '男', '2', '4', '5', '6', '7', '8', '9', '10', '11'],
					['A2', '男', '3', '4', '5', '6', '7', '8', '9', '10', '11'],
					['A3', '男', '4', '4', '5', '6', '7', '8', '9', '10', '11'],
					['A4', '男', '5', '4', '5', '6', '7', '8', '9', '10', '11'],
					['A5', '男', '6', '4', '5', '6', '7', '8', '9', '10', '11'],
					['A6', '男', '7', '4', '5', '6', '7', '8', '9', '10', '11']
				]
			}),
			colModel: new Ext.grid.ColumnModel({
				columns: [
					{header: '姓名', width: 25, dataIndex: 'id'},
					{header: '性别', width: 25, dataIndex: 'nr1'},
					{header: '年龄', width: 50, dataIndex: 'text1'},
					{header: '学历', width: 50, dataIndex: 'info1'},
					{header: '婚姻状况', width: 60, dataIndex: 'special1'},
					{header: '住址', width: 25, dataIndex: 'nr2'},
					{header: '公司', width: 50, dataIndex: 'text2'},
					{header: '职业', width: 50, dataIndex: 'info2'},
					{header: '爱好', width: 60, dataIndex: 'special2'},
					{header: '工作性质', width: 60, dataIndex: 'special3'},
					{header: '个人方向', width: 50, dataIndex: 'changed'}
				],
				rows: [
					[
						{},
						{header: '基本信息', colspan: 4, align: 'center'},
						{header: '其他信息', colspan: 4, align: 'center'},
						{header: '备注', colspan: 2, align: 'center'}
					]
				]
			}),
			plugins: [new Ext.ux.plugins.GroupHeaderGrid()]
		};
	new Ext.Viewport({
		layout: 'fit',
		items: [grid]
	});
});*/
/**
 * 创建主界面
 */
function createMainUI(){
	var airTicketPanel = getAirTicketGrid();//创建机票显示列表	
	var tbar = getAirTicketTbar();	//创建机票工具栏
	var panel = new Ext.Panel({
		id:"panel",
		layout:"border",
		tbar:tbar,
		items:[
			//{region:"north",border:false,split:true,layout:'fit',height:23,items:[form]},
				{region:"center",split:true,layout:"fit",items:[airTicketPanel]}]
	});
	var view = new Ext.Viewport({
		layout:"fit",
		items:[panel]
	});
	return view;
}
/**
 * 创建机票列表
 * @return {}
 */
function getAirTicketGrid(){
    var gridEmq = new GridEmq();
    gridEmq.head="ID,STATION_NAME,BELONG_UNIT_NAME,MEASURE_POINT_NAME,SUB_POINT_NAME,SUB_POINT_ADDRESS,MEASURE_POINT_TYPE,MEASURE_POINT_KIND,PASS_TYPE,E_CONFIGURE,E_MODEL,E_TYPE,E_MANUFACTURER,E_FACTORY_NUMBER,E_CONNECTION,E_TENSION,E_ELECTRICITY,E_RANK,E_MESSAGE_INTERFACE,E_MESSAGE_TYPE,E_COL_MODEL,E_COL_FACTORY,E_TV_RATE,E_TA_RATE,E_MULTIPLE,E_CHECK_TIME,E_CHECK_RESULT,T_MODEL,T_TYPE,T_FACTORY,T_DISCREPANCY,T_UNMBER,T_TENSION_FIRST,T_TENSION_SECOND,T_TENSION_SECOND_CHARGE,T_RANK,T_TV_KIND,T_IS_SPECIAL,T_IS_SPECIAL_TV,T_CHECK_TIME,T_CHECK_RESULT,EI_MODEL,EI_TYPE,EI_MANUFACTURER,EI_DISCREPANCY,EI_UNMBER,EI_RATED_TENSION_FIRST,EI_RATED_TENSION_SECOND,EI_RATED_TENSION_SECOND_CHARGE,EI_VERACITY_RANK,EI_TV_KIND,EI_IS_SPECIAL,EI_IS_SPECIAL_TV,EI_CHECK_TIME,EI_CHECK_RESULT,LT_MODEL,LT_TYPE,LT_MANUFACTURER,LT_UNMBER,GPS_MODEL,GPS_TYPE,GPS_MANUFACTURER,GPS_UNMBER,TV_AREA,TV_LENGTH,TA_LENGTH,TV_TIME,TV_RESULT,TV_CHARGE_TIME,TV_CHARGE_RESULT,TA_CHARGE_TIME,TA_CHARGE_RESULT,FILE_ID";
//    gridEmq.head="姓名,性别,年龄,学历,婚姻状况,住址,公司,职业,爱好,工作性质,个人方向";
//    gridEmq.tableType=2;
//    gridEmq.renderer="renderIcon";
//    gridEmq.issum="false,true";
//    gridEmq.moreHead=[[";基本信息,4;其他信息,4;备注,2"]];
	var airListGrid = new Ext.grid.GridPanelEx({
	  id:"airListGrid",
	  extdata:gridEmq,
	  fn:PlantService.getDataListForGrid
//	  fn:PlantService.testExtGrid
    });
	
    return airListGrid;
}
/**
 * 创建机票信息栏
 * @return {}
 */
function getAirTicketTbar(){
	var form = new Ext.FileImportPanel({
		id:"docForm",
		type:"passBook"
	});
	var commbo = new Ext.form.commonCombox({
		id:"combo",
		fn:PlantService.testCombox,
		fieldLabel:"商业公司",
		emptyText:"请选择商业公司"
	});
	var tbar = new Ext.Toolbar(	
		{id:'airtbar',items:[
				{xtype:'datefield',fieldLabel:'开始日期',id:'importDateStart',format:'Y-m-d H:i:s',width:85},
				{xtype:'datefield',fieldLabel:'终止日期',id:'importDateEnd',format:'Y-m-d H:i:s',width:85},
				{xtype:'tbspacer'},
				{xtype:'tbtext',text:'姓名'},
				{xtype:'textfield',id:'personName',width:85,listeners:{"focus":function(){openPersonWindow();}}},
				{xtype:'tbspacer'},
				{xtype:'tbtext',text:'编号'},
				{xtype:'textfield',id:'sortId',name:'sortId',fieldLabel: '编号',width:80},
				{xtype:'tbspacer'},
				{pressed:true,id:'querryAir',text:'查询',handler:querryAir},
				{xtype:'tbseparator'},
				form,
				{pressed:true,id:'downLoad',text:'下载',handler:handlerClickBtnDownLoad},
				{xtype:'tbtext',text:'下拉框'},
				commbo,
				{pressed:true,text:'导出word',handler:handlerClickBtnAllAreaWord}

	]});
	return tbar;
}
/**
 * 查询
 */
function querryAir(){
//	var co = {};
//	if("" ==Ext.getCmp('takeStartDate').getValue() ||  null == Ext.getCmp('takeStartDate').getValue()){
//		co.boardingSDate = null;		
//	}else{
//		co.boardingSDate = Ext.getCmp('takeStartDate').getValue();	
//	}
//	if("" ==Ext.getCmp('takeEndDate').getValue() ||  null == Ext.getCmp('takeEndDate').getValue()){
//		co.boardingEDate = null;		
//	}else{
//		co.boardingEDate = Ext.getCmp('takeEndDate').getValue();	
//	}
//	if("" ==Ext.getCmp('personName').getValue() ||  null == Ext.getCmp('personName').getValue()){
//		co.name = null;		
//	}else{
//		co.name = Ext.getCmp('personName').getValue();	
//	}
//	co.sortId = Ext.getCmp("sortId").getValue();
//	co.queryType = 2;
//	co.auditState = null;
	Ext.getCmp("importDateStart").setValue(new Date());
	Ext.getCmp("airListGrid").load();
	Ext.getCmp("airListGrid").getStore().on('load',function(){Ext.getCmp("importDateEnd").setValue(new Date());});
	
}


function handlerClickBtnAllAreaWord()  
{  
  var oWD = new ActiveXObject("Word.Application");  
  Msg.info(oWD);
  var oDC = oWD.Documents.Add("",0,1);  
  var orange =oDC.Range(0,1);  
  var sel = document.body.createTextRange();  
  sel.moveToElementText(airListGrid);  
  sel.select();  
  sel.execCommand("Copy");  
  orange.Paste();  
  oWD.Application.Visible = true;  
}  

function setAirStartDate(com){
	var firstDayOfMonth = (new Date()).getFirstDateOfMonth().format("Y-m-d");
	com.setValue(firstDayOfMonth);	
}

function setAirEndDate(com){
	var today = (new Date()).format("Y-m-d");
	com.setValue(today);
}

function handlerClickBtnDownLoad(){
	PlantService.getDataList(function(fileName){
	    location="do_download.jsp?name="+fileName;
	});
}

function createValidBarDetailGrid()
{
	var grid = Ext.getCmp('validBarDetailGrid');
	if(grid)
	{
		return grid;
	}
	var validBarDetailStore = new Ext.data.DWRStore({id:'validBarDetailStore',fn:null,
	    fields:[{name:'yyear',type:'int'},
	        {name:'mmonth',type:'int'},
	        {name:'wweek',type:'int'},
	        {name:'weekName',type:'string'},
	        {name:'bar',type:'string'},
	        {name:'barName',type:'string'},
	        {name:'comId',type:'string'},
	        {name:'comName',type:'string'},
	        {name:'weekStartDate',type:'date'},
	        {name:'weekEndDate',type:'date'},
	        {name:'targetValue',type:'float'},
	        {name:'state',type:'int'},
	        {name:'stateName',type:'string'}]}); 
	        
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	
	var cm = new Ext.grid.ColumnModelEx({sumheader:true,columns:[sm,	 		 	
	 	{header: "卷烟规格", dataIndex: 'bar',sortable:true,sumcaption:'合计'},
	 	{header: "卷烟名称", dataIndex: 'barName',sortable:true},
	 	{header: "指标", dataIndex: 'targetValue',align:'right',sortable:true,renderer: Util.rmbMoney,issum:true}
       ]});
        
    var validBarDetailGrid = new Ext.grid.GridPanelEx({
        id:'validBarDetailGrid', 
        //title:'需求计划信息',
        height:Util.getH(.4),
        store: validBarDetailStore,
        cm: cm,
        sm: sm,
        frame:true,
        border:true
    });
    
    return validBarDetailGrid;
}

/**
 * 从弹出组织结构树窗口获取人员
 */
function getPersonInfo(rec){
	Ext.getCmp("personName").setValue(rec.get("hrname"));
}

