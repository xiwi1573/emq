/*
* @history	
*	2009-9-18 modified by guqiong:
*			add Ext.icss.Util.Format 
*				Ext.icss.Util.Format.ExtStrategy 
*				Ext.icss.Util.Format.ExcelStrategy 
* @history	
*	2010-4-16 modified by zhenggang:
*			add Ext.icss.Util.Browser
*/


// 格式化数字显示方式，也可以用于对数字重定精度。

/*
 * 测试 alert(formatNumber(0,'')); alert(formatNumber(12432.21,'#,###'));
 * alert(formatNumber(12432.21,'#,###.000#'));
 * alert(formatNumber(12432,'#,###.00'));
 * alert(formatNumber('12432.415','#,###.0#'));
 */
Ext.namespace("Ext.icss");

Ext.icss.Util = function(){
    return {
    
        /**
         * 按指定的格式化模式格式化数字
         * <p>
         * Example code:
         * <pre><code>
         var ret = Ext.icss.Util.formatNumber(12432.21,'#,###');
         var ret = Ext.icss.Util.formatNumber(12432.21,'#,###.000#');
         var ret = Ext.icss.Util.formatNumber(12432,'#,###.00');
         var ret = Ext.icss.Util.formatNumber('12432.415','#,###.0#');
         * </code></pre>
         * @param {Number/String} number 需要格式化的数字
         * @param {String} pattern 格式化模式
         * @return {String} 格式化后的串
         */
        formatNumber: function(number, pattern){
            var str = number.toString();
            var strInt;
            var strFloat;
            var formatInt;
            var formatFloat;
            if (/\./g.test(pattern)) {
                formatInt = pattern.split('.')[0];
                formatFloat = pattern.split('.')[1];
            }
            else {
                formatInt = pattern;
                formatFloat = null;
            }
            
            if (/\./g.test(str)) {
                if (formatFloat != null) {
                    var tempFloat = Math.round(parseFloat('0.' +
                    str.split('.')[1]) *
                    Math.pow(10, formatFloat.length)) /
                    Math.pow(10, formatFloat.length);
                    strInt = (Math.floor(number) + Math.floor(tempFloat)).toString();
                    strFloat = /\./g.test(tempFloat.toString()) ? tempFloat.toString().split('.')[1] : '0';
                }
                else {
                    strInt = Math.round(number).toString();
                    strFloat = '0';
                }
            }
            else {
                strInt = str;
                strFloat = '0';
            }
            if (formatInt != null) {
                var outputInt = '';
                var zero = formatInt.match(/0*$/)[0].length;
                var comma = null;
                if (/,/g.test(formatInt)) {
                    comma = formatInt.match(/,[^,]*/)[0].length - 1;
                }
                var newReg = new RegExp('(\\d{' + comma + '})', 'g');
                
                if (strInt.length < zero) {
                    outputInt = new Array(zero + 1).join('0') + strInt;
                    outputInt = outputInt.substr(outputInt.length - zero, zero)
                }
                else {
                    outputInt = strInt;
                }
                
                var outputInt = outputInt.substr(0, outputInt.length % comma) +
                outputInt.substring(outputInt.length % comma).replace(newReg, (comma != null ? ',' : '') + '$1')
                outputInt = outputInt.replace(/^,/, '');
                
                strInt = outputInt;
            }
            
            if (formatFloat != null) {
                var outputFloat = '';
                var zero = formatFloat.match(/^0*/)[0].length;
                
                if (strFloat.length < zero) {
                    outputFloat = strFloat + new Array(zero + 1).join('0');
                    //outputFloat        = outputFloat.substring(0,formatFloat.length);
                    var outputFloat1 = outputFloat.substring(0, zero);
                    var outputFloat2 = outputFloat.substring(zero, formatFloat.length);
                    outputFloat = outputFloat1 +
                    outputFloat2.replace(/0*$/, '');
                }
                else {
                    outputFloat = strFloat.substring(0, formatFloat.length);
                }
                
                strFloat = outputFloat;
            }
            else {
                if (pattern != '' || (pattern == '' && strFloat == '0')) {
                    strFloat = '';
                }
            }
            
            return strInt + (strFloat == '' ? '' : '.' + strFloat);
        },
        /**
         * 将数字格式化为人民币格式，保留2位有效数字
         * @param {Number/String} value The numeric value to format
         * @return {String} The formatted currency string
         */
        rmbMoney: function(v){
            v = (Math.round((v - 0) * 100)) / 100;
            v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10)) ? v + "0" : v);
            v = String(v);
            var ps = v.split('.');
            var whole = ps[0];
            var sub = ps[1] ? '.' + ps[1] : '.00';
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)) {
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            v = whole + sub;
            if (v.charAt(0) == '-') {
                return '-' + v.substr(1);
            }
            return v;
        },
        
        /**
         * 将数字进行四舍五入
         * @param v 数字
         * @param dot 有效数字中的小数点位数
         * @return 四舍五入后的结果
         */
        roundNumber: function(v,dot){
            var v = String(Math.round(v * Math.pow(10,dot)) / Math.pow(10,dot));
            var ps = v.split('.');
            var i=0;
            var sub0 = ".";
            for(;i<dot;i++){
            	sub0 = sub0+'0';
            }
            var whole = ps[0];
            var sub = ps[1] ? '.' + ps[1] : sub0;
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)) {
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            v = whole + sub;
            if (v.charAt(0) == '-') {
                return '-' + v.substr(1);
            }
            return v;
        },
            /**
         * 设置下拉框的第一行
         * @param 下拉框id,field:字段名
         */
     setCombox:function(id,field) {
	DWREngine.setAsync(false);
	var combox = Ext.getCmp(id);
	combox.init();
	var rec = combox.store.getAt(0);
	combox.setValue(rec.get(field));
	DWREngine.setAsync(true);
},
 /**
         * 复制Ext.data.Record[]
         */
