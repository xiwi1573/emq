

Ext.onReady(function() {
			createMianUI();
		});

function createMianUI() {
	var combo = new Ext.form.commonCombox({
				id : "combo",
				fn : Test.testCombox,
				valueField : "code",
				displayField : "text"
			});
	var frm = new Ext.form.FormPanel({
	  items:[{xtype:'tbtext',text:'商业公司:'},combo],
	  bbar:[{text:"ok",handler:advent}]
	});
	frm.render(Ext.getBody());
	
}

function advent(){
  alert(Ext.getCmp("combo").getValue());
  alert(Ext.getCmp("combo").getRawValue());
}