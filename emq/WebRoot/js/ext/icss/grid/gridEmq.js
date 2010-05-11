 /**
  * 构造Ext表格工具，支持多表头
  */
 function GridEmq(){
	/**
	 * 表格类型1:simple,2:double,3:third
	 */
	var tableType=1;
	
	/**
	 * 列名1,列名2...
	 */
	var head="";
	
	/**
	 * [[列名1,跨多少列;列名2,跨多少列],[列名1,跨多少列;列名2,跨多少列]]
	 */
	var moreHead=[];
	
	/**
	 * 列宽100,200...
	 */
	var winth="";
	
	/**
	 * 列锁定到某行
	 */
	var lockColumn=0;
	
	/**
	 * 列类型 string,int,date,float
	 */
	var colunmType="";
	
	/**
	 * 渲染类型 Ext.util.Format.dateRenderer('Y-m-d'),Util.rmbMoney
	 */
	var renderer="";
	
	/**
	 * 对齐方式 center,right.... ,
	 */
	var alignType="";
	
	/**
	 * 是否需要合计 false,true.... ,
	 */
	var issum="";
	
	/**
	 * 根据传入数据获取ExtGrid的格式数据
	 * @return
	 */
	this.getExtGrid = function(){
		var renturnList = [];
		var headList = [];
		var windthList = [];
		var colunmTypeList = [];
		var alignTypeList = [];
		var issumTypeList = [];
		var rendererList = [];
		
		//表头
		if(this.head!=null){
			headList = this.head.split(",");
		}
//		列宽
		if(this.winth!=null&&this.winth!=""){
			windthList = this.winth.split(",");
		}else{
			for(var i=0;i<headList.length;i++){
				windthList.push("80");
			}
		}
		if(windthList.length<headList.length){
			for(var i=windthList.size();i<headList.length;i++){
				windthList.push("80");
			}
		}
//		列类型
		if(this.colunmType!=null&&this.colunmType!=""){
			colunmTypeList = this.colunmType.split(",");
		}else{
			for(var i=0;i<headList.length;i++){
				colunmTypeList.push("string");
			}
		}
		if(colunmTypeList.length<headList.length){
			for(var i=colunmTypeList.length;i<headList.length;i++){
				colunmTypeList.push("string");
			}
		}
//		对齐方式
		if(this.alignType!=null&&this.alignType!=""){
			alignTypeList = this.alignType.split(",");
		}else{
			for(var i=0;i<headList.length;i++){
				alignTypeList.push("center");
			}
		}
		if(alignTypeList.length<headList.length){
			for(var i=alignTypeList.length;i<headList.length;i++){
				alignTypeList.push("center");
			}
		}
		
//		是否合计
		if(this.issum!=null&&this.issum!=""){
			issumTypeList = this.issum.split(",");
		}else{
			for(var i=0;i<headList.length;i++){
				issumTypeList.push("false");
			}
		}
		if(issumTypeList.length<headList.length){
			for(var i=colunmTypeList.length;i<headList.length;i++){
				issumTypeList.push("false");
			}
		}
		
//		是否需要渲染
		if(this.renderer!=null&&this.renderer!=""){
			rendererList = this.renderer.split(",");
			for(var i=0;i<rendererList.length;i++){
				if(rendererList[i]!=""){
					rendererList[i]=",renderer:"+rendererList[i];
				}
			}
		}else{
			for(var i=0;i<headList.length;i++){
				rendererList.push("");
			}
		}
		if(rendererList.length<headList.length){
			for(var i=rendererList.length;i<headList.length;i++){
				rendererList.push("");
			}
		}
		
		//标题
		var header =[];
		//Map dataMap = (HashMap)valueMap.get(0);
		var locked = true;
		for(var i=0;i<headList.length;i++){
			if(i>=this.lockColumn){
				locked = false;
			}
			if(this.issum==null||this.issum.length==0){
				header.push("{header:'"+headList[i]+"',dataIndex:'"+headList[i]+"',width:"+windthList[i]+",locked:"+locked+",sortable:true,align:'"+alignTypeList[i]+"'"+rendererList[i]+"}");
			}else{
				if(i==0){
					header.push("{header:'"+headList[i]+"',dataIndex:'"+headList[i]+"',width:"+windthList[i]+",locked:"+locked+",sortable:true,align:'"+alignTypeList[i]+"',sumcaption:'合计'"+rendererList[i]+"}");
				}else{
					header.push("{header:'"+headList[i]+"',dataIndex:'"+headList[i]+"',width:"+windthList[i]+",locked:"+locked+",sortable:true,align:'"+alignTypeList[i]+"',issum:"+issumTypeList[i]+rendererList[i]+"}");
				}
			}
		}
		
		//数据
		var fields = [];
		for(var i=0;i<headList.length;i++){
			fields.push("{name:'"+headList[i]+"',type:'"+colunmTypeList[i]+"'}");
		}
		
		renturnList.push(header);
		renturnList.push(fields);
		
		if(typeof(this.tableType)=="undefined"){
			this.tableType = 1;
		}
		if(this.tableType!=1){
			var moreHeadList = [];
			for(var i=0;i<this.moreHead.length;i++){
				var moreHeadValue = this.moreHead[i];
				var tempHeadList = ["{}","{}"];
				for(var j=0;j<moreHeadValue.length;j++){
					var tempHeadValue = moreHeadValue[j];
					if(tempHeadValue!=""){
						var mHead = tempHeadValue.split(";");
						for(var x=0;x<mHead.length;x++){
							var tempValue = mHead[x];
							if(tempValue==""){
								tempHeadList.push("{}");
							}else{
								var t = tempValue.split(",");
								tempHeadList.push("{header:'"+t[0]+"',colspan:"+t[1]+",align:'center'}");
							}
						}
					}
				}
			    moreHeadList.push(tempHeadList);
		    }
		   renturnList.push(moreHeadList);
		}
		return renturnList;
	}

	
	
};
