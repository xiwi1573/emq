/**
 * use common tree
 * 
 * @param
 * 
 */

Ext.tree.commonTree = function(config) {
	this.initConfig = config || {};
	Ext.tree.commonTree.superclass.constructor.call(this, config);
};

Ext.tree.commonTree = Ext.extend(Ext.tree.TreePanel, {
			initComponent : function() {
				if (typeof(this.fn) == 'undefined') {
					new Ext.Msg.alert("错误!", "请您添加有效的数据源。");
				}
				var treeLoader = new Ext.tree.DWRTreeLoader({
							fn : this.fn
						});
				var rootName;
				if (undefined == this.rootName) {
					rootName = '机构';
				} else {
					rootName = this.rootName;
				}
				var root = new Ext.tree.AsyncTreeNode({
							text : rootName,
							id : '-1',
							nid : '-3'
						});

				this.root = root;
				this.border = false;
				this.loader = treeLoader;
				this.autoScroll = true;
				treeLoader.on("beforeload", function(loader, node) {
							loader.args[0] = node.id;
						});
				Ext.tree.commonTree.superclass.initComponent.call(this);
			},
			// 取选中节点
			getSelectedNode : function() {
				return this.getSelectionModel().getSelectedNode();
			}
		});

Ext.reg('commonTree', Ext.tree.commonTree);
