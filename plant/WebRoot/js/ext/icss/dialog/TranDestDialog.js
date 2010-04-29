/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
 
Ext.icss.TranDestDialog = function(config){
	this.initConfig = config || {};
		
	Ext.icss.TranDestDialog.superclass.constructor.call(this, config);
	
};

Ext.icss.TranDestDialog = Ext.extend(Ext.Window, {
    maximizable :true,
    initComponent : function(){
    	if(typeof(this.fn) !== 'function')
    	{
    		Msg.info('请在传入接收确定后的处理函数');
    	}
    	if(typeof(this.compComponent) == 'undefined')
    	{
    		this.compId = null;
    	}
    	else
    	{
    		this.compId = this.compComponent.getValue();
    	}
    	if(typeof(this.compId)=='undefined'||this.compId == '')
    	{
    		this.compId = null;
    	}

    	if(typeof(UtilService) == 'undefined' || typeof(UtilService.getArriveAreaInfo) == 'undefined'){
    			Msg.info('请在引用此js文件的jsp文件中，增加对UtilService.getArriveAreaInfo的支持');
    	}
    		
		var tranDestDialogStore = new Ext.data.DWRStore({id:'tranDestDialogStore',
	        fn:UtilService.getArriveAreaInfo,
	    	fields:[{name:'arriveRegionShortName',type:'string'},
 	               {name:'provinceShortName',type:'string'},
 	               {name:'arriveRegionName',type:'string'},
 	               {name:'id.arriveRegionId',type:'string'},
 	               {name:'pyCode',type:'string'}
 	               ]
	    	});  
	    var pytext = new Ext.form.TextField({xtype:'textfield',width:190,
	    					emptyText:'输入拼音首字母',enableKeyEvents:true});
		pytext.on('keyup',this.onFilter,this);
		
	 	var sm = new Ext.grid.RowSelectionModel();
	 	var cm =new Ext.grid.ColumnModelEx({columns:[
	 	    {header: "运达地简称", width:150,  sortable: false, dataIndex: 'arriveRegionShortName'},
	 	    {header: "省份简称", width:150,  sortable: false, dataIndex: 'provinceShortName'},
	 	    {header: "运达地名称", width:150,  sortable: false, dataIndex: 'arriveRegionName'},
	 	    {header: "运达地id", width:150,  sortable: false,hidden:true, dataIndex: 'id.arriveRegionId'}
	 	 ]});
	 	var tranDestDialogGrid = new Ext.grid.GridPanelEx({
	 	    id:'tranDestDialogGrid',
	 	    store:tranDestDialogStore,
	 	    cm:cm,
	 	    sm:sm,
	 	    height:320,
	 	    tbar:[{xtype:'tbtext',text:'拼音码'},pytext]
	 	 });
			
	    this.grid = tranDestDialogGrid;
	    this.width = Util.getW(0.5);
		this.height = Util.getH(0.5);
	    this.items = [this.grid];
	    this.layout = 'fit';
	    this.title = '选择运达地';	    		
		this.modal = true;
		this.buttons = [{text:'确定',handler:this.onOK, scope:this.ownerCt},
						{text:'取消',handler:this.onCancel,scope:this.ownerCt}];
        this.buttonAlign = 'center';
        this.closeAction = 'hide';
        this.grid.on('rowdblclick',this.onRowdbclick);
	        
        Ext.icss.TranDestDialog.superclass.initComponent.call(this);
    },
    refresh:function(){
    	if(typeof(this.compComponent) == 'undefined')
    	{
    		this.compId = null;
    	}
    	else
    	{
    		this.compId = this.compComponent.getValue();
    	}
    	if(typeof(this.compId)=='undefined'||this.compId == '')
    	{
    		this.compId = null;
    	}
    	if(this.grid){
    		var ds = this.grid.getStore();
    		ds.load({params:[this.compId]});
    	}
    },
    onRowdbclick:function(g, ridx, e ){
    	this.ownerCt.fn(this.ownerCt,g);
    },
    onOK:function(btn,event){
    	this.ownerCt.fn(this.ownerCt,this.ownerCt.grid);
    },
    onCancel:function(btn,event){
    	this.ownerCt.hide();
    },
    onFilter:function(txt,e){
		var text = txt.getValue();
    	if(this.grid)
    	{
	    	var re = new RegExp(text, 'i'); 
	    	this.grid.store.filter('pyCode',text,true)
    	}
	}
});
Ext.reg('TranDestDialog', Ext.icss.TranDestDialog);