copyRecords:function(records)
{var newRecords=[];
    
	 
	 
	for(var i=0;i<records.length;i++)
	{
	newRecords[newRecords.length]=records[i].copy(['recId'+i]);
	
	}
	return newRecords;
},
         /**
         * 将数字格式化为负数的人民币格式，保留2位有效数字
         * @param {Number/String} value The numeric value to format
         * @return {String} The formatted currency string
         */
        
        rmbNegativeMoney: function(v){
           
            var  num=parseFloat(v);
              
           if(num&&num>0)
           {
           num=0-num;
            
           }
           return Ext.icss.Util.rmbMoney(num);;
        },
        
        screenHeight: function(){
            return screen.availHeight;
        },
        screenWidth: function(){
            return screen.availWidth;
        },
        getH: function(percentage){
            return screen.availHeight * percentage;
        },
        getW: function(percentage){
            return screen.availWidth * percentage;
        },
        /**
        * 删除左右两端的空格
        * */
        trim:function(str) { 
	   return str.replace(/(^\s*)|(\s*$)/g, "");
        },
        
	/**
	 * form对象中，设置下拉对象的值
	 * <p>
	 * @param {} form from对象
	 * @param {} rec 记录
	 * @param {} idexp id表达式
	 * @param {} pattern id中需要替换的变量
	 * <pre><code>
	 使用示例:
	 设置以preplan_ 开始的对象的值,其中{id}为rec中的id，如下对象的命名，将符合此约束：
	 {
				xtype : 'datefield',
				id : 'preplan_arriveDate',
				fieldLabel : '预计到货日期',
				name : 'arriveDate',
				format : 'Y-m-d',
				width : w
		}
		符合:setFormData(frm,rec,'preplan_{id}',/{id}/);
		
		var bbank_idCombo = new Ext.form.ComboBox({
	    	id:'preplan_bbankIdCombo',
	        store: bbank_idStore,
	        fieldLabel:'需方账号',
	        readOnly:true,
	        valueField:'bankId',
	        displayField:'bank',
	        typeAhead: true,
	        mode: 'remote',
	        triggerAction: 'all',
	        emptyText:'选择需方账号',
	        selectOnFocus:true,
	        width:w
	    });
	    符合:setFormData(frm,rec,'preplan_{id}Combo',/{id}/);
	 * </code></pre>
	 */
	setFormData : function(form, rec, idexp, pattern) {

			var values = rec.data;
			if (Ext.isArray(values)) {
				for (var i = 0, len = values.length; i < len; i++) {
					var v = values[i];
					var f = null;
					var nid = null;
					if (idexp && pattern) {
						nid = idexp.replace(pattern, v.id);
					} else {
						nid = v.id;
					}
					
					f = form.findField(nid);
					if(!f){
						f = Ext.getCmp(nid);
					}
					
					if (f) {
						f.setValue(v.value);
						if (form.trackResetOnLoad) {
							f.originalValue = f.getValue();
						}
					}
				}
			} else {
				var field, id, nid;
				for (id in values) {

					if (typeof values[id] != 'function') {
						if (idexp && pattern) {
							nid = idexp.replace(pattern, id);
						} else {
							nid = id;
						}
						field = form.findField(nid);
						if(!field){
							field = Ext.getCmp(nid);
						}
						if (field) {
							field.setValue(values[id]);
							if (form.trackResetOnLoad) {
								field.originalValue = field.getValue();
							}
						}
					}
				}
			}
		},
		/**
		 * 判断是否为全部
		 * @param allKey 全部的编码
		 * @return true or false
		 */
		isAll:function(allKey){
			return (Global.ALL_KEY == allKey);
		},
		/**
		 * 将空字串转为null
		 * @param {} str
		 * @return {}
		 */
		 str2NULL:function(str){
			if(typeof(str) == 'string' && str.trim() == '')
			{
				str = null;	
			}
			
			return str;
		}
    };
}
();


/**
 * 数组的操作方法
 */
Ext.icss.Util.Array = function(){
    return {
    
        /**
         * 在数组a中，指定的pos之前，插入o对象
         * @param a 数组a
         * @param pos 数组a的位置
         * @param o 要插入到数组a的对象
         */
        insert: function(a, pos, o){
            if (a) {
                var newpos = pos;
                var tmpo = null;
                a[a.length] = null;
                for (var i = (a.length - 1); i > pos; --i) {
                    a[i] = a[i - 1];
                    
                }
                a[pos] = o;
            }
            return a;
        },
        
        /**
         *把对象o追加到数组a的尾部
         *@param a 数组
         *@param o 要追加的对象
         */
        append: function(a, o){
            if (a) {
                a[a.length] = o;
            }
            return a;
        },
        setAt:function(a,pos,o){
        	if(a){
        		a[pos] = o;
        	}
        }
        
    };
}
();


/**
 * 数组的操作方法
 */
Ext.icss.Util.Grid = function(){
    return {
    
        /**
         * 在数组a中，指定的pos之前，插入o对象
         * @param a 数组a
         * @param pos 数组a的位置
         * @param o 要插入到数组a的对象
         */
        selectAll: function(grid, colid, e){
            var sm = grid.getSelectionModel();
            var cm = grid.getColumnModel();
            
            //如果点的列不是checkbox，则不做处理
            if (cm.config[colid].id && cm.config[colid].id != "checker") {
                return true;
            }
            
            //处理全选与反选
            if (grid.getStore().getTotalCount() > sm.getCount()) {
                sm.suspendEvents();
                sm.selectAll();
                sm.resumeEvents();
				Global.isSelectAll = true;
            }
            else {
                sm.clearSelections();
				Global.isSelectAll = false;
            }
            
            //列头的选择状态处理
            cm.config[colid].onHdMouseDown(e, e.getTarget());
            
            return true;
        },
        
		/**
		 * 取得表格中的最后选中的行记录对象
		 * @param {grid} Ext.grid object
		 * @return {record}
		 */
		getSelectedRecord:function(grid){
			var sm = grid.getSelectionModel();
			return sm.getSelected();
		},
		/**
		 * 取得表格中的最后选中的行记录对象
		 * @param {grid} Ext.grid object
		 * @return {record}
		 */
		getSelectedRecords:function(grid){
			var sm = grid.getSelectionModel();
			return sm.getSelections();
		}
    };
}
();

/**
 * DWR工具函数
 */
Ext.icss.Util.DWR = function(){
    return {
    
       /**
        * 设置DWR调用是否使用同步，或异步
        * @param isSyn false 同步 true 异步
        * @note 此函数最好成对出现
        */
        setAsync: function(isSyn){
           if(DWREngine)
           {
           		DWREngine.setAsync(isSyn);
           }
        },
        /**
         * 安装DWR错误处理方法
         * @param errorHandler DWR错误处理函数其声明方式为: function error(msg,e)
         */
        setErrorHandler: function(errorHandler){
        	DWREngine.setErrorHandler(errorHandler);
        }
    };
}
();
/**
 * 日期相关的工具函数
 */
Ext.icss.Util.Date = function(){
	return {
		/**
		 * 取得指定控件的时间对象
		 * @param {} datepicker date控件
		 * @param {} today 当控件值无效时，是否返回当前日期 true or false
		 * @return {Date}
		 */
		getDate:function(datepicker,today){
			
			var dt = null;
			if(datepicker)	{
				dt = datepicker.getValue();
			}
			
			if(!dt && (true == today))	{
				dt = new Date();
			} 
			return dt;
		},
		/**
		 * 取得指定控件的时间,并按要求格式化
		 * @param {} datepicker date控件
		 * @param fmt 满足Date要求的格式化字串
		 * @return {yyyy-mm-dd} string
		 */
		getFormatDate:function(datepicker,fmt){
			if(!fmt){
				fmt = 'Y-m-d';
			}
			var querydate = null;
			if(datepicker)
			{
				querydate = datepicker.getValue();
			}
				
			if(querydate)
			{
				return querydate.format(fmt);
			}
			else
			{
				return (new Date()).format(fmt);
			}
		},		
		/**
		 * 返回指定格式的当前日期
		 * @param fmt 满足Date要求的格式化字串
		 * @return date string
		 */
		getCurrentDate:function(fmt){
			if(fmt){
				return (new Date()).format(fmt);
			}else{
				return new Date();
			}
		},
		/**
        * 比较两个日期的大小
        * @param d1 date1
        * @param d2 date 2
        * @param time true 带时间比较 ,false 不带时间比较
        * @return 0: d1 == d2, >0: d1 > d2 , <0: d1<d2, -2:error 
        */
        compare: function(d1,d2,time){
            if(!Ext.isDate(d1) || !Ext.isDate(d2)){
            	return -2;
            }            
            var n = 0;
            if(true == time){
            	n = (d1 - d2);
            }else{
            	n = (d1.clearTime() - d2.clearTime());
            }
			return n;
            
        },
        /**
         * 检查日期d是否在[lhd .. rhd]区间内
         * @param d date 
         * @param lhd left handle date
         * @param rhd right handle date
         * @return true or false
         */
        between:function(d,lhd,rhd){
        	return d.between(lhd,rhd);
        },
        /**
         * 检查d2是否在[d1 .. (d1 + days)]区间内
         * @param d1 date 1
         * @param d2 date 2
         * @param days 间隔天数
         * @return true or false
         */
        verifyPeriod:function(d1,d2,days){
        	var td1 = d1.add(Date.DAY,days);
        	return d2.between(d1,td1);
        },
        /**
         * 检查date是否跨越半年执行
         * @param d date
         * @return true 没跨越 false 跨越
         */
        verifyHalfYear:function(d){
        	var sysdate = Global.getSystemDate();
        	var sysm = sysdate.getMonth();
        	var dm = d.getMonth();
        	var sy = sysdate.getYear();
        	var dy = d.getYear();
        	if(sysm < 6)
        	{//跨半年检查
        		return (sy == dy && (dm < 6 ));
        	}
        	else
        	{//跨年检查
        		return (sy == dy && (dm > 5 && (dm < 12)));
        	}
        },
         /**
         * 根据计划所属年月检查date是否跨越半年执行
         * @param d date
         * @return true 没跨越 false 跨越
         */
        verifyHalfYearBaseYear:function(d,baseDate){
        	var sysdate = baseDate;
        	var sysm = sysdate.getMonth();
        	var dm = d.getMonth();
        	var sy = sysdate.getYear();
        	var dy = d.getYear();
        	if(sysm < 6)
        	{//跨半年检查
        		return (sy == dy && (dm < 6 ));
        	}
        	else
        	{//跨年检查
        		return (sy == dy && (dm > 5 && (dm < 12)));
        	}
        },

        /**
         * 根据日期的月份，返回上下半年的标识
         * @param date 日期
         * @return 1 上半年 2 下半年
         */
        getHalfYear:function(date){
        	var m = date.format('n');
        	return (m < 7) ? 1 : 2;   
        },
        /**
         * 计算两个日期之间的天数
         * @param sdt 开始日期
         * @param edt 结束日期
         * @return number 
         */
        getDayInterval:function(sdt,edt){
        	var days = Math.floor((edt - sdt)/(24*60*60*1000));
        	return days;
        },
        /**
         * 根据传入的天数和初始日期，获取递增的日期和星期
         * @param sdt 开始日期
         * @param days 天数
         * @return {}, weekinfo.date:日期，weekinfo.weekIndex：星期，weekinfo.weekCN：星期中文
         */
        getWeekInterval:function(sdt,days){
        	var weeks = [];        	
        	var weekCN = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'];
        	for(var i=0; i<days; ++i)
        	{
        		var weekinfo = {};
        		var date = new Date(sdt.add(Date.DAY,i));
        		weekinfo.date = date;
        		weekinfo.weekIndex = date.getDay();
        		weekinfo.weekCN = weekCN[weekinfo.weekIndex];
        		weeks[weeks.length] = weekinfo;
        	}        	
        	return weeks;
        },
        addDays:function(d,days)
        {
        	//var dtmp = new Date(d);
        	return d.add(Date.DAY,days);
        }
	};
}
();

/**
 * 计量单位转换
 */
Ext.icss.Util.ConvertUnit = function(){
    return {
    
        /**
         * 卷烟基本单位转换，从万支->箱
         * 万支/5 = 箱
         * 卷烟基本单位：万支
         */
        BaseToBox: function(v){
            return v / 5.0;
        },
        /**
         * 卷烟基本单位转换，从箱->万支
         * 万支 = 箱 * 5
         * 卷烟基本单位：万支
         */
        BoxToBase: function(v){
            return v * 5.0;
        }
    };
}
();

/**
 * 定义别名
 * @type 
 */
Util = Ext.icss.Util;

/**
*将传入的日期型转换为字符串
**
*/
function date2string(d)
{if(d){var s_date = d.getFullYear()+'-'+(d.getMonth()+1)+'-'+d.getDate();
	return s_date;}
	else return d;
	
}
/*
**处理保留小数点位数的JS函数
**
*/
function tofloat(f,dec)   
  {     
        if(dec<0)   return   "Error:dec<0!";     
        result   =   parseInt(f)+(dec==0?"":".");     
        f  -= parseInt(f);     
        if(f==0)     
            for(var   i=0;   i<dec;   i++)   result   +=   '0';     
        else   
        {     
            for(var   i=0;   i<dec;   i++)   f   *=   10;     
            result   +=   parseInt(Math.round(f));     
        }     
        return   result;     
  }   
  
/*
**取前后五年的年度数组
**
*/
function getYearArray(){
	
	var rearray = new Array();
	var now = new Date();
	var year = now.getFullYear();
	for(i=4;i > 0;i--){
		var farray = new Array(2);
		farray[0]=year-i;
		farray[1]=year-i;
		rearray[rearray.length]=farray;
	}
	var yarray = new Array(2);
	yarray[0]=year;
	yarray[1]=year;
	rearray[rearray.length]=yarray;
	for(i=1;i<5;i++){
		var barray = new Array(2);
		barray[0]=year+i;
		barray[1]=year+i;
		rearray[rearray.length]=barray;
	}		
	return rearray;
} 
/*
**取月份的数组
**
*/
function getMonthArray(){
	var monthArray = [
        ['1', '1'],
        ['2', '2'],
        ['3', '3'],
        ['4', '4'],
        ['5', '5'],
        ['6', '6'],
        ['7', '7'],
        ['8', '8'],
        ['9', '9'],
        ['10', '10'],
        ['11', '11'],
        ['12', '12']
        ]; 
    return monthArray;
}
	
	//用来记录选中的记录，最终提交的就是此数组
	var selection = [];
	
	//判断所选的数据是否在数组中
	function inArray(value){
		for(var i = 0; i < selection.length; i++) {
			if(selection[i] == value) return true;
		}
		return false;
	}
	//向数组中增加数据
	function addValue(value){
		selection[selection.length] = value;
	}
	
	//从数组中删除数据
	function removeValue(value){
		var focus = new Array();
		for(var i = 0; i < selection.length; i++) {
			if(selection[i] == value) {
				focus.push(i);
			}
		}
		if(focus.length > 0) {
			for(var k = focus.length-1; k >= 0; k--) selection.splice(focus[k], 1);
		}
	}
	
	//全选
	function selectAll(ds){
	   selection = [];//如果是全选，那么填充之前要先清空数组
	   for(var i=0;i<ds.getTotalCount();i++){
			selection[selection.length] = ds.getAt(i).get('replycount');
	   }
	}
	
	//监听checkBox的选择事件
	function listenInCheck(value,w){
		if(w.checked){
			addValue(value);
		}else{
			removeValue(value);
		}
	}

/**
 * 格式化输出工具类
 */
Ext.icss.Util.Format = function(){
    return {
    	/*
    	* 格式化状态值
    	* @param formatStrategy 指定输出格式 'excel'或null
    	* @param v 须格式化的值 0..2或空
    	*/
        rendererBaseState: function(v,formatStrategy){
        	if(formatStrategy !=null && formatStrategy =='excel')
        		return 	Ext.icss.Util.Format.ExcelStrategy.rendererBaseState(v); 
        	else
        		return 	Ext.icss.Util.Format.ExtStrategy.rendererBaseState(v); 	       
		}
    };
}
();

/**
 * 适应于Excel输出的格式化策略类
 */
Ext.icss.Util.Format.ExcelStrategy = function(){
    return {
    	/*
    	* 格式化状态值
    	* @param v 须格式化的值 0..2或空
    	*/
        rendererBaseState: function(v){ 
       		if(v==1 || v==2) 
				return "是";
			else 
				return "";			 
        }
    };
}
(); 

/**
 * 适应于ext js输出的格式化策略类
 */
Ext.icss.Util.Format.ExtStrategy = function(){
    return {
    	/*
    	* 格式化状态值
    	* @param v 须格式化的值 0..2或空
    	*/
        rendererBaseState: function(v){
			if(v==1 || v==2)
				return "<span style='color:red;font-weight:900;font-size:130%';font-family:'Arial Black'>√</span>";
			else
				return "";						 
        },
        
        rendererState: function(v){
			if(v==1 || v==2)
				return "<span style='color:red;font-weight:900;font-size:130%';font-family:'Arial Black'>√</span>";
			else
				return "";						 
        }
    };
}
(); 
/**
 * 浏览器相关工具
 */
Ext.icss.Util.Browser = function(){
	var rslt = {};
	var ua = navigator.userAgent.toLowerCase();
	rslt.isOpera= ua.indexOf("opera")>-1; // 是Opera
	rslt.isIE7= !rslt.isOpera && ua.indexOf("msie 7")>-1; // IE7
	rslt.isIE8= !rslt.isOpera && ua.indexOf("msie 8")>-1; //IE8
	rslt.isIE6= !rslt.isOpera && !rslt.isIE7 && !rslt.isIE8 && ua.indexOf("msie")>-1; //IE6
	rslt.isFireFox= !rslt.isOpera && ua.indexOf("firefox")>-1 //FireFox
	ua = undefined;
	return rslt;
}
();



