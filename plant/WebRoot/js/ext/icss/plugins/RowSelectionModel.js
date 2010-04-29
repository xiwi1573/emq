/*!
 * icss zhaoqy 
 *  2009-07-21 v0.1 
 *  分批动态渲染时，如果需要使用全选按钮，以及行选择事件等那么请使用此类。
 *  已经解决的bug：
 *	1、选择时，无法准确的获取所选行的record对象的bug
          2、在一屏内选择，然后拖动滚动条后下一屏显示的内容也会被选中的bug。比如选择第3、4行时，滚屏的时候其它行滚动到3、4行的位置时会变成选中状态。
	3、在一屏内第一条数据选不中的bug
	4、全选后拖动滚动条出现空白屏的bug
	5、反复拖动滚动条时，无法记录已选中行的bug
	6、不能取消选中行的bug
 *  2009-07-21 v0.2
 * 已经解决的bug：
	1、解决CheckboxSelectionModel.ocked = true时，无法选中任意一行的bug
	2、解决拖动滚动条后，无法使用shift+左键做多选的bug
	3、解决拖动滚动条后，ctrl+左键多选，放开ctrl后选择其他行，先前选中的行还会被高亮的bug
	4、解决全选然后在拖动滚动条后，全选框对勾消失的bug
	5、解决在一屏内选择，然后拖动滚动条后下一屏显示的内容也会被选中的bug。比如选择第3、4行时，滚屏的时候其它行滚动到3、4行的位置时会变成选中状态。
	6、解决ctrl+左键不能取消选中行的bug
 * 
 */
Ext.namespace('Ext.ux.grid.icss');

Ext.ux.grid.icss.RowSelectionModel = Ext.extend(Ext.grid.RowSelectionModel, {
	
	//分批渲染的时候是否刷新
	isRefresh : true,
	handleMouseDown : function(g, rowIndex, e){
		if(e.button !== 0){
            return;
        };
        var view = this.grid.getView();
		var vr = view.getVisibleRows();
		var index = rowIndex+vr.first;
        if(e.shiftKey && this.last !== false){
			this.isRefresh = false;
            var last = this.last;
            this.selectRange(last, index, e.ctrlKey,(last-vr.first),rowIndex);
            this.last = last; 
			view.focusRow(rowIndex);
        }else{
			this.isRefresh = true;
            var isSelected = this.isSelected(index);
            if(e.ctrlKey && isSelected){
                this.deselectRow(index,false,rowIndex);
            }else if(!isSelected || this.getCount() > 1){
                this.selectRow(index, e.ctrlKey || e.shiftKey,false,rowIndex);
                view.focusRow(rowIndex);
            }
        }
    },
	
	selectRow : function(index, keepExisting, preventViewNotify,ri){
		if((index < 0 || index >= this.grid.store.getCount())) return;
        var r = this.grid.store.getAt(index);
        if(r && this.fireEvent("beforerowselect", this, index, keepExisting, r) !== false){
            if(!keepExisting || this.singleSelect){
				if(this.grid.store.getCount()==this.getSelections().length){
					this.clearSelections(true);
				}else{
					this.clearSelections();
				}
				var checkers = Ext.query(".x-grid3-hd-inner");
				if(Global.isSelectAll&&checkers.length>0){
					for(var i=0,len =checkers.length ;i<len;i++){
						var hd = new Ext.Element(checkers[i]);
						var isChecker = hd.hasClass('x-grid3-hd-checker');
						if(isChecker){
							hd.removeClass('x-grid3-hd-checker-on');
							Global.isSelectAll = false;
							break;
						}
					}
				}
            }
            this.selections.add(r);
            this.last = this.lastActive = index;
            if(!preventViewNotify){
				if(ri>=0){
					this.grid.getView().onRowSelect(ri);
				}else{
					this.grid.getView().onRowSelect(index);
				}
            }
            this.fireEvent("rowselect", this, index, r);
            this.fireEvent("selectionchange", this);
        }
    },
	
	deselectRow : function(index, preventViewNotify,ri){
        if(this.last == index){
            this.last = false;
        }
        if(this.lastActive == index){
            this.lastActive = false;
        }
        var r = this.grid.store.getAt(index);
        if(r){
            this.selections.remove(r);
            if(!preventViewNotify){
				if(ri>=0){
					this.grid.getView().onRowDeselect(ri);
				}else{
					this.grid.getView().onRowDeselect(index);
				}
            }
            this.fireEvent("rowdeselect", this, index, r);
            this.fireEvent("selectionchange", this);
        }
    },
	
	selectRange : function(startRow, endRow, keepExisting,sindex,eindex){
        if(!keepExisting){
            this.clearSelections();
        }
        if(startRow <= endRow){
            for(var i = startRow,k=sindex; i <= endRow; i++){
                this.selectRow(i, true,false,k);
				k++;
            }
        }else{
            for(var i = startRow,k=sindex; i >= endRow; i--){
                this.selectRow(i, true,false,k);
				k--;
            }
        }
    },
	
	clearSelections : function(fast){
		var ds = this.grid.store;
		var view = this.grid.getView();
		var vr = view.getVisibleRows();
        if(fast !== true){
            var s = this.selections;
            s.each(function(r){
				var index = ds.indexOfId(r.id);
                this.deselectRow(index,false,index-vr.first);
            }, this);
            s.clear();
        }else{
			var rs = ds.getRange(vr.first, vr.last);
			for(var i = 0 ,len = rs.length; i < len; i++){
				var r = rs[i];
				var index = ds.indexOfId(r.id);
				this.deselectRow(index,false,index-vr.first);
			}
            this.selections.clear();
        }
        this.last = false;
    },
	
	selectAll : function(){
        this.selections.clear();
        for(var i = 0, len = this.grid.store.getCount(); i < len; i++){
            this.selectRow(i, true);
        }
    },
	
	onRefresh : function(){
        var ds = this.grid.store, index;
        var s = this.getSelections();
		var view = this.grid.getView();
		var vr = view.getVisibleRows();
		var endRow = Math.min(vr.last,ds.getCount()-1);
		var rs = ds.getRange(vr.first, endRow);
		for(var i = 0 ,len = rs.length; i < len; i++){
			if(s.length==0){
				break;
			}
			var r = rs[i];
			var isSelect = this.isIdSelected(r.id);
			if(isSelect){
				index = ds.indexOfId(r.id);
				var ri = index - vr.first;
				this.selectRow(index, true,false,ri);
			}
        }
        if(s.length != this.selections.getCount()){
            this.fireEvent("selectionchange", this);
        }
    }
});