/**
 * 
 * Microsoft Excel 应用，导出功能
 * @author 张尧伟 zhangyw@icss.com.cn
 */
Ext.namespace("Ext.icss");
Ext.icss.ExcelApp = function() {

	return {
		excelApp :  null,
		EXCEL_APP : "Excel.Application",
		/**
		 * 创建Excel应用程序对象
		 * @param visible{} true or false 表示主程序窗口对象是否可见
		 * @param displayAlerts{} true or false 如果不想在宏运行时被无穷无尽的提示和警告消息所困扰，
		 * 请将本属性设置为 False；这样每次出现需用户应答的消息时，Microsoft Excel 将选择默认应答。
		 * @return Application 对象
		 */
		createApp : function(visible, displayAlerts) {

			if (!this.excelApp) {
				this.excelApp = new ActiveXObject(this.EXCEL_APP);
			}
			if (this.excelApp) {
				this.excelApp.Visible = visible;
				this.excelApp.DisplayAlerts = displayAlerts;
			}

			return this.excelApp;
		},

		/**
		 * 关闭指定的应用程序对象
		 * @param exApp 需要关闭的EXCEL应用程序对象
		 */
		closeApp : function(exApp) {
			if (exApp) {
				exApp.Quit();
			}
		},

		/**
		 * 在指定的应用程序对象中，创建一个新的工作簿
		 * @param exApp EXCEL应用程序对象
		 * @return Workbook 对象
		 */
		addWorkbook : function(exApp) {
			if (!exApp) {
				return;
			}

			var workbook = exApp.Workbooks.Add();
			if (workbook) {
				workbook.Activate();
			}
			return workbook;
		},

		/**
		 * 在指定的workbook对象中，查找索引为index的工作表对象
		 * @param workbook Workbook 对象
		 * @param index 工作表的索引
		 * @return Worksheet 对象
		 */
		findWorksheet : function(workbook, index) {
			var worksheet = null;
			if (index) {
				worksheet = workbook.Worksheets(index);
			} else {
				worksheet = workbook.ActiveSheet;
			}

			return worksheet;
		},
		/**
		 * 设置指定的单元格值
		 * @param cell 单元格对象，取Worksheet.Cells属性 
		 * @param row cell的行
		 * @param col cell的列
		 * @param value 值
		 */
		setCellValue:function(cell,row,col,value){
			cell(row,col).Value = value;
		},

		/**
		 * 返回一个指定开始与结束表格的Range对象
		 * @param worksheet Worksheet对象
		 * @param startCell eg. cell(1,1)
		 * @param endCell eg. cell(10,1)
		 * @return Range对象
		 */
		getRange:function(worksheet,startCell,endCell){
			return worksheet.Range(startCell, endCell);
		},
		/**
		 * 设置区域边框线的样式
		 * @param range 目标区域
		 * @param style 要设置的样式,具体取值参考Ext.icss.Excel.XlBorderWeight
		 */
		setBordersLineStyle:function(range,style){
			range.Borders.LineStyle = style;
		},
		/**
		 * 设置区域range的字体样式
		 * @param range
		 * @param xlboolean 取值参考Ext.icss.Excel.XlBoolean
		 */
		setFontBold:function(range,xlboolean){
			range.Font.Bold = xlboolean;
		},
		/**
		 * 字体大小
		 */
		setFontSize:function(range,size)
		{
			range.Font.Size = size;
		},
		/**
		 * 设置单元格格式
		 */
		setNumberFormat:function(range,fmt){
			range.NumberFormat = fmt;
		},
		/**
		 * 自动调整区域列宽
		 */
		columnAutoFit:function(range){
			range.Columns.AutoFit();
		},
		
		printPreview : function() {
			var visible = this.excelApp.Visible;
			if (this.excelApp.ActiveSheet) {

				if (false == visible) {
					this.excelApp.Visible = true;
				}
				this.excelApp.ActiveSheet.PrintPreview();

				this.excelApp.Visible = visible;
			}
		},

		printOut : function() {
			if (this.excelApp.ActiveSheet) {

				this.excelApp.ActiveSheet.PrintOut();
			}

		},

		close : function() {
			this.closeApp(this.excelApp);
			this.excelApp = null;
		},
		
		getWorksheet:function(index){
			if(!this.excelApp){
				this.createApp(true,false);
			}
			
			var workbook = null;
			if(!this.excelApp.ActiveWorkbook){
				workbook = this.addWorkbook(this.excelApp);
			}
			return this.findWorksheet(workbook,index);
			
		}
	};
}();

ExcelApp = Ext.icss.ExcelApp;

