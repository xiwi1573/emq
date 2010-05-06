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
 * type:el-电能关口台帐模板,mi-互感器关口台帐模板
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
		var panelId = this.id;
		if(typeof(this.type)=="undefined"){
			this.type = "el";
		}
		var fileType = this.type;
		this.height=20;
		this.method='POST';
		this.frame=false;
		this.border=false;
		this.fileUpload=true;
		this.labelAlign="left";	
		this.labelWidth=76;
		this.layout="column";
		this.items=[
		     {region:"west",border:false,split:true,layout:'form',items:[{xtype:'textfield',
				fieldLabel: '选择导入文件',						
				id:'fileName',
				name: 'myFile',
				inputType:'file'
		     }]},
		     {region:"center",height:20,border:false,split:true,layout:'form',items:[{xtype:'button',pressed:true,
		        id:'ExcelImport',text:'文件导入',
		        handler:function(parameter){
					if(Ext.getCmp("fileName").getValue()==""){
						Msg.info("请选择需要导入的文件！");
						return false;
					}
					Ext.getCmp(panelId).getForm().submit({
						method:"post",
						url:'/EMQ/fileImportServlet?type='+fileType, 
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
	      }]}
	    ];	
		this.on("click",function(){
		  alert("");
	    });
		Ext.FileImportPanel.superclass.initComponent.call(this);
	}
});
// 注册
Ext.reg('Ext.FileImportPanel', Ext.FileImportPanel);