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
					{header: '姓名', width: 50, dataIndex: 'id'},
					{header: '性别', width: 50, dataIndex: 'nr1'},
					{header: '年龄', width: 50, dataIndex: 'text1'},
					{header: '学历', width: 50, dataIndex: 'info1'},
					{header: '婚姻状况', width: 60, dataIndex: 'special1'},
					{header: '住址', width: 50, dataIndex: 'nr2'},
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
    gridEmq.head="姓名,性别,年龄,学历,婚姻状况,住址,公司,职业,爱好,工作性质,个人方向";
    gridEmq.tableType=2;
    gridEmq.moreHead=[[";基本信息,4;其他信息,4;备注,2"]];
	var airListGrid = new Ext.grid.GridPanelEx({
	  id:"airListGrid",
	  extdata:gridEmq,
	  fn:PlantService.testExtGrid
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
		type:"mi"
	});
	var commbo = new Ext.form.commonCombox({
		id:"combo",
		fn:PlantService.testCombox,
		fieldLabel:"商业公司",
		emptyText:"请选择商业公司"
	});
	var tbar = new Ext.Toolbar(	
		{id:'airtbar',items:[
				{xtype:'datefieldex',fieldLabel:'终止日期',id:'importDateEnd',format:'Y-m-d',width:85},
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
				commbo
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
	Ext.getCmp("airListGrid").load();
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

/**
 * 从弹出组织结构树窗口获取人员
 */
function getPersonInfo(rec){
	Ext.getCmp("personName").setValue(rec.get("hrname"));
}

