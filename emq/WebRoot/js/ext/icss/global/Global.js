/**
 * 全局信息定义
 */
Ext.namespace("Ext.icss");

Ext.icss.Global = function(){
    return {  
	
		isSelectAll:false,
    	/**
    	 * 集团组织编码
    	 */
    	GROUP_CODE:'12530104',
    	/**
    	 * 合同到期提醒天数
    	 */
    	ALERTDAY:3,
    	/**
    	 * 国家局交易系统，红云集团编码
    	 */
    	PRODUCER_CODE:'12530101',
        /**
         * 返回集团组织编码
         * <p>
         * Example code:
         * <pre><code>
         Ext.icss.Global.getGroupCode();
         * </code></pre>
         * @return GROUP_CODE
         */
        getGroupCode: function(){
        	return this.GROUP_CODE;
        },
         /**
         * 返回国家局交易系统，红云集团编码
         * <p>
         * Example code:
         * <pre><code>
         Ext.icss.Global.getProdurcerCode();
         * </code></pre>
         * @return GROUP_CODE
         */
        getProdurcerCode: function(){
        	return this.PRODUCER_CODE;
        },
        CONDITION_LEVEL : {
        			LEVEL1 : 1,// 全国
        			LEVEL2 : 2,// 市场总部
					LEVEL3 : 3,// 省
					LEVEL4 : 4,// 市场部
					LEVEL5 : 5,  // 市(商业公司)
					LEVEL6 : 6  // 城市
		     },
		/**
		* 取得条件对象的级次信息
		*/
		getConditionLevel : function() {
			return this.CONDITION_LEVEL;
		},
        /**
         * 北京合同
         */
        CONTRACT_1253:{code:'12530104',name:'北京合同'},
        /**
         * 昆明合同
         */
        CONTRACT_9953:{code:'99530103',name:'昆明合同'},
        
        /**
         * 配货请求默认值设置相关
         */
        sendRegionDefault:1,
        sendAreaDefault:2,
        sendAreaDefaultName:'云南省昆明市北郊上庄昆明卷烟厂仓库',
        trantypeDefault:1,
        trantypeDefaultName:'汽运',
        
        /**
         * 返回合同类型编码对应的名称
         * <p>
         * Example code:
         * <pre><code>
         Ext.icss.Global.getContractTypeName('12530101');
         * </code></pre>
         * @return 合同类型编码对应的名称
         */
        getContractTypeName:function(code){
        	if(code == this.CONTRACT_1253.code)
        	{
        		return this.CONTRACT_1253.name;
        	}else if (code == this.CONTRACT_9953.code)
        	{
        		return this.CONTRACT_9953.name;
        	}
        },
        INFOMATION_TITLE:'提示',
        ERROR_TITLE:'错误',
        WARNING_TITLE:'警告',
        /**
         * 入库类型
         */
        IN_TYPE:'IN_TYPE',
        getInType:function(){
        	return this.IN_TYPE;
        },
        /**
         * 全部编码
         */
        ALL_KEY:-99,
        ALL_KEY_NAME:'全部',
        /**
         * 本省对象信息
         * @type 
         */
        LOCAL_PROVINCE:{code:'530000',name:'云南省'},
        getLocalProvince:function(){
        	return this.LOCAL_PROVINCE;
        },
        /**
         * 默认批号
         */
        BATCHNUM:'0',
        getDefaultLoadMask:function(){
        	return {msg:'正在加载数据，请稍候……'};
        },
        getNoneState: function(){
        	return 99;
        },
        
        /**
         * 汽车运输类型得ID
         */
        CAR_TRAN_TYPE:'1',
        /**
         * 默认运输类型为汽运
         */
        DEFAULT_TRAN_TYPE:'1',
        DEFAULT_TRAN_TYPE_NAME:'汽运',
        /**
         * 调度期限
         */
        TIMELIMIT:2,
        /**
         * 配货请求及预计划最大有效期限
         */
        getValidityPeriod:function(){
        	return 29;
        },
        /**
         * 配货请求及预计划最小有效期限
         */
         getValidityPeriodReverse:function(){
        	return -29;
        },
        /**
         * 默认预计到达天数
         */
        getDefaultArriveDays:function()
        {
        	return 7;
        },
        /**
         * 配货请求及预计划跨年半年最大有效期限
         */
        getValidityGapPeriod:function(){
        	return 45;
        },
        getSystemDate:function(){
        	return new Date();
        },
        
        getCurrentDate:function(){
        	return new Date();
        },
	
		/**
		 * @author liuguo
		 * @description 税率
		 */
		getTaxRate:function(){
			return 0.17;
		},
		/**
		 * @author liuguo
		 * @description 获取承运部门为"自提"的部门Id
		 */
		getTranDepIdWithSelfFetch:function(){
			return '04131';
		},
		
		/**
		 * @author liuguo
		 * @description 获取承运部门为"自提"的部门名称
		 */
		getTranDepNameWithSelfFetch:function(){
			return '自提';

		},
		
		/**
		 * @author liuguo
		 * @description 获取承运公司为"自提"的公司Id
		 */
		getTranCompIdWithSelfFetch:function(){
			return '0413';
		},
		
		/**
		 * @author liuguo
		 * @description 获取承运公司为"自提"的公司名称
		 */
		getTranCompNameWithSelfFetch:function(){
			return '自提';
		},
		
		/**
		 * @author 张尧伟
		 * @description 取得出口烟计划的价格类型
		 */
		getPriceTypeByExport:function(){
			return {priceType:'3',priceTypeName:'协议价'};
		}, 
		/**
        * 增值税发票代码
        */
        InfoTypeCode:'1101001140',
        /**
         * 金税开票正式环境 true ,false为测试环境
         */
        isTaxcard:true,
		/**
		 * @author liuguo
		 * @description 描述是否实在开发环境中打印，与之相对的是生产环境
		 * @return 如果是在开发环境中打印则返回true，在生产环境中打印则返回false
		 */
		isPrintInDeveloping:function(){
			//FIXME:在项目正式上线的时候，需要修改为生产环境对应的信息，即改为false。
			//TODO:有关出库、入库和通知单打印的地方都需要由这里来控制。
			return false;
		},
		/**
    	* 进度检查对象常量定义
    	*/
	    VerifyPlanResult:{
	        	OK : 0,//通过
				OK_AND_ERROR: -1,//即有成功的，也有失败的
				TARGET_MONTH_UNDEFINED : 100,//月指标进度没有定义
				TARGET_MONTH_ERROR : 101,//超月指标进度
				TARGET_WEEK_UNDEFINED : 200,//周指标进度没有定义
				TARGET_WEEK_ERROR : 201,//超周指标进度
				XY_ERROR : 300//超协议执行
	   },
        /**
         * 客户区各区域的key属性
         */
        clientPXKey:{
        	precontract:100
        },
        /**
         * 通过客户区各区域的key属性,取得对应的高宽
         */
        getClientHW:function(key){
        	switch(key)
        	{
        	case this.clientPXKey.precontract:
        		return {width:Util.getW(0.33),height:Util.getH(0.37)};
        	}
        },
        getClientH:function(key){
        	return this.getClientHW(key).height;
        },
        getClientW:function(key){
        	return this.getClientHW(key).width;
        },
        isAllKey:function(key){
        	return (this.ALL_KEY == key);
        },
        /**
         * double or float precision
         */
        PRECISION : 0.000001,
        getTranRetMsg:function(code){
        	var msg = '';
        	if(code == '000')
        	{
        		msg =  '000	传输成功!';
        	}
        	else if(code == '100')
        	{
        		msg =  '100	用户不存在!';
        	}
        	else if(code == '100')
        	{
        		msg =  '100	用户不存在!';
        	}
        	else if(code == '101')
        	{
        		msg =  '101	用户已停用!';
        	}
        	else if(code == '102')
        	{
        		msg =  '102	帐户名密码不正确!';
        	}
        	else if(code == '103')
        	{
        		msg =  '103	调用接口名错误!';
        	}
        	else if(code == '104')
        	{
        		msg =  '104	应用系统错误! ';
        	}
        	return msg;
        },
        
        /**
         * rendererOK状态样式
         */
        RENDERER_OK_STATE:"<span style='color:red;font-weight:900;font-size:130%';font-family:'Arial Black'>√</span>"
        
    };
}
();
/**
 * 定义别名
 * @type 
 */
Global = Ext.icss.Global;
