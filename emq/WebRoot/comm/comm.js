/**
 * 针对DWR错误处理的公共函数定义
 * @param {} msg
 * @param {} e
 */
//DWR的错误处理
function DWRErrorHandler(msg,e)
{
	var errmsg = '';
	if(e){
		errmsg = e.message;
	}
	
	if(typeof(msg) == 'object')
	{
		errmsg = '文件名:' + msg.fileName + '<br>' + '行号:' + msg.lineNumber + '<br>' + '错误描述:' + msg.message;
	}else if(typeof(msg) == 'string'){
		errmsg = msg;
	}
	Ext.Msg.hide();
	alert(errmsg);
	/*		
	Ext.Msg.show({
				title:'调用错误',
		   		msg: errmsg,
		   		buttons: Ext.Msg.OK,
		   		//fn: fun,
		   		//animEl: 'elId',
		   		icon: Ext.MessageBox.ERROR
			});
	*/
}