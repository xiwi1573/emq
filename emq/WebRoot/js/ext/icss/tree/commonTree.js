/**
 * use common tree
 * 定义树中层次字典：
 * (供电局:Gdj)(县区市:Xqs)(变电站:Bdz)(维护班组:Whbz)(人员:Ry)(用电户:Ydh)(线路:Xl)
 * 方法以getGdj这种定义。二级getGdjXqs
 * ('1':供电局+县区市+变电站),('2':供电局+维护班组+人员),('3':用电户+变电站),('4':线路+变电站)
 */

Ext.tree.commonTree = function(config) {
	this.initConfig = config || {};
	Ext.tree.commonTree.superclass.constructor.call(this, config);
};

Ext.tree.commonTree = Ext.extend(Ext.tree.TreePanel, {
			initComponent : function() {
				var serviceJs ="PlantService" ;
				var treeAssemble = this.treeAssemble ;
				var levelNum = treeAssemble.length ;
				var initRootFn = serviceJs+".get"+treeAssemble[0];
				//初始化loader第一层方法，以root为父节点取数。
				var treeLoader = new Ext.tree.DWRTreeLoader({
							fn :eval(initRootFn)
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
							treeLevel : 'root',
							nextLevel : 'root0'
						});

				this.root = root;
				this.border = false;
				this.loader = treeLoader;
				this.autoScroll = true;
				treeLoader.on("beforeload", function(loader, node) {
					        var treeLevel = node.attributes.treeLevel;
					        var nextLevel = node.attributes.nextLevel;
					        var ifleaf =false;
					        if(treeLevel!="root"){
					          var curL = Number(treeLevel.substring(4,treeLevel.length));
					          var nextL =curL+1;
					          nextLevel ="root"+nextL;
					          if((nextL+1)==levelNum){
					            ifleaf = true ;
					          }
					          var cfn ="";
					          for(var i=0;i<=nextL;i++){
					          	cfn +=treeAssemble[i];
					          }
					          loader.fn =eval(serviceJs+".get"+cfn);
					        }
					        loader.args[0] = node.id;
							loader.args[1] = treeLevel;
							loader.args[2] = nextLevel;
							loader.args[3] = ifleaf;
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
