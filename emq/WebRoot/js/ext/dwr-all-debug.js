/**
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 * 作者：zhangyw
 *
 *
 *完整调用示例： 
function dwrtest(){	
	var store=new Ext.data.DWRStoreEx({		
  		id:"LIST_ID",
  		fn:TreeDataServiceImpl.getXML,  
  		reader:new Ext.data.XmlStringReader({
				record:"c_order"
				//totalRecords:'rows'
				},
				[{name:'LIST_ID',type:'int'},{name:'SNAME'},{name:'BNAME'},
					{name:'ORDER_DATE'},{name:'SETTLEWAY'}]
				)
  		});  		
	var cm=new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer({width:50}),
		{header: "LIST_ID",dataIndex:"LIST_ID",sortable:true},
		{header: "SNAME",dataIndex:"SNAME",sortable:true},
		{header: "BNAME",dataIndex:"BNAME",sortable:true},
		{header: "ORDER_DATE",dataIndex:"ORDER_DATE",sortable:true},
		{header: "SETTLEWAY",dataIndex:"SETTLEWAY",sortable:true}
		]);	
		
	var grid=new Ext.grid.GridPanel({  	
		store:store,
	  	cm:cm, 
	    loadMask: {msg:'正在加载数据，请稍候……'},
	    //autoExpandColumn:4,
	    frame:true,	    
	    tbar:[{pressed:true,text:'查询',handler:query}]
	  });	 
 	 new Ext.Viewport({layout:"fit",items:grid});
 	
 	store.load({params:{params:'a param',param:'b param'}}); 	
 	return grid;
 }

var grid;
Ext.onReady(function()
{		
	grid = dwrtest();
});

function query()
{
	grid.getStore().load({params:{params:'a param',param:'b param'}});
}
*/

/**
* Ext.data.DWRProxy实现对DWR调用的代理，主要实现了load方法
* load方法主要是对DWR方法的调用及处理,目前，只支持调用DWR参数类型为MAP的方法
* 调用举例：
* java方法定义：
* public String getXML(Map param)
* {
*		System.out.println("params:" + param.get("params") + "  param:" + param.get("param"));
*		do somthing....
* }
*
* js中调用：store.load({params:{params:'a param',param:'b param'}});
*	{params:{params:'a param',param:'b param'}}为参数对象，其中，
*	每个属性及值对应MAP中的key:value键值对
*/

Ext.data.DWRProxy = function(fn){
    Ext.data.DWRProxy.superclass.constructor.call(this);
    this.fn = fn;
};
Ext.extend(Ext.data.DWRProxy, Ext.data.DataProxy, {  
    load : function(params, reader, callback, scope, arg){
        params = params || {};       
        if(this.fireEvent("beforeload", this, params) !== false)
        {
        	var proxy=this;
			//dwr回调函数
      		dwrcallback = function(ret){
		        var result;
		        try 
		        {	    	
		        	
		        	if(typeof(ret) == "object") 
		        	{
		            	result = reader.readRecords(ret);
		        	}
		        	else
		        	{
		        		var o = eval("("+ret+")");
		        		result = reader.readRecords(o);
		        	}
		        }catch(e)
		        {
		            this.fireEvent("loadexception", this, arg, null, e);
		            callback.call(scope, null, arg, false);
		            return;
		        }
		        callback.call(scope, result, arg, true);
	        }
	        //需要将参数加入到调用参数
        	var callParams = new Array();
        	if(Ext.isArray(params))
        	{
	        	for(var i=0; i<params.length;++i)
	        	{
	        		callParams.push(params[i]);
	        	}
        	}
        	else if(typeof(params) == 'object' && params.query)
        	{//为了支持下拉Combox的调用,增加对query参数的支持
        		for(var i=0; i<params.query.length;++i)
	        	{
	        		callParams.push(params.query[i]);
	        	}
        	}
	        //将回调函数加入的参数数组
	        callParams.push(dwrcallback);
      		this.fn.apply(this, callParams);
        }   
    }

});

/**
* Ext.data.DWRStore实现对DWR调用后返回的记录进行存储
*	当调用的DWR方法返回的是JSON对象时，使用此存储类进行管理
*/
Ext.data.DWRStore = function(c){
    Ext.data.DWRStore.superclass.constructor.call(this, Ext.apply(c, {
        proxy: c.fn ? new Ext.data.DWRProxy(c.fn): undefined,
        reader: c.reader ? c.reader : new Ext.data.JsonReader(c, c.fields)
    }));
};
Ext.extend(Ext.data.DWRStore, Ext.data.StoreEx);


