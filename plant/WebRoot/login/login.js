/**
 * fool create
 * 
 * @type String
 */

Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';

Ext.onReady(function() {
			Ext.QuickTips.init();
			createMainUI();
		});

function createMainUI() {
	var form = new Ext.FormPanel({
				height : 100,
				width : 300,
				frame : true,
				labelWidth : 80,
				labelAlign : 'right',
				baseCls : 'x-plain',
				items : [{
							xtype : 'textfield',
							name : 'name',
							fieldLabel : '用户名',
							allowBlank : false,
							msgTarget : 'side'
						}, {
							xtype : 'textfield',
							name : 'password',
							inputType : 'password',
							fieldLabel : '密　码',
							allowBlank : false,
							msgTarget : 'side'
						}]
			});
	var window = new Ext.Window({
				title : '用户登录',
				width : 300,
				height : 150,
				plain : false,
				draggable:false,
				bodyStyle : 'padding:5px;',
				buttonAlign : 'center',
				closable : false,
				resizable : false,
				items : form,
				buttons : [{
					text : '登录',
					listeners : {
						'click' : function() {
							if (form.getForm().isValid()) {
								form.getForm().submit({
											url : '../servlet/Login',
											waitMsg : '系统正在验证您的登录信息,请稍候...',
											method:'post',
											success : function(form, action) {
												Ext.Msg.alert('欢迎','欢迎登录后台管理系统!');
												this.document.location='login_suc.jsp';
												//Ext.Msg.alert('提示',
														//action.result.msg);
											},
											failure : function(form, action) {
												//Ext.Msg.alert('提示',
														//action.result.msg);
												Ext.Msg.alert('错误','请核对用户密码后再登录!');
											}
										});
							}
						}
					}
				}, {
					text : '重置',
					listeners : {
						'click' : function() {
							
							form.getForm().reset();
						}
					}
				}]
			});
	window.show();

}