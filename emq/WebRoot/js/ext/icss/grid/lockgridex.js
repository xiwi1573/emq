/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
/**
 * @include "../js/Ext2.1/icss/lockgrid.js"
 * @type {}
 */
/** Editor grid with locking column support */
Ext.grid.LockingEditorGridPanel = Ext.extend(Ext.grid.EditorGridPanel,{
	clicksToEdit : 1,
    getView : function(){
        if(!this.view){
            this.view = new Ext.grid.LockingGridView(this.viewConfig);
        }
        return this.view;
    },
    initComponent : function(){
        if(!this.cm) {
            this.cm = new Ext.grid.LockingColumnModel(this.columns);
            delete this.columns;
        }
        if(!this.groupedHeaders)
        {
    	    this.groupedHeaders = this.groupedHeaders;
        }
        //在编辑完成后，需要更新合计列
        this.on('afteredit',this.onAfteredit,this);
        Ext.grid.LockingEditorGridPanel.superclass.initComponent.call(this);
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
  }
});