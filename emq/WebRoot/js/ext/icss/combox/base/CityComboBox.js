/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 城市下拉框
 */
 
Ext.form.CityComboBox = function(config){
	this.initConfig = config || {};
		
	Ext.form.CityComboBox.superclass.constructor.call(this, config);
	
};

Ext.form.CityComboBox = Ext.extend(Ext.form.ComboBox, {
    
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
    		if(typeof(AuthTreeService) == 'undefined' || typeof(AuthTreeService.getCity) == 'undefined'){
    			Msg.info('请在引用此js文件的jsp文件中，增加对getOrganization的支持');
    		}
    		
    		this.store = new Ext.data.DWRStore({id:this.id,
		        fn:AuthTreeService.getCity,
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
        Ext.form.CityComboBox.superclass.initComponent.call(this);
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
Ext.reg('CityComboBox', Ext.form.CityComboBox);

