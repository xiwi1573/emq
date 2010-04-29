/*!
 *  2009-07-20 v0.1 icss zhaoqy
 *  分批动态渲染时，如果需要使用全选按钮，那么请使用此类。
 *  已经解决的bug：
	1、选择时，无法准确的获取所选行的record对象的bug
          2、在一屏内选择，然后拖动滚动条后下一屏显示的内容也会被选中的bug。比如选择第3、4行时，滚屏的时候其它行滚动到3、4行的位置时会变成选中状态。
	3、在一屏内第一条数据选不中的bug
	4、全选后拖动滚动条出现空白屏的bug
	5、反复拖动滚动条时，无法记录已选中行的bug
	6、不能取消选中行的bug
	7、另外，解决了项目组目前在Grid中只能用ctrl+鼠标左键多选的问题.--有待验证
 * 
 */
 
Ext.namespace('Ext.ux.grid.icss');

Ext.ux.grid.icss.CheckboxSelectionModel = Ext.extend(Ext.ux.grid.icss.RowSelectionModel, {
    header: '<div class="x-grid3-hd-checker">&#160;</div>',
    width: 20,
    sortable: false,
    menuDisabled:true,
    fixed:true,
    dataIndex: '',
    id: 'checker',
	
	initEvents : function(){
        Ext.grid.CheckboxSelectionModel.superclass.initEvents.call(this);
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
            Ext.fly(view.innerHd).on('mousedown', this.onHdMouseDown, this);

        }, this);
    },
	
    onMouseDown : function(e, t){
		if(e.button === 0 && t.className == 'x-grid3-row-checker'){
            e.stopEvent();
			var g = this.grid, view = g.getView();
			var vr = view.getVisibleRows();
            var row = e.getTarget('.x-grid3-row');
            if(row){
				var index = row.rowIndex;
                index = index+vr.first;
                if(this.isSelected(index)){
                    this.deselectRow(index,false,row.rowIndex);
                }else{
                    this.selectRow(index, true,false,row.rowIndex);
                }
            }
        }
    },
	
	onHdMouseDown : function(e, t){
        if(t.className == 'x-grid3-hd-checker'){
            e.stopEvent();
            var hd = Ext.fly(t.parentNode);
            var isChecked = hd.hasClass('x-grid3-hd-checker-on');
            if(isChecked){
                hd.removeClass('x-grid3-hd-checker-on');
                if(this.grid.store.getCount()==this.getSelections().length){
					this.clearSelections(true);
				}else{
					this.clearSelections();
				}
            }else{
                hd.addClass('x-grid3-hd-checker-on');
				if(!Global.isSelectAll){
					this.selectAll();
				}
            }
        }
    },
	
    renderer : function(v, p, record){
        return '<div class="x-grid3-row-checker">&#160;</div>';
    }
});