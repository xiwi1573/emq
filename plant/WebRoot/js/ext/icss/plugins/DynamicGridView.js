/*!
 * 基于Ext2.1实现分批动态渲染(dynamic)，以及与锁列，行合计，全选等功能相结合
 *   icss/zhaoqy
 */
/**
 * @class Ext.ux.grid.icss.DynamicGridView
 * @extends Ext.grid.GridView
 *  2009-07-19  v0.1 
 *	1、分批动态渲染数据，经过测试，2000行，40列，锁定5列，每屏显示10行，纯渲染速度在3秒以下。
	2、支持列锁定功能；锁列、解锁列不再出现浏览器假死现象。
	3、支持行合计功能(计算所有的行，而不是渲染的列)。
	4、支持任意字段排序功能(所有行排序)
	5、支持按照要求设置某行为警戒色(比如红色)的功能。
	6、支持随意订制表格中的任意单元格的格式的功能。
	7、支持动态扩展列。
	8、支持行的选择事件。
	修复的已知bug：
	1、解决IE下最后几行丢失不显示的bug.
	2、解决IE下横向滚动条位置不正确的bug.
	3、解决IE下横向滚动条拖动时表头不动的bug.
	4、解决IE下加了checkBox后，锁定列和不锁定列对不齐的bug.
	5、解决了一个IE下如果没有锁定列，右侧会出现两个滚动条的bug.
 *  2009-07-20  v0.2：
	1、添加支持CheckboxSelectionModel多选的功能 。另外实现了分批渲染时的行选择模式：Ext.ux.grid.icss.RowSelectionModel
	2、解决了IE8(7)下偶尔出现的：因为计算显示行数不准确,导致大片空白行的bug
 *  2009-07-22  v0.3：
	1、支持鼠标放在Grid非锁定区域的滑轮滚动渲染事件。而且可以自定义滑轮滚动一次渲染的条数。
	2、解决了当窗口拉高，表格变高时，表格底部产生空白的bug。
	3、解决在有主细表的情况下,对主表进行多选,滚动的时候会不停查询细表数据，导致滚动很慢或甚至查死的bug。
	4、提升了全选或者不全选时，拖动滚动条渲染时的效率。
	5、增加一个属性refreshHeader，表示在拖动滚动条以及滚动鼠标滑轮时，是否重新渲染表头默认为fasle。
	      如果以后有更改表格数据，表头的合计数会即时变化的需求，那么只需要在Grid中增加一个配置即可：
		viewConfig : {
			refreshHeader:true
		},
 *  2009-07-23  v0.4：
	1、提升了拖动滚动条或者滚动滑轮时的渲染效率。IE8下测试，1000条数据，40列，锁定5列，拖动(滑轮滚动)滚动条渲染的时间在毫秒级。
	2、在有主细表的页面，使用sm.isRefresh来控制是否刷新细表。提升了主细表页面全选时拖动滚动条或者滚动滑轮时的渲染效率。
	      比如PreplanEndAuditMain.js中,在获取明细的代码之前加一个判断，可以防止全选状态下拖动滚动条时不停刷新细表的情况：
		//安装选择行时的事件处理
		var sm = preplanGrid.getSelectionModel(); 
		sm.on('rowselect',function(sm , row, rec){
	  		//取明细
			if(sm.isRefresh){//加一个判断
				getPreplanDetail(rec);
			}
		});
	3、修正了全选时连续拖动滚动条后，再选择某行无法取消其他选择行的bug
	4、修正了重新查询后，滚动条还是停留在重新查询之前的位置的bug
 *  2009-07-23  v0.5：
	1、修正了大数据量(2000条以上)全选拖动时，IE报出的“是否停止运行脚本”的bug。
	2、提升大数据量(2000条以上)全选后，点击某行取消其他行选定状态的效率。
 *  2009-07-25  v0.6：
	1、去掉了不必要的属性。对Ext.ux.grid.icss.CheckboxSelectionModel的实现做了调整。
	2、将DynamicGridView与Lockinggrid分离，方便项目组自己决定哪些Grid需要使用动态渲染组件。
	3、将BufferView改名为DynamicGridView，更加符合实现的原理。增加了必要的注释。
	4、修正了数据在一屏内显示时，横向滚动条长度不正确的bug。
 */
 
