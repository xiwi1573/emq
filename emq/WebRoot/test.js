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
//	var airListStore = new Ext.data.DWRStore({
//		id:'airListStore',
//		fn:null,
//	 	pruneModifiedRecords:true,
//        fields: [
//           {name: 'id',type:'int'},//流水号
//           {name: 'organise',type:'string'},//报销机构名称
//           {name: 'organiseId',type:'int'},//报销机构ID
//           {name: 'sortId',type:'string'}, // 行程单号       
//           {name: 'boardingDate',type:'date'},//乘机日期
//           {name: 'personId',type:'int'},//乘机人ID
//           {name: 'name',type:'string'},//乘机人姓名
//           {name: 'duty',type:'string'},//乘机人职务
//           {name: 'dutyId',type:'int'},//乘机人职务ID
//           {name: 'voyage',type:'string'},//航程
//           {name: 'fullPrice',type:"float"},//全价
//		   {name: 'agio',type:'string'},//折扣
//		   {name: 'agioPrice',type:"float"},//折扣价
//		   {name: 'tax',type:"float"},//税费
//		   {name: 'totalPrice',type:"float"},//票面金额  小计
//		   {name: 'writeOffFee',type:"float"},//限额标准
//		   {name: 'backingSum',type:"float"},//当期节余
//		   {name: 'booker',type:'string'},//订票人
//		   {name: 'remark',type:'string'},//
//		   {name: 'auditState',type:'int'},//单据状态
//		   {name: 'pid',type:'string'},//工号
//		   {name: 'auditDate',type:'date'},//综合管理部审核时间
//  		   {name: 'auditPersonId',type:'string'},//综合管理部审核人ID
//  		   {name: 'auditPersonName',type:'string'},//综合管理部审核人NAME
//  		   {name: 'finalPersonId',type:'string'},//财务审核人ID
//  		   {name: 'finalPersonName',type:'string'},//财务审核人NAME
//  		   {name: 'finalAuditDate',type:'string'},//财务审核时间
//  		   {name: 'writeState',type:'int'},//与差旅费报销状态
//  		   {name: 'importDate',type:'date'}//机票导入时间
//        ]
//    });
//    var rowNum =  new Ext.grid.RowNumberer({
//     width : 30
//    });
//    rowNum.locked = true;
// 	 var sm = new Ext.grid.CheckboxSelectionModel();
//     sm.locked = true;
//	var airListCM = new Ext.grid.LockingColumnModel({sumheader:true,columns:[
//		//rowNum,
//		//sm,
//		{header:'流水号',dataIndex:'id',width:50,locked:true,sortable:true,align:'right',sumcaption:'合计'}
//		,{header:'工号',dataIndex:'pid',width:80,locked:true,sortable:true,align:'left'}
//		,{header:'行程单号',dataIndex:'sortId',locked:true,width:130,sortable:true,align:'right'}
//		,{header:'单据状态',dataIndex:'auditState',locked:true,sortable: true,hidden:false}
//		,{header:'部门',dataIndex:'organise',width:90,locked:true,sortable:true,align:'left'}
//		,{header:'姓名',dataIndex:'name',width:80,locked:true,sortable:true,align:'left'}
//		,{header:'职务',dataIndex:'duty',width:80,locked:true,sortable:true,align:'left'}
//		,{header:'乘机时间',dataIndex:'boardingDate',width:80,locked:false,sortable:true,align:'left',renderer: Ext.util.Format.dateRenderer('Y-m-d')}
//		,{header:'航程',dataIndex:'voyage',width:80,locked:false,sortable:true,align:'left'}
//		,{header:'全价',dataIndex:'fullPrice',locked:false,sortable:true,align:'right',renderer:Util.rmbMoney,issum:true}
//		,{header:'折扣',dataIndex:'agio',locked:false,sortable:true,align:'right'}
//		,{header:'折扣价',dataIndex:'agioPrice',locked:false,sortable:true,align:'right',renderer:Util.rmbMoney,issum:true}
//		,{header:'税费',dataIndex:'tax',locked:false,sortable:true,align:'right',renderer:Util.rmbMoney,issum:true}
//		,{header:'票面金额',dataIndex:'totalPrice',locked:false,sortable:true,align:'right',renderer:Util.rmbMoney,issum:true}
//		,{header:'限制金额',dataIndex:'writeOffFee',locked:false,sortable:true,align:'right',renderer:Util.rmbMoney,issum:true}
//		,{header:'当期节余',dataIndex:'backingSum',locked:false,sortable:true,align:'right',renderer:Util.rmbMoney,issum:true}
//		,{header:'导入时间',dataIndex:'importDate',locked:false,width:120,sortable:true,align:'left',renderer: Ext.util.Format.dateRenderer('Y-m-d')}
//		,{header:'备注',dataIndex:'remark',locked:false,sortable:true,align:'left'}
//	]});	
//	var airListGrid = null;
//	PlantService.testExtGrid(function(list){
//	  var airListGrid = new Ext.grid.GridPanelEx({
//	  	id:"airListGrid"
//	  });
//	  return airListGrid;
//	});
//	var gridEmq = new GridEmq();
	/* 
	 * 单表头封装测试
	gridEmq.head="编号,姓名";
	
	var airListGrid = new Ext.grid.GridPanelEx({
	  id:"airListGrid",
	  extdata:gridEmq.getExtGrid(),
	  fn:PlantService.testExtGrid
    });*/
    var gridEmq = new GridEmq();
    gridEmq.head="姓名,性别,年龄,学历,婚姻状况,住址,公司,职业,爱好,工作性质,个人方向";
    gridEmq.tableType=2;
    gridEmq.moreHead=[[";基本信息,4;其他信息,4;备注,2"]];
	var airListGrid = new Ext.grid.GridPanelEx({
	  id:"airListGrid",
	  extdata:gridEmq,
	  fn:PlantService.testExtGrid
    });
	
//    	height: Ext.icss.Util.getH(0.7),
//    	trackMouseOver:true,//鼠标跟踪轨迹
    	//store:airListStore,
	  	//cm:"undefined", 
//	    loadMask: {msg:'正在加载数据，请稍候……'},
//	    frame:true,
//	    border:false
  
	//airListGrid.on('headerclick',Util.Grid.selectAll);
    return airListGrid;
}
/**
 * 创建机票信息栏
 * @return {}
 */
function getAirTicketTbar(){
	var form = new Ext.FileImportPanel({
		id:"docForm"
	});
	var tbar = new Ext.Toolbar(	
		{id:'airtbar',items:[
				{xtype:'tbtext',text:'导入时间:'},
				{xtype:'tbspacer'},
				{xtype:'datefieldex',fieldLabel:'导入时间',id:'importDateStart',format:'Y-m-d',width:85},
				{xtype:'tbtext',text:'至'},
				{xtype:'tbspacer'},
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
				form
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

/**
 * 从弹出组织结构树窗口获取人员
 */
function getPersonInfo(rec){
	Ext.getCmp("personName").setValue(rec.get("hrname"));
}