/**
* Ext.data.DWRStoreEx 实现对DWR调用后返回XML字符串的存储
*	很多情况下，我们的调用都是返回一个XML串，而不是XML应答包，
*	EXT中，XmlReader类只对XML应答包进行解析处理，而对于XML字符串没做这样的封装
*   此类的reader必须是XmlStringReader
*/
Ext.data.DWRStoreEx = function(c){
			
    Ext.data.DWRStoreEx.superclass.constructor.call(this, Ext.apply(c, {
        proxy: c.fn ? new Ext.data.DWRProxy(c.fn) : undefined,
        reader: c.reader ? c.reader : new Ext.data.XmlStringReader(c, c.fields)
    }));
};
Ext.extend(Ext.data.DWRStoreEx, Ext.data.StoreEx);


/**
 * @class Ext.data.XmlReader
 * @extends Ext.data.DataReader
 * Data reader class to create an Array of {@link Ext.data.Record} objects from an XML document
 * based on mappings in a provided Ext.data.Record constructor.<br><br>
 * <p>
 * <em>Note that in order for the browser to parse a returned XML document, the Content-Type
 * header in the HTTP response must be set to "text/xml".</em>
 * <p>
 * Example code:
 * <pre><code>
var Employee = Ext.data.Record.create([
   {name: 'name', mapping: 'name'},     // "mapping" property not needed if it's the same as "name"
   {name: 'occupation'}                 // This field will use "occupation" as the mapping.
]);
var myReader = new Ext.data.XmlStringReader({
   totalRecords: "results", // The element which contains the total dataset size (optional)
   record: "row",           // The repeated element which contains row information
   id: "id"                 // The element within the row that provides an ID for the record (optional)
}, Employee);
</code></pre>
 * <p>
 * This would consume an XML file like this:
 * <pre><code>
&lt;?xml version="1.0" encoding="UTF-8"?>
&lt;dataset>
 &lt;results>2&lt;/results>
 &lt;row>
   &lt;id>1&lt;/id>
   &lt;name>Bill&lt;/name>
   &lt;occupation>Gardener&lt;/occupation>
 &lt;/row>
 &lt;row>
   &lt;id>2&lt;/id>
   &lt;name>Ben&lt;/name>
   &lt;occupation>Horticulturalist&lt;/occupation>
 &lt;/row>
&lt;/dataset>
</code></pre>
 * @cfg {String} totalRecords The DomQuery path from which to retrieve the total number of records
 * in the dataset. This is only needed if the whole dataset is not passed in one go, but is being
 * paged from the remote server.
 * @cfg {String} record The DomQuery path to the repeated element which contains record information.
 * @cfg {String} success The DomQuery path to the success attribute used by forms.
 * @cfg {String} id The DomQuery path relative from the record element to the element that contains
 * a record identifier value.
 * @constructor
 * Create a new XmlReader.
 * @param {Object} meta Metadata configuration options
 * @param {Object} recordType Either an Array of field definition objects as passed to
 * {@link Ext.data.Record#create}, or a Record constructor object created using {@link Ext.data.Record#create}.
 */
