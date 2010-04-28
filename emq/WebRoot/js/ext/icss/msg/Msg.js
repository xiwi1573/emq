/**
 * 显示常用消息类
 * @include "../global/Global.js"
 */
Ext.namespace("Ext.icss");

Ext.icss.Msg = function(){
    return {  
    	
        /**
         * 显示等待消息窗口
         * <p>
         * Example code:
         * <pre><code>
         Ext.icss.Msg.waiting('msg','title');
         * </code></pre>
         * @param strMsg 需要显示的消息
    	 * @param strTitle 标题
         */
        waiting: function(strMsg,strTitle){
        	
        	var title = strTitle;
        	if(!title)
        	{
        		title = Global.INFOMATION_TITLE;
        	}
        	Ext.Msg.wait(strMsg,title,{hideParent:false});
        },
        /**
         * 显示信息消息窗口
         * <p>
         * Example code:
         * <pre><code>
         Ext.icss.Msg.info('msg','title');
         * </code></pre>
         * @param strMsg 需要显示的消息
    	 * @param strTitle 标题
         */
        info:function(strMsg,strTitle){
        	/*
        	var title = strTitle;
        	if(!title)
        	{
        		title = Global.INFOMATION_TITLE;
        	}
        	
        	Ext.Msg.show({
		 		title:title,
				msg: strMsg,
				buttons: Ext.Msg.OK,
				icon: Ext.MessageBox.INFO
			});
			*/
        	alert(strMsg);
        	this.hide();
        },
        /**
         * 显示错误消息窗口
         * <p>
         * Example code:
         * <pre><code>
         Ext.icss.Msg.error('msg','title');
         * </code></pre>
         * @param strMsg 需要显示的消息
    	 * @param strTitle 标题
         */
        error:function(strMsg,strTitle){
        	/*
        	var title = strTitle;
        	if(!title)
        	{
        		title = Global.ERROR_TITLE;
        	}
        	Ext.Msg.show({
		 		title:title,
				msg: strMsg,
				buttons: Ext.Msg.OK,
				icon: Ext.MessageBox.ERROR
			});*/
        	alert(strMsg);
        	this.hide();
        },
        /**
         * 显示警告消息窗口
         * <p>
         * Example code:
         * <pre><code>
         Ext.icss.Msg.warning('msg','title');
         * </code></pre>
         * @param strMsg 需要显示的消息
    	 * @param strTitle 标题
         */
        warning:function(strMsg,strTitle){
        	alert(strMsg);
        	this.hide();
        	/*
        	var title = strTitle;
        	if(!title)
        	{
        		title = Global.WARNING_TITLE;
        	}
        	Ext.Msg.show({
		 		title:title,
				msg: strMsg,
				buttons: Ext.Msg.OK,
				icon: Ext.MessageBox.WARNING
			});*/
        },
        /**
         * 显示确定提示消息窗口
         * <p>
         * Example code:
         * <pre><code>
         Ext.icss.Msg.confirm('msg','title');
         * </code></pre>
    	 * @param fn 用户选择按钮后的回调方法，此方法声名格式为 function fn(btn){},
    	 * 		btn为用户选择的按钮id值：
    	 * 		- yes 用户选择了确定
    	 * 		- no  用户选择了取消
    	 * @param strMsg 需要显示的消息
    	 * @param strTitle 标题
    	 * @return boolean true 用户选择了确认 false 用户选择了取消 
         */
        confirm:function(fn,strMsg,strTitle){
        	/*
        	var title = strTitle;
        	if(!title)
        	{
        		title = Global.INFOMATION_TITLE;
        	}
        	Ext.Msg.show({
		 		title:title,
				msg: strMsg,
				buttons: Ext.MessageBox.YESNO,
				icon: Ext.MessageBox.QUESTION,
				fn:fn
			});*/

        	var bOk = confirm(strMsg);
        	this.hide();
        	if(typeof(fn) == 'function')
        	{
        		var btn = (bOk ? 'yes' : 'no');
	        	fn(btn);
        	}
        	
        	return bOk;

        },
        /**
         * 调用HTML DOM的confirm方法显示确定提示消息窗口
         * <p>
         * Example code:
         * <pre><code>
         Ext.icss.Msg.confirmDOM('msg');
         * </code></pre>
    	 * @param strMsg 需要显示的消息
    	 * @return true 用户选择了确认 false 用户选择了取消
         */
        confirmDOM:function(strMsg){
        	return confirm(strMsg);
        },
        /**
         * 处理弹出输入窗口
         * @param title 标题
         * @param msg 显示消息
         * @param fn 处理函数
         * @param value 默认值
         * @param multiline true 多行 false 单行，默认为单行
         * @param scope 作用范围
         */
        prompt:function(title, msg, fn,value,multiline,scope){
        	Ext.Msg.prompt(title, msg,fn,scope,multiline,value);
        },        
        hide: function(){
        	Ext.Msg.hide();
        }    
    };
}
();
/**
 * 定义别名
 * @type 
 */
Msg = Ext.icss.Msg;