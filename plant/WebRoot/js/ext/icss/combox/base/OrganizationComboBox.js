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
	
	//市场总部
	var areaComboBox = new Ext.form.OrganizationComboBox({
			id:'areaComboBox',
			fieldLabel : '市场总部',
			anchor:anchorSize,
			co:{childLevel: Global.CONDITION_LEVEL.LEVEL2,all:true}
		});
	//省
	var provinceComboBox = new Ext.form.OrganizationComboBox({
			id:'provinceComboBox',
			fieldLabel : '省',
			anchor:anchorSize,
			co:{childLevel: Global.CONDITION_LEVEL.LEVEL3,all:true}
		});
	//省份与市场总部联动查询处理
	provinceComboBox.on('beforequery',function(queryEvent){
		var areaCbx = Ext.getCmp('areaComboBox');
		var co = Ext.getCmp('provinceComboBox').getCo();
		co.areaId = areaCbx.getValue();
		co.childLevel = Global.CONDITION_LEVEL.LEVEL3;
		if(Global.isAllKey(co.areaId))
		{
			co.areaId = null;
		}
		queryEvent.query = [co];
	});
		
	var p = new Ext.FormPanel({
		renderTo : 'testdiv',
		title : '测试下拉',
		id : 'testform',
		labelAlign : 'right',
				
		items : [areaComboBox,provinceComboBox]
	});
	return p; 
}
</code>
 */
 
Ext.form.OrganizationComboBox = function(config){
	this.initConfig = config || {};
		
	Ext.form.OrganizationComboBox.superclass.constructor.call(this, config);
	
};

Ext.form.OrganizationComboBox = Ext.extend(Ext.form.ComboBox, {
    
    initComponent : function(){
    	if(typeof(this.co) == 'undefined')
    	{
    		this.co = {};
    	}
    	if(typeof(this.cbx) == 'undefined')
    	{
    		this.cbx = null;
    	}
    	
    	if(!this.store)
    	{
    		if(typeof(AuthTreeService) == 'undefined' || typeof(AuthTreeService.getOrganization) == 'undefined'){
    			Msg.info('请在引用此js文件的jsp文件中，增加对getOrganization的支持');
    		}
    		
    		this.store = new Ext.data.DWRStore({id:this.id,
		        fn:AuthTreeService.getOrganization,
		    	fields:[{name:'code',type:'string'},
		    		{name:'text',type:'string'}]
		    		/*,
		    		{name:'leaf',type:'int'},
		    		{name:'nodeType',type:'int'},
		    		{name:'qtip',type:'string'},
		    		{name:'data',type:'string'},
		    		{name:'level',type:'int'},
		    		{name:'parentId',type:'string'}*/
		    });  
		    
		    this.valueField = 'code';
		    this.displayField = 'text';
		    this.typeAhead =  true;
		    this.selectOnFocus = true;
		    this.mode = 'remote';
        	this.triggerAction = 'all';
        	this.readOnly = true;
    	}
    	
    	this.on('beforequery',this.onBeforeQuery);
        Ext.form.OrganizationComboBox.superclass.initComponent.call(this);
    },
    onBeforeQuery:function(queryEvent){
    	if(this.co){
    		queryEvent.query = [this.co];
    	}
    },
    init:function(co){
    	if(co)
    	{
    		this.store.load({params:[co]});
    	}else{
    		this.store.load({params:[this.co]});
    	}
    },
    initAllKey:function(){
    	if(this.co.all)
        {
	        	this.setValue(Global.ALL_KEY);
	        	this.setRawValue(Global.ALL_KEY_NAME);
        }
    },
    getCo:function(){
    	return this.co;
    }
    
});
Ext.reg('OrganizationComboBox', Ext.form.OrganizationComboBox);

