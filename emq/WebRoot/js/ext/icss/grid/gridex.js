/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
 
/**
 * @class Ext.grid.EditorGridPanelEx
 * @extends Ext.grid.EditorGridPanel
 * @version 1.0
 * <p>此类是为Ext表格封装</p>
 * @constructor
 * @param {Object} config The config object
 */

Ext.grid.GridPanelEx = Ext.extend(Ext.grid.GridPanel, {
  getView : function() {
        if (!this.view) {
            this.view = new Ext.grid.GridViewEx(this.viewConfig);
        }
        return this.view;
    },
  initComponent : function() {
  	var cm = null;
    if(typeof(this.cm)=="undefined"){
    	var headerList = [];
    	var fieldList = [];
    	var rowsList = [];
    	if(typeof(this.extdata)!="undefined"){
    		var gridDate = this.extdata.getExtGrid();
    		headerList = gridDate[0];
    		fieldList = gridDate[1];
    		if(this.extdata.tableType!=1){
    			rowsList = gridDate[2];
    		}
    	}
    	var rowNum =  new Ext.grid.RowNumberer({
	      width : 30
	    });
	    rowNum.locked = true;
	    var sm = new Ext.grid.CheckboxSelectionModel();
	    sm.locked = true;
	    var issum = false;
	    var columnMetadata ="[";
	    for(var i=0;i<headerList.length;i++){
	    	columnMetadata += headerList[i]+",";
		}
		if(columnMetadata.length>2){
			columnMetadata = columnMetadata.substring(0,columnMetadata.length-1);
		}
		columnMetadata += "]";
		if(columnMetadata.indexOf('sumcaption')!=-1){
			issum = true;
		}
		columnMetadata = Ext.util.JSON.decode(columnMetadata);
		Util.Array.insert(columnMetadata,0,rowNum);
		Util.Array.insert(columnMetadata,1,sm);
		cm = new Ext.grid.ColumnModelEx(columnMetadata);
		cm.sumheader = issum;
		if(this.extdata.tableType!=1&&rowsList.length>0){
			var rowsMetadata ="[";
		    for(var i=0;i<rowsList.length;i++){
		    	rowsMetadata += "[";
		    	var rowList = rowsList[i];
		    	for(var j=0;j<rowList.length;j++){
		    		rowsMetadata += rowList[j]+",";
		    	}
		    	if(rowsMetadata.length>5){
				    rowsMetadata = rowsMetadata.substring(0,rowsMetadata.length-1);
			    }
			    rowsMetadata += "],"
			}
			rowsMetadata = rowsMetadata.substring(0,rowsMetadata.length-1);
			rowsMetadata += "]";
		    cm.rows = Ext.util.JSON.decode(rowsMetadata);
	    }
	}
	this.cm=cm;
	this.trackMouseOver=true;
	this.loadMask={msg:'正在加载数据，请稍候……'};
	this.frame=true;
	this.border=false;
	if(typeof(this.height)=="undefined"){
		this.height=Ext.icss.Util.getH(0.4);
	}
	
    if (!this.cm) {
        this.cm = new Ext.grid.ColumnModelEx(this.columns);
        this.colModel = this.cm;
        delete this.columns;
    }
    if(!this.groupedHeaders)
    {
    	this.groupedHeaders = this.groupedHeaders;
    }
    if(!this.loadMask)
    {
    	this.loadMask = Global.getDefaultLoadMask();
    }
    var fns = null;
    if(typeof(this.fn)=="undefined"){
    	this.fn = null;
    }
    fns = this.fn;
    if(typeof(this.store)=="undefined"){
    	var storeMetadata = "{fields:[";
    	for(var i=0;i<fieldList.length;i++){
	    	storeMetadata += fieldList[i]+",";
		}
		if(storeMetadata.length>10){
			storeMetadata = storeMetadata.substring(0,storeMetadata.length-1);
		}
		storeMetadata += "]}";
		storeMetadata = Ext.util.JSON.decode(storeMetadata);
		storeMetadata.fn = fns;
		this.store = new Ext.data.DWRStore(storeMetadata);  	
	}
	if(this.extdata.tableType!=1){
		this.plugins=[new Ext.ux.plugins.GroupHeaderGrid()];
	}
	this.getStore().on('load',this.onload,this);
    this.on('afteredit',this.onAfteredit,this);
    this.on('headerclick',Util.Grid.selectAll);
   
    Ext.grid.GridPanelEx.superclass.initComponent.call(this);
  },
  onload:function(ds,recs,opt){
	  if(ds)
	  {
	  	if(ds.getCount()==0){
	  		this.recalculation();
	  	}
	  	
	  }
  },
  load:function(p){
  	if(typeof(p)=="undefined"){
  		this.store.load({params:[]});
  	}else{
  		this.store.load({params:[p]});
  	}
  	
  },
  getGroupedHeaders : function(){
        return this.groupedHeaders;
  },
  /**
   * 处理修改后，更新合计
   * @param {} event
   * grid - This grid
   * record - The record being edited
   * field - The field name being edited
   * value - The value being set
   * originalValue - The original value for the field, before the edit.
   * row - The grid row index
   * column - The grid column index
   */
  onAfteredit:function(event){
  	var grid = event.grid;
  	var cm = grid.getColumnModel();
  	if(false == cm.sumheader)
  	{
  		return;
  	}

  	var value = event.value;
  	var origValue = event.originalValue;
  	
  	cm.config[event.column].sumvalue -= origValue;
  	cm.config[event.column].sumvalue += value;
  	grid.getView().updateHeaders();
  },
  /**
   * 重新计算合计列
   */
  recalculation:function(){
  	var cm = this.getColumnModel();
  	if(true != cm.sumheader)
  	{
  		return;
  	}  	
  	var view = this.getView();
  	view.recalculation(this.getStore());
  },
   /**
   * 取得指定列ID的列上的合计数量
   * @param {} dataIndex
   */
  getSumValue:function(dataIndex){
  	var cm = this.getColumnModel();
  	if(true != cm.sumheader)
  	{
  		return;
  	}  	
  	var view = this.getView();
  	return view.getSumValue(dataIndex);
  },
  /**
   * 设置合计列的合计值
   * @param {} dataIndex 列名或列ID
   * @param {} v 合计值
   */
  setSumValue:function(dataIndex,v){
  	var cm = this.getColumnModel();
  	if(true != cm.sumheader)
  	{
  		return;
  	}  	
  	var view = this.getView();
  	view.setSumValue(dataIndex,v);
  }
});


Ext.grid.EditorGridPanelEx = Ext.extend(Ext.grid.EditorGridPanel, {
	/**
     * @cfg {Number} 覆盖clicksToEdit
     * The number of clicks on a cell required to display the cell's editor (defaults to 2)
     */
 	clicksToEdit: 1,
  getView : function() {
        if (!this.view) {
            this.view = new Ext.grid.GridViewEx(this.viewConfig);
        }
        return this.view;
    },
  initComponent : function() {
    if (!this.cm) {
    	
        this.cm = new Ext.grid.ColumnModelEx(this.columns);
        this.colModel = this.cm;
        delete this.columns;
    }
    if(!this.sm){
    	this.sm = new Ext.grid.RowSelectionModel();
    }
    if(!this.groupedHeaders)
    {
    	this.groupedHeaders = this.groupedHeaders;
    }
    if(!this.loadMask)
    {
    	this.loadMask = Global.getDefaultLoadMask();
    }
    this.getStore().on('load',this.onload,this);
    
    
    //在编辑完成后，需要更新合计列
    this.on('afteredit',this.onAfteredit,this);
    Ext.grid.EditorGridPanelEx.superclass.initComponent.call(this);
  },
  
   onload:function(ds,recs,opt){
	  if(ds.getCount()==0)
	  {
	  	this.recalculation(ds);
	  }
  },
  
  getGroupedHeaders : function(){
        return this.groupedHeaders;
  },
  /**
   * 处理修改后，更新合计
   * @param {} event
   * grid - This grid
   * record - The record being edited
   * field - The field name being edited
   * value - The value being set
   * originalValue - The original value for the field, before the edit.
   * row - The grid row index
   * column - The grid column index
   */
  onAfteredit:function(event){
  	var grid = event.grid;
  	var cm = grid.getColumnModel();
  	if(false == cm.sumheader)
  	{
  		return;
  	}

  	var value = event.value;
  	var origValue = event.originalValue;
  	
  	cm.config[event.column].sumvalue -= origValue;
  	cm.config[event.column].sumvalue += value;
  	grid.getView().updateHeaders();
  },
  /**
   * 重新计算合计列
   */
  recalculation:function(){
  	var cm = this.getColumnModel();
  	if(true != cm.sumheader)
  	{
  		return;
  	}  	
  	var view = this.getView();
  	view.recalculation(this.getStore());
  },
   /**
   * 取得指定列ID的列上的合计数量
   * @param {} dataIndex
   */
  getSumValue:function(dataIndex){
  	var cm = this.getColumnModel();
  	if(true != cm.sumheader)
  	{
  		return;
  	}  	
  	var view = this.getView();
  	return view.getSumValue(dataIndex);
  },
  /**
   * 设置合计列的合计值
   * @param {} dataIndex 列名或列ID
   * @param {} v 合计值
   */
  setSumValue:function(dataIndex,v){
  	var cm = this.getColumnModel();
  	if(true != cm.sumheader)
  	{
  		return;
  	}  	
  	var view = this.getView();
  	view.setSumValue(dataIndex,v);
  }  
});

