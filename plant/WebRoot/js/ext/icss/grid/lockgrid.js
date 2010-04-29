Ext.grid.LockingGridPanel = Ext.extend(Ext.grid.GridPanel, {
    getView : function() {
        if (!this.view) {
            this.view = new Ext.grid.LockingGridView(this.viewConfig);
        }else{
			Ext.apply(this.view, this.viewConfig);
		}
        return this.view;
    },
    initComponent : function() {
    if (!this.cm) {
    	
        this.cm = new Ext.grid.LockingColumnModel(this.columns);
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
    
    
   this.getStore().on('load',this.onload,this);
    Ext.grid.LockingGridPanel.superclass.initComponent.call(this);
    
  },
 onload:function(ds,recs,opt){
	  if(ds)
	  {
	  	if(ds.getCount()==0){
	  		this.recalculation();
	  	}
	  }
  },
  getGroupedHeaders : function(){
        return this.groupedHeaders;
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

Ext.grid.LockingGridView = function(config) {
    Ext.apply(this, config);
    if (!this.templates) this.templates = {};
    this.fixedRows = 0;
    if (!this.templates.master) {
        this.templates.master = new Ext.Template(
            '<div class="x-grid3" hidefocus="true">',
                '<div class="x-grid3-locked">',
                '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset">{lockedHeader}</div></div><div class="x-clear"></div></div>',
                '<div class="x-grid3-scroller"><div class="x-grid3-body">{lockedBody}</div></div>',
                '</div>',
                '<div class="x-grid3-viewport">',
                '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset">{header}</div></div><div class="x-clear"></div></div>',
                '<div class="x-grid3-scroller"><div class="x-grid3-body">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
                '</div>',
                '<div class="x-grid3-resize-marker">&#160;</div>',
                '<div class="x-grid3-resize-proxy">&#160;</div>',
            '</div>'
        );
    }
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
    
    Ext.grid.LockingGridView.superclass.constructor.call(this);
};

Ext.extend(Ext.grid.LockingGridView, Ext.grid.GridView, {
    lockText : "Lock",
    unlockText : "Unlock",
	
    //Template has changed and we need a few other pointers to keep track
    initElements : function() {
        var E = Ext.Element;

        var el = this.grid.getGridEl();
        el = el.dom.firstChild;//.dom.childNodes[1];
        var cs = el.childNodes;

        this.el = new E(el);

        this.lockedWrap = new E(cs[0]);
        this.lockedHd = new E(this.lockedWrap.dom.firstChild);
        this.lockedInnerHd = this.lockedHd.dom.firstChild;
        this.lockedScroller = new E(this.lockedWrap.dom.childNodes[1]);
        this.lockedBody = new E(this.lockedScroller.dom.firstChild);

        this.mainWrap = new E(cs[1]);
        this.mainHd = new E(this.mainWrap.dom.firstChild);
        this.innerHd = this.mainHd.dom.firstChild;
        this.scroller = new E(this.mainWrap.dom.childNodes[1]);
        if (this.forceFit) {
            this.scroller.setStyle('overflow-x', 'hidden');
        }
        this.mainBody = new E(this.scroller.dom.firstChild);

        this.focusEl = new E(this.scroller.dom.childNodes[1]);
        this.focusEl.swallowEvent("click", true);

        this.resizeMarker = new E(cs[2]);
        this.resizeProxy = new E(cs[3]);
        this.grid.getStore().on('load',this.onload,this);
    },
  /**
     * 当删除行时，需要进行合计列处理
     * @param {} ds
     * @param {} record
     * @param {} index
     * @param {} isUpdate
     */
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
    
    onload:function(ds,recs,opt){
	  if(ds.getCount()==0)
	  {
	  	this.recalculation(ds);
	  }
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
    // private
    hasLockedRows : function() {
        var fc = this.lockedBody.dom.firstChild;
        return fc && fc.className != 'x-grid-empty';
    },

    getLockedRows : function() {
        return this.hasLockedRows() ? this.lockedBody.dom.childNodes : [];
    },

    getLockedRow : function(row) {
        return this.getLockedRows()[row];
    },

    getCell : function(rowIndex, colIndex) {
        var locked = this.cm.getLockedCount();
        var row;
        if (colIndex < locked) {
            row = this.getLockedRow(rowIndex);
        } else {
            row = this.getRow(rowIndex);
            colIndex -= locked;
        }
        return row.getElementsByTagName('td')[colIndex];
    },

    getHeaderCell : function(index) {
        var locked = this.cm.getLockedCount();
        if (index < locked) {
            return this.lockedHd.dom.getElementsByTagName('td')[index];
        } else {
            return this.mainHd.dom.getElementsByTagName('td')[(index-locked)];
        }
    },  
  
    scrollToTop : function() {
        Ext.grid.LockingGridView.superclass.scrollToTop.call(this);
        this.syncScroll();
    },

    syncScroll : function(e) {
        Ext.grid.LockingGridView.superclass.syncScroll.call(this, e);
        var mb = this.scroller.dom;
        this.lockedScroller.dom.scrollTop = mb.scrollTop;
    },

    processRows : function(startRow, skipStripe) {
        if (this.ds.getCount() < 1) {
            return;
        }
        skipStripe = skipStripe || !this.grid.stripeRows;
        startRow = startRow || 0;
        var cls = ' x-grid3-row-alt ';
        var rows = this.getRows();
        var lrows = this.getLockedRows();
        for (var i = startRow, len = rows.length; i < len; i++) {
            var row = rows[i];
            var lrow = lrows[i];
            row.rowIndex = i;
            lrow.rowIndex = i;
            if(this.fixedRows > i)
            {
            	row.className = 'x-grid3-header';
                lrow.className = 'x-grid3-header';
            }
            if (!skipStripe) {
                var isAlt = ((i+1) % 2 == 0);
                var hasAlt = (' '+row.className + ' ').indexOf(cls) != -1;
                if (isAlt == hasAlt) {
                    continue;
                }
                if (isAlt) {
                    row.className += " x-grid3-row-alt";
                    lrow.className += " x-grid3-row-alt";
                } else {
                    row.className = row.className.replace("x-grid3-row-alt", "");
                    lrow.className = lrow.className.replace("x-grid3-row-alt", "");
                }
            }
        }
    },

    updateSortIcon : function(col, dir) {
        var sc = this.sortClasses;
        var clen = this.cm.getColumnCount();
        var lclen = this.cm.getLockedCount();
        var hds = this.mainHd.select('td').removeClass(sc);
        var lhds = this.lockedHd.select('td').removeClass(sc);
        if (lclen > 0 && col < lclen) {
            lhds.item(col).addClass(sc[dir == "DESC" ? 1 : 0]);
        } else {
            hds.item(col-lclen).addClass(sc[dir == "DESC" ? 1 : 0]);
        }
    },
  

    updateAllColumnWidths : function() {
        var tw = this.cm.getTotalWidth();
        var lw = this.cm.getTotalLockedWidth();
        var clen = this.cm.getColumnCount();
        var lclen = this.cm.getLockedCount();
        var ws = [];
        for (var i = 0; i < clen; i++) {
            ws[i] = this.getColumnWidth(i);
        }

        this.innerHd.firstChild.firstChild.style.width = tw - lw;
        this.mainWrap.dom.style.left = lw;
        this.lockedInnerHd.firstChild.firstChild.style.width = lw;

        for (var i = 0; i < clen; i++) {
            var hd = this.getHeaderCell(i);
            hd.style.width = ws[i];
        }

        var ns = this.getRows();
        var lns = this.getLockedRows();
        for (var i = 0, len = ns.length; i < len; i++) {
            ns[i].style.width = tw - lw;
            ns[i].firstChild.style.width = tw-lw;
            lns[i].style.width = lw;
            lns[i].firstChild.style.width = lw;
            for (var j = 0; j < lclen; j++) {
                var row = lns[i].firstChild.rows[0];
                row.childNodes[j].style.width = ws[j];
            }
            for (var j = lclen; j < clen; j++) {
                var row = ns[i].firstChild.rows[0];
                row.childNodes[j].style.width = ws[j];
            }
        }
        this.onAllColumnWidthsUpdated(ws, tw);
    },

    updateColumnWidth : function(col, width) {
        var w = this.cm.getColumnWidth(col);
        var tw = this.cm.getTotalWidth();
        var lclen = this.cm.getLockedCount();
        var lw = this.cm.getTotalLockedWidth();

        var hd = this.getHeaderCell(col);
        hd.style.width = w;

        var ns, gw;
        if (col < lclen) {
            ns = this.getLockedRows();
            gw = lw;
            this.lockedInnerHd.firstChild.firstChild.style.width = gw;
            this.mainWrap.dom.style.left= this.cm.getTotalLockedWidth();
        } else {
            ns = this.getRows();
            gw = tw - lw;
            col -= lclen;
            this.innerHd.firstChild.firstChild.style.width = gw;
        }

        for (var i = 0, len = ns.length; i < len; i++) {
            ns[i].style.width = gw;
            ns[i].firstChild.style.width = gw;
            ns[i].firstChild.rows[0].childNodes[col].style.width = w;
        }

        this.onColumnWidthUpdated(col, w, tw);
        this.layout();
        this.changeHeight();
    },

    updateColumnHidden : function(col, hidden) {
        var tw = this.cm.getTotalWidth();
        var lw = this.cm.getTotalLockedWidth();
        var lclen = this.cm.getLockedCount();

        this.innerHd.firstChild.firstChild.style.width = tw;

        var display = hidden ? 'none' : '';

        var hd = this.getHeaderCell(col);
        hd.style.display = display;

        var ns, gw;
        if (col < lclen) {
            ns = this.getLockedRows();
            gw = lw;
            this.lockedHd.dom.firstChild.firstChild.style.width = gw;
            this.mainWrap.dom.style.left= this.cm.getTotalLockedWidth();
        } else {
            ns = this.getRows();
            gw = tw - lw;
            col -= lclen;
            this.innerHd.firstChild.firstChild.style.width = gw;
        }

        for (var i = 0, len = ns.length; i < len; i++) {
            ns[i].style.width = gw;
            ns[i].firstChild.style.width = gw;
            ns[i].firstChild.rows[0].childNodes[col].style.display = display;
        }

        this.onColumnHiddenUpdated(col, hidden, tw);

        delete this.lastViewWidth;         
        this.layout();
        this.changeHeight();
    },
    // private
    onColumnMove : function(cm, oldIndex, newIndex) {
        Ext.grid.LockingGridView.superclass.onColumnMove.call(this, cm, oldIndex, newIndex);
        this.changeHeight();
    },
    changeHeight : function() {
        if (this.lockedInnerHd == undefined || this.innerHd == undefined) return;

        this.lockedInnerHd.firstChild.firstChild.style.height = "auto";
        this.innerHd.firstChild.firstChild.style.height = "auto";
        var height = (this.lockedInnerHd.firstChild.firstChild.offsetHeight > this.innerHd.firstChild.firstChild.offsetHeight) ? 
                          this.lockedInnerHd.firstChild.firstChild.offsetHeight : this.innerHd.firstChild.firstChild.offsetHeight;
        this.lockedInnerHd.firstChild.firstChild.style.height = height;
        this.innerHd.firstChild.firstChild.style.height = height;

        if (this.lockedScroller == undefined || this.scroller == undefined) return;
        this.lockedScroller.dom.style.height = this.el.dom.offsetHeight - height;
        this.scroller.dom.style.height = this.el.dom.offsetHeight - height;
    },
    doRender : function(cs, rs, ds, startRow, colCount, stripe) {
        var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount-1;
        var tw = this.cm.getTotalWidth();
        var lw = this.cm.getTotalLockedWidth();
        var clen = this.cm.getColumnCount();
        var lclen = this.cm.getLockedCount();
        var tstyle = 'width:'+this.getTotalWidth()+';';
        var buf = [], lbuf = [], cb, lcb, c, p = {}, rp = {tstyle: tstyle}, r;
        
        //清0合计列
        if(true == this.cm.sumheader)
        {
        	this.clearSumZero();	
        }
        
        for (var j = 0, len = rs.length; j < len; j++) {
            r = rs[j]; cb = []; lcb = [];
            var rowIndex = (j+startRow);
            for (var i = 0; i < colCount; i++) {
                c = cs[i];
                p.id = c.id;
                p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
                p.attr = p.cellAttr = "";
                p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
                p.style = c.style;
                if (p.value == undefined || p.value === "") p.value = "&#160;";
                if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
                    p.css += ' x-grid3-dirty-cell';
                }
                if (c.locked) {
                    lcb[lcb.length] = ct.apply(p);
                } else {
                    cb[cb.length] = ct.apply(p);
                }
                
                //处理求和问题
                if(true == this.cm.sumheader)
        		{
	                if(true == this.cm.config[i].issum)
	                {
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
            if (stripe && ((rowIndex+1) % 2 == 0)) {
                alt[0] = "x-grid3-row-alt";
            }
            if (r.dirty) {
                alt[1] = " x-grid3-dirty-row";
            }
            rp.cols = colCount;
            if (this.getRowClass) {
                alt[2] = this.getRowClass(r, rowIndex, rp, ds);
            }
      
            rp.alt = alt.join(" ");
            rp.cells = lcb.join("");
            rp.tstyle = 'width:'+lw+';';
            lbuf[lbuf.length] =  rt.apply(rp);

            rp.cells = cb.join("");
            rp.tstyle = 'width:'+(tw-lw)+';';
            buf[buf.length] =  rt.apply(rp);
        }
        
        //如果有合计行，则需要重新渲染表头
        if(true == this.hasSumColumn)
        {
        	this.updateHeaders();
        }
        
        return [buf.join(""), lbuf.join("")];
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
    renderUI : function() {

        var header = this.renderHeaders();
        var body = this.templates.body.apply({rows:''});


        var html = this.templates.master.apply({
            body: body,
            header: header[0],
            lockedBody: body,
            lockedHeader: header[1]
        });

        var g = this.grid;
        g.getGridEl().dom.innerHTML = html;

        this.initElements();

        var bd = this.renderRows();
        if (bd == '') bd = ['', ''];
    
        this.mainBody.dom.innerHTML = bd[0];
        this.lockedBody.dom.innerHTML = bd[1];
        this.processRows(0, true);

        Ext.fly(this.innerHd).on("click", this.handleHdDown, this);
        Ext.fly(this.lockedInnerHd).on("click", this.handleHdDown, this);
        this.mainHd.on("mouseover", this.handleHdOver, this);
        this.mainHd.on("mouseout", this.handleHdOut, this);
        this.mainHd.on("mousemove", this.handleHdMove, this);
        this.lockedHd.on("mouseover", this.handleHdOver, this);
        this.lockedHd.on("mouseout", this.handleHdOut, this);
        this.lockedHd.on("mousemove", this.handleHdMove, this);
        this.mainWrap.dom.style.left= this.cm.getTotalLockedWidth();
        this.scroller.on('scroll', this.syncScroll,  this);
        if (g.enableColumnResize !== false) {
            this.splitone = new Ext.grid.GridView.SplitDragZone(g, this.lockedHd.dom);
            this.splitone.setOuterHandleElId(Ext.id(this.lockedHd.dom));
            this.splitone.setOuterHandleElId(Ext.id(this.mainHd.dom));
        }

        if (g.enableColumnMove) {
            this.columnDrag = new Ext.grid.GridView.ColumnDragZone(g, this.innerHd);
            this.columnDrop = new Ext.grid.HeaderDropZone(g, this.mainHd.dom);
        }

        if (g.enableHdMenu !== false) {
            if (g.enableColumnHide !== false) {
                this.colMenu = new Ext.menu.Menu({id:g.id + "-hcols-menu"});
                this.colMenu.on("beforeshow", this.beforeColMenuShow, this);
                this.colMenu.on("itemclick", this.handleHdMenuClick, this);
            }
            this.hmenu = new Ext.menu.Menu({id: g.id + "-hctx"});
            this.hmenu.add(
                {id:"asc", text: this.sortAscText, cls: "xg-hmenu-sort-asc"},
                {id:"desc", text: this.sortDescText, cls: "xg-hmenu-sort-desc"}
            );
            if (this.grid.enableColLock !== false) {
                this.hmenu.add('-',
                    {id:"lock", text: this.lockText, cls: "xg-hmenu-lock"},
                    {id:"unlock", text: this.unlockText, cls: "xg-hmenu-unlock"}
                );
            }
      
            if (g.enableColumnHide !== false) {
                this.hmenu.add('-',
                    {id:"columns", text: this.columnsText, menu: this.colMenu, iconCls: 'x-cols-icon'}
                );
            }
            this.hmenu.on("itemclick", this.handleHdMenuClick, this);
        }

        if (g.enableDragDrop || g.enableDrag) {
            var dd = new Ext.grid.GridDragZone(g, {
                ddGroup : g.ddGroup || 'GridDD'
            });
        }
        this.updateHeaderSortState();
    },

    layout : function() {
        if (!this.mainBody) {
            return;
        }
        var g = this.grid;
        var c = g.getGridEl(), cm = this.cm,
            expandCol = g.autoExpandColumn,
            gv = this;
        var lw = cm.getTotalLockedWidth();
        var csize = c.getSize(true);
        var vw = csize.width;

        if (vw < 20 || csize.height < 20) {
            return;
        }

        if (g.autoHeight) {
            this.scroller.dom.style.overflow = 'visible';
            this.lockedScroller.dom.style.overflow = 'visible';
        } else {
            this.el.setSize(csize.width, csize.height);

            var hdHeight = this.mainHd.getHeight();
            var vh = csize.height - (hdHeight);

            this.scroller.setSize(vw- lw, vh);
            var scrollbar = (this.scroller.dom.scrollWidth > this.scroller.dom.clientWidth)?17:0;
            this.lockedScroller.setSize(cm.getTotalLockedWidth(), vh-scrollbar);
            if (this.innerHd) {
                this.innerHd.style.width = (vw)+'px';
            }
        }
        if (this.forceFit) {
            if (this.lastViewWidth != vw) {
                this.fitColumns(false, false);
                this.lastViewWidth = vw;
            }
        } else {
            this.autoExpand();
        }
        this.mainWrap.dom.style.left = lw +'px';
    
        this.onLayout(vw, vh);
    },

    renderHeaders : function() {
        var cm = this.cm, ts = this.templates;
        var ct = ts.hcell;
        var tw = this.cm.getTotalWidth();
        var lw = this.cm.getTotalLockedWidth();

        var cb = [], lb = [], sb = [], lsb = [], p = {};

        for (var i = 0, len = cm.getColumnCount(); i < len; i++) {
            p.id = cm.getColumnId(i);
            p.value = cm.getColumnHeader(i) || "";
            p.style = this.getColumnStyle(i, true);
            p.tooltip = this.getColumnTooltip(i);
            
            if (cm.config[i].align == 'right') {
                p.istyle = 'padding-right:16px';
            }
            if (cm.isLocked(i)) {
                lb[lb.length] = ct.apply(p);
            } else {
                cb[cb.length] = ct.apply(p);
            }
        }
        
        //return [ts.header.apply({cells: cb.join(""), tstyle:'width:'+(tw-lw)+';'}),
        //ts.header.apply({cells: lb.join(""), tstyle:'width:'+(lw)+';'})];
        
        //处理合计行表头
        var sumheaders = null;
        if(true == cm.sumheader)
        {
        	sumheaders = this.buildSumHeaders(cm,ts,tw,lw);
        	sb = sumheaders[0];
        	lsb = sumheaders[1];
        }
        
        return [ts.header.apply({cells: cb.join(""), sumcells: sb.join(""), tstyle:'width:'+(tw-lw)+';'}),
        ts.header.apply({cells: lb.join(""), sumcells: lsb.join(""), tstyle:'width:'+(lw)+';'})];
        
    },
  	/**
     * 生成合计表头
     * @param {} cm
     * @param {} ts
     * @param {} tw
     * @param {} lw
     * @return {}array
     */
    buildSumHeaders:function(cm,ts,tw,lw){
        var ct = ts.hcell;

        var sb = [], lsb = [],sp = {};

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
            
            if (cm.isLocked(i)) {
                lsb[lsb.length] = ct.apply(sp);
            } else {
                sb[sb.length] = ct.apply(sp);
            }
        }
        return [sb,lsb];
    },
    // private
    getColumnTooltip : function(i) {
        var tt = this.cm.getColumnTooltip(i);
        if (tt) {
            if (Ext.QuickTips.isEnabled()) {
                return 'ext:qtip="'+tt+'"';
            } else {
                return 'title="'+tt+'"';
            }
        }
        return "";
    },

    updateHeaders : function() {
        var hd = this.renderHeaders();
        this.innerHd.firstChild.innerHTML = hd[0];
        this.lockedInnerHd.firstChild.innerHTML = hd[1];
    },
    // private
    insertRows : function(dm, firstRow, lastRow, isUpdate) {
        if (firstRow === 0 && lastRow == dm.getCount()-1) {
            this.refresh();
        } else {
            if (!isUpdate) {
                this.fireEvent("beforerowsinserted", this, firstRow, lastRow);
            }
            var html = this.renderRows(firstRow, lastRow);
            
            var before = this.getRow(firstRow);
            if (before) {
                Ext.DomHelper.insertHtml('beforeBegin', before, html[0]);
            } else {
                Ext.DomHelper.insertHtml('beforeEnd', this.mainBody.dom, html[0]);
            }
      
            var beforeLocked = this.getLockedRow(firstRow);
            if (beforeLocked) {
                Ext.DomHelper.insertHtml('beforeBegin', beforeLocked, html[1]);
            } else {
                Ext.DomHelper.insertHtml('beforeEnd', this.lockedBody.dom, html[1]);
            }
            if (!isUpdate) {
                this.fireEvent("rowsinserted", this, firstRow, lastRow);
                this.processRows(firstRow);
            }
        }
    },
    // private
    removeRow : function(row) {
        Ext.removeNode(this.getRow(row));
        if (this.cm.getLockedCount() > 0) {
            Ext.removeNode(this.getLockedRow(row));
        }
    },

    getColumnData : function() {
        var cs = [], cm = this.cm, colCount = cm.getColumnCount();
        for (var i = 0; i < colCount; i++) {
            var name = cm.getDataIndex(i);
            cs[i] = {
                name : (typeof name == 'undefined' ? this.ds.fields.get(i).name : name),
                renderer : cm.getRenderer(i),
                id : cm.getColumnId(i),
                style : this.getColumnStyle(i),
                locked : cm.isLocked(i)
            };
        }
        return cs;
    },

    renderBody : function() {
        var markup = this.renderRows();
        return [this.templates.body.apply({rows: markup[0]}), this.templates.body.apply({rows: markup[1]})];
    },

    refresh : function(headersToo) {
        this.fireEvent("beforerefresh", this);
        this.grid.stopEditing();

        var result = this.renderBody();
        this.mainBody.update(result[0]);
        this.lockedBody.update(result[1]);

        if (headersToo === true) {
            this.updateHeaders();
            this.updateHeaderSortState();
        }
        this.processRows(0, true);
        this.layout();
        this.applyEmptyText();
        this.fireEvent("refresh", this);
    },

    handleLockChange : function() {
        this.refresh(true);
    },

    onDenyColumnHide : function() {

    },

    onColumnLock : function() {
        this.handleLockChange.apply(this, arguments);
    },

    addRowClass : function(row, cls) {
        var r = this.getRow(row);
        if (r) {
            this.fly(r).addClass(cls);
            r = this.getLockedRow(row);
            this.fly(r).addClass(cls);
        }
    },

    removeRowClass : function(row, cls) {
        var r = this.getRow(row);
        if (r) {
            this.fly(r).removeClass(cls);
            r = this.getLockedRow(row);
            this.fly(r).removeClass(cls);
        }
    },
  
    handleHdMenuClick : function(item) {
        var index = this.hdCtxIndex;
        var cm = this.cm, ds = this.ds;
        switch (item.id) {
            case "asc":
                ds.sort(cm.getDataIndex(index), "ASC");
                break;
            case "desc":
                ds.sort(cm.getDataIndex(index), "DESC");
                break;
            case "lock":
                var lc = cm.getLockedCount();
                if (cm.getColumnCount(true) <= lc+1) {
                    this.onDenyColumnLock();
                    return;
                }
                if (lc != index) {
                    cm.setLocked(index, true, true);
                    cm.moveColumn(index, lc);
                    this.grid.fireEvent("columnmove", index, lc);
                } else {
                    cm.setLocked(index, true);
                }
                this.changeHeight();
                break;
            case "unlock":
                var lc = cm.getLockedCount();
                if ((lc-1) != index) {
                    cm.setLocked(index, false, true);
                    cm.moveColumn(index, lc-1);
                    this.grid.fireEvent("columnmove", index, lc-1);
                } else {
                    cm.setLocked(index, false);
                }
                this.changeHeight();
                break;
            default:
                index = cm.getIndexById(item.id.substr(4));
                if (index != -1) {
                    if (item.checked && cm.getColumnsBy(this.isHideableColumn, this).length <= 1) {
                        this.onDenyColumnHide();
                        return false;
                    }
                    cm.setHidden(index, item.checked);
                }
        }
        return true;
    },

    handleHdDown : function(e, t) {
        if (Ext.fly(t).hasClass('x-grid3-hd-btn')) {
            e.stopEvent();
            var hd = this.findHeaderCell(t);
            Ext.fly(hd).addClass('x-grid3-hd-menu-open');
            var index = this.getCellIndex(hd);
            this.hdCtxIndex = index;
            var ms = this.hmenu.items, cm = this.cm;
            ms.get("asc").setDisabled(!cm.isSortable(index));
            ms.get("desc").setDisabled(!cm.isSortable(index));
            if (this.grid.enableColLock !== false) {
                ms.get("lock").setDisabled(cm.isLocked(index));
                ms.get("unlock").setDisabled(!cm.isLocked(index));
            }
            this.hmenu.on("hide", function() {
                Ext.fly(hd).removeClass('x-grid3-hd-menu-open');
            }, this, {single:true});
            this.hmenu.show(t, "tl-bl?");
        }
    }
});

Ext.grid.LockingColumnModel = function(config) {
	
    Ext.grid.LockingColumnModel.superclass.constructor.call(this, config);
    //alert('config.groupedHeaders ' + config.groupedHeaders);
	//this.groupedHeaders = config.groupedHeaders;
	
};

Ext.extend(Ext.grid.LockingColumnModel, Ext.grid.ColumnModel, {
    getTotalLockedWidth : function() {
        var totalWidth = 0;
        for (var i = 0; i < this.config.length; i++) {
            if (this.isLocked(i) && !this.isHidden(i)) {
                totalWidth += this.getColumnWidth(i);
            }
        }
        return totalWidth;
    },
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