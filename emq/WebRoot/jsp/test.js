/*
 * @author foolice
 */

Ext.onReady(function() {
			createMianUI();
			init();
		});
function init() {
	var root = Ext.getCmp('mainTree').getRootNode();
	root.expand();
}

function createMianUI() {
	var combo = new Ext.form.commonCombox({
				id : 'numCombo',
				fn : PlantService.testCombox,
				emptyText : '请选择显示条数'
			});
	var mainGrid = createMainGrid();
	var mainTree = createMainTree();
	var panel = new Ext.Panel({
				id : "panel",
				layout : "border",
				tbar : [{
							xtype : 'tbspacer'
						}, {
							xtype : 'tbspacer'
						}, {
							xtype : 'tbtext',
							text : '显示数量'
						}, combo, {
							xtype : 'tbseparator'
						}, {
							pressed : true,
							text : '查  询',
							id : 'btn_query',
							handler : queryInfo
						}],
				items : [
						// {region:"north",border:false,split:true,layout:'fit',height:23,items:[form]},
						{
					region : "center",
					split : true,
					layout : "fit",
					items : [mainTree]
				}]
			});
	var mainUI = new Ext.Viewport({
				layout : "fit",
				items : [panel]
			});
}

function queryInfo() {
	var curNode = Ext.getCmp('mainTree').getSelectedNode();
	alert(curNode.id);
	alert(curNode.text);
	alert(curNode.attributes.nid);
	Ext.getCmp('mainGrid').getStore().load({
				params : [Ext.getCmp('numCombo').getValue()]
			});
}

function createMainTree() {
	var tree = new Ext.tree.commonTree({
				id : 'mainTree',
				fn : PlantService.getTreeNode,
				rootName : '测试'
			});
	var treeLoader = tree.getLoader();
	treeLoader.on("load", function(o, node, response) {
				node.on("click", function(node, event) {
                     alert(node.isLeaf());
                     alert(node.id);
						});
				var childNodes = node.childNodes;
				for (var i = 0; i < childNodes.length; i++) {
					childNodes[i].on("click", function(node, event) {
                      alert(node.isLeaf());
                     alert(node.id);
							});
				}
			});
	return tree;
}

function createMainGrid() {
	var gridEmq = new GridEmq();
	gridEmq.head = "姓名,性别,年龄,学历,婚姻状况,住址,公司,职业,爱好,工作性质,个人方向";
	gridEmq.tableType = 2;
	gridEmq.moreHead = [[";基本信息,4;其他信息,4;备注,2"]];
	var mainGrid = new Ext.grid.GridPanelEx({
				id : "mainGrid",
				extdata : gridEmq,
				fn : PlantService.getInstanceExtGrid
			});

	return mainGrid;
}