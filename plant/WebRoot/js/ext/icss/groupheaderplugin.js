/**
 *  Based on the code in this thread: http://extjs.com/forum/showthread.php?t=22337
 *  
 */
 
Array.prototype.contains = function(element) {
	return this.indexOf(element) !== -1;
}; 
 
Ext.namespace("Ext.ux.plugins");

Ext.ux.plugins.GroupHeaderGrid = function(config) {
	Ext.apply(this, config);
};
Ext.extend(Ext.ux.plugins.GroupHeaderGrid, Ext.util.Observable, {
	// PLUGIN INIT
	init: function(grid){
		this.grid = grid;
		this.initTemplates();
		this.view.renderHeaders.createInterceptor(this.renderHeaders(),this);
	},
	initTemplates: function(){
		this.view = this.grid.getView();
		this.view.initTemplates.call(this);
		//Ext.grid.GroupingView.superclass.initTemplates.call(this);  //what about non groupingView?
		if(!this.templates.groupedHeader){
		    this.templates.groupedHeader = new Ext.Template(
	             '<table class="groupedHeader" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
	             '<thead>{grandParentRow}</thead>',
	             '</table>',
	             '<table class="groupedHeader" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
	             '<thead>{parentRow}</thead>',
	             '</table>',
	             '<table class="groupedHeader" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
	             '<thead><tr class="x-grid3-hd-row">{cells}</tr></thead>',
	             '</table>'
        	);
    	}
		if (!this.templates.groupRow){
			this.templates.groupRow = new Ext.Template(
				'<tr class="x-grid3-hd-row">{groupCells}</tr>'
			);
		}     
	    if(!this.templates.groupCell){
		    this.templates.groupCell = new Ext.Template(
				 '<td style="{style}" id="{grId}" class="{cellClass}"><div class="x-grid3-hd-inner" unselectable="on" style="text-align:{align}">{header}</div></td>'
	        );
	    }
	},
	renderHeaders : function(){
		this.view = this.grid.getView();
		this.view.renderHeaders = this.renderGroupedHeaders.createDelegate(this);
			
		this.view.getHeaderCell = this.getHeaderCell.createDelegate(this);
		this.view.updateSortIcon = this.updateSortIcon.createDelegate(this);
		this.view.getColumnWidth = this.getColumnWidth.createDelegate(this);
		this.view.getColumnStyle = this.getColumnStyle.createDelegate(this);
		this.view.getTotalWidth = this.getTotalWidth.createDelegate(this);
		this.view.updateColumnWidth = this.updateColumnWidth.createDelegate(this);
		this.view.updateColumnHidden=this.updateColumnHidden.createDelegate(this);
			
		this.view.hideGroup = this.hideGroup.createDelegate(this);
		
		this.buildRows();
		this.linkColumnsToHeaders();
		this.view.renderHeaders();
		return false;
	},
	updateHeaders : function(){
        this.innerHd.firstChild.innerHTML = this.renderHeaders();
    },
	renderGroupedHeaders : function() {
		var cm = this.grid.getColumnModel();
		
		
		if(!cm.columnsByHeader){
			this.buildRows();
        	this.linkColumnsToHeaders();
        }
		var rows = cm.rows;
	    var groups;
		
        var cellTemplate = this.templates.hcell;

        var cellMarkup = [], sb = [], cellParams = {};
        var groupCellMarkup = [];
		var rowsMarkup = [];
		
		for (var i = 0; i < rows.length; i++) {//create markup for rows
			groups = rows[i];
			for (var j = 0; j < groups.length; j++) {//create markup for group cells
				if(groups[j]){
					//groups[j].id = this.grid.id+'-'+i+'-'+j;  //make the id unique across multiple grids
					groupCellMarkup[groupCellMarkup.length] = this.renderGroupCell(groups[j]);
				}
			}
			rowsMarkup[rowsMarkup.length] = this.renderGroupRows(groupCellMarkup.join(""));
			var groupCellMarkup = [];
		}
        for(var i = 0, len = cm.getColumnCount(); i < len; i++){ // create markup for leaf cells
        	cellMarkup[cellMarkup.length] = this.renderHeaderCell(cm, i);
        }
        // use a different template
        return this.templates.groupedHeader.apply({grandParentRow: rowsMarkup[0], parentRow: rowsMarkup[1], cells: cellMarkup.join(""), tstyle:'width:'+this.view.getTotalWidth()+';'});
	},
	renderGroupCell : function(group) {
		var template = this.templates.groupCell;
		var cellClass;
		var rowspan;
		if(group.colspan == 0 || group.isHidden){
			group.style += 'display:none;';
		} else {
			if(!group.width){
				var w = this.getGroupCellWidth(group.id);  // get total width of the cells under this group
				if(w > 0){
					group.style = 'width:'+w+'px';
				} else {
					group.style = 'display:none;';  // starts hidden
					group.isHidden = true;
				}
			} else {
				group.style = 'width:'+group.width+'px';
			}
		}
		
		cellClass = "ux-grid-hd-group-cell";
		if(group.rowspan > 1){
			cellClass += " ux-grid-rowspanner-first";  // class to remove the horizontal border in rowspanned cells
		}
    	return template.apply({
			header: group.header,
			colspan: group.colspan,
			align: group.align,
			rowspan: group.rowspan,
			style: group.style,
			cellClass: cellClass,
			grId: group.id
		});
	},
	renderGroupRows : function(groupCellMarkup) {
		var template = this.templates.groupRow;
		return template.apply({groupCells: groupCellMarkup});
	},
	renderHeaderCell : function(cm,index){
		var template = this.templates.hcell;
		var params = {};
		params.id = cm.getColumnId(index);
	    params.value = cm.getColumnHeader(index) || "&nbsp;";
	    params.style = this.view.getColumnStyle(index, true);
	    params.tooltip = this.getColumnTooltip(index);
	    if(cm.config[index].align == 'right'){
	    	params.istyle = 'padding-right:16px';
	    }
	    return template.apply(params);
	},
	getColumnTooltip : function(i){
        var tt = this.grid.getColumnModel().getColumnTooltip(i);
        if(tt){
            if(Ext.QuickTips.isEnabled()){
                return 'ext:qtip="'+tt+'"';
            }else{
                return 'title="'+tt+'"';
            }
        }
        return "";
    },
	// from gridview, with minor fixes
	getHeaderCell : function(index){
	    var hds = this.view.mainHd.select('.x-grid3-cell');
	    return (hds.item(index).dom);
	},
	updateSortIcon : function(col, dir){
	    var sc = this.view.sortClasses;
	    var hds = this.view.mainHd.select('.x-grid3-cell').removeClass(sc);
	    hds.item(col).addClass(sc[dir == "DESC" ? 1 : 0]);
	},
	getColumnWidth : function(col){
		var cm = this.grid.getColumnModel();
        var w = cm.getColumnWidth(col);
        if(typeof w == 'number'){
            return (Ext.isBorderBox ? w : (w-this.view.borderWidth > 0 ? w-this.view.borderWidth:0)) + 'px';
        }
        return w;
    },
    getRawColumnWidth: function(col){
    	var cm = this.grid.getColumnModel();
    	var w = cm.getColumnWidth(col);
    	return w;
    },
	getColumnStyle : function(col, isHeader){
		var cm = this.grid.getColumnModel();
        var style = !isHeader ? (cm.config[col].css || '') : '';
        style += 'width:'+this.view.getColumnWidth(col)+';';
        if(cm.isHidden(col)){
            style += 'display:none;';
        }
        var align = cm.config[col].align;
        if(align){
            style += 'text-align:'+align+';';
        }
        return style;
    },
	getTotalWidth : function(){
        return this.grid.getColumnModel().getTotalWidth()+'px';
    },
    getGroupCellWidth: function(grp){
    	// loop through the grouped header and get it's constituent cells
    	var cm = this.grid.getColumnModel();    	
    	var c = cm.columnsByHeader[grp];
    	
    	var width = 0;
    	var unhiddenCount = 0;
    	for(var f=0; f<c.length; f++){
    		var di = c[f];
    		for(var i = 0; i < cm.config.length; i++){
				if(cm.config[i].dataIndex == di){
					var colID = cm.getColumnId(i);
					if(!cm.isHidden(colID)){
						unhiddenCount++;
						var w = this.getRawColumnWidth(colID);
						width += w;
					}
					break;
				}
    		}
		}
		var cw = (Ext.isBorderBox ? width : (width-this.view.borderWidth > 0 ? width-this.view.borderWidth:0));
    	return cw;
    },
    hideGroup: function(grpId){  // not yet implemented
    	var cm = this.grid.getColumnModel();
    	// run hide column as normal for each but skip the grouped header refresh for speed
    	for(var i = 0; i < cm.config.length; i++){
			if(item.cols.contains(cm.config[i].dataIndex)){
				cm.setHidden(cm.getColumnId(i), item.checked);
			}
		}
    	// now udpate the header, we know it's a show or hide operation
		var h = Ext.get(item.domId);
		if(item.checked){
			h.dom.style.display = '';
		} else {
			h.dom.style.display = 'none';
		}
    },
    updateHiddenGroup: function(grpID, hidden){
    	// from a group ext-id, find and set the isHidden flag in the cm
    	var grps = this.grid.getColumnModel().groupedHeaders;
    	for(var x in grps){
    		if(grps[x].id == grpID){
    			grps[x].isHidden = hidden;
    			if(grps[x].subHeadings && hidden === false){
    				for(y in grps[x].subHeadings){
	    				if(grps[x].subHeadings[y].id){
    						grps[x].subHeadings[y].isHidden = hidden;
    					}
    				}
    			}
    		}
    	}
    },
    updateGroupedHeaders: function(c){
    	// update all the grouped headers for a column
    	var cm = this.grid.getColumnModel();
    	var col = cm.getDataIndex(c);
    	var headers = cm.headersByColumn[col];
    	
    	if(headers){
    		for(var x=0; x<headers.length; x++){
	    		var newWidth = this.getGroupCellWidth(headers[x]);
    			var h = Ext.get(headers[x]);
    			if(newWidth > 0){
    				h.dom.style.display = '';
    				h.dom.style.width = newWidth+'px';
    				this.updateHiddenGroup(headers[x], false);
    			} else {
	    			h.dom.style.display = 'none';
	    			this.updateHiddenGroup(headers[x], true);
    			}
    		}
    	}
    	
    	// force a scroll (twice for IE), to make sure header is redisplayed correctly       
        var mb = this.view.scroller.dom;
        this.view.innerHd.scrollLeft = mb.scrollLeft;
        this.view.innerHd.scrollLeft = mb.scrollLeft;
    	return;
    },
    updateColumnWidth : function(col, width){
        var w = this.getColumnWidth(col);
        var tw = this.getTotalWidth();

        var gh = this.view.innerHd.firstChild.childNodes;
        for(x in gh){
        	if(gh[x].style){
       			gh[x].style.width = tw;
        	}
        }
        //this.view.innerHd.firstChild.firstChild.style.width = tw;
        // more than one row so can't use firstChild
        var hd = this.getHeaderCell(col);
        hd.style.width = w;
        
        var ns = this.view.getRows();
        for(var i = 0, len = ns.length; i < len; i++){
            ns[i].style.width = tw;
            ns[i].firstChild.style.width = tw;
            ns[i].firstChild.rows[0].childNodes[col].style.width = w;
        }
        
        this.view.onColumnWidthUpdated(col, w, tw);
        this.updateGroupedHeaders(col);
    },
	
	updateColumnHidden : function(col, hidden, skipGroupedHeaders){
        var tw = this.getTotalWidth();
        //this.view.innerHd.firstChild.firstChild.style.width = tw;
        // more than one row so can't use firstChild
        var gh = this.view.innerHd.firstChild.childNodes;
        for(x in gh){
        	if(gh[x].style){
       			gh[x].style.width = tw;
        	}
        }

        var display = hidden ? 'none' : '';
		
		
        var hd = this.getHeaderCell(col);
        hd.style.display = display;

        var ns = this.view.getRows();
        for(var i = 0, len = ns.length; i < len; i++){
            ns[i].style.width = tw;
            ns[i].firstChild.style.width = tw;
            ns[i].firstChild.rows[0].childNodes[col].style.display = display;
        }
        
        this.view.onColumnHiddenUpdated(col, hidden, tw);
        if(skipGroupedHeaders !== true){
			this.updateGroupedHeaders(col);
        }
		
        delete this.view.lastViewWidth; // force recalc
        this.view.layout();
    },
    buildRows: function(){
		// convert the received groupedHeaders data into rows
    	var cm = this.grid.getColumnModel();
		var rows = [];
		rows[0] = [];
		rows[1] = [];
		var baseId = Ext.id()+'-';//this.grid.id;
		var cnt=0;
		var groupedHeaders = cm.groupedHeaders;
		for(var x=0; x<groupedHeaders.length; x++){				
			
			groupedHeaders[x].isHidden = (groupedHeaders[x].colspan == 0) ? true : false;
			
			if(groupedHeaders[x].rowspan > 1){
				groupedHeaders[x].id = baseId+cnt;
				cnt++;
				var rs = {header:"", 
						  id: baseId+cnt,
						  columns: groupedHeaders[x].columns,
						  colspan: groupedHeaders[x].colspan,
						  width: groupedHeaders[x].width};
				rows[0].push(groupedHeaders[x]);
				rows[1].push(rs);
				cnt++;
			}
			// this should be an either/or.  can't have subheadings and be rowspanned.
			if(groupedHeaders[x].subHeadings && groupedHeaders[x].subHeadings.length>0){
				for(var s=0; s<groupedHeaders[x].subHeadings.length; s++){
					groupedHeaders[x].subHeadings[s].id = baseId+cnt;
					cnt++;
					rows[1].push(groupedHeaders[x].subHeadings[s]);
					groupedHeaders[x].subHeadings[s].isHidden = (groupedHeaders[x].subHeadings[s].colspan == 0) ? true : false;
				}
				groupedHeaders[x].id = baseId+cnt;
				cnt++;
				rows[0].push(groupedHeaders[x]);
			}
		}
		//return rows;
		cm.rows = rows;
	},
	linkColumnsToHeaders: function(){
		// create two arrays of data: one showing id's of headers by column id, one of columns by header id
		// these are then used to work out which columns a header gets it's width from and used in show/hide functions
		var cm = this.grid.getColumnModel();
		var rows = cm.rows;
		var colHeaders = [];
		for(var x=0; x<rows.length; x++){
			for (var y=0; y<rows[x].length; y++){
				if(rows[x][y].columns && rows[x][y].columns.length > 0){
					if(!colHeaders[rows[x][y].id]){
						colHeaders[rows[x][y].id] = [];
					}
					for(var z=0; z<rows[x][y].columns.length; z++){
						colHeaders[rows[x][y].id].push(rows[x][y].columns[z]);
					}
				}
				if(rows[x][y].subHeadings && rows[x][y].subHeadings.length > 0){
					for(var s=0; s<rows[x][y].subHeadings.length; s++){
						if(rows[x][y].subHeadings[s].columns && rows[x][y].subHeadings[s].columns.length > 0){
							if(!colHeaders[rows[x][y].id]){
								colHeaders[rows[x][y].id] = [];
							}
							for(var z=0; z<rows[x][y].subHeadings[s].columns.length; z++){
								colHeaders[rows[x][y].id].push(rows[x][y].subHeadings[s].columns[z]);
							}
						}
					}
				}
			}
		}
		
		cm.columnsByHeader = colHeaders;
		
		var headerCols = [];
		for(x in colHeaders){
			if(typeof colHeaders[x] != 'function'){
				for(var y=0; y<colHeaders[x].length; y++){
					if(!headerCols[colHeaders[x][y]]){
						headerCols[colHeaders[x][y]] = [];
					}
					headerCols[colHeaders[x][y]].push(x);
				}
			}
		}
		cm.headersByColumn = headerCols;
		return;
	}
});