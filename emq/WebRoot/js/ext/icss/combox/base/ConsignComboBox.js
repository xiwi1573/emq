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
 
Ext.form.ConsignComboBox = function(config){
	this.initConfig = config || {};
		
	Ext.form.ConsignComboBox.superclass.constructor.call(this, config);
	
};

Ext.form.ConsignComboBox = Ext.extend(Ext.form.ComboBox, {
    
    initComponent : function(){
    	if(typeof(this.orgId) == 'undefined')
    	{
    		this.orgId = Global.GROUP_CODE;
    	}
    	if(typeof(this.state) == 'undefined')
    	{
    		this.state = 1;
    	}
    	
    	if(typeof(this.defaultValue) == 'undefined')
    	{
    		this.defaultValue = 1;
    	}
    	
    	
    	
    	if(!this.store)
    	{//getCConsign(String orgId);
    		if(typeof(UtilService) == 'undefined' || typeof(UtilService.getCConsign) == 'undefined'){
    			Msg.info('请在引用此js文件的jsp文件中，增加对getCConsign的支持');
    		}

    		this.store = new Ext.data.DWRStore({id:this.id,
		        fn:UtilService.getCConsign,
		    	fields:[{name:'memId',type:'string'},
		    		{name:'consinId',type:'string'},
		    		{name:'consign',type:'string'},
		    		{name:'areaId',type:'string'}]
		    });  
		    
		    this.valueField = 'consinId';
		    this.displayField = 'consign';
		    this.typeAhead =  true;
		    this.selectOnFocus = true;
		    this.mode = 'remote';
        	this.triggerAction = 'all';
        	this.readOnly = true;
    	}
    	
    	this.on('beforequery',this.onBeforeQuery);
        Ext.form.ConsignComboBox.superclass.initComponent.call(this);
    },
    onBeforeQuery:function(queryEvent){
    	queryEvent.query = [this.orgId,this.state];
    },
    init:function(orgId,state){
    	this.store.load({params:[orgId,state]});
    },
   	setDefaultValue:function(defaultValue)
   	{
   		this.defaultValue = defaultValue;
   	}
   
    
});
Ext.reg('ConsignComboBox', Ext.form.ConsignComboBox);

