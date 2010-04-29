/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * 有权限控制的省份下拉
 * @include "../global/Global.js"
 * @include "../msg/Msg.js"
 * @class Ext.form.ComboBox
 * @extends Ext.form.TriggerField
 * A combobox control with support for autocomplete, remote-loading, paging and many other features.
 * @constructor
 * Create a new ComboBox.
 * @param {Object} config Configuration options
 * <code>使用示例
function createMainUI()
{
	var specialTypeCombox = new Ext.form.OrganizationComboBox({
		enumType:'SPECIALTYPE',
		fieldLabel:'用烟类型',
		width:200,
		all:false,//是否包含'全部'
		id:'specialTypeCombox'
	});
	var inTypeCombox = new Ext.form.ProvinceComboBox({
		enumType:'IN_TYPE',
		fieldLabel:'入库类型',
		width:200,
		all:false,
		id:'inTypeCombox'
	});
	var outTypeCombox = new Ext.form.CodeEnumByUserComboBox({
		enumType:'OUT_TYPE',
		fieldLabel:'出库类型',
		width:200,
		all:true,
		id:'outTypeCombox'
	});
	var p = new Ext.FormPanel({
		renderTo : 'testdiv',
		title : '测试下拉',
		id : 'testform',
		labelAlign : 'right',
				
		items : [specialTypeCombox,inTypeCombox,outTypeCombox]
	});
	return p; 
}
</code>
 */
 
Ext.icss.OrgComDialog = function(config){
	this.initConfig = config || {};
		
	Ext.icss.OrgComDialog.superclass.constructor.call(this, config);
	
};

Ext.icss.OrgComDialog = Ext.extend(Ext.Window, {
    maximizable :true,
    initComponent : function(){
    	if(typeof(this.fn) !== 'function')
    	{
    		Msg.info('请在传入接收确定后的处理函数');
    	}

    	if(typeof(UtilService) == 'undefined' || typeof(UtilService.getComOrgInfo) == 'undefined'){
    			Msg.info('请在引用此js文件的jsp文件中，增加对UtilService.getComOrgInfo的支持');
    	}
    		
		var orgComDialogStore = new Ext.data.DWRStore({id:'orgComDialogStore',
	        fn:UtilService.getComOrgInfo,
	    	fields:[{name:'code',type:'string'},
	    		{name:'text',type:'string'},
	    		{name:'data',type:'string'}]
	    	});  
	    var pytext = new Ext.form.TextField({xtype:'textfield',width:190,
	    					emptyText:'输入拼音首字母',enableKeyEvents:true});
		pytext.on('keyup',this.onFilter,this);
		
		var sm = new Ext.grid.RowSelectionModel();
		var torgComDialogGrid = new Ext.grid.EditorGridPanelEx({
		    	id:'orgComDialogGrid',
		        store: orgComDialogStore,
		        frame:true,
		        //border:true,
		        height:Util.getH(.12),
		        cm: new Ext.grid.ColumnModelEx({columns:[
		            {header: "商业公司ID", width:90,  sortable: true, dataIndex: 'code'},
		            {header: "商业公司名称", width:250,  sortable: true, dataIndex: 'text'},
		            {header: "拼音码", width:100,  sortable: true, dataIndex: 'data'}
		        ]}),
		        sm:sm,
		       	stripeRows: true,
		       	tbar:[{xtype:'tbtext',text:'拼音码'},pytext]
		    });
			
		    this.grid = torgComDialogGrid;
		    this.width = Util.getW(0.5);
			this.height = Util.getH(0.5);
		    this.items = [this.grid];
		    this.layout = 'fit';
		    this.title = '选择商业公司';	    		
    		this.modal = true;
    		this.buttons = [{text:'确定',handler:this.onOK, scope:this.ownerCt},{text:'取消',handler:this.onCancel,scope:this.ownerCt}];
	        this.buttonAlign = 'center';
	        this.closeAction = 'hide';
	        this.grid.on('rowdblclick',this.onRowdbclick);
	        
	        
        Ext.icss.OrgComDialog.superclass.initComponent.call(this);
    },
    refresh:function(){
    	if(this.grid){
    		var ds = this.grid.getStore();
    		ds.load();
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
	    	this.grid.store.filter('data',text,true)
    	}
	}
								
    
});
Ext.reg('OrgComDialog', Ext.icss.OrgComDialog);