Ext.data.XmlStringReader = function(meta, recordType){
    meta = meta || {};
    Ext.data.XmlStringReader.superclass.constructor.call(this, meta, recordType || meta.fields);
};
Ext.extend(Ext.data.XmlStringReader, Ext.data.DataReader, {
    /**
     * This method is only used by a DataProxy which has retrieved data from a remote server.
	 * @param {Object} response The XHR object which contains the parsed XML document.  The response is expected
	 * to contain a property called <tt>responseXML</tt> which refers to an XML document object.
     * @return {Object} records A data block which is used by an {@link Ext.data.StoreEx} as
     * a cache of Ext.data.Records.
     */
    read : function(response){
        var strText = response.responseText;
        if(!strText) {
            throw {message: "XmlReader.read: XML Document not available"};
        }
        return this.readRecords(strText);
    },

	
	/**
	*	Ext.data.XmlStringReader.parseText
	*	将strText字符串解析为XMLDocument对象
	*/
	parseText : function(strText)
	{
		
		var xmlDom = null;
		if(null != strText)
		{
			try //Internet Explorer
			{
			  xmlDom=new ActiveXObject("Microsoft.XMLDOM");
			}
			catch(e)
			{
			  try //Firefox, Mozilla, Opera, etc.
			    {
			    	xmlDom=document.implementation.createDocument("","",null);
			    }
			  catch(e) {throw {message: e.message};}
			}
			
			try 
			{
			  	xmlDom.async = "true";
	　　			xmlDom.loadXML(strText);
			}catch(e) {throw {message: e.message};}
		}

		return xmlDom;
	},
    
    /**
     * Create a data block containing Ext.data.Records from an XML document.
	 * @param {Object} doc A parsed XML document.
     * @return {Object} records A data block which is used by an {@link Ext.data.StoreEx} as
     * a cache of Ext.data.Records.
     */
    readRecords : function(strText){
        /**
         * After any data loads/reads, the raw XML Document is available for further custom processing.
         * @type XMLDocument
         */
         
        var doc = this.parseText(strText);
        //doc.save('c:\\temp.xml');
        //var tmproot = doc.documentElement;
   		//alert('XML rows:'+  tmproot.childNodes.length);
        
        this.xmlData = doc;
        var root = doc.documentElement || doc;
    	var q = Ext.DomQuery;
    	var recordType = this.recordType;
    	var fields = recordType.prototype.fields;
    	var sid = this.meta.id;
    	var totalRecords = 0, success = true;
    	if(this.meta.totalRecords){
    	    totalRecords = q.selectNumber(this.meta.totalRecords, root, 0);
    	}

        if(this.meta.success){
            var sv = q.selectValue(this.meta.success, root, true);
            success = sv !== false && sv !== 'false';
    	}
    	var records = [];
    	var ns = q.select(this.meta.record, root);
        for(var i = 0, len = ns.length; i < len; i++) {
	        var n = ns[i];
	        var values = {};
	        var id = sid ? q.selectValue(sid, n) : undefined;
	        for(var j = 0, jlen = fields.length; j < jlen; j++){
	            var f = fields.items[j];
                var v = q.selectValue(f.mapping || f.name, n, f.defaultValue);
	            v = f.convert(v, n);
	            values[f.name] = v;
	        }
	        var record = new recordType(values, id);
	        record.node = n;
	        records[records.length] = record;
	    }

	    return {
	        success : success,
	        records : records,
	        totalRecords : totalRecords || records.length
	    };
    }
});


Ext.tree.DWRTreeLoader=function(config){
  Ext.apply(this, config);
  Ext.tree.DWRTreeLoader.superclass.constructor.call(this);	
};

Ext.extend(Ext.tree.DWRTreeLoader,Ext.tree.TreeLoader,{
	fn:null,
	args:[],
	load:function(node,callback){			
		if(this.clearOnLoad){
	            while(node.firstChild){
	                node.removeChild(node.firstChild);
	            }
	        }	       
	        if(this.doPreload(node)){ // preloaded json children
	            if(typeof callback == "function"){
	                callback();
	            }
	        }else if(this.fn){
	            this.loadNode(node, callback);
	        }
	},
	loadNode:function(node,callback){	
		if(this.fireEvent("beforeload", this, node, callback) !== false)
		{
			var ps=[];
			for(var i=0;i<this.args.length;i++)
			{
				ps[ps.length]=this.args[i];
			}
			
			ps[this.args.length]=function(ret)
			{
				var is = ps.length;
				ps[is-1].processResponse(ret, node, callback);
	        	ps[is-1].fireEvent("load", ps[is-1], node, ret);
				/*
				this.processResponse(ret, node, callback);
	        	this.fireEvent("load", this, node, ret);
	        	*/	
			};
			
			ps[ps.length]=this;//the callback function's scope		
			this.fn.apply(this,ps);
		}
		else
		{
            if(typeof callback == "function")
            {
                callback();
            }
        }		
	},
	processResponse : function(ret, node, callback){
        try {
            var o = ret;
            node.beginUpdate();            
            if(o && typeof o=="object"){
            for(var i = 0, len = o.length; i < len; i++){
                var n = this.createNode(o[i]);
                if(n){
                    node.appendChild(n);
                }
            }}           
            node.endUpdate();            
            if(typeof callback == "function"){
                callback(this, node);
            }
        }catch(e){
            this.handleFailure(response);
        }
    }    
});