Ext.namespace('Ext.ux.grid.icss');

Ext.ux.grid.icss.DynamicGridView = function(config) {
    Ext.apply(this, config);
    this.templates = {};
    this.templates.master = new Ext.Template(
        '<div class="x-grid3" hidefocus="true"><div class="liveScroller"><div></div></div>',
            '<div class="x-grid3-viewport"">',
                '<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset">{header}</div></div><div class="x-clear"></div></div>',
                '<div class="x-grid3-scroller" style="overflow-y:hidden !important;"><div class="x-grid3-body">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
            "</div>",
            '<div class="x-grid3-resize-marker">&#160;</div>',
            '<div class="x-grid3-resize-proxy">&#160;</div>',
        "</div>"
    );
    this._gridViewSuperclass = Ext.ux.grid.icss.DynamicGridView.superclass;
    this._gridViewSuperclass.constructor.call(this);
};
Ext.extend(Ext.ux.grid.icss.DynamicGridView,Ext.grid.GridView, {
	/**
	 * @cfg {Number} rowHeight
	 * 每行的高度，默认是19，这个是默认高度，不要修改。
	 */
	rowHeight:19,
	borderHeight:2,
	scrollDelay:50,
	liveScroller:null,
    liveScrollerInset:null,
	horizontalScrollOffset:17,//IE7、8下值固定，不要修改
    hdHeight:0,
    rowClipped:0,
	lastRowIndex:0,
    visibleRows:1,
	scrollOnceAmount:3,//鼠标滑轮每次滚动时渲染的条数
	refreshHeader:false,//是否刷新表头，在不需要动态更新表头数据(比如合计)的情况下，选择false可以提高渲染效率
	renderHeader:false,
	
	initElements : function(){
        var E = Ext.Element;
        var el = this.grid.getGridEl().dom.firstChild;
	    var cs = el.childNodes;
	    this.el = new E(el);
        this.mainWrap = new E(cs[1]);

        // liveScroller and liveScrollerInset
        this.liveScroller = new E(cs[0]);
        this.liveScrollerInset = this.liveScroller.dom.firstChild;
        this.liveScroller.on('scroll', this.onLiveScroll,this);

        var thd = this.mainWrap.dom.firstChild;
	    this.mainHd = new E(thd);
	    this.hdHeight = thd.offsetHeight;
	    this.innerHd = this.mainHd.dom.firstChild;
        this.scroller = new E(this.mainWrap.dom.childNodes[1]);
        if(this.forceFit){
            this.scroller.setStyle('overflow-x', 'hidden');
        }
        this.mainBody = new E(this.scroller.dom.firstChild);
        this.mainBody.on('mousewheel', this.syncMousewheel,this);
	    this.focusEl = new E(this.scroller.dom.childNodes[1]);
        this.focusEl.swallowEvent("click", true);

        this.resizeMarker = new E(cs[2]);
        this.resizeProxy = new E(cs[3]);
    },

	getStyleRowHeight : function(){
		return Ext.isBorderBox ? (this.rowHeight + this.borderHeight) : this.rowHeight;
	},

	getCalculatedRowHeight : function(){
		return this.rowHeight + (Ext.isBorderBox ?this.borderHeight : 0);
	},

	getVisibleRowCount : function(){
		var rh = this.getCalculatedRowHeight();
		var visibleHeight = this.scroller.dom.clientHeight;
		return (visibleHeight < 1) ? 0 : Math.floor(visibleHeight / rh);
	},

	getVisibleRows: function(){
		var scrollTop = this.liveScroller.dom.scrollTop;
		var count = this.getVisibleRowCount();
		var start = (scrollTop == 0 ? 0 : Math.floor(scrollTop/this.getCalculatedRowHeight())-1);
		return {
			first: Math.max(start, 0),
			last: Math.min(start + count, this.ds.getCount()-1)
		};
	},
	
	// private
	refreshRow : function(record){
        var ds = this.ds, index;
		var vr = this.getVisibleRows();
        if(typeof record == 'number'){
            index = record;
            record = ds.getAt(index);
        }else{
            index = ds.indexOf(record);
        }
		//如果不在当前屏内刷新没意义，拖动的时候自然会刷新
		if(index>=vr.first&&index<=vr.last){
			var cls = [];
			this.insertRows(ds, index, index, true);
			var  currentIndex = index - vr.first;
			this.getRow(currentIndex).rowIndex = index;
			this.onRemove(ds, record, currentIndex+1, true);
			this.fireEvent("rowupdated", this, currentIndex, record);
		}
    },
	
	isRowRendered: function(index){
		var row = this.getRow(index);
		return row && row.childNodes.length > 0;
	},
	
	isRowHolderRendered: function(index){
		var row = this.getRow(index);
		return row;
	},

	//拖动滚动条触发的事件
	onLiveScroll : function(){
		if (this.scrollDelay) {
			if (!this.renderTask) {
				this.renderTask = new Ext.util.DelayedTask(this.refreshBody, this);
			}
			this.renderTask.delay(this.scrollDelay);
			var mb = this.scroller.dom;
			this.lockedScroller.dom.scrollTop = mb.scrollTop;
		}
	},
	
	//渲染
	refreshBody : function(){
        this.grid.stopEditing();
        var result = this.renderBody();
        this.mainBody.update(result);
        this.processRows(0, true);
		this.refreshPart();
	},
	
	renderBody : function() {
		var vr = this.getVisibleRows();
        var markup = this.renderRows(vr.first,vr.last);//只渲染当前屏幕显示的行
        return this.templates.body.apply({rows: markup});
    },
	
	scrollToTop : function() {
        Ext.ux.grid.icss.DynamicGridView.superclass.scrollToTop.call(this);
        this.liveScroller.dom.scrollTop = 0;
        this.liveScroller.dom.scrollLeft = 0;
    },
	
	syncLiveScroll : function(first){
		var scrollTopsss = (first+1)*this.getCalculatedRowHeight();
		this.liveScroller.dom.scrollTop = scrollTopsss;
	},
	
	//处理鼠标滚轮事件，只支持IE
	syncMousewheel : function(e){
		var vr = this.getVisibleRows();
		if(e.getWheelDelta()==-1){//滚轮往下
			var last = Math.min(vr.last+this.scrollOnceAmount,this.ds.getCount()-1);
			var first = vr.first + this.scrollOnceAmount;
			if(last == (this.ds.getCount()-1)){
				first = last - this.getVisibleRowCount();
			}
			this.syncLiveScroll(first);
		}else if(e.getWheelDelta()==1){//滚轮往上
			var first = Math.max(vr.first-this.scrollOnceAmount,0);
			var last = vr.last - this.scrollOnceAmount;
			if(first == 0){
				last = first + this.getVisibleRowCount();
			}
			this.syncLiveScroll(first);
		}
		var mb = this.scroller.dom;
		this.lockedScroller.dom.scrollTop = mb.scrollTop;
	},
	
	onColumnWidthUpdated : function(col, w, tw){
        this.adjustVisibleRows();
        this.adjustBufferInset();
    },
	
	onAllColumnWidthsUpdated : function(ws, tw){
        this.adjustVisibleRows();
        this.adjustBufferInset();
    },
	
	layout : function(){
        if(!this.mainBody){
            return;
        }
        var g = this.grid;
        var c = g.getGridEl(), cm = this.cm,
                expandCol = g.autoExpandColumn,
                gv = this;

        var csize = c.getSize(true);
        var vw = csize.width;
        if(vw < 20 || csize.height < 20){
            return;
        }

        if(g.autoHeight){
            this.scroller.dom.style.overflow = 'visible';
        }else{
            this.el.setSize(csize.width, csize.height);
            var hdHeight = this.mainHd.getHeight();
            var vh = csize.height - (hdHeight);
            this.scroller.setSize(vw, vh);
            if(this.innerHd){
                this.innerHd.style.width = (vw)+'px';
            }
        }
        this.liveScroller.dom.style.top = this.hdHeight+"px";
        if(this.forceFit){
            if(this.lastViewWidth != vw){
                this.fitColumns(false, false);
                this.lastViewWidth = vw;
            }
        }else {
            this.autoExpand();
        }
        this.adjustVisibleRows();
        this.adjustBufferInset();
        this.onLayout(vw, vh);
    },
    
    
	
    adjustBufferInset : function(){
        var liveScrollerDom = this.liveScroller.dom;
        var g = this.grid, ds = g.store;
        var c  = g.getGridEl();
        var elWidth = c.getSize().width;

		if(!ds.totalLength){
			ds.totalLength = 0;
		}
        var hiddenRows = (ds.totalLength == this.visibleRows-this.rowClipped)
                       ? 0
                       : Math.max(0, ds.totalLength-(this.visibleRows-this.rowClipped));
        if (hiddenRows == 0) {
            liveScrollerDom.style.display = 'none';
            return;
        } else {
            liveScrollerDom.style.display = '';
        }
        var scrollbar = this.cm.getTotalWidth()+this.scrollOffset > elWidth;
        var contHeight = liveScrollerDom.parentNode.offsetHeight +
                         ((ds.totalLength > 0 && scrollbar)
                         ? - this.horizontalScrollOffset
                         : 0)
                         - this.hdHeight;
        liveScrollerDom.style.height = Math.max(contHeight, this.horizontalScrollOffset*2)+"px";
        if (this.rowHeight == -1) {
            return;
        }
		this.liveScrollerInset.style.height = (hiddenRows == 0 ? 0 : contHeight+(hiddenRows*this.getCalculatedRowHeight()))+"px";
    },
	
    adjustVisibleRows : function(){
		if (this.rowHeight == 19) {
			if (this.getRows()[0]) {
				this.rowHeight = this.getRows()[0].offsetHeight;
                if (this.rowHeight <= 0) {
                    this.rowHeight = 19;
                    return;
                }
            } else {
                return;
            }
		}
        var g = this.grid, ds = g.store;
        var c = g.getGridEl();
        var cm = this.cm;
        var size = c.getSize();
        var width = size.width;
        var vh = size.height;
        var vw = width-this.scrollOffset;
        if (cm.getTotalWidth() > vw) {
            vh -= this.horizontalScrollOffset;
        }
        vh -= this.mainHd.getHeight();
        var totalLength = ds.totalLength || 0;
		var visibleRows = Math.max(1, Math.floor(vh/this.getCalculatedRowHeight()));
        this.rowClipped = 0;
		if (totalLength > visibleRows && this.getCalculatedRowHeight() / 3 < (vh - (visibleRows*this.getCalculatedRowHeight()))) {
            visibleRows = Math.min(visibleRows+1, totalLength);
            this.rowClipped = 1;
        }
        if (this.visibleRows == visibleRows) {
            return;
        }
        this.visibleRows = visibleRows;
        if (this.rowIndex + (visibleRows-this.rowClipped) > totalLength) {
            this.rowIndex     = Math.max(0, totalLength-(visibleRows-this.rowClipped));
            this.lastRowIndex = this.rowIndex;
        }
    },
	
	refreshPart : function(){
        var ds = this.grid.store, index;
		var view = this.grid.getView();
		var sm = this.grid.getSelectionModel();
		sm.isRefresh = false;
		var s = sm.getSelections();
		var vr = view.getVisibleRows();
		var endRow = Math.min(vr.last,ds.getCount()-1);
		var rs = ds.getRange(vr.first, endRow);		
		for(var i = 0 ,len = rs.length; i < len; i++){
			if(s.length==0){
				break;//提高效率，没有选中项的话直接break
			}
			var r = rs[i];
			var isSelect = sm.isIdSelected(r.id);
			if(isSelect){
				index = ds.indexOfId(r.id);
				var ri = index - vr.first;
				sm.selectRow(index, true,false,ri);
			}
        }
        if(s.length != sm.selections.getCount()){
            sm.fireEvent("selectionchange", sm);
        }
    }
});