Ext.grid.GridViewEx = function(config) {
    Ext.apply(this, config);
    if (!this.templates) this.templates = {};
    
    
    //增加合计行模板
    if(!this.templates.header){
            this.templates.header = new Ext.Template(
                    '<table border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
                    '<thead><tr class="x-grid3-hd-row">{cells}</tr><tr class="x-grid3-hd-row x-grid3-row-sum">{sumcells}</tr></thead>',
                    "</table>"
                    );
    }
    
    if(!this.templates.sumcells){
            this.templates.sumcells = new Ext.Template(
                    '<td class="x-grid3-hd x-grid3-cell x-grid3-td-{id}" style="{style}"><div {tooltip} {attr} class="x-grid3-hd-inner x-grid3-hd-{id}" unselectable="on" style="{istyle}">', '',
                    '{value}', '', '',
                    "</div></td>"
                    );
    }
    
    
    Ext.grid.GridViewEx.superclass.constructor.call(this);
};

Ext.extend(Ext.grid.GridViewEx, Ext.grid.GridView, {
	
	insertRows : function(ds, firstRow, lastRow, isUpdate){
        if(!isUpdate && firstRow === 0 && lastRow == ds.getCount()-1){
            this.refresh();
        }else{
            if(!isUpdate){
                this.fireEvent("beforerowsinserted", this, firstRow, lastRow);
            }
            var html = this.renderRows(firstRow, lastRow);
            var before = this.getRow(firstRow);
            if(before){
                Ext.DomHelper.insertHtml('beforeBegin', before, html);
            }else{
                Ext.DomHelper.insertHtml('beforeEnd', this.mainBody.dom, html);
            }
            if(!isUpdate){
                this.fireEvent("rowsinserted", this, firstRow, lastRow);
                this.processRows(firstRow);
            }
        }
    },
    onUpdate : function(ds, record){
        this.refreshRow(record);
    },
        // private
    refreshRow : function(record){
        var ds = this.ds, index;
        if(typeof record == 'number'){
            index = record;
            record = ds.getAt(index);
        }else{
            index = ds.indexOf(record);
        }
        var cls = [];
        this.insertRows(ds, index, index, true);
        this.getRow(index).rowIndex = index;
        this.onRemove(ds, record, index+1, true);
        this.fireEvent("rowupdated", this, index, record);
        /*
        if(true == record.dirty && true == this.cm.sumheader){
        	var cm = this.cm;
        	var colCount =  cm.getColumnCount();
        	for(var i=0; i<colCount;++i)
        	{
        		var c = cm.config[i];
        		if( true == c.issum && typeof record.modified[c.dataIndex] !== 'undefined'){
                    var value = record.get(c.dataIndex);
                    var origValue = record.modified[c.dataIndex];
                    if(origValue){
			  			cm.config[i].sumvalue -= origValue;
			  			cm.config[i].sumvalue += value;
                	}
        		}
        	}
		  	this.updateHeaders();
        }*/
    },
    onRemove : function(ds, record, index, isUpdate){
        if(isUpdate !== true){
            this.fireEvent("beforerowremoved", this, index, record);
        }
        this.removeRow(index);
        if(isUpdate !== true){
            this.processRows(index);
            this.applyEmptyText();
            this.fireEvent("rowremoved", this, index, record);
            
            //处理删除行时的合计行问题
            this.processSumForRemoved(ds,record,index);
        }
    },
    
    //Template has changed and we need a few other pointers to keep track
    doRender : function(cs, rs, ds, startRow, colCount, stripe){
        var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount-1;
        var tstyle = 'width:'+this.getTotalWidth()+';';
        // buffers
        var buf = [], cb, c, p = {}, rp = {tstyle: tstyle}, r;
		//如果不是在编辑状态下，则需要将合计列的数据清0        
        if(false == rs[0].dirty && true ==(rs.length == ds.getCount())){
        	this.clearSumZero();
        }
        for(var j = 0, len = rs.length; j < len; j++){
            r = rs[j]; cb = [];
            var rowIndex = (j+startRow);
            for(var i = 0; i < colCount; i++){
                c = cs[i];
                p.id = c.id;
                p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
                p.attr = p.cellAttr = "";
                p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
                p.style = c.style;
                if(p.value == undefined || p.value === "") p.value = "&#160;";
                if(r.dirty && typeof r.modified[c.name] !== 'undefined'){
                    p.css += ' x-grid3-dirty-cell';
                }
                cb[cb.length] = ct.apply(p);
                
                //处理求和问题
                if(true == this.cm.sumheader)
        		{
	                if(true == this.cm.config[i].issum && false == r.dirty)
	                {//如果是求和列且不为脏数据
	                	if(!this.cm.config[i].sumvalue){
	                			this.cm.config[i].sumvalue = 0.0;
	                	}
	                	if(this.cm.config[i].sfn)
	                	{//如果存在求和函数，则调用
	                		this.cm.config[i].sumvalue = this.cm.config[i].sfn(this.cm.config[i].sumvalue,r.get(c.name),r);
	                	}
	                	else
	                	{//否则，直接累计
	                		if(r.data[c.name]){
	                			this.cm.config[i].sumvalue += r.data[c.name];
	                		}
	                	}
	                }
        		}
        		
            }
            var alt = [];
            if(stripe && ((rowIndex+1) % 2 == 0)){
                alt[0] = "x-grid3-row-alt";
            }
            if(r.dirty){
                alt[1] = " x-grid3-dirty-row";
            }
            rp.cols = colCount;
            if(this.getRowClass){
                alt[2] = this.getRowClass(r, rowIndex, rp, ds);
            }
            rp.alt = alt.join(" ");
            rp.cells = cb.join("");
            buf[buf.length] =  rt.apply(rp);
        }
        
        //如果有合计行，则需要重新渲染表头
        if(true == this.cm.sumheader)
        {
        	this.updateHeaders();
        }
        return buf.join("");
    },
     /**
     * 将求和的列清0
     */
    clearSumZero:function(){
    	this.hasSumColumn = false;
    	var cm = this.cm;
    	for(var i=0; i<cm.config.length;++i)
    	{
    		if(cm.config[i].issum)
    		{
    			this.hasSumColumn = true;
    			this.cm.config[i].sumvalue = 0.0;
    		}
    	}
    },
    /**
   * 取得指定列ID的列上的合计数量
   * @param {} dataIndex
   */
	getSumValue:function(dataIndex){
		var colCount = this.cm.getColumnCount();
		
		for(var i=0; i<this.cm.config.length; ++i)
		{
			if(true == this.cm.config[i].issum)
			{
				if(this.cm.config[i].dataIndex == dataIndex)
				{
					return this.cm.config[i].sumvalue;
				}
				else if((typeof dataIndex == 'number') && dataIndex == i)
				{
					return this.cm.config[i].sumvalue; 	
				}
			}
		}
		return;
   },
   /**
   * 设置合计列的合计值
   * @param {} dataIndex 列名或列ID
   * @param {} v 合计值
   */
   setSumValue:function(dataIndex,v){
   		var colCount = this.cm.getColumnCount();
		
		for(var i=0; i<this.cm.config.length; ++i)
		{
			if(true == this.cm.config[i].issum)
			{
				if(this.cm.config[i].dataIndex == dataIndex)
				{
					this.cm.config[i].sumvalue = v;
					this.updateHeaders();
				}
				else if((typeof dataIndex == 'number') && dataIndex == i)
				{
					this.cm.config[i].sumvalue = v; 	
					this.updateHeaders();
				}
								
			}
		}
   },
   renderHeaders : function(){
        var cm = this.cm, ts = this.templates;
        var ct = ts.hcell;

        var cb = [], sb = [],lb = [], lsb = [], p = {};

        for(var i = 0, len = cm.getColumnCount(); i < len; i++){
            p.id = cm.getColumnId(i);
            p.value = cm.getColumnHeader(i) || "";
            p.style = this.getColumnStyle(i, true);
            p.tooltip = this.getColumnTooltip(i);
            if(cm.config[i].align == 'right'){
                p.istyle = 'padding-right:16px';
            } else {
                delete p.istyle;
            }
            cb[cb.length] = ct.apply(p);
        }
        
        //处理合计行表头
        if(true == cm.sumheader)
        {
        	sb = this.buildSumHeaders(cm,ts);
        }
        
        return ts.header.apply({cells: cb.join(""), sumcells: sb.join(""),tstyle:'width:'+this.getTotalWidth()+';'});
        
        //return [ts.header.apply({cells: cb.join(""), sumcells: sb.join(""), tstyle:'width:'+(tw-lw)+';'}),
        //ts.header.apply({cells: lb.join(""), sumcells: lsb.join(""), tstyle:'width:'+(lw)+';'})];
        
        //return ts.header.apply({cells: cb.join(""), tstyle:'width:'+this.getTotalWidth()+';'});
    },
    
    
    /**
     * 生成合计表头
     * @param {} cm
     * @param {} ts
     * @param {} tw
     * @param {} lw
     * @return {}array
     */
    buildSumHeaders:function(cm,ts){
        var ct = ts.hcell;

        var sb = [], sp = {};

        for (var i = 0, len = cm.getColumnCount(); i < len; i++) {

            sp.id = 'sum' + cm.getColumnId(i);
            if(cm.config[i].sumcaption){
            	sp.value = cm.config[i].sumcaption;
            }else{
            	sp.value = "&nbsp;";
            }
            
            //cm.config[i].sumcaption = "&nbsp;";
            if (true == cm.config[i].issum && typeof cm.config[i].sumvalue == 'number'){
            	if(cm.config[i].renderer)
            	{
            		sp.value = cm.config[i].renderer(cm.config[i].sumvalue);
            	}else{
            		sp.value = cm.config[i].sumvalue;
            	}
            }
            
            
            sp.style = this.getColumnStyle(i, false);
            sp.tooltip = this.getColumnTooltip(i);
            
            if (cm.config[i].align == 'right') {
                sp.istyle = 'padding-right:16px';
            }

             sb[sb.length] = ct.apply(sp);

        }
        return sb;
    },
    /**
     * 处理删除行时，更新合计行的数据
     * @param {} ds
     * @param {} record
     * @param {} index
     */
    processSumForRemoved: function(ds,record,index){
    	var cm = this.cm;
    	if(true != cm.sumheader){
    		return;
    	}
    	
    	var cfg = cm.config;
    	for(var i=0; i<cfg.length; ++i){
    		if(true == cfg[i].issum && cfg[i].sumvalue){
    			var val = record.get(cfg[i].dataIndex);
    			if(val){
    				cfg[i].sumvalue -= val;
    			}
    		}
    	}
    	this.updateHeaders();
    },
	/**
	 * 重新计算合计列
	 */
	recalculation:function(ds){
	  	
	  	this.clearSumZero();
	  	if(true != this.hasSumColumn){
	  		return;
	  	}

	  	var colCount = this.cm.getColumnCount();
	  	var records = ds.getRange();
		for(var k=0; k<records.length; ++k){
			record = records[k];
			for(var i=0; i<this.cm.config.length; ++i){
				if(true == this.cm.config[i].issum){
					var val = record.get(this.cm.config[i].dataIndex);
					if(typeof val == 'number'){
						this.cm.config[i].sumvalue += val;
					}
				}
			}
		};
		
		this.updateHeaders();
	  	
	}
    
});


Ext.grid.ColumnModelEx = function(config) {
	//自动完成选择编辑控件时，全选内容
	if(config && Ext.isArray(config.columns))
	{
		
		for(var i=0; i<config.columns.length; ++i)
		{	
			var c = config.columns[i];
			if(c.editor)
			{
				if(typeof(c.editor.selectOnFocus))
				{
					c.editor.selectOnFocus = true; 
				}
			}
		}
	}
    Ext.grid.ColumnModelEx.superclass.constructor.call(this, config);
    //alert('config.groupedHeaders ' + config.groupedHeaders);
	//this.groupedHeaders = config.groupedHeaders;
	
};

Ext.extend(Ext.grid.ColumnModelEx, Ext.grid.ColumnModel, {
    /**
     * Returns true if the specified column menu is disabled.
     * @param {Number} col The column index
     * @return {Boolean}
     */
    isMenuDisabled : function(col){
    	if(-1 == col){
    		return true;
    	}else{
        	return !!this.config[col].menuDisabled;
    	}
    }
});
Ext.reg('gridex', Ext.grid.GridPanelEx);
Ext.reg('editorgridex', Ext.grid.EditorGridPanelEx);


