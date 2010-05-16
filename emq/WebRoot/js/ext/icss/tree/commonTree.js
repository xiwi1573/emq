/**
 * use common tree
 * 
 * @param
 * 
 * rootName为导航树指定一个根,id为节点的唯一标志,nid记录节点的其他信息,没有则无需指定,可以为空。
 * treeType指定树的类型;('1':供电局+县区市+变电站),('2':供电局+维护班组+人员),('3':用电户+变电站),('4':线路+变电站)。如果不指定默认'1'。
 * treeLevel
 * 节点在某种树中层次的标致。例如：('1':供电局+县区市+变电站)中'1_1'表示所在的层次为：供电局;'1_2'表示所在的层次为：县区市依次类推。
 * 当节点为根节点则为root。
 */

Ext.tree.commonTree = function(config) {
	this.initConfig = config || {};
	Ext.tree.commonTree.superclass.constructor.call(this, config);
};

Ext.tree.commonTree = Ext.extend(Ext.tree.TreePanel, {
			initComponent : function() {
				var treeLoader = new Ext.tree.DWRTreeLoader({
							fn : PlantService.getCommonTreeNode
						});
				var treeType;
				if (undefined == this.treeType) {
					treeType = '1';
				} else {
					treeType = this.treeType;
				}
				var rootName;
				if (undefined == this.rootName) {
					rootName = '机构';
				} else {
					rootName = this.rootName;
				}
				var root = new Ext.tree.AsyncTreeNode({
							text : rootName,
							treeType : "1",
							id : '-1',
							treeLevel : 'root',
							nid : '-1'
						});

				this.root = root;
				this.border = false;
				this.loader = treeLoader;
				this.autoScroll = true;
				treeLoader.on("beforeload", function(loader, node) {
							loader.args[0] = node.id;
							loader.args[1] = node.attributes.treeLevel;
							loader.args[2] = treeType;
							loader.args[3] = node.attributes.nid;
						});
				Ext.tree.commonTree.superclass.initComponent.call(this);
				this.init();
			},
			// 取选中节点
			getSelectedNode : function() {
				return this.getSelectionModel().getSelectedNode();
			},
			init : function() {
				this.getRootNode().expand();
			},
			// 取选中节点的父节点
			getParentNode : function() {
				return this.getSelectionModel().getSelectedNode().parentNode;
			}
		});

Ext.reg('commonTree', Ext.tree.commonTree);
