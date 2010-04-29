/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * 发货地域映射表
 * @include "../global/Global.js"
 * @include "../msg/Msg.js"
 * @class Ext.form.ComboBox
 * @extends Ext.form.TriggerField
 * A combobox control with support for autocomplete, remote-loading, paging and many other features.
 * @constructor
 * Create a new ComboBox.
 * @param {Object} config Configuration options
 * <code>使用示例

</code>
 */
 
Ext.form.ConsignByAreaComboBox = function(config){
	this.initConfig = config || {};
		
	Ext.form.ConsignByAreaComboBox.superclass.constructor.call(this, config);
	
};

Ext.form.ConsignByAreaComboBox = Ext.extend(Ext.form.ComboBox, {
    
    initComponent : function(){
    	if(typeof(this.orgId) == 'undefined')
    	{
    		this.orgId = Global.GROUP_CODE;
    	}
    	
    	if(typeof(this.areaId) == 'undefined')
    	{
    		this.areaId = 1;
    	}
    	
    	if(typeof(this.defaultValue) == 'undefined')
    	{
    		this.defaultValue = 1;
    	}
    	
    	if(typeof(this.cbx) == 'undefined')
    	{
    		this.cbx = null;
    	}
    	
    	if(!this.store)
    	{//getCConsign(String orgId);
    		if(typeof(UtilService) == 'undefined' || typeof(UtilService.getConsignByArea) == 'undefined'){
    			Msg.info('请在引用此js文件的jsp文件中，增加对getOutAreaMap的支持');
    		}

    		this.store = new Ext.data.DWRStore({id:this.id,
		        fn:UtilService.getConsignByArea,
		    	fields:[
		    		{name:'consinId',type:'string'},
		    		{name:'consign',type:'string'},
		    		{name:'areaId',type:'string'}]
		    });  
		    this.store.on('load',this.selectDefault);
		    this.valueField = 'consinId';
		    this.displayField = 'consign';
		    this.typeAhead =  true;
		    this.selectOnFocus = true;
		    this.mode = 'remote';
        	this.triggerAction = 'all';
        	this.readOnly = true;
    	}
    	
    	this.on('beforequery',this.onBeforeQuery);
        Ext.form.ConsignByAreaComboBox.superclass.initComponent.call(this);
    },
    onBeforeQuery:function(queryEvent){
    	queryEvent.query = [this.orgId,this.areaId];
    },
    init:function(orgId,areId){
    	this.store.load({params:[orgId,areaId]});
    },
   	setDefaultValue:function(defaultValue)
   	{
   		this.defaultValue = defaultValue;
   	},
   	selectDefault:function(ds, records, options)
   	{
   		this.setValue(this.defaultValue);
   	}
    
});
Ext.reg('ConsignByAreaComboBox', Ext.form.ConsignByAreaComboBox);

