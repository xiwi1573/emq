/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
/**
 * 文件导入Panel
 * @param {Object} config
 */
Ext.FileImportPanel = function(config){
	this.initConfig = config || {};
	Ext.FileImportPanel.superclass.constructor.call(this, config);
};
// 继承
Ext.FileImportPanel = Ext.extend(Ext.form.FormPanel, {
	initComponent: function(){
		// -------------- 业务属性 --------------------
		// -------------- UI属性 --------------------
		var pan = this;
		this.xtype='textfield';
		this.fieldLabel='选择导入文件';
		this.name='myFile';
		this.width=Util.getW(.6);
		this.height=Util.getH(.03);
		this.inputType='file';
		this.on("click",function(){
		  alert("");
	    });
		Ext.FileImportPanel.superclass.initComponent.call(this);
	},
	//开始导入文件parameter=1:...2:...
	fileImport:function(parameter){
		this.getForm().submit({
		method:"post",
		url:'/EMQ/fileImportServlet?type='+parameter, 
		waitMsg:"正在导入文件,请稍等.....",
		success:function(form,action){
			Msg.info("文件导入成功！");
		},
		failure:function(form,action){
			if(action){
				Msg.info("文件导入失败！"+action.result.message);
			}
		},
		scope:this
	});
	}
});
// 注册
Ext.reg('Ext.FileImportPanel', Ext.FileImportPanel